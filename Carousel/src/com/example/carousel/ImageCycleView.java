package com.example.carousel;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.loopj.android.image.SmartImageView;

/**
 * @author anumbrella Created on 2015-7-12 下午8:38:38
 */
public class ImageCycleView extends LinearLayout {

	/**
	 * 上下文环境
	 */
	private Context mContext;

	/**
	 * 
	 * 图片轮播视图
	 */
	private ViewPager mAdPager;

	/**
	 * 图片轮播指示器
	 * 
	 */
	private ViewGroup viewGroup;

	/**
	 * 图片轮播指示个图
	 */
	private ImageView mImageView;

	/**
	 * 图片轮播指示视图列表
	 */
	private ImageView[] imageViews;

	/**
	 * 任务处理
	 */
	private Handler mHandler = new Handler();

	/**
	 * 图片轮播暂停与运行标识
	 */
	private boolean isStop;

	/**
	 * 滚动图片适配器
	 */
	private ImageCycleAdapter mAdapter;

	public ImageCycleView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

	}

	public ImageCycleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;

		// 加载布局
		LayoutInflater.from(context).inflate(R.layout.ad_cycle_view, this);

		mAdPager = (ViewPager) findViewById(R.id.ad_pager);

		mAdPager.setOnPageChangeListener(new GuidePageChangeListener());

		/**
		 * 在触摸屏进行按住和松开事件的操作
		 * 
		 * 动作有： ACTION_DOWN: //按住事件发生后执行的操作
		 * 
		 * ACTION_MOVE: //移动事件发生后执行的操作
		 * 
		 * ACTION_UP: //松开事件发生后执行的操作
		 * 
		 **/

		mAdPager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {

				case MotionEvent.ACTION_UP:
					// 开始图片滚动
					startImageTimerTask();
					break;
				default:
					// 停止图片滚动
					stopImageTimerTask();
					break;
				}
				return false;
			}
		});

		// 轮播图片下方的小圆点指示图片
		viewGroup = (ViewGroup) findViewById(R.id.viewGroup);

	}

	/**
	 * @param imageUrlList
	 * @param isFromUrl
	 * @param imageCycleViewListener
	 * 
	 *            加载图片资源的方法 装填图片数据
	 * 
	 */
	public void setImageViewResources(ArrayList<String> imageUrlList,
			ArrayList<Integer> imageResourceList, boolean isFromUrl,
			ImageCycleViewListener imageCycleViewListener) {

		// 首先清除其中的数据
		viewGroup.removeAllViews();
		final int count;
		// 获取图片的数量
		if (isFromUrl) {
			count = imageUrlList.size();
		} else {
			count = imageResourceList.size();
		}

		// 生成一个指示图片的数组大小
		imageViews = new ImageView[count];

		for (int i = 0; i < count; i++) {
			mImageView = new ImageView(mContext);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			params.leftMargin = 20;

			/**
			 * FIT_XY 不按比例缩放图片，目标是把图片塞满整个View。
			 * 
			 * FIT_CENTER 把图片按比例扩大/缩小到View的宽度，居中显示
			 * 
			 * FIT_START 把图片按比例扩大/缩小到View的宽度，顶部显示
			 * 
			 * FIT_END 把图片按比例扩大/缩小到View的宽度，底部显示
			 * 
			 * CENTER_CROP 按比例扩大图片的size居中显示，使得图片长(宽)等于或大于View的长(宽)
			 * 
			 * CENTER 按图片的原来size居中显示，当图片长/宽超过View的长/宽，则截取图片的居中部分显
			 * 
			 * */

			mImageView.setScaleType(ScaleType.FIT_START);
			mImageView.setLayoutParams(params);

			imageViews[i] = mImageView;
			if (i == 0) {
				imageViews[i]
						.setBackgroundResource(R.drawable.banner_dian_focus);
			} else {
				imageViews[i]
						.setBackgroundResource(R.drawable.banner_dian_blur);
			}

			viewGroup.addView(imageViews[i]);

		}

		// 加载显示的图片

		mAdapter = new ImageCycleAdapter(mContext, isFromUrl, imageUrlList,
				imageResourceList, imageCycleViewListener);

		// 设定适配器
		mAdPager.setAdapter(mAdapter);

		startImageTimerTask();

	}

	/**
	 * 关闭图片轮播任务
	 */
	private void stopImageTimerTask() {
		// TODO Auto-generated method stub
		isStop = true;
		// 删除任务
		mHandler.removeCallbacks(imageViewTimerTask);

	}

	/**
	 * 图片轮播(手动控制自动轮播与否，便于资源控件）
	 */
	public void startImageCycle() {
		startImageTimerTask();
	}

	/**
	 * 暂停轮播—用于节省资源
	 */
	public void pushImageCycle() {
		stopImageTimerTask();
	}

	/**
	 * 开启图片轮播任务
	 */
	private void startImageTimerTask() {
		// TODO Auto-generated method stub
		// 开启任务之前先把之前的任务给停止下来

		stopImageTimerTask();

		// 图片滚动任务开启
		mHandler.postDelayed(imageViewTimerTask, 3000);

	}

	/**
	 * 定义图片滚动的定时器任务
	 */
	private Runnable imageViewTimerTask = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (imageViews != null) {
				// 滚动为下一个图标显示

				int index = ((mAdPager.getCurrentItem() + 1) % imageViews.length);
				mAdPager.setCurrentItem(index);
				if (!isStop) {
					// if isStop=true //当你退出后 要把这个给停下来 不然 这个一直存在
					// 就一直在后台循环
					mHandler.postDelayed(imageViewTimerTask, 3000);

				}

			}

		}
	};

	private final class GuidePageChangeListener implements OnPageChangeListener {

		/**
		 * onPageScrollStateChanged(int grg0) 此方法是在状态改变的时候调用，其中state这个参数
		 * 有三种状态（0，1，2）。 arg0 ==1的时辰默示正在滑动，(SCROLL_STATE_DRAGGING)
		 * arg0==2的时辰默示滑动完毕了，(SCROLL_STATE_SETTLING)
		 * arg0==0的时辰默示什么都没做。(SCROLL_STATE_IDLE)
		 * 
		 * 当页面开始滑动的时候，三种状态的变化顺序为（1，2，0）
		 **/

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			// 启动轮播
			if (arg0 == ViewPager.SCROLL_STATE_IDLE) {
				startImageTimerTask();
			}

		}

		/**
		 * 
		 * onPageScrolled(int arg0,float arg1,int arg2)
		 * 当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法回一直得到调用。
		 * 
		 * 其中三个参数的含义分别为： arg0:当前页面，及你点击滑动的页面 arg1:当前页面偏移的百分比 arg2:当前页面偏移的像素位置
		 **/
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		/**
		 * onPageSelected(int index)
		 * 此方法是页面跳转完后得到调用，index是你当前选中的页面的Position（位置编号）。
		 * */
		@Override
		public void onPageSelected(int index) {
			// TODO Auto-generated method stub
			// 图片的指针,采用取余数可以无限循环
			index = (index % imageViews.length);

			// 设置当前显示的图片
			// 设置图片滚动指示器背景（指示小圆点的显示情况）

			imageViews[index]
					.setBackgroundResource(R.drawable.banner_dian_focus);
			for (int i = 0; i < imageViews.length; i++) {
				if (i != index) {
					imageViews[i]
							.setBackgroundResource(R.drawable.banner_dian_blur);
				}
			}

		}

	}

	/**
	 * 显示图片适配类
	 */
	private class ImageCycleAdapter extends PagerAdapter {

		/**
		 * 图片视图缓存列表
		 */
		private ArrayList<SmartImageView> mImageViewCacheList;

		/**
		 * 图片资源列表
		 */
		private ArrayList<String> mAdList = new ArrayList<String>();

		/**
		 * 图片点击监控类
		 */
		private ImageCycleViewListener mImageCycleViewListener;

		private boolean isFromUrl;

		/**
		 * 上下文环境
		 */
		private Context mContext;

		/**
		 * 本地图片资源
		 */
		private ArrayList<Integer> mAdResourceList = new ArrayList<Integer>();

		public ImageCycleAdapter(Context context, boolean isFromUrl,
				ArrayList<String> addList, ArrayList<Integer> addArrayList,
				ImageCycleViewListener imageCycleViewListener) {
			this.mContext = context;
			this.mAdList = addList;
			this.isFromUrl = isFromUrl;
			this.mAdResourceList = addArrayList;
			this.mImageViewCacheList = new ArrayList<SmartImageView>();
			this.mImageCycleViewListener = imageCycleViewListener;

		}

		/**
		 * View getCount() 这个方法，是获取当前窗体界面数(即可以滑动的数目)
		 */
		@Override
		public int getCount() {
			// TODO Auto-generated method stub

			// 根据实际情况返回具体的图片数目
			if (isFromUrl) {
				return mAdList.size();
			} else {
				return mAdResourceList.size();
			}

			// return Integer.MAX_VALUE; // 返回最大值代表可以无限滑动
		}

		/**
		 * isViewFromObject(View, Object) 这个方法，在帮助文档中原文是could be implemented as
		 * return view == object, 也就是用于判断是否由对象生成界面
		 ***/
		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == object;
		}

		/**
		 * instantiateItem(ViewGroup, int)
		 * 这个方法，return一个对象，这个对象表明了PagerAdapter适配器选择哪个对象*放在当前的ViewPager中
		 ***/
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {

			Log.i("test", "position" + position);
			String imageUrl = mAdList.get(position % mAdList.size());
			Integer imageResouce = mAdResourceList.get(position
					% mAdResourceList.size());
			SmartImageView imageView = null;
			if (mImageViewCacheList.isEmpty()) {
				imageView = new SmartImageView(mContext);

				imageView.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				imageView.setScaleType(ImageView.ScaleType.FIT_START);

			} else {
				imageView = mImageViewCacheList.remove(0);
			}

			// 设置图片点击监控事件
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (isFromUrl) {
						Log.i("test", "position2" + position);
						mImageCycleViewListener.onImageClick(position, v);

					} else {

						mImageCycleViewListener.onImageClick(position, v);
					}

				}
			});

			if (isFromUrl) {
				// 从url上面加载图片的资源
				imageView.setTag(imageUrl);
				container.addView(imageView);
				imageView.setImageUrl(imageUrl);
			} else {

				// 从本地加载图片资源
				container.addView(imageView);
				imageView.setImageUrl(null, imageResouce);

			}

			return imageView;

		}

		/**
		 * destroyItem(ViewGroup, int, Object) 这个方法，是从ViewGroup中移出当前图片
		 ***/
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			SmartImageView view = (SmartImageView) object;
			mAdPager.removeView(view);
			// 退出时把图片加入到缓存中
			mImageViewCacheList.add(view);
		}

	}

	/**
	 * 轮播控件的监听事件
	 */
	public static interface ImageCycleViewListener {

		/**
		 * 单击图片事件处理
		 * 
		 * @param position
		 * @param imageView
		 */
		public void onImageClick(int position, View imageView);

	}

}
