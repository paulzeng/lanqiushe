package com.lanqiushe.auth;

import android.app.Activity;
import android.content.Context;
import cn.lanqiushe.App;
import cn.lanqiushe.entity.User;

import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

public class SinaWeiBoManager {
 
	 
	public static boolean isAuthorize(Context context){
		App app = (App) context.getApplicationContext();
		User user =  app.getUser();
		return user.accessToken!=null;
		
		 
	}
	/**
	 * 微博授权
	 * @param activity
	 */
	public static SsoHandler authorize(Activity activity,WeiboAuthListener listener) {
		// 创建微博实例
		WeiboAuth mWeiboAuth = new WeiboAuth(activity, Constants.APP_KEY,
				Constants.REDIRECT_URL, Constants.SCOPE);
		SsoHandler mSsoHandler = new SsoHandler(activity, mWeiboAuth);
		mSsoHandler.authorize(listener);
		return mSsoHandler;
	}
	
	 
}
