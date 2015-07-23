package com.example.search;

import java.util.Random;
import java.util.Vector;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

/**
 * @author anumbrella Created on 2015-7-21 下午4:45:50
 */
/**
 * @author anumbrella Created on 2015-7-23 下午9:52:35
 */
public class KeywordsFlow extends FrameLayout implements OnGlobalLayoutListener {

	/**
	 * ID_X、ID_Y分别为TextView存放为数组x轴和y轴距离的键值
	 */
	private static final int IDX_X = 0;
	private static final int IDX_Y = 1;

	/**
	 * 单个TextView的长度存放为数组的键值
	 */
	private static final int IDX_TXT_LENGTH = 2;

	/**
	 * 存放TextView在y轴上距离屏幕中点的距离的键值
	 */
	private static final int IDX_DIS_Y = 3;

	/**
	 * 设定默认动画持续的时间
	 */
	private static final long ANIM_DURATION = 800l;

	/**
	 * 显示TextView的最大的个数
	 */
	private static final int MAX = 10;

	/**
	 * 视图动画插值器
	 */
	private static Interpolator interpolator;

	/**
	 * 动画分别为不透明到透明，透明到不透明
	 */
	private static AlphaAnimation animAlphaToPaque, animAlphaToTransparent;

	/**
	 * 大小变化动画,分别为大变为正常，正常变为大，没有变为正常，正常变为没有
	 */
	private static ScaleAnimation animScaleLargeToNormal,
			animScaleNormalToLarge, animScaleZeroToNormal,
			animScaleNormalToZero;

	/**
	 * 最后一次动画运行的时间
	 */
	private long lastStartAnimationTime;

	/**
	 * 动画持续的时间
	 */
	private long animDuration;

	/**
	 * 设置随机函数，用于生成距离不确定的值
	 */
	private Random random;

	/**
	 * 存储用来显示的关键字 (与ArrayList相比具有同步的特性)
	 * 
	 */
	private Vector<String> vecKeywords;

	/**
	 * 本类布局的宽高
	 */
	private int width, height;

	public KeywordsFlow(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public KeywordsFlow(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public KeywordsFlow(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init();
	}

	/**
	 * 进行一些初始化的事情
	 * 
	 */

	private void init() {
		// TODO Auto-generated method stub

		this.lastStartAnimationTime = 0l;
		this.animDuration = ANIM_DURATION;
		this.random = new Random();
		this.vecKeywords = new Vector<String>();

		/**
		 * 当前的类为frameLayout,所以监听的是视图改变时的状态
		 * 当在一个视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变时，所要调用的回调函数的接口类
		 * 
		 * view事件的观察者
		 */
		getViewTreeObserver().addOnGlobalLayoutListener(this);

		// 使用android资源中的加载动画
		// 减速进入
		this.interpolator = AnimationUtils.loadInterpolator(getContext(),
				android.R.anim.decelerate_interpolator);

		this.animAlphaToPaque = new AlphaAnimation(0.0f, 1.0f);
		this.animAlphaToTransparent = new AlphaAnimation(1.0f, 0.0f);
		this.animScaleLargeToNormal = new ScaleAnimation(2, 1, 2, 1);
		this.animScaleNormalToLarge = new ScaleAnimation(1, 2, 1, 2);
		this.animScaleNormalToZero = new ScaleAnimation(1, 0, 1, 0);
		this.animScaleZeroToNormal = new ScaleAnimation(0, 1, 0, 1);

	}
	
	
	public long getDuration(){
		return this.animDuration;
	}
	
	
	public void setDuration(long duration){
		this.animDuration = duration;
	}
	
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.ViewTreeObserver.OnGlobalLayoutListener#onGlobalLayout()
	 * 用于监听布局改变或视图树中某个视图的可视状态发生改变
	 */
	@Override
	public void onGlobalLayout() {
		// TODO Auto-generated method stub

	}

}
