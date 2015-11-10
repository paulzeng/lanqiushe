 
package cn.lanqiushe.entity;

import java.io.Serializable;

import cn.lanqiushe.manager.StringManager;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * ��¼�û���Ϣ
 * @author lee
 *
 */

public class User implements Serializable{
	/**
	 * �û�id
	 */
  public String userId;
  /**
   * �û���  /�ǳ�
   */
  public String nickName;
  /**
   * �û����ܵ�����
   */
  public String pwd;
  /**
   * QQ �����¼��ʱ��QQ�ǳ�
   */
  /**
   * openid	����Ψһ��ʶ�û���ݣ�ÿһ��openid��QQ�����Ӧ����
   */
   public String openid;
  /**
   * �û�ͷ��
   */
  public String portrait;
  /**
   * �û��ֻ�����
   */
  public String phone;
  /**
   * ��ǰ�û����Ա�
   */
  public String gender;
  /**
   * ������е�λ��
   */
  public String post;
  /**
   * ��ǰ�û��������Ϣ
   */
  public Team team;
  /**
   * �󶨵�΢���ǳ�
   */
   public String weiBoName;
   /**
    * ΢����Oauth2AccessToken ����û�����л������ԡ���������Ԫ���ó������������
    */
   public String accessToken;
   public String expiresIn;
   public String uid;
   /**
    * �Ƿ��ڵ�¼������ʾ�û���¼�ۼ�
    */
   public boolean isShowFootprint(){
	  return this.nickName !=null;
   }
   /**
    * �Ƿ��һ�ε�¼
    * nicknameΪnull ˵��Ϊ��һ�ε�¼
    * @return
    */
   public boolean isFirstLogin(){
	  return  this.userId==null;
   }
   /**
    * ����splash���棬�����ж��Ƿ�ֱ�ӽ���main����
    */
   public boolean isEntryLogin(){
	   return this.userId==null;
   }
   
 /**
  * �Ƿ������΢��
  */
 public boolean isBindWeibo(){
	 return accessToken!=null;
 }
 /**
  * �Ƿ���ֻ�
  */
 public boolean isBindPhone(){
	 return phone!=null;
 }
 
 
}
