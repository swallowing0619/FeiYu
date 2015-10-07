package com.example.addfunction;

import java.util.zip.Inflater;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.addfunction.PieGraph.OnSliceClickedListener;
import com.example.data.DataInfo;
import com.example.feiyu.R;

public class SendPersentActivity extends Activity {

	private Button editBt;
	private TextView title_txt;
	private ImageButton returnBt;
	private TextView send_percent;
	private TextView send_sum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sendpersent);

		editBt = (Button) findViewById(R.id.editBt);
		title_txt = (TextView) findViewById(R.id.title_txt);
		returnBt = (ImageButton) findViewById(R.id.returnBt);
		send_percent = (TextView) findViewById(R.id.send_percent);
		send_sum = (TextView) findViewById(R.id.send_sum);

		editBt.setVisibility(View.INVISIBLE);
		title_txt.setText("统计发送率");
		
		returnBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();

			}
		});

		chartgraph();
		
		
	}

	public void chartgraph() {
		// Inflater inflater=new Inflater();
		// final View v = inflater.inflate(R.layout.fragment_piegraph,
		// container, false);
		final Resources resources = getResources();
		final PieGraph pg = (PieGraph) this.findViewById(R.id.piegraph);
		// final Button animateButton = (Button)
		// this.findViewById(R.id.animatePieButton);
		PieSlice slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.red1));
		slice.setSelectedColor(resources.getColor(R.color.red2));
		//正确比例
		int success = DataInfo.DBHelper.sendSuccess_count();
		int fail = DataInfo.DBHelper.sendFail_count();
		int sum = success+fail;
		if(success == 0 && fail == 0){
//			slice.setValue(1);
		}else{
			float c = ((float)success/(float)sum)*100;
			slice.setValue(c);
		}
		slice.setTitle("发送成功");
		pg.addSlice(slice);
		slice = new PieSlice();
		slice.setColor(resources.getColor(R.color.gray4));
		if(success == 0 && fail == 0){
			slice.setValue(99);
		}else{
			float c = ((float)fail/(float)sum)*100;
			slice.setValue(c);
		}
		float s=0;
		if(success == 0){
			s=0;
		}else{
			s = ((float)success/(float)sum)*100 ;
		}
		send_percent.setText(s+"%");
		send_sum.setText((sum)+"");
		pg.addSlice(slice);
		// slice = new PieSlice();
		// slice.setColor(resources.getColor(R.color.purple));
		// slice.setValue(8);
		// pg.addSlice(slice);
		pg.setOnSliceClickedListener(new OnSliceClickedListener() {

			@Override
			public void onClick(int index) {
				if (index == 0) // 第一个
				{
					Toast.makeText(SendPersentActivity.this, "发送成功比例",
							Toast.LENGTH_SHORT).show();
				}else if(index==1)
				{
					Toast.makeText(SendPersentActivity.this, "发送失败比例",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public Animator.AnimatorListener getAnimationListener() {
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)
			return new Animator.AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animation) {

				}

				@Override
				public void onAnimationEnd(Animator animation) {
					Log.d("piefrag", "anim end");
				}

				@Override
				public void onAnimationCancel(Animator animation) {// you might
																	// want to
																	// call
																	// slice.setvalue(slice.getGoalValue)
					Log.d("piefrag", "anim cancel");
				}

				@Override
				public void onAnimationRepeat(Animator animation) {

				}
			};
		else
			return null;

	}
//		Bitmap b = BitmapFactory.decodeResource(getResources(),
//				R.drawable.ic_launcher);
//		pg.setBackgroundBitmap(b);

//		SeekBar seekBar = (SeekBar) this.findViewById(R.id.seekBarRatio);
//		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//			@Override
//			public void onProgressChanged(SeekBar seekBar, int progress,
//					boolean fromUser) {
//				pg.setInnerCircleRatio(progress);
//			}
//
//			@Override
//			public void onStartTrackingTouch(SeekBar seekBar) {
//
//			}
//
//			@Override
//			public void onStopTrackingTouch(SeekBar seekBar) {
//
//			}
//		});
//
//		seekBar = (SeekBar) this.findViewById(R.id.seekBarPadding);
//		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//			@Override
//			public void onProgressChanged(SeekBar seekBar, int progress,
//					boolean fromUser) {
//				pg.setPadding(progress);
//			}
//
//			@Override
//			public void onStartTrackingTouch(SeekBar seekBar) {
//
//			}
//
//			@Override
//			public void onStopTrackingTouch(SeekBar seekBar) {
//
//			}
//		});
//
//	}

	

}
