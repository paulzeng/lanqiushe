package cn.lanqiushe.ui;

import java.util.ArrayList;

import cn.lanqiushe.R;
import cn.lanqiushe.adapter.ParkInfoAdapter;
import cn.lanqiushe.adapter.ParkInfoPhotosAdapter;
import cn.lanqiushe.entity.Park;
import cn.lanqiushe.manager.LogManager;
import cn.lanqiushe.manager.TitleManager;
import cn.lanqiushe.view.CirclePageIndicator;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

public class NearbyParkInfoActivity extends BaseActivity {

	private ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.layout_nearby_park_info);
		TitleManager.showTitle(this, null, R.string.title_detailed_info,
				R.string.park, 0);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void init() {
		lv = (ListView) findViewById(R.id.nearby_park_info_lv);
		Park park = (Park) getIntent().getSerializableExtra("park");
		initHeader(park);
		ParkInfoAdapter adapter = new ParkInfoAdapter(this, park);
		lv.setAdapter(adapter);
		super.init();
	}

	private void initHeader(Park park ) {
		View headEr = View.inflate(this,
				R.layout.layout_nearby_park_info_header, null);
		lv.addHeaderView(headEr);
		ArrayList<View> vs = new ArrayList<View>();
		View layout0 = View.inflate(this, R.layout.item_nearby_park_info_vp,
				null);
		View layout1 = View.inflate(this, R.layout.item_nearby_park_info_vp,
				null);
		View layout2 = View.inflate(this, R.layout.item_nearby_park_info_vp,
				null);
		View layout3 = View.inflate(this, R.layout.item_nearby_park_info_vp,
				null);
		vs.add(layout0);
		vs.add(layout1);
		vs.add(layout2);
		vs.add(layout3);
		ViewPager mPager = (ViewPager) headEr.findViewById(R.id.park_info_header_vp);
		mPager.setAdapter(new ParkInfoPhotosAdapter(vs,park.urls));
		CirclePageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.park_info_header_indicator);
		mIndicator.setViewPager(mPager);
	}

	 
}
