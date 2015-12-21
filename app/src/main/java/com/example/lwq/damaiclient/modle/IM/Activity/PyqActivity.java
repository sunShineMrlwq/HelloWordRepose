package com.example.lwq.damaiclient.modle.IM.Activity;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lwq.damaiclient.R;
import com.example.lwq.damaiclient.adapter.RecommendAdapter;
import com.example.lwq.damaiclient.app.BaseActivity;
import com.example.lwq.damaiclient.modle.IM.adapter.PYQAdapter;
import com.example.lwq.damaiclient.utils.BlurUtil;
import com.example.lwq.damaiclient.utils.ListViewHeightBased;
import com.example.lwq.damaiclient.widget.AppBaryx;
import com.hp.hpl.sparta.xpath.ThisNodeTest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lwq on 2015/12/8.
 */
public class PyqActivity extends BaseActivity implements View.OnClickListener {
    private AppBaryx appBaryx;
    private TextView header_back,type_city, header_tab, header_title,header_conetnt;
    private ImageView imgae_right,fragment_my_center_to;
     ListView dmListView;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
//        getWindow().setEnterTransition(new Explode());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pyq);
        initView();
        setBackground(R.mipmap.user);
        initData();
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
        fragment_my_center_to = (ImageView) findViewById(R.id.fragment_my_center_to);
        dmListView= (ListView) findViewById(R.id.DmListView);
        header_back.setOnClickListener(this);
    }
    //设置背景虚化
    private void setBackground(int id){
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), id);//从资源文件中得到图片，并生成Bitmap图片
        final Bitmap blurBmp = BlurUtil.fastblur(this, bmp, 10);//0-25，表示模糊值
        final Drawable newBitmapDrawable = new BitmapDrawable(blurBmp); // 将Bitmap转换为Drawable
        fragment_my_center_to.post(new Runnable(){//调用UI线程
            @Override
            public void run() {
                fragment_my_center_to.setBackgroundDrawable(newBitmapDrawable);//设置背景
            }
        });
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
    public void  initData(){
        List<String> list=new ArrayList<String>();
        for(int i=0;i<20;i++){
            list.add("");
        }
        PYQAdapter recommendAdapter=new PYQAdapter(this,list);
        dmListView.setAdapter(recommendAdapter);
        ListViewHeightBased.setListViewHeightBasedOnChildren(dmListView);
    }
}
