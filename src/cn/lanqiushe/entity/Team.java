package cn.lanqiushe.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ���
 * @author lee
 *
 */
public class Team implements Serializable {
	/**
	 * ��ӵ�id
	 */
   public String teamId;
   /**
	 * �������
	 */
   public String teamName;
   /**
	 * ���logo
	 */
   public String teamLogo;
   /**
    * ����ӵ���Ա����
    */
   public String playerTotal;
   /**
    * �Ҿ����ӵľ���
    */
   public String distance;
   
   /**
	 * ������еĳ�Ա
	 */
   public ArrayList<Player> players;
}
