package com.example.circulardemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

/**
 * @author anumbrella Created on 2015-7-18 下午11:17:05
 * 
 *         绘画圆角视图类
 */
public class RoundImageView extends ImageView {

	/**
	 * 图片的类型,圆角or圆形
	 */
	private int type;

	public static final int TYPE_CIRCLE = 0;
	public static final int TYPE_ROUND = 1;

	/**
	 * 圆角大小的默认取值
	 */
	private static final int BORDER_RADIUS_DEFAULT = 10;

	/**
	 * 圆角的大小
	 */
	private int mBorderRadius;

	/**
	 * 绘图的画笔
	 */
	private Paint mBitmapPaint;

	/**
	 * 圆的半径
	 */
	private int mRadius;

	/**
	 * 3x3 矩阵，主要用于缩小放大
	 */
	private Matrix mMatrix;

	/**
	 * 渲染图像，使用图像为绘制图形着色
	 */
	private BitmapShader mBitmapShader;

	/**
	 * view的宽度
	 */
	private int vWidth;

	/**
	 * 绘制圆角矩形
	 */
	private RectF mRoundRectF;

	/**
	 * 字符串用于保存activity的临时状态的数据
	 * 
	 */

	private final static String STATE_INSTANCE = "state_instance";

	private final static String STATE_TYPE = "state_type";

	private final static String STATE_BORDER_RADIUS = "state_border_radius";

	public RoundImageView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
	}

	public RoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mMatrix = new Matrix();
		mBitmapPaint = new Paint();
		mBitmapPaint.setAntiAlias(true);

		// 获得自定义的属性数组
		TypedArray mTyped = context.obtainStyledAttributes(attrs,
				R.styleable.RoundImageView);

		// 获取圆角的大小，默认为10dip
		mBorderRadius = mTyped.getDimensionPixelSize(
				R.styleable.RoundImageView_borderRadius, (int) TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
								BORDER_RADIUS_DEFAULT, getResources()
										.getDisplayMetrics()));
		// 默认为圆
		type = mTyped.getInt(R.styleable.RoundImageView_type, TYPE_CIRCLE);

		mTyped.recycle();

	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// 如果定义为圆
		if (type == TYPE_CIRCLE) {

			// 取宽或高的最小值
			vWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());

			mRadius = vWidth / 2;
			setMeasuredDimension(vWidth, vWidth);

		}

	}

	/**
	 * 初始化设置图形填充器
	 */
	private void initShader() {
		Drawable drawable = getDrawable();
		if (drawable == null) {
			// 如果没有drawable就直接结束该方法
			return;
		}

		Bitmap bmp = drawableToBitmap(drawable);

		/**
		 * BitmapShader(Bitmap bitmap,Shader.TileMode tileX,Shader.TileMode
		 * tileY) 调用这个方法来产生一个画有一个位图的渲染器（Shader）。 bitmap 在渲染器内使用的位图 tileX
		 * 在位图上X方向花砖模式 tileY 在位图上Y方向花砖模式 TileMode：（一共有三种） CLAMP
		 * ：如果渲染器超出原始边界范围，会复制范围内边缘染色。 REPEAT ：横向和纵向的重复渲染器图片，平铺。 MIRROR
		 * ：横向和纵向的重复渲染器图片，这个和REPEAT 重复方式不一样，他是以镜像方式平铺。
		 */

		// 将bmp作为着色器，就是在指定区域内绘制bmp
		mBitmapShader = new BitmapShader(bmp, TileMode.CLAMP, TileMode.CLAMP);
		// 定义缩放比例
		float scale = 1.0f;
		// 如果是圆的类型,拿到位图bmp宽高的最小值
		// 定义缩放的比例
		if (type == TYPE_CIRCLE) {
			int bSize = Math.min(bmp.getWidth(), bmp.getHeight());
			scale = vWidth * 1.0f / bSize;
		} else if (type == TYPE_ROUND) {

			// getWidth(),getHeight()是获得view视图的宽高
			if (!(bmp.getWidth() == getWidth() && bmp.getHeight() == getHeight())) {
				// 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；
				// 缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
				scale = Math.max(getWidth() * 1.0f / bmp.getWidth(),
						getHeight() * 1.0f / bmp.getHeight());
			}

		}

		// shader的变换矩阵，我们这里主要用于放大或者缩小
		mMatrix.setScale(scale, scale);

		// 设置变换矩阵
		mBitmapShader.setLocalMatrix(mMatrix);
		// 设置shader填充器
		mBitmapPaint.setShader(mBitmapShader);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 获取图片源
		if (getDrawable() == null) {
			return;
		}
		// 初始化图片填充器
		initShader();

		// 根据实际的类型来绘图
		if (type == TYPE_ROUND) {
			canvas.drawRoundRect(mRoundRectF, mBorderRadius, mBorderRadius,
					mBitmapPaint);
		} else {
			canvas.drawCircle(mRadius, mRadius, mRadius, mBitmapPaint);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onSizeChanged(int, int, int, int)
	 * 
	 * 在onDraw()方法之前调用的
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		// 默认为圆，如果不是圆，则要绘画出矩形图像
		if (type == TYPE_ROUND || mRoundRectF == null) {
		
			mRoundRectF = new RectF(0, 0, w, h);
		}

	}

	/**
	 * @param drawable
	 * @return 将drawable转换为bitmap
	 */
	private Bitmap drawableToBitmap(Drawable drawable) {

		// 如果drawable中有bitmap,直接从其中取出来
		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bd = (BitmapDrawable) drawable;
			return bd.getBitmap();
		}

		// 如果没有bitmap,则根据drawable生成bitmap
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		drawable.draw(canvas);
		return bitmap;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onSaveInstanceState()
	 * 每当activity被销毁掉时就会调用该方法去保存activity的临时数据
	 */
	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		// 根据键值对进行对象的存储(把对象打包保存)
		bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
		bundle.putInt(STATE_TYPE, type);
		bundle.putInt(STATE_BORDER_RADIUS, mBorderRadius);
		return bundle;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onRestoreInstanceState(android.os.Parcelable)
	 * onRestoreInstanceState被调用的前提是，activity“确实”被系统销毁了
	 */
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		// 如果被销毁的activity中又被保存的bundle
		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			// 获取activity被销毁时打包保存的对象
			super.onRestoreInstanceState(((Bundle) state)
					.getParcelable(STATE_INSTANCE));
			this.type = bundle.getInt(STATE_TYPE);
			this.mBorderRadius = bundle.getInt(STATE_BORDER_RADIUS);
		} else {
			super.onRestoreInstanceState(state);
		}

	}

	/**
	 * @param type
	 *            设定图形的类型
	 */
	public void setType(int type) {
		if (this.type != type) {
			this.type = type;
			// 判定type如果既不是圆又不是圆角,就设定为默认值圆
			if (this.type != TYPE_CIRCLE && this.type != TYPE_ROUND) {
				this.type = TYPE_CIRCLE;
			}
		}

		/*
		 * requestLayout：当view确定自身已经不再适合现有的区域时，该view本身调用这个方法要求parent
		 * view重新调用他的onMeasure onLayout来对重新设置自己位置。
		 * 特别的当view的layoutparameter发生改变，并且它的值还没能应用到view上，这时候适合调用这个方法
		 */
		requestLayout();

	}

	/**
	 * @return 获取type的值
	 */
	public int getType() {
		return this.type;
		
	}

	/**
	 * @param borderRadius
	 *            设定圆角的大小
	 */
	public void setBorderRadius(int borderRadius) {
		int pxVal = dp2px(borderRadius);
		if (pxVal != borderRadius) {
			this.mBorderRadius = pxVal;
			invalidate();
		}

	}

	public int dp2px(int dpVal) {
		// 根据传入的数值来判定返回的数据
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpVal, getResources().getDisplayMetrics());

	}

}
