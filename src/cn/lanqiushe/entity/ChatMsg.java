package cn.lanqiushe.entity;

import java.io.Serializable;
/**
 * ͨѶ---������ģ��    �Լ� ͨѶ--������ģ��---����������
 * @author lee
 *   ������Ϊ�����ˣ���    ��
 *
 *
 */
public class ChatMsg implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2692628421768718629L;
	/**
	 * ���ࡣ Ⱥ�Ļ��ǵ���
	 */
	public static final int CHAT_SINGLE = 0;
	public static final int CHAT_GROUP =1;
	/**
	 * ������Ϣ������ͷ��
	 */
	public String head;
	/**
	 * ������Ϣ����������
	 */
	public String name;
	
 
	
	
	
	/**
	 * ���������   
	 */
	public String content;
	 
	/**
	 * ���͸�����Ϣ��ʱ��
	 */
	public String time;
	/**
	 * �������� 
	 * 0������
	 * 1��Ⱥ�� 
	 */
    public  int type;//Ĭ�ϵ���
	
	 /**
	  * ����������Ϣ�ǶԷ����Ļ������Լ��ķ���
	  */
    public boolean isFromOther = true;
 
 

}
