using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;
using System.IO;

namespace VotingSystem
{
    public static class AES
    {
        private static byte[] secret = System.Text.Encoding.ASCII.GetBytes("rc*Ku2adLhFDS#K@ZV=pT2TUqkz&3V]}"); //_-66$pP6&ZP<mM_j&ZP<mM_j

        public static String encrypt(string plainText, byte[] key)
        {
            if (plainText == null || plainText.Length <= 0)
                throw new ArgumentNullException("plainText");
            if (key == null || key.Length <= 0)
                key = secret;
            System.Text.UTF8Encoding UTF8 = new System.Text.UTF8Encoding();
            AesManaged tdes = new AesManaged();
            tdes.BlockSize = 128;
            tdes.Key = key;
            tdes.Mode = CipherMode.ECB;
            tdes.Padding = PaddingMode.PKCS7;
            ICryptoTransform crypt = tdes.CreateEncryptor();
            byte[] plain = UTF8.GetBytes(plainText); 
            byte[] cipher = crypt.TransformFinalBlock(plain, 0, plain.Length);
            String encryptedText = Convert.ToBase64String(cipher);
            return encryptedText;
        }

        public static String decrypt(string cipherText, byte[] key)
        {
            if (cipherText == null || cipherText.Length <= 0)
                throw new ArgumentNullException("plainText");
            if (key == null || key.Length <= 0)
                key = secret;
            System.Text.UTF8Encoding UTF8 = new System.Text.UTF8Encoding();
            AesManaged tdes = new AesManaged();
            tdes.BlockSize = 128;
            tdes.Key = key;
            tdes.Mode = CipherMode.ECB;
            tdes.Padding = PaddingMode.PKCS7;
            ICryptoTransform crypt = tdes.CreateDecryptor();
            byte[] cipher = Convert.FromBase64String(cipherText); 
            byte[] plain = crypt.TransformFinalBlock(cipher, 0, cipher.Length);
            String decryptedText = UTF8.GetString(plain);
            return decryptedText;
        }

    }
}
