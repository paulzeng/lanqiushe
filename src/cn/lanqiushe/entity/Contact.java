 
package cn.lanqiushe.entity;

import java.io.Serializable;

/**
 * �ֻ�ͨѶ¼�е���ϵ��    �Լ�΢����ϵ���б�
 * @author lee
 *
 */
public class Contact implements Serializable {

	/**
	 * id  
	 */
  public String id;
  /**
   * ��ϵ������
   */
  public String name;
  /**
   * ��ϵ�˵绰���� �������ֻ���ϵ�ˣ�
   */
  public String phone;
  /**
   * ��ϰ��ͷ��
   */
  public String head;
  /**
   * �͸���ϵ�˵Ĺ�ϵ״̬  
   *  
   *  0������״̬           ����ʾ��ǰ�û��͸���ϵ�˻�û���κι�ϵ�� ��ʾ�����룩
   *  1���Ѿ�����           ����ʾ��ǰ�û�������ϵ�˸��ˣ����Ǹ��˻�û�л�Ӧ����ʾ�������룻�� 
   *  2�����                   ����ʾ���Ѿ�����������ˣ��������ڻظ����ˡ��ظ�������Ϊ�����ҵ����롣��ʾ����ӣ�
   *  3.�Ѿ����          ����ʾ������2��״̬�£������ť��ȷ����ӣ���ô��ǰ�û��͸���ϵ�����ս�������ϵ��
   */
  public int status;
  
}
