package com.example.lwq.damaiclient.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lwq.damaiclient.R;
import com.example.lwq.damaiclient.adapter.TabListPagerAdapter;
import com.example.lwq.damaiclient.app.BaseFragment;
import com.example.lwq.damaiclient.widget.AppBaryx;
import com.example.lwq.damaiclient.widget.TabPageIndicator;

/**
 * Created by lwq on 2015/12/4.
 */
public class FileFragment extends BaseFragment {
    private View root;
    private ViewPager mViewPager;
    private TabPageIndicator mTabPageIndicator;
    private TabListPagerAdapter mTabsAdapter;
    private AppBaryx appBaryx;
    private TextView header_back,type_city, header_tab, header_title;
    private ImageView  imgae_right;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(null==root){
            root=inflater.inflate(R.layout.fragment_file,null);
            initView();
        }
        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) root.getParent();
        if (parent != null) {
            parent.removeView(root);
        }
        initTab();
        return root;
    }

    @Override
    protected void lazyLoad() {

    }
    public void initView(){//初始化布局
        appBaryx = (AppBaryx) root.findViewById(R.id.footbar);
        type_city = appBaryx.getType_city();
        header_tab = appBaryx.getHeader_tab();
        header_title = appBaryx.getHeader_titletv();
        imgae_right = appBaryx.getImgae_right();
        header_tab.setText("电影");
        header_tab.setVisibility(View.VISIBLE);
        type_city.setVisibility(View.VISIBLE);
        header_title.setVisibility(View.GONE);
        imgae_right.setVisibility(View.GONE);

        mTabPageIndicator = (TabPageIndicator) root.findViewById(R.id.category_strip);
        mViewPager = (ViewPager)root.findViewById(R.id.viewpager);
        //关闭预加载，默认一次只加载一个Fragment
        mViewPager.setOffscreenPageLimit(1);
    }
    //初始化tab
    public void initTab(){
        String[] tabText=new String[]{"正在上映","即将上映","最热","花絮","超级预告"};
        //创建FragmentPagerAdapter适配器，并将指定要显示的fragemnt的控件
        mTabsAdapter=new TabListPagerAdapter(getChildFragmentManager(), tabText);
        mViewPager.setAdapter(mTabsAdapter);//给ViewPager设置适配器
        mViewPager.setCurrentItem(0);
        mTabPageIndicator.setViewPager(mViewPager);//获取指示器控件
    }
}
