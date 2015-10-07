package com.example.userui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.feiyu.R;

public class FunctionIntroActivity  extends Activity{

	private Button editBt;
	private TextView title_txt;
	private ImageButton returnBt;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_functionintroduce);
		initview();
	}
	
	public void initview()
	{
		editBt = (Button) findViewById(R.id.editBt);
		title_txt=(TextView) findViewById(R.id.title_txt);
		returnBt=(ImageButton) findViewById(R.id.returnBt);
		
		editBt.setVisibility(View.GONE);
		title_txt.setText("π¶ƒ‹ΩÈ…‹");
		
		returnBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
}
