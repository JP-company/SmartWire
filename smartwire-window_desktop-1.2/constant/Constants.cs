using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace smartwire_window_desktop
{
    public static class Constants
    {
        public static readonly string TOKEN_FILE_PATH = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "setting2.txt");
        public static readonly string MACHINEID_FILE_PATH = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "setting1.txt");
        public static readonly string API_URL = "https://smartwire-backend-f39394ac6218.herokuapp.com/";
        public static readonly string DIRECTORY_PATH = "C:\\spmEzCut\\LogMessage";
    }
}
