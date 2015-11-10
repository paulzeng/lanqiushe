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
	 * ����ת����ƴ������
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	/**
	 * ����ƴ��������ListView�����������
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
		// ʵ��������תƴ����
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		// �����Ҳഥ������
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// ����ĸ�״γ��ֵ�λ��
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
				// ����Ҫ����adapter.getItem(position)����ȡ��ǰposition����Ӧ�Ķ���
				Toast.makeText(getApplication(),
						((SortModel) mAdapter.getItem(position)).getName(),
						Toast.LENGTH_SHORT).show();
			}
		});

		SourceDateList = filledData(getResources().getStringArray(R.array.date));

		// ����a-z��������Դ����
		Collections.sort(SourceDateList, pinyinComparator);
		mAdapter = new SortAdapter(this, SourceDateList);
		contacts_ballfriend_listview.setAdapter(mAdapter);
	}

	/**
	 * ΪListView�������
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String [] date){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<date.length; i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			//����ת����ƴ��
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
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
