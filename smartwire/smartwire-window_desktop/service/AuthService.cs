using Newtonsoft.Json;
using System.Net.Http.Headers;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using System;

namespace smartwire_window_desktop
{
    internal class AuthService
    {
        private readonly HttpClient _httpClient;

        public AuthService(HttpClient httpClient)
        {
            _httpClient = httpClient;
            _httpClient.DefaultRequestHeaders.Accept.Clear();
            _httpClient.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
        }


        public async Task<HttpResponseMessage> LoginAsync(string userId, string userPassword)
        {
            var LoginDto = new LoginDto
            {
                loginId = userId,
                loginPassword = userPassword
            };

            // 아이디, 비번 객체를 json String 으로 변환, HTTP POST request 만들어서 URL로 보냄
            string jsonData = JsonConvert.SerializeObject(LoginDto);
            var content = new StringContent(jsonData, Encoding.UTF8, "application/json");

            return await _httpClient.PostAsync(Constants.API_URL + "api/login", content);
        }

        public async Task<HttpResponseMessage> UploadLog(int machineId, string log, string date, string logTime, string file)
        {
            var logDto = new LogDto
            {
                machineId = machineId,
                log = log,
                date = date,
                logTime = logTime,
                file = file,
                //thickness = thickness,
                //actualProcessTime = actualProcessTime
            };

            string jsonData = JsonConvert.SerializeObject(logDto);
            Console.WriteLine("jsonData={0}",jsonData);
            var content = new StringContent(jsonData, Encoding.UTF8, "application/json");

            return await _httpClient.PostAsync(Constants.API_URL + "api/log_test", content);
        }
    }
}
