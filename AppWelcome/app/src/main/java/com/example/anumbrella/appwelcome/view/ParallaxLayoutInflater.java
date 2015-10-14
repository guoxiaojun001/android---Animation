package com.example.anumbrella.appwelcome.view;

import android.content.Context;
import android.view.LayoutInflater;

/**
 * Created by anumbrella on 15-10-9.
 * <p/>
 * 自定义的LayoutInflater
 */
public class ParallaxLayoutInflater extends LayoutInflater {


    protected ParallaxLayoutInflater(LayoutInflater original, Context newContext) {
        super(original, newContext);
        setUpLayoutFactory();
    }

    /**
     * 绑定自定义的Factory
     */
    private void setUpLayoutFactory() {
        //如果当前的Factory不是自定义的Factory，就进行绑定
        if (!(getFactory() instanceof ParallaxFactory)) {
            setFactory(new ParallaxFactory(this, getFactory()));
        }
    }

    /**
     * 如果要在自己的views中通过LayoutInflater.Factory来创建LayoutInflater
     * 可以用cloneInContext(Context)来克隆一个当前存在的ViewFactory
     * 然后再用setFactory(LayoutInfalter.FActory) 来设置成自己的Factory.
     *
     * @param newContext
     * @return
     */
    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return new ParallaxLayoutInflater(this, newContext);
    }
}
