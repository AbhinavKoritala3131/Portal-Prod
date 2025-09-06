package org.example.security;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
public class SSNEncryptor {

 // TO ENCRYPT : user.setSsn(SSNEncryptor.encrypt(user.getSsn())); // Before saving to DB
    // TO DECRYPT : String decryptedSsn = SSNEncryptor.decrypt(user.getSsn()); // Only for authorized roles
 // TO Display : return "***-**-" + decryptedSsn.substring(5);
        private static final String ALGORITHM = "AES";
        private static final String SECRET = "MySuperSecretKey"; // 🔒 16 bytes

        private static SecretKey getSecretKey() {
            return new SecretKeySpec(SECRET.getBytes(), ALGORITHM);
        }

        public static String encrypt(String ssn) throws Exception {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
            byte[] encrypted = cipher.doFinal(ssn.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        }

        public static String decrypt(String encryptedSsn) throws Exception {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey());
            byte[] decoded = Base64.getDecoder().decode(encryptedSsn);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted);
        }


}
