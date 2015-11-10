package cn.lanqiushe.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 球队
 * @author lee
 *
 */
public class Team implements Serializable {
	/**
	 * 球队的id
	 */
   public String teamId;
   /**
	 * 球队名字
	 */
   public String teamName;
   /**
	 * 球队logo
	 */
   public String teamLogo;
   /**
    * 该球队当队员数量
    */
   public String playerTotal;
   /**
    * 我距该球队的距离
    */
   public String distance;
   
   /**
	 * 该球队中的成员
	 */
   public ArrayList<Player> players;
}
