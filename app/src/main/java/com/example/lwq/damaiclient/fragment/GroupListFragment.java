package com.example.lwq.damaiclient.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.lwq.damaiclient.R;
import com.example.lwq.damaiclient.adapter.RecommendAdapter;
import com.example.lwq.damaiclient.app.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lwq on 2015/12/4.
 */
public class GroupListFragment extends BaseFragment {
    private View root;
    private ListView dmListView;
    ProgressBar imag_ProgressBar;
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    LinearLayout progress_lin;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(null==root){
            root=inflater.inflate(R.layout.fragment_grouplist,null);
            initView();
            isPrepared=true;
            lazyLoad();
        }
        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) root.getParent();
        if (parent != null) {
            parent.removeView(root);
        }
        return root;
    }
    public void  initView(){
        imag_ProgressBar = (ProgressBar) root.findViewById(R.id.imag_ProgressBar);
        dmListView= (ListView) root.findViewById(R.id.group_listview);
        progress_lin = (LinearLayout) root.findViewById(R.id.progress_lin);
    }
    @Override
    protected void lazyLoad() {
        if (!isPrepared||!isVisible) {
            return;
        }
        progress_lin.setVisibility(View.GONE);
        imag_ProgressBar.setVisibility(View.GONE);
        initData();
    }
    public void  initData(){
        List<String> list=new ArrayList<String>();
        for(int i=0;i<20;i++){
            list.add("");
        }
        RecommendAdapter recommendAdapter=new RecommendAdapter(getActivity(),list);
        dmListView.setAdapter(recommendAdapter);
    }
}
