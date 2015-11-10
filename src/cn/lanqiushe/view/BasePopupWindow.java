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
//		// 设置SelectPicPopupWindow的View
//		this.setContentView(mPopView);
//		// 设置SelectPicPopupWindow弹出窗体的宽
//		this.setWidth(LayoutParams.MATCH_PARENT);
//		// 设置SelectPicPopupWindow弹出窗体的高
//		this.setHeight(LayoutParams.MATCH_PARENT);
//		// 设置SelectPicPopupWindow弹出窗体可点击
//		this.setFocusable(true);
//		// 设置SelectPicPopupWindow弹出窗体动画效果
//		// this.setAnimationStyle(R.style.AnimBottom);
//		// 实例化一个ColorDrawable颜色为半透明
//		ColorDrawable dw = new ColorDrawable(0xb0000000);
//		// 设置SelectPicPopupWindow弹出窗体的背景
//		this.setBackgroundDrawable(dw);
//		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
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
