using System;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Windows.Forms;
using System.Collections.Generic;
using System.Text.Json;
using smartwire_window_desktop.dto;

namespace smartwire_window_desktop
{
    public partial class Main : System.Windows.Forms.Form
    {
        private List<MachineDto> machines = null;
        private JwtMemberDto member = null;
        private LogFileDetector _logFileDetector;
        private HttpClient httpClient = SingletonHttpClient.Instance;
        private AuthService authService;

        public Main() { }

        // 여기서 헤더에 토큰이 설정되어 넘어오면 서버에 검증 요청
        public Main(string jwtToken)
        {
            InitializeComponent();
            authService = new AuthService(httpClient);

            // HTTPS 보안 프로토콜 설정
            ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls | SecurityProtocolType.Tls11 | SecurityProtocolType.Tls12;

            // 여기서 jwt 토큰으로 다시 로그인을 위한 HTTP request를 보내야댐
            if (!string.IsNullOrEmpty(jwtToken)) {
                autoLogin(jwtToken);
            }
        }


        private async void autoLogin(string jwtToken) {
            using (HttpClient client = new HttpClient())
            {
                client.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", jwtToken);

                // 서버에 요청 보내기
                try 
                { 
                    HttpResponseMessage response = await client.GetAsync(Constants.API_URL + "api/auto_login");
                    if (response.IsSuccessStatusCode)
                    {
                        SaveMemberInfomation(response);
                    }
                } catch (HttpRequestException ex)
                {
                    Console.WriteLine(ex.ToString());
                    System.Threading.Thread.Sleep(1000);
                    autoLogin(jwtToken);
                }
            }
        }


        private async void btnLogin_Click(object sender, EventArgs e)
        {
            string userId = tbxId.Text;
            string userPassword = tbxPassword.Text;

            // 로그인 요청해서 HTTP response 를 받아옴
            try 
            {
                HttpResponseMessage response = await authService.LoginAsync(userId, userPassword);
                // 로그인 응답 처리
                // 성공 응답 코드 처리
                if (response.IsSuccessStatusCode)
                {
                    if (response.Headers.Contains("Authorization"))
                    {
                        MessageBox.Show("로그인 완료", "로그인");
                        // jwt 암호화해서 저장
                        string token = response.Headers.GetValues("Authorization").FirstOrDefault().Substring("Bearer ".Length).Trim();
                        Encryption.SaveData(token, Constants.TOKEN_FILE_PATH);

                        SaveMemberInfomation(response);
                        httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", token);
                    }
                    else
                    {
                        MessageBox.Show("서버 오류 입니다. 스마트 와이어 고객센터 010-8714-4246 에 문의주세요.", "서버 오류");
                    }
                }
                // 실패 응답 코드 처리
                else
                {
                    MessageBox.Show("로그인에 실패했습니다.", "로그인");
                }
            } catch (HttpRequestException ex)
            {
                Console.WriteLine(ex.ToString());
                MessageBox.Show("인터넷 연결을 확인해주세요.", "인터넷 연결 확인");
            }


            
        }

        private async void SaveMemberInfomation(HttpResponseMessage response)
        {
            MachineSelectView.VisibleChanged += MachineSelectView_VisibleChanged;
            MainView.VisibleChanged += MainView_VisibleChanged;

            string jsonContent = await response.Content.ReadAsStringAsync();

            var options = new JsonSerializerOptions
            {
                PropertyNameCaseInsensitive = true
            };

            //JSON 데이터를 C# 객체로 역직렬화
            var responseData = System.Text.Json.JsonSerializer.Deserialize<MemberMachineDataDto>(jsonContent, options);

            machines = responseData.MachineDtoList;
            member = responseData.JwtMemberDto;

            MachineSelectView.Visible = true;
        }


        private void MachineSelectView_VisibleChanged(object sender, EventArgs e)
        {
            if (MachineSelectView.Visible)
            {
                if (!string.IsNullOrEmpty(Encryption.LoadData(Constants.MACHINEID_FILE_PATH)))
                {
                    MainView.Visible = true;
                    return;
                }

                // ComboBox를 MachineSelectView 내에서 찾는다. comboBoxMachine이 ComboBox의 이름이라고 가정
                ComboBox comboBox = MachineSelectView.Controls.Find("LIstBoxMahcine", true).FirstOrDefault() as ComboBox;

                if (comboBox != null && machines != null)
                {
                    // 기존 항목을 지웁니다.
                    comboBox.Items.Clear();
                    foreach (var machine in machines)
                    {
                        // ComboBox에 Machine 객체의 machineName 프로퍼티를 사용하여 항목을 추가합니다.
                        comboBox.Items.Add(machine.MachineName);
                    }
                }
            }
        }

        private void BtnMahcineSave_Click(object sender, EventArgs e)
        {
            ComboBox comboBox = MachineSelectView.Controls.Find("LIstBoxMahcine", true).FirstOrDefault() as ComboBox;
            string machineName = comboBox.Text;

            if (string.IsNullOrEmpty(machineName))
            {
                return;
            }
            foreach (var machine in machines)
            {
                if (machineName == machine.MachineName)
                {
                    Encryption.SaveData(machine.Id.ToString(), Constants.MACHINEID_FILE_PATH);
                }
            }
            MainView.Visible = true;
        }

        private void MainView_VisibleChanged(object sender, EventArgs e)
        {
            if (MainView.Visible && _logFileDetector == null)
            {
                int machineId = int.Parse(Encryption.LoadData(Constants.MACHINEID_FILE_PATH));
                MachineDto machine = null;
                foreach (MachineDto machineDto in machines)
                {
                    if (machineDto.Id == machineId)
                    {
                        machine = machineDto;
                        break;
                    }
                }
                _logFileDetector = new LogFileDetector(machine);
            }
        }
    }
}