package com.example.login;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.data.DataInfo;
import com.example.feiyu.R;
import com.example.feiyumain.MainActivity;
import com.example.net.HttpRequest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener {
	private Button registerBtn;
	private Button identifyBtn;
	private EditText identifyEditText;
	private EditText passwordEditview;
	private EditText accountEditview;
	private long exitTime = 0;
	private boolean flag = true;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除标题
		setContentView(R.layout.activity_register);

		registerBtn = (Button) findViewById(R.id.registerBtn);
		identifyBtn = (Button) findViewById(R.id.register_identifyBtn);
		identifyEditText = (EditText) findViewById(R.id.register_identifyEditText);
		passwordEditview = (EditText) findViewById(R.id.register_passwordEditview);
		accountEditview = (EditText) findViewById(R.id.register_accountEditview);

		identifyBtn.setOnClickListener(this);
		// 注册按钮监听
		registerBtn.setOnClickListener(this);

	}

	// handler接受消息回调方法
	private Callback callback = new Callback() {
		public boolean handleMessage(Message msg) {
			// 刷新
			if (msg.what == 0) {
				// 回到登录界面
				Toast.makeText(getApplicationContext(), "验证码已发送",
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 1) {
				Toast.makeText(getApplicationContext(), "用户名有误，请先检查",
						Toast.LENGTH_SHORT).show();
//				identifyBtn.setText("获取验证码");
				// 密码置空
				accountEditview.setText("");
			} else if (msg.what == 2) {
				// 回到登录界面
				Toast.makeText(getApplicationContext(), "注册成功",
						Toast.LENGTH_SHORT).show();
				finish();
			} else if (msg.what == 3) {
				Toast.makeText(getApplicationContext(), "用户名已存在或验证码错误",
						Toast.LENGTH_SHORT).show();
				// 密码置空
				// finish();

			} else if (msg.what == 4) {
				Toast.makeText(getApplicationContext(), "网络连接不可用",
						Toast.LENGTH_SHORT).show();
			}else if(msg.what == 5){//验证码发送倒计时
				identifyBtn.setText("倒计时 "+ms + "s");
			}else if(msg.what == 6){//验证码按钮设置
				identifyBtn.setText("获取验证码");
				ms = 60;
			}
			return false;
		}
	};
	// 线程访问组件的中间代理
	private Handler handler = new Handler(callback);

	private class RegisterRunnale implements Runnable {

		@Override
		public void run() {
			while (true) {
				// 不能从线程访问界面上的组件
				try {
					HttpRequest httprequest = new HttpRequest();
					boolean flag = httprequest.register(accountEditview
							.getText().toString(), passwordEditview.getText()
							.toString(), identifyEditText.getText().toString());
					Thread.sleep(100);
					if (flag) {
						System.out.println("OK");
						handler.sendEmptyMessage(2);
						break;
					} else {
						System.out.println("fail");
						if (DataInfo.Request_diable) {
							handler.sendEmptyMessage(4);
						} else {
							handler.sendEmptyMessage(3);
						}
						break;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

		}

	}

	private class IdentifyRunnale implements Runnable {

		@Override
		public void run() {
			while (true) {
				// 不能从线程访问界面上的组件
				try {
					HttpRequest httprequest = new HttpRequest();
					System.out.println("发送服务器请求");
					DataInfo.forget_identify  = false;//非注册的验证码发送
					
					boolean flag = httprequest.getIdentify(accountEditview
							.getText().toString());
					Thread.sleep(100);
					if (flag) {
						System.out.println("OK");
						handler.sendEmptyMessage(0);
						break;
					} else {
						System.out.println("fail");
						if (DataInfo.Request_diable) {
							handler.sendEmptyMessage(4);
						} else {
							handler.sendEmptyMessage(1);
						}
						break;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.registerBtn) {
			if (!(identifyEditText.getText().toString()).equals("")) {
				// 注册
				// 启动向服务器连接的线程
				Toast.makeText(getApplicationContext(), "注册",
						Toast.LENGTH_SHORT).show();
				RegisterRunnale regrunnale = new RegisterRunnale();
				new Thread(regrunnale).start();
			} else {
				Toast.makeText(getApplicationContext(), "请先输入验证码",
						Toast.LENGTH_SHORT).show();
			}
		}

		else if (v.getId() == R.id.register_identifyBtn) {
			// 发送验证码
			if ((accountEditview.getText().toString()).equals("")) {
				Toast.makeText(getApplicationContext(), "请先输入用户名",
						Toast.LENGTH_SHORT).show();
			} else {
				 if (ms == 60) {
						Toast.makeText(getApplicationContext(), "验证码发送中",
								Toast.LENGTH_SHORT).show();
						Timerunnable timerunnale = new Timerunnable();
						new Thread(timerunnale).start();
						 IdentifyRunnale irunnale = new IdentifyRunnale();
						 new Thread(irunnale).start();
						 }
				
				
			}

		}

	}
	
	private int ms = 60;
	
	private class Timerunnable implements Runnable {

		public void run() {
			exitTime = System.currentTimeMillis();
			
			while (true) {
				if (System.currentTimeMillis() - exitTime >= 1000 && ms > 0) {
//					System.out.println("exitTime1"+exitTime);
					ms--;
					exitTime = System.currentTimeMillis();
					handler.sendEmptyMessage(5);
					continue;
				}
				else if(ms==0){
					ms = 0;
					handler.sendEmptyMessage(6);
//					identify_flag = true;
					System.out.println("break");
					return;
				}
				
			}
			
			
			
		}
	}
}
