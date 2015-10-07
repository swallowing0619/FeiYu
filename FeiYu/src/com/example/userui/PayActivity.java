package com.example.userui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.feiyu.R;

public class PayActivity extends Activity{

	private Button editBt;
	private TextView title_txt;
	private ImageButton returnBt;
	private WebView webView;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);
		initview();
	}
	
	public void initview()
	{
		editBt = (Button) findViewById(R.id.editBt);
		title_txt=(TextView) findViewById(R.id.title_txt);
		returnBt=(ImageButton) findViewById(R.id.returnBt);
	
		
		editBt.setVisibility(View.GONE);
		title_txt.setText("≥‰÷µ");
		
		returnBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		
		webView=(WebView) findViewById(R.id.webView);
		webView.loadUrl("http://item.taobao.com/item.htm?spm=a1z10.1-c.w4004-10838214656.2.HXRab1&id=45011463813");
		
	}
}