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
		// ȥ������
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_forget);

		ensureButton = (Button) findViewById(R.id.forget_ensureBtn);
		identifyBtn = (Button) findViewById(R.id.forget_identifyBtn);
		identifyEditText = (EditText) findViewById(R.id.forget_identifyEditText);
		passwordEditview = (EditText) findViewById(R.id.forget_passwordEditview);
		accountEditview = (EditText) findViewById(R.id.forget_accountEditview);

		// �������밴ť����
		ensureButton.setOnClickListener(this);
		// ��ȡ��֤�����
		identifyBtn.setOnClickListener(this);
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
				Toast.makeText(getApplicationContext(), "�û��������ڣ����ȼ��",
						Toast.LENGTH_SHORT).show();
				// �����ÿ�
//				identifyBtn.setText("��ȡ��֤��");
				accountEditview.setText("");
			} else if (msg.what == 2) {
				// �ص���¼����
				Toast.makeText(getApplicationContext(), "�޸�����ɹ�",
						Toast.LENGTH_SHORT).show();
				finish();
				//�����ڸ�����Ϣ�е��޸�����
				if (DataInfo.ResetPwd) {
					DataInfo.ResetPwd = false;
					Intent in = new Intent(ForgetActivity.this,
							LoginActivity.class);
					UserInfoActivity.userinfoActivity.finish();
					MainActivity.main_activity.finish();
					startActivity(in);

				}
			} else if (msg.what == 3) {
				Toast.makeText(getApplicationContext(), "�û��������ڣ�����ע��",
						Toast.LENGTH_SHORT).show();
				// �����ÿ�
				accountEditview.setText("");
				identifyEditText.setText("");
				passwordEditview.setText("");

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

	private class ForgetRunnale implements Runnable {

		@Override
		public void run() {
			while (true) {
				// ���ܴ��̷߳��ʽ����ϵ����
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
				// ���ܴ��̷߳��ʽ����ϵ����
				try {
					HttpRequest httprequest = new HttpRequest();
					System.out.println("���ͷ���������");
					DataInfo.forget_identify  = true;//���Ϊ��������ķ�����֤��
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
				Toast.makeText(getApplicationContext(), "����������֤��",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "�޸�����",
						Toast.LENGTH_SHORT).show();
				ForgetRunnale regrunnale = new ForgetRunnale();
				new Thread(regrunnale).start();

			}

		}

		else if (v.getId() == R.id.forget_identifyBtn) {
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

				// if ((System.currentTimeMillis() - exitTime) < 30000) {
				// Toast.makeText(getApplicationContext(), "��ȴ���֤��ķ���",
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