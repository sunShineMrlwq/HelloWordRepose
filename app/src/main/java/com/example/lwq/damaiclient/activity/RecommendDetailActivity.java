package com.example.lwq.damaiclient.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.transition.Explode;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lwq.damaiclient.R;
import com.example.lwq.damaiclient.app.BaseActivity;
import com.example.lwq.damaiclient.widget.AppBaryx;

/**
 * Created by lwq on 2015/12/8.
 */
public class RecommendDetailActivity extends BaseActivity implements View.OnClickListener {
    private AppBaryx appBaryx;
    private TextView header_back,type_city, header_tab, header_title,header_conetnt;
    private ImageView imgae_right;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
//        getWindow().setEnterTransition(new Explode());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_detail);
        initView();
    }

    public void initView() {//初始化布局
        appBaryx = (AppBaryx) findViewById(R.id.footbar);
        header_back= appBaryx.getHeader_back();
        type_city = appBaryx.getType_city();
        header_tab = appBaryx.getHeader_tab();
        header_title = appBaryx.getHeader_titletv();
        imgae_right = appBaryx.getImgae_right();
        header_conetnt= appBaryx.getHeader_conetnt();
        header_back.setText("返回");
        header_tab.setText("戏曲详情");
        header_tab.setVisibility(View.VISIBLE);
        header_back.setVisibility(View.VISIBLE);
        header_conetnt.setVisibility(View.GONE);
        type_city.setVisibility(View.GONE);
        header_title.setVisibility(View.GONE);
        imgae_right.setVisibility(View.GONE);


        header_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_back:
                onBackPressed();
//                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
            break;
        }

    }
}
