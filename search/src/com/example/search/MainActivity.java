package com.example.search;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private static final int START = 1;
	private ImageView back_arrow;
	private Animation shakeAnimation;
	private DeletableEditText searchEditText;
	private KeywordsFlow keywordsFlow;
	private int STATE = 1;

	private static String[] keywords = new String[] { "Anumbrella", "shuyun",
			"打伞的她", "By anumbrella", "完美搭档", "致青春", "非常完美", "一生一世", "穿越火线",
			"天龙八部", "匹诺曹", "让子弹飞", "穿越火线", "情定三生", "心术", "马向阳下乡记", "人在囧途",
			" 高达", " 刀剑神域", "泡芙小姐", "尖刀出鞘", "甄嬛传", "兵出潼关", "电锯惊魂3D", "古剑奇谭",
			"同桌的你" };

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case START:
				keywordsFlow.rubKeywords();
				feedKeywordsFlow(keywords, keywordsFlow);
				keywordsFlow.startShow(KeywordsFlow.ANIMATION_OUT, 1);
				sendEmptyMessageDelayed(START, 4000);
				break;

			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
		initView();
	}

	// 初始化视图
	private void initView() {
		// TODO Auto-generated method stub
		keywordsFlow = (KeywordsFlow) findViewById(R.id.keywordsflow);
		keywordsFlow.setDuration(1000l);
		keywordsFlow.setOnItemClickListener(this);
		back_arrow = (ImageView) findViewById(R.id.back_arrow);
		// 设置图标上下晃动
		back_arrow.setAnimation(shakeAnimation);
		searchEditText = (DeletableEditText) findViewById(R.id.search_view);
		feedKeywordsFlow(keywords, keywordsFlow);
		keywordsFlow.startShow(KeywordsFlow.ANIMATION_IN, 1);
		handler.sendEmptyMessageDelayed(START, 4000);

	}

	// 将字符串填充到vector中去
	private void feedKeywordsFlow(String[] attr, KeywordsFlow keywordsFlow) {
		// TODO Auto-generated method stub
		Random random = new Random();
		for (int i = 0; i < KeywordsFlow.MAX; i++) {
			int ran = random.nextInt(attr.length);
			String tmp = attr[ran];
			keywordsFlow.feedKeywords(tmp);

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v instanceof TextView) {
			String keyword = ((TextView) v).getText().toString().trim();
			searchEditText.setText(keyword);
			// 选择插入的位置
			searchEditText.setSelection(keyword.length());
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		back_arrow.clearAnimation();
		handler.removeMessages(START);
		STATE = 0;

	}

	@Override
	protected void onPause() {
		super.onPause();
		handler.removeMessages(START);
		STATE = 0;

	}

	@Override
	protected void onStop() {
		super.onStop();
		handler.removeMessages(START);
		STATE = 0;

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (STATE == 0) {
			keywordsFlow.rubKeywords();
			handler.sendEmptyMessageDelayed(START, 3000);

		}

	}

}
