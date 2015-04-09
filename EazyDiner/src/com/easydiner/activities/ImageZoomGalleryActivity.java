package com.easydiner.activities;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.eazydiner.R;
import com.easydiner.dao.ImageZoomGalleryAdapter;

public class ImageZoomGalleryActivity extends EasyDinerBaseActivity {

	private ViewPager vpGalleryContainer;
	private ArrayList<String> arrayListImage;
	private String restaurantId;
	private ImageZoomGalleryAdapter zoomGalleryAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_gallery_layout);
		arrayListImage = getIntent().getExtras()
				.getStringArrayList("manuImage");
		restaurantId = getIntent().getExtras()
				.getString("rest_id");
		initialize();
		vpGalleryContainer.setAdapter(zoomGalleryAdapter);
	}

	private void initialize() {
		vpGalleryContainer = (ViewPager) findViewById(R.id.vpGalleryContainer);
		zoomGalleryAdapter = new ImageZoomGalleryAdapter(
				getSupportFragmentManager(), arrayListImage, restaurantId,
				ImageZoomGalleryActivity.this);
	}
}
