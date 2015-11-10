package cn.lanqiushe.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.lanqiushe.R;
import cn.lanqiushe.R.id;
public class BasePopupWindow extends PopupWindow {
	private TextView tv_title;
	private Button btn_del, btn_cancel;
	private View mPopView;

	public BasePopupWindow(Activity context, OnClickListener itemsOnClick) {
//		super(context);
//		LayoutInflater inflater = (LayoutInflater) context
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		mPopView = inflater.inflate(R.layout.layout_pop_del, null);
//		btn_del = (Button) mPopView.findViewById(R.id.btn_del);
//		btn_cancel.setOnClickListener(itemsOnClick);
//		btn_del.setOnClickListener(itemsOnClick);
//		// ����SelectPicPopupWindow��View
//		this.setContentView(mPopView);
//		// ����SelectPicPopupWindow��������Ŀ�
//		this.setWidth(LayoutParams.MATCH_PARENT);
//		// ����SelectPicPopupWindow��������ĸ�
//		this.setHeight(LayoutParams.MATCH_PARENT);
//		// ����SelectPicPopupWindow��������ɵ��
//		this.setFocusable(true);
//		// ����SelectPicPopupWindow�������嶯��Ч��
//		// this.setAnimationStyle(R.style.AnimBottom);
//		// ʵ����һ��ColorDrawable��ɫΪ��͸��
//		ColorDrawable dw = new ColorDrawable(0xb0000000);
//		// ����SelectPicPopupWindow��������ı���
//		this.setBackgroundDrawable(dw);
//		// mMenuView���OnTouchListener�����жϻ�ȡ����λ�������ѡ������������ٵ�����
//		mPopView.setOnTouchListener(new OnTouchListener() {
//
//			public boolean onTouch(View v, MotionEvent event) {
//
//				int height = mPopView.findViewById(R.id.ll_popwindow).getTop();
//				int y = (int) event.getY();
//				if (event.getAction() == MotionEvent.ACTION_UP) {
//					if (y < height) {
//						dismiss();
//					}
//				}
//				return true;
//			}
//		});
	}
	public void setTitle(String title){
		tv_title.setText(title);
	}
}
