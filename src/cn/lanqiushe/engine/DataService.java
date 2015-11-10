package cn.lanqiushe.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import cn.lanqiushe.App;
import cn.lanqiushe.R;
import cn.lanqiushe.entity.ChatMsg;
import cn.lanqiushe.entity.Contact;
import cn.lanqiushe.entity.Notify;
import cn.lanqiushe.entity.Park;
import cn.lanqiushe.entity.Player;
import cn.lanqiushe.entity.ResultStatus;
import cn.lanqiushe.entity.Team;
import cn.lanqiushe.entity.User;
import cn.lanqiushe.manager.ConstantManager;
import cn.lanqiushe.manager.LogManager;
import cn.lanqiushe.manager.MediaManager;
import cn.lanqiushe.manager.StringManager;
import cn.lanqiushe.manager.ToastManager;
import cn.lanqiushe.ui.CreateTeamActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * 所有网络访问的入口
 * 
 * @author lee
 * 
 */
public class DataService {
	private static final String tag = "DataService";
	/**
	 * 服务端返回数据标准格式 { "code":100, "msg":"你的参数没有传递", "data":"" }
	 * 所有返回的数据，首先判断code是否正确，如果不正确：可以给用户提示，或者获取msg中的信息做为提示内容 如果正确解析data中我们需要获得的数据
	 * 
	 */
	private static final String CODE = "code";
	private static final String MSG = "msg";
	private static final String DATA = "data";

	// -----------------------注册登录模块---------------------------------
	/**
	 * 登录 (这里应该包含： 验证 微博和QQ)
	 * 
	 * @param context
	 * @param flag
	 *            用来判断是新浪微博还是QQ登录
	 */
	public static void login(final HashMap<String, Object> map,
			final Context context, final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			handler.sendEmptyMessage(ConstantManager.LOADING_1);
			addRequest(new StringRequest(makeUrl("visitor/login.do", map),
					new Listener<String>() {
						@Override
						public void onResponse(String response) {
							LogManager.e(tag, response);
							try {
								if (isResultNormal(response)) {
									// 三種情況 1、微博 2.QQ 3.自己的賬號
									JSONObject obj = new JSONObject(response)
											.optJSONObject(DATA);
									App app = (App) context
											.getApplicationContext();
									User user = new User();// 登录就应该就保存在本地的所有数据清除
									fillUserInfo(obj, user);
									app.setUser(user);
									handler.sendEmptyMessage(ConstantManager.SUCCESS_1);
								} else {
									// JSONObject obj = new
									// JSONObject(response);
									// obj.optString(MSG);
									Message msg = handler.obtainMessage(
											ConstantManager.FAIL_CODE_ERROR,
											"登录失败，用户名或密码错误");
									handler.sendMessage(msg);
									// 提示。
								}
							} catch (JSONException e) {

								e.printStackTrace();
							}

						}

					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
						}
					}), context);
		}
	}

	private static void fillUserInfo(JSONObject obj, User user) {
		// 正常必有数据
		user.phone = obj.optString("mob");
		user.userId = obj.optString("userId");
		user.portrait = obj.optString("portrait");
		user.nickName = obj.optString("nickName");
		user.post = parsePost(obj.optString("post"));
		user.gender = parseGender(obj.optString("sex"));
		// 球队数据信息
		JSONObject jsonTeam = obj.optJSONObject("team");
		if (jsonTeam != null) {
			Team team = user.team;
			if (team == null) {
				team = new Team();
			}
			team.teamId = jsonTeam.optString("teamId");
			team.teamName = jsonTeam.optString("teamName");
			team.teamLogo = jsonTeam.optString("teamLogo");
			ArrayList<Player> players = new ArrayList<Player>();
			JSONArray jsonPlayers = jsonTeam.optJSONArray("player");
			for (int j = 0, len = (jsonPlayers == null ? 0 : jsonPlayers
					.length()); j < len; j++) {
				JSONObject jsonPlayer = jsonPlayers.optJSONObject(j);
				Player p = new Player();
				p.playerId = jsonPlayer.optString("playerId");
				p.playerProtrait = jsonPlayer.optString("playerPortrait");
				p.nickName = jsonPlayer.optString("playerNickName");
				p.playerPost = parsePost(jsonPlayer.optString("playerPost"));
				players.add(p);
			}
			// 当球员数量不足3个的时候，用null来填充

			int size = players.size();
			for (int i = 0; i < ConstantManager.MAX_PLAYERS - size; i++) {
				players.add(null);
			}
			team.players = players;
			user.team = team;
		}
		//
	}

	/**
	 * 验证是否第三方登录是否授权
	 * 
	 * @param onlyId
	 *            授权唯一标示
	 * @param type
	 *            sina 或者 qq
	 */
	public static void verificationAuthorize(String onlyId, final int type,
			final Context context, final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			Message msg = handler.obtainMessage(ConstantManager.LOADING_1,
					R.string.dialog_auth);
			handler.sendMessage(msg);
			addRequest(new StringRequest("http://www.baidu.com",
					new Listener<String>() {
						@Override
						public void onResponse(String response) {
							Message msg = handler
									.obtainMessage(ConstantManager.SUCCESS_1);
							msg.arg1 = type;
							if (type == ConstantManager.LOGIN_QQ) {
								// 解析response
								if (false) {
									// 第一次授权，以就是服务端没有这个onlyId唯一串号
								} else {// 非第一次授权，返回user信息
									User user = new User();
									user.nickName = "用户名";
									user.gender = "男";
									msg.obj = user;

									App app = (App) context
											.getApplicationContext();
									app.setUser(user);

								}

							} else {// 微博
								if (true) { // 第一次授权

								} else {// 非第一次授权，返回user信息
									User user = new User();
									App app = (App) context
											.getApplicationContext();
									app.setUser(user);
								}
							}
							handler.sendMessage(msg);
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
						}
					}), context);
		}
	}

	/**
	 * 给手机发送验证码
	 */
	public static void sendVerificationCode(HashMap<String, Object> map,
			Context context, final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			// handler.sendEmptyMessage(ConstantManager.LOADING);
			addRequest(
					new StringRequest(makeUrl("visitor/mob_unique_verify.do",
							map), new Listener<String>() {
						@Override
						public void onResponse(String response) {
							LogManager.e(tag, response);
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							// handler.sendEmptyMessage(ConstantManager.RESULT_FAIL);
						}
					}), context);

		}
	}

	/**
	 * 检查验证码的正确性
	 */
	public static void verification(HashMap<String, Object> map,
			Context context, final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			handler.sendEmptyMessage(ConstantManager.LOADING_1);
			addRequest(
					new StringRequest(
							makeUrl("visitor/captcha_verify.do", map),
							new Listener<String>() {
								@Override
								public void onResponse(String response) {
									LogManager.e(tag, response);
									// if (isResultNormal(response)) {
									handler.sendEmptyMessage(ConstantManager.SUCCESS_1);
									// } else {
									// Message msg = handler
									// .obtainMessage(
									// / ConstantManager.FAIL_CODE_ERROR,
									// "验证失败");
									// handler.sendMessage(msg);
									// }

								}
							}, new ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError error) {
									handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
								}
							}), context);
		}
	}

	// ----------------我 模块---------------------------------

	public static void getUserInfo(final HashMap<String, Object> map,
			final Context context, final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			handler.sendEmptyMessage(ConstantManager.LOADING_2);
			addRequest(
					new StringRequest(
							makeUrl("user/get_user_base_info.do", map),
							new Listener<String>() {
								@Override
								public void onResponse(String response) {
									try {
										if (isResultNormal(response)) {
											JSONObject obj = new JSONObject(
													response)
													.getJSONObject(DATA);
											App app = (App) context
													.getApplicationContext();
											User user = new User();
											fillUserInfo(obj, user);
											app.setUser(user);
											handler.sendEmptyMessage(ConstantManager.SUCCESS_2);
										} else {
											Message msg = handler
													.obtainMessage(
															ConstantManager.FAIL_CODE_ERROR,
															"获取信息失败");
											handler.sendMessage(msg);
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}

								}
							}, new ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError error) {
									handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
								}
							}), context);
		}
	}

	/**
	 * 修改用户信息
	 */
	public static void alertUserInfo(String userId, String nickName,
			String sex, String post, final Context context,
			final Handler handler) {

		try {
			AjaxParams params = new AjaxParams();
			params.put("userId", userId);
			params.put("nickName", nickName);
			params.put("post", post);
			params.put("sex", sex);

			FinalHttp fh = new FinalHttp();
			fh.post(makeUrl("user/alter_user_base_info.do", null), params,
					new AjaxCallBack<Object>() {
						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
							super.onFailure(t, errorNo, strMsg);
						}

						@Override
						public void onLoading(long count, long current) {
							handler.sendEmptyMessage(ConstantManager.LOADING_1);
							super.onLoading(count, current);
						}

						@Override
						public void onSuccess(Object t) {
							LogManager.e(tag, "注册" + t.toString());
							try {
								// 上传成功后。把文件中的所有文件都删除了
								if (isResultNormal(t.toString())) {

									// Message msg =
									// handler.obtainMessage(ConstantManager.SUCCESS_1,
									// userid);
									// handler.sendMessage(msg);
									// File[] files =
									// logo.getParentFile().listFiles();
									// for (File f : files) {
									// f.delete();
									// }
								} else {
									handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
								}
							} catch (Exception e) {
								handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
								e.printStackTrace();
							}

							super.onSuccess(t);
						}
					});
		} catch (Exception e) {
			handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
			e.printStackTrace();
		}

		//
		// if (isNetworkConnected(handler, context)) {
		// handler.sendEmptyMessage(ConstantManager.LOADING_1);
		// addRequest(new StringRequest("http://www.baidu.com",
		// new Listener<String>() {
		// @Override
		// public void onResponse(String response) {
		// if (isResultNormal(response)) {
		// App app = (App) context.getApplicationContext();
		// User user = app.getUser();
		// user.nickName = (String) map.get("nickName");
		// user.portrait = (String) map.get("url");
		// user.post = (String) map.get("post");
		// user.gender = (String) map.get("sex");
		// app.setUser(user);
		// handler.sendEmptyMessage(ConstantManager.SUCCESS_1);
		// } else {
		// Message msg = handler
		// .obtainMessage(
		// ConstantManager.FAIL_CODE_ERROR,
		// "修改失败");
		// handler.sendMessage(msg);
		// }
		//
		// }
		// }, new ErrorListener() {
		//
		// @Override
		// public void onErrorResponse(VolleyError error) {
		// handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
		// }
		// }), context);
		// }
	}

	/**
	 * 创建球队
	 */
	public static void createTeam(String userId, final File logo,
			String teamName, final Context context, final Handler handler) {
		try {
			AjaxParams params = new AjaxParams();
			params.put("userId", userId);
			params.put("name", teamName);
			params.put("logo", logo);
			FinalHttp fh = new FinalHttp();
			fh.post(makeUrl("user/member/create_team.do", null), params,
					new AjaxCallBack<Object>() {
						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							LogManager.e(tag, "statst...");
							handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
							super.onFailure(t, errorNo, strMsg);
						}

						@Override
						public void onLoading(long count, long current) {
							LogManager.e(tag, "onLoading ...");
							handler.sendEmptyMessage(ConstantManager.LOADING_1);
							super.onLoading(count, current);
						}

						@Override
						public void onSuccess(Object t) {
							LogManager.e(tag, "创建球队成功" + t.toString());
							// 上传成功后。把文件中的所有文件都删除了
							if (isResultNormal(t.toString())) {
								LogManager.e(tag, "onSuccess ...");
								handler.sendEmptyMessage(ConstantManager.SUCCESS_1);
								File[] files = logo.getParentFile().listFiles();
								for (File f : files) {
									f.delete();
								}

							} else { 	
								LogManager.e(tag, "dedddd ...");
								handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
							}

							super.onSuccess(t);
						}
					});
		} catch (Exception e) {
			LogManager.e(tag, "yicyang le ...");
			handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
			e.printStackTrace();
		}

		//
		//
		//
		// if (isNetworkConnected(handler, context)) {
		// handler.sendEmptyMessage(ConstantManager.LOADING_1);
		// addRequest(
		// new StringRequest(
		// makeUrl("user/member/create_team.do", map),
		// new Listener<String>() {
		// @Override
		// public void onResponse(String response) {
		// Log.e("xx", response);
		// if (isResultNormal(response)) {
		// handler.sendEmptyMessage(ConstantManager.SUCCESS_1);
		// } else {
		// Message msg = handler
		// .obtainMessage(
		// ConstantManager.FAIL_CODE_ERROR,
		// "创建球队失败");
		// handler.sendMessage(msg);
		//
		// }
		//
		// // 跳转到me界面，并且要刷新数据。
		// // post数据到s端
		// // App app = ((App)
		// // context.getApplicationContext());
		// // User user = app.getUser();
		// // final Team team = new Team();
		// // team.teamName = teamName;
		// // team.teamLogo =
		// //
		// "http://imgsrc.baidu.com/forum/w%3D580/sign=dd3203209a504fc2a25fb00dd5dde7f0/1d2daa345982b2b722beb6ec30adcbef76099b5b.jpg";
		// // ArrayList<Player> ps = new
		// // ArrayList<Player>();
		// // Player p1 = new Player();
		// // p1.nickName = "李蛟blue";
		// // p1.playerPost = "前锋";
		// // p1.playerProtrait = "";
		// // ps.add(p1);
		// // ps.add(null);
		// // ps.add(null);
		// // team.players = ps;
		// // user.team = team;
		// // app.setUser(user);
		// // Message msg = handler.obtainMessage(
		// // ConstantManager.RESULT_SUCCESS,
		// // response);
		// // handler.sendMessage(msg);
		// }
		// }, new ErrorListener() {
		//
		// @Override
		// public void onErrorResponse(VolleyError error) {
		// handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
		// }
		// }), context);
		// }
	}

	// --------------------附近模块---------------------------------------------------------------
	/**
	 * 获取球队球队列表
	 */
	public static void getTeamList(final int page,
			final HashMap<String, Object> map, final Context context,
			final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			// handler.sendEmptyMessage(ConstantManager.LOADING);
			addRequest(
					new StringRequest(
							makeUrl("user/search_nearby_team.do", map),
							new Listener<String>() {
								@Override
								public void onResponse(String response) {
									LogManager
											.e(tag, "获取球队列表结果----" + response);
									try {
										if (isResultNormal(response)) {
											JSONArray arr = new JSONObject(
													response)
													.optJSONArray(DATA);

											ArrayList<Team> list = new ArrayList<Team>();
											for (int i = 0, len = arr.length(); i < len; i++) {
												// 一定要返回的数据 1、球队名字 2 ，球队人数，3、距离
												// 4、球队logo, 5点
												// 击进入后，球队球员信息
												JSONObject obj = arr
														.optJSONObject(i);
												Team t = new Team();
												t.teamId = obj
														.optString("teamId");
												t.teamLogo = // obj.optString("logo");
												"http://imgsrc.baidu.com/forum/w%3D580/sign=ae6042c4d000baa1ba2c47b37710b9b1/3c631ed8bc3eb13502a73893a71ea8d3fd1f441b.jpg";
												t.teamName = obj
														.optString("name");
												t.distance = parseDisatnce(obj
														.optString("range"));
												t.playerTotal = parsePlayerNumber(obj
														.optString("buildState"));
												list.add(t);
											}

											// ----test-----
											for (int i = (page - 1) * 10; i < page * 10; i++) {
												Team t = new Team();
												t.teamName = "test--name";
												t.distance = "1km";
												t.playerTotal = "(1/3)";
												list.add(t);
											}
											Message msg = handler
													.obtainMessage(
															ConstantManager.SUCCESS_1,
															list);
											handler.sendMessage(msg);

										} else {
											// 没有获取到数据，提示。
											Message msg = handler
													.obtainMessage(
															ConstantManager.FAIL_CODE_ERROR,
															"");
											handler.sendMessage(msg);
										}
									} catch (JSONException e) {
										Message msg = handler
												.obtainMessage(
														ConstantManager.FAIL_CODE_ERROR,
														"");
										handler.sendMessage(msg);
										e.printStackTrace();
									}

								}
							}, new ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError error) {
									// handler.sendEmptyMessage(ConstantManager.RESULT_FAIL);
								}
							}), context);
		}
	}

	/**
	 * 获得球友列表
	 * 
	 * @param context
	 * @param handler
	 */
	public static void getPlayerList(final int page,
			final HashMap<String, Object> map, Context context,
			final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			// handler.sendEmptyMessage(ConstantManager.LOADING);
			addRequest(
					new StringRequest(makeUrl("user/search_nearby_golfers.do",
							map), new Listener<String>() {
						@Override
						public void onResponse(String response) {
							LogManager.e(tag, "获得球友列表-----" + response);
							try {
								if (isResultNormal(response)) {
									ArrayList<Player> list = new ArrayList<Player>();
									// --test 无球队player测试--
									Player pp = new Player();
									pp.playerId = "userId";
									pp.playerProtrait = "http://imgsrc.baidu.com/forum/w%3D580/sign=ae6042c4d000baa1ba2c47b37710b9b1/3c631ed8bc3eb13502a73893a71ea8d3fd1f441b.jpg";
									pp.nickName = "nickName";
									pp.playerPost = parsePost("1");
									pp.sex = "1";

									pp.distance = "1.0km";
									list.add(pp);
									// --test--

									// 真正的解析
									JSONArray arr = new JSONObject(response)
											.optJSONArray(DATA);
									for (int i = 0, len = arr.length(); i < len; i++) {
										JSONObject obj = arr.optJSONObject(i);
										Player p = new Player();
										p.playerId = obj.optString("userId");
										p.playerProtrait = // obj.optString("portrait");
										"http://imgsrc.baidu.com/forum/w%3D580/sign=ae6042c4d000baa1ba2c47b37710b9b1/3c631ed8bc3eb13502a73893a71ea8d3fd1f441b.jpg";
										p.nickName = obj.optString("nickName");
										p.playerPost = parsePost(obj
												.optString("post"));
										p.sex = parseGender(obj
												.optString("sex"));

										p.distance = parseDisatnce(obj
												.optString("range"));

										// 可选择
										JSONObject jsonTeam = obj
												.optJSONObject("team");
										if (jsonTeam != null) {
											Team team = new Team();
											team.teamId = jsonTeam
													.optString("teamId");
											team.teamName = jsonTeam
													.optString("teamName");
											team.playerTotal = parsePlayerNumber(jsonTeam
													.optString("teamBuildState"));

											p.team = team;
										}

										list.add(p);
									}
									// ----------------test
									for (int i = (page - 1) * 10; i < page * 10; i++) {
										Player p = new Player();
										p.playerProtrait = "http://imgsrc.baidu.com/forum/w%3D580/sign=ae6042c4d000baa1ba2c47b37710b9b1/3c631ed8bc3eb13502a73893a71ea8d3fd1f441b.jpg";
										p.nickName = "nickName";
										p.playerPost = "1";
										p.sex = "1";
										p.distance = "1km";
										list.add(p);
									}

									Message msg = handler.obtainMessage(
											ConstantManager.SUCCESS_1, list);
									handler.sendMessage(msg);
								} else {
									handler.sendEmptyMessage(ConstantManager.FAIL_CODE_ERROR);
								}
							} catch (JSONException e) {
								handler.sendEmptyMessage(ConstantManager.FAIL_CODE_ERROR);
								e.printStackTrace();
							}

						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							// handler.sendEmptyMessage(ConstantManager.RESULT_FAIL);
						}
					}), context);
		}
	}

	/**
	 * 获得附近的球场
	 * 
	 * @param map
	 * @param context
	 * @param handler
	 */
	public static void getParkList(final int page,
			final HashMap<String, Object> map, Context context,
			final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			// handler.sendEmptyMessage(ConstantManager.LOADING);
			addRequest(
					new StringRequest(makeUrl("user/search_nearby_court.do",
							map), new Listener<String>() {
						@Override
						public void onResponse(String response) {

							LogManager.e(tag, "获得附近的球场-----" + response);
							try {
								if (isResultNormal(response)) {
									// 获取成功
									ArrayList<Park> list = new ArrayList<Park>();
									JSONArray arr = new JSONObject(response)
											.optJSONArray(DATA);
									for (int i = 0, len = arr.length(); i < len; i++) {
										JSONObject obj = arr.optJSONObject(i);
										Park p = new Park();
										p.courtId = obj.optString("courtId");

										String[] urls = {
												"http://imgt2.bdstatic.com/it/u=64941403,312324199&fm=23&gp=0.jpg",
												"http://imgt5.bdstatic.com/it/u=148020234,468633401&fm=23&gp=0.jpg",
												"http://pic26.nipic.com/20121216/10603465_222904439163_2.jpg",
												"http://www.led-100.com/news/2010/image/LED20100715-3.jpg" };
										p.urls = // obj.optString("logo").split("|");
										urls;
										p.logo = urls[0];
										p.name = obj.optString("name");
										p.money = obj.optString("price")
												+ "元/小时";
										double d = Double.parseDouble(obj
												.optString("range")) / 1000;
										String s = String.valueOf(d);
										p.distance = s.substring(0,
												s.lastIndexOf(".") + 2)
												+ "km";
										list.add(p);
									}

									// -----test------

									for (int i = (page - 1) * 10; i < page * 10; i++) {
										Park p = new Park();
										p.logo = "http://img3.douban.com/view/ark_article_cover/cut/public/3406427.jpg";
										p.name = "testname" + i;
										p.money = "test元/小时";
										p.distance = "test1km";
										list.add(p);
									}

									Message msg = handler.obtainMessage(
											ConstantManager.SUCCESS_1, list);
									handler.sendMessage(msg);
								} else {
									// 获取失败,需要刷新
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							// handler.sendEmptyMessage(ConstantManager.RESULT_FAIL);
						}
					}), context);
		}
	}

	/**
	 * 获取球队信息
	 * 
	 * @param map
	 * @param context
	 * @param handler
	 */

	public static void getTeamInfo(final HashMap<String, Object> map,
			final Context context, final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			handler.sendEmptyMessage(ConstantManager.LOADING_1);
			addRequest(
					new StringRequest(
							makeUrl("user/nearby_team_detail.do", map),
							new Listener<String>() {
								@Override
								public void onResponse(String response) {
									LogManager.e(tag, response);
									try {
										if (isResultNormal(response)) {

											JSONObject jsonobj = new JSONObject(
													response)
													.optJSONObject(DATA);
											Team team = new Team();
											team.teamId = jsonobj
													.optString("teamId");
											team.teamLogo = "http://pic26.nipic.com/20121216/10603465_222904439163_2.jpg";
											// jsonobj.optString("teamLogo");
											team.teamName = jsonobj
													.optString("teamName");
											team.distance = "xxkm";
											JSONArray arr = jsonobj
													.optJSONArray("player");
											ArrayList<Player> ps = new ArrayList<Player>();
											for (int i = 0, len = arr.length(); i < len; i++) {
												JSONObject obj = arr
														.optJSONObject(i);
												Player p1 = new Player();
												p1.playerPost = parsePost(obj
														.optString("post"));
												p1.nickName = obj
														.optString("playerNickName");
												ps.add(p1);
											}
											int len = ps.size();
											for (int j = 0; j < ConstantManager.MAX_PLAYERS
													- len; j++) {
												ps.add(null);
											}
											team.players = ps;
											Message msg = handler
													.obtainMessage(
															ConstantManager.SUCCESS_1,
															team);
											handler.sendMessage(msg);
										} else {
											Message msg = handler
													.obtainMessage(
															ConstantManager.FAIL_CODE_ERROR,
															"失败");
											handler.sendMessage(msg);
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}
							}, new ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError error) {
									handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
								}
							}), context);
		}
	}

	/**
	 * 获取球友详细信息
	 * 
	 * @param map
	 * @param context
	 * @param handler
	 */
	public static void getPlayerInfo(final HashMap<String, Object> map,
			final Context context, final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			handler.sendEmptyMessage(ConstantManager.LOADING_1);
			addRequest(
					new StringRequest(makeUrl("user/nearby_golfers_detail.do",
							map), new Listener<String>() {
						@Override
						public void onResponse(String response) {
							LogManager.e(tag, response);
							try {
								if (isResultNormal(response)) {
									JSONArray arr = new JSONObject(response)
											.optJSONObject(DATA).optJSONArray(
													"player");
									ArrayList<Player> ps = new ArrayList<Player>();
									for (int i = 0, len = arr.length(); i < len; i++) {
										JSONObject obj = arr.optJSONObject(i);
										Player p1 = new Player();
										p1.playerPost = parsePost(obj
												.optString("post"));
										p1.nickName = obj
												.optString("playerNickName");
										ps.add(p1);
									}
									int len = ps.size();
									for (int j = 0; j < ConstantManager.MAX_PLAYERS
											- len; j++) {
										ps.add(null);
									}

									Message msg = handler.obtainMessage(
											ConstantManager.SUCCESS_1, ps);
									handler.sendMessage(msg);
								} else {
									Message msg = handler.obtainMessage(
											ConstantManager.FAIL_CODE_ERROR,
											"失败");
									handler.sendMessage(msg);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
						}
					}), context);
		}
	}

	/**
	 * 加入球队
	 * 
	 * @param map
	 * @param context
	 * @param handler
	 */
	public static void joinTeam(final HashMap<String, Object> map,
			final Context context, final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			// handler.sendEmptyMessage(ConstantManager.LOADING);
			addRequest(
					new StringRequest(makeUrl(
							"user/member/apply_join_team_notify.do", map),
							new Listener<String>() {
								@Override
								public void onResponse(String response) {
									LogManager.e(tag, response);
									if (isResultNormal(response)) {
										handler.sendEmptyMessage(ConstantManager.SUCCESS_2);
									} else {
										Message msg = handler
												.obtainMessage(
														ConstantManager.FAIL_CODE_ERROR,
														"加入球队失败");
										handler.sendMessage(msg);
									}
								}
							}, new ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError error) {
									handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
								}
							}), context);
		}
	}

	/**
	 * 退出球队
	 * 
	 * @param map
	 * @param context
	 * @param handler
	 */
	public static void exitTeam(final HashMap<String, Object> map,
			final Context context, final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			addRequest(
					new StringRequest(makeUrl("user/player/exit_team.do", map),
							new Listener<String>() {
								@Override
								public void onResponse(String response) {
									LogManager.e(tag, response);
									if (isResultNormal(response)) {
										handler.sendEmptyMessage(ConstantManager.SUCCESS_3);
									} else {
										Message msg = handler
												.obtainMessage(
														ConstantManager.FAIL_CODE_ERROR,
														"退出球队失败原因");
										handler.sendMessage(msg);
									}

								}
							}, new ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError error) {
									handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
								}
							}), context);
		}
	}

	/**
	 * 发起约占
	 * 
	 * @param map
	 * @param context
	 * @param handler
	 */

	public static void sendPKInfo(final HashMap<String, Object> map,
			final Context context, final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			addRequest(
					new StringRequest(makeUrl("user/player/fight_notify.do",
							map), new Listener<String>() {
						@Override
						public void onResponse(String response) {
							LogManager.e(tag, response);
							if (isResultNormal(response)) {
								handler.sendEmptyMessage(ConstantManager.SUCCESS_4);
							} else {
								Message msg = handler
										.obtainMessage(
												ConstantManager.FAIL_CODE_ERROR,
												"约战失败");
								handler.sendMessage(msg);
							}
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
						}
					}), context);
		}
	}

	/**
	 * invite
	 */
	public static void invitePlayerBuildTeam(final HashMap<String, Object> map,
			final Context context, final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			addRequest(
					new StringRequest(makeUrl(
							"user/player/invate_join_team_notify.do", map),
							new Listener<String>() {
								@Override
								public void onResponse(String response) {
									LogManager.e(tag, response);
									if (isResultNormal(response)) {
										handler.sendEmptyMessage(ConstantManager.SUCCESS_4);
									} else {
										Message msg = handler
												.obtainMessage(
														ConstantManager.FAIL_CODE_ERROR,
														"邀请失败");
										handler.sendMessage(msg);
									}
								}
							}, new ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError error) {
									handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
								}
							}), context);
		}
	}

	// ----------------------------------设置模块------------------------------------------------------

	/**
	 * 获得手机通讯录列表
	 * 
	 * @param map
	 * @param context
	 * @param handler
	 */

	public static void getPhoneContactsList(final HashMap<String, Object> map,
			final Context context, final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			handler.sendEmptyMessage(ConstantManager.LOADING_1);
			addRequest(
					new StringRequest(makeUrl("user/mob_golfer_state.do", map),
							new Listener<String>() {
								@Override
								public void onResponse(String response) {
									LogManager.e(tag, response);
									try {
										if (isResultNormal(response)) {
											// JSONObject jsonData = new
											// JSONObject(
											// response)
											// .optJSONObject(DATA);
											// JSONArray addArr = jsonData
											// .optJSONArray("addUser");
											// JSONArray inviteArr = jsonData
											// .optJSONArray("inviteUser");
											ArrayList<Contact> list = new ArrayList<Contact>();
											//
											// // 待添加列表
											// for (int i = 0, len = addArr
											// .length(); i < len; i++) {
											// JSONObject obj = addArr
											// .optJSONObject(i);
											// Contact c = new Contact();
											// c.status =
											// R.string.status_tianjia;
											// // c.head =
											// "http://www.apkbus.com/data/attachment/portal/201404/14/110156ad9z3vu94di3kd56.jpeg";
											// //obj.optString("logo");
											// c.name =
											// obj.optString("nickName");
											// list.add(c);
											// }
											// // 邀请好友列表
											// for (int i = 0, len = inviteArr
											// .length(); i < len; i++) {
											// JSONObject obj = inviteArr
											// .optJSONObject(i);
											// Contact c = new Contact();
											// c.status =
											// R.string.status_yaoqing;
											// //c.head =
											// "http://www.apkbus.com/data/attachment/portal/201404/14/110156ad9z3vu94di3kd56.jpeg";
											// // obj.optString("logo");
											// c.name =
											// obj.optString("nickName");
											// list.add(c);
											// }
											// ------test-----------
											for (int i = 0; i < 20; i++) {
												Contact c = new Contact();
												c.status = R.string.status_tianjia;
												c.name = "testname" + i;
												list.add(c);
											}
											for (int i = 0; i < 20; i++) {
												Contact c = new Contact();
												c.status = R.string.status_yaoqing;
												c.name = "testname" + i;
												list.add(c);
											}
											for (int i = 0; i < 20; i++) {
												Contact c = new Contact();
												c.status = R.string.status_yitianjia;
												c.name = "testname" + i;
												list.add(c);
											}
											for (int i = 0; i < 20; i++) {
												Contact c = new Contact();
												c.status = R.string.status_yiyaoqing;
												c.name = "testname" + i;
												list.add(c);
											}

											// -------test-----------

											Message msg = handler
													.obtainMessage(
															ConstantManager.SUCCESS_1,
															list);
											handler.sendMessage(msg);
										} else {
											Message msg = handler
													.obtainMessage(
															ConstantManager.FAIL_CODE_ERROR,
															"获取网络通讯录信息失败");
											handler.sendMessage(msg);
										}
									} catch (Exception e) {
										Message msg = handler
												.obtainMessage(
														ConstantManager.FAIL_CODE_ERROR,
														"获取网络通讯录信息失败");
										handler.sendMessage(msg);
										e.printStackTrace();
									}
								}
							}, new ErrorListener() {

								public void onErrorResponse(VolleyError error) {
									handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
								}
							}), context);
		}
	}

	/**
	 * 改变通讯录中联系人和我的关系
	 * 
	 * @param map
	 * @param context
	 * @param handler
	 */
	public static void changeContactsSatus(final HashMap<String, Object> map,
			final Context context, final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			// handler.sendEmptyMessage(ConstantManager.LOADING_1);
			HashMap<String, Object> map0 = new HashMap<String, Object>();
			map0.put("userId", map.get("userId"));
			map0.put("golfersId", map.get("golfersId"));

			addRequest(
					new StringRequest(
							makeUrl("user/apply_add_golfers.do", map0),
							new Listener<String>() {
								@Override
								public void onResponse(String response) {
									LogManager.e(tag, response);
									if (isResultNormal(response)) {
										// 修改成功
										Message msg = handler.obtainMessage(
												ConstantManager.SUCCESS_3,
												map.get("position"));
										msg.arg1 = (Integer) map.get("status");
										handler.sendMessage(msg);
									} else {
										// Message msg =
										// handler.obtainMessage(ConstantManager.FAIL_CODE_ERROR,
										// "");
										handler.sendEmptyMessage(ConstantManager.FAIL_CODE_ERROR);
									}

								}
							}, new ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError error) {
									handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
								}
							}), context);
		}
	}

	/**
	 * 设置-->修改密码
	 */
	public static void alertPwd(final HashMap<String, Object> map,
			final Context context, final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			handler.sendEmptyMessage(ConstantManager.LOADING_1);
			addRequest(new StringRequest(makeUrl("user/alt_login_pwd.do", map),
					new Listener<String>() {
						@Override
						public void onResponse(String response) {
							LogManager.e(tag, response);
							if (isResultNormal(response)) {
								// 修改成功
								handler.sendEmptyMessage(ConstantManager.SUCCESS_1);
							} else {
								Message msg = handler.obtainMessage(
										ConstantManager.FAIL_CODE_ERROR,
										"密码修改失败");
								handler.sendMessage(msg);
							}
							//
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
						}
					}), context);
		}
	}

	/**
	 * 设置---->通讯录---->改变手机号码
	 */
	public static void changePhone(final HashMap<String, Object> map,
			final Context context, final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			handler.sendEmptyMessage(ConstantManager.LOADING_1);
			addRequest(new StringRequest("http://www.baidu.com",
					new Listener<String>() {
						@Override
						public void onResponse(String response) {
							LogManager.e(tag, response);
							if (isResultNormal(response)) {
								// 修改成功
								handler.sendEmptyMessage(ConstantManager.SUCCESS_1);
							} else {
								Message msg = handler.obtainMessage(
										ConstantManager.FAIL_CODE_ERROR, "失败");
								handler.sendMessage(msg);
							}
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
						}
					}), context);
		}
	}

	/**
	 * 绑定微博获取用户信息
	 * 
	 * @param map
	 * @param context
	 * @param handler
	 */

	public static void getWeiboFansByUserId(final HashMap<String, Object> map,
			final Context context, final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			handler.sendEmptyMessage(ConstantManager.LOADING_1);
			addRequest(new StringRequest(
					makeUrl("user/user_bind_info.do", map),
					new Listener<String>() {
						@Override
						public void onResponse(String response) {
							LogManager.e(tag, response);
							if (isResultNormal(response)) {
								// 修改成功
								handler.sendEmptyMessage(ConstantManager.SUCCESS_1);
							} else {
								Message msg = handler.obtainMessage(
										ConstantManager.FAIL_CODE_ERROR, "失败");
								handler.sendMessage(msg);
							}
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
						}
					}), context);
		}
	}

	/**
	 * 绑定手机号码
	 */
	public static void bindPhone(Context context, final String phone,
			String pwd, final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			handler.sendEmptyMessage(ConstantManager.LOADING_1);

			addRequest(new StringRequest("http://www.baidu.com",
					new Listener<String>() {
						@Override
						public void onResponse(String response) {
							Message msg = handler.obtainMessage(
									ConstantManager.SUCCESS_1, phone);
							handler.sendMessage(msg);
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
						}
					}), context);
		}
	}

	// -------------------通讯模块----------------------------------------
	/**
	 * 获取通知列表
	 * 
	 * @param context
	 * @param handler
	 */
	public static void getNotifyList(final int page,
			final HashMap<String, Object> map, final Context context,
			final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			handler.sendEmptyMessage(ConstantManager.LOADING_1);
			makeUrl("user/get_notify.do", map);
			addRequest(new StringRequest("http://www.baidu.com",
					new Listener<String>() {
						@Override
						public void onResponse(String response) {
							Resources rs = context.getResources();
							LogManager.e(tag, "通知界面。。。。" + response);
							try {
								// if (isResultNormal(response)) {
								// JSONArray arr = new JSONObject(response)
								// .optJSONArray(DATA);
								ArrayList<Notify> list = new ArrayList<Notify>();
								// for (int i = 0, len = arr.length(); i < len;
								// i++) {
								// JSONObject obj = arr.optJSONObject(i);
								// Notify notify = new Notify();
								// // 通知信息id
								// notify.notifyId =
								// // "1";//obj.optString("notifyId");
								// // 通知者头像
								// notify.notifierHead =
								// "http://img5.douban.com/mpic/s27222327.jpg";
								// obj.optString("portrait");
								// // 通知类型
								// notify.notifyType = "通知";//
								// obj.optString("notifyId");
								// notify.notifySimpleContent = "通知简单内容";//
								// obj.optString("notifyId");
								// notify.notifyStatus = "同意";//
								// obj.optString("type");
								// notify.notifyTime = "昨天";//
								// obj.optString("time");
								// list.add(notify);
								// }

								// -------------------test-----------------------
								for (int i = (page - 1) * 10; i < page * 10; i++) {
									Notify notify = new Notify();
									notify.notifyId = "" + i;
									notify.notifierHead = "http://img3.douban.com/view/ark_article_cover/cut/public/3404269.jpg";
									notify.notifyType = parseNotifyType(rs,
											new Random().nextInt(4) + 1 + "");
									notify.notifySimpleContent = "test内容";
									notify.notifyStatus = parseNotifyHandleStatus(
											rs, new Random().nextInt(2) + 1
													+ "");
									notify.notifyTime = "前天";
									list.add(notify);
								}
								// ---------test--------------------
								Message msg = handler.obtainMessage(
										ConstantManager.SUCCESS_1, list);
								handler.sendMessage(msg);
								// } else {
								// // 返回错误
								// Message msg =
								// handler.obtainMessage(ConstantManager.FAIL_JSON_OR_CODE_ERROR_1,
								// "状态 有误");
								// handler.sendMessage(msg);
								//
								// }
							} catch (Exception e) {
								handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
								e.printStackTrace();
							}

						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
						}
					}), context);

		}
	}

	/**
	 * 
	 * 处理通知接口，包括 删除单条通知 和点击同意按钮
	 * 
	 * @param map
	 * @param context
	 * @param handler
	 */

	public static void handleNotify(final HashMap<String, Object> map,
			final Context context, final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			handler.sendEmptyMessage(ConstantManager.LOADING_2);
			HashMap<String, Object> map0 = new HashMap<String, Object>();
			map0.put("notifyId", map.get("notifyId"));
			final int handleType = (Integer) map.get("handleType");
			map0.put("handleType", handleType);// 2代表删除
			makeUrl("user/handle_notify.do", map0);
			addRequest(new StringRequest("http://www.baidu.com",
					new Listener<String>() {
						@Override
						public void onResponse(String response) {
							LogManager.e(tag, "删除通知。。。。" + response);
							// 这里只需要判断成功删除或者状态改变成功与否。再通过改变集合来实现
							// if (isResultNormal(response)) {
							// 要删除或者要改变状态的通知索引
							int position = (Integer) map.get("position");
							Message msg;
							if (ConstantManager.NOTIFY_TREATEDTYPE_STATUS == handleType) {// 改变
								msg = handler.obtainMessage(
										ConstantManager.SUCCESS_3, position);
							} else {// 删除通知条目
								msg = handler.obtainMessage(
										ConstantManager.SUCCESS_2, position);

							}

							handler.sendMessage(msg);

							// } else {
							// Message msg =
							// handler.obtainMessage(ConstantManager.FAIL_CODE_ERROR,
							// "失败");
							// handler.sendMessage(msg);
							// }
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
						}
					}), context);

		}
	}

	/**
	 * 获取聊天列表
	 * 
	 * @param map
	 * @param context
	 * @param handler
	 */
	public static void getChatList(final int page,
			final HashMap<String, Object> map, Context context,
			final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			// handler.sendEmptyMessage(ConstantManager.LOADING);
			makeUrl("user/get_my_chat_item_info.do", map);
			addRequest(new StringRequest("http://www.baidu.com",
					new Listener<String>() {
						@Override
						public void onResponse(String response) {
							LogManager.e(tag, "聊天界面。。。。" + response);

							// try {
							// if (isResultNormal(response)) {
							// JSONArray arr = new JSONObject(response)
							// .optJSONObject(DATA).optJSONArray(
							// "singleItem");
							ArrayList<ChatMsg> list = new ArrayList<ChatMsg>();
							// for (int i = 0, len = arr.length(); i <
							// len; i++) {
							// JSONObject obj = arr.optJSONObject(i);
							// Notify notify = new Notify();
							// notify.id = obj.optString("notifyId");
							// notify.head =
							// "http://img3.douban.com/view/ark_article_cover/cut/public/3325482.jpg";//obj.optString("portrait");
							// notify.title = "title";//
							// obj.optString("notifyId");
							// notify.content = "content";//
							// obj.optString("notifyId");
							//
							// notify.time =
							// "昨天";//obj.optString("time");
							// list.add(notify);
							// }

							for (int i = (page - 1) * 10; i < page * 10; i++) {
								ChatMsg chat = new ChatMsg();
								chat.head = "http://img3.douban.com/view/ark_article_cover/cut/public/3404269.jpg";

								if (i % 2 == 0) {//
									chat.type = ChatMsg.CHAT_SINGLE;// 单聊
									chat.name = "单聊";
								} else {
									chat.type = ChatMsg.CHAT_GROUP;
									chat.name = "群聊VS群聊";
								}
								chat.content = "聊天内容";// obj.optString("notifyId");
								chat.time = "前天";
								list.add(chat);
							}

							Message msg = handler.obtainMessage(
									ConstantManager.SUCCESS_1, list);
							handler.sendMessage(msg);
							// } else {
							//
							// Message msg = handler.obtainMessage(
							// ConstantManager.CHANGE_PHONE_FAIL,
							// "失败");
							// handler.sendMessage(msg);
							// }
							// } catch (JSONException e) {
							// // TODO Auto-generated catch block
							// e.printStackTrace();
							// }

						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
						}
					}), context);

		}
	}

	/**
	 * 通讯--》聊天-->删除条目
	 * 
	 * @param map
	 * @param context
	 * @param handler
	 */
	public static void delSingleChatInfo(final HashMap<String, Object> map,
			Context context, final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			handler.sendEmptyMessage(ConstantManager.LOADING_2);
			addRequest(new StringRequest("http://www.baidu.com",
					new Listener<String>() {
						@Override
						public void onResponse(String response) {
							LogManager.e(tag, response);
							// if(){

							Message msg = handler.obtainMessage(
									ConstantManager.SUCCESS_2,
									map.get("position"));
							handler.sendMessage(msg);

							// }else{
							// Message msg =
							// handler.obtainMessage(ConstantManager.FAIL_JSON_ERROR_2,
							// map.get("position"));
							// handler.sendMessage(msg);
							// }

						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
						}
					}), context);

		}
	}

	/**
	 * 获取聊天的信息信息。包括群聊和单聊
	 * 
	 * @param map
	 * @param context
	 * @param handler
	 */

	public static void getChatDetailInfo(final HashMap<String, Object> map,
			Context context, final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			handler.sendEmptyMessage(ConstantManager.LOADING_1);
			makeUrl("user/single_chat_detail.do", map);// 单聊
			makeUrl("user/player/group_chat_detail.do", map);
			addRequest(new StringRequest("http://www.baidu.com",
					new Listener<String>() {
						@Override
						public void onResponse(String response) {
							LogManager.e(tag, response);
							ArrayList<ChatMsg> list = new ArrayList<ChatMsg>();
							for (int i = 0; i < 40; i++) {
								ChatMsg c = new ChatMsg();
								//
								c.head = "http://img3.douban.com/view/ark_article_cover/cut/public/3404269.jpg";
								c.content = "内容" + i;
								if (i % 2 == 0) {
									c.isFromOther = false;
								}
								list.add(c);

							}

							Message msg = handler.obtainMessage(
									ConstantManager.SUCCESS_1, list);
							handler.sendMessage(msg);
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
						}
					}), context);

		}
	}

	/**
	 * 发送聊天信息
	 * 
	 * @param what
	 * @param map
	 * @param context
	 * @param handler
	 */
	public static void addSingleChatInfo(final HashMap<String, Object> map,
			Context context, final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			// handler.sendEmptyMessage(ConstantManager.LOADING);
			makeUrl("user/add_single_chat.do", map);
			addRequest(new StringRequest("http://www.baidu.com",
					new Listener<String>() {
						@Override
						public void onResponse(String response) {
							LogManager.e(tag, response);

							Message msg = handler.obtainMessage(
									ConstantManager.SUCCESS_2,
									map.get("content"));
							handler.sendMessage(msg);
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
						}
					}), context);

		}
	}

	/**
	 * 获取挡圈群聊成员列表
	 * 
	 * @param map
	 * @param context
	 * @param handler
	 *            user/player/group_chat_detail.do?groupId=1&userId=1&count=100
	 */
	public static void getCurrGroupChatUserList(
			final HashMap<String, Object> map, Context context,
			final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			handler.sendEmptyMessage(ConstantManager.LOADING_1);
			makeUrl("user/player/group_chat_detail.do", map);
			addRequest(new StringRequest("http://www.baidu.com",
					new Listener<String>() {
						@Override
						public void onResponse(String response) {
							LogManager.e(tag, response);
							ArrayList<Player> list = new ArrayList<Player>();
							for (int i = 0; i < 30; i++) {
								Player p = new Player();
								p.playerId = "";
								p.playerProtrait = "http://img3.douban.com/view/ark_article_cover/cut/public/3404269.jpg";
								p.nickName = "name";
								p.playerPost = "1";
								list.add(p);
							}
							Message msg = handler.obtainMessage(
									ConstantManager.SUCCESS_1, list);
							handler.sendMessage(msg);
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
						}
					}), context);

		}
	}

	/**
	 * 获取我的球友信息
	 * 
	 * @param what
	 * @param map
	 * @param context
	 * @param handler
	 */
	public static void getMyGolfersItemInfo(final int what,
			final HashMap<String, Object> map, Context context,
			final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			// handler.sendEmptyMessage(ConstantManager.LOADING);
			addRequest(new StringRequest(
					makeUrl("get_my_glofers_item.do", map),
					new Listener<String>() {
						@Override
						public void onResponse(String response) {
							LogManager.e(tag, response);
							Message msg = handler.obtainMessage(what, response);
							handler.sendMessage(msg);
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
						}
					}), context);

		}
	}

	/**
	 * 获取客服信息
	 * 
	 * @param what
	 * @param map
	 * @param context
	 * @param handler
	 */
	public static void getServicerInfo(final int what,
			final HashMap<String, Object> map, Context context,
			final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			// handler.sendEmptyMessage(ConstantManager.LOADING);
			addRequest(new StringRequest(makeUrl("user/get_servicer.do", map),
					new Listener<String>() {
						@Override
						public void onResponse(String response) {

							Message msg = handler.obtainMessage(what, response);
							handler.sendMessage(msg);
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
						}
					}), context);

		}
	}

	/**
	 * 绑定微博
	 */
	public void bindWeibo() {
	}

	/**
	 * 解除微博绑定
	 * 
	 * @param map
	 * @param context
	 * @param handler
	 */
	public static void unBindWeibo(final HashMap<String, Object> map,
			Context context, final Handler handler) {
		if (isNetworkConnected(handler, context)) {
			handler.sendEmptyMessage(ConstantManager.LOADING_1);
			// makeUrl("user/un_bind.do", map)
			addRequest(new StringRequest("http://www.baidu.com",
					new Listener<String>() {
						@Override
						public void onResponse(String response) {
							// if (isResultNormal(response)) {
							handler.sendEmptyMessage(ConstantManager.SUCCESS_2);
							// } else {
							// Message msg = handler
							// .obtainMessage(
							// ConstantManager.FAIL_CODE_ERROR,
							// "解绑失败");
							// handler.sendMessage(msg);
							// }

						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
						}
					}), context);

		}
	}

	// ----------------工具类-------------------------
	// http://cua0702.oicp.net:88/blueball/visitor/upload_user_logo.do

	public static void sendRegisterUserInfo(String mob, String pwd,
			String nickName, String post, String sex, double lng, double lat,
			final File logo, final Handler handler) {
		try {
			AjaxParams params = new AjaxParams();
			params.put("mob", mob);
			params.put("pwd", pwd);
			params.put("nickName", nickName);
			params.put("post", post);
			params.put("sex", sex);
			params.put("lng", String.valueOf(lng));
			params.put("lat", String.valueOf(lat));
			params.put("logo", logo);
			FinalHttp fh = new FinalHttp();
			fh.post(makeUrl("visitor/regist.do", null), params,
					new AjaxCallBack<Object>() {
						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
							super.onFailure(t, errorNo, strMsg);
						}

						@Override
						public void onLoading(long count, long current) {
							handler.sendEmptyMessage(ConstantManager.LOADING_1);
							super.onLoading(count, current);
						}

						@Override
						public void onSuccess(Object t) {
							LogManager.e(tag, "注册" + t.toString());
							try {
								// 上传成功后。把文件中的所有文件都删除了
								if (isResultNormal(t.toString())) {
									String userid = new JSONObject(t.toString())
											.optJSONObject(DATA).optString(
													"userId");
									// {"code":"1001","msg":"","data":{"userId":"1082"}}

									Message msg = handler.obtainMessage(
											ConstantManager.SUCCESS_1, userid);
									handler.sendMessage(msg);
									File[] files = logo.getParentFile()
											.listFiles();
									for (File f : files) {
										f.delete();
									}
								} else {
									handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
								}
							} catch (Exception e) {
								handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
								e.printStackTrace();
							}

							super.onSuccess(t);
						}
					});
		} catch (Exception e) {
			handler.sendEmptyMessage(ConstantManager.FAIL_SERVER_ERROR);
			e.printStackTrace();
		}

	}

	/**
	 * 当网络访问
	 */

	private static void sendFailMsg(Handler handler, Object text) {

		Message msg = handler.obtainMessage(ConstantManager.FAIL_CODE_ERROR,
				text);
		handler.sendMessage(msg);
	}

	/**
	 * 解析
	 */
	private static String parsePlayerNumber(String number) {
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		sb.append(number);
		sb.append("/3");
		sb.append(")");
		return sb.toString();
	}

	/**
	 * 解析距离
	 */
	private static String parseDisatnce(String range) {
		double d = Double.parseDouble(range) / 1000;
		String s = String.valueOf(d);
		return s.substring(0, s.lastIndexOf(".") + 2) + "km";
	}

	/**
	 * 1=控球后卫 2=得分后卫 3=小前锋 4=大前锋 5=中锋
	 */

	private static String parsePost(String post) {
		if ("1".equals(post)) {
			return "控球后卫";
		} else if ("2".equals(post)) {
			return "得分后卫";
		} else if ("3".equals(post)) {
			return "小前锋";
		} else if ("4".equals(post)) {
			return "大前锋";
		} else if ("5".equals(post)) {
			return "中锋";
		} else {
			return "美国队长";
		}
	}

	public static String unParsePost(String post) {
		if ("控球后卫".equals(post)) {
			return "1";
		} else if ("得分后卫".equals(post)) {
			return "2";
		} else if ("小前锋".equals(post)) {
			return "3";
		} else if ("大前锋".equals(post)) {
			return "4";
		} else if ("中锋".equals(post)) {
			return "5";
		} else {
			return "-1";
		}
	}

	/**
	 * 通过type解析通知的类型
	 * 
	 * @param type
	 * @return
	 */
	public static String parseNotifyType(Resources rs, String type) {
		switch (Integer.parseInt(type)) {
		case ConstantManager.NOTIFY_TYPE_PK:
			return rs.getString(R.string.notify_type_pk);
		case ConstantManager.NOTIFY_TYPE_BUILD:
			return rs.getString(R.string.notify_type_build);
		case ConstantManager.NOTIFY_TYPE_APPLY:
			return rs.getString(R.string.notify_type_apply);
		case ConstantManager.NOTIFY_TYPE_ADD:
			return rs.getString(R.string.notify_type_add);
		default:
			return rs.getString(R.string.error);
		}
	}

	/**
	 * 解析通知的处理状态
	 * 
	 * @param status
	 * @return
	 */
	public static String parseNotifyHandleStatus(Resources rs, String status) {
		switch (Integer.parseInt(status)) {
		case ConstantManager.NOTIFY_STATUS_UNTREATED:
			return rs.getString(R.string.agree);
		case ConstantManager.NOTIFY_STATUS_TREATED:
			return rs.getString(R.string.agreed);
		default:
			return rs.getString(R.string.error);
		}

	}

	/**
	 * 性别解析
	 * 
	 * @param gender
	 * @return
	 */
	private static String parseGender(String gender) {
		if ("1".equals(gender)) {
			return "男";

		} else if ("2".equals(gender)) {
			return "女";
		} else {
			return "女博士";
		}
	}

	public static String unParseGender(String gender) {
		if ("男".equals(gender)) {
			return "1";

		} else if ("女".equals(gender)) {
			return "2";
		} else {
			return "-1";
		}
	}

	/**
	 * url
	 * 
	 * @return
	 */
	private static String makeUrl(String url, HashMap<String, Object> map) {
		StringBuffer sb = new StringBuffer(url);
		if (map != null) {
			sb.append("?");
			Set<String> keys = map.keySet();
			for (String key : keys) {
				sb.append(key + "=" + map.get(key) + "&");
			}
		}
		return URL_BASE + sb.toString();
	}

	/**
	 * 判断返回的json数据是否正常
	 * 
	 * @param arg0
	 * @return
	 */
	private static boolean isResultNormal(String response) {
		return response.contains("\"code\":\"1001\"");
	}

	/**
	 * 所有请求通过该方法添加到网络队列
	 * 
	 * @param request
	 * @param context
	 */
	private static RequestQueue mRequestQueue;

	private static void addRequest(Request request, Context context) {
		// 初始化请求，整个应用中使用
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(context);
		}
		mRequestQueue.add(request);
	}

	/**
	 * 封装网络提示
	 * 
	 * @param handler
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Handler handler, Context context) {
		boolean flag = isNetworkConnected(context);
		if (!flag) {
			handler.sendEmptyMessage(ConstantManager.FAIL_NO_NET);
		}
		return flag;
	}

	/**
	 * 检测网络是否可用
	 * 
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	// ----------------url start---------------------------

	private static final String URL_BASE = "http://211.149.188.193:8080/blueball/";
	// "http://cua0702.oicp.net:88/blueball/";
	private static final String URL_LOGIN = URL_BASE + "";
	// ----------------url end---------------------------
}
