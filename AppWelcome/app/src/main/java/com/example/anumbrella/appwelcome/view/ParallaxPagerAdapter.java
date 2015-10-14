package com.example.anumbrella.appwelcome.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

/**
 * Created by anumbrella on 15-10-9.
 * <p/>
 * ViewPager页面适配器
 */
public class ParallaxPagerAdapter extends PagerAdapter {


    private int count = 0;

    private final Context mContext;

    /**
     * 用于缓存下一个要显示的视图
     */
    private final LinkedList<View> recycleBin = new LinkedList<View>();

    public ParallaxPagerAdapter(Context context) {
        this.mContext = context;
    }


    /**
     * 设定页面的总数
     *
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }


    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


    /**
     * return一个对象，这个对象表明了PagerAdapter适配器
     * 选择哪个对象放在当前的ViewPager中
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view;
        if (!recycleBin.isEmpty()) {
            view = recycleBin.pop();
        } else {
            view = new View(mContext);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }
        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container,int position,Object object){
        View view = (View) object;
        //这个视图是当前视图的下一个(PagerAdapter启动会自动调用两次instantiateItem()方法)
        container.removeView(view);
        //添加下一个视图到栈的顶部
        recycleBin.push(view);

    }

}
