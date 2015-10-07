package com.example.login;

import java.io.IOException;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.data.DataInfo;
import com.example.feiyu.R;
import com.example.feiyumain.MainActivity;
import com.example.net.HttpRequest;

public class LoginActivity extends Activity implements OnClickListener {
	private Button registerButton;
	private Button enterButton;
	private Button forgetButton;
	private EditText login_account;
	private EditText login_password;
	public static LoginActivity loginactivity;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		loginactivity = this;

		if (DataInfo.DBHelper == null) {
			DataInfo.DBHelper = new com.example.data.DatabaseHelper(
					getApplicationContext());
		}

		// 获取登录控件
		login_account = (EditText) findViewById(R.id.loginaccount);
		login_password = (EditText) findViewById(R.id.loginpassword);

//		int account_sum = DataInfo.DBHelper.query_sum();
//		System.out.println("account_sum"+account_sum);
//		if (account_sum > 0) {
//			String account = DataInfo.DBHelper.first_account();
//			System.out.println("name"+account);
//			String pwd = DataInfo.DBHelper.query("pwd", "name", account);
//			login_account.setText(account);
//			login_password.setText(pwd);
//		}

		registerButton = (Button) findViewById(R.id.registerBtn);
		enterButton = (Button) findViewById(R.id.enterButton);
		forgetButton = (Button) findViewById(R.id.forgetButton);
		// 监听
		forgetButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);
		enterButton.setOnClickListener(this);

	}

	// handler接受消息回调方法
	private Callback callback = new Callback() {
		public boolean handleMessage(Message msg) {
			// 刷新
			if (msg.what == 0) {
				// 进入主界面
				Toast.makeText(getApplicationContext(), "登录成功",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				
				LoadingActivity.loadingactivity.finish();
				finish();
				LoginActivity.this.startActivity(intent);

			} else if (msg.what == 1) {
				Toast.makeText(getApplicationContext(), "用户名或密码不正确，请重新输入",
						Toast.LENGTH_SHORT).show();
				// 密码置空
				login_password.setText("");
				login_account.setText("");
				LoadingActivity.loadingactivity.finish();
			}

			else if (msg.what == 2) {
				Toast.makeText(getApplicationContext(), "网络连接不可用", 3000).show();
				LoadingActivity.loadingactivity.finish();
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
					// 打开等待界面
					Intent intent = new Intent(LoginActivity.this,
							LoadingActivity.class);
					LoginActivity.this.startActivity(intent);

					HttpRequest httprequest = new HttpRequest();
					boolean flag = httprequest.login(login_account.getText()
							.toString(), login_password.getText().toString());

					Thread.sleep(100);
					if (flag) {
						System.out.println("OK");
						handler.sendEmptyMessage(0);
						break;
					} else {
						System.out.println("fail");
						if (DataInfo.Request_diable) {
							System.out.println("网络连接不可用");
							handler.sendEmptyMessage(2);
						} else {
							handler.sendEmptyMessage(1);
						}
						break;
					}
				} catch (JSONException | IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}

	};

	private long loginTime = 0;

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.registerBtn) { // 进入注册界面
			Intent intent = new Intent(LoginActivity.this,
					RegisterActivity.class);
			LoginActivity.this.startActivity(intent);
		}
		// 账号登录
		else if (v.getId() == R.id.enterButton) {
			System.out.println("登录！！！！！！");
			// 启动向服务器连接的线程
			loginTime = System.currentTimeMillis();
			new Thread(runnable).start();


		} else if (v.getId() == R.id.forgetButton) { // 进入忘记密码界面
			Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
			LoginActivity.this.startActivity(intent);

		}
	}

}
