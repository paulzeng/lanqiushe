//package cn.lanqiushe.ui;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.AdapterView;
//import android.widget.TextView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ListView;
//
//import cn.lanqiushe.adapter.MessageAdapter;
//import cn.lanqiushe.entity.Notify;
//import cn.lanqiushe.impl.ListItemClickHelp;
//import cn.lanqiushe.manager.DataManager;
//import cn.lanqiushe.manager.ToastManager;
//import cn.lanqiushe.view.BaseListView;
//import cn.lanqiushe.view.pullrefresh.PullToRefreshBase;
//import cn.lanqiushe.view.pullrefresh.PullToRefreshListView;
//import cn.lanqiushe.view.pullrefresh.PullToRefreshBase.OnRefreshListener;
//import cn.lanqiushe.R;
//
//public class NotifyMessageActivity extends BaseActivity implements ListItemClickHelp{
//	MessageAdapter adapter;
//	List<Notify> mData = new ArrayList<Notify>();
//	List<Notify> oldData = new ArrayList<Notify>();
//	List<Notify> newData = new ArrayList<Notify>();
//	private PullToRefreshListView mPullListView;
//	private ListView mListView;
//	private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
//	private boolean mIsStart = true;
//	private int mCurIndex = 0;
//	private TextView emptyView;
//
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.contacts_notify);
//		initView();
//	}
//
//	void initView() {
//		initNotifyMessage();
//	}
//
//	// 初始化通知消息
//	private void initNotifyMessage() {
////		// mData = DataManager.getNotifyMessages();
////		mPullListView = (PullToRefreshListView) findViewById(R.id.notify_messafe_listview);
////		mPullListView.setPullLoadEnabled(false);
////		mPullListView.setScrollLoadEnabled(true);
////		mCurIndex = mData.size();
////		mListView = mPullListView.getRefreshableView();
////		//adapter = new NotifyMessageAdapter(this, mData,this);
////		mListView.setAdapter(adapter);
////		// 设置数据为空时，显示的视图
////		setEmptyView();
////		// 添加点击
////		mListView.setOnItemClickListener(new OnItemClickListener() {
////
////			@Override
////			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
////					long arg3) {
////				// TODO Auto-generated method stub
////
////			}
////		});
////		mPullListView
////				.setOnRefreshListener(new OnRefreshListener<BaseListView>() {
////					@Override
////					public void onPullDownToRefresh(
////							PullToRefreshBase<BaseListView> refreshView) {
////						mIsStart = true;
////						new GetDataTask().execute();
////					}
////
////					@Override
////					public void onPullUpToRefresh(
////							PullToRefreshBase<BaseListView> refreshView) {
////						mIsStart = false;
////						new GetDataTask().execute();
////					}
////				});
////		setLastUpdateTime();
//	}
//
//	void setEmptyView() {
//		emptyView = new TextView(this);
//		emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
//				LayoutParams.FILL_PARENT));
//		emptyView.setText("暂无通知，下拉进行刷新");
//
//		emptyView.setGravity(Gravity.CENTER_HORIZONTAL
//				| Gravity.CENTER_VERTICAL);
//		emptyView.setVisibility(View.GONE);
//		emptyView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				mPullListView.doPullRefreshing(true, 500);
//			}
//		});
//		((ViewGroup) mListView.getParent()).addView(emptyView);
//		mListView.setEmptyView(emptyView);
//	}
//
//	// 刷新通知消息的线程
//	class GetDataTask extends AsyncTask<Void, Void, String[]> {
//
//		@Override
//		protected String[] doInBackground(Void... params) {
//			// Simulates a background job.
//			if (mIsStart) {
//				newData = DataManager.getNewNotifyMessages();
//			} else {
//				oldData = DataManager.getOldNotifyMessages();
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(String[] result) {
//			boolean hasMoreData = true;
//			if (mIsStart) {
//				// TODO增加新的数据
//				ToastManager.show(NotifyMessageActivity.this, "下拉刷新");
//				mPullListView.onPullDownRefreshComplete();
//				newData.addAll(mData);
//				mData = newData;
//				mCurIndex = mData.size();
//				//adapter = new NotifyMessageAdapter(NotifyMessageActivity.this,
//				//		mData,NotifyMessageActivity.this);
//				mListView.setAdapter(adapter);
//			} else {
//				int end = mCurIndex + mData.size();
//				if (end >= mData.size()) {
//					end = mData.size();
//					hasMoreData = false;
//				}
//				mData.addAll(oldData);
//				adapter.notifyDataSetChanged();
//				mCurIndex = end;
//				mPullListView.onPullUpRefreshComplete();
//				mPullListView.setHasMoreData(hasMoreData);
//			}
//
//			setLastUpdateTime();
//			super.onPostExecute(result);
//		}
//	}
//
//	private void setLastUpdateTime() {
//		String text = formatDateTime(System.currentTimeMillis());
//		mPullListView.setLastUpdatedLabel(text);
//	}
//
//	private String formatDateTime(long time) {
//		if (0 == time) {
//			return "";
//		}
//
//		return mDateFormat.format(new Date(time));
//	}
//
//	@Override
//	public void onClick(View item, View widget, int position, int which) {
//		// TODO Auto-generated method stub
//		
//	}
//
//}
