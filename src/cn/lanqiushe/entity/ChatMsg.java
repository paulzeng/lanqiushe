package cn.lanqiushe.entity;

import java.io.Serializable;
/**
 * 通讯---》聊天模块    以及 通讯--》聊天模块---》聊天详情
 * @author lee
 *   聊天会分为两个人，你    我
 *
 *
 */
public class ChatMsg implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2692628421768718629L;
	/**
	 * 分类。 群聊还是单聊
	 */
	public static final int CHAT_SINGLE = 0;
	public static final int CHAT_GROUP =1;
	/**
	 * 该条消息发送者头像
	 */
	public String head;
	/**
	 * 该条消息发送者名字
	 */
	public String name;
	
 
	
	
	
	/**
	 * 聊天的内容   
	 */
	public String content;
	 
	/**
	 * 发送该条信息的时间
	 */
	public String time;
	/**
	 * 聊天类型 
	 * 0、单聊
	 * 1、群聊 
	 */
    public  int type;//默认单聊
	
	 /**
	  * 这条聊天信息是对法发的还是我自己的发的
	  */
    public boolean isFromOther = true;
 
 

}
