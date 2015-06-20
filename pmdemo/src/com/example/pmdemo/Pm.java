package com.example.pmdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;

public class Pm extends View {

	private Paint paint_white, paint_black;
	private RectF rectF;
	private int color_;
	private int color_black = 0x70000000;
	private int color_white = 0xddffffff;

	private int aqi; // 空气AQI
	private float avg; // 平均AQI,每一aqi值所代表的度数
	private float maxAqi = 350; // 最大aqi数据
	private float degree; // 偏转的角度
	private int aqiText; // AQI文本

	private int height; // 手机屏幕高度
	private int width; // 手机屏幕宽度

	private float finalAngle=250; // 旋转终止的角度

	public Pm(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init();
	}

	public Pm(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public Pm(Context context) {
		super(context);
		init();
	}

	// 设置图像的宽度和高度
	public void setScreen(int h, int w) {
		this.height = h;
		this.width = w;
	}

	// 设置最终旋转地角度
	public void setFinalAngle(float Angle) {
		this.finalAngle = Angle;
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measureHeight = MeasureWidth(widthMeasureSpec);
		int measureWidth = MeasureHeight(heightMeasureSpec);
		setMeasuredDimension(measureHeight, measureWidth);
	}

	/**
	 * 依据specMode的值，（MeasureSpec有3种模式分别是UNSPECIFIED, EXACTLY和AT_MOST）
	 * 如果是AT_MOST，specSize 代表的是最大可获得的空间； 如果是EXACTLY，specSize 代表的是精确的尺寸；
	 * 如果是UNSPECIFIED，对于控件尺寸来说，没有任何参考意义。
	 * 
	 * @param measureSpec
	 * @return
	 */
	private int MeasureWidth(int measureSpec) {
		// TODO Auto-generated method stub
		int result = width;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else {
			// Measure the text
			result = width;
			if (specMode == MeasureSpec.AT_MOST) {
				// Respect AT_MOST value if that was what is called for by
				// measureSpec
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	private int MeasureHeight(int measureSpec) {
		// TODO Auto-generated method stub
		int result = height;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = height;
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);

			}
		}
		return result;
	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);

		// 最外层圆圈
		rectF.set(0, 0, width, height); // 设置圆的相切的矩形（width,height）屏幕左上为（0，0）
		paint_black.setAntiAlias(true); // 设置图像为最佳，无锯齿
		paint_black.setStyle(Style.STROKE); // 设置画笔为空心 即画出的图像不是填充的
		paint_black.setStrokeWidth(2); // 设置画笔的宽度
		paint_black.setColor(color_black); // 设置画笔的颜色

		// 画圆弧，参数1.坐标，2.起点度数，x轴正方向为0度，3.旋转的度数，4.是否画出圆心，与圆心相连接，5.画笔的参数
		canvas.drawArc(rectF, -225, finalAngle, false, paint_black);

		// 里层圆圈
		rectF.set(30, 30, width - 30, height - 30);
		paint_black.setStrokeWidth(30);
		canvas.drawArc(rectF, -225, 270, false, paint_black);

		// 里层遮盖(转动的白色覆盖的圆弧)
		paint_white.setColor(color_);
		paint_white.setAntiAlias(true);
		paint_white.setStrokeWidth(30);
		paint_white.setStyle(Style.STROKE);
		canvas.drawArc(rectF, -225,degree-1, false, paint_white);

		// 指针白线
		paint_white.setStrokeWidth(3);
		if (aqi > 0) {
			// 将度数设为0，同时第4个参数为true，设定为连接圆心
			canvas.drawArc(rectF, -226 + degree, 0, true, paint_white);
		}

		// 画圆心
		rectF.set(width / 2 - 10, width / 2 - 10, width / 2 + 10,
				width / 2 + 10);
		paint_white.setStrokeWidth(3);
		paint_white.setColor(color_);
		canvas.drawArc(rectF, 0, 360, false, paint_white);

		// 遮罩层 将圆心里面的线遮盖
		rectF.set(width / 2 - 10, width / 2 - 10, width / 2 + 10,
				width / 2 + 10);
		paint_white.setStyle(Style.FILL);
		paint_white.setStrokeWidth(0);
		paint_white.setColor(getResources().getColor(R.color.xx)); // 背景颜色，，
		canvas.drawArc(rectF, 0, 360, false, paint_white);

		// 设置文本位置
		paint_white.setTextSize(80); // 设置文本的字体大小
		paint_white.setStrokeWidth(1);
		paint_white.setTextAlign(Align.CENTER);
		paint_white.setColor(color_white);
		canvas.drawText("" + aqiText, width / 2, width /3*2 , paint_white); // 设置字体位置

		paint_white.setTextSize(50);
		canvas.drawText("PM2.5：73", width / 2, width /4*3, paint_white);

		canvas.drawText("By Anumbrella", width / 2, width-20, paint_white);

	}

	private void init() {
		// TODO Auto-generated method stub

		rectF = new RectF();
		paint_white = new Paint();
		paint_black = new Paint();

		color_black = getResources().getColor(R.color._FCB503);
		color_ = getResources().getColor(R.color._F09D01);
		avg = finalAngle/maxAqi; // 每度pm的aqi的值所代表的度数
		Log.v("avg", avg+"");
		Log.v("avg", ""+finalAngle);

		/**
		 * 注册一个回调函数，当一个视图树将要绘制时调用这个回调函数。 参数 listener 将要被添加的回调函数 异常
		 * IllegalStateException 如果isAlive() 返回false
		 */
		this.getViewTreeObserver().addOnPreDrawListener(
				new OnPreDrawListener() {

					@Override
					public boolean onPreDraw() {
						// TODO Auto-generated method stub
						new Thread(runnable).start();
						getViewTreeObserver().removeOnPreDrawListener(this);
						return false;
					}
				});

	}

	Runnable runnable = new Runnable() {
		int count = 0;
		int statck = 0;
		boolean flag = false;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				while (!flag) {
					switch (statck) {
					case 0:
						Thread.sleep(250);
						statck = 1;
						break;
					case 1:
						Thread.sleep(15);
						if (count <= maxAqi) {
							degree += avg;
						}
						count++;
						aqiText++; // 显示变化的数字
						postInvalidate(); // 实时更新视图
						break;

					}

					if (count >= aqi) {
						flag = true; // 大于当前空气pm指数就停止
					}

				}

			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	};

	public void setAqi(int aqi) {
		this.aqi = aqi;
		invalidate(); // 更新视图
	}

}
