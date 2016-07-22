package org.iplantc.de.commons.client.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author aramsey
 */
public class MD5Util {
    public static String hex(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i]
                                           & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
    }
    public static String md5Hex (String message) {
        try {
            MessageDigest md =
                    MessageDigest.getInstance("MD5");
            return hex (md.digest(message.getBytes()));
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }
}
