package com.easydiner.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.eazydiner.R;
import com.easydiner.dao.ImageGalleryAdapter;

public class GalleryActivity extends Activity {
	
	ViewPager imagePager;
	private ImageGalleryAdapter galleryAdapter;
	private ArrayList<String> galleryImg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.galleryview_layout);
		galleryImg = getIntent().getExtras().getStringArrayList("gallery_image");
		imagePager = (ViewPager) findViewById(R.id.pagerGalleryContainer);
		galleryAdapter = new ImageGalleryAdapter(GalleryActivity.this, galleryImg);
		imagePager.setAdapter(galleryAdapter);
	}
}
