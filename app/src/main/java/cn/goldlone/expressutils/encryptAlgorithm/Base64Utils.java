package cn.goldlone.expressutils.encryptAlgorithm;

import android.util.Base64;

/**
 * Created by CN on 2017/5/9.
 */

public class Base64Utils {
    /**
     * BASE64 解码
     * @param key 需要Base64解码的字符串
     * @return 字节数组
     */
    public static byte[] decryptBase64(String key) {
        return Base64.decode(key, 0);
    }


    /**
     * BASE64 编码
     * @param key 需要Base64编码的字节数组
     * @return 字符串
     */
    public static String encryptBase64(byte[] key) {
        return new String(Base64.encode(key, 0));
    }

}
