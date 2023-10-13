using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;

namespace smartwire_window_desktop
{
    public class SingletonHttpClient
    {
        private static readonly Lazy<HttpClient> lazy = new Lazy<HttpClient>(() => new HttpClient());

        public static HttpClient Instance { get { return lazy.Value; } }

        private SingletonHttpClient()
        {
        }
    }
}