package com.example.search;

import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * @author anumbrella Created on 2015-7-21 下午4:45:50
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
	public static final int MAX = 10;

	/**
	 * 定义位移动画的类型：由外围移动到坐标点(TextView的具体位置)
	 */
	private static final int OUTSIDE_TO_LOCATION = 1;

	/**
	 * 定义位移动画的类型：由坐标点(TextView的具体位置)到外围
	 */
	private static final int LOCATION_TO_OUTSIDE = 2;

	/**
	 * 定义动画的类型：由中心点移动到坐标点(TextView的具体位置)
	 */
	private static final int CENTER_TO_LOCATION = 3;

	/**
	 * 定义动画的类型：由坐标点(TextView的具体位置)移动到中心点
	 */
	private static final int LOCATION_TO_CENTER = 4;

	/**
	 * 设置字体大小的最大最小值
	 */
	private static final int TEXT_SIZE_MAX = 20;
	private static final int TEXT_SIZE_MIN = 15;

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
	 * 是否显示动画
	 */
	private boolean enableShow;

	/**
	 * 由外到内的动画
	 */
	public static final int ANIMATION_IN = 1;

	/**
	 * 由内到外的动画
	 */

	public static final int ANIMATION_OUT = 2;

	/**
	 * 设置每个TextView的点击事件监控
	 */
	private OnClickListener itemClickListener;

	/**
	 * 定义文字TextView的动画是向内或者是向外
	 */
	private int txtTypeAnimation;

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

	public long getDuration() {
		return this.animDuration;
	}

	public void setDuration(long duration) {
		this.animDuration = duration;
	}

	/**
	 * @return
	 * 
	 *         将字符串写入到vecKeywords数组当中
	 */
	public boolean feedKeywords(String keyword) {
		boolean result = false;
		if (vecKeywords.size() < MAX) {
			result = vecKeywords.add(keyword);
		}
		return result;
	}

	/**
	 * @return
	 * 
	 *         该类从MainActivity类中启动，为该类的启动入口
	 */

	public boolean startShow(int animationType, int type) {
		// 如果时间差大于动画持续的时间就执行动画
		if (System.currentTimeMillis() - lastStartAnimationTime > animDuration) {
			enableShow = true;
			if (animationType == ANIMATION_IN) {
				if (type == 1) {
					txtTypeAnimation = OUTSIDE_TO_LOCATION;
				} else if (type == 2) {
					txtTypeAnimation = LOCATION_TO_CENTER;
				}

			} else if (animationType == ANIMATION_OUT) {

				if (type == 1) {
					txtTypeAnimation = LOCATION_TO_OUTSIDE;
				} else if (type == 2) {
					txtTypeAnimation = CENTER_TO_LOCATION;
				}
			}

			/**
			 * 某些TextView不会显示出来,隐藏掉某些TextView
			 **/
			disapper();
			boolean result = showTextView();
			Log.i("test", "result" + result);
			return result;

		}

		return false;
	}

	/**
	 * @return 显示textView的动画组件
	 */

	/**
	 * @return
	 */
	private boolean showTextView() {
		// TODO Auto-generated method stub
		// 开始时height和width都为零,vecKeywords.size()为10,enableShow为true
		// (当从onGlobalLayout()启动该方法时)width和height为手机屏幕宽度和高度

		Log.i("test", "height " + height + "  width " + width
				+ " vecKeywords.size() " + vecKeywords.size() + " enableShow "
				+ enableShow);
		if (width > 0 && height > 0 && enableShow && vecKeywords.size() > 0
				&& vecKeywords != null) {
			enableShow = false;
			// 更改动画最后显示的时间
			lastStartAnimationTime = System.currentTimeMillis();

			// 找到中心点的位置
			/**
			 * java位运算左移一位扩大2倍，右移一位缩小2倍(能被2整除正数的基本满足,其余的约为2倍);
			 * */
			int yCenter = height >> 1;
			int xCenter = width >> 1;
			// 关键字的个数。
			int size = vecKeywords.size();
			// 每个TextView分别在x轴和在y轴的长度大小
			int xItem = width / size, yItem = height / size;
			// linkedList 是基于链表实现的，在插入删除的时候性能好,查找效率低
			// arrayList在查找元素的时候性能好,插入删除效率低
			LinkedList<Integer> listX = new LinkedList<Integer>();
			LinkedList<Integer> listY = new LinkedList<Integer>();

			for (int i = 0; i < size; i++) {
				listX.add(xItem * i); // 存储每个textView在x轴上的距离
				listY.add(yItem * i + (yItem >> 2)); // 存储每个TextView在y轴上的距离,最大的距离超过了手机屏幕,后面会处理
			}
			// Log.i("test", " xCenter " + xCenter + " yCenter " + yCenter);
			// 在y轴中心上面的一些TextView
			LinkedList<TextView> listTxtTop = new LinkedList<TextView>();
			// 在y轴中心下面的一些TextView
			LinkedList<TextView> listTxtBootom = new LinkedList<TextView>();

			// 对每个TextView的位置、属性和动画进行设置
			for (int i = 0; i < size; i++) {
				// 获取字符串
				String keyword = vecKeywords.get(i);
				// 随机位置，糙值（定义方法随机获取x,y坐标的值）
				int xy[] = getRandomXY(random, listX, listY);

				// 随机设置字体的大小
				int textSize = TEXT_SIZE_MIN
						+ random.nextInt(TEXT_SIZE_MAX - TEXT_SIZE_MIN);

				// 正式实例化一个TextView用于显示
				TextView textView = new TextView(getContext());
				// 为每个TextView添加点击监控
				textView.setOnClickListener(itemClickListener);
				textView.setText(keyword);
				textView.setTextColor(Color.parseColor("#8cffffff"));
				textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

				/**
				 * 设置背景阴影
				 * 
				 * radius：阴影半径
				 * 
				 * dx：X轴方向的偏移量
				 * 
				 * dy：Y轴方向的偏移量
				 * 
				 * color：阴影颜色
				 * 
				 * 注意：如果半径被设置为0，意思就是去掉阴影。
				 * 
				 * */
				textView.setShadowLayer(1, 1, 1, 0xdd696969);
				textView.setGravity(Gravity.CENTER);

				Paint paint = textView.getPaint();
				// 返回大于等于TextView字符串长度的值
				int strWidth = (int) Math.ceil(paint.measureText(keyword));

				// 将字符串的长度保存到数组当中去
				xy[IDX_TXT_LENGTH] = strWidth;

				// 第一次修正:修正x坐标(当宽度几乎要超过屏幕了)
				if (xy[IDX_X] + strWidth > width - (xItem >> 1)) {
					int baseX = width - strWidth;
					// 减少文本右边边缘一致的概率
					xy[IDX_X] = baseX - xItem + random.nextInt(xItem >> 1);
				} else if (xy[IDX_X] == 0) {
					xy[IDX_X] = Math.max(random.nextInt(xItem), xItem / 3);
				}
				// 距离y轴中心的距离
				xy[IDX_DIS_Y] = Math.abs(xy[IDX_Y] - yCenter);
				// 通过setTog(),传递数据，在attachToScreen()中获取到数据
				// 每一个textView中都含有一个四个元素的数组，分别存储着位置信息
				textView.setTag(xy);
				if (xy[IDX_Y] > yCenter) {
					listTxtTop.add(textView);
				} else {
					listTxtBootom.add(textView);
				}
			}

			// 修正TextView的y轴的坐标值
			attachToScreen(listTxtTop, yCenter, xCenter, yItem);
			attachToScreen(listTxtBootom, yCenter, xCenter, yItem);

		}

		return false;
	}

	/**
	 * @param listTxtTop
	 * @param yCenter
	 * @param xCenter
	 * @param yItem
	 * 
	 *            修正TextView的Y坐标将将其添加到容器上
	 */
	private void attachToScreen(LinkedList<TextView> listTxt, int yCenter,
			int xCenter, int yItem) {
		// TODO Auto-generated method stub

		int size = listTxt.size();

		// 根据TextView中每个Y轴坐标距离的大小进行从小到大的排序
		sortXYList(listTxt, size);

		for (int i = 0; i < size; i++) {
			TextView textView = listTxt.get(i);
			int[] iXY = (int[]) textView.getTag();

			// 修正y轴距离的值
			int yDistance = iXY[IDX_Y] - yCenter;

			int yMove = Math.abs(yDistance);
			// 内嵌入一个for(),用于检测两个textView是否发生重叠交叉
			inner: for (int k = i - 1; k >= 0; k--) {

				int[] kXY = (int[]) listTxt.get(k).getTag();
				// TextView开始的x坐标的值
				int startX = kXY[IDX_X];
				int endX = startX + kXY[IDX_TXT_LENGTH];

				// y轴以中心点为分隔线，在判定是否在同一侧
				if (yDistance * (kXY[IDX_Y] - yCenter) > 0) {
					// 判定当前第i个TextView与前面的TextView是否有出现x轴上交叉
					if (isXMixed(startX, endX, iXY[IDX_X], iXY[IDX_X]
							+ iXY[IDX_TXT_LENGTH])) {

						// 两个TextView相差的距离
						int tmpMove = Math.abs(iXY[IDX_X] - kXY[IDX_Y]);
						if (tmpMove > yItem) {
							yMove = tmpMove;
						}else if (tmpMove <= yItem || tmpMove == 0) {							
							Log.i("test", "55555");
							yMove = yItem + Math.max(yItem, yMove >> 1)
									+ random.nextInt(yMove);
						}
						break inner;

					}
				}

			}

			if (yMove > yItem) {
				int maxMove = yMove - yItem;
				int randomMove = random.nextInt(maxMove);
				int realMove = Math.max(randomMove, maxMove >> 1) * yDistance
						/ Math.abs(yDistance);

				iXY[IDX_Y] = iXY[IDX_Y] - realMove;
				iXY[IDX_DIS_Y] = Math.abs(iXY[IDX_Y] - yCenter);

			}
			// 对已经调整过的前i个y轴距离的TextView再次进行排序
			sortXYList(listTxt, i + 1);
			// 对布局进行一些设置
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.WRAP_CONTENT,
					FrameLayout.LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.LEFT | Gravity.TOP;
			params.leftMargin = iXY[IDX_X];
			params.topMargin = iXY[IDX_Y];
			addView(textView, params);

			// 设置每个TextView的动画
			AnimationSet animationSet = getAnimationSet(iXY, xCenter, yCenter,
					txtTypeAnimation);
			textView.startAnimation(animationSet);

		}

	}

	// 设置每个TextView的动画
	private AnimationSet getAnimationSet(int[] iXY, int xCenter, int yCenter,
			int Type) {
		// TODO Auto-generated method stub
		AnimationSet animationSet = new AnimationSet(true);
		// 设置动画的插值器(设置动画为减速进入)
		animationSet.setInterpolator(interpolator);
		if (Type == OUTSIDE_TO_LOCATION) {
			animationSet.addAnimation(animAlphaToPaque);
			animationSet.addAnimation(animScaleLargeToNormal);
			TranslateAnimation translate = new TranslateAnimation((iXY[IDX_X]
					+ (iXY[IDX_TXT_LENGTH] >> 1) - xCenter) << 1, 0, iXY[IDX_Y]
					- yCenter, 0);
			animationSet.addAnimation(translate);

		} else if (Type == CENTER_TO_LOCATION) {
			animationSet.addAnimation(animAlphaToPaque);
			animationSet.addAnimation(animScaleLargeToNormal);
			TranslateAnimation translate = new TranslateAnimation(xCenter
					- iXY[IDX_X], 0, yCenter - iXY[IDX_Y], 0);
			animationSet.addAnimation(translate);

		} else if (Type == LOCATION_TO_CENTER) {
			animationSet.addAnimation(animAlphaToTransparent);
			animationSet.addAnimation(animScaleNormalToZero);
			TranslateAnimation translate = new TranslateAnimation(0, xCenter
					- iXY[IDX_X], 0, yCenter - iXY[IDX_Y]);
			animationSet.addAnimation(translate);

		} else if (Type == LOCATION_TO_OUTSIDE) {
			animationSet.addAnimation(animAlphaToTransparent);
			animationSet.addAnimation(animScaleNormalToLarge);
			TranslateAnimation translate = new TranslateAnimation(0,
					(iXY[IDX_X] + (iXY[IDX_TXT_LENGTH] >> 1) - xCenter) << 1,
					0, iXY[IDX_Y] - yCenter);
			animationSet.addAnimation(translate);

		}

		animationSet.setDuration(animDuration);
		return animationSet;
	}

	/**
	 * 
	 * 判定两个TextView是否在x轴上出现交集的情况
	 * 
	 * @param startA
	 * @param endA
	 * @param startB
	 * @param endB
	 * @return
	 */
	private boolean isXMixed(int startA, int endA, int startB, int endB) {
		// TODO Auto-generated method stub
		boolean result = false;

		if (startA >= startB && startA <= endB) {
			result = true;
		} else if (endA >= startB && endA <= endB) {
			result = true;
		} else if (startB >= startA && startB <= endA) {
			result = true;
		} else if (endB >= startB && endB <= endA) {
			result = true;
		}

		return result;
	}

	/**
	 * 
	 * 根据与中心点的距离由近到远进行冒泡排序。
	 * 
	 * @param listTxt
	 * @param endIndx
	 */
	private void sortXYList(LinkedList<TextView> listTxt, int endIndx) {
		// TODO Auto-generated method stub
		for (int i = 0; i < endIndx; i++) {
			for (int k = i + 1; k < endIndx; k++) {
				if (((int[]) listTxt.get(k).getTag())[IDX_DIS_Y] < ((int[]) listTxt
						.get(i).getTag())[IDX_DIS_Y]) {
					TextView iTmp = listTxt.get(i);
					TextView kTmp = listTxt.get(k);
					listTxt.set(i, kTmp);
					listTxt.set(k, iTmp);
				}
			}
		}

	}

	private int[] getRandomXY(Random ran, LinkedList<Integer> listX,
			LinkedList<Integer> listY) {
		// TODO Auto-generated method stub
		int arr[] = new int[4];
		// 删除掉某个值，会返回当前的值
		arr[IDX_X] = listX.remove(ran.nextInt(listX.size()));
		arr[IDX_Y] = listY.remove(ran.nextInt(listY.size()));
		return arr;
	}

	// 根据textView的状态隐藏掉某些
	private void disapper() {
		// TODO Auto-generated method stub
		// 获取所有的textView的组件
		// 开始时size会为零,布局当中还没有组件
		int size = getChildCount();
		Log.i("test", "size " + size);

		for (int i = size - 1; i >= 0; i--) {
			final TextView txt = (TextView) getChildAt(i);
			// 如果视图不可见就删除掉
			if (txt.getVisibility() == TextView.GONE) {
				removeView(txt);
				continue;
			}
			// 获取每个可显示的txt的margin值
			LayoutParams params = (LayoutParams) txt.getLayoutParams();

			int[] xy = new int[] { params.leftMargin, params.topMargin,
					txt.getWidth() };

			// 活动动画的属性
			AnimationSet animationSet = getAnimationSet(xy, width >> 1,
					height >> 1, txtTypeAnimation);
			// 设置动画的监听类
			animationSet.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					txt.setOnClickListener(null);
					txt.setClickable(false);
					txt.setVisibility(View.GONE);

				}
			});
			txt.startAnimation(animationSet);

		}

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
		// 获得布局的高和宽(当前对象组件为frameLayout)
		int tmpWidth = getWidth();
		int tmpHeight = getHeight();
		if (tmpHeight != height || tmpWidth != width) {
			this.height = tmpHeight;
			this.width = tmpWidth;
		}
		// 布局视图或视图组件的状态发生改变时,再次调用显示textView
		showTextView();

	}

	public void setOnItemClickListener(OnClickListener listener) {
		this.itemClickListener = listener;
	}

	public Vector<String> getKeywords() {
		return vecKeywords;
	}

	/**
	 * 直接清除所有的TextView。在清除之前不会显示动画。
	 */
	public void rubAllViews() {
		removeAllViews();
	}

	public void rubKeywords() {
		vecKeywords.clear();
	}

}
