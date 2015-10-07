package com.example.feiyumain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.data.DataInfo;
import com.example.feiyu.R;
import com.example.login.LoginActivity;
import com.example.net.HttpRequest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ImageButton editMS;
	private ImageButton MSLibrary;
	private ImageButton enterPhone;
	private ImageButton userInfo;
	private ImageButton lookupmess;
	public static MainActivity main_activity;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		creatDatabase();// �������ݿ�
		// ��ȡ�ؼ�
		accessControl();
		// ��Ӽ���
		addListener();
		main_activity = this;

	}

	private void creatDatabase() {
		if (DataInfo.DBHelper == null) {
			DataInfo.DBHelper = new com.example.data.DatabaseHelper(
					getApplicationContext());
		}
		if (DataInfo.listItem == null)
			DataInfo.listItem = new ArrayList<HashMap<String, Object>>();
	}

	// ��ȡ�ؼ�
	private void accessControl() {
		editMS = (ImageButton) findViewById(R.id.editmessBtn);
		MSLibrary = (ImageButton) findViewById(R.id.messlibBtn);
		enterPhone = (ImageButton) findViewById(R.id.enternumBtn);
		userInfo = (ImageButton) findViewById(R.id.userinfoBtn);
		lookupmess = (ImageButton) findViewById(R.id.lookuPmessBT);
	}

	// ��Ӽ���
	private void addListener() {
		MyOnClickListener ml = new MyOnClickListener();
		editMS.setOnClickListener(ml);
		MSLibrary.setOnClickListener(ml);
		enterPhone.setOnClickListener(ml);
		userInfo.setOnClickListener(ml);
		lookupmess.setOnClickListener(ml);
	}

	// handler������Ϣ�ص�����
	private Callback callback = new Callback() {
		public boolean handleMessage(Message msg) {
			// ˢ��
			if (msg.what == 0) {
				String account = DataInfo.DBHelper.first_account();
				String msgcount = DataInfo.DBHelper.query("msgcount", "name",
						account);
				Toast.makeText(getApplicationContext(),
						"���Ķ���ʣ�ࣺ" + msgcount + "��", 5000).show();
			}
			if (msg.what == 1) {
				Toast.makeText(getApplicationContext(), "��������������",
						Toast.LENGTH_SHORT).show();

			}
			return false;
		}
	};
	// �̷߳���������м����
	private Handler handler = new Handler(callback);

	private Runnable runnable = new Runnable() {

		public void run() {
			while (true) {
				// ���ܴ��̷߳��ʽ����ϵ����
				try {
					HttpRequest httprequest = new HttpRequest();
					boolean flag = httprequest.getUserInfo();
					Thread.sleep(100);
					if (flag) {
						System.out.println("OK");
						handler.sendEmptyMessage(0);
						break;
					} else {
						System.out.println("fail");
						if (DataInfo.Request_diable) {
							handler.sendEmptyMessage(1);
						}
						break;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}

	};

	private class MyOnClickListener implements OnClickListener {

		public void onClick(View v) {
			Intent intent = new Intent();

			if (v.getId() == R.id.editmessBtn) {
				intent.setClass(MainActivity.this, SlideActivity.class);
				intent.putExtra("index", "0");
				startActivity(intent);

			} else if (v.getId() == R.id.messlibBtn) {
				intent.setClass(MainActivity.this, SlideActivity.class);
				intent.putExtra("index", "2");
				startActivity(intent);

			} else if (v.getId() == R.id.enternumBtn) {
				intent.setClass(MainActivity.this, SlideActivity.class);
				intent.putExtra("index", "1");
				startActivity(intent);

			} else if (v.getId() == R.id.userinfoBtn) {
				intent.setClass(MainActivity.this, SlideActivity.class);
				intent.putExtra("index", "3");
				startActivity(intent);
			} else if (v.getId() == R.id.lookuPmessBT) {
				Toast.makeText(getApplicationContext(), "�û���Ϣ��ѯ��",
						Toast.LENGTH_SHORT).show();
				// �����߳�
				new Thread(runnable).start();

			}
		}
	}

	private long exitTime = 0;

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {

				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
