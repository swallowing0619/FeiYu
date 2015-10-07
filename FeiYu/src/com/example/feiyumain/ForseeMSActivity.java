package com.example.feiyumain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONException;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.data.DataInfo;
import com.example.feiyu.R;
import com.example.net.HttpRequest;

public class ForseeMSActivity extends Activity implements OnClickListener {
	private int id_ms;
	private EditText forseetxt;
	public ListView phoneList_forsee;
	public PhoneListAdapter adapter;
	private Button send;
	private Button send_timer;
	private Button editBt;
	private ImageButton returnBt;
	private TextView title;
	private ImageView title_image;
	private int flag = 2;

	private int now_hour;
	private int now_minute;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forseems);
		initView();
		send.setOnClickListener(this);
	}

	private void initView() {
		title = (TextView) this.findViewById(R.id.title_txt);
		// title_image = (ImageView) this.findViewById(R.id.title_image);
		title.setText("预览");

		forseetxt = (EditText) findViewById(R.id.foresee_text);
		send = (Button) findViewById(R.id.send);
		send_timer = (Button) findViewById(R.id.timer_send);
		editBt = (Button) findViewById(R.id.editBt);
		returnBt = (ImageButton) findViewById(R.id.returnBt);

		phoneList_forsee = (ListView) findViewById(R.id.phonelistViewforsee);
		forseetxt.setText("编号" + DataInfo.QuHao + "，" + DataInfo.MESSAGE);
		forseetxt.setFocusable(false);
		// 添加监听
		send.setOnClickListener(this);
		returnBt.setOnClickListener(this);
		editBt.setOnClickListener(this);
		send_timer.setOnClickListener(this);

		adapter = new PhoneListAdapter(flag, getApplicationContext());// 实例化适配器
		phoneList_forsee.setAdapter(adapter);// 绑定适配器
	}

	// handler接受消息回调方法
	private Callback callback = new Callback() {
		public boolean handleMessage(Message msg) {
			// 刷新
			if (msg.what == 0) {
				// 进入主界面
				if (DataInfo.SEND_type.equals("ALL_SEND")) {
					Toast.makeText(getApplicationContext(), "发送信息成功",
							Toast.LENGTH_SHORT).show();
					// DataInfo.listItem.clear();// 清空号码列表和预览信息
					// DataInfo.MESSAGE = "";
					// finish();
				} else if (DataInfo.SEND_type.equals("_SEND")) {
					Toast.makeText(getApplicationContext(), "部分号码发送失败,请查看失败记录",
							Toast.LENGTH_SHORT).show();
					// DataInfo.listItem.clear();
				}
			}
			if (msg.what == 1) {
				if (DataInfo.ERR_CODE.equals("6"))
					Toast.makeText(getApplicationContext(), "余额不足，请先充值",
							Toast.LENGTH_SHORT).show();

			} else if (msg.what == 2) {
				Toast.makeText(getApplicationContext(), "网络连接有问题",
						Toast.LENGTH_SHORT).show();

			} else if (msg.what == 3) {

				// 进入主界面
//				if (DataInfo.SEND_type.equals("ALL_SEND")) {
					Toast.makeText(getApplicationContext(), "定时信息发送成功",
							Toast.LENGTH_SHORT).show();
					// DataInfo.listItem.clear();// 清空号码列表和预览信息
					// DataInfo.MESSAGE = "";
					// finish();
//				}
				// else if (DataInfo.SEND_type.equals("_SEND")) {
				// Toast.makeText(getApplicationContext(), "部分号码发送失败,请查看失败记录",
				// Toast.LENGTH_SHORT).show();
				// // DataInfo.listItem.clear();
				// }
			}
			return false;
		}
	};
	// 线程访问组件的中间代理
	private Handler handler = new Handler(callback);

	// 定时发送的线程
	private Runnable RegularlySendrunnable = new Runnable() {

		public void run() {
			while (true) {
				// 不能从线程访问界面上的组件
				try {
					HttpRequest httprequest = new HttpRequest();
					if (DataInfo.listItem.size() <= 3) {
						DataInfo.sendlist = DataInfo.listItem;
						// 定时发送
						boolean flag = httprequest.Regularly_sendMessage(
								forseetxt.getText().toString(),
								DataInfo.TIMER_HOUR, DataInfo.TIMER_MINUTE);
						Thread.sleep(100);
						if (flag) {
							// System.out.println("SEND OK");
							handler.sendEmptyMessage(3);
							break;
						} else {
							System.out.println("fail");
							if (DataInfo.Request_diable) {
								handler.sendEmptyMessage(2);
							} else {
								handler.sendEmptyMessage(1);
							}
							break;
						}
					} else {
						DataInfo.Group = DataInfo.listItem.size() / 3 + 1;
						DataInfo.sendlist = new ArrayList<HashMap<String, Object>>();
						boolean send_flag = true;
						// 按组发送
						for (int j = 0; j < DataInfo.Group; j++) {
							if (DataInfo.listItem.size() >= 3) {
								for (int i = 0; i < 3; i++) {
									DataInfo.sendlist.add(DataInfo.listItem
											.get(i));
								}
								for (int i = 0; i < 3; i++) {
									DataInfo.listItem.remove(2 - i);
								}
							} else {
								for (int i = 0; i < DataInfo.listItem.size(); i++) {
									DataInfo.sendlist.add(DataInfo.listItem
											.get(i));
								}
								DataInfo.listItem.clear();
							}
							// 发送定时短信
							boolean flag = httprequest.Regularly_sendMessage(
									forseetxt.getText().toString(),
									DataInfo.TIMER_HOUR, DataInfo.TIMER_MINUTE);
							if (!flag) {
								send_flag = false;
								// System.out.println("有发送失败");
							}
							Thread.sleep(1000);
							DataInfo.sendlist.clear();
						}

						if (send_flag) {
							handler.sendEmptyMessage(3);
							break;
						} else {
							System.out.println("fail");
							if (DataInfo.Request_diable) {
								handler.sendEmptyMessage(2);
							} else {
								handler.sendEmptyMessage(1);
							}
							break;
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}

	};

	// 立即发送的线程
	private Runnable runnable = new Runnable() {

		public void run() {
			while (true) {
				// 不能从线程访问界面上的组件
				try {
					HttpRequest httprequest = new HttpRequest();
					if (DataInfo.listItem.size() <= 4) {
						DataInfo.sendlist = DataInfo.listItem;
						boolean flag = httprequest.sendMessage(forseetxt
								.getText().toString());
						Thread.sleep(100);
						if (flag) {
							// System.out.println("SEND OK");
							handler.sendEmptyMessage(0);
							break;
						} else {
							System.out.println("fail");
							if (DataInfo.Request_diable) {
								handler.sendEmptyMessage(2);
							} else {
								handler.sendEmptyMessage(1);
							}
							break;
						}
					} else {
						DataInfo.Group = DataInfo.listItem.size() / 4 + 1;
						DataInfo.sendlist = new ArrayList<HashMap<String, Object>>();
						boolean send_flag = true;
						// 按组发送
						for (int j = 0; j < DataInfo.Group; j++) {
							if (DataInfo.listItem.size() >= 4) {
								for (int i = 0; i < 4; i++) {
									DataInfo.sendlist.add(DataInfo.listItem
											.get(i));
								}
								for (int i = 0; i < 4; i++) {
									DataInfo.listItem.remove(3 - i);
								}
							} else {
								for (int i = 0; i < DataInfo.listItem.size(); i++) {
									DataInfo.sendlist.add(DataInfo.listItem
											.get(i));
								}
								DataInfo.listItem.clear();
							}
							// 发送
							boolean flag = httprequest.sendMessage(forseetxt
									.getText().toString());
							if (!flag) {
								send_flag = false;
								System.out.println("有发送失败");
							}
							Thread.sleep(100);
							DataInfo.sendlist.clear();
						}

						if (send_flag) {
							handler.sendEmptyMessage(0);
							break;
						} else {
							System.out.println("fail");
							if (DataInfo.Request_diable) {
								handler.sendEmptyMessage(2);
							} else {
								handler.sendEmptyMessage(1);
							}
							break;
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}

	};

	@Override
	public void onClick(View v) {
		// 发送短信
		if (v.getId() == R.id.send) {
			
			// 发送消息和手机号
			Toast.makeText(getApplicationContext(), "信息发送中", Toast.LENGTH_SHORT)
					.show();
			// 启动线程
			new Thread(runnable).start();
			this.finish();
			EnterPhoneActivity.enterphone_activity.finish();
			// }

		} else if (v.getId() == R.id.returnBt) {
			this.finish();
		} else if (v.getId() == R.id.editBt) {
			// 输入框重新获取焦点
			forseetxt.setFocusable(true);
			forseetxt.setFocusableInTouchMode(true);
			forseetxt.requestFocus();
			forseetxt.requestFocusFromTouch();
		} else if (v.getId() == R.id.timer_send) {
			// 定时发送
			// 设定时间

			Calendar calendar = Calendar.getInstance();
			int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			now_hour = hourOfDay;
			now_minute = minute;

			TimePickerDialog timePickerDialog = new TimePickerDialog(
					ForseeMSActivity.this,
					new TimePickerDialog.OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							int sethour = hourOfDay;
							int setminute = minute;

							if(sethour < now_hour || (sethour == now_hour && setminute < now_minute)) {
								Toast.makeText(getApplicationContext(),
										"设定时间早于当前时间，请重设", Toast.LENGTH_SHORT)
										.show();
							} else {
								 System.out.println("Time: " + sethour + ":" +
								 setminute);
								DataInfo.TIMER_HOUR = sethour + "";
								DataInfo.TIMER_MINUTE = "" + setminute;
								Toast.makeText(getApplicationContext(),
										"正在发送定时信息", Toast.LENGTH_SHORT).show();
								// 启动线程
								new Thread(RegularlySendrunnable).start();
								ForseeMSActivity.this.finish();
								EnterPhoneActivity.enterphone_activity.finish();
							}
						}
					}, hourOfDay, minute, true);
			timePickerDialog.show();

		}

	}

}
