package com.easydiner.dao;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageGalleryAdapter extends PagerAdapter {
	
	private Context context;
	private ArrayList<String> imgUrl;
	
	public ImageGalleryAdapter(Context context, ArrayList<String> imgUrl) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.imgUrl = imgUrl;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imgUrl.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view == ((ImageView) object);
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
						.cacheOnDisk(true)
						.resetViewBeforeLoading(true)
						.cacheInMemory(true)
						.build();
		ImageView imageView = new ImageView(context);
		
		imageLoader.displayImage(imgUrl.get(position), imageView, options);
		
		((ViewPager) container).addView(imageView, 0);
		
		return imageView;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		//super.destroyItem(container, position, object);
		((ViewPager) container).removeView((ImageView) object);
	}

}
