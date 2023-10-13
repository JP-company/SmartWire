using smartwire_window_desktop.domain;
using System;
using System.Collections.Specialized;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace smartwire_window_desktop
{
    internal class LogFileDetector
    {
        private FileSystemWatcher folderWatcher;
        private FileSystemWatcher fileWatcher;

        private readonly HttpClient httpClient = SingletonHttpClient.Instance;
        private readonly AuthService authService;
        private readonly OrderedDictionary logMap = new LogMap().GetLogMapping();
        private readonly MachineDto machine;

        private string currentFilePath;
        private long lastReadPosition = 0;
        private string previousContent = ""; // 이전에 읽은 로그 내용 저장
        private string date = DateTime.Now.ToString("yyyy-MM-dd");
        private bool isFolderCreated = false;
        private bool _isStopped;

        public bool GetIsStopped() { return _isStopped; }
        public void SetIsStoppedFalse() { _isStopped = false; }

        public LogFileDetector(MachineDto machineDto)
        {
            authService = new AuthService(httpClient);
            // HTTPS 보안 프로토콜 설정
            ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls | SecurityProtocolType.Tls11 | SecurityProtocolType.Tls12;
            this.machine = machineDto;

            InitializeFolderWatcher(); // 폴더 감지기 초기화
            MonitorLatestFile();
        }

        private void InitializeFolderWatcher()
        {
            // folderWatcher 변수에 특정 폴더를 감지하는 객체 주입
            folderWatcher = new FileSystemWatcher(Constants.DIRECTORY_PATH)
            {
                NotifyFilter = NotifyFilters.FileName,
                Filter = "*.log",
            };
            folderWatcher.Created += FolderWatcher_Created;  // 폴더에서 뭔가가 만들어질때 이벤트 발생, FolderWatcher_Created() 메서드 실행
            folderWatcher.EnableRaisingEvents = true;
        }
        
        private async void FolderWatcher_Created(object sender, FileSystemEventArgs e)
        {
            if (e.ChangeType == WatcherChangeTypes.Created)
            {
                await Task.Delay(500); // 이벤트 발생 직후 파일 읽어오는데까지 텀을 둬서 정확한 로그정보 입수

                if (!isFolderCreated) // 이중호출 방지
                {
                    lastReadPosition = 0; // 새 파일이므로 위치 초기화
                    isFolderCreated = true;
                }
                MonitorLatestFile();
            }
        }
        
        private void MonitorLatestFile()
        {
            currentFilePath = Directory.GetFiles(Constants.DIRECTORY_PATH, "*.log")
                                        .OrderByDescending(f => new FileInfo(f).CreationTime)
                                        .FirstOrDefault();

            if (currentFilePath != null)
            {
                ReadFile(currentFilePath, true);
                InitializeFileWatcher(currentFilePath);
            }
        }

        private void InitializeFileWatcher(string filePath)
        {
            fileWatcher?.Dispose();
            fileWatcher = new FileSystemWatcher(Path.GetDirectoryName(filePath))
            {
                NotifyFilter = NotifyFilters.LastWrite,
                Filter = Path.GetFileName(filePath),
            };
            fileWatcher.Changed += (sender, e) => { 
                ReadFile(filePath, false);
                isFolderCreated = false;
            };
            fileWatcher.EnableRaisingEvents = true;
        }

        private void ReadFile(string filePath ,bool isFirst)
        {
            try
            {
                using (FileStream fs = new FileStream(filePath, FileMode.Open, FileAccess.Read, FileShare.ReadWrite))
                {
                    fs.Seek(lastReadPosition, SeekOrigin.Begin);

                    using (StreamReader reader = new StreamReader(fs, Encoding.Default))
                    {
                        string newLog = reader.ReadToEnd();

                        if (string.IsNullOrEmpty(newLog) || newLog == previousContent)
                        {
                            return; // 새로운 내용이 없거나 이전에 읽은 내용과 동일하면 리턴
                        }

                        // 추가된 로그(파일 내용)으로 서버에 정보 전송
                        AnalyzeAndUploadLog(newLog, isFirst);

                        lastReadPosition = fs.Position;
                        previousContent = newLog; // 새로 읽은 내용을 이전 내용으로 저장
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
        }

        private async void AnalyzeAndUploadLog(string newLog, bool isFirst)
        {
            if (isFirst)
            {
                string[] logs = newLog.Split('\n').Reverse().ToArray();
                
                foreach (string line in logs)
                {
                    if (line.Contains("Reset") || line.Contains("Nc File:"))
                    {
                        string logMessage = line.Contains("Reset") ? "stop_리셋" : "start_작업 시작";
                        string fileName = null;
                        string logDate = DateTime.Now.ToString("yyyy-MM-dd");
                        string logTime = DateTime.Now.ToString("HH:mm:ss");
                        string pattern = @"\d{4}-\d{2}-\d{2}  \d{2}:\d{2}:\d{2} [APap][Mm]";

                        // 날짜, 시간 정규표현식으로 가져오기
                        Match match = Regex.Match(line, pattern);
                        if (match.Success)
                        {
                            string dateTimeStr = match.Value;
                            string dateFromLog = dateTimeStr.Split(' ')[0];
                            if (!date.Equals(dateFromLog)) // 오늘 날짜와 같지 않아야 바꿈
                            {
                                logDate = dateFromLog;
                                date = dateFromLog;
                            }
                            logTime = dateTimeStr.Split(' ')[2];
                            string ampm = dateTimeStr.Split(' ')[3];
                            if (ampm.Equals("pm") && !logTime.StartsWith("12"))
                            {
                                DateTime time = DateTime.ParseExact(logTime, "HH:mm:ss", null);
                                time = time.AddHours(12);
                                logTime = time.ToString("HH:mm:ss");
                            }
                            else if (ampm.Equals("am") && logTime.StartsWith("12"))
                            {
                                DateTime time = DateTime.ParseExact(logTime, "HH:mm:ss", null);
                                time = time.AddHours(-12);
                                logTime = time.ToString("HH:mm:ss");
                            }
                        }

                        // 작업 시작일 때 파일 이름 추출
                        if (line.Contains("Nc File:"))
                        {
                            int startIndex = line.IndexOf("No.") + 8;
                            int endIndex = line.IndexOf(".NC", startIndex);

                            if (startIndex != -1 && endIndex != -1)
                            {
                                fileName = line.Substring(startIndex, endIndex - startIndex + 3);
                            }
                        }

                        try
                        {
                            HttpResponseMessage response = await authService.UploadLog(machine.Id, logMessage, logDate, logTime, fileName);
                            Console.WriteLine("Response HTTP Code ={0}", response.IsSuccessStatusCode);
                            string jsonContent = await response.Content.ReadAsStringAsync();
                            Console.WriteLine($"{jsonContent}");
                            break;
                        }
                        catch (HttpRequestException ex)
                        {
                            Console.WriteLine(ex.ToString());
                            await Task.Delay(1000);
                            AnalyzeAndUploadLog(newLog, isFirst);
                        }
                    }
                }

                foreach (string line in logs)
                {
                    string trimmedLine = line.Trim();

                    foreach (string key in logMap.Keys)
                    {
                        if (line.Contains(key))
                        {
                            Console.WriteLine(line);
                            string logMessage = (string)logMap[key];
                            string fileName = null;
                            string logDate = DateTime.Now.ToString("yyyy-MM-dd");
                            string logTime = DateTime.Now.ToString("HH:mm:ss");

                            // 작업 시작, 리셋은 중복호출이니까 패스
                            if (key.Equals("Nc File:") || key.Equals("Reset"))
                            {
                                return;
                            }

                            if (logMessage.StartsWith("stop"))
                            {
                                _isStopped = true;
                            }

                            // 로그 내용 response
                            try
                            {
                                HttpResponseMessage response = await authService.UploadLog(machine.Id, logMessage, logDate, logTime, fileName);
                                Console.WriteLine("Response HTTP Code ={0}", response.IsSuccessStatusCode);
                                string jsonContent = await response.Content.ReadAsStringAsync();
                                Console.WriteLine($"{jsonContent}");
                                return;
                            }
                            catch (HttpRequestException ex)
                            {
                                Console.WriteLine(ex.ToString());
                                await Task.Delay(1000);
                                AnalyzeAndUploadLog(newLog, isFirst);
                            }
                        }
                    }
                }
            }


            Console.WriteLine(newLog);
            foreach (string key in logMap.Keys)
            {
                if (newLog.Contains(key)) 
                {
                    string logMessage = (string)logMap[key];
                    string pattern = @"\d{4}-\d{2}-\d{2}  \d{2}:\d{2}:\d{2} [APap][Mm]";
                    string logDate = null;
                    string logTime = DateTime.Now.ToString("HH:mm:ss");
                    string fileName = null;

                    if (logMessage.StartsWith("stop"))
                    {
                        _isStopped = true;
                    }

                    // 날짜, 시간 정규표현식으로 가져오기
                    Match match = Regex.Match(newLog, pattern);
                    if (match.Success)
                    {
                        string dateTimeStr = match.Value;
                        string dateFromLog = dateTimeStr.Split(' ')[0];
                        if (!date.Equals(dateFromLog)) // 오늘 날짜와 같지 않아야 바꿈
                        {
                            logDate = dateFromLog;
                            date = dateFromLog;
                        }
                        logTime = dateTimeStr.Split(' ')[2];
                        string ampm = dateTimeStr.Split(' ')[3];
                        if (ampm.Equals("pm") && !logTime.StartsWith("12"))
                        {
                            DateTime time = DateTime.ParseExact(logTime, "HH:mm:ss", null);
                            time = time.AddHours(12);
                            logTime = time.ToString("HH:mm:ss");
                        } else if (ampm.Equals("am") && logTime.StartsWith("12"))
                        {
                            DateTime time = DateTime.ParseExact(logTime, "HH:mm:ss", null);
                            time = time.AddHours(-12);
                            logTime = time.ToString("HH:mm:ss");
                        }
                    }

                    // 작업 시작일 때 파일 이름 추출
                    if (key.Equals("Nc File:"))
                    {
                        int startIndex = newLog.IndexOf("No.") + 8;
                        int endIndex = newLog.IndexOf(".NC", startIndex);

                        if (startIndex != -1 && endIndex != -1)
                        {
                            fileName = newLog.Substring(startIndex, endIndex - startIndex + 3);
                        }
                    }

                    try
                    {
                        HttpResponseMessage response = await authService.UploadLog(machine.Id, logMessage, logDate, logTime, fileName);
                        Console.WriteLine("Response HTTP Code ={0}", response.IsSuccessStatusCode);
                        string jsonContent = await response.Content.ReadAsStringAsync();
                        Console.WriteLine($"{jsonContent}");
                        return;
                    }
                    catch (HttpRequestException ex)
                    {
                        Console.WriteLine(ex.ToString());
                        await Task.Delay(1000);
                        AnalyzeAndUploadLog(newLog, isFirst);
                    }
                    return;
                }
            }
        }
    }
}