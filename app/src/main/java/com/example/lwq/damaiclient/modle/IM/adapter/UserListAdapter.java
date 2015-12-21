package com.example.lwq.damaiclient.modle.IM.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.arrownock.exception.ArrownockException;
import com.arrownock.social.IAnSocialCallback;
import com.example.lwq.damaiclient.R;
import com.example.lwq.damaiclient.modle.IM.Activity.UserInfoActivity;
import com.example.lwq.damaiclient.modle.IM.bean.User;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class UserListAdapter extends BaseAdapter {
	private List<User> userList;
	private Map<String,Boolean> friendStatus;
	private Context ct;

	public UserListAdapter(Context ct){
		this.ct = ct;
		userList = new ArrayList<User>();
		friendStatus = new HashMap<String,Boolean>();
	}

	public void applyData(List<User> users){
		userList.clear();
		userList.addAll(users);

//		refreshFriendStatus();

		notifyDataSetChanged();

	}



	@Override
	public int getCount() {
		return userList.size();
	}

	@Override
	public User getItem(int position) {
		return userList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(ct).inflate(R.layout.adapter_friend_search, null);
			holder.chat_tv_username= (TextView) convertView.findViewById(R.id.chat_tv_username);
			holder.re_frineds= (RelativeLayout) convertView.findViewById(R.id.re_frineds);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.chat_tv_username.setText(userList.get(position).userName);
		holder.re_frineds.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ct, UserInfoActivity.class);
				Bundle b=new Bundle();
				b.putSerializable("user", userList.get(position));
				i.putExtra("USERNAME", userList.get(position).userName);
				i.putExtras(b);
				ct.startActivity(i);
			}
		});
		return convertView;
	}


	public class ViewHolder{
		ImageView chat_image_user;
		TextView chat_tv_username ;
		RelativeLayout re_frineds;
	}
}
