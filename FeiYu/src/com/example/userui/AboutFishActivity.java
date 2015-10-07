package com.example.userui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.feiyu.R;

public class AboutFishActivity extends Activity implements OnClickListener {

	private Button editBt;
	private TextView title_txt;
	private ImageButton returnBt;
	private RelativeLayout functionintroduce_layout, ifnewversion_layout,
			feedback_layout, connectus_layout;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutfish);

		editBt = (Button) findViewById(R.id.editBt);
		title_txt = (TextView) findViewById(R.id.title_txt);
		returnBt=(ImageButton) findViewById(R.id.returnBt);

		functionintroduce_layout = (RelativeLayout) findViewById(R.id.functionintroduce_layout);
		ifnewversion_layout = (RelativeLayout) findViewById(R.id.ifnewversion_layout);
		feedback_layout = (RelativeLayout) findViewById(R.id.feedback_layout);
		connectus_layout = (RelativeLayout) findViewById(R.id.connectus_layout);
		editBt.setVisibility(View.INVISIBLE);
		title_txt.setText("���ڷ���");

		returnBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();

			}
		});

		functionintroduce_layout.setOnClickListener(this);
		ifnewversion_layout.setOnClickListener(this);
		feedback_layout.setOnClickListener(this);
		connectus_layout.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		Intent intent = new Intent();
		switch (arg0.getId()) {
		case R.id.functionintroduce_layout:
			intent.setClass(AboutFishActivity.this, FunctionIntroActivity.class);
			startActivity(intent);
			break;
		case R.id.ifnewversion_layout:
			// �������������ƥ�䣬���Ƿ����°汾
			
			if (true) {
				new AlertDialog.Builder(this).setTitle("���汾")
						.setMessage("���Ѿ������°汾").setNegativeButton("ȷ��", null)
						.show();
			}
//			 else {
//				new AlertDialog.Builder(this).setTitle("���汾")
//						.setMessage("��Ҫ������+new version+��")
//						.setPositiveButton("��", null)
//						.setNegativeButton("��", null).show();
//			}

			break;
		case R.id.feedback_layout:
			intent.setClass(AboutFishActivity.this, FeedbackActivity.class);
			startActivity(intent);
			break;
		case R.id.connectus_layout:
			intent.setClass(AboutFishActivity.this, ConnectusActivity.class);
			startActivity(intent);
			break;

		}

	}
}
