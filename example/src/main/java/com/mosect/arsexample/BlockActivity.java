package com.mosect.arsexample;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mosect.arecyclershadow.LinearRoundShadow;
import com.mosect.ashadow.RoundShadow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BlockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);
        RecyclerView rvContent = findViewById(R.id.rv_content);
        rvContent.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));


        final List<String> data = new ArrayList<>();
        rvContent.setAdapter(new RecyclerView.Adapter<Item2Holder>() {
            @NonNull
            @Override
            public Item2Holder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
                View view = getLayoutInflater().inflate(R.layout.item2, parent, false);
                final Item2Holder holder = new Item2Holder(view);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        String text = "点击了Item" + (pos + 1);
                        Toast.makeText(BlockActivity.this, text, Toast.LENGTH_SHORT).show();
                    }
                });
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull Item2Holder holder, int position) {
                holder.tvName.setText(data.get(position));
            }

            @Override
            public int getItemCount() {
                return data.size();
            }
        });

        final List<Integer> blockSizes = new ArrayList<>();
        // 添加间距计算器
        rvContent.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(
                    @NonNull Rect outRect,
                    @NonNull View view,
                    @NonNull RecyclerView parent,
                    @NonNull RecyclerView.State state) {
                int pos = parent.getChildAdapterPosition(view);
                int blockStart = 0;
                int count = 0;
                for (int bs : blockSizes) {
                    blockStart = count;
                    count += bs;
                    if (pos < count) {
                        // 表示此Item在此块
                        break;
                    }
                }
                if (pos == blockStart) {
                    // 块的第一个item，需要增加上边距
                    outRect.top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            20, getResources().getDisplayMetrics());
                }
                if (pos == data.size() - 1) {
                    // 最后一个item，增加下边距
                    outRect.bottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            20, getResources().getDisplayMetrics());
                }
                // 左右边距任何情况都存在
                outRect.left = outRect.right = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 11, getResources().getDisplayMetrics());
            }
        });
        // 添加块阴影绘制器
        LinearRoundShadow shadow = new LinearRoundShadow();
        rvContent.addItemDecoration(shadow);

        // 产生item
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            String text = "这是Item" + (i + 1);
            data.add(text);
        }

        // 分块展示
        int index = 0;
        while (index < data.size()) {
            int count = Math.min(15, data.size() - index);
            // 计算一块占用多少item
            int blockSize = random.nextInt(count) + 1;
            blockSizes.add(blockSize);
            // 产生块阴影
            LinearRoundShadow.Block block = shadow.block();
            // 设置块位置
            block.setStart(index).setEnd(index + blockSize - 1);
            // 产生阴影key
            RoundShadow.Key key = new RoundShadow.Key();
            // 阴影圆角
            float round = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                    getResources().getDisplayMetrics());
            key.radii = new float[8];
            Arrays.fill(key.radii, round);
            // 阴影填充色
            key.solidColor = Color.parseColor("#ffffff");
            // 阴影半径
            key.shadowRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5,
                    getResources().getDisplayMetrics());
            // 阴影颜色
            key.shadowColor = Color.parseColor("#0d000000");
            // 将阴影信息设置到块阴影信息中
            block.setShadowKey(key);

            index += blockSize;
        }
    }

    static class Item2Holder extends RecyclerView.ViewHolder {
        TextView tvName;

        Item2Holder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }
}
