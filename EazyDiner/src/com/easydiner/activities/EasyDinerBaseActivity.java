package com.easydiner.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.classes.CommonFunction;
import com.eazydiner.R;
import com.google.android.gms.common.ConnectionResult;
import com.nostra13.universalimageloader.core.ImageLoader;

public class EasyDinerBaseActivity extends FragmentActivity {
	String gcmRegId;
	CommonFunction _commonFunction;
	Context _thisContext;
	Activity _thisActivity;
	SharedPreferences spref;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		CountDownTimer timer = new CountDownTimer(Long.MAX_VALUE, 240000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				
				//showRemainderPopup();
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				start();
			}
		}.start();

	}

	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub

	}
	
	private void showRemainderPopup() {
		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.remainder_popup_layout);

		LinearLayout llCancelPopup = (LinearLayout) dialog
				.findViewById(R.id.llCancelPopup);
		
		llCancelPopup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
		dialog.show();
	}
}