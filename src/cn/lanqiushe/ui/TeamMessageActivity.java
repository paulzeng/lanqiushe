package cn.lanqiushe.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import cn.lanqiushe.jiansuo.CharacterParser;
import cn.lanqiushe.jiansuo.PinyinComparator;
import cn.lanqiushe.jiansuo.SideBar;
import cn.lanqiushe.jiansuo.SortAdapter;
import cn.lanqiushe.jiansuo.SortModel;
import cn.lanqiushe.jiansuo.SideBar.OnTouchingLetterChangedListener;
import cn.lanqiushe.manager.ToastManager;

import cn.lanqiushe.R;
 
public class TeamMessageActivity extends BaseActivity implements
		OnItemClickListener {
	private ListView contacts_ballfriend_listview;  
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter mAdapter;

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_my_team);
		initView();
		initViews();
	}

	void initView() { 
		contacts_ballfriend_listview = (ListView) findViewById(R.id.contacts_ballfriend); 
		contacts_ballfriend_listview.setOnItemClickListener(this);
	}

	void initViews() {
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = mAdapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					contacts_ballfriend_listview.setSelection(position);
				}

			}
		});
 
		contacts_ballfriend_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				Toast.makeText(getApplication(),
						((SortModel) mAdapter.getItem(position)).getName(),
						Toast.LENGTH_SHORT).show();
			}
		});

		SourceDateList = filledData(getResources().getStringArray(R.array.date));

		// 根据a-z进行排序源数据
		Collections.sort(SourceDateList, pinyinComparator);
		mAdapter = new SortAdapter(this, SourceDateList);
		contacts_ballfriend_listview.setAdapter(mAdapter);
	}

	/**
	 * 为ListView填充数据
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String [] date){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<date.length; i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			//汉字转换成拼音
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// 正则表达式，判断首字母是否是英文字母
			if(sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}else{
				sortModel.setSortLetters("#");
			}
			
			mSortList.add(sortModel);
		}
		return mSortList;
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ListView listView = (ListView) parent;
		@SuppressWarnings("unchecked")
		SortModel mSortModel = (SortModel) listView
				.getItemAtPosition(position);
		ToastManager.show(TeamMessageActivity.this,
				mSortModel.getName());
	}
}
