package com.example.lwq.damaiclient.modle.IM.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.lwq.damaiclient.R;
import com.example.lwq.damaiclient.app.BaseActivity;
import com.example.lwq.damaiclient.modle.IM.adapter.IMPagerAdapter;

/**
 * Created by lwq on 2015/12/10.
 */
public class IMMainActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private IMPagerAdapter mTabsAdapter;
    private ImageView[] imageViews = new ImageView[4];
    private TextView[] textViews = new TextView[4];
    private Fragment[] fragments;
    private int currentTabIndex = 0, tab_index = 0;//当前索引和选择的索引
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_main);
        initView();
        initTab();
    }
    //初始化布局
    public void initView() {//初始化布局
        imageViews[0] = (ImageView) findViewById(R.id.im_mian_image_chat);
        imageViews[1] = (ImageView) findViewById(R.id.im_mian_image_txl);
        imageViews[2] = (ImageView) findViewById(R.id.im_mian_image_fx);
        imageViews[3] = (ImageView) findViewById(R.id.im_mian_image_mine);

        textViews[0] = (TextView) findViewById(R.id.im_mian_tv_chat);
        textViews[1] = (TextView) findViewById(R.id.im_mian_tv_txl);
        textViews[2] = (TextView) findViewById(R.id.im_mian_tv_fx);
        textViews[3] = (TextView) findViewById(R.id.im_mian_tv_mine);

        imageViews[0].setSelected(true);
        textViews[0].setTextColor(getResources().getColor(R.color.red));

        findViewById(R.id.im_mian_lin_chat).setOnClickListener(this);
        findViewById(R.id.im_mian_lin_txl).setOnClickListener(this);
        findViewById(R.id.im_mian_lin_fx).setOnClickListener(this);
        findViewById(R.id.im_mian_lin_mine).setOnClickListener(this);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setOnPageChangeListener(new GuidePageslister());
        //关闭预加载，默认一次只加载一个Fragment
        mViewPager.setOffscreenPageLimit(1);
    }
    //初始化tab
    public void initTab() {
        String[] tabText = new String[]{"聊天", "通讯录", "发现", "我"};
        //创建FragmentPagerAdapter适配器，并将指定要显示的fragemnt的控件
        mTabsAdapter = new IMPagerAdapter(getSupportFragmentManager(), tabText);
        mViewPager.setAdapter(mTabsAdapter);//给ViewPager设置适配器
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_mian_lin_chat:
                tab_index = 0;
                break;
            case R.id.im_mian_lin_txl:
                tab_index = 1;
                break;
            case R.id.im_mian_lin_fx:
                tab_index = 2;
                break;
            case R.id.im_mian_lin_mine:
                tab_index = 3;
                break;
        }
        showModule(tab_index);
    }
    //添加显示各模块
    public void showModule(int tab_index ) {
        mViewPager.setCurrentItem(tab_index);

        imageViews[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        imageViews[tab_index].setSelected(true);
        textViews[currentTabIndex].setTextColor(getResources().getColor(R.color.black));
        textViews[tab_index].setTextColor(getResources().getColor(R.color.red));
        currentTabIndex = tab_index;
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
                showModule(arg0);
            } catch (Exception e) {
            }
        }
    }
}
