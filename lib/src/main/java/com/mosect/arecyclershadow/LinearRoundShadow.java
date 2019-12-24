package com.mosect.arecyclershadow;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mosect.ashadow.RoundShadow;
import com.mosect.ashadow.Shadow;
import com.mosect.ashadow.ShadowManager;
import com.mosect.ashadow.UnsupportedKeyException;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView线性布局圆角阴影
 */
public class LinearRoundShadow extends RecyclerView.ItemDecoration {

    private List<Block> blocks;
    private Rect drawRect = new Rect();
    private Block tempBlock;

    @Override
    public void onDraw(
            @NonNull Canvas canvas,
            @NonNull RecyclerView parent,
            @NonNull RecyclerView.State state) {
        RecyclerView.LayoutManager lm = parent.getLayoutManager();
        if (lm instanceof LinearLayoutManager && null != blocks) {
            LinearLayoutManager llm = (LinearLayoutManager) lm;
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                int pos = parent.getChildAdapterPosition(child);
                for (Block block : blocks) {
                    if (block.match(pos)) {
                        // 此item匹配阴影块
                        if (null == block.shadowKey) continue;
                        if (null == block.shadow) {
                            // 没有阴影，创建一个阴影
                            try {
                                block.shadow = ShadowManager.getDefault().get(block.shadowKey);
                            } catch (UnsupportedKeyException e) {
                                e.printStackTrace();
                            }
                            if (null == block.shadow) continue;
                        }
                        if (block.shadowKey.hasRound()) {
                            // 有圆角
                            if (llm.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                                drawHorizontal(canvas, block, pos, child);
                            } else if (llm.getOrientation() == LinearLayoutManager.VERTICAL) {
                                drawVertical(canvas, block, pos, child);
                            }
                        } else {
                            // 无圆角
                            drawRect.set(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
                            block.shadow.draw(canvas, drawRect, null);
                        }
                    }
                }
            }
        }
    }

    /**
     * 绘制水平方向阴影
     *
     * @param canvas 画布
     * @param block  阴影块
     * @param pos    下标
     * @param child  子视图
     */
    private void drawHorizontal(Canvas canvas, Block block, int pos, View child) {
        // 块第一个和最后一个，需要靠边多绘制圆角矩形半径
        int startOffset, endOffset;
        if (pos != block.start) {
            startOffset = (int) Math.max(
                    block.shadowKey.radii[0],
                    block.shadowKey.radii[2]
            );
        } else {
            startOffset = 0;
        }
        if (pos != block.end) {
            endOffset = (int) Math.max(
                    block.shadowKey.radii[4],
                    block.shadowKey.radii[6]
            );
        } else {
            endOffset = 0;
        }

        // 如果存在绘制偏移（第一个或最后一个），需要clip子视图本身
        int clipStart = startOffset == 0 ?
                (int) (child.getLeft() - block.shadowKey.shadowRadius) : child.getLeft();
        int clipEnd = endOffset == 0 ?
                (int) (child.getRight() + block.shadowKey.shadowRadius) : child.getRight();
        int clipTop = (int) (child.getTop() - block.shadowKey.shadowRadius);
        int clipBottom = (int) (child.getBottom() + block.shadowKey.shadowRadius);
        drawRect.left = child.getLeft() - startOffset;
        drawRect.top = child.getTop();
        drawRect.right = child.getRight() + endOffset;
        drawRect.bottom = child.getBottom();

        int sc = canvas.save();
        canvas.clipRect(clipStart, clipTop, clipEnd, clipBottom);
        block.shadow.draw(canvas, drawRect, null);
        canvas.restoreToCount(sc);
    }

    /**
     * 绘制垂直方向阴影
     *
     * @param canvas 画布
     * @param block  阴影块
     * @param pos    下标
     * @param child  子视图
     */
    private void drawVertical(Canvas canvas, Block block, int pos, View child) {
        // 块第一个和最后一个，需要靠边多绘制圆角矩形半径
        int startOffset, endOffset;
        if (pos != block.start) {
            startOffset = (int) Math.max(
                    block.shadowKey.radii[1],
                    block.shadowKey.radii[3]
            );
        } else {
            startOffset = 0;
        }
        if (pos != block.end) {
            endOffset = (int) Math.max(
                    block.shadowKey.radii[5],
                    block.shadowKey.radii[7]
            );
        } else {
            endOffset = 0;
        }

        // 如果存在绘制偏移（第一个或最后一个），需要clip子视图本身
        int clipStart = startOffset == 0 ?
                (int) (child.getTop() - block.shadowKey.shadowRadius) : child.getTop();
        int clipEnd = endOffset == 0 ?
                (int) (child.getBottom() + block.shadowKey.shadowRadius) : child.getBottom();
        int clipLeft = (int) (child.getLeft() - block.shadowKey.shadowRadius);
        int clipRight = (int) (child.getRight() + block.shadowKey.shadowRadius);
        drawRect.left = child.getLeft();
        drawRect.top = child.getTop() - startOffset;
        drawRect.right = child.getRight();
        drawRect.bottom = child.getBottom() + endOffset;

        int sc = canvas.save();
        canvas.clipRect(clipLeft, clipStart, clipRight, clipEnd);
        block.shadow.draw(canvas, drawRect, null);
        canvas.restoreToCount(sc);
    }

    /**
     * 创建一个阴影块
     *
     * @return 已添加的阴影块
     */
    public Block block() {
        Block block = new Block();
        addBlock(block);
        return block;
    }

    /**
     * 添加一个阴影块
     *
     * @param block 阴影块
     */
    public void addBlock(@NonNull Block block) {
        if (null == blocks) {
            blocks = new ArrayList<>();
        }
        blocks.add(block);
    }

    /**
     * 移除一个阴影块
     *
     * @param block 阴影块
     * @return true，移除成功；否则移除失败
     */
    public boolean removeBlock(@NonNull Block block) {
        if (null != blocks) {
            return blocks.remove(block);
        }
        return false;
    }

    /**
     * 清空阴影块
     */
    public void clearBlocks() {
        if (null != blocks) {
            blocks.clear();
        }
    }

    /**
     * 阴影块，跨item绘制
     */
    public static class Block {

        Shadow shadow;
        private RoundShadow.Key shadowKey;
        private int start = -1;
        private int end = -1;

        boolean match(int pos) {
            return start >= 0 && end >= start && pos >= start && pos <= end;
        }

        /**
         * 获取圆角阴影key（阴影信息）
         *
         * @return 圆角阴影key
         */
        public RoundShadow.Key getShadowKey() {
            return shadowKey;
        }

        /**
         * 设置圆角阴影key
         *
         * @param shadowKey 阴影key
         * @return this
         */
        public Block setShadowKey(RoundShadow.Key shadowKey) {
            this.shadowKey = shadowKey;
            return this;
        }

        /**
         * 获取阴影块开始的下标
         *
         * @return 开始的下标
         */
        public int getStart() {
            return start;
        }

        /**
         * 设置阴影块开始的下标
         *
         * @param start 开始的下标
         * @return this
         */
        public Block setStart(int start) {
            this.start = start;
            return this;
        }

        /**
         * 获取阴影块结束的下标
         *
         * @return 开始的下标
         */
        public int getEnd() {
            return end;
        }


        /**
         * 设置阴影块结束的下标
         *
         * @param end 结束的下标
         * @return this
         */
        public Block setEnd(int end) {
            this.end = end;
            return this;
        }
    }
}
