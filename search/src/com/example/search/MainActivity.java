package com.example.search;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private static final int START = 1;
	private ImageView back_arrow;
	private Animation shakeAnimation;
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

}
