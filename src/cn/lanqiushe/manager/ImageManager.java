package cn.lanqiushe.manager;
import cn.lanqiushe.R;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class ImageManager {
	/**
	 * ��ȡͼƬ����
	 * 
	 * @return
	 */
	public static ImageLoader getInstance() {
		return ImageLoader.getInstance();
	}

	/**
	 * �û�ͷ������
	 */

	private static DisplayImageOptions userHeadOptions;
	public static DisplayImageOptions getUserHeadOptions() {
		if (userHeadOptions == null) {
			userHeadOptions = new DisplayImageOptions.Builder()
			        .showImageForEmptyUri(R.drawable.default_user_head)
			        .showImageOnFail(R.drawable.default_user_head)
			        .showImageOnLoading(R.drawable.default_user_head)
					.cacheInMemory(true) //�����ڴ�
					.cacheOnDisc(true)//�����ļ�
					.displayer(new RoundedBitmapDisplayer(100)) //ͷ��ȫ����Բ��
					.build();
		}
		return userHeadOptions;
	}
	/**
	 * ���ͷ������
	 */
	private static DisplayImageOptions teamLogoOptions;
	public static DisplayImageOptions getTeamLogoOptions() {
		if (teamLogoOptions == null) {
			teamLogoOptions = new DisplayImageOptions.Builder()
					.cacheInMemory(true) //�����ڴ�
					.cacheOnDisc(true)//�����ļ�
					.displayer(new RoundedBitmapDisplayer(100)) //ͷ��ȫ����Բ�ε�
					.build();
		}
		return teamLogoOptions;
	}
	
	
	
	private static DisplayImageOptions norOptions;
	public static DisplayImageOptions getNorOptions() {
		if (norOptions == null) {
			norOptions = new DisplayImageOptions.Builder()
					.cacheInMemory(true) //�����ڴ�
					.cacheOnDisc(true)//�����ļ�
					.displayer(new SimpleBitmapDisplayer()) 
					.build();
		}
		return norOptions;
	}
 

}
