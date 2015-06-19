package com.example.pmdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels; // 获取手机屏幕宽度
		int height = dm.heightPixels;// 获取手机屏幕高度

		Pm pm = (Pm)findViewById(R.id.pm);
		pm.setFinalAngle(270); // 设置偏转的角度
		pm.setScreen(width/3*2, width/3*2); // 设置图像的大小		
		pm.setAqi(250); // 设置空气aqi的值

	}

}
