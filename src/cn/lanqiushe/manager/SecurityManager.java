package cn.lanqiushe.manager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityManager {

	/**
	 * 得到一个MD5的加密码串
	 * @param plainText 普通的文本
	 * @return 经过MD5加密码的串.
	 * @throws java.security.NoSuchAlgorithmException
	 */
	public static String getMD5HashText(String plainText){
		return getHashText(plainText,"MD5");
	}


	/**
	 * 加密码文本
	 * @param plainText 普通通文本.
	 * @param algorithm 具体实现算法.
	 * @return
	 * @throws java.security.NoSuchAlgorithmException
	 */
	private static String getHashText(String plainText, String algorithm) {
		MessageDigest mdAlgorithm = null;
		try {
			mdAlgorithm = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		mdAlgorithm.update(plainText.getBytes());
		byte[] digest = mdAlgorithm.digest();
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < digest.length; i++) {
			plainText = Integer.toHexString(0xFF & digest[i]);
			if (plainText.length() < 2) {
				plainText = "0" + plainText;
			}
			hexString.append(plainText);
		}
		return hexString.toString();
	}
}
