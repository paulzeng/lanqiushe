package cn.lanqiushe.manager;

import java.util.HashMap;

import cn.lanqiushe.ui.ChangePhoneNumberActivity;
 
import cn.lanqiushe.ui.MeSetActivity;
import cn.lanqiushe.ui.MainActivity;
import cn.lanqiushe.ui.NearbyTeamInfoActivity;
import cn.lanqiushe.ui.GroupChatPlayerActivity;
import cn.lanqiushe.ui.RInputCellphoneActivity;
import cn.lanqiushe.App;
import cn.lanqiushe.R;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TitleManager {
	/**
	 * app title����,����ĳ�������setTiele�����е�flag��flag�������title����Ҫ��ʾ��iv
	 */

	//public static final int BACK_ARROW = 1;// ���أ���ͷ��
	public static final int TITLE_NAME = 2;// ����
	public static final int SET = 4; // me �����set
	public static final int BACK_ARROW_H = 5; // ����
	public static final int NEXT_ARROW_H = 6; // ��һ��
	public static final int CHANGE_PHONE = 7;// ��������
	public static final int GROUP = 8;//  Ⱥ�ĳ�Ա
	public static final int PLAYER_INFO = 9;// ��ǰ�����û�����Ϣ 
	/**
	 * 
	 * @param from
	 *            ��ǰ�Ľ���
	 * @param flags
	 *            title����Ҫ��ʾ��view
	 * @param title
	 *            title������Ҫ��ʾ���ı�
	 * @param left
	 *            title���Ҫ��ʾ���ı���Դ
	 * @param right
	 *            title�ұ���Ҫ��ʾ���ı���Դ
	 * @param to
	 *            ��Ҫ��������һ������
	 */
	public static void showTitle(final Activity from, int[] flags, Object title,
			int left, int right) {
 
		if(title instanceof Integer){
			int title2 = (Integer)title;
			if (title2 > 0) {
				TextView tv = (TextView) from.findViewById(R.id.title_title_tv);
				tv.setText(title2);
			}
		}
		if(title instanceof String){
			TextView tv = (TextView) from.findViewById(R.id.title_title_tv);
			tv.setText((String)title);
		}
		
		
		if (left > 0) {// ����������ı�����Linearlayoutһ��Ҫ��ʾ
			LinearLayout ll = (LinearLayout) from
					.findViewById(R.id.title_back_ll);
			ll.setVisibility(View.VISIBLE);
			ll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					from.finish();
				}
			});
			TextView tv = (TextView) from.findViewById(R.id.title_back_tv);
			tv.setText(left);
		}
		if (right > 0) {
			LinearLayout ll = (LinearLayout) from
					.findViewById(R.id.title_next_ll);
			ll.setVisibility(View.VISIBLE);
			TextView tv = (TextView) from.findViewById(R.id.title_next_tv);
			tv.setText(right);
		}
      
		if (flags != null) {// Ϊnull ��û��title bar
			for (int flag : flags) {
				switch (flag) {
				case GROUP: {
					ImageView iv = (ImageView) from
							.findViewById(R.id.title_group_iv);
					iv.setVisibility(View.VISIBLE);
	 

					break;
				}
				case PLAYER_INFO: {
					ImageView iv = (ImageView) from
							.findViewById(R.id.title_player_info_iv);
					iv.setVisibility(View.VISIBLE);
	 

					break;
				}
				case CHANGE_PHONE: {
					ImageView iv = (ImageView) from
							.findViewById(R.id.title_change_phone_iv);
					iv.setVisibility(View.VISIBLE);
					iv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							HashMap<String, Object> map = new HashMap<String, Object>();
 							UIManager.switcher(from, RInputCellphoneActivity.class);
							App app = (App) from.getApplication();
							app.exit(App.EXIT_APP);
						}
					});
					break;
				}

//				case BACK_ARROW: {
//					ImageView iv = (ImageView) from
//							.findViewById(R.id.title_back_iv);
//					iv.setVisibility(View.VISIBLE);
//					iv.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							from.finish();
//						}
//					});
//					break;
//				}
			 
				case SET: {
					ImageView iv = (ImageView) from
							.findViewById(R.id.title_set_iv);
					iv.setVisibility(View.VISIBLE);
					iv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							UIManager.switcher(from, MeSetActivity.class);
						}
					});
					break;

				}

				}
			}
		}else{
			ImageView iv = (ImageView) from
					.findViewById(R.id.title_set_iv);
			 
			if(iv.isShown()){
				iv.setVisibility(View.GONE);
			}
		}

	}

	 
	public static void showTitle(final Activity activity, int[] flags, int title) {
		showTitle(activity, flags, title, 0, 0);
	}

	
}
