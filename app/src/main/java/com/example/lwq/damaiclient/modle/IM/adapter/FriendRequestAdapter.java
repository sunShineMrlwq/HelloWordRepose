package com.example.lwq.damaiclient.modle.IM.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arrownock.social.IAnSocialCallback;
import com.example.lwq.damaiclient.R;
import com.example.lwq.damaiclient.app.MyApplication;
import com.example.lwq.damaiclient.modle.IM.Activity.FriendRequestActivity;
import com.example.lwq.damaiclient.modle.IM.Activity.UserInfoActivity;
import com.example.lwq.damaiclient.modle.IM.IMApp.UserManager;
import com.example.lwq.damaiclient.modle.IM.IMUtils.Constant;
import com.example.lwq.damaiclient.modle.IM.bean.FriendRequest;
import com.example.lwq.damaiclient.modle.IM.bean.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendRequestAdapter extends BaseAdapter {
	private List<FriendRequest> friendRequests;
	private Map<String,Boolean> friendStatus;
	private Context ct;

	public FriendRequestAdapter(Context ct){
		this.ct = ct;
		friendRequests = new ArrayList<FriendRequest>();
		friendStatus = new HashMap<String,Boolean>();
	}

	public void applyData(List<FriendRequest> users){
		friendRequests.clear();
		friendRequests.addAll(users);

//		refreshFriendStatus();

		notifyDataSetChanged();

	}



	@Override
	public int getCount() {
		return friendRequests.size();
	}

	@Override
	public FriendRequest getItem(int position) {
		return friendRequests.get(position);
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
			convertView = LayoutInflater.from(ct).inflate(R.layout.adapter_friend_request, null);
			holder.chat_tv_username= (TextView) convertView.findViewById(R.id.chat_tv_username);
			holder.tv_addfriend= (Button) convertView.findViewById(R.id.tv_addfriend);
			holder.tv_onaddfriend= (Button) convertView.findViewById(R.id.tv_onaddfriend);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.chat_tv_username.setText(friendRequests.get(position).userName);
		if(TextUtils.equals(FriendRequest.STATUS_PENDING,friendRequests.get(position).status)){//等待接受中
			holder.tv_addfriend.setVisibility(View.VISIBLE);
			holder.tv_onaddfriend.setVisibility(View.VISIBLE);
			holder.tv_addfriend.setText("接受");
			holder.tv_addfriend.setTextColor(ct.getResources().getColor(R.color.lanse));
			holder.tv_addfriend.setClickable(true);
			holder.tv_addfriend.setClickable(true);
			holder.tv_addfriend.setEnabled(true);
		}
		if(TextUtils.equals(FriendRequest.STATUS_APPROVED,friendRequests.get(position).status)){//已接受
			holder.tv_addfriend.setVisibility(View.VISIBLE);
			holder.tv_onaddfriend.setVisibility(View.GONE);
			holder.tv_addfriend.setText("已添加");
			holder.tv_addfriend.setTextColor(ct.getResources().getColor(R.color.transparent_background));
			holder.tv_addfriend.setClickable(false);
			holder.tv_addfriend.setEnabled(false);
		}
		if(TextUtils.equals(FriendRequest.STATUS_REJECTED,friendRequests.get(position).status)){//已拒绝
			holder.tv_addfriend.setVisibility(View.VISIBLE);
			holder.tv_onaddfriend.setVisibility(View.GONE);
			holder.tv_addfriend.setText("已拒绝");
			holder.tv_addfriend.setTextColor(ct.getResources().getColor(R.color.transparent_background));
			holder.tv_addfriend.setClickable(false);
			holder.tv_addfriend.setEnabled(false);

		}
		final ViewHolder finalHolder = holder;
		holder.tv_addfriend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UserManager.getInstance(ct).approveFriendRequest(friendRequests.get(position), new IAnSocialCallback(){

					@Override
					public void onSuccess(JSONObject jsonObject) {
						finalHolder.tv_addfriend.setVisibility(View.VISIBLE);
						finalHolder.tv_onaddfriend.setVisibility(View.GONE);
						finalHolder.tv_addfriend.setText("已添加");
						finalHolder.tv_addfriend.setTextColor(ct.getResources().getColor(R.color.transparent_background));
						finalHolder.tv_addfriend.setClickable(false);
						finalHolder.tv_addfriend.setClickable(false);
						Intent i = new Intent(ct, UserInfoActivity.class);
						i.putExtra("USERNAME", friendRequests.get(position).userName);
						i.putExtra("isFriend",true);
						ct.startActivity(i);
					}
					@Override
					public void onFailure(JSONObject jsonObject) {
					}
				});
			}
		});

		holder.tv_onaddfriend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UserManager.getInstance(ct).rejectFriendRequest(friendRequests.get(position), new IAnSocialCallback(){

					@Override
					public void onSuccess(JSONObject jsonObject) {
						finalHolder.tv_addfriend.setVisibility(View.VISIBLE);
						finalHolder.tv_onaddfriend.setVisibility(View.GONE);
						finalHolder.tv_addfriend.setText("已拒绝");
						finalHolder.tv_addfriend.setTextColor(ct.getResources().getColor(R.color.transparent_background));
						finalHolder.tv_addfriend.setClickable(false);
						finalHolder.tv_addfriend.setClickable(false);
					}
					@Override
					public void onFailure(JSONObject jsonObject) {
					}
				});
			}
		});
		return convertView;
	}


	public class ViewHolder{
		ImageView chat_image_user;
		TextView chat_tv_username ;
		Button tv_addfriend,tv_onaddfriend;
	}

	public interface newFriendsCallback{//搜索好友的回调接口
		public void onFinish(List<User> data);
	}
}
