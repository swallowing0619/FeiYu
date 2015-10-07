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
		title.setText("Ԥ��");

		forseetxt = (EditText) findViewById(R.id.foresee_text);
		send = (Button) findViewById(R.id.send);
		send_timer = (Button) findViewById(R.id.timer_send);
		editBt = (Button) findViewById(R.id.editBt);
		returnBt = (ImageButton) findViewById(R.id.returnBt);

		phoneList_forsee = (ListView) findViewById(R.id.phonelistViewforsee);
		forseetxt.setText("���" + DataInfo.QuHao + "��" + DataInfo.MESSAGE);
		forseetxt.setFocusable(false);
		// ��Ӽ���
		send.setOnClickListener(this);
		returnBt.setOnClickListener(this);
		editBt.setOnClickListener(this);
		send_timer.setOnClickListener(this);

		adapter = new PhoneListAdapter(flag, getApplicationContext());// ʵ����������
		phoneList_forsee.setAdapter(adapter);// ��������
	}

	// handler������Ϣ�ص�����
	private Callback callback = new Callback() {
		public boolean handleMessage(Message msg) {
			// ˢ��
			if (msg.what == 0) {
				// ����������
				if (DataInfo.SEND_type.equals("ALL_SEND")) {
					Toast.makeText(getApplicationContext(), "������Ϣ�ɹ�",
							Toast.LENGTH_SHORT).show();
					// DataInfo.listItem.clear();// ��պ����б��Ԥ����Ϣ
					// DataInfo.MESSAGE = "";
					// finish();
				} else if (DataInfo.SEND_type.equals("_SEND")) {
					Toast.makeText(getApplicationContext(), "���ֺ��뷢��ʧ��,��鿴ʧ�ܼ�¼",
							Toast.LENGTH_SHORT).show();
					// DataInfo.listItem.clear();
				}
			}
			if (msg.what == 1) {
				if (DataInfo.ERR_CODE.equals("6"))
					Toast.makeText(getApplicationContext(), "���㣬���ȳ�ֵ",
							Toast.LENGTH_SHORT).show();

			} else if (msg.what == 2) {
				Toast.makeText(getApplicationContext(), "��������������",
						Toast.LENGTH_SHORT).show();

			} else if (msg.what == 3) {

				// ����������
//				if (DataInfo.SEND_type.equals("ALL_SEND")) {
					Toast.makeText(getApplicationContext(), "��ʱ��Ϣ���ͳɹ�",
							Toast.LENGTH_SHORT).show();
					// DataInfo.listItem.clear();// ��պ����б��Ԥ����Ϣ
					// DataInfo.MESSAGE = "";
					// finish();
//				}
				// else if (DataInfo.SEND_type.equals("_SEND")) {
				// Toast.makeText(getApplicationContext(), "���ֺ��뷢��ʧ��,��鿴ʧ�ܼ�¼",
				// Toast.LENGTH_SHORT).show();
				// // DataInfo.listItem.clear();
				// }
			}
			return false;
		}
	};
	// �̷߳���������м����
	private Handler handler = new Handler(callback);

	// ��ʱ���͵��߳�
	private Runnable RegularlySendrunnable = new Runnable() {

		public void run() {
			while (true) {
				// ���ܴ��̷߳��ʽ����ϵ����
				try {
					HttpRequest httprequest = new HttpRequest();
					if (DataInfo.listItem.size() <= 3) {
						DataInfo.sendlist = DataInfo.listItem;
						// ��ʱ����
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
						// ���鷢��
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
							// ���Ͷ�ʱ����
							boolean flag = httprequest.Regularly_sendMessage(
									forseetxt.getText().toString(),
									DataInfo.TIMER_HOUR, DataInfo.TIMER_MINUTE);
							if (!flag) {
								send_flag = false;
								// System.out.println("�з���ʧ��");
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

	// �������͵��߳�
	private Runnable runnable = new Runnable() {

		public void run() {
			while (true) {
				// ���ܴ��̷߳��ʽ����ϵ����
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
						// ���鷢��
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
							// ����
							boolean flag = httprequest.sendMessage(forseetxt
									.getText().toString());
							if (!flag) {
								send_flag = false;
								System.out.println("�з���ʧ��");
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
		// ���Ͷ���
		if (v.getId() == R.id.send) {
			
			// ������Ϣ���ֻ���
			Toast.makeText(getApplicationContext(), "��Ϣ������", Toast.LENGTH_SHORT)
					.show();
			// �����߳�
			new Thread(runnable).start();
			this.finish();
			EnterPhoneActivity.enterphone_activity.finish();
			// }

		} else if (v.getId() == R.id.returnBt) {
			this.finish();
		} else if (v.getId() == R.id.editBt) {
			// ��������»�ȡ����
			forseetxt.setFocusable(true);
			forseetxt.setFocusableInTouchMode(true);
			forseetxt.requestFocus();
			forseetxt.requestFocusFromTouch();
		} else if (v.getId() == R.id.timer_send) {
			// ��ʱ����
			// �趨ʱ��

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
										"�趨ʱ�����ڵ�ǰʱ�䣬������", Toast.LENGTH_SHORT)
										.show();
							} else {
								 System.out.println("Time: " + sethour + ":" +
								 setminute);
								DataInfo.TIMER_HOUR = sethour + "";
								DataInfo.TIMER_MINUTE = "" + setminute;
								Toast.makeText(getApplicationContext(),
										"���ڷ��Ͷ�ʱ��Ϣ", Toast.LENGTH_SHORT).show();
								// �����߳�
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
