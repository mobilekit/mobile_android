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
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements
// MapView.OpenAPIKeyAuthenticationResultListener
// MapView.CurrentLocationEventListener,
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
	private final double X_RANGE = 0.025;
	private final double Y_RANGE = 0.025;

	private int product_cnt;
	private ArrayList<Data> data;

	private MapPOIItem[] poiItem;
	private JSONArray jsonArray;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mapView = (MapView) findViewById(R.id.daumMapView);
		mapView.setDaumMapApiKey("f401d2bb5e8f15928c99c68e0a71f43ea29fb2e0");
		mapView.setMapViewEventListener(this);
		// mapView.setOpenAPIKeyAuthenticationResultListener(this);
		// mapView.setCurrentLocationEventListener(this);
		mapView.setPOIItemEventListener(this);
		mapView.setMapType(MapView.MapType.Standard);
		// //////////////////// button /////////////////////////
		tolist = (Button) findViewById(R.id.Tolist);
		tolist.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (first_parsing == true) {
					Intent intent = new Intent(MainActivity.this,
							ListActivity.class);
					intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT); 
					intent.putParcelableArrayListExtra("data", data);
					startActivity(intent);
				}
			}
		});
		mylocation = (Button) findViewById(R.id.myLocation);
		mylocation.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (first_parsing == true) {
					user_location(mylati, mylongti);
				}
			}
		});
		// /////////////////////////////////////////////////////

		// //////////////////location/////////////////////////
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		
		criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(true);// 공급자가 금전적 비용을 부과할수 있는지 여부.
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		mygpsprovider = locationManager.getBestProvider(criteria, true);

		locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				mylati = location.getLatitude();
				mylongti = location.getLongitude();
				if (first_parsing == false) {				
					first_parsing = true;

					new GetJson().execute(mylati, mylongti);
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

	private class GetJson extends AsyncTask<Object, String, JSONArray> {
		
		MapPoint p;
		@Override
		protected JSONArray doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			try {
				p = MapPoint.mapPointWithGeoCoord((Double) arg0[0],(Double) arg0[1]);
				lowlati = (Double)arg0[0] - X_RANGE;
				highlati = (Double)arg0[0] + X_RANGE;
				lowlongti = (Double)arg0[1] - Y_RANGE;
				highlongti = (Double)arg0[1] + Y_RANGE;
				// Create a new HTTP Client
				DefaultHttpClient defaultClient = new DefaultHttpClient();
				// Setup the get request
				HttpGet httpGetRequest = new HttpGet(
						"http://202.31.139.172:9092/index.php/mobile2/json/"
								+ Double.toString((Double) arg0[0]) + "/"
								+ Double.toString((Double) arg0[1]) + "/"
								+ Double.toString(X_RANGE * 2) + "/"
								+ Double.toString(Y_RANGE * 2));
				// Execute the request in the client
				HttpResponse httpResponse = defaultClient
						.execute(httpGetRequest);
				// Grab the response
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(httpResponse.getEntity()
								.getContent(), "UTF-8"));
				String json = reader.readLine();
				// Instantiate a JSON object from the request response

				jsonArray = new JSONArray(json);
				return jsonArray;

			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);

			if (jsonArray != null) {

				product_cnt = jsonArray.length();
				data = new ArrayList<Data>();

				try {
					for (int i = 0; i < product_cnt; i++) {
						Data p_data = new Data();
						p_data.setdata(
								jsonArray.getJSONObject(i).getString("title1"),
								jsonArray.getJSONObject(i).getString("href"),
								jsonArray.getJSONObject(i).getString("img"),
								jsonArray.getJSONObject(i).getString(
										"pre_price"), jsonArray
										.getJSONObject(i)
										.getString("cur_price"), Double
										.parseDouble(jsonArray.getJSONObject(i)
												.getString("x")), Double
										.parseDouble(jsonArray.getJSONObject(i)
												.getString("y")));
						data.add(p_data);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int zoomlvl = mapView.getZoomLevel();
				show_mark();
				mapView.setMapCenterPointAndZoomLevel(p, zoomlvl, false);
			}
		}
	}

	public void user_location(double lati, double longti) {
		new GetJson().execute(lati, longti);
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
		mapView.fitMapViewAreaToShowAllPOIItems();
	}

	// /////////////////////////////////////////////////////////////////

	// /////////////////MapView.MapViewEventListener///////////////////
	@Override
	public void onMapViewCenterPointMoved(MapView arg0, MapPoint arg1) {
		if (first_parsing == true) {
			double maplati = arg1.getMapPointGeoCoord().latitude;
			double maplongti = arg1.getMapPointGeoCoord().longitude;

			if (maplati < lowlati || maplati > highlati
					|| maplongti < lowlongti || maplongti > highlongti) {
				new GetJson().execute(maplati, maplongti);
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

	// /////////////////////////////////////////////////////////

	// ///////////////////POIItemEventListener///////////////////
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
	// /////////////////////////////////////////////////////////
}
