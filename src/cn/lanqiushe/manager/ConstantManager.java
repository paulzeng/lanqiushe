package cn.lanqiushe.manager;

public class ConstantManager {
	public static final String BASE_URL = "http//cua0702.oicp.net:88/blueball/";
	/**
	 * 登录成功后，保持的用户信息
	 */
	public static final String SP_USER_NAME = "username";
	public static final String SP_USER_PWD = "userpwd";

	/**
	 * 球队人员的最大人数
	 */
	public static final int MAX_PLAYERS = 3;

	/**
	 * 球队logo 存放file中的名字
	 */
	public static final String FILE_NAME_TEAM_LOGO = "team_logo";

	/**
	 * 图片上传有关的what
	 */
	public static final int RESULT_CODE = 1;
	public static final int DATA_NULL = 2;
	public static final int SHOW_PHOTO = 3;
	public static final int UP_PHOTO_SUCCES = 4;
	public static final int UN_PHOTO_TYPE = 5;
	public static final int UP_PHOTO_FAIL = 6;
	public static final int UP_PHOTO_LOADING = 7;
	public static final int GET_USER_INFO_SUCCESS = 8;
	public static final int GET_USER_INFO_FAIL = 9;
	/**
	 * 网络加载有关的what
	 * 如果同一个handle会处理多个请求结果。。会用1.2.3来表示。请求结果
	 * 
	 */
	public static final int FAIL_NO_NET = 1000;
	public static final int FAIL_SERVER_ERROR = 1001;
	public static final int FAIL_CODE_ERROR = 1002;
	
	
	public static final int LOADING_1 = 1003;
	public static final int SUCCESS_1 = 1004;

	
	
	public static final int LOADING_2 = 1005;
	public static final int SUCCESS_2 = 1006;
	 
	 
	
	
	public static final int LOADING_3 = 1007;
	public static final int SUCCESS_3 = 1008;
	 
 
	public static final int LOADING_4 = 1009;
	public static final int SUCCESS_4 = 1010;
 
	
	
	
	
	public static final int NOTIFYLIST = 1;
	public static final int HANDLENOTIFY = 2;
	public static final int CHATMSGLIST = 3;
	public static final int SIGNLCHATMSG = 4;
	public static final int SENDMSG = 5;
	public static final int TEAMER = 6;
	public static final int Server = 7;
	/**
	 * 授权登录的不同状态
	 */
	public static final int LOGIN_WEIBO = 2000;
	public static final int LOGIN_QQ = 2001;
	public static final int LOGIN_TYPE_MY = 1;
    public static final int LOGIN_TYPE_OTHER =  2;
    	
	
	
	
	/**
	 * 请求码
	 */
	public static final int REQUEST_CODE_BUILD_TEAM_FROM_ME = 3001;
	public static final int REQUEST_CODE_BUILD_TEAM_FROM_PLAYER_INFO = 3002;

	/**
	 * 广播相关
	 */
	public static final String ACTION_CHANGE_BOTTOM_TAB = "action_change_bottom_tab";
	public static final String ACTION_CHANGE_TOP_TAB_NEARBY = "action_change_top_tab";
	public static final String ACTION_CHANGE_TOP_TAB_CONTACT = "action_change_contact_top_tab";
	public static final String ACTION_REFRESH_ME = "RefreshUserInfoReceiver";
	public static final String BOTTOM_TAB_INDEX = "bottom_tab_index";
	public static final String TOP_TAB_INDEX = "top_tab_index";
	public static final String TOP_TAB_INDEX_CONTACT = "top_contact_tab_index";
	public static final int REFRESH_HEAD = 0;
	public static final int REFRESH_TEAM_INFO = 1;
	public static final int REFRESH_ALL = 2;

	/**
	 * intent传递数据
	 */
	public static final String INTENT_KEY_REFRESH = "REFRESH_ME";

	// -----------s端返回参数-------
	/**
	 * 性别
	 */
	public static final int gender_man = 0;
	public static final int gender_women = 1;
	/**
	 * 球员所在位置
	 */
	// public static final int gender_women = 1;
	// public static final int gender_women = 1;
	// public static final int gender_women = 1;
	// public static final int gender_women = 1;
	// public static final int gender_women = 1;

	/**
	 * 通知的不同状态（通讯->通知）
	 */
	public static final int NOTIFY_STATUS_UNTREATED = 1;// 未处理。界面显示同意按钮
	public static final int NOTIFY_STATUS_TREATED = 2;// 处理过了。界面显示已同意

	/**
	 * 处理通知接口不同参数代表，不同的操作
	 */
	public static final int NOTIFY_TREATEDTYPE_STATUS = 1;// 表示对通知状态的改变
	public static final int NOTIFY_TREATEDTYPE_DEL = 2;// 表示删除通知
	/**
	 * 通知類型
	 */
	public static final int NOTIFY_TYPE_PK = 1;
	public static final int NOTIFY_TYPE_BUILD = 2;
	public static final int NOTIFY_TYPE_APPLY = 3;
	public static final int NOTIFY_TYPE_ADD = 4;

}
