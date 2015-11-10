package cn.lanqiushe.entity;

/**
 * ����˷��ص�״̬���
 * 
 * @author lee
 * 
 */

public final class ResultStatus {

	private ResultStatus() {
	}

	// -----------�ɹ�״̬��-----------------------
	/**
	 * �ɹ�
	 */
	public static final int SUCCESS = 1001;

	// -----------ʧ��״̬��-----------------------
	/**
	 * ����������
	 */
	public static final int LACK_PARAMETER = 2001;
	/**
	 * ���ݸ�ʽ����
	 */
	public static final int FORMAT_ERROR = 2002;
	/**
	 * δ��¼
	 */
	public static final int NOT_LOGIN = 2003;
	/**
	 * δ�ҵ���¼
	 */
	public static final int NOT_FOUND = 2004;
	/**
	 * ״̬������
	 */
	public static final int LOCK = 2005;
	/**
	 * �����ظ��ύ
	 */
	public static final int REPOST = 2006;
	/**
	 * δ��Ȩ
	 */
	public static final int UNAUTHORIZED = 2007;
	/**
	 * ǩ������
	 */
	public static final int SIGN_ERROR = 2008;

	// -----------��ȷ��״̬-------
	
	/**
	 * δ֪
	 */
	public static final int UNKNOW = 3001;
	/**
	 * �쳣
	 */
	public static final int EXCEPTION = 3002;

}
