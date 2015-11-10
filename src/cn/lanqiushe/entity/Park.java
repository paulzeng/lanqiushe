package cn.lanqiushe.entity;

import java.io.Serializable;

public class Park implements Serializable{
	/**
	 * id
	 */
	public String courtId;
	/**
	 * 球场logo
	 */
	public String logo;
	/**
	 * 球场名字
	 */
   public String name;
   /**
    * 球场价格
    */
   public String money;
   /**
    * 当前离球场距离
    */
   public String distance;
   /**
    * 球场图片
    */
   public String[] urls;
}
