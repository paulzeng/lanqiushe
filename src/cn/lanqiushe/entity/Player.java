package cn.lanqiushe.entity;

import java.io.Serializable;

/**
 * 球员信息
 * 
 * @author lee
 * 
 */
public class Player implements Serializable{
	/**
	 * 队员id
	 */
	public String playerId;
	/**
	 * 队员头像
	 */
	public String playerProtrait;
	/**
	 * 该队员的名字
	 */
	public String nickName;
	/**
	 * 队员在球队中的位置
	 */
	public String playerPost;
	/**
	 * 球员性别
	 */
	public String sex;
	/**
	 * 该球员所属，球队的信息
	 */
	public Team team;
	/**
	 * 当前距离该球友的距离
	 */
	public String distance;
}
