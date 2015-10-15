package com.example.anumbrella.appwelcome.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.anumbrella.appwelcome.R;

/**
 * Created by anumbrella on 15-10-9
 * <p/>
 * 实现自定义的Factory
 */
public class ParallaxFactory implements LayoutInflater.Factory {


    private final LayoutInflater.Factory mFactory;

    private ParallaxLayoutInflater mInflater;


    /**
     * 前缀数组
     */
    private static final String[] mClassPrefixList = {
            "android.widget.",
            "android.webkit.",
            "android.view."
    };

    public ParallaxFactory(ParallaxLayoutInflater inflater, LayoutInflater.Factory factory) {
        this.mFactory = factory;
        this.mInflater = inflater;
    }

    /**
     * (根据xml文件生成每一个View)
     *
     * @param name
     * @param context
     * @param attrs
     * @return
     */
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (context instanceof LayoutInflater.Factory) {
            view = ((LayoutInflater.Factory) context).onCreateView(name, context, attrs);
        }

        if (mFactory != null && view == null) {
            view = mFactory.onCreateView(name, context, attrs);
        }
        if (view == null) {
            view = createViewOrFailQuietly(name, context, attrs);
        }

        if (view != null) {
            onViewCreated(view, context, attrs);
        }
        return view;
    }


    /**
     * 创建一个视图
     *
     * @param name
     * @param context
     * @param attrs
     * @return
     */
    private View createViewOrFailQuietly(String name, Context context, AttributeSet attrs) {
        if (name.contains(".")) {
            return createViewOrFailQuietly(name, null, context, attrs);
        }

        //一旦创建成功,就退出
        String prefix = mClassPrefixList[0];
        final View view = createViewOrFailQuietly(name, prefix, context, attrs);
        if (view != null) {
            return view;
        }
        return null;
    }

    /**
     * 根据前缀创建View
     *
     * @param name
     * @param prefix
     * @param context
     * @param attrs
     * @return
     */
    private View createViewOrFailQuietly(String name, String prefix, Context context,
                                         AttributeSet attrs) {
        try {
            return mInflater.createView(name, prefix, attrs);
        } catch (Exception ignore) {
            return null;
        }

    }


    /**
     * 进行视图数据绑定
     *
     * @param view
     * @param context
     * @param attrs
     */
    private void onViewCreated(View view, Context context, AttributeSet attrs) {

        int[] attrIds = {R.attr.a_in, R.attr.a_out, R.attr.x_in,
                R.attr.x_out, R.attr.y_in, R.attr.y_out};

        TypedArray array = context.obtainStyledAttributes(attrs, attrIds);

        //建立视图数据保存（按设定的顺序进行操作）
        if (array != null) {
            if (array.length() > 0) {
                ParallaxViewTag tag = new ParallaxViewTag();
                tag.alphaIn = array.getFloat(0, 0f);
                tag.alphaOut = array.getFloat(1, 0f);
                tag.xIn = array.getFloat(2, 0f);
                tag.xOut = array.getFloat(3, 0f);
                tag.yIn = array.getFloat(4, 0f);
                tag.yOut = array.getFloat(5, 0f);
                view.setTag(R.id.parallax_view_tag, tag);
            }
            array.recycle();
        }

    }

}
