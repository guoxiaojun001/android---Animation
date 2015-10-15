package com.example.anumbrella.appwelcome.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.anumbrella.appwelcome.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anumbrella on 15-10-9.
 * <p/>
 * 视差滚动
 */
public class ParallaxContainer extends FrameLayout {


    private ViewPager viewPager;


    public ViewPager.OnPageChangeListener mPagerChangeListener;

    /**
     * 是否可以无限循环
     */
    private boolean isLooping = false;

    /**
     * 滑动页面的数目
     */
    private int pageCount = 0;


    /**
     * ViewPager适配器
     */
    private final ParallaxPagerAdapter adapter;


    public int currentPosition = 0;

    /**
     * 页面布局的宽度
     */
    private int containerWidth = 0;


    /**
     * 页面背景动画ImageView
     */
    private ImageView mImageView;

    private List<View> listView = new ArrayList<View>();


    /**
     * 存储所用的视图的列表
     */
    private List<View> parallaxViews = new ArrayList<View>();


    /**
     * 是否结束了滑动标识
     */
    private boolean isEnd = false;


    /**
     * 设定动画持续的时间
     */
    private final long DELAY_TIME = 900;


    public ParallaxContainer(Context context) {
        super(context);
        adapter = new ParallaxPagerAdapter(context);
    }

    public ParallaxContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        adapter = new ParallaxPagerAdapter(context);
    }

    public ParallaxContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        adapter = new ParallaxPagerAdapter(context);
    }


    public void setLooping(boolean looping) {
        this.isLooping = looping;
        updateApapterCount();
    }


    private void updateApapterCount() {
        adapter.setCount(isLooping ? Integer.MAX_VALUE : pageCount);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        containerWidth = getMeasuredWidth();
        if (viewPager != null) {
            //更新滑动页面
            mPagerChangeListener.onPageScrolled(viewPager.getCurrentItem(), 0, 0);
        }
        super.onWindowFocusChanged(hasFocus);
    }


    public void setImage(ImageView imageView) {
        this.mImageView = imageView;
    }


    /**
     * 设定页面资源和视图滑动消失的方式
     *
     * @param inflater
     * @param childIds
     */
    public void setupChildren(LayoutInflater inflater, int... childIds) {
        if (getChildCount() > 0) {
            throw new RuntimeException("setupChildren只能使用一次,当前父布局为空时");
        }

        ParallaxLayoutInflater parallaxLayoutInflater = new ParallaxLayoutInflater(inflater, getContext());
        for (int childId : childIds) {
            //将视图布局添加到当前的布局当中去
            View view = parallaxLayoutInflater.inflate(childId, this);
            listView.add(view);
        }

        pageCount = getChildCount();
        for (int i = 0; i < pageCount; i++) {
            //获得子视图布局
            View view = getChildAt(i);
            addParallaxView(view, i);
        }

        //pageCount被更新了的
        updateApapterCount();

        viewPager = new ViewPager(getContext());
        viewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        viewPager.setId(R.id.parallax_pager);
        //设定ViewPager页面变化监听
        attachOnPageChangeListener();
        viewPager.setAdapter(adapter);
        addView(viewPager, 0);


    }

    /**
     * 设定ViewPager页面变化监听类
     */
    private void attachOnPageChangeListener() {

        mPagerChangeListener = new ViewPager.OnPageChangeListener() {

            private boolean isLeft = false;


            /**
             *
             * 当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法回一直得到
             * 调用。其中三个参数的含义分别为：
             * pageIndex:当前页面，及你点击滑动的页面
             * offset:当前页面偏移的百分比
             * offsetPixels:当前页面偏移的像素位置
             *
             */
            @Override
            public void onPageScrolled(int pageIndex, float offset, int offsetPixels) {

                //当滑动的像素距离小于10,不会进入下一个页面
                if (offsetPixels < 10) {
                    isLeft = false;
                }

                if (pageCount > 0) {
                    pageIndex = pageIndex % pageCount;
                }

                //当滑动到第4张图片
                if (pageIndex == 3) {

                    if (isLeft) {

                    } else {
                        //将图片移动到屏幕的左边去
                        mImageView.setX(mImageView.getLeft() - offsetPixels);
                    }
                }

                ParallaxViewTag tag;
                //遍历所有图片
                for (View view : parallaxViews) {
                    tag = (ParallaxViewTag) view.getTag(R.id.parallax_view_tag);
                    if (tag == null) {
                        continue;
                    }

                    if ((pageIndex == tag.index - 1 || isLooping) && containerWidth != 0) {

                        view.setVisibility(VISIBLE);
                        //设置屏幕视图沿着x轴旋转
                        view.setRotationX((containerWidth - offsetPixels) * tag.xIn);

                        //设置屏幕右边进入(向右方滑动)(offsetPixels 变化为从大——>小)
                        view.setTranslationX((containerWidth - offsetPixels) * tag.xIn);

                        //设置从顶部进入
                        view.setTranslationY(0 - (containerWidth - offsetPixels) * tag.yIn);

                        //设置透明度变化
                        view.setAlpha(1.0f - (containerWidth - offsetPixels) * tag.alphaIn / containerWidth);

                    } else if (pageIndex == tag.index) {


                        view.setVisibility(VISIBLE);
                        //设置屏幕视图沿着x轴旋转
                        view.setRotationX(0 - offsetPixels * tag.xOut);
                        //设置屏幕左边离开(向左方滑动)(offsetPixels 变化为小——>大)
                        view.setTranslationX(0 - offsetPixels * tag.xOut);

                        //设置从顶部离开
                        view.setTranslationY(0 - offsetPixels * tag.yOut);

                        //设置透明的变化
                        view.setAlpha(1.0f - offsetPixels * tag.alphaOut / containerWidth);
                    } else {
                        view.setVisibility(GONE);
                    }
                }
            }

            /**
             * 设定滑动选中的页面的index
             * @param position
             */
            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }

            /**
             * 状态改变监听
             *
             * @param state
             */
            @Override
            public void onPageScrollStateChanged(int state) {
                //设定动画背景
                mImageView.setBackgroundResource(R.drawable.man_run);
                final AnimationDrawable animationDrawable = (AnimationDrawable) mImageView.getBackground();
                switch (state) {
                    //什么都不做
                    case 0:
                        finishAnim(animationDrawable);
                        break;
                    //正在滑动
                    case 1:
                        //开始动画
                        isEnd = false;
                        animationDrawable.start();
                        break;
                    //滑动结束

                    case 2:
                        finishAnim(animationDrawable);
                        break;
                }
            }
        };


        viewPager.setOnPageChangeListener(mPagerChangeListener);

    }

    /**
     * 停止动画
     *
     * @param animationDrawable
     */
    private void finishAnim(final AnimationDrawable animationDrawable) {

        //如果结束了,什么也不做，直接返回
        if (isEnd) {
            return;
        }
        isEnd = true;

        //设置动画持续的时间
        final long delay = DELAY_TIME;
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (delay > 0) {
                    try {
                        //按时间延迟结束动画
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (animationDrawable.isRunning() && isEnd) {
                    animationDrawable.stop();
                }
            }
        }).start();


    }

    /**
     * 给每个视图设定索引指针index
     *
     * @param view
     * @param i
     */
    private void addParallaxView(View view, int pageIndex) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0, childCount = viewGroup.getChildCount(); i < childCount; i++) {
                addParallaxView(viewGroup.getChildAt(i), pageIndex);
            }
        }

        //获取视图中绑定的数据
        ParallaxViewTag tag = (ParallaxViewTag) view.getTag(R.id.parallax_view_tag);
        if (tag != null) {
            //给每个ViewPager的页面的所用的视图设定索引
            tag.index = pageIndex;
            parallaxViews.add(view);
        }

    }


}
