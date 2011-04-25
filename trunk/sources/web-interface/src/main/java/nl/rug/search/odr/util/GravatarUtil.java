package nl.rug.search.odr.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import nl.rug.search.odr.QueryStringBuilder;


/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public abstract class GravatarUtil {

    private static final String GRAVATAR_URL = "http://www.gravatar.com/avatar/%s.png";
    
    private static String hex(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i]
                    & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    private static String md5Hex(String message) {
        try {
            MessageDigest md =
                    MessageDigest.getInstance("MD5");
            return hex(md.digest(message.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static String getGravatar(String email, int size) {
        email = md5Hex(email);
        
        String url = String.format(GRAVATAR_URL, email);
        
        return new QueryStringBuilder()
                .setUrl(url)
                .append("d", "mm")
                .append("r", "g")
                .append("s", size)
                .toString();
    }
}
