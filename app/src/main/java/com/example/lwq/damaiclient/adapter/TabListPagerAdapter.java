package com.example.lwq.damaiclient.adapter;
import java.util.List;
import com.example.lwq.damaiclient.fragment.GroupListFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
/**
 * PagerAdpater
 */
public class TabListPagerAdapter extends  FragmentPagerAdapter {
	private String[] titels;
	private Fragment[] fragments ;
	public static int currentIndex=0;
	public TabListPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	public TabListPagerAdapter(FragmentManager fm,
							   String[] titels) {
		super(fm);
		this.titels = titels;
		fragments = new Fragment[titels.length];
	}

	@Override
	public Fragment getItem(int position) {
		if (position==0||null==fragments[position]) {
			Bundle b= new Bundle();
			GroupListFragment nFragment=new GroupListFragment();
			nFragment.setArguments(b);
			fragments[position] = nFragment;
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
