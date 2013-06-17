package kr.ac.kumoh.Amobile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements
		MapView.MapViewEventListener, MapView.POIItemEventListener {
	private MapView mapView;
	private Button tolist, mylocation;

	private LocationManager locationManager;
	private LocationListener locationListener;
	private Criteria criteria;
	private String mygpsprovider;

	private boolean first_parsing = false;

	private double mylati, mylongti;
	private double lowlati, highlati;
	private double lowlongti, highlongti;
	private final double X_RANGE = 0.06;
	private final double Y_RANGE = 0.06;

	private int product_cnt;
	private ArrayList<Data> data;

	private MapPOIItem[] poiItem;
	private JSONArray jsonArray;

	private ProgressDialog mPdProgress;

	private boolean isThreadStatus = false;

	private GetJson getJson;
	private Handler handler;
	private Runnable runnable;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mapView = (MapView) findViewById(R.id.daumMapView);
		mapView.setDaumMapApiKey("f401d2bb5e8f15928c99c68e0a71f43ea29fb2e0");
		mapView.setMapViewEventListener(this);
		mapView.setPOIItemEventListener(this);
		mapView.setMapType(MapView.MapType.Standard);

		tolist = (Button) findViewById(R.id.Tolist);
		tolist.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (first_parsing == true && isThreadStatus == false) {

					if (product_cnt == 0) {
						Toast.makeText(MainActivity.this, "현 지역에 데이터가 없습니다",
								Toast.LENGTH_SHORT).show();
					} else {
						Intent intent = new Intent(MainActivity.this,
								ListActivity.class);
						intent.putParcelableArrayListExtra("data", data);
						startActivity(intent);
					}
				}
			}
		});

		mylocation = (Button) findViewById(R.id.myLocation);
		mylocation.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (first_parsing == true) {
					user_location();
				}
			}
		});

		// location////////////////////////////////////////////////////////////////////
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == false) {
			Toast.makeText(this, "GPS 사용을 체크해주세요.", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(
					android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
		}

		criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(true);

		criteria.setPowerRequirement(Criteria.POWER_LOW);
		mygpsprovider = locationManager.getBestProvider(criteria, true);

		locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				mylati = location.getLatitude();
				mylongti = location.getLongitude();

				if (first_parsing == false) {
					mapView.setZoomLevel(6, false);
					user_location();
					lowlati = mylati - X_RANGE;
					highlati = mylati + X_RANGE;
					lowlongti = mylongti - Y_RANGE;
					highlongti = mylongti + Y_RANGE;

					parsing(mylati, mylongti);

					first_parsing = true;
				}
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};
		locationManager.requestLocationUpdates(mygpsprovider, 0, 0,
				locationListener);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}

	// GetJson/////////////////////////////////////////////////////////////////////
	private class GetJson extends AsyncTask<Object, String, JSONArray> {
		protected void onPreExecute() {
			mPdProgress = new ProgressDialog(MainActivity.this);
			mPdProgress.setMessage("Loading...");
			mPdProgress.setIndeterminate(true);
			mPdProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mPdProgress.setCancelable(false);
			mPdProgress.show();
		}

		protected JSONArray doInBackground(Object... arg0) {
			try {
				DefaultHttpClient defaultClient = new DefaultHttpClient();
				HttpGet httpGetRequest = new HttpGet(
						"http://202.31.139.172:9092/index.php/mobile2/json/"
								+ Double.toString((Double) arg0[0]) + "/"
								+ Double.toString((Double) arg0[1]) + "/"
								+ Double.toString(X_RANGE + 0.01) + "/"
								+ Double.toString(Y_RANGE + 0.01));

				HttpResponse httpResponse = defaultClient
						.execute(httpGetRequest);

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(httpResponse.getEntity()
								.getContent(), "UTF-8"));
				String json = reader.readLine();
				jsonArray = new JSONArray(json);
				return jsonArray;
			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
			}
			return null;
		}

		protected void onPostExecute(JSONArray jsonArray) {

			product_cnt = jsonArray.length();
			data = new ArrayList<Data>();

			try {
				for (int i = 0; i < product_cnt; i++) {
					Data p_data = new Data();
					p_data.setdata(
							jsonArray.getJSONObject(i).getString("title1"),
							jsonArray.getJSONObject(i).getString("href"),
							jsonArray.getJSONObject(i).getString("img"),
							jsonArray.getJSONObject(i).getString("pre_price"),
							jsonArray.getJSONObject(i).getString("cur_price"),
							Double.parseDouble(jsonArray.getJSONObject(i)
									.getString("x")), Double
									.parseDouble(jsonArray.getJSONObject(i)
											.getString("y")));

					data.add(p_data);
				}

				MapPoint p = mapView.getMapCenterPoint();
				int zoomlvl = mapView.getZoomLevel();
				show_mark();
				mapView.setMapCenterPointAndZoomLevel(p, zoomlvl, false);

			} catch (JSONException e) {
				e.printStackTrace();
			}

			mPdProgress.dismiss();
			isThreadStatus = false;
			handler.removeCallbacks(runnable);
		}
	}

	// user_function/////////////////////////////////////////////////////////////////////
	public void parsing(double lati, double longti) {
		isThreadStatus = true;
		getJson = new GetJson();
		getJson.execute(lati, longti);

		handler = new Handler();
		handler.postDelayed(runnable = new Runnable() {
			public void run() {
				if (isThreadStatus == true) {
					getJson.cancel(true);
					mPdProgress.dismiss();
					Toast toast = Toast.makeText(MainActivity.this,
							"네트워크가 불안정합니다", Toast.LENGTH_SHORT);
					toast.show();
					isThreadStatus = false;
				}
			}
		}, 10000);
	}

	public void user_location() {
		mapView.setMapCenterPoint(
				MapPoint.mapPointWithGeoCoord(mylati, mylongti), true);
	}

	public void show_mark() {
		poiItem = new MapPOIItem[product_cnt];
		mapView.removeAllPOIItems();

		for (int i = 0; i < product_cnt; i++) {
			poiItem[i] = new MapPOIItem();
			poiItem[i].setItemName(data.get(i).getname());
			poiItem[i].setMapPoint(MapPoint.mapPointWithGeoCoord(data.get(i)
					.getlati(), data.get(i).getlongti()));
			poiItem[i].setTag(i);
			mapView.addPOIItem(poiItem[i]);
		}
		user_marker(mylati, mylongti);
		mapView.fitMapViewAreaToShowAllPOIItems();
	}

	public void user_marker(double lati, double longti) {

		MapPOIItem poi_user = new MapPOIItem();
		poi_user.setItemName("현위치");
		poi_user.setMapPoint(MapPoint.mapPointWithGeoCoord(lati, longti));
		poi_user.setMarkerType(MapPOIItem.MarkerType.CustomImage);
		poi_user.setCustomImageResourceId(R.drawable.user);
		poi_user.setShowCalloutBalloonOnTouch(false);
		mapView.addPOIItem(poi_user);
	}

	// MapView.MapViewEventListener/////////////////////////////////////////////
	@Override
	public void onMapViewCenterPointMoved(MapView arg0, MapPoint arg1) {
		if (first_parsing == true) {
			double maplati = arg1.getMapPointGeoCoord().latitude;
			double maplongti = arg1.getMapPointGeoCoord().longitude;

			if (maplati < lowlati || maplati > highlati
					|| maplongti < lowlongti || maplongti > highlongti) {
				lowlati = maplati - X_RANGE;
				highlati = maplati + X_RANGE;
				lowlongti = maplongti - Y_RANGE;
				highlongti = maplongti + Y_RANGE;

				if (isThreadStatus == false)
					parsing(maplati, maplongti);

			}
		}
	}

	@Override
	public void onMapViewDoubleTapped(MapView arg0, MapPoint arg1) {
	}

	@Override
	public void onMapViewInitialized(MapView arg0) {
	}

	@Override
	public void onMapViewLongPressed(MapView arg0, MapPoint arg1) {
	}

	@Override
	public void onMapViewSingleTapped(MapView arg0, MapPoint arg1) {
	}

	@Override
	public void onMapViewZoomLevelChanged(MapView arg0, int arg1) {
	}

	// POIItemEventListener///////////////////////////////////////////////////
	@Override
	public void onCalloutBalloonOfPOIItemTouched(MapView arg0, MapPOIItem arg1) {
		Uri uri = Uri.parse(data.get(arg1.getTag()).gethref());
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}

	@Override
	public void onDraggablePOIItemMoved(MapView arg0, MapPOIItem arg1,
			MapPoint arg2) {
	}

	@Override
	public void onPOIItemSelected(MapView arg0, MapPOIItem arg1) {
	}

}
