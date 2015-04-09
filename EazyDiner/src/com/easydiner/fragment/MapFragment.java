package com.easydiner.fragment;

import java.util.ArrayList;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.classes.Constant;
import com.classes.Pref;
import com.eazydiner.R;
import com.easydiner.activities.DetailsActivity;
import com.easydiner.dataobject.ListItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {

	private MapView m;
	private static GoogleMap googleMap;
	private static ArrayList<ListItem> arrayList;
	private static int resultCount = 0;
	private Constant _constant;
	private LatLng currentPos;
	private SharedPreferences _pref;
	private SharedPreferences.Editor _pEditor;
	private TextView name, type, address;
	private LinearLayout llMapRestaurantName, llTransitLayout, llCarLayout;
	private int position;

	@SuppressWarnings("unchecked")
	public static MapFragment newInstance(Bundle bundleToPass) {
		// TODO Auto-generated method stub
		MapFragment frag = new MapFragment();
		arrayList = (ArrayList<ListItem>) bundleToPass.getSerializable("list");
		resultCount = (int) bundleToPass.getInt("resultCount");
		frag.setArguments(bundleToPass);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		_constant = new Constant();
		View rootView = inflater.inflate(R.layout.map_fragment_layout,
				container, false);

		m = (MapView) rootView.findViewById(R.id.map);
		m.onCreate(savedInstanceState);
		m.onResume();
		_pref = new Pref(getActivity()).getSharedPreferencesInstance();
		_pEditor = new Pref(getActivity()).getSharedPreferencesEditorInstance();
		Log.e("list item", String.valueOf(resultCount));
		Log.e("MAP_DETAILS", String.valueOf(_constant.MAP_DETAILS));
		create();
		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (googleMap != null) {
			arrayList.clear();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (googleMap != null) {
			arrayList.clear();
		}
	}
	
	

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (googleMap != null) {
			arrayList.clear();
		}
	}

	@SuppressWarnings("static-access")
	private void create() {

		try {

			MapsInitializer.initialize(getActivity());
			googleMap = m.getMap();
			// map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			googleMap.setMyLocationEnabled(true);

			// For showing a move to my loction button
			// For dropping a marker at a point on the Map
			// map.addMarker(new MarkerOptions().position(new LatLng(latitude,
			// longitude)).title("My Home").snippet("Home Address"));
			// For zooming automatically to the Dropped PIN Location
			// map.animateCamera(CameraUpdateFactory.newLatLngZoom(new
			// LatLng(latitude,longitude), 12.0f));

			final LatLngBounds.Builder builder = new LatLngBounds.Builder();

			// Toast.makeText(getActivity(), String.valueOf(arrayList.size()),
			// Toast.LENGTH_LONG).show();
			if (_constant.MAP_DETAILS == 0) {
				for (int i = 0; i < arrayList.size(); i++) {
					ListItem _item = arrayList.get(i);

					if (!_item.getLat().equalsIgnoreCase("(null)")
							&& !_item.getLng().equalsIgnoreCase("(null)")
							&& !_item.getLat().equalsIgnoreCase("")
							&& !_item.getLng().equalsIgnoreCase("")) {
						final LatLng pos = new LatLng(Float.parseFloat(_item
								.getLat()), Float.parseFloat(_item.getLng()));
						builder.include(pos);
						if (_item.getReviewed()) {
							BitmapDescriptor icon = BitmapDescriptorFactory
									.fromResource(R.drawable.map_balloon);
							googleMap.addMarker(new MarkerOptions()
									.position(pos)
									.title(_item.getItemname())
									.snippet(
											_item.getItemtype() + "\n"
													+ _item.getItemlocation())
									.icon(icon));
						} else {
							BitmapDescriptor icon = BitmapDescriptorFactory
									.fromResource(R.drawable.map_balloon_nonrated);
							googleMap.addMarker(new MarkerOptions()
									.position(pos)
									.title(_item.getItemname())
									.snippet(
											_item.getItemtype() + "\n"
													+ _item.getItemlocation())
									.icon(icon));
						}

					}
				}
			} else {

				final LatLng pos = new LatLng(Float.parseFloat(_pref.getString(
						"restaurent_lat", "")), Float.parseFloat(_pref
						.getString("restaurent_long", "")));
				builder.include(pos);
				/* if (_item.getReviewed()) { */
				BitmapDescriptor icon = BitmapDescriptorFactory
						.fromResource(R.drawable.map_balloon);
				googleMap.addMarker(new MarkerOptions()
						.position(pos)
						.title(_pref.getString("restaurent_name", ""))
						.snippet(
								_pref.getString("restaurent_type", "")
										+ "\n"
										+ _pref.getString(
												"restaurent_location", ""))
						.icon(icon));
			}

			if (_constant.MAP_DETAILS == 0) {
				currentPos = new LatLng(Float.parseFloat(_pref.getString(
						"currLat", "")), Float.parseFloat(_pref.getString(
						"currLng", "")));
			}

			else {
				currentPos = new LatLng(Float.parseFloat(_pref.getString(
						"restaurent_lat", "")), Float.parseFloat(_pref
						.getString("restaurent_long", "")));
			}

			if (arrayList.size() > 0) {
				googleMap
						.setOnCameraChangeListener(new OnCameraChangeListener() {

							public void onCameraChange(CameraPosition arg0) {
								googleMap.animateCamera(CameraUpdateFactory
										.newLatLngZoom(currentPos, 14.0f));
								googleMap.setOnCameraChangeListener(null);
							}
						});
			}

			if (currentPos != null) {

				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						currentPos, 10.0f));
				googleMap.setOnCameraChangeListener(null);

			}
			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getActivity(), "Sorry! unable to create maps",
						Toast.LENGTH_SHORT).show();
			}

			googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {

				@Override
				public View getInfoContents(Marker arg0) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public View getInfoWindow(Marker selectedMarker) {
					// TODO Auto-generated method stub
					View mapdetailsView = getActivity().getLayoutInflater()
							.inflate(R.layout.map_popup_layout, null);
					name = (TextView) mapdetailsView
							.findViewById(R.id.tvMapPopupItemName);
					type = (TextView) mapdetailsView
							.findViewById(R.id.tvMapPopupItemType);
					address = (TextView) mapdetailsView
							.findViewById(R.id.tvMapPopupItemAddress);
					llMapRestaurantName = (LinearLayout) mapdetailsView
							.findViewById(R.id.llMapRestaurantName);
					llTransitLayout = (LinearLayout) mapdetailsView
							.findViewById(R.id.llTransitLayout);
					llCarLayout = (LinearLayout) mapdetailsView
							.findViewById(R.id.llCarLayout);

					setTextFont();
					String[] id = selectedMarker.getId().toString().split("m");
					position = Integer.parseInt(id[1]);
					if (_constant.MAP_DETAILS == 0) {
						name.setText(arrayList.get(position).getItemname());
						type.setText(arrayList.get(position).getItemtype());
						address.setText(arrayList.get(position)
								.getItemlocation());
					} else {
						name.setText(_pref.getString("restaurent_name", ""));
						type.setText(_pref.getString("restaurent_type", ""));
						address.setText(_pref.getString("restaurent_location",
								""));
					}

					llCarLayout.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Log.v("test", "ttt");
						}
					});

					return mapdetailsView;
				}

			});

			googleMap
					.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

						@Override
						public void onInfoWindowClick(Marker selectedMarker) {
							// TODO Auto-generated method stub

							String[] id = selectedMarker.getId().toString()
									.split("m");
							position = Integer.parseInt(id[1]);

							Intent intent = new Intent(getActivity(),
									DetailsActivity.class);
							_pEditor.putString("itemId", String
									.valueOf(arrayList.get(position)
											.getItemId()));
							_pEditor.commit();
							Constant.NEAR_BY_LIST = 0;
							startActivity(intent);
							getActivity().overridePendingTransition(
									R.anim.slide_in_right,
									R.anim.slide_out_left);

							/*
							 * String uri = String.format(Locale.ENGLISH,
							 * "geo:%f,%f", Float.parseFloat(arrayList
							 * .get(position).getLat()), Float
							 * .parseFloat(arrayList.get(position) .getLng()));
							 * Intent intent = new Intent(Intent.ACTION_VIEW,
							 * Uri .parse(uri));
							 * getActivity().startActivity(intent);
							 */
						}
					});

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void setTextFont() {
		String fontPath1 = "fonts/Aller_Bd.ttf";
		String fontPath2 = "fonts/Aller_Rg.ttf";
		Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(),
				fontPath1);
		Typeface tf2 = Typeface.createFromAsset(getActivity().getAssets(),
				fontPath2);
		name.setTypeface(tf1);
		type.setTypeface(tf2);
		address.setTypeface(tf2);
	}
}
