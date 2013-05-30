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


public class MainActivity extends Activity implements
//MapView.OpenAPIKeyAuthenticationResultListener
//MapView.CurrentLocationEventListener,
MapView.MapViewEventListener,
MapView.POIItemEventListener
{	
	private MapView mapView;
	private Button tolist, mylocation;
	
	private LocationManager locationManager;
	private LocationListener locationListener;
	private Criteria criteria;
	private String mygpsprovider;
	
	private boolean first_parsing = false;
	
	private double mylati, mylongti;
	private double lowlati,highlati;
	private double lowlongti,highlongti;
	private double range = 0.005;
	
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
		
		////////////////////// button /////////////////////////
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
				user_location();								
			}
		});
		///////////////////////////////////////////////////////
		
		////////////////////location/////////////////////////
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(true);//공급자가 금전적 비용을 부과할수 있는지 여부.
		criteria.setPowerRequirement(Criteria.POWER_LOW);
        mygpsprovider = locationManager.getBestProvider(criteria, true);
		
		locationListener = new LocationListener(){
			public void onLocationChanged(Location location){ 
				mylati = location.getLatitude();
				mylongti = location.getLongitude();
				if(first_parsing == false)
				{	
					first_parsing = true;
					load_data();
					show_mark();
					lowlati = mylati - range;
					highlati = mylati + range;
					lowlongti = mylongti - range;
					highlongti = mylongti + range;
				}
			}
			public void onStatusChanged(String provider, int status, Bundle extras){}
			public void onProviderEnabled(String provider){}
			public void onProviderDisabled(String provider){}
		};	
		locationManager.requestLocationUpdates(mygpsprovider, 0, 0, locationListener);		
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);		 
	}
	//////////////////////////////////////////////////////////////////////////////////
	
	
	
	/////////////////////////////////////user_function///////////////////////////////
	public void user_location(){
		mapView.setMapCenterPointAndZoomLevel
		(MapPoint.mapPointWithGeoCoord(mylati,mylongti), 1, true);
	}
	
	public void load_data(){
		data = new ArrayList<Data>();
		Data p_data ;		
		/* 이런식으로 데이터 가져와서 붙여넣으면 될듯!!
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
	///////////////////////////////////////////////////////////////////
	
	///////////////////MapView.MapViewEventListener///////////////////
	@Override
	public void onMapViewCenterPointMoved(MapView arg0, MapPoint arg1) {
		if(first_parsing == true){
			double maplati = arg1.getMapPointGeoCoord().latitude;
			double maplongti = arg1.getMapPointGeoCoord().longitude;
			if(maplati < lowlati || maplati > highlati || maplongti <lowlongti || maplongti >highlongti){
				mapView.removeAllPOIItems();
				load_data();
				show_mark();		
				lowlati = maplati - range;
				highlati = maplati + range;
				lowlongti = maplongti - range;
				highlongti = maplongti + range;
			}
		}
	}
	@Override
	public void onMapViewDoubleTapped(MapView arg0, MapPoint arg1) {}
	@Override
	public void onMapViewInitialized(MapView arg0) {}
	@Override
	public void onMapViewLongPressed(MapView arg0, MapPoint arg1) {}
	@Override
	public void onMapViewSingleTapped(MapView arg0, MapPoint arg1) {}
	@Override
	public void onMapViewZoomLevelChanged(MapView arg0, int arg1) {}
	///////////////////////////////////////////////////////////
	
	/////////////////////POIItemEventListener///////////////////
	@Override
	public void onCalloutBalloonOfPOIItemTouched(MapView arg0, MapPOIItem arg1) {
		Uri uri = Uri.parse(data.get(arg1.getTag()).geturl());
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
	@Override
	public void onDraggablePOIItemMoved(MapView arg0, MapPOIItem arg1, MapPoint arg2) {}
	@Override
	public void onPOIItemSelected(MapView arg0, MapPOIItem arg1) {}
	///////////////////////////////////////////////////////////
	
}
