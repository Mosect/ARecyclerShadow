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

import com.mosect.arecyclershadow.SingleItemShadow;
import com.mosect.ashadow.RoundShadow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SingleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        RecyclerView rvContent = findViewById(R.id.rv_content);

        final List<String> data = new ArrayList<>();
        // 设置适配器
        rvContent.setAdapter(new RecyclerView.Adapter<Item1Holder>() {
            @NonNull
            @Override
            public Item1Holder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
                View view = getLayoutInflater().inflate(R.layout.item1, parent, false);
                Item1Holder holder = new Item1Holder(view);
                holder.tvText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(SingleActivity.this, "点击了文本", Toast.LENGTH_SHORT).show();
                    }
                });
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull Item1Holder holder, int position) {
                holder.tvText.setText(data.get(position));
            }

            @Override
            public int getItemCount() {
                return data.size();
            }
        });

        // Item间距
        rvContent.addItemDecoration(new RecyclerView.ItemDecoration() {
            int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11,
                    getResources().getDisplayMetrics());

            @Override
            public void getItemOffsets(
                    @NonNull Rect outRect,
                    @NonNull View view,
                    @NonNull RecyclerView parent,
                    @NonNull RecyclerView.State state) {
                outRect.top = outRect.left = outRect.right = margin;
                int pos = parent.getChildAdapterPosition(view);
                if (pos == data.size() - 1) {
                    outRect.bottom = margin;
                }
            }
        });
        // 设置布局管理
        rvContent.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        // Item阴影
        RoundShadow.Key shadowKey = new RoundShadow.Key();
        shadowKey.solidColor = Color.parseColor("#ffffff");
        shadowKey.shadowColor = Color.parseColor("#0d000000");
        shadowKey.shadowRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                getResources().getDisplayMetrics());
        shadowKey.radii = new float[8];
        float round = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                getResources().getDisplayMetrics());
        Arrays.fill(shadowKey.radii, round);
        SingleItemShadow itemShadow = new SingleItemShadow(shadowKey);
        rvContent.addItemDecoration(itemShadow);

        // 添加item数据
        String[] texts = {
                "单车欲问边，属国过居延。\n征蓬出汉塞，归雁入胡天。\n大漠孤烟直，长河落日圆。\n萧关逢候骑，都护在燕然。",
                "水光潋滟晴方好，山色空蒙雨亦奇。\n欲把西湖比西子，淡妆浓抹总相宜。",
                "古木阴中系短篷，杖藜扶我过桥东。\n沾衣欲湿杏花雨，吹面不寒杨柳风。",
                "人言落日是天涯，望极天涯不见家。\n已恨碧山相阻隔，碧山还被暮云遮。",
                "洛阳城东西，长作经时别。\n昔去雪如花，今来花似雪。"
        };
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            data.add(texts[random.nextInt(texts.length)]);
        }

    }

    static class Item1Holder extends RecyclerView.ViewHolder {
        TextView tvText;

        Item1Holder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tv_text);
        }
    }
}
