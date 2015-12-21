package com.example.lwq.damaiclient.fragment;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lwq.damaiclient.R;
import com.example.lwq.damaiclient.activity.RecommendDetailActivity;
import com.example.lwq.damaiclient.adapter.RecommendAdapter;
import com.example.lwq.damaiclient.app.BaseFragment;
import com.example.lwq.damaiclient.utils.AnimationUtils;
import com.example.lwq.damaiclient.utils.DialogUtils;
import com.example.lwq.damaiclient.utils.ListViewHeightBased;
import com.example.lwq.damaiclient.widget.AppBaryx;
import com.example.lwq.damaiclient.widget.DmListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by lwq on 2015/12/4.
 */
public class RecommendFragment extends BaseFragment implements View.OnClickListener {
    View root;
    private LinearLayout viewGroup;
    private DmListView dmListView;
    private ViewPager guidePages;
    private ScheduledExecutorService scheduledExecutorService;
    private int currentItem;
    private ImageView[] imageViews;
    private AppBaryx appBaryx;
    private TextView type_city, header_tab, header_title,header_back;
    private ImageView imgae_right;
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            guidePages.setCurrentItem(currentItem);
        };
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(null==root){
            root=inflater.inflate(R.layout.fragment_recommend,null);
            initView();
            isPrepared=true;
//            lazyLoad();
        }
        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) root.getParent();
        if (parent != null) {
            parent.removeView(root);
        }
        fillGuanggao();
        initData();
        return root;
    }
    public void  initView(){
        appBaryx = (AppBaryx) root.findViewById(R.id.footbar);
        type_city = appBaryx.getType_city();
        header_tab = appBaryx.getHeader_tab();
        header_title = appBaryx.getHeader_titletv();
        imgae_right = appBaryx.getImgae_right();
        type_city.setVisibility(View.VISIBLE);
        header_title.setVisibility(View.VISIBLE);
        header_tab.setVisibility(View.GONE);
        imgae_right.setVisibility(View.GONE);

        guidePages = (ViewPager) root.findViewById(R.id.guidePages);
        viewGroup = (LinearLayout) root.findViewById(R.id.viewGroup);
        dmListView= (DmListView) root.findViewById(R.id.recommend_listview);

        type_city.setOnClickListener(this);
    }
    @Override
    protected void lazyLoad() {
        if (!isPrepared||!isVisible) {
            return;
        }
//        initData();
    }

    private List<View> viewList = new ArrayList<View>();

    int[] arraylist=new int[]{R.mipmap.d1,R.mipmap.d2,R.mipmap.c3,R.mipmap.c1,R.mipmap.d3,R.mipmap.d4};
    //热门的滑动
    public void fillGuanggao() {
        //viewList.clear();
//        viewGroup.removeAllViews();
        for(int i = 0; i < arraylist.length; i++){
            final ImageView iv = new ImageView(getActivity());
            iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            // 加载网络图片
            iv.setBackgroundResource(arraylist[i]);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    AnimationUtils.btnRotate(v);
                    Intent i=new Intent(getActivity(),RecommendDetailActivity.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getContext().startActivity(i,ActivityOptions.
                                makeSceneTransitionAnimation(getActivity(),Pair.create(v,"share")).toBundle());
                    } else {
                        startActivity(i);
                        getActivity().overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
                    }

//                   getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                    //  getActivity().overridePendingTransition(R.anim.wave_scale, R.anim.my_alpha_action);
                    // getActivity().overridePendingTransition(R.anim.fade, R.anim.hold);
                    //         getActivity().overridePendingTransition(R.anim.my_scale_action, R.anim.my_alpha_action);
                    //          getActivity().overridePendingTransition(R.anim.scale_rotate, R.anim.my_alpha_action);
                    //                getActivity().overridePendingTransition(R.anim.scale_translate_rotate, R.anim.my_alpha_action);
                    //               getActivity().overridePendingTransition(R.anim.scale_translate, R.anim.my_alpha_action);
                      //     getActivity().overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
                    //             getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    //            getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                    //           getActivity().overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                    //              getActivity().overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
                    //        getActivity().overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
                }
            });
            viewList.add(iv);
        }

        guidePages.setAdapter(new MyViewPagerAdapter(viewList));
        guidePages.setOnPageChangeListener(new GuidePageslister());
        imageViews = new ImageView[viewList.size()];
        for (int i = 0; i < imageViews.length; i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView = new ImageView(getActivity());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(7, 0, 7, 0);
            imageViews[i] = imageView;
            if (i == 0){
                imageViews[i].setImageDrawable(getActivity().getResources().getDrawable(
                        R.drawable.rect_cirl_small_red));
            }else{
                imageViews[i].setImageDrawable(getActivity().getResources().getDrawable(
                        R.drawable.rect_cirl_small_black));
            }
            viewGroup.addView(imageViews[i]);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.type_city:

                DialogUtils.showPopupWindow(getActivity(),v);
                break;
        }

    }

    //页面滑动的事件监听
    class GuidePageslister implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
        @Override
        public void onPageSelected(int arg0) {
            try {
                currentItem = arg0;
                for(int i=0;i<imageViews.length;i++){
                    if(arg0==i){
                        imageViews[i].setImageDrawable(getResources().getDrawable(
                                R.drawable.rect_cirl_small_red));
                    }else{
                        imageViews[i].setImageDrawable(getResources().getDrawable(
                                R.drawable.rect_cirl_small_black));
                    }
                }
            } catch (Exception e) {
            }
        }
    }
    public class MyViewPagerAdapter extends PagerAdapter {
        private List<View> mListViews;

        public MyViewPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListViews.get(position));
        }
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            container.addView(mListViews.get(position), 0);

            return mListViews.get(position);
        }
        @Override
        public int getCount() {
            return mListViews.size();
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    @Override
    public void onStart() {
        // 用一个定时器 来完成图片切换
        // Timer 与 ScheduledExecutorService 实现定时器的效果

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 通过定时器 来完成 每2秒钟切换一个图片
        // 经过指定的时间后，执行所指定的任务
        // scheduleAtFixedRate(command, initialDelay, period, unit)
        // command 所要执行的任务
        // initialDelay 第一次启动时 延迟启动时间
        // period 每间隔多次时间来重新启动任务
        // unit 时间单位
        scheduledExecutorService.scheduleAtFixedRate(new ViewPagerTask(), 5, 5,
                TimeUnit.SECONDS);
        super.onStart();
    }

    // 用来完成图片切换的任务
    private class ViewPagerTask implements Runnable {

        public void run() {
            // 实现我们的操作
            // 改变当前页面
            currentItem = (currentItem + 1) % imageViews.length;
            // Handler来实现图片切换
            handler.obtainMessage().sendToTarget();
        }
    }

    @Override
    public void onStop() {
        // 停止图片切换
        scheduledExecutorService.shutdown();
        super.onStop();
    }

    public void  initData(){
        List<String> list=new ArrayList<String>();
        for(int i=0;i<20;i++){
            list.add("");
        }
        RecommendAdapter recommendAdapter=new RecommendAdapter(getActivity(),list);
        dmListView.setAdapter(recommendAdapter);
        ListViewHeightBased.setListViewHeightBasedOnChildren(dmListView);
    }
}
