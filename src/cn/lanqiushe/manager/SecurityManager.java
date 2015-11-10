package cn.lanqiushe.manager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityManager {

	/**
	 * �õ�һ��MD5�ļ����봮
	 * @param plainText ��ͨ���ı�
	 * @return ����MD5������Ĵ�.
	 * @throws java.security.NoSuchAlgorithmException
	 */
	public static String getMD5HashText(String plainText){
		return getHashText(plainText,"MD5");
	}


	/**
	 * �������ı�
	 * @param plainText ��ͨͨ�ı�.
	 * @param algorithm ����ʵ���㷨.
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
