using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.IO;

namespace smartwire_window_desktop
{
    internal class Encryption
    {
        public static void SaveData(string data, string filePath)
        {
            try
            {
                // Convert the token to bytes
                byte[] dataBytes = Encoding.UTF8.GetBytes(data);

                // Encrypt the bytes
                byte[] encryptedBytes = ProtectedData.Protect(dataBytes, null, DataProtectionScope.CurrentUser);

                // Save the encrypted bytes to a file
                File.WriteAllBytes(filePath, encryptedBytes);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }

            
        }

        public static string LoadData(string filePath)
        {
            // Check if the file exists
            if (!File.Exists(filePath))
            {
                return null;
            }

            try 
            {
                // Read the encrypted bytes from the file
                byte[] encryptedBytes = File.ReadAllBytes(filePath);

                // Decrypt the bytes
                byte[] tokenBytes = ProtectedData.Unprotect(encryptedBytes, null, DataProtectionScope.CurrentUser);

                String result = Encoding.UTF8.GetString(tokenBytes);

                return result;
            } catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
            return null;
        }
    }
}
