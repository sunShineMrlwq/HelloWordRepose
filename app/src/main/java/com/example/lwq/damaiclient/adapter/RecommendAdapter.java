package com.example.lwq.damaiclient.adapter;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.example.lwq.damaiclient.R;


/**
 * @author lwq
 * RecommendAdapter
 * */
public class RecommendAdapter extends BaseAdapter {
	private List<String> list;
	private Context context;
    private int flag;
	public RecommendAdapter(Context context, List<String> list){
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
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_recommend, null);
			holder.imageView_title= (ImageView) convertView.findViewById(R.id.item_image);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(position%2==0){
			holder.imageView_title.setImageResource(R.mipmap.l1);
		}else{
			holder.imageView_title.setImageResource(R.mipmap.l2);
		}
		return convertView;
	}
	public class ViewHolder{
		ImageView imageView_title;
	}

}
