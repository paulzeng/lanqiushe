package cn.lanqiushe.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.lanqiushe.App;
import cn.lanqiushe.R;
import cn.lanqiushe.adapter.ContactsAdapter;
import cn.lanqiushe.engine.DataService;
import cn.lanqiushe.entity.Contact;
import cn.lanqiushe.entity.User;
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.StringManager;
import cn.lanqiushe.manager.ThreadPool;
import cn.lanqiushe.manager.TitleManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.manager.UIManager;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.FriendshipsAPI;

/**
 * ΢�������б��ֻ�ͨѶ¼�б�
 * 
 * @author lee
 * 
 */
public class MBContactsActivity extends BaseActivity implements
		OnItemClickListener {

	private ListView mLV;
	private Dialog dialog;
	private int type;
	public static final String UNBIND = "unbind";
	public static final String UNBIND_TYPE = "unbind_type";
	public static final int UNBIND_TYPE_SINA = 0;
	public static final int UNBIND_TYPE_PHONE = 1;
	// ÿҳ����������
	private static final int PAGE_COUNT = 30;
	// weibo ��Ȩ

	// ��ȡ�û���api
	private FriendshipsAPI fApi;

	private ContactsAdapter adapter;
	private Dialog loadDialog;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			UIManager.toggleDialog(loadDialog);
			switch (msg.what) {
			case ConstantManager.SUCCESS_1:
				ArrayList<Contact> contactList = (ArrayList<Contact>) msg.obj;
				adapter = new ContactsAdapter(MBContactsActivity.this);
				adapter.setList(contactList);
				mLV.setAdapter(adapter);
				break;
			case ConstantManager.SUCCESS_2: {
				// �����
				Intent i = new Intent(MeSetActivity.ACTION);
				i.putExtra(UNBIND_TYPE, UNBIND_TYPE_SINA);
				sendBroadcast(i);
				dialog.cancel();
				finish();
				break;
			}
			case ConstantManager.SUCCESS_3: {
				Contact c = adapter.getList().get((Integer) msg.obj);
				c.status = msg.arg1;
				adapter.notifyDataSetChanged();
				break;
			}

			case ConstantManager.FAIL_CODE_ERROR: {
				ToastManager.show(MBContactsActivity.this, (String) msg.obj);
				break;
			}
			case ConstantManager.FAIL_SERVER_ERROR:
			case ConstantManager.FAIL_NO_NET: {
				ToastManager.show(MBContactsActivity.this,
						R.string.toast_un_net);
				break;
			}
			case ConstantManager.LOADING_1: {
				loadDialog = UIManager
						.getLoadingDialog(MBContactsActivity.this);
				loadDialog.show();
				break;
			}
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.layout_get_contact_list);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void init() {
		type = getIntent().getIntExtra(MeSetActivity.FROM, -1);
		mLV = (ListView) findViewById(R.id.get_contact_list_lv);
		mLV.setOnItemClickListener(this);

		// ����������˵��΢������˵�����ǰ󶨵��ˡ�����
		if (type == MeSetActivity.SINA) {// ΢����صİ�
			initWeibo();
		} else { // �ֻ���صİ�
			initPhone();
		}

		super.init();
	}

	private void initPhone() {
		TitleManager.showTitle(this, new int[] { TitleManager.CHANGE_PHONE },
				R.string.title_mbcontact, R.string.title_back, 0);
		ThreadPool.getInstance().addTask(new Runnable() {

			@Override
			public void run() {
				ContentResolver resolver = getContentResolver();
				Cursor c2 = null;
				Cursor c1 = resolver.query(
						ContactsContract.Contacts.CONTENT_URI, new String[] {
								ContactsContract.Contacts._ID,
								ContactsContract.Contacts.DISPLAY_NAME }, null,
						null, null);
				ArrayList<Contact> list = new ArrayList<Contact>();
				while (c1.moveToNext()) {
					Contact c = new Contact();
					c.id = c1.getString(c1
							.getColumnIndex(ContactsContract.Contacts._ID));
					c.name = c1.getString(c1
							.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					c2 = resolver.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ "=" + c.id, null, null);
					if (c2.moveToNext()) {
						c.phone = c2.getString(c2
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					}
					list.add(c);
				}
				if (c2 != null) {
					c2.close();
				}
				c1.close();
				HashMap<String, Object> map = new HashMap<String, Object>();
				// map.put("userId", ((App)getApplication()).getUser().userId);
				map.put("userId", 1);
				StringBuffer phones = new StringBuffer();
				for (Contact a : list) {
					phones.append(a.phone);
					phones.append("|");
				}
				// test
				map.put("phones", "13360526361|18926480702|15949508764");
				// map.put("phones", phones.toString());
				if (!StringManager.isEmpty(phones.toString())) {
					DataService.getPhoneContactsList(map,
							MBContactsActivity.this, handler);
				} else {
					handler.sendEmptyMessage(ConstantManager.FAIL_CODE_ERROR);
				}
			}
		});

		// QueryHandler queryHandler = new QueryHandler(getContentResolver());
		// queryHandler.startQuery(TOKEN, null,
		// ContactsContract.Contacts.CONTENT_URI, new String[] {
		// ContactsContract.Contacts._ID,
		// ContactsContract.Contacts.DISPLAY_NAME }, null, null,
		// null);
		// phoneAdapter = new ContactPhoneAdapter(this, null, true);
		// mLV.setAdapter(phoneAdapter);
	}

	private void initWeibo() {
		TitleManager.showTitle(this, null, R.string.title_add_friend,
				R.string.title_back, R.string.title_unbind);
		// ���΢������
		User user = ((App) getApplication()).getUser();
		Oauth2AccessToken accessToken = new Oauth2AccessToken(user.accessToken,
				user.expiresIn);
		fApi = new FriendshipsAPI(accessToken);
		// ��˿
		// fApi.followers(Long.parseLong(accessToken.getUid()),PAGE_COUNT, 0,
		// true, new FollowersListener());

		fApi.friends(Long.parseLong(((App) getApplication()).getUser().uid),
				PAGE_COUNT, 0, true, new FriendsListener());
	}

	class FriendsListener implements RequestListener {

		@Override
		public void onComplete(String arg0) {

			try {
				JSONObject obj = new JSONObject(arg0);
				// int next_cursor = obj.optInt("next_cursor");
				JSONArray array = obj.optJSONArray("users");
				int len = array.length();
				// if (next_cursor > 0) {// �f����ע���˲���pagecount
				// fApi.friends(Long.parseLong(accessToken.getUid()),
				// PAGE_COUNT, next_cursor, true, new FriendsListener());
				// }
				ArrayList<Contact> list = new ArrayList<Contact>();
				for (int i = 0; i < len; i++) {
					JSONObject json = array.optJSONObject(i);
					Contact c = new Contact();
					// c.head = json.optString("profile_image_url");
					c.name = json.optString("screen_name");
					c.status = R.string.status_tianjia;
					list.add(c);
				}
				Message msg = handler.obtainMessage(ConstantManager.SUCCESS_1,
						list);
				handler.sendMessage(msg);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// //�ҵĹ�ע��
			// fApi.friends(Long.parseLong(accessToken.getUid()), count, cursor,
			// trim_status, listener);
			//

			// ArrayList<Contact> list = new ArrayList<Contact>();
			// for(int i=0;i<100;i++){
			// Contact c = new Contact();
			// c.name = "����blue";
			// if(i%4==0){
			// c.status = R.string.status_tianjia;
			// }
			// if(i%4==1){
			// c.status =R.string.status_yaoqing;
			// }
			// if(i%4==2){
			// c.status = R.string.status_yitianjia;
			// }
			// if(i%4==3){
			// c.status = R.string.status_yiyaoqing;
			// }
			// list.add(c);
			// }
			// weiboAdapter = new ContactWeiBoAdapter(this, list);
			// mLV.setAdapter(weiboAdapter);
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub

		}

	}

	//
	// class FriendsListener implements RequestListener{
	//
	// @Override
	// public void onComplete(String arg0) {
	// LogManager.e("xx", "---FriendsListener-----"+arg0);
	//
	// }
	//
	// @Override
	// public void onWeiboException(WeiboException arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// }

	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.title_next_ll:
			// title ����΢�������
			dialog = UIManager.getCommWarnDialog(this,
					R.string.dialog_content_unbind, new OnClickListener() {
						@Override
						public void onClick(View v) {
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("bindId", "�󶨵�id");
							DataService.unBindWeibo(map,
									MBContactsActivity.this, handler);
						}
					});
			dialog.show();
			break;

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Contact c = adapter.getList().get(position);
		int status = c.status;
		// ���������Ӻ� ������ ��ô��û�е���¼�
		if (status == R.string.status_yitianjia
				|| status == R.string.status_yiyaoqing) {
			return;
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		// userId=1&golfersId=2
		App app = (App) getApplication();
 		//map.put("userId", app.getUser().userId);
 		//map.put("golfersId", c.id);
		
		
		//test
		map.put("userId", 1);
 		map.put("golfersId", 2);
		map.put("position", position);

		switch (status) {
		case R.string.status_tianjia:
			// �����������
			map.put("status", R.string.status_yitianjia);

			break;
		case R.string.status_yaoqing:
			// ������������
			map.put("status", R.string.status_yiyaoqing);

			break;
		}

		DataService.changeContactsSatus(map, this, handler);
	}

	/**
	 * �첽��ѯ�ֻ�ͨѶ¼�е���ϵ��
	 * 
	 * @author lee
	 * 
	 */
	// private final class QueryHandler extends AsyncQueryHandler {
	// public QueryHandler(ContentResolver cr) {
	// super(cr);
	// }
	//
	// @Override
	// protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
	// super.onQueryComplete(token, cookie, cursor);
	// phoneAdapter.changeCursor(cursor);
	//
	// }
	//
	// }

}
