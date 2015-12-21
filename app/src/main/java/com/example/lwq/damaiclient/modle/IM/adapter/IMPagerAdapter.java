package com.example.lwq.damaiclient.modle.IM.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.lwq.damaiclient.modle.IM.Fragment.ChatFragment;
import com.example.lwq.damaiclient.modle.IM.Fragment.FxFragment;
import com.example.lwq.damaiclient.modle.IM.Fragment.MyFragment;
import com.example.lwq.damaiclient.modle.IM.Fragment.TxlFragment;

/**
 * PagerAdpater
 */
public class IMPagerAdapter extends  FragmentPagerAdapter {
	private String[] titels;
	private Fragment[] fragments ;
	public static int currentIndex=0;
	public IMPagerAdapter(FragmentManager fm,
						  String[] titels) {
		super(fm);
		this.titels = titels;
		fragments = new Fragment[titels.length];
	}

	@Override
	public Fragment getItem(int position) {
		if (position==0||null==fragments[position]) {
			Bundle b= new Bundle();
			switch (position){
				case 0:
					ChatFragment cFragment=new ChatFragment();
					cFragment.setArguments(b);
					fragments[position] = cFragment;
					break;
				case 1:
					TxlFragment tFragment=new TxlFragment();
					tFragment.setArguments(b);
					fragments[position] = tFragment;
					break;
				case 2:
					FxFragment fxFragment=new FxFragment();
					fxFragment.setArguments(b);
					fragments[position] = fxFragment;
					break;
				case 3:
					MyFragment mFragment=new MyFragment();
					mFragment.setArguments(b);
					fragments[position] = mFragment;
					break;
				default:
					break;
			}
		}
		return fragments[position];
	}

	@Override
	public int getCount() {
		return titels.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		currentIndex=position;
		return titels[position];
	}
}
