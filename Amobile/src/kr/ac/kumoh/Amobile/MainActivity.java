package kr.ac.kumoh.Amobile;

import java.util.ArrayList;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements
//MapView.OpenAPIKeyAuthenticationResultListener
//MapView.CurrentLocationEventListener,
/*MapView.MapViewEventListener,*/
MapView.POIItemEventListener
{	
	
	private MapView mapView;
	private Button tolist, mylocation;
	private LocationManager locationManager;
	private LocationListener locationListener;
	private double lati, longti;
	private boolean goto_user = false;
	
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
				intent.putParcelableArrayListExtra("data", data);
				startActivity(intent);
			}
		});
		
		mylocation = (Button) findViewById(R.id.myLocation);
		mylocation.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				//mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
				goto_user =false;
				user_location();								
			}
		});
		////////////////////location 리스너/////////////////////////

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new LocationListener(){
			public void onLocationChanged(Location location){ 
				lati = location.getLatitude();
				longti = location.getLongitude();
				if (goto_user == false){
					mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lati,longti),  true);
					goto_user = true;
				}
			}
			public void onStatusChanged(String provider, int status, Bundle extras){}
			public void onProviderEnabled(String provider){				
			}
			public void onProviderDisabled(String provider){}
		};	
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		
				
		
		////////////처음 자신의 위치 띄우기////////////////////
		user_location();
		
		/////////////////DB읽기,마커띄우기->onMapViewCenterPointMoved로 나중에//////////
		load_db();
		show_mark();
		///////////////마커 띄우기//////////////////////
		
		
	}

	/*
	////////////////////////mapview 관련 /////////////////////////////
	@Override
	public void onMapViewCenterPointMoved(MapView arg0, MapPoint arg1) {
		//맵 이동 했을때 이거 쓰거나 안쓰거나.
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
	/////////////////////////////////////////////////////*/
	
	/////////////////////마커 관련 //////////////////////////////
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
		//서울시청 문제 최근 gps 주소 가져오는 방법.
		mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lati,longti),  true);
	}
	
	
	public void load_db(){
		///////////////////db 값 임의 초기화////////////////////
		
		data = new ArrayList<Data>();
		Data p_data ;
		
		/*???
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
		
		
		/*db 갯수 세기 방법 추가
		product_cnt = 100;//순차적으로 cnt or 데이터 가져오기전에 		
		data = new Data[product_cnt];
		
		/*db에서 메모리로 데이터 넣기.
		while(db!=NULL){	
			data[i].setdata(가게명, url주소, 위도, 경도) //DB에서 일루
		}	*/
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
	
	
	

	
	
	
	/*
	public void user_marker(double lati, double longti){
		MapPOIItem poi_user = new MapPOIItem();
		
		poi_user.setItemName("현위치");
		//poiItem.setUserObject(String.format("item%d", 2));
		poi_user.setMapPoint(MapPoint.mapPointWithGeoCoord(lati,longti));
		poi_user.setMarkerType(MapPOIItem.MarkerType.CustomImage);
		poi_user.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
		poi_user.setCustomImageResourceId(R.drawable.user);
		//poiItem.setCustomImageAnchorPointOffset(new MapPOIItem.ImageOffset(22,0));
		//poiItem.setShowCalloutBalloonOnTouch(true);
		mapView.addPOIItem(poi_user); 
		
	}*/
	
	
	
	
	/*
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
		
	}*/
	
}
