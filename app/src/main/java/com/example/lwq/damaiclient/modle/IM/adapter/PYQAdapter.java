package com.example.lwq.damaiclient.modle.IM.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lwq.damaiclient.R;

import java.util.List;


/**
 * @author lwq
 * ChatAdapter
 * */
public class PYQAdapter extends BaseAdapter {
	private List<String> list;
	private Context context;
	public PYQAdapter(Context context, List<String> list){
		this.context=context;
		this.list=list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_pyq ,null);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}
	public class ViewHolder{
		ImageView chat_image_user;
		TextView chat_tv_username,chat_tv_message,chat_tv_time;
	}
}
