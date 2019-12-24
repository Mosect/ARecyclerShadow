package com.mosect.arecyclershadow;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 单个阴影，表示整个RecyclerView的Item都使用一个阴影
 */
public class SingleItemShadow extends ShadowItemDecoration {

    private Object shadowKey;

    /**
     * SingleItemShadow对象构建
     *
     * @param shadowKey 阴影key，目前只实现了圆角矩形阴影
     *                  {@link com.mosect.ashadow.RoundShadow.Key}
     */
    public SingleItemShadow(Object shadowKey) {
        this.shadowKey = shadowKey;
    }

    public SingleItemShadow() {
    }

    /**
     * 设置阴影Key
     *
     * @param shadowKey 阴影key，目前只实现了圆角矩形阴影
     *                  {@link com.mosect.ashadow.RoundShadow.Key}
     */
    public void setShadowKey(Object shadowKey) {
        this.shadowKey = shadowKey;
    }

    public Object getShadowKey() {
        return shadowKey;
    }

    @Nullable
    @Override
    protected Object getShadowKey(@NonNull RecyclerView parent, @NonNull View child) {
        return shadowKey;
    }
}
