package com.example.lwq.damaiclient.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.lwq.damaiclient.R;
import com.example.lwq.damaiclient.widget.CustomDialog;


public class DialogUtils {
	private static PopupWindow popupWindow = null;
	public static void showPopupWindow(final Context context,View v) {
		// 一个自定义的布局，作为显示的内容
		View contentView = LayoutInflater.from(context).inflate(
				R.layout.alert, null);
		Button bt_cancel= (Button) contentView.findViewById(R.id.alert_cancel_btn);
		Button bt_ok= (Button) contentView.findViewById(R.id.alert_confirm_btn);
		RelativeLayout bj_popu = (RelativeLayout) contentView.findViewById(R.id.bj_popu);
		// 设置按钮的点击事件
		  popupWindow = new PopupWindow(context);
		popupWindow.setContentView(contentView);
		popupWindow.setOutsideTouchable(false);
		popupWindow.setFocusable(true);
		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.color.transparent));
 	    popupWindow.setWidth(DisplayUtil.getScreenWidth(context));
		popupWindow.setHeight(DisplayUtil.getScreenHeight(context));

		popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {
			}
		});
		bt_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		bt_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		bj_popu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
	}
}
