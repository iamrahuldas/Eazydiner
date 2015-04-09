package com.easydiner.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.classes.ConnectionDetector;
import com.classes.Constant;
import com.classes.CustomAlertProgressDialog;
import com.classes.CustomTimePickerDialog;
import com.classes.JsonobjectPost;
import com.classes.Pref;
import com.eazydiner.R;
import com.easydiner.dao.RestaurantAdapter;
import com.easydiner.dataobject.RestorentItem;
import com.google.android.gms.maps.internal.IMapViewDelegate;

public class BooknowPopupActivity extends EasyDinerBaseActivity {

	private LinearLayout llPopupBookNow, llTermsAndCodition;
	private Spinner spnBookingPersonNoPopup;
	private EditText etBookingDate, etPersonName, etPhone, etEmail,
			etBookingTimePopup;
	private ImageView ivSearchItemAlllist, ivMenuAlllist;
	private Spinner spnBookingPersonNo;
	private LinearLayout llRestName, llAllResturant;
	private EditText etResturentName;
	private int year;
	private int month;
	private int day;
	private int hour, currHour;
	private int minute, currMinute;
	private String selectTime = "", bookingDate = "", bookingTime = "",
			aTime = "";
	static final int DATE_DIALOG_ID = 999;
	static final int TIME_DIALOG_ID = 1111;
	private RestaurantAdapter adpterObj;
	private JSONObject jObjList, jsonObject1, jsonObject2;
	private List<NameValuePair> nameValuePairs, nameValuePairsBooking;
	private ArrayList<RestorentItem> arrayList;
	private static final String TAG_DATA = "data";
	private static final String TAG_ITEMDATA = "itemdata";
	private static final String TAG_ERNODE = "erNode";
	private static final String TAG_ERCODE = "erCode";
	private static final String TAG_ERMSG = "erMsg";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_GETITEM = "getItem";
	private static final String TAG_ACCESSTOKN = "accessTokn";
	private static final String TAG_RESNAME = "resName";
	private static final String TAG_EMAILID = "emailid";
	private static final String TAG_MOBILE = "mobile";
	private static final String TAG_DATE = "date";
	private static final String TAG_TIME = "time";
	private static final String TAG_NOOFPERSON = "noOfPerson";
	private static final String TAG_BOOKDATA = "bookData";
	private static final String TAG_RESTURENTID = "resturentId";
	private static final String TAG_BOOKINGID = "bookingId";
	private static final String TAG_LOCATION = "location";
	private PopupWindow popupWindowRestorent;
	private ImageView arrow_down, ivCondition;
	private int restaurenId = 0, personNo = 1, flagCheckbox = 1;
	private int rescFlag = 0, rescShow = 0;
	public static RelativeLayout rlBookNowBultton;
	private SimpleDateFormat format, formatDate, formatTime, formatTodayDate;
	private TextView tvBookingText1, tvBookingText3, tvBookingText5,
			tvBookingText6, tvBookingText7, textAlertMessageOne,
			textAlertMessageTwo;
	private ConnectionDetector _connectionDetector;
	private CustomTimePickerDialog timePickerDialog;
	private int timeFlag = 0;
	private SharedPreferences _pref;

	Date seleteTime, seleteRngMin, seleteRngMax, currentTime;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.booknow_layout_new);
		initialize();

		bookingDate = new StringBuilder()
				.append(String.valueOf(Calendar.getInstance().get(
						Calendar.DAY_OF_MONTH)))
				.append('-')
				.append(Calendar.getInstance().get(Calendar.MONTH) + 1)
				.append('-')
				.append(String.valueOf(Calendar.getInstance()
						.get(Calendar.YEAR))).toString();

		if (!_pref.getString("accessToken", "").equalsIgnoreCase(null)
				&& !_pref.getString("accessToken", "").equalsIgnoreCase("")) {

			etPersonName.setText(_pref.getString("userName", ""));
			etPhone.setText(_pref.getString("userPhoneNo", ""));
			etEmail.setText(_pref.getString("userId", ""));

		}
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.booking_parson_spnarr,
				R.layout.spinner_item_layout);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnBookingPersonNo.setAdapter(adapter);
		try {
			etResturentName.setText(getIntent().getExtras().getString(
					"restaurantName"));
			tvBookingText6.setText(getIntent().getExtras().getString(
					"restaurantDeals"));
			restaurenId = getIntent().getExtras().getInt("restaurantId");

		} catch (Exception e) {

		}
		etBookingDate.setText(format.format(new Date().getTime()) + ",");
		currHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		currMinute = Calendar.getInstance().get(Calendar.MINUTE);
		if (currMinute < 30) {
			currMinute = 30;
		} else {
			currMinute = 0;
			currHour = currHour + 1;
		}
		updateTime(currHour, currMinute);

		ivCondition.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (flagCheckbox == 0) {
					ivCondition.setImageResource(R.drawable.checkbox_ticked);
					flagCheckbox = 1;
				} else {
					ivCondition.setImageResource(R.drawable.checkbox_unticked);
					flagCheckbox = 0;
				}
			}
		});

		arrow_down.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});

		/*
		 * etBookingDate.setText(new
		 * StringBuilder().append(Calendar.getInstance(
		 * ).get(Calendar.YEAR)).append("/")
		 * .append(Calendar.getInstance().get(Calendar.MONTH) + 1).append("/")
		 * .append
		 * (Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).append(" "));
		 */

		ivSearchItemAlllist.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				ivSearchItemAlllist.setSelected(true);

				startActivity(new Intent(BooknowPopupActivity.this,
						SearchListActivity.class));
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);

			}
		});

		ivMenuAlllist.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				MenuListActivity.toAllListActivity = 1;
				startActivity(new Intent(BooknowPopupActivity.this,
						MenuListActivity.class));
				overridePendingTransition(R.anim.slide_down_info,
						R.anim.slide_up_info);

			}
		});

		llPopupBookNow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				try {
					seleteTime = formatTime.parse(aTime);
					seleteRngMin = formatTime.parse("8:00 am");
					seleteRngMax = formatTime.parse("11:00 pm");

					Log.v("selectTime", String.valueOf(seleteTime));
					Log.v("seleteRngMin", String.valueOf(seleteRngMin));
					Log.v("seleteRngMax", String.valueOf(seleteRngMax));

				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if (seleteTime.compareTo(seleteRngMin) < 0
						|| seleteTime.compareTo(seleteRngMax) > 0) {
					Log.v("seleteRng", "true");

					timeValidationPopup(1);
				}

				else if (restaurenId == 0) {
					Toast.makeText(BooknowPopupActivity.this,
							"Please Select Restaurant Name", Toast.LENGTH_LONG)
							.show();
				} else if (etPersonName.getText().toString()
						.equalsIgnoreCase("")) {
					Toast.makeText(BooknowPopupActivity.this,
							"Please Enter Your Name", Toast.LENGTH_LONG).show();
				} else if (etPhone.getText().toString().equalsIgnoreCase("")) {
					Toast.makeText(BooknowPopupActivity.this,
							"Please Enter Your Mobile No.", Toast.LENGTH_LONG)
							.show();
				} else if (etEmail.getText().toString().equalsIgnoreCase("")) {
					Toast.makeText(BooknowPopupActivity.this,
							"Please Enter Your Email Id", Toast.LENGTH_LONG)
							.show();
				} else if (etEmail.getText().toString().indexOf("@") == -1
						|| etEmail.getText().toString().indexOf(".") == -1) {
					Toast.makeText(BooknowPopupActivity.this,
							"Please Enter A Valid Email Id", Toast.LENGTH_LONG)
							.show();
				} else if (etPhone.getText().toString().length() < 10) {
					Toast.makeText(BooknowPopupActivity.this,
							"Please Enter A Proper Mobile No.",
							Toast.LENGTH_LONG).show();
				} else {
					jsonObject1 = new JSONObject();
					jsonObject2 = new JSONObject();
					try {
						jsonObject2.put(TAG_ACCESSTOKN, "");
						jsonObject2.put(TAG_RESTURENTID, restaurenId);
						jsonObject2.put(TAG_NAME, etPersonName.getText()
								.toString().trim());
						jsonObject2.put(TAG_EMAILID, etEmail.getText()
								.toString().trim());
						jsonObject2.put(TAG_MOBILE, etPhone.getText()
								.toString().trim());
						jsonObject2.put(TAG_DATE, bookingDate);
						jsonObject2.put(TAG_TIME, bookingTime);
						jsonObject2.put(TAG_NOOFPERSON, personNo);
						jsonObject1.put(TAG_BOOKDATA, jsonObject2);

						Log.v("rest_value", jsonObject1.toString());

						nameValuePairsBooking = new ArrayList<NameValuePair>(2);
						nameValuePairsBooking.add(new BasicNameValuePair(
								"data", jsonObject1.toString()));
						AstClassBooking astClassBooking = new AstClassBooking();
						astClassBooking.execute("");

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		});

		etBookingDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * new SlideDateTimePicker.Builder(getSupportFragmentManager())
				 * .setListener(listener) .setInitialDate(new Date())
				 * .build().show();
				 */
				onCreateDialog(DATE_DIALOG_ID).show();
			}
		});

		etBookingTimePopup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// onCreateDialog(TIME_DIALOG_ID).show();

				timePickerDialog = new CustomTimePickerDialog(
						BooknowPopupActivity.this, timePickerListener, hour,
						minute, false);
				timePickerDialog.show();

			}
		});

		etResturentName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (_connectionDetector.isConnectingToInternet()) {
					if (!etResturentName.getText().toString()
							.equalsIgnoreCase("")) {
						jsonObject1 = new JSONObject();
						jsonObject2 = new JSONObject();

						try {
							if (rescFlag == 0) {
								jsonObject2.put(TAG_ACCESSTOKN, "");
								jsonObject2.put(TAG_RESNAME, etResturentName
										.getText().toString());
								jsonObject1.put(TAG_GETITEM, jsonObject2);
								nameValuePairs = new ArrayList<NameValuePair>(2);
								nameValuePairs.add(new BasicNameValuePair(
										"data", jsonObject1.toString()));

								AstClassRestName astClassRestName = new AstClassRestName();
								astClassRestName.execute("");
							} else {
								rescFlag = 0;
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						rescShow = 0;
						popupWindowRestorent.dismiss();
					}
				} else {
					Toast.makeText(BooknowPopupActivity.this,
							"No internet connection available",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		spnBookingPersonNo
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						if (position < 4) {
							personNo = position + 1;
						} else {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									BooknowPopupActivity.this);
							alertDialogBuilder.setTitle("Notice");
							alertDialogBuilder
									.setMessage("For more than 4 guests, contact EazyConcierge at 786 100 4444");
							alertDialogBuilder.setPositiveButton("Ok",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											String number = "7861004444";
											Intent dial = new Intent();
											dial.setAction("android.intent.action.DIAL");
											dial.setData(Uri.parse("tel:"
													+ number));
											startActivity(dial);

											spnBookingPersonNo.setSelection(0);
											personNo = 1;

										}
									});
							alertDialogBuilder.setNegativeButton("Cancel",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();

											spnBookingPersonNo.setSelection(0);
											personNo = 1;
										}
									});

							AlertDialog alertDialog = alertDialogBuilder
									.create();
							alertDialog.show();
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});

		llTermsAndCodition.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri
						.parse("https://www.eazydiner.com/terms"));
				startActivity(intent);
			}
		});

		llAllResturant.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Constant.FILTER_BY_LIST = 0;
				Constant.NEAR_BY_LIST = 2;
				Intent intent = new Intent(BooknowPopupActivity.this,
						AlllistActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		etPersonName.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				setEditTextHint();
				etPersonName.setHint("");
				return false;
			}
		});
		etPhone.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				setEditTextHint();
				etPhone.setHint("");
				return false;
			}
		});
		etEmail.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				setEditTextHint();
				etEmail.setHint("");
				return false;
			}
		});
		etResturentName.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				setEditTextHint();
				etResturentName.setHint("");
				return false;
			}
		});
	}

	@SuppressLint({ "CutPasteId", "SimpleDateFormat" })
	private void initialize() {
		llPopupBookNow = (LinearLayout) findViewById(R.id.llPopupBookNow);
		spnBookingPersonNoPopup = (Spinner) findViewById(R.id.spnBookingPersonNoPopup);
		etBookingDate = (EditText) findViewById(R.id.etBookingDatePopup);
		// etBookingTime = (EditText)findViewById(R.id.etBookingTimePopup);
		ivSearchItemAlllist = (ImageView) findViewById(R.id.ivSearchItemAlllist);
		ivMenuAlllist = (ImageView) findViewById(R.id.ivMenuAlllist);
		llRestName = (LinearLayout) findViewById(R.id.llRestName);
		etResturentName = (EditText) findViewById(R.id.etResturentNamePopup);
		spnBookingPersonNo = (Spinner) findViewById(R.id.spnBookingPersonNoPopup);
		tvBookingText1 = (TextView) findViewById(R.id.tvBookingText1);
		tvBookingText3 = (TextView) findViewById(R.id.tvBookingText3);
		tvBookingText5 = (TextView) findViewById(R.id.tvBookingText5);
		tvBookingText6 = (TextView) findViewById(R.id.tvBookingText6);
		tvBookingText7 = (TextView) findViewById(R.id.tvBookingText7);
		arrow_down = (ImageView) findViewById(R.id.arrow_down);
		etPersonName = (EditText) findViewById(R.id.etBookingNamePopup);
		etPhone = (EditText) findViewById(R.id.etBookingMobilePopup);
		etEmail = (EditText) findViewById(R.id.etBookingEmailPopup);
		llTermsAndCodition = (LinearLayout) findViewById(R.id.llTermsAndCodition);
		llAllResturant = (LinearLayout) findViewById(R.id.llAllResturant);
		arrayList = new ArrayList<RestorentItem>();
		adpterObj = new RestaurantAdapter(BooknowPopupActivity.this, arrayList);
		ivCondition = (ImageView) findViewById(R.id.ivCondition);
		// format = new SimpleDateFormat("EEE dd MMM, hh:mm aa");
		format = new SimpleDateFormat("EEE dd MMM");
		_connectionDetector = new ConnectionDetector(BooknowPopupActivity.this);
		etBookingTimePopup = (EditText) findViewById(R.id.etBookingTimePopup);
		formatDate = new SimpleDateFormat("dd-MM-yyyy");

		formatTime = new SimpleDateFormat("h:m a");
		_pref = new Pref(BooknowPopupActivity.this)
				.getSharedPreferencesInstance();

		setTextFont();
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			DatePickerDialog datePickerDialog = new DatePickerDialog(
					BooknowPopupActivity.this, datePickerListener, Calendar
							.getInstance().get(Calendar.YEAR), Calendar
							.getInstance().get(Calendar.MONTH), Calendar
							.getInstance().get(Calendar.DAY_OF_MONTH));
			datePickerDialog.getDatePicker().setMinDate(
					System.currentTimeMillis() - 1000);
			return datePickerDialog;
		case TIME_DIALOG_ID:
			// set time picker as current date
			/*
			 * hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY); minute =
			 * Calendar.getInstance().get(Calendar.MINUTE); return new
			 * TimePickerDialog(BooknowPopupActivity.this, timePickerListener,
			 * hour, minute, false); timePickerDialog = new
			 * CustomTimePickerDialog(BooknowPopupActivity.this,
			 * timePickerListener, hour, minute, false);
			 */
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		@Override
		public void onDateSet(DatePicker datePickert, int selectedYear,
				int selectedMonth, int selectedDay) {
			try {
				year = selectedYear;
				month = selectedMonth;
				day = selectedDay;

				String monthStr = "";
				if (month + 1 < 10) {
					monthStr = String.valueOf(month + 1);
				} else {
					monthStr = String.valueOf(month + 1);
				}

				String currentDate = new StringBuilder()
						.append(String.valueOf(Calendar.getInstance().get(
								Calendar.DAY_OF_MONTH)))
						.append('-')
						.append(Calendar.getInstance().get(Calendar.MONTH) + 1)
						.append('-')
						.append(String.valueOf(Calendar.getInstance().get(
								Calendar.YEAR))).toString();

				String selectDate = String.valueOf(day) + "-" + monthStr + "-"
						+ String.valueOf(year);

				Date date = formatDate.parse(selectDate);
				Date date2 = formatDate.parse(currentDate);

				long dateDiff = (date.getTime() - date2.getTime())
						/ (1000 * 60 * 60 * 24);

				if (dateDiff > 90) {

					timeValidationPopup(3);

					bookingDate = currentDate;
					selectDate = currentDate;
					etBookingDate.setText(format.format(date2) + ",");

					Log.v("bookingDate1", bookingDate);

				}

				else {

					bookingDate = selectDate;
					etBookingDate.setText(format.format(date) + ",");

					Log.v("bookingDate2", bookingDate);

				}

				currHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
				currMinute = Calendar.getInstance().get(Calendar.MINUTE);
				if (currMinute < 30) {
					currMinute = 30;
				} else {
					currMinute = 0;
					currHour = currHour + 1;
				}
				updateTime(currHour, currMinute);

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
			// TODO Auto-generated method stub
			hour = hourOfDay;
			minute = minutes;

			updateTime(hour, minute);
		}

	};

	// Used to convert 24hr format to 12hr format with AM/PM values
	private void updateTime(int hours, int mins) {

		int selectedHour = hours;
		int selectedMins = mins;

		selectTime = String.valueOf(hours) + ":" + String.valueOf(mins);
		String timeSet = "";

		if (hours > 12) {
			hours -= 12;
			timeSet = "PM";
		} else if (hours == 0) {
			hours += 12;
			timeSet = "AM";
		} else if (hours == 12)
			timeSet = "PM";
		else
			timeSet = "AM";

		String minutes = "";
		if (mins < 10)
			minutes = "0" + mins;
		else
			minutes = String.valueOf(mins);

		aTime = new StringBuilder().append(hours).append(':').append(minutes)
				.append(" ").append(timeSet).toString();

		bookingTime = new StringBuilder().append(selectedHour).append(':')
				.append(selectedMins).toString();

		etBookingTimePopup.setText(aTime);

		String currentDate = new StringBuilder()
				.append(String.valueOf(Calendar.getInstance().get(
						Calendar.DAY_OF_MONTH)))
				.append('-')
				.append(Calendar.getInstance().get(Calendar.MONTH) + 1)
				.append('-')
				.append(String.valueOf(Calendar.getInstance()
						.get(Calendar.YEAR))).toString();

		int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);

		String timeset1 = "";

		if (currentHour > 12) {
			currentHour -= 12;
			timeset1 = "PM";
		} else if (currentHour == 0) {
			currentHour += 12;
			timeset1 = "AM";
		} else if (currentHour == 12)
			timeset1 = "PM";
		else
			timeset1 = "AM";

		String currentminutes = "";
		if (currentMinute < 10) {
			currentminutes = "0" + String.valueOf(currentMinute);
		} else {
			currentminutes = String.valueOf(currentMinute);
		}

		// Append in a StringBuilder

		String currTime = new StringBuilder().append(currentHour).append(':')
				.append(currentminutes).append(" ").append(timeset1).toString();

		try {

			seleteTime = formatTime.parse(aTime);
			currentTime = formatTime.parse(currTime);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (bookingDate.equalsIgnoreCase(currentDate)
				&& seleteTime.compareTo(currentTime) < 0) {

			Log.v("test",
					String.valueOf(bookingDate.equalsIgnoreCase(currentDate)
							&& seleteTime.compareTo(currentTime) < 0));

			timeValidationPopup(2);

		}

	}

	private void timeValidationPopup(int flagPopup) {
		// TODO Auto-generated method stub

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				BooknowPopupActivity.this);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View alertView = inflater.inflate(R.layout.alert_layout, null);
		textAlertMessageOne = (TextView) alertView
				.findViewById(R.id.textAlertMessageOne);
		textAlertMessageTwo = (TextView) alertView
				.findViewById(R.id.textAlertMessageTwo);
		setTextFontAlert();
		if (flagPopup == 1) {
			textAlertMessageOne.setText("Please select time between");
			textAlertMessageTwo.setText("8:00 am to 11:00 pm");
		} else if (flagPopup == 2) {
			textAlertMessageOne.setText("Please select proper time");
			textAlertMessageTwo.setVisibility(View.GONE);
		} else {
			textAlertMessageOne.setText("Please select date within 90 days");
			textAlertMessageTwo.setVisibility(View.GONE);
		}
		alertDialog.setView(alertView);
		alertDialog.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						dialog.dismiss();

						currHour = Calendar.getInstance().get(
								Calendar.HOUR_OF_DAY);
						currMinute = Calendar.getInstance()
								.get(Calendar.MINUTE);
						if (currMinute < 30) {
							currMinute = 30;
						} else {
							currMinute = 0;
							currHour = currHour + 1;
						}
						updateTime(currHour, currMinute);
					}
				});
		AlertDialog dialog = alertDialog.create();
		dialog.show();

	}

	private class AstClassRestName extends AsyncTask<String, String, Long> {
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {

				JsonobjectPost j_parser = new JsonobjectPost();

				jObjList = j_parser.getJSONObj(Constant.BASE_URL
						+ "autofillrestaurant", nameValuePairs,
						jsonObject1.toString());

			} catch (Exception e) {
				Log.v("Exception", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			// TODO Auto-generated method stub
			if (jObjList.toString() != null) {
				try {
					JSONObject objData = jObjList.getJSONObject(TAG_DATA);
					JSONArray listArray = objData.getJSONArray(TAG_ITEMDATA);
					JSONObject objErr = jObjList.getJSONObject(TAG_ERNODE);
					arrayList.clear();
					if (objErr.get(TAG_ERCODE).toString().equalsIgnoreCase("0")) {
						for (int i = 0; i < listArray.length(); i++) {
							JSONObject jsonObject = listArray.getJSONObject(i);
							RestorentItem _item = new RestorentItem();

							_item.setId(jsonObject.getInt(TAG_ID));
							_item.setName(jsonObject.getString(TAG_NAME));
							_item.setLocation(jsonObject
									.getString(TAG_LOCATION));
							arrayList.add(_item);
						}

						adpterObj.notifyDataSetChanged();
						if (rescShow == 1) {
							popupWindowRestorent.dismiss();
						}
						displayRescPopup();
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					// popupWindowRestorent.dismiss();
					e.printStackTrace();
				}

			}
		}
	}

	private void displayRescPopup() {
		LayoutInflater layoutinflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View popupView = layoutinflater.inflate(R.layout.restorent_list_layout,
				null);

		popupWindowRestorent = new PopupWindow(popupView,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		popupWindowRestorent.setBackgroundDrawable(new BitmapDrawable(
				getResources(), ""));
		popupWindowRestorent.setOutsideTouchable(true);
		popupWindowRestorent.setFocusable(true);

		final ListView lvRestListPopup = (ListView) popupView
				.findViewById(R.id.lvRestList);

		lvRestListPopup.setAdapter(adpterObj);

		lvRestListPopup.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				rescFlag = 1;
				etResturentName.setText(arrayList.get(position).getName());
				restaurenId = arrayList.get(position).getId();
				popupWindowRestorent.dismiss();
				popupWindowRestorent.setFocusable(false);

			}
		});

		/*
		 * popupWindowRestorent.showAtLocation(layout, Gravity.NO_GRAVITY, p.x +
		 * OFFSET_X, p.y + OFFSET_Y);
		 */
		rescShow = 1;
		popupWindowRestorent.showAsDropDown(llRestName);
	}

	private class AstClassBooking extends AsyncTask<String, String, Long> {

		private AlertDialog dialog;

		public AstClassBooking() {
			CustomAlertProgressDialog alertProgressDialog = new CustomAlertProgressDialog(
					BooknowPopupActivity.this, "Please wait...");
			dialog = alertProgressDialog.getDialog();
			dialog.show();
		}

		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				JsonobjectPost j_parser = new JsonobjectPost();

				jObjList = j_parser.getJSONObj(Constant.BASE_URL
						+ "resturentbooking", nameValuePairsBooking,
						jsonObject1.toString());

			} catch (Exception e) {
				Log.v("Exception", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			if (jObjList.toString() != null) {

				try {
					JSONObject objData = jObjList.getJSONObject(TAG_DATA);
					JSONObject objItemData = objData
							.getJSONObject(TAG_ITEMDATA);
					/*
					 * JSONObject objErr = jObjList.getJSONObject(TAG_ERNODE);
					 * if(objErr.getString(TAG_ERCODE).equalsIgnoreCase("0")) {
					 */
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							BooknowPopupActivity.this);
					alertDialogBuilder.setTitle("Booking Details");
					alertDialogBuilder
							.setMessage("Thank You\nYour Booking Id Is "
									+ objItemData.getString(TAG_BOOKINGID));
					alertDialogBuilder.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									startActivity(new Intent(
											BooknowPopupActivity.this,
											HomeActivity.class));

								}
							});
					alertDialogBuilder.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});

					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
					// }

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		rlBookNowBultton.setBackgroundColor(Color.parseColor("#FC4415"));
		overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
	}

	private void setTextFont() {

		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		String fontPath3 = "fonts/Aller_LtIt.ttf";
		Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
		Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
		Typeface tf3 = Typeface.createFromAsset(getAssets(), fontPath3);

		tvBookingText1.setTypeface(tf1);
		tvBookingText3.setTypeface(tf2);
		tvBookingText5.setTypeface(tf3);
		tvBookingText6.setTypeface(tf2);
		tvBookingText7.setTypeface(tf2);
		etBookingDate.setTypeface(tf2);
		etPersonName.setTypeface(tf2);
		etResturentName.setTypeface(tf2);
		etPhone.setTypeface(tf2);
		etEmail.setTypeface(tf2);

	}

	private void setTextFontAlert() {

		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		String fontPath3 = "fonts/Aller_LtIt.ttf";
		Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
		Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
		Typeface tf3 = Typeface.createFromAsset(getAssets(), fontPath3);

		textAlertMessageOne.setTypeface(tf2);
		textAlertMessageTwo.setTypeface(tf2);

	}

	/*
	 * private SlideDateTimeListener listener = new SlideDateTimeListener() {
	 * 
	 * @SuppressLint("SimpleDateFormat")
	 * 
	 * @Override public void onDateTimeSet(Date date) { // TODO Auto-generated
	 * method stub etBookingDate
	 * .setText(String.valueOf(format.format(date.getTime())));
	 * 
	 * formatDate = new SimpleDateFormat("yyyy/MM/dd"); formatTime = new
	 * SimpleDateFormat("kk:mm");
	 * 
	 * bookingDate = String.valueOf(formatDate.format(date.getTime()));
	 * bookingTime = String.valueOf(formatTime.format(date.getTime()));
	 * 
	 * Log.e("time", bookingTime); Log.e("date", bookingDate);
	 * 
	 * }
	 * 
	 * public void onDateTimeCancel() {
	 * 
	 * }; };
	 */

	private void setEditTextHint() {
		etResturentName.setHint("RESTAURANT NAME");
		etPersonName.setHint("NAME");
		etPhone.setHint("MOBILE");
		etEmail.setHint("EMAIL");
	}
}
