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
		// ȥ������
		setContentView(R.layout.activity_register);

		registerBtn = (Button) findViewById(R.id.registerBtn);
		identifyBtn = (Button) findViewById(R.id.register_identifyBtn);
		identifyEditText = (EditText) findViewById(R.id.register_identifyEditText);
		passwordEditview = (EditText) findViewById(R.id.register_passwordEditview);
		accountEditview = (EditText) findViewById(R.id.register_accountEditview);

		identifyBtn.setOnClickListener(this);
		// ע�ᰴť����
		registerBtn.setOnClickListener(this);

	}

	// handler������Ϣ�ص�����
	private Callback callback = new Callback() {
		public boolean handleMessage(Message msg) {
			// ˢ��
			if (msg.what == 0) {
				// �ص���¼����
				Toast.makeText(getApplicationContext(), "��֤���ѷ���",
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 1) {
				Toast.makeText(getApplicationContext(), "�û����������ȼ��",
						Toast.LENGTH_SHORT).show();
//				identifyBtn.setText("��ȡ��֤��");
				// �����ÿ�
				accountEditview.setText("");
			} else if (msg.what == 2) {
				// �ص���¼����
				Toast.makeText(getApplicationContext(), "ע��ɹ�",
						Toast.LENGTH_SHORT).show();
				finish();
			} else if (msg.what == 3) {
				Toast.makeText(getApplicationContext(), "�û����Ѵ��ڻ���֤�����",
						Toast.LENGTH_SHORT).show();
				// �����ÿ�
				// finish();

			} else if (msg.what == 4) {
				Toast.makeText(getApplicationContext(), "�������Ӳ�����",
						Toast.LENGTH_SHORT).show();
			}else if(msg.what == 5){//��֤�뷢�͵���ʱ
				identifyBtn.setText("����ʱ "+ms + "s");
			}else if(msg.what == 6){//��֤�밴ť����
				identifyBtn.setText("��ȡ��֤��");
				ms = 60;
			}
			return false;
		}
	};
	// �̷߳���������м����
	private Handler handler = new Handler(callback);

	private class RegisterRunnale implements Runnable {

		@Override
		public void run() {
			while (true) {
				// ���ܴ��̷߳��ʽ����ϵ����
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
				// ���ܴ��̷߳��ʽ����ϵ����
				try {
					HttpRequest httprequest = new HttpRequest();
					System.out.println("���ͷ���������");
					DataInfo.forget_identify  = false;//��ע�����֤�뷢��
					
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
				// ע��
				// ��������������ӵ��߳�
				Toast.makeText(getApplicationContext(), "ע��",
						Toast.LENGTH_SHORT).show();
				RegisterRunnale regrunnale = new RegisterRunnale();
				new Thread(regrunnale).start();
			} else {
				Toast.makeText(getApplicationContext(), "����������֤��",
						Toast.LENGTH_SHORT).show();
			}
		}

		else if (v.getId() == R.id.register_identifyBtn) {
			// ������֤��
			if ((accountEditview.getText().toString()).equals("")) {
				Toast.makeText(getApplicationContext(), "���������û���",
						Toast.LENGTH_SHORT).show();
			} else {
				 if (ms == 60) {
						Toast.makeText(getApplicationContext(), "��֤�뷢����",
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
