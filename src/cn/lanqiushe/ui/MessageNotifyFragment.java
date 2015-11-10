package cn.lanqiushe.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import cn.lanqiushe.App;
import cn.lanqiushe.R;
import cn.lanqiushe.adapter.MessageAdapter;
import cn.lanqiushe.engine.DataService;
import cn.lanqiushe.entity.Notify;
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.LogManager;
import cn.lanqiushe.manager.StringManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.manager.UIManager;
import cn.lanqiushe.view.BaseListView;

import cn.lanqiushe.view.pullrefresh.PullToRefreshBase;
import cn.lanqiushe.view.pullrefresh.PullToRefreshBase.OnRefreshListener;

public class MessageNotifyFragment extends BaseFragment implements
		OnRefreshListener<BaseListView>, OnItemLongClickListener,
		OnItemClickListener {
	private Dialog mDelNotifyLoading;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mNotifyPullRefresh.onPullDownRefreshComplete();
			mNotifyPullRefresh.setLastUpdatedLabel(new Date().toLocaleString());
			mNotifyPullRefresh.onPullUpRefreshComplete();
			UIManager.toggleDialog(mDelNotifyLoading);
			switch (msg.what) {
			case ConstantManager.LOADING_1: {
				// 整个列表加载的时候用的提示框
				break;
			}
			case ConstantManager.SUCCESS_1: {
				listNotifies = (ArrayList<Notify>) msg.obj;
				if (notifyAdapter == null && notifyPage == 1) {
					// 第一次加载
					notifyAdapter = new MessageAdapter(fa,
							MessageAdapter.CATEGORY_NOTIFY);
					notifyAdapter.setHandler(handler);
					notifyAdapter.setNotifyList(listNotifies);
					mNotifyListView.setAdapter(notifyAdapter);
				} else {
					notifyAdapter.setNotifyList(listNotifies);
					notifyAdapter.notifyDataSetChanged();
				}
				  // ((MainActivity) fa).setEmptyView(mNotifyListView, nearByLvEmptyView, R.string.lv_empty_no_park);
				break;
			}

			// --------- 删除通知------------------
			case ConstantManager.SUCCESS_2: {
				// 删除通知成功
				int position = (Integer) msg.obj;
				notifyAdapter.getNotifyList().remove(position);
				notifyAdapter.notifyDataSetChanged();
				break;
			}
			case ConstantManager.LOADING_2: {
				// 删除通知中
				mDelNotifyLoading = UIManager.getLoadingDialog(fa);
				mDelNotifyLoading.show();
				break;
			}

			// -----------改变状态------------
			case ConstantManager.SUCCESS_3: {
				// 删除通知成功
				int position = (Integer) msg.obj;
				Notify notify = notifyAdapter.getNotifyList().get(position);
				notify.notifyStatus = fa.getResources().getString(
						R.string.agreed);
				notifyAdapter.notifyDataSetChanged();
				break;
			}

			case ConstantManager.LOADING_3: {
				break;
			}

			case ConstantManager.FAIL_CODE_ERROR:{
				ToastManager.show(fa, (String) msg.obj);
				break;
			}
			// ----------comm------------------
			case ConstantManager.FAIL_SERVER_ERROR:
			case ConstantManager.FAIL_NO_NET: {
				ToastManager.show(fa, R.string.toast_un_net);
				break;
			}

			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogManager.e(tag, "NotifyMessageFragment  开始");
		return inflater.inflate(R.layout.contacts_notify, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void init() {
		mNotifyPullRefresh = initPullRefresh(R.id.message_notify_prlv, this);
		mNotifyListView = ((MainActivity) fa).getListView(mNotifyPullRefresh);
		notifyMap = new HashMap<String, Object>();
		App app = (App) fa.getApplication();
		notifyMap.put("userId", app.getUser().userId);
		// notifyMap.put("userId", 1);
		onPullDownToRefresh(mNotifyPullRefresh);
		mNotifyListView.setOnItemLongClickListener(this);
		mNotifyListView.setOnItemClickListener(this);
		super.init();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<BaseListView> refreshView) {

		notifyPage = 0;
		notifyAdapter = null;
		DataService.getNotifyList(++notifyPage, notifyMap, fa, handler);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<BaseListView> refreshView) {

		DataService.getNotifyList(++notifyPage, notifyMap, fa, handler);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {

		Notify fy = notifyAdapter.getNotifyList().get(position);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("position", position);
		map.put("notifyId", fy.notifyId);
		map.put("handleType", ConstantManager.NOTIFY_TREATEDTYPE_DEL);// 2代表删除通知
		DataService.handleNotify(map, fa, handler);
 
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	  ToastManager.show(fa, "进入通知系详情界面");
	}

	@Override
	public void onDestroy() {
		LogManager.e("xx", "通知界面销毁。。。");
		super.onDestroy();
	}
}
