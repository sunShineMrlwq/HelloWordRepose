package com.example.lwq.damaiclient.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lwq.damaiclient.R;


public class AppBaryx extends RelativeLayout {
    private ImageView imgae_right;
    private TextView header_back,type_city,header_conetnt,header_tab;
    private EditText header_titletv;
    private SharedPreferences sp;
    private Context context;

    public AppBaryx(Context ct) {
        super(ct);
        doSting(ct);
    }

    public AppBaryx(Context context,
                    AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        doSting(context);
    }

    public AppBaryx(Context context,
                    AttributeSet attrs) {
        super(context, attrs);
        doSting(context);
    }

    public void doSting(final Context ct) {
        context = ct;
        RelativeLayout root = null;
//		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//			root=(RelativeLayout) LayoutInflater.from(ct).inflate(
//					R.layout.inculde_header2, this,
//					true);
//			root.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 76));
//		} else
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            root = (RelativeLayout) LayoutInflater.from(ct).inflate(
                    R.layout.inculde_header1, this,
                    true);
        } else {
            root = (RelativeLayout) LayoutInflater.from(ct).inflate(
                    R.layout.inculde_header2, this,
                    true);
        }
        header_titletv = (EditText) root.findViewById(R.id.header_title);
        header_conetnt = (TextView) root.findViewById(R.id.header_conetnt);
        header_back = (TextView) root.findViewById(R.id.header_back);
        type_city = (TextView) root.findViewById(R.id.type_city);
        header_tab= (TextView) root.findViewById(R.id.header_tab);
        imgae_right= (ImageView) root.findViewById(R.id.imgae_right);
    }
    public TextView getHeader_back() {return header_back;}
    public ImageView getImgae_right() {return imgae_right;}
    public EditText getHeader_titletv() {return header_titletv;}
    public TextView getHeader_conetnt() {return header_conetnt;}
    public TextView getType_city() {return type_city;}
    public TextView getHeader_tab() {return header_tab;}
}
