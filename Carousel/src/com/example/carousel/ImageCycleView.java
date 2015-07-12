package com.example.carousel;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * @author anumbrella
 * Created on 2015-7-12 下午8:38:38
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
	 * 图片轮播暂停与运行标识
	 */
	private boolean isStop;

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
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					startImageTimerTask();
					break;
				default:
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
	 * 开启图片轮播任务
	 */
	private void stopImageTimerTask() {
		// TODO Auto-generated method stub

	}

	/**
	 * 关闭图片轮播任务
	 */
	private void startImageTimerTask() {
		// TODO Auto-generated method stub

	}

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
			index = index % imageViews.length;

			// 设置当前显示的图片
			// 设置图片滚动指示器背景（指示小圆点的显示情况）

			imageViews[index]
					.setBackgroundResource(R.drawable.banner_dian_focus);
			for (int i = 0; i < imageViews.length; i++) {
				if (index != i) {
					imageViews[index]
							.setBackgroundResource(R.drawable.banner_dian_blur);
				}
			}

		}

	}

}
