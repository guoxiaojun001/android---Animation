package com.example.carousel;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.carousel.ImageCycleView.ImageCycleViewListener;

public class MainActivity extends Activity {

	private ImageCycleView mAdCycleView;

	private ArrayList<String> mImageUrl;
	private ArrayList<Integer> mImageResource;

	private String imageUrl1 = "http://image.tianjimedia.com/uploadImages/2010/247/749A0YPE4833.jpg";
	private String imageUrl2 = "http://wallcoo.com/nature/land-photography/wallpapers/1024x768/Sky-picture-88.jpg";
	private String imageUrl3 = "http://img6.ph.126.net/d-dLbShvQmzmXx39ZAUlQQ==/2504845817765216216.jpg";

	private Integer[] mIntegers = new Integer[] { R.drawable.image1,
			R.drawable.image2, R.drawable.image3, R.drawable.image4,
			R.drawable.image5, R.drawable.image6 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mImageUrl = new ArrayList<String>();
		mImageUrl.add(imageUrl1);
		mImageUrl.add(imageUrl2);
		mImageUrl.add(imageUrl3);

		mImageResource = new ArrayList<Integer>();
		for (int i = 0; i < mIntegers.length; i++) {
			mImageResource.add(mIntegers[i]);
		}
		mAdCycleView = (ImageCycleView) findViewById(R.id.ad_view);
		mAdCycleView.setImageViewResources(mImageUrl, mImageResource, false,
				imageCycleViewListener);

	}

	private ImageCycleViewListener imageCycleViewListener = new ImageCycleViewListener() {
		@Override
		public void onImageClick(int position, View imageView) {
			// TODO 单击图片处理事件
			Toast.makeText(MainActivity.this, "这是第" + (position + 1) + "图片",
					Toast.LENGTH_SHORT).show();
		}
	};

}
