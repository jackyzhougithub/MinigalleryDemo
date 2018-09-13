package com.fun.minigallery.download;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author jacky_zhou
 * @version 2018/9/12.
 */
public class MD5Tool {
    private static final char HEX_DIGITS[] =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 获取字符串对应的MD5
     *
     * @param password 字符串
     * @return 字符串对应的MD5
     */
    public static String getMD5(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();
            return toHexString(messageDigest).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将字节数组转换成String
     *
     * @param b 字节数组
     * @return 转换后的String
     */
    private static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);// 无符号右移运算符“>>>”是把操作数转换成二进制数向右移动指定的位数
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }
}
