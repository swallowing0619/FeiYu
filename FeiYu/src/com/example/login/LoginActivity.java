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

		// ��ȡ��¼�ؼ�
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
		// ����
		forgetButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);
		enterButton.setOnClickListener(this);

	}

	// handler������Ϣ�ص�����
	private Callback callback = new Callback() {
		public boolean handleMessage(Message msg) {
			// ˢ��
			if (msg.what == 0) {
				// ����������
				Toast.makeText(getApplicationContext(), "��¼�ɹ�",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				
				LoadingActivity.loadingactivity.finish();
				finish();
				LoginActivity.this.startActivity(intent);

			} else if (msg.what == 1) {
				Toast.makeText(getApplicationContext(), "�û��������벻��ȷ������������",
						Toast.LENGTH_SHORT).show();
				// �����ÿ�
				login_password.setText("");
				login_account.setText("");
				LoadingActivity.loadingactivity.finish();
			}

			else if (msg.what == 2) {
				Toast.makeText(getApplicationContext(), "�������Ӳ�����", 3000).show();
				LoadingActivity.loadingactivity.finish();
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
					// �򿪵ȴ�����
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
							System.out.println("�������Ӳ�����");
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
		if (v.getId() == R.id.registerBtn) { // ����ע�����
			Intent intent = new Intent(LoginActivity.this,
					RegisterActivity.class);
			LoginActivity.this.startActivity(intent);
		}
		// �˺ŵ�¼
		else if (v.getId() == R.id.enterButton) {
			System.out.println("��¼������������");
			// ��������������ӵ��߳�
			loginTime = System.currentTimeMillis();
			new Thread(runnable).start();


		} else if (v.getId() == R.id.forgetButton) { // ���������������
			Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
			LoginActivity.this.startActivity(intent);

		}
	}

}
