package com.example.lwq.damaiclient.modle.IM.Utils.PinYinSearh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lwq.damaiclient.R;
import com.example.lwq.damaiclient.modle.IM.Activity.ChatMessageActivity;
import com.example.lwq.damaiclient.modle.IM.Activity.FriendRequestActivity;
import com.example.lwq.damaiclient.modle.IM.Activity.UserInfoActivity;
import com.example.lwq.damaiclient.modle.IM.IMUtils.Constant;
import com.example.lwq.damaiclient.modle.IM.bean.User;

public class PinyinAdapter extends BaseExpandableListAdapter {

	// 字符串
	private List<String> strList;
	private List<User> ulist;
	private AssortPinyinList assort = new AssortPinyinList();

	private Context context;

	private LayoutInflater inflater;
	// 中文排序
	private LanguageComparator_CN cnSort = new LanguageComparator_CN();

	public PinyinAdapter(Context context, List<String> strList, List<User> list) {
		super();
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.strList = strList;
		this.ulist=list;
		if (strList == null) {
			strList = new ArrayList<String>();
		}

		long time = System.currentTimeMillis();
		// 排序
		sort();
	}

	private void sort() {
		// 分类
		for (String str : strList) {
			assort.getHashList().add(str);
		}
		assort.getHashList().sortKeyComparator(cnSort);
		for(int i=0,length=assort.getHashList().size();i<length;i++)
		{
			Collections.sort((assort.getHashList().getValueListIndex(i)),cnSort);
		}
		
	}

	public Object getChild(int group, int child) {
		return assort.getHashList().getValueIndex(group, child);
	}

	public long getChildId(int group, int child) {
		return child;
	}

	public View getChildView(final int group,final int child, boolean arg2,
			View contentView, ViewGroup arg4) {
		if (contentView == null) {
			contentView = inflater.inflate(R.layout.adapter_chat_search, null);
		}
		TextView textView = (TextView) contentView.findViewById(R.id.name);
		 ImageView userimage= (ImageView) contentView.findViewById(R.id.txl_image_user);
		String username="";
		if(group==0&&child==0){
			textView.setText("新朋友");
			userimage.setImageResource(R.mipmap.new_f);
		}else if(group==0&&child==1){
			userimage.setImageResource(R.mipmap.qunliao);
			textView.setText("群聊");
		}else{
			userimage.setImageResource(R.mipmap.user);
			username=assort.getHashList().getValueIndex(group, child);
			textView.setText(username);
		}
		final String finalUsername = username;
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (group == 0) {
					switch (child){
						case 0:
							context.startActivity(new Intent(context, FriendRequestActivity.class));
							break;
						case 1:
							break;
					}
					return;
				}
				User user=null;
				for(int i=0;i<ulist.size();i++){
					if(TextUtils.equals(finalUsername,ulist.get(i).userName)){
						user=ulist.get(i);
					}
				}
				Intent i = new Intent(context, UserInfoActivity.class);
				Bundle b = new Bundle();
				b.putSerializable("user", user);
				i.putExtras(b);
				i.putExtra("USERNAME", finalUsername);
				i.putExtra("isFriend",true);
				context.startActivity(i);
			}
		});
		return contentView;
	}

	public int getChildrenCount(int group) {
		return assort.getHashList().getValueListIndex(group).size();
	}

	public Object getGroup(int group) {
		return assort.getHashList().getValueListIndex(group);
	}

	public int getGroupCount() {
		return assort.getHashList().size();
	}

	public long getGroupId(int group) {
		return group;
	}

	public View getGroupView(int group, boolean arg1, View contentView,
			ViewGroup arg3) {
		if (contentView == null) {
			contentView = inflater.inflate(R.layout.list_group_item, null);
			contentView.setClickable(true);
		}
		TextView textView = (TextView) contentView.findViewById(R.id.name);
		if(!assort.getFirstChar(assort.getHashList()
				.getValueIndex(group, 0)).equals("#")){
			textView.setText(assort.getFirstChar(assort.getHashList()
					.getValueIndex(group, 0)));
			textView.setVisibility(View.VISIBLE);
		}else{
			textView.setVisibility(View.GONE);
		}
		// 禁止伸展

		return contentView;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

	public AssortPinyinList getAssort() {
		return assort;
	}

}
