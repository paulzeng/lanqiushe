package cn.lanqiushe.entity;

import java.io.Serializable;

public class Notify implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3796396819451100712L;
	/**
	 * ����֪ͨ��id
	 */
	public String notifyId;
	/**
	 * ��������֪ͨ���˵�id
	 */
	public String notifierId;
	/**
	 * ֪ͨ�����˵�ͷ��
	 */
	public String notifierHead;
	/**
	 * ֪ͨ���ͣ������֪ͨ��Լս֪ͨ�ȣ�
	 */
	public String notifyType;
	/**
	 * ֪ͨ���ݣ��򵥰棩
	 */
	public String notifySimpleContent;
	/**
	 *  ֪ͨ����(��ϸ)
	 */
	public String notifyDetailContent;
	/**
	 * ��֪ͨ��ǰ״̬����ͬ�⣬����ȣ�
	 */
	public String notifyStatus;
	/**
	 * ��֪ͨ������ʱ��
	 */
	public String notifyTime;
	
	 
}
