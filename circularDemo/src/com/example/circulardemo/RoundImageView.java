package com.example.circulardemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapShader;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
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

		type = mTyped.getInt(R.styleable.RoundImageView_type, TYPE_CIRCLE);// 默认为圆

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
}
