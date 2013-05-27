package kr.ac.kumoh.Amobile;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements
//MapView.OpenAPIKeyAuthenticationResultListener
MapView.CurrentLocationEventListener,
MapView.MapViewEventListener,
MapView.POIItemEventListener
{	
	private MapView mapView;
	private Button tolist, mylocation;
	private LocationManager locationManager;
	private LocationListener locationListener;
	private double lati, longti;
	private boolean set = false;
	MapPOIItem[] poiItem ;
	
	//// marker 관련 임시 변수//
	int product_cnt;
	
	String[] db_name;
	double[] db_p_lati;  
	double[] db_p_longti; 
	String[] db_url;	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
			
			
		mapView = (MapView) findViewById(R.id.daumMapView);
		mapView.setDaumMapApiKey("f401d2bb5e8f15928c99c68e0a71f43ea29fb2e0");
		mapView.setMapViewEventListener(this);
		// mapView.setOpenAPIKeyAuthenticationResultListener(this);
		// mapView.setCurrentLocationEventListener(this);
		mapView.setPOIItemEventListener(this);
		mapView.setMapType(MapView.MapType.Standard);
		
		mapView.setCurrentLocationEventListener(this);
		
		///////////////////db 값 임의 초기화////////////////////
		product_cnt=3;
		db_name = new String[product_cnt];// "이정돈 돼야지 100%할인 쿠폰";
		db_p_lati = new double[product_cnt];//37.53737528;
		db_p_longti = new double[product_cnt];//127.00557633;
		db_url = new String[product_cnt];  //"http://www.naver.com" ;
		
		
		db_name[0]="이정돈 돼야지 100%할인 쿠폰";
		db_name[1]="파송송 닭닭닭 70% 할인 쿠폰 ";
		db_name[2]="더롹 50%할인 쿠폰";
		db_p_lati[0]=36.139042;
		db_p_lati[1]=36.1414065;
		db_p_lati[2]=36.1396188;
		db_p_longti[0]=128.3968962;
		db_p_longti[1]=128.3962129;
		db_p_longti[2]=128.3964415;
		db_url[0]="http://www.naver.com";		
		db_url[1]="http://www.daum.net";		
		db_url[2]="http://www.google.com";	
		//////////////////////////////////////////////
	
		//////////////List btn 관련/////////////////////
		tolist = (Button) findViewById(R.id.Tolist);
		tolist.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				Intent intent = new Intent(MainActivity.this,ListActivity.class);
				startActivity(intent);
			}
		});
		
		mylocation = (Button) findViewById(R.id.myLocation);
		mylocation.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
			}
		});
		
			
		///////////////////마커 띄우기//////////////////////
		this.show_mark(product_cnt);
		
		//////////////처음 자신의 위치 띄우기////////////////////
		this.user_location();
		
		
		
	}

	///////////////////////mapview 관련 /////////////////////////////
	@Override
	public void onMapViewCenterPointMoved(MapView arg0, MapPoint arg1) {
		
	}
	@Override
	public void onMapViewDoubleTapped(MapView arg0, MapPoint arg1) {

	}
	@Override
	public void onMapViewInitialized(MapView arg0) {
		
	}
	@Override
	public void onMapViewLongPressed(MapView arg0, MapPoint arg1) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onMapViewSingleTapped(MapView arg0, MapPoint arg1) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onMapViewZoomLevelChanged(MapView arg0, int arg1) {
		// TODO Auto-generated method stub

	}
	/////////////////////////////////////////////////////
	
	
	/////////////////////마커 관련 //////////////////////////////
	@Override
	public void onCalloutBalloonOfPOIItemTouched(MapView arg0, MapPOIItem arg1) {
		// TODO Auto-generated method stub
		String[] url = new String[product_cnt];  //"http://www.naver.com" ;
		int tag;
		
		for(int i=0; i<product_cnt; i++)
			url[i] = db_url[i];			
		
		tag=arg1.getTag();
		
		Uri uri = Uri.parse(url[tag-1]);
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
		//이전 gps 주소 known

		mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lati,longti),  true);
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new LocationListener(){
			public void onLocationChanged(Location location){ 
				lati = location.getLatitude();
				longti = location.getLongitude();
				if (set==false){
					mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lati,longti),  true);
					set = true;				
				}
			}
			public void onStatusChanged(String provider, int status, Bundle extras){}
			public void onProviderEnabled(String provider){
				
				
			}
			public void onProviderDisabled(String provider){}
		};		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}
	
	public void show_mark(int product_cnt){
		String[] name = new String[product_cnt];// "이정돈 돼야지 100%할인 쿠폰";
		double[] p_lati = new double[product_cnt];//37.53737528;
		double[] p_longti = new double[product_cnt];//127.00557633;
		
		MapPOIItem[] 
		poiItem = new MapPOIItem[product_cnt];
		
		for(int i=0; i<product_cnt; i++){
			name[i] = db_name[i];
			p_lati[i] = db_p_lati[i];
			p_longti[i] = db_p_longti[i];
			
			poiItem[i]= new MapPOIItem();
			poiItem[i].setItemName(name[i]);
			poiItem[i].setTag(1+i);
			poiItem[i].setMapPoint(MapPoint.mapPointWithGeoCoord(p_lati[i],p_longti[i]));
			poiItem[i].setMarkerType(MapPOIItem.MarkerType.BluePin);
			poiItem[i].setShowAnimationType(MapPOIItem.ShowAnimationType.DropFromHeaven);
			poiItem[i].setShowCalloutBalloonOnTouch(true);
			mapView.addPOIItem(poiItem[i]);	
		}
		mapView.fitMapViewAreaToShowAllPOIItems();

	}
	@Override
	public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
	  MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();
	  
	  Log.i("MainActivity", String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", 
	  mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));
	}

	@Override
	public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float headingAngle) {
	  Log.i("MainActivity", String.format("MapView onCurrentLocationDeviceHeadingUpdate: device heading = %f degrees", headingAngle));
	}

	@Override
	public void onCurrentLocationUpdateFailed(MapView mapView) {
	  // 현위치 갱신 실패 시
	 
	}
	@Override
	public void onCurrentLocationUpdateCancelled(MapView arg0) {
		// TODO Auto-generated method stub
		
	}


	
}
