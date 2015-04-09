package com.classes;

import java.io.IOException;

import com.eazydiner.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAlertProgressDialog {
	private AlertDialog.Builder progressAlertDiagogBuilder;
	private AlertDialog dialog;
	private GifAnimationDrawable laoding;
	private TextView textProgressBar;
	private ImageView ivProgressBar;
	private Context _context;
	private String displayText;
	private LayoutInflater inflater;
	private View progressView;

	public CustomAlertProgressDialog(Context context, String displayText) {
		try {
			this._context = context;
			this.displayText = displayText;
			progressAlertDiagogBuilder = new AlertDialog.Builder(this._context);
			
			inflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			progressView = inflater.inflate(R.layout.progress_dialog_layout,
					null);
			textProgressBar = (TextView) progressView
					.findViewById(R.id.textProgressBar);
			ivProgressBar = (ImageView) progressView
					.findViewById(R.id.ivProgressBar);
			setTextFont();
			textProgressBar.setText(this.displayText);
			laoding = new GifAnimationDrawable(this._context.getResources()
					.openRawResource(R.raw.loading));
			laoding.setOneShot(false);
			ivProgressBar.setImageDrawable(laoding);
			ivProgressBar.getDrawable().setVisible(true, true);
			progressAlertDiagogBuilder.setView(progressView);
			dialog = progressAlertDiagogBuilder.create();
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public AlertDialog getDialog(){
		return dialog;
	}

	private void setTextFont() {
		String fontPath2 = "fonts/Aller_Rg.ttf";
		Typeface tf1 = Typeface
				.createFromAsset(_context.getAssets(), fontPath2);

		textProgressBar.setTypeface(tf1);
	}
}
