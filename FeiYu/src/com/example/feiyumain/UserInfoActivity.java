package com.example.feiyumain;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.text.Layout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.data.DataInfo;
import com.example.feiyu.R;
import com.example.login.ForgetActivity;
import com.example.net.HttpRequest;
import com.example.userui.AboutFishActivity;
import com.example.userui.PayActivity;
import com.example.userui.PersonalActivity;

public class UserInfoActivity extends Activity implements OnClickListener {
	private TextView userinfo_name;
	private TextView userinfo_msgcount;
	private Button exit;
	private RelativeLayout changePwd;
	private RelativeLayout userinfo;
	private RelativeLayout pay;
	private RelativeLayout reward;
	private RelativeLayout about;
	public static UserInfoActivity userinfoActivity;
	private String account;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userinfo);
		userinfoActivity = this;
		userinfo_name = (TextView) findViewById(R.id.mine_usernameTV);
		//查询本地数据库
		account = DataInfo.DBHelper.first_account();
		userinfo_name.setText(account);
		//查询本地数据库
		userinfo_msgcount = (TextView) findViewById(R.id.mine_remainmessTV);
		String msgcount = DataInfo.DBHelper.query("msgcount", "name",
				account);
		userinfo_msgcount.setText(msgcount);
		
		exit = (Button) findViewById(R.id.exitButton);
		changePwd = (RelativeLayout) findViewById(R.id.changepwd_layout);
		userinfo = (RelativeLayout) findViewById(R.id.userinfo_layout);
		pay = (RelativeLayout) findViewById(R.id.pay_layout);
		reward = (RelativeLayout) findViewById(R.id.reward_layout);
		about = (RelativeLayout) findViewById(R.id.exchange_layout);

		exit.setOnClickListener(this);
		changePwd.setOnClickListener(this);
		userinfo.setOnClickListener(this);
		pay.setOnClickListener(this);
		reward.setOnClickListener(this);
		about.setOnClickListener(this);

		// 获取用户信息
		// 启动线程
		new Thread(runnable).start();

		// userinfo_name.setText(DataInfo.ACCOUNT);
		// userinfo_msgcount.setText(DataInfo.MSG_COUNT);
	}

	// handler接受消息回调方法
	private Callback callback = new Callback() {
		public boolean handleMessage(Message msg) {
			// 刷新
			if (msg.what == 0) {
				// 更新用户信息
				userinfo_name.setText(account);
				String msgcount = DataInfo.DBHelper.query("msgcount", "name",
						account);
				userinfo_msgcount.setText(msgcount);
			} else if (msg.what == 1) {
				Toast.makeText(getApplicationContext(), "网络连接有问题",
						Toast.LENGTH_SHORT).show();

			}
			return false;
		}
	};
	// 线程访问组件的中间代理
	private Handler handler = new Handler(callback);

	private Runnable runnable = new Runnable() {

		public void run() {
			while (true) {
				// 不能从线程访问界面上的组件
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
	private long exitTime = 0;
	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.exitButton) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出登录，下次请重新登录",
						Toast.LENGTH_SHORT).show();
				
				exitTime = System.currentTimeMillis();
			} else {
				DataInfo.DBHelper.userinfo_delete(account);
				finish();
				MainActivity.main_activity.finish();
				android.os.Process.killProcess(android.os.Process.myPid()); // 获取PID
				System.exit(0); // 常规java、c#的标准退出法，返回值为0代表正常退出
			}
			
			

		} else if (v.getId() == R.id.changepwd_layout) {
			DataInfo.ResetPwd = true;
			Intent in = new Intent(UserInfoActivity.this, ForgetActivity.class);
			startActivity(in);
		} else if (v.getId() == R.id.userinfo_layout) {

			Intent in = new Intent(UserInfoActivity.this,
					PersonalActivity.class);
			startActivity(in);

		} else if (v.getId() == R.id.pay_layout) {

			Intent intent = new Intent();
			intent.setClass(UserInfoActivity.this, PayActivity.class);
			startActivity(intent);

		} else if (v.getId() == R.id.reward_layout) {

		} else if (v.getId() == R.id.exchange_layout) {
			Intent intent = new Intent();
			intent.setClass(UserInfoActivity.this, AboutFishActivity.class);
			startActivity(intent);

		}

	}
}
