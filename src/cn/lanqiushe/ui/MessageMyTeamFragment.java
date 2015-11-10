package cn.lanqiushe.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import cn.lanqiushe.App;
import cn.lanqiushe.R;
import cn.lanqiushe.engine.DataService;
import cn.lanqiushe.entity.User;
import cn.lanqiushe.jiansuo.CharacterParser;
import cn.lanqiushe.jiansuo.PinyinComparator;
import cn.lanqiushe.jiansuo.SideBar;
import cn.lanqiushe.jiansuo.SortAdapter;
import cn.lanqiushe.jiansuo.SortModel;
import cn.lanqiushe.jiansuo.SideBar.OnTouchingLetterChangedListener;
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.LogManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.manager.UIManager;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MessageMyTeamFragment extends BaseFragment implements
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

	private Dialog loadDialog;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			UIManager.toggleDialog(loadDialog);
			switch (msg.what) {
			// ��ȡ����
			case ConstantManager.TEAMER:
				String teamResult = msg.obj.toString();
				LogManager.e(tag, "�ҵ������б�:"+teamResult);
				break;
			// ��ȡ�ͷ���Ϣ
			case ConstantManager.Server:
				String result = msg.obj.toString();
				LogManager.e(tag, "�ͷ�:"+result);
				break;
			case ConstantManager.FAIL_SERVER_ERROR:
			case ConstantManager.FAIL_NO_NET: {

				break;
			}
			case ConstantManager.LOADING_1: {
				loadDialog = UIManager.getLoadingDialog(fa);
				loadDialog.show();
				break;
			}
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogManager.e(tag, "NotifyMessageFragment  ��ʼ");
		return inflater.inflate(R.layout.contacts_my_team, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		App app = (App) fa.getApplication();
		User user = app.getUser();
		initNet(user.userId);
		initServer();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void findViews() {
		super.findViews();
	}

	/**
	 * ��ȡ������Ϣ
	 * 
	 * @param userId
	 */
	void initNet(String userId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		DataService.getMyGolfersItemInfo(ConstantManager.TEAMER, map, fa,
				handler);
	}

	void initServer() {
		DataService.getServicerInfo(ConstantManager.Server, null, fa, handler);
	}

	@Override
	public void init() {
		contacts_ballfriend_listview = (ListView) fa
				.findViewById(R.id.contacts_ballfriend);
		contacts_ballfriend_listview.setOnItemClickListener(this);
		// ʵ��������תƴ����
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) fa.findViewById(R.id.sidrbar);
		dialog = (TextView) fa.findViewById(R.id.dialog);
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

		contacts_ballfriend_listview
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// ����Ҫ����adapter.getItem(position)����ȡ��ǰposition����Ӧ�Ķ���
						Toast.makeText(
								fa,
								((SortModel) mAdapter.getItem(position))
										.getName(), Toast.LENGTH_SHORT).show();
					}
				});

		SourceDateList = filledData(getResources().getStringArray(R.array.date));

		// ����a-z��������Դ����
		Collections.sort(SourceDateList, pinyinComparator);
		mAdapter = new SortAdapter(fa, SourceDateList);
		contacts_ballfriend_listview.setAdapter(mAdapter);
		super.init();
	}

	/**
	 * ΪListView�������
	 * 
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String[] date) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < date.length; i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			// ����ת����ƴ��
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
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
		SortModel mSortModel = (SortModel) listView.getItemAtPosition(position);
		ToastManager.show(fa, mSortModel.getName());
	}

}
