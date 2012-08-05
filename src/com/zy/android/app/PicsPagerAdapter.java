package com.zy.android.app;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.viewpagerindicator.TitleProvider;
import com.zy.android.dos.Item;

public class PicsPagerAdapter extends FragmentStatePagerAdapter implements
		TitleProvider {
	
	private List<Item> mData;
//    private LayoutInflater mInflater;

	public PicsPagerAdapter(FragmentManager fm, Context ctx) {
		super(fm);
//		mInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mData = new ArrayList<Item>();
	}
	
	public void addItem(final Item item) {
        mData.add(item);
    }

	@Override
	public String getTitle(int position) {
		return mData.get(position).getTitle();
	}
	
	public Item getItemByPos(int pos) {
		return mData.get(pos);
	}

	@Override
	public Fragment getItem(int position) {
		return PicDetailFragment.newInstance(mData.get(position));
	}

	@Override
	public int getCount() {
		return mData.size();
	}
	
//	@Override
//    public Object instantiateItem(View collection, int position) {
//        View v = layoutInflater.inflate(...);
//        ((ViewPager) collection).addView(v,0);
//        return tv;
//    }
//
//    @Override
//    public void destroyItem(View collection, int position, Object view) {
//        ((ViewPager) collection).removeView((TextView) view);
//    }

}
