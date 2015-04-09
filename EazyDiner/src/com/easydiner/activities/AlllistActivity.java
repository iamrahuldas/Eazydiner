package com.easydiner.activities;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.classes.Constant;
import com.classes.Pref;
import com.eazydiner.R;
import com.easydiner.dataobject.ListItem;
import com.easydiner.fragment.AlllistFragment;
import com.easydiner.fragment.MapFragment;

public class AlllistActivity extends EasyDinerBaseActivity {

	private static ImageView /* ivNearList, */ivSearchItemAlllist,
			ivMenuAlllist;
	private TextView textSearchAlllist;
	private static ImageView ivMapView;/* , ivDashboardAlllist, ivHomeAlllist; */
	private RelativeLayout /* Rlalllistheader, */allist_book_now,
			rlPopupCancel, rlAllistBtnEazyCon;
	private LinearLayout llSearchAlllist;
	private int len, flag = 0;
	public static ArrayList<ListItem> arrayList;
	private Constant _constant;
	private PopupWindow popupWindow;
	private int bookPopupFlag = 0;
	private SharedPreferences.Editor _pEditor;
	private SharedPreferences _pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alllist_layout);
		/* ivNearList = (ImageView) findViewById(R.id.ivNearList); */
		ivMapView = (ImageView) findViewById(R.id.ivMapView);
		ivSearchItemAlllist = (ImageView) findViewById(R.id.ivSearchItemAlllist);
		/*
		 * Rlalllistheader = (RelativeLayout)
		 * findViewById(R.id.Rlalllistheader);
		 */
		allist_book_now = (RelativeLayout) findViewById(R.id.allist_book_now);
		rlAllistBtnEazyCon = (RelativeLayout) findViewById(R.id.rlAllistBtnEazyCon);
		llSearchAlllist = (LinearLayout) findViewById(R.id.llSearchAlllist);
		ivMenuAlllist = (ImageView) findViewById(R.id.ivMenuAlllist);
		textSearchAlllist = (TextView) findViewById(R.id.textSearchAlllist);
		/*
		 * ivDashboardAlllist = (ImageView)
		 * findViewById(R.id.ivDashboardAlllist); ivHomeAlllist = (ImageView)
		 * findViewById(R.id.ivHomeAlllist);
		 */
		_pref = new Pref(AlllistActivity.this).getSharedPreferencesInstance();
		_pEditor = new Pref(AlllistActivity.this)
				.getSharedPreferencesEditorInstance();
		_constant = new Constant();
		setTestFont();

		if (getIntent().hasExtra("selectMapView")) {
			arrayList = Constant.arrayListItem;
			loadMapFragment();
		} else {
			loadListFragment();

		}
		/*
		 * if (_pref.getString("accessToken", "").equalsIgnoreCase("")) {
		 * Log.v("in not", "in not");
		 * ivDashboardAlllist.setVisibility(View.GONE);
		 * 
		 * }else { Log.v("in not", "in not");
		 * ivDashboardAlllist.setVisibility(View.VISIBLE); }
		 */

		/*
		 * ivDashboardAlllist.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub startActivity(new Intent(AlllistActivity.this,
		 * DashboardActivity.class));
		 * overridePendingTransition(R.anim.slide_in_right,
		 * R.anim.slide_out_left); } });
		 * 
		 * ivHomeAlllist.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub startActivity(new Intent(AlllistActivity.this,
		 * HomeActivity.class));
		 * overridePendingTransition(R.anim.slide_in_right,
		 * R.anim.slide_out_left); } });
		 */

		ivMapView.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("static-access")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_constant.NEAR_BY_LIST = 1;
				_constant.MAP_DETAILS = 0;
				loadMapFragment();
			}
		});

		/*
		 * ivNearList.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub loadListFragment(); } });
		 */
		allist_book_now.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				allist_book_now.setBackgroundColor(Color.parseColor("#9B9A9A"));
				BooknowPopupActivity.rlBookNowBultton = allist_book_now;
				startActivity(new Intent(AlllistActivity.this,
						BooknowPopupActivity.class));
				overridePendingTransition(R.anim.slide_up_in,
						R.anim.slide_down_out);
			}
		});
		rlAllistBtnEazyCon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String number = "7861004444";
				Intent dial = new Intent();
				dial.setAction("android.intent.action.DIAL");
				dial.setData(Uri.parse("tel:" + number));
				startActivity(dial);
			}
		});
		ivSearchItemAlllist.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (flag == 0) {
					ivSearchItemAlllist.setSelected(true);
					flag = 1;
				} else {
					ivSearchItemAlllist.setSelected(false);
					flag = 0;
				}

				if (llSearchAlllist.getVisibility() == View.GONE) {
					llSearchAlllist.setVisibility(View.VISIBLE);
					Animation animation = AnimationUtils.loadAnimation(
							AlllistActivity.this, R.anim.animation_open);
					animation.setDuration(500);
					llSearchAlllist.setAnimation(animation);
					llSearchAlllist.animate();
					animation.start();
				} else {
					llSearchAlllist.setVisibility(View.GONE);
					Animation animation = AnimationUtils.loadAnimation(
							AlllistActivity.this, R.anim.animation_close);
					animation.setDuration(500);
					llSearchAlllist.setAnimation(animation);
					llSearchAlllist.animate();
					animation.start();
				}

			}
		});

		llSearchAlllist.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("static-access")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(AlllistActivity.this,
						SearchListActivity.class));
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		ivMenuAlllist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				MenuListActivity.toAllListActivity = 1;
				startActivity(new Intent(AlllistActivity.this,
						MenuListActivity.class));
				overridePendingTransition(R.anim.slide_down_info,
						R.anim.slide_up_info);

			}
		});

		llSearchAlllist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(AlllistActivity.this,
						SearchListActivity.class));
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Log.e("bookPopupFlag", String.valueOf(bookPopupFlag));
		if (bookPopupFlag == 1) {
			popupWindow.dismiss();
			bookPopupFlag = 0;
		} else {
			startActivity(new Intent(AlllistActivity.this, HomeActivity.class));
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
		}
	}

	private void loadListFragment() {
		unSelectTab();
		// ivNearList.setSelected(true);
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction tran = fm.beginTransaction();
		AlllistFragment mvAc = AlllistFragment.newInstance();
		tran.replace(R.id.tabFrameLayout, mvAc);
		tran.commit();
	}

	public void loadMapFragment() {
		unSelectTab();
		ivMapView.setSelected(true);
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction tran = fm.beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putSerializable("list", arrayList);

		if (!arrayList.isEmpty()) {
			bundle.putInt("resultCount", arrayList.size());
		} else {
			bundle.putInt("resultCount", 0);
		}
		MapFragment mvAc = MapFragment.newInstance(bundle);
		tran.replace(R.id.tabFrameLayout, mvAc);
		tran.commit();
	}

	public static void unSelectTab() {
		// ivNearList.setSelected(false);
		ivMapView.setSelected(false);
	}

	private void setTestFont() {
		String fontPath1 = "fonts/Aller_Lt.ttf";
		Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
		textSearchAlllist.setTypeface(tf1);
	}

}
