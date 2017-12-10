package security;
import java.security.*;

/**
 * Created by BG on 12/3/16.
 */

public class SecurityManager{

        private MessageDigest messageSecure;

        public SecurityManager() throws NoSuchAlgorithmException {
            messageSecure = MessageDigest.getInstance("MD5");

        }

        public String securityEncrypt(String encryptPassword) throws NoSuchAlgorithmException {

            messageSecure.update(encryptPassword.getBytes());

            byte MessageByte[] = messageSecure.digest();

            StringBuffer buffmessage = new StringBuffer();
            for (int i = 0; i < MessageByte.length; i++) {
                buffmessage.append(Integer.toString((MessageByte[i] & 0xff) + 0x100, 16).substring(1));
            }
            return buffmessage.toString();
        }

        public String securityDecryption(String decryptPass){

            messageSecure.update(decryptPass.getBytes());
            byte MessageByte[] = messageSecure.digest();

            StringBuffer decString = new StringBuffer();
            for (int i=0;i<MessageByte.length;i++) {

                String hex=Integer.toHexString(0xff & MessageByte[i]);
                if(hex.length()==1) decString.append('0');
                decString.append(hex);
            }

            return decString.toString();
        }


    }

