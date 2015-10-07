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

	// ��Ļ��Ϣ
	public static final int SCREEN_WIDTH = DevicTool.deviceWidth;
	public static final int SCREEN_HEIGHT = DevicTool.deviceHeight;
	public static Context context;
	public static Canvas canvas;
	// �ȴ���ð�ݵ���
	public static Fish fish;
	public static int width = 100;
	public static int height = 80;
	// ���ݿ����
	public static DatabaseHelper DBHelper;
	// ����
	public static String QuHao = "";
	public static String MESSAGE = "";
	// �ֻ������б�
	public static ArrayList<HashMap<String, Object>> listItem;
	//�������е���������
	public static int ORDER = 1;

	// ��ǰ�ֻ����뷢���б�
	public static int Group;
	public static ArrayList<HashMap<String, Object>> sendlist;

	// ����ʱ��
	public static ArrayList<String> time;

	public static boolean LOGIN = true;
	// �Ƿ�Ϊ�޸�����
	public static boolean ResetPwd = false;

	// �����������صĴ�����
	public static boolean Request_diable = false;// �������Ӳ�����
	public static boolean Request_timeout = false;// �������ӳ�ʱ
	public static String ERR_CODE = "";// Ϊ6��������
	public static String SEND_type = "";
	public static String failList[];
	public static String faillist_Index[];
	public static boolean forget_identify = false;
	
	//�ط�״̬
	public static boolean RESEND_FLAG = false;
	//�ط������id
	public static ArrayList<String> RESEND_ID = new ArrayList<String>();
	
	//��ʱ��ʱ��
	public static String TIMER_HOUR = "";
	public static String TIMER_MINUTE = "";
	
	//�ж��ַ����Ƿ���ĳ�ַ�
	public static boolean isHave(String strs, String s) {
		/*
		 * �˷�����������������һ����Ҫ���ҵ��ַ������ڶ�����Ҫ���ҵ��ַ����ַ���
		 */
		for (int i = 0; i < strs.length(); i++) {
			if (strs.indexOf(s) != -1) {// ѭ�������ַ��������е�ÿ���ַ������Ƿ�������в��ҵ�����
				return true;// ���ҵ��˾ͷ����棬���ڼ�����ѯ
			}
		}
		return false;// û�ҵ�����false
	}

	// ͼƬ��ϣ��
	public static HashMap<String, Bitmap> maps = new HashMap<String, Bitmap>();

	// ����ͼƬ�ķ���
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

	// ��ȡͼƬ�ķ���
	public static Bitmap getBitmap(String name) {
		return maps.get(name);
	}

}
