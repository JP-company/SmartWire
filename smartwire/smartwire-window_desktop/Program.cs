using System;
using System.Net.Http.Headers;
using System.Net.Http;
using System.Windows.Forms;


namespace smartwire_window_desktop
{
    internal static class Program
    {
        /// <summary>
        /// 해당 애플리케이션의 주 진입점입니다.
        /// </summary>
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(
                new Main(InitializeHttpClient(SingletonHttpClient.Instance, Constants.TOKEN_FILE_PATH))
                );
        }


        public static string InitializeHttpClient(HttpClient httpClient, string tokenFilePath)
        {
            string token = Encryption.LoadData(tokenFilePath);
            if (!string.IsNullOrEmpty(token))
            {
                httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", token);
                return token;
            }
            else
            {
                return null;
            }
        }
    }
}
