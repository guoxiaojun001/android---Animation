package com.example.search;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

/**
 * @author anumbrella Created on 2015-7-18 下午7:40:48
 * 
 *         绘画带删除的搜索输入框
 */
public class DeletableEditText extends EditText {

	private Drawable mRightDrawable;

	private boolean isHasFocus;

	public DeletableEditText(Context context) {
		super(context);
		init();
		// TODO Auto-generated constructor stub
	}

	public DeletableEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public DeletableEditText(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init();
	}

	/**
	 * 初始化一些内容
	 */
	private void init() {
		// TODO Auto-generated method stub

		/**
		 * 在xml中定义的图片资源
		 * 
		 * 该方法返回包含控件左,上,右,下四个位置的Drawable的数组
		 * */
		Drawable[] drawables = this.getCompoundDrawables();

		// 取得right位置的Drawable
		// 即我们在布局文件中设置的android:drawableRight
		mRightDrawable = drawables[2];

		// 为EditText编辑框添加焦点变化监听(当前对象为编辑框)
		this.setOnFocusChangeListener(new FocusChangeListener());
		// 为EditText编辑框添加输入文字变化监听
		this.addTextChangedListener(new TextChangeListener());

		// 默认设置清除按钮图标不可见
		setCleanDrawableVisible(false);

	}

	/**
	 * 
	 * (模拟点击清除图标的操作)
	 * 
	 * 当手指抬起的位置在clean的图标的区域 我们将此视为进行清除操作
	 * 
	 * getWidth():得到控件的宽度
	 * 
	 * event.getX():抬起时的坐标(改坐标是相对于控件本身而言的)
	 * 
	 * getTotalPaddingRight():clean的图标左边缘至控件右边缘的距离
	 * 
	 * getPaddingRight():clean的图标右边缘至控件右边缘的距离 于是:
	 * 
	 * getWidth() - getTotalPaddingRight()表示: 控件左边到clean的图标左边缘的区域
	 * 
	 * getWidth() - getPaddingRight()表示: 控件左边到clean的图标右边缘的区域
	 * 所以这两者之间的区域刚好是clean的图标的区域
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			// 模拟点击了清除图标按钮的事件
			if ((event.getX() > (getWidth() - getTotalPaddingRight()))
					&& (event.getX() < (getWidth() - getPaddingRight()))) {
				setText("");
			}

			break;

		default:
			break;
		}

		return super.onTouchEvent(event);
	}

	@Override
	public boolean performClick() {
		return super.performClick();
	}

	/**
	 * 焦点获取监听类
	 * 
	 */
	private class FocusChangeListener implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			isHasFocus = hasFocus;
			if (isHasFocus) {
				// 如果编辑框有输入文字,就显示删除的图片按钮
				boolean isVisibleClean = getText().toString().length() >= 1;
				Log.i("index", getText().toString().length() + "");
				setCleanDrawableVisible(isVisibleClean);
			} else {
				setCleanDrawableVisible(false);
			}

		}

	}

	/**
	 * EditText编辑框的监听类,监听编辑框的输入的变化的改变
	 * 
	 */
	private class TextChangeListener implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			// 监听输入变化后的编辑框是否有内容
			boolean isVisible = getText().toString().length() >= 1;
			Log.i("index", isVisible + "isVisible");
			setCleanDrawableVisible(isVisible);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * 隐藏或者显示编辑框右边的清除的图片按钮
	 * 
	 **/
	private void setCleanDrawableVisible(boolean isVisibleClean) {
		// TODO Auto-generated method stub

		Drawable rightDrawable;
		if (isVisibleClean) {
			rightDrawable = mRightDrawable;
		} else {
			rightDrawable = null;
		}

		// 重新设置当前位置左,上,右,下的图片
		// 使用代码设置该控件left, top, right, and bottom处的图标
		// 重新更新四个图片的资源
		setCompoundDrawables(getCompoundDrawables()[0],
				getCompoundDrawables()[1], rightDrawable,
				getCompoundDrawables()[3]);

	}
	
	
    // 显示一个动画,以提示用户输入
    public void setShakeAnimation() {
        this.startAnimation(shakeAnimation(5));
        
    }
 
    //CycleTimes动画重复的次数
    public Animation shakeAnimation(int CycleTimes) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 10);
        translateAnimation.setInterpolator(new CycleInterpolator(CycleTimes));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }
	
	

}
