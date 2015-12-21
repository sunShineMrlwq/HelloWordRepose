package com.example.lwq.damaiclient.utils;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by lwq on 2015/12/7.
 */
public class ListViewHeightBased {


    public  static void setListViewHeightBasedOnChildren(ListView plistView){
        try {
            // 获取ListView对应的Adapter
            ListAdapter listAdapter = plistView.getAdapter();
            if (listAdapter == null) {
                return;
            }
            int totalHeight = 0;
            int len=0;
            len=listAdapter.getCount();
            for (int i = 0; i < len; i++) {
                // listAdapter.getCount()返回数据项的数目
                View listItem = listAdapter.getView(i, null, plistView);
                // 计算子项View 的宽高
                listItem.measure(0, 0);
                // 统计所有子项的总高度
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = plistView.getLayoutParams();
            int d=plistView.getDividerHeight();
            params.height = (int) (totalHeight+len*d);
            // listView.getDividerHeight()获取子项间分隔符占用的高度
            // params.height最后得到整个ListView完整显示需要的高度
            plistView.setLayoutParams(params);
        } catch (Exception e) {
            Log.d("ListViewHeightBased", "出错了");
        }
    }
}
