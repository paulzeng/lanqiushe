package cn.lanqiushe.entity;

import java.io.Serializable;

/**
 * ��Ա��Ϣ
 * 
 * @author lee
 * 
 */
public class Player implements Serializable{
	/**
	 * ��Աid
	 */
	public String playerId;
	/**
	 * ��Աͷ��
	 */
	public String playerProtrait;
	/**
	 * �ö�Ա������
	 */
	public String nickName;
	/**
	 * ��Ա������е�λ��
	 */
	public String playerPost;
	/**
	 * ��Ա�Ա�
	 */
	public String sex;
	/**
	 * ����Ա��������ӵ���Ϣ
	 */
	public Team team;
	/**
	 * ��ǰ��������ѵľ���
	 */
	public String distance;
}
