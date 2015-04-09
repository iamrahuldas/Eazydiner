package com.easydiner.fragment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.eazydiner.R;

public class ImageZoomGalleryFragment extends Fragment {

	private String galleryImage, restaurantId;
	private int page;
	/* private SubsamplingScaleImageView subScalingImageviewItem; */
	private SubsamplingScaleImageView subScalingImageviewItem;
	private ViewGroup rootView;
	private Bitmap imgBitmap;
	private String iconsStoragePathFile, iconsStoragePathDir;
	private File sdIconStorageDir, sdIconStorageFile;

	// newInstance constructor for creating fragment with arguments
	public static ImageZoomGalleryFragment newInstance(String galleryImage,
			int page, String restaurantId) {
		ImageZoomGalleryFragment fragmentObj = new ImageZoomGalleryFragment();
		Bundle args = new Bundle();
		args.putString("galleryImage", galleryImage);
		args.putInt("pageNo", page);
		args.putString("restaurantId", restaurantId);
		fragmentObj.setArguments(args);
		return fragmentObj;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		galleryImage = getArguments().getString("galleryImage");
		page = getArguments().getInt("pageNo");
		restaurantId = getArguments().getString("restaurantId");
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = (ViewGroup) inflater.inflate(
				R.layout.image_gallery_fragment_layout, container, false);
		initialize();
		iconsStoragePathFile = Environment.getExternalStorageDirectory()
				+ File.separator + getActivity().getPackageName()
				+ File.separator + "cache_" + restaurantId + File.separator
				+ "menu_" + page;
		iconsStoragePathDir = Environment.getExternalStorageDirectory()
				+ File.separator + getActivity().getPackageName()
				+ File.separator + "cache_" + restaurantId;
		sdIconStorageDir = new File(iconsStoragePathDir);
		sdIconStorageFile = new File(iconsStoragePathFile);
		if (!sdIconStorageFile.isFile()) {
			Log.v("exist", "not");
			AstClassLoadImage astClassLoadImage = new AstClassLoadImage();
			astClassLoadImage.execute("");
		} else {
			Log.v("exist", "yes");
			String path = sdIconStorageFile.toString().replaceAll(" ", "%20");
			try {
				subScalingImageviewItem.setImageUri(path);
			} catch (Exception e) {
				Toast.makeText(getActivity(), "Image format not supporting",
						Toast.LENGTH_LONG).show();
			}
		}

		return rootView;
	}

	private void initialize() {
		subScalingImageviewItem = (SubsamplingScaleImageView) rootView
				.findViewById(R.id.subScalingImageviewItem);
	}

	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			// Log exception
			return null;
		}
	}

	private String storeImage(Bitmap imageData) {
		// get path to external storage (SD card)
		if (!sdIconStorageDir.isDirectory()) {
			sdIconStorageDir.mkdirs();
		}
		String filePath = "";

		try {
			filePath = sdIconStorageFile.toString();
			FileOutputStream fileOutputStream = new FileOutputStream(filePath);

			BufferedOutputStream bos = new BufferedOutputStream(
					fileOutputStream);

			// choose another format if PNG doesn't suit you
			imageData.compress(CompressFormat.PNG, 100, bos);

			bos.flush();
			bos.close();

		} catch (FileNotFoundException e) {
			Log.w("TAG", "Error saving image file: " + e.getMessage());

		} catch (IOException e) {
			Log.w("TAG", "Error saving image file: " + e.getMessage());

		} catch (Exception e) {
			Toast.makeText(getActivity(), "Image format not supported. Retry later.",
					Toast.LENGTH_LONG).show();

		}

		return filePath;
	}

	private class AstClassLoadImage extends AsyncTask<String, String, Long> {

		@SuppressWarnings("static-access")
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {

				imgBitmap = getBitmapFromURL(galleryImage);

			} catch (Exception e) {
				Log.v("Exception", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			// TODO Auto-generated method stub
			String imgPath = storeImage(imgBitmap).replaceAll(" ", "%20");
			Log.v("filepath", imgPath);
			try {
				subScalingImageviewItem.setImageUri(imgPath);
			} catch (Exception e) {
				Toast.makeText(getActivity(), "Image format not supporting",
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
