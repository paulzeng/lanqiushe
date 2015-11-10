package cn.lanqiushe.entity;

/**
 * 服务端返回的状态结果
 * 
 * @author lee
 * 
 */

public final class ResultStatus {

	private ResultStatus() {
	}

	// -----------成功状态码-----------------------
	/**
	 * 成功
	 */
	public static final int SUCCESS = 1001;

	// -----------失败状态码-----------------------
	/**
	 * 参数不完整
	 */
	public static final int LACK_PARAMETER = 2001;
	/**
	 * 数据格式错误
	 */
	public static final int FORMAT_ERROR = 2002;
	/**
	 * 未登录
	 */
	public static final int NOT_LOGIN = 2003;
	/**
	 * 未找到记录
	 */
	public static final int NOT_FOUND = 2004;
	/**
	 * 状态被锁定
	 */
	public static final int LOCK = 2005;
	/**
	 * 请求重复提交
	 */
	public static final int REPOST = 2006;
	/**
	 * 未授权
	 */
	public static final int UNAUTHORIZED = 2007;
	/**
	 * 签名错误
	 */
	public static final int SIGN_ERROR = 2008;

	// -----------不确定状态-------
	
	/**
	 * 未知
	 */
	public static final int UNKNOW = 3001;
	/**
	 * 异常
	 */
	public static final int EXCEPTION = 3002;

}
