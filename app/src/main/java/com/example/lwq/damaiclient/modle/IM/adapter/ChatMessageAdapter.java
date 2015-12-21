package com.example.lwq.damaiclient.modle.IM.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lwq.damaiclient.R;
import com.example.lwq.damaiclient.modle.IM.Activity.ChatMessageActivity;
import com.example.lwq.damaiclient.modle.IM.IMApp.AnIMManger;
import com.example.lwq.damaiclient.modle.IM.bean.Chat;
import com.example.lwq.damaiclient.modle.IM.bean.IMMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author lwq
 * ChatAdapter
 * */
public class ChatMessageAdapter extends BaseAdapter {
	private List<String> list;
	private Context context;
	private static final int TYPE_MAX_COUNT =2;
	List<IMMessage> messageList;
	private Map<String,Integer> msgIdIndexMap;
	Chat mChat;
	public ChatMessageAdapter(Context context, List<IMMessage> messageList){
		this.context=context;
		this.mChat=mChat;
		this.messageList=messageList;
		msgIdIndexMap = new HashMap<String,Integer>();
	}
	public void applyData(List<IMMessage> msgs){
		messageList.clear();
		messageList.addAll(msgs);
		notifyDataSetChanged();

		for(int i=0;i<msgs.size();i++){//提取发送中的信息
			IMMessage msg = msgs.get(i);
			if(msg.status!=null && msg.status.equals(IMMessage.STATUS_SENDING)){
				msgIdIndexMap.put(msg.msgId, i);
			}
		}
	}
	//更新刚发送的信息
	public int addMessage(IMMessage msg){
		messageList.add(msg);
		notifyDataSetChanged();

		if(msg.status.equals(IMMessage.STATUS_SENDING)){//添加到发送中的信息
			msgIdIndexMap.put(msg.msgId, messageList.size()-1);
		}
		return messageList.size()-1;
	}
	//更新message消息的状态
	public void updateMessageStatus(String msgId,String status){
		Log.i("updateMessageStatus",status);
		if(msgIdIndexMap.containsKey(msgId)){
			IMMessage msg = messageList.get(msgIdIndexMap.get(msgId));
			msg.status = status;
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return messageList.size();
	}

	@Override
	public Object getItem(int position) {
		return messageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public int getItemViewType(int position) {
		if(TextUtils.equals(messageList.get(position).fromClient,
				AnIMManger.getInstance(context).getCurrentClientId())){
			return  0;
		}else{
			return  1;
		}
	}
	@Override
	public int getViewTypeCount() {
		return TYPE_MAX_COUNT;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int type=getItemViewType(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			switch (type) {
				case 0:
					convertView = LayoutInflater.from(context).inflate(R.layout.adapter_chatsend_item, null);
					holder.chat_image_user = (ImageView) convertView.findViewById(R.id.chat_image_user);
					holder.chat_tv_message = (TextView) convertView.findViewById(R.id.chat_tv_message);
					holder.chat_tv_time = (TextView) convertView.findViewById(R.id.chat_tv_time);
					holder.progress_lin= (ProgressBar) convertView.findViewById(R.id.progress_lin);
					convertView.setTag(holder);
					break;
				case 1:
					convertView = LayoutInflater.from(context).inflate(R.layout.adapter_chatjs_item, null);
					holder.chat_image_user = (ImageView) convertView.findViewById(R.id.chat_image_user);
					holder.chat_tv_message = (TextView) convertView.findViewById(R.id.chat_tv_message);
					holder.chat_tv_time = (TextView) convertView.findViewById(R.id.chat_tv_time);
					convertView.setTag(holder);
					break;
			}
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(type==0){
			holder.chat_image_user.setImageResource(R.mipmap.user);
			holder.chat_tv_message.setText(messageList.get(position).message);
			if(messageList.get(position).status.equals(IMMessage.STATUS_SENDING)){
				holder.progress_lin.setVisibility(View.VISIBLE);
			}else if(messageList.get(position).status.equals(IMMessage.STATUS_SENT)){
				holder.progress_lin.setVisibility(View.GONE);
			}
		}else{
			holder.chat_image_user.setImageResource(R.mipmap.l2);
			holder.chat_tv_message.setText(messageList.get(position).message);
		}

		return convertView;
	}
	public class ViewHolder{
		ImageView chat_image_user;
		TextView chat_tv_message,chat_tv_time;
		ProgressBar progress_lin ;
	}
}
