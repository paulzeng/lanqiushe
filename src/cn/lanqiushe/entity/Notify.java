package cn.lanqiushe.entity;

import java.io.Serializable;

public class Notify implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3796396819451100712L;
	/**
	 * 该条通知的id
	 */
	public String notifyId;
	/**
	 * 发起这条通知的人的id
	 */
	public String notifierId;
	/**
	 * 通知发出人的头像
	 */
	public String notifierHead;
	/**
	 * 通知类型，（组队通知，约战通知等）
	 */
	public String notifyType;
	/**
	 * 通知内容（简单版）
	 */
	public String notifySimpleContent;
	/**
	 *  通知内容(详细)
	 */
	public String notifyDetailContent;
	/**
	 * 该通知当前状态，（同意，处理等）
	 */
	public String notifyStatus;
	/**
	 * 该通知发出的时间
	 */
	public String notifyTime;
	
	 
}
