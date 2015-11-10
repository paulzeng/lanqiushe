package cn.lanqiushe;

import java.util.ArrayList;
import java.util.Map;

import cn.lanqiushe.entity.Player;
import cn.lanqiushe.entity.Team;
import cn.lanqiushe.entity.User;
import cn.lanqiushe.manager.PreferenceManager;
import cn.lanqiushe.manager.ThreadPool;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.lang.reflect.Field;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class App extends Application {
	public ArrayList<Activity> activitys = new ArrayList<Activity>();
	private User user;
	private static final String SAVE_USER_FILENAME = "user";

	@Override
	public void onCreate() {
		super.onCreate();
		initImageLoader(getApplicationContext());
	}

	public void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 退出账号（部分数据需要清除）， 退出app（只是关闭所有的界面）
	 */
	public static final int EXIT_ACCOUNT = 0;
	public static final int EXIT_APP = 1;
	public static final int EXIT_ALERT_PWD = 2;
	public void exit(int type) {
		if (type == EXIT_ACCOUNT||type==EXIT_ALERT_PWD) {
          User user = getUser();
          user.userId = null;
          setUser(user);
		}  
		for (Activity activity : activitys) {
			activity.finish();
		}

	}

	/**
	 * 登录成功后保存用户信息
	 */
	public User getUser() {
		if (user == null) {// 如果不为null，说明user信息还在内存中保存
			File file = new File(getFilesDir().getAbsolutePath(),
					SAVE_USER_FILENAME);
			FileInputStream fis = null;
			ObjectInputStream ois = null;
			try {
				if (!file.exists()) {
					// 如果该文件不存在，
					file.createNewFile();
					return new User();
				}
				fis = new FileInputStream(file);
				ois = new ObjectInputStream(fis);
				user = (User) ois.readObject();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (ois != null) {
					try {
						ois.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
			if (user == null) {// 如果这里还是为null,说明，应该创建一个user.但是没有任何的数据
				user = new User();
			}

		}

		return user;
	}

	public void setUser(User user) {
		this.user = user;
		saveUserInfoToFile(user);
	}

	/**
	 * 保存用户
	 * 
	 * @param user
	 */
	private void saveUserInfoToFile(final User user) {

		ThreadPool.getInstance().addTask(new Runnable() {

			@Override
			public void run() {
				File file = new File(getFilesDir().getAbsolutePath(),
						SAVE_USER_FILENAME);

				FileOutputStream fos = null;
				ObjectOutputStream oos = null;
				try {
					if (!file.exists()) {
						file.createNewFile();
					}
					fos = new FileOutputStream(file);
					oos = new ObjectOutputStream(fos);
					oos.writeObject(user);
					oos.flush();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (oos != null) {
						try {
							oos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}
		});

	}

}
