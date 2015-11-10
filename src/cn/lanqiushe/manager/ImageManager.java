package cn.lanqiushe.manager;
import cn.lanqiushe.R;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class ImageManager {
	/**
	 * 获取图片加载
	 * 
	 * @return
	 */
	public static ImageLoader getInstance() {
		return ImageLoader.getInstance();
	}

	/**
	 * 用户头像配置
	 */

	private static DisplayImageOptions userHeadOptions;
	public static DisplayImageOptions getUserHeadOptions() {
		if (userHeadOptions == null) {
			userHeadOptions = new DisplayImageOptions.Builder()
			        .showImageForEmptyUri(R.drawable.default_user_head)
			        .showImageOnFail(R.drawable.default_user_head)
			        .showImageOnLoading(R.drawable.default_user_head)
					.cacheInMemory(true) //缓存内存
					.cacheOnDisc(true)//缓存文件
					.displayer(new RoundedBitmapDisplayer(100)) //头像全部是圆形
					.build();
		}
		return userHeadOptions;
	}
	/**
	 * 球队头像配置
	 */
	private static DisplayImageOptions teamLogoOptions;
	public static DisplayImageOptions getTeamLogoOptions() {
		if (teamLogoOptions == null) {
			teamLogoOptions = new DisplayImageOptions.Builder()
					.cacheInMemory(true) //缓存内存
					.cacheOnDisc(true)//缓存文件
					.displayer(new RoundedBitmapDisplayer(100)) //头像全部是圆形的
					.build();
		}
		return teamLogoOptions;
	}
	
	
	
	private static DisplayImageOptions norOptions;
	public static DisplayImageOptions getNorOptions() {
		if (norOptions == null) {
			norOptions = new DisplayImageOptions.Builder()
					.cacheInMemory(true) //缓存内存
					.cacheOnDisc(true)//缓存文件
					.displayer(new SimpleBitmapDisplayer()) 
					.build();
		}
		return norOptions;
	}
 

}
