package com.example.lwq.damaiclient.modle.IM.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.lwq.damaiclient.R;
import com.example.lwq.damaiclient.modle.IM.Activity.ChatMessageActivity;
import com.example.lwq.damaiclient.modle.IM.Activity.UserInfoActivity;
import com.example.lwq.damaiclient.modle.IM.IMApp.AnIMManger;
import com.example.lwq.damaiclient.modle.IM.IMApp.UserManager;
import com.example.lwq.damaiclient.modle.IM.IMUtils.Constant;
import com.example.lwq.damaiclient.modle.IM.bean.Chat;
import com.example.lwq.damaiclient.modle.IM.bean.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author lwq
 * ChatAdapter
 * */
public class ChatAdapter extends BaseAdapter {
	private List<String> list;
	private Context context;
	private List<Chat> chatList;
	private List<Chat> filteredChatList;
	private Map<String,User> userMap;
	private Handler handler;
	public ChatAdapter(Context context, List<String> list){
		this.context=context;
		this.list=list;
		chatList = new ArrayList<Chat>();
		filteredChatList = new ArrayList<Chat>();
		userMap = new HashMap<String,User>();
		handler = new Handler();
	}
	public void fillLocalData(){//获取聊天记录
		AnIMManger.getInstance(context).getAllMyChat(new AnIMManger.GetChatCallback(){
			@Override
			public void onFinish(List<Chat> data) {
				applyData(data);
			}
		});
	}
	public void applyData(List<Chat> chats){
		chatList.clear();
		chatList.addAll(chats);
		filter(null);

		refreshUseMap();
		notifyDataSetChanged();

	}
	public void filter(String charText) {
		filteredChatList.clear();
		if (charText==null || charText.length() == 0) {
			filteredChatList.addAll(chatList);
		} else {
			for (Chat chat : chatList) {
				if(chat.topic!=null && chat.topic.topicName.contains(charText)){
					filteredChatList.add(chat);
				}else if(chat.targetClientId!=null){
					if(userMap.containsKey(chat.targetClientId) && userMap.get(chat.targetClientId).userName.contains(charText)){
						filteredChatList.add(chat);
					}
				}
			}
		}
		notifyDataSetChanged();
	}
	private void refreshUseMap(){
		final List<Chat> chats = new ArrayList<Chat>();
		chats.addAll(chatList);
		new Thread(new Runnable(){
			@Override
			public void run() {
				for(Chat chat : chats){
					if(chat.topic==null && chat.targetClientId!=null && !userMap.containsKey(chat.targetClientId)){
						User user = UserManager.getInstance(context).getUserByClientId(chat.targetClientId);
						userMap.put(chat.targetClientId, user);
					}
				}
				handler.post(new Runnable(){
					@Override
					public void run() {
						notifyDataSetChanged();
					}
				});

			}
		}).start();
	}

	@Override
	public int getCount() {
		return filteredChatList.size();
	}

	@Override
	public Object getItem(int position) {
		return filteredChatList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_chat, null);
			holder.chat_image_user= (ImageView) convertView.findViewById(R.id.chat_image_user);
			holder.chat_tv_username= (TextView) convertView.findViewById(R.id.chat_tv_username);
			holder.chat_tv_message= (TextView) convertView.findViewById(R.id.chat_tv_message);
			holder.chat_tv_time= (TextView) convertView.findViewById(R.id.chat_tv_time);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(position%2==0){
			holder.chat_image_user.setImageResource(R.mipmap.user);
		}else{
			holder.chat_image_user.setImageResource(R.mipmap.l2);
		}
		final Chat chat=filteredChatList.get(position);
		if(chat.targetClientId!=null) {
			if (userMap.containsKey(chat.targetClientId)) {
				holder.chat_tv_username.setText(userMap.get(chat.targetClientId).userName);
			}
		}
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(context,ChatMessageActivity.class);
				Bundle b = new Bundle();
				b.putSerializable(Constant.INTENT_EXTRA_KEY_CHAT, chat);
				i.putExtras(b);
				context.startActivity(i);
			}
		});
		return convertView;
	}
	public class ViewHolder{
		ImageView chat_image_user;
		TextView chat_tv_username,chat_tv_message,chat_tv_time;
	}
}
