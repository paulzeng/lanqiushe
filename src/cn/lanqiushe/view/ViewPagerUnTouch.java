package cn.lanqiushe.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ViewPagerUnTouch extends ViewPager {

	public ViewPagerUnTouch(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public ViewPagerUnTouch(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
@Override
public boolean onTouchEvent(MotionEvent arg0) {
	// TODO Auto-generated method stub
	return  super.onTouchEvent(arg0);
}
}
