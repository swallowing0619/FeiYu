package com.example.userui;

import com.example.feiyu.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class FeedbackActivity extends Activity {

	private Button editBt, send;
	private TextView title_txt;
	private ImageButton returnBt;
	private EditText advice_edit;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		initview();
	}

	public void initview() {
		editBt = (Button) findViewById(R.id.editBt);
		title_txt = (TextView) findViewById(R.id.title_txt);
		returnBt = (ImageButton) findViewById(R.id.returnBt);
		send = (Button) findViewById(R.id.feedbacksendButton);
		advice_edit = (EditText) findViewById(R.id.feedbackEdit);
		editBt.setVisibility(View.GONE);
		title_txt.setText("�������");

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (!(advice_edit.getText().toString().equals(""))) {
					Toast.makeText(getApplicationContext(), "����ѷ��ͣ���л���ı��������",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		returnBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
}
