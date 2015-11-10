 
package cn.lanqiushe.entity;

import java.io.Serializable;

/**
 * 手机通讯录中的联系人    以及微博联系人列表
 * @author lee
 *
 */
public class Contact implements Serializable {

	/**
	 * id  
	 */
  public String id;
  /**
   * 联系人名字
   */
  public String name;
  /**
   * 联系人电话号码 （仅仅手机联系人）
   */
  public String phone;
  /**
   * 练习人头像
   */
  public String head;
  /**
   * 和该联系人的关系状态  
   *  
   *  0，邀请状态           （表示当前用户和该联系人还没有任何关系； 显示：邀请）
   *  1，已经邀请           （表示当前用户主动联系了该人，但是该人还没有回应；显示：已邀请；） 
   *  2，添加                   （表示我已经邀请过该人了，该人现在回复我了。回复的内容为接收我的邀请。显示：添加）
   *  3.已经添加          （表示当我在2的状态下，点击按钮，确定添加，那么当前用户和该联系人最终建立了联系）
   */
  public int status;
  
}
