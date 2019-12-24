package com.mosect.arecyclershadow;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mosect.ashadow.Shadow;
import com.mosect.ashadow.ShadowManager;
import com.mosect.ashadow.UnsupportedKeyException;

import java.util.HashMap;
import java.util.Map;

/**
 * item阴影
 */
public abstract class ShadowItemDecoration
        extends RecyclerView.ItemDecoration {

    private Map<Object, Shadow> shadowMap;
    private Rect shadowRect = new Rect();

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child.getVisibility() != View.VISIBLE) continue;
            Object shadowKey = getShadowKey(parent, child);
            if (null != shadowKey) {
                Shadow shadow = null;
                if (null != shadowMap) {
                    shadow = shadowMap.get(shadowKey);
                }
                if (null == shadow) {
                    try {
                        shadow = ShadowManager.getDefault().get(shadowKey);
                        if (null == shadowMap) {
                            shadowMap = new HashMap<>();
                        }
                        shadowMap.put(shadow.getKey(), shadow);
                        draw(c, parent, child, shadow);
                    } catch (UnsupportedKeyException e) {
                        e.printStackTrace();
                    }
                } else {
                    draw(c, parent, child, shadow);
                }
            }
        }
    }

    private void draw(Canvas canvas, RecyclerView parent, View child, Shadow shadow) {
        shadowRect.setEmpty();
        getShadowRect(parent, child, shadowRect);
        if (!shadowRect.isEmpty()) {
            onShadowDraw(canvas, parent, child, shadow, shadowRect);
        }
    }

    /**
     * 绘制阴影
     *
     * @param canvas     画布
     * @param parent     父视图
     * @param child      子视图
     * @param shadow     阴影
     * @param shadowRect 阴影范围
     */
    protected void onShadowDraw(
            @NonNull Canvas canvas,
            @NonNull RecyclerView parent,
            @NonNull View child,
            @NonNull Shadow shadow,
            @NonNull Rect shadowRect) {
        shadow.draw(canvas, shadowRect, null);
    }

    /**
     * 获取阴影范围
     *
     * @param parent 父视图
     * @param child  子视图
     * @param out    阴影范围保存至此对象
     */
    protected void getShadowRect(@NonNull RecyclerView parent, @NonNull View child, @NonNull Rect out) {
        out.set(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
    }

    /**
     * 获取阴影key
     *
     * @param parent 父视图
     * @param child  子视图
     * @return 阴影key，返回null表示没有阴影
     */
    @Nullable
    protected abstract Object getShadowKey(@NonNull RecyclerView parent, @NonNull View child);
}
