package cn.lanqiushe.manager;

import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.lanqiushe.R;
import cn.lanqiushe.entity.ChatMsg;
import cn.lanqiushe.entity.Park;
import cn.lanqiushe.entity.Player;
import cn.lanqiushe.entity.Team;
import cn.lanqiushe.entity.User;

public class UIManager {
	/**
	 * ɾ��popupwindow
	 */
	public static PopupWindow getDelPopupWindow(Context context){
		View view  = View.inflate(context, R.layout.layout_pop_del, null);
		final PopupWindow pw  = new PopupWindow(view,100, 100);
		
		 
		
		return pw;
	}
	
	
	
	/**
	 * ͨ�õ�dialog��ʾ��
	 * 
	 * @param context
	 * @param contentRes
	 *            ��ʾ����
	 * @param sureRes
	 *            ���bt����
	 * @param cancleRes
	 *            �ұ�bt����
	 * @param l
	 *            ����ұ�bt��Ķ�������
	 * @return
	 */
	public static Dialog getCommWarnDialog(Context context, int contentRes,
			int sureRes, int cancleRes, final OnClickListener l) {
		final Dialog dialog = new Dialog(context, R.style.float_base);
		View view = View.inflate(context, R.layout.layout_common_dialog, null);
		TextView content = (TextView) view.findViewById(R.id.warn_content_tv);
		TextView sure = (TextView) view.findViewById(R.id.warn_sure_bt);
		TextView cancle = (TextView) view.findViewById(R.id.warn_cancle_bt);
		content.setText(contentRes);
		sure.setText(sureRes);
		cancle.setText(cancleRes);
		dialog.setContentView(view);
		// �����Ի������� dismiss�Ի���
		dialog.setCanceledOnTouchOutside(true);
		view.findViewById(R.id.warn_sure_bt).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						l.onClick(v);
						dialog.dismiss();

					}
				});
		view.findViewById(R.id.warn_cancle_bt).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = getScreenWidth(context) - dpToPx(context, 50);
		window.setGravity(Gravity.CENTER_VERTICAL); // �˴���������dialog��ʾ��λ��

		return dialog;
	}

	/**
	 * ͨ�õ�dialog��ʾ��
	 * 
	 * @param context
	 * @param contentRes
	 *            ��ʾ����
	 * @param sureRes
	 *            ���bt����
	 * @param cancleRes
	 *            �ұ�bt����
	 * @param l
	 *            ����ұ�bt��Ķ�������
	 * @return
	 */
	public static Dialog getDownDialog(Context context, int contentRes,
			int sureRes, int cancleRes, final OnClickListener l) {
		final Dialog dialog = new Dialog(context, R.style.float_base);
		View view = View.inflate(context, R.layout.layout_common_dialog, null);
		TextView content = (TextView) view.findViewById(R.id.warn_content_tv);
		TextView sure = (TextView) view.findViewById(R.id.warn_sure_bt);
		TextView cancle = (TextView) view.findViewById(R.id.warn_cancle_bt);
		content.setText(contentRes);
		sure.setText(sureRes);
		cancle.setText(cancleRes);
		dialog.setContentView(view);
		// �����Ի������� dismiss�Ի���
		dialog.setCanceledOnTouchOutside(true);
		view.findViewById(R.id.warn_sure_bt).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						l.onClick(v);
						dialog.dismiss();

					}
				});
		view.findViewById(R.id.warn_cancle_bt).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = getScreenWidth(context) - dpToPx(context, 50);
		window.setGravity(Gravity.CENTER_VERTICAL); // �˴���������dialog��ʾ��λ��

		return dialog;
	}

	/**
	 * ͨ�õ�dialog��ʾ�� ����������������button�����֣�
	 * 
	 * @param context
	 * @param contentRes
	 * @param l
	 * @return
	 */

	public static Dialog getCommWarnDialog(Context context, int contentRes,
			final OnClickListener l) {

		return getCommWarnDialog(context, contentRes, R.string.dialog_sure,
				R.string.dialog_cancle, l);
	}

	/**
	 * ��Ҫ�Զ�����ߵ�button���֣��ұ�Ĭ��Ϊ��ȡ����
	 * 
	 * @param context
	 * @param contentRes
	 * @param sureRes
	 * @param l
	 * @return
	 */
	public static Dialog getCommWarnDialog(Context context, int contentRes,
			int sureRes, final OnClickListener l) {

		return getCommWarnDialog(context, contentRes, sureRes,
				R.string.dialog_cancle, l);
	}

	// ----------------------------------------����Ϊ��ʾ�Ի����----------------------------------------------------------------
	/**
	 * action sheet dialog
	 * 
	 * @param context
	 * @param view
	 * @return
	 */

	public static Dialog getActionSheet(Context context, View view) {
		final Dialog dialog = new Dialog(context, R.style.action_sheet);
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(true);
		view.findViewById(R.id.action_sheet_cancle_bt).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});

		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		int screenW = getScreenWidth(context);
		lp.width = screenW;
		window.setGravity(Gravity.BOTTOM); // �˴���������dialog��ʾ��λ��
		window.setWindowAnimations(R.style.action_sheet_animation); // ��Ӷ���
		return dialog;
	}

	// ----------------------------------------����Ϊaction sheet
	// �Ի���----------------------------------------------------------------
	/**
	 * �������ʱ�����ʾ�Ի���
	 * 
	 * @param context
	 * @param loadingTextRes
	 * @return
	 */
	public static Dialog getLoadingDialog(Context context) {

		return getLoadingDialog(context, R.string.dialog_loading);
	}

	public static Dialog getLoadingDialog(Context context, Object loadingTextRes) {
		final Dialog dialog = new Dialog(context, R.style.netLoadingDialog);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.custom_progress_dialog);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = getScreenWidth(context) - dpToPx(context, 100);
		window.setGravity(Gravity.CENTER_VERTICAL); // �˴���������dialog��ʾ��λ��
		TextView titleTxtv = (TextView) dialog.findViewById(R.id.dialog_tv);
		if(loadingTextRes instanceof String){
			titleTxtv.setText((String)loadingTextRes);
		}
		if(loadingTextRes instanceof Integer){
			titleTxtv.setText((Integer)loadingTextRes);
		}
		
		return dialog;
	}
    /**
     * �ر�dialog
     * @param loadDialog
     */
	public static void toggleDialog(Dialog loadDialog) {
		if (loadDialog != null && loadDialog.isShowing()) {
			loadDialog.dismiss();
		}

	}

	// ----------------------------------------����Ϊ��������ʵ�ʱ�����ʾ�Ի���--------------------

	/**
	 * sp���� dp װ��Ϊ px
	 */
	public static int dpToPx(Context context, int dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return Math.round(dpValue * scale);
	}

	/**
	 * ���߷���
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.widthPixels;
	}

	// ----------------------------------------����Ϊ�������϶Ի���Ĺ�����--------------------

	// ----------------------------------------����Ϊ�����л��Ĺ�����--------------------

	/**
	 * ͨ��startActivityForResult ����Ľ��棬��������
	 * 
	 * @param context
	 * @param resultCode
	 */
	public static void resultActivity(Context context, int resultCode) {
		resultActivity(context, resultCode, null);
	}

	public static void resultActivity(Context context, int resultCode,
			Map<String, Object> extras) {
		Intent i = new Intent();
		putExtras(extras, i);
		((Activity) context).setResult(resultCode, i);
		((Activity) context).finish();

	}

	/**
	 * startActivityForResult ���ʽ
	 * 
	 * @param from
	 * @param to
	 * @param requestCode
	 */
	public static void switcherFor(Context from, Class<?> to, int requestCode) {
		switcherFor(from, to, requestCode, null);
	}

	public static void switcherFor(Context from, Class<?> to, int requestCode,
			Map<String, Object> extras) {
		Intent i = new Intent(from, to);
		putExtras(extras, i);
		((Activity) from).startActivityForResult(i, requestCode);

	}

	/**
	 * startActivity ����ui
	 * 
	 * @param from
	 * @param to
	 */
	public static void switcher(Context from, Class<?> to) {
		switcher(from, to, null);
	}

	public static void switcher(Context from, Class<?> to,
			Map<String, Object> extras) {
		Intent i = new Intent(from, to);
		putExtras(extras, i);
		from.startActivity(i);
	}

	/**
	 * intent �� ��������
	 * 
	 * @param extras
	 * @param i
	 */
	private static void putExtras(Map<String, Object> extras, Intent i) {
		if (extras != null) {
			for (String name : extras.keySet()) {
				Object obj = extras.get(name);
				if (obj instanceof String) {
					i.putExtra(name, (String) obj);
				}
				if (obj instanceof Integer) {
					i.putExtra(name, (Integer) obj);
				}
				if (obj instanceof String[]) {
					i.putExtra(name, (String[]) obj);
				}
				if(obj instanceof Team){
					i.putExtra(name, (Team)obj);
				}
				if(obj instanceof Player){
					i.putExtra(name, (Player)obj);
				}
				if(obj instanceof Park){
					i.putExtra(name, (Park)obj);
				}
				if(obj instanceof User){
					i.putExtra(name, (User)obj);
				}
				if(obj instanceof ChatMsg){
					i.putExtra(name, (ChatMsg)obj);
				}
				 

			}
		}
	}
}
