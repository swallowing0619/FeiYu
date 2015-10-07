package com.example.data;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceView;
import android.widget.SimpleAdapter;

import com.example.tool.DevicTool;
import com.example.wait.Fish;

public class DataInfo {

	// 屏幕信息
	public static final int SCREEN_WIDTH = DevicTool.deviceWidth;
	public static final int SCREEN_HEIGHT = DevicTool.deviceHeight;
	public static Context context;
	public static Canvas canvas;
	// 等待的冒泡的鱼
	public static Fish fish;
	public static int width = 100;
	public static int height = 80;
	// 数据库对象
	public static DatabaseHelper DBHelper;
	// 区号
	public static String QuHao = "";
	public static String MESSAGE = "";
	// 手机号码列表
	public static ArrayList<HashMap<String, Object>> listItem;
	//号码排列的区内排序
	public static int ORDER = 1;

	// 当前手机号码发送列表
	public static int Group;
	public static ArrayList<HashMap<String, Object>> sendlist;

	// 发送时间
	public static ArrayList<String> time;

	public static boolean LOGIN = true;
	// 是否为修改密码
	public static boolean ResetPwd = false;

	// 服务器器返回的错误码
	public static boolean Request_diable = false;// 网络连接不可用
	public static boolean Request_timeout = false;// 网络连接超时
	public static String ERR_CODE = "";// 为6就是余额不足
	public static String SEND_type = "";
	public static String failList[];
	public static String faillist_Index[];
	public static boolean forget_identify = false;
	
	//重发状态
	public static boolean RESEND_FLAG = false;
	//重发号码的id
	public static ArrayList<String> RESEND_ID = new ArrayList<String>();
	
	//定时的时间
	public static String TIMER_HOUR = "";
	public static String TIMER_MINUTE = "";
	
	//判断字符串是否含有某字符
	public static boolean isHave(String strs, String s) {
		/*
		 * 此方法有两个参数，第一个是要查找的字符串，第二个是要查找的字符或字符串
		 */
		for (int i = 0; i < strs.length(); i++) {
			if (strs.indexOf(s) != -1) {// 循环查找字符串数组中的每个字符串中是否包含所有查找的内容
				return true;// 查找到了就返回真，不在继续查询
			}
		}
		return false;// 没找到返回false
	}

	// 图片哈希表
	public static HashMap<String, Bitmap> maps = new HashMap<String, Bitmap>();

	// 加载图片的方法
	public static void load(SurfaceView view) {
		try {
			// Bitmap bmp = BitmapFactory.decodeStream(view.getResources()
			// .getAssets().open("top_juli.png"));
			// maps.put("juli", bmp);
			// bmp = BitmapFactory.decodeStream(view.getResources().getAssets()
			// .open("top_score.png"));
			// maps.put("score", bmp);
			Bitmap bmp;
			String[] array = { "welcomea", "welcomeb", "welcomec" };
			for (int i = 0; i < array.length; i++) {
				bmp = BitmapFactory.decodeStream(view.getResources()
						.getAssets().open(array[i] + ".png"));
				maps.put(array[i], bmp);
			}
		} catch (IOException e) {
		}
	}

	// 获取图片的方法
	public static Bitmap getBitmap(String name) {
		return maps.get(name);
	}

}
