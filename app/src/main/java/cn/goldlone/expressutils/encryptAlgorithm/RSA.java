package cn.goldlone.expressutils.encryptAlgorithm;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {

	public static final String KEY_RSA = "RSA";

	/**
	 * RSA 使用公钥加密
	 * @param encryptingStr
	 * @param publicKeyStr
	 * @return
	 */
	public static String encryptByPublic(String encryptingStr, String publicKeyStr){
		try {
			// 将公钥由字符串转为UTF-8格式的字节数组
			byte[] publicKeyBytes = Base64Utils.decryptBase64(publicKeyStr);
			// 获得公钥
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
			// 取得待加密数据
	        byte[] data = encryptingStr.getBytes("UTF-8");
			KeyFactory factory;
			factory = KeyFactory.getInstance(KEY_RSA);
			PublicKey publicKey = factory.generatePublic(keySpec);
			// 对数据加密  
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // xxxxxxxxxxxxxxxx返回加密后由Base64编码的加密信息
			//反回加密后字符串
            return  Base64Utils.encryptBase64(cipher.doFinal(data));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * RSA 使用私钥解密
	 * @param encryptedStr
	 * @param privateKeyStr
	 * @return
	 */
	public static String decryptByPrivate(String encryptedStr, String privateKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        // 对私钥解密
        byte[] privateKeyBytes = Base64Utils.decryptBase64(privateKeyStr);
        // 获得私钥
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        // 获得待解密数据
        byte[] data = Base64Utils.decryptBase64(encryptedStr);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = factory.generatePrivate(keySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        // 返回UTF-8编码的解密信息
        return new String(cipher.doFinal(data), "UTF-8");
	}
}
