package kr.ac.kumoh.Amobile;

import java.util.ArrayList;

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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements
//MapView.OpenAPIKeyAuthenticationResultListener
MapView.CurrentLocationEventListener,
/*MapView.MapViewEventListener,*/
MapView.POIItemEventListener
{	
	private MapView mapView;
	private Button tolist, mylocation;
	private LocationManager locationManager;
	private Criteria criteria;
	private String provider;
	
	///??/
	private LocationListener locationListener;
	private double mylati, mylongti;
	//// marker 관련  변수//
	private int product_cnt;
	private ArrayList<Data> data;
	private MapPOIItem[] poiItem ;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
					
		mapView = (MapView) findViewById(R.id.daumMapView);
		mapView.setDaumMapApiKey("f401d2bb5e8f15928c99c68e0a71f43ea29fb2e0");
		/* mapView.setMapViewEventListener(this);*/
		// mapView.setOpenAPIKeyAuthenticationResultListener(this);
		// mapView.setCurrentLocationEventListener(this);
		mapView.setPOIItemEventListener(this);
		mapView.setMapType(MapView.MapType.Standard);
		
	
		///////////// btn 리스너/////////////////////
		tolist = (Button) findViewById(R.id.Tolist);
		tolist.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,ListActivity.class);
				//액티비티 데이터 교환
				//intent.putExtra("data", data);
				intent.putParcelableArrayListExtra("data", data);
				startActivity(intent);
			}
		});
		
		mylocation = (Button) findViewById(R.id.myLocation);
		mylocation.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				//mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
			//	goto_user =false;
				user_location();								
			}
		});
		////////////////////location/////////////////////////
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(true);//공급자가 금전적 비용을 부과할수 있는지 여부.
		criteria.setPowerRequirement(Criteria.POWER_LOW);
        provider = locationManager.getBestProvider(criteria, true);
		

		locationListener = new LocationListener(){
			public void onLocationChanged(Location location){ 
				mylati = location.getLatitude();
				mylongti = location.getLongitude();
			}
			public void onStatusChanged(String provider, int status, Bundle extras){}
			public void onProviderEnabled(String provider){}
			public void onProviderDisabled(String provider){}
		};	
		locationManager.requestLocationUpdates(provider, 0, 0, locationListener);		
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	
		user_location();	
		load_db();
		show_mark();
		user_location();
		 
	}

	
	/////////////////////POIItemEventListener///////////////////
	@Override
	public void onCalloutBalloonOfPOIItemTouched(MapView arg0, MapPOIItem arg1) {
		Uri uri = Uri.parse(data.get(arg1.getTag()).geturl());
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
	@Override
	public void onDraggablePOIItemMoved(MapView arg0, MapPOIItem arg1,
			MapPoint arg2) {
		// TODO Auto-generated method stub	
	}
	@Override
	public void onPOIItemSelected(MapView arg0, MapPOIItem arg1) {
		// TODO Auto-generated method stub	
	}
	///////////////////////////////////////////////////////////
	
	
	
	
	public void user_location(){
		mapView.setMapCenterPointAndZoomLevel
		(MapPoint.mapPointWithGeoCoord(mylati,mylongti), 1, true);
		
		Toast.makeText(this,
				mapView.getMapCenterPoint().getMapPointGeoCoord().latitude
				+" "+mapView.getMapCenterPoint().getMapPointGeoCoord().longitude,
				Toast.LENGTH_SHORT).show();
	}
	
	
	
	public void load_db(){
		data = new ArrayList<Data>();
		Data p_data ;		
		/*
		while(true){
			p_data = new Data();
			p_data.setdata(name, url, lati, longti);
			data.add(p_data);
		}
		*/
		p_data = new Data();
		p_data.setdata("이정돈 돼야지 100%할인 쿠폰", "http://www.naver.com", 36.139042, 128.3968962);
		data.add(p_data);	
		p_data = new Data();
		p_data.setdata("파송송 닭닭닭 70% 할인 쿠폰", "http://www.daum.net", 36.1414065, 128.3962129);
		data.add(p_data);	
		p_data = new Data();
		p_data.setdata("더롹 50%할인 쿠폰", "http://www.google.com", 36.1396188, 128.3964415);
		data.add(p_data);
		
		product_cnt = data.size();		
	}
	
	public void show_mark(){		
		poiItem = new MapPOIItem[product_cnt];	
	
		for(int i=0; i<product_cnt; i++){
			poiItem[i] = new MapPOIItem();
			poiItem[i].setItemName(data.get(i).getname());
			poiItem[i].setMapPoint(MapPoint.mapPointWithGeoCoord(data.get(i).getlati(),data.get(i).getlongti()));
			poiItem[i].setTag(i);		
			mapView.addPOIItem(poiItem[i]);
		}	
		mapView.fitMapViewAreaToShowAllPOIItems();	
	}



	@Override
	public void onCurrentLocationDeviceHeadingUpdate(MapView arg0, float arg1) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onCurrentLocationUpdate(MapView arg0, MapPoint arg1, float arg2) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onCurrentLocationUpdateCancelled(MapView arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onCurrentLocationUpdateFailed(MapView arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
