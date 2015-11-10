 
package cn.lanqiushe.entity;

import java.io.Serializable;

import cn.lanqiushe.manager.StringManager;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * 登录用户信息
 * @author lee
 *
 */

public class User implements Serializable{
	/**
	 * 用户id
	 */
  public String userId;
  /**
   * 用户名  /昵称
   */
  public String nickName;
  /**
   * 用户加密的密码
   */
  public String pwd;
  /**
   * QQ 号码登录的时候，QQ昵称
   */
  /**
   * openid	用于唯一标识用户身份（每一个openid与QQ号码对应）。
   */
   public String openid;
  /**
   * 用户头像
   */
  public String portrait;
  /**
   * 用户手机号码
   */
  public String phone;
  /**
   * 当前用户的性别
   */
  public String gender;
  /**
   * 在球队中的位置
   */
  public String post;
  /**
   * 当前用户的球队信息
   */
  public Team team;
  /**
   * 绑定的微博昵称
   */
   public String weiBoName;
   /**
    * 微博中Oauth2AccessToken 本身没有序列化。所以。将其的组成元素拿出来。单独存放
    */
   public String accessToken;
   public String expiresIn;
   public String uid;
   /**
    * 是否在登录界面显示用户登录痕迹
    */
   public boolean isShowFootprint(){
	  return this.nickName !=null;
   }
   /**
    * 是否第一次登录
    * nickname为null 说明为第一次登录
    * @return
    */
   public boolean isFirstLogin(){
	  return  this.userId==null;
   }
   /**
    * 用于splash界面，用于判断是否直接进入main界面
    */
   public boolean isEntryLogin(){
	   return this.userId==null;
   }
   
 /**
  * 是否绑定新浪微博
  */
 public boolean isBindWeibo(){
	 return accessToken!=null;
 }
 /**
  * 是否绑定手机
  */
 public boolean isBindPhone(){
	 return phone!=null;
 }
 
 
}
