package com.example.login;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.data.DataInfo;
import com.example.feiyu.R;
import com.example.feiyumain.MainActivity;
import com.example.feiyumain.UserInfoActivity;
import com.example.net.HttpRequest;

public class ForgetActivity extends Activity implements OnClickListener {
	private Button ensureButton;
	private Button identifyBtn;
	private EditText identifyEditText;
	private EditText passwordEditview;
	private EditText accountEditview;
	private long exitTime = 0;
	private boolean flag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_forget);

		ensureButton = (Button) findViewById(R.id.forget_ensureBtn);
		identifyBtn = (Button) findViewById(R.id.forget_identifyBtn);
		identifyEditText = (EditText) findViewById(R.id.forget_identifyEditText);
		passwordEditview = (EditText) findViewById(R.id.forget_passwordEditview);
		accountEditview = (EditText) findViewById(R.id.forget_accountEditview);

		// 重置密码按钮监听
		ensureButton.setOnClickListener(this);
		// 获取验证码监听
		identifyBtn.setOnClickListener(this);
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
				Toast.makeText(getApplicationContext(), "用户名不存在，请先检查",
						Toast.LENGTH_SHORT).show();
				// 密码置空
//				identifyBtn.setText("获取验证码");
				accountEditview.setText("");
			} else if (msg.what == 2) {
				// 回到登录界面
				Toast.makeText(getApplicationContext(), "修改密码成功",
						Toast.LENGTH_SHORT).show();
				finish();
				//若属于个人信息中的修改密码
				if (DataInfo.ResetPwd) {
					DataInfo.ResetPwd = false;
					Intent in = new Intent(ForgetActivity.this,
							LoginActivity.class);
					UserInfoActivity.userinfoActivity.finish();
					MainActivity.main_activity.finish();
					startActivity(in);

				}
			} else if (msg.what == 3) {
				Toast.makeText(getApplicationContext(), "用户名不存在，请先注册",
						Toast.LENGTH_SHORT).show();
				// 密码置空
				accountEditview.setText("");
				identifyEditText.setText("");
				passwordEditview.setText("");

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

	private class ForgetRunnale implements Runnable {

		@Override
		public void run() {
			while (true) {
				// 不能从线程访问界面上的组件
				try {
					HttpRequest httprequest = new HttpRequest();
					boolean flag = httprequest.forgetPwd(accountEditview
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
				} catch (JSONException e) {
					// TODO Auto-generated catch block
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
					DataInfo.forget_identify  = true;//标记为忘记密码的发送验证码
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
		if (v.getId() == R.id.forget_ensureBtn) {
			if (identifyEditText.getText().toString().equals("")) {
				Toast.makeText(getApplicationContext(), "请先输入验证码",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "修改密码",
						Toast.LENGTH_SHORT).show();
				ForgetRunnale regrunnale = new ForgetRunnale();
				new Thread(regrunnale).start();

			}

		}

		else if (v.getId() == R.id.forget_identifyBtn) {
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

				// if ((System.currentTimeMillis() - exitTime) < 30000) {
				// Toast.makeText(getApplicationContext(), "请等待验证码的发送",
				// Toast.LENGTH_SHORT).show();
				// flag = false;
				// exitTime = System.currentTimeMillis();
				// } else {
				// flag = true;
				// }
			}
		}

	}
//	private boolean identify_flag = true;
	private int ms = 60;
	
	private class Timerunnable implements Runnable {

		public void run() {
			exitTime = System.currentTimeMillis();
//			identify_flag = false;
			
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