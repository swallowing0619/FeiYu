package com.example.login;



import java.io.IOException;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.widget.Toast;

import com.example.feiyu.R;
import com.example.feiyumain.MainActivity;
import com.example.net.HttpRequest;

public class LoadingActivity extends Activity {
	
	 public static LoadingActivity loadingactivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.loading);
		loadingactivity = this;
	//����Handler��postDelayed�������ȴ�10000������ִ��run������
	//��Activity�����Ǿ�����Ҫʹ��Handler��������UI����ִ��һЩ��ʱ�¼���
	//����Handler��post�����ȿ���ִ�к�ʱ�¼�Ҳ������һЩUI���µ����飬�ȽϺ��ã��Ƽ�ʹ��
		
		
	
	
		
   }
	
	
}