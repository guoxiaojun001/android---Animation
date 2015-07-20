package com.example.circulardemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author anumbrella Created on 2015-7-20 下午2:37:54
 *         图形变换不能这样写，，动画有延时，，只能用于绘制各种图案
 */
public class MainActivity extends Activity {

	private RoundImageView check1;
	private RoundImageView check2;
	private RoundImageView check3;
	private RoundImageView check4;
	private RoundImageView check5;
	private RoundImageView check6;
	private RoundImageView check7;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.circulaer_layout);

		check1 = (RoundImageView) findViewById(R.id.check1);
		check2 = (RoundImageView) findViewById(R.id.check2);
		check3 = (RoundImageView) findViewById(R.id.check3);
		check4 = (RoundImageView) findViewById(R.id.check4);
		check5 = (RoundImageView) findViewById(R.id.check5);
		check6 = (RoundImageView) findViewById(R.id.check6);
		check7 = (RoundImageView) findViewById(R.id.check7);

		check1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (check1.getType() == RoundImageView.TYPE_CIRCLE) {
					check1.setType(RoundImageView.TYPE_ROUND);
					check1.setBorderRadius(15);
				} else {
					check1.setType(RoundImageView.TYPE_CIRCLE);
				}

			}
		});
		check2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (check2.getType() == RoundImageView.TYPE_CIRCLE) {
					check2.setType(RoundImageView.TYPE_ROUND);
					check2.setBorderRadius(100);
				} else {
					check2.setType(RoundImageView.TYPE_CIRCLE);
				}

			}
		});

		check3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (check3.getType() == RoundImageView.TYPE_CIRCLE) {
					check3.setType(RoundImageView.TYPE_ROUND);
					check3.setBorderRadius(50);
				} else {
					check3.setType(RoundImageView.TYPE_CIRCLE);
				}

			}
		});

		check4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (check4.getType() == RoundImageView.TYPE_CIRCLE) {
					check4.setType(RoundImageView.TYPE_ROUND);
					check4.setBorderRadius(80);
				} else {
					check4.setType(RoundImageView.TYPE_CIRCLE);
				}

			}
		});

		check5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (check5.getType() == RoundImageView.TYPE_CIRCLE) {
					check5.setType(RoundImageView.TYPE_ROUND);
					check5.setBorderRadius(5);
				} else {
					check5.setType(RoundImageView.TYPE_CIRCLE);
				}

			}
		});

		check6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (check6.getType() == RoundImageView.TYPE_CIRCLE) {
					check6.setType(RoundImageView.TYPE_ROUND);
					check6.setBorderRadius(10);
				} else {
					check6.setType(RoundImageView.TYPE_CIRCLE);
				}

			}
		});
		check7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (check7.getType() == RoundImageView.TYPE_CIRCLE) {
					check7.setType(RoundImageView.TYPE_ROUND);
					check7.setBorderRadius(5);
				} else {
					check7.setType(RoundImageView.TYPE_CIRCLE);
				}

			}
		});

	}

}
