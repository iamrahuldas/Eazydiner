package com.easydiner.dao;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.easydiner.fragment.ImageZoomGalleryFragment;

public class ImageZoomGalleryAdapter extends FragmentStatePagerAdapter {

	private ArrayList<String> arrayList;
	private Context _context;
	private String restaurantId;

	public ImageZoomGalleryAdapter(FragmentManager fm,
			ArrayList<String> arrayList, String restaurantId, Context context) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.arrayList = arrayList;
		this.restaurantId = restaurantId;
		this._context = context;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return ImageZoomGalleryFragment.newInstance(arrayList.get(position),
				position, restaurantId);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}

}
