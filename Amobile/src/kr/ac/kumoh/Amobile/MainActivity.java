package kr.ac.kumoh.Amobile;

import java.util.List;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements
// MapView.OpenAPIKeyAuthenticationResultListener,
		MapView.MapViewEventListener
// ,MapView.CurrentLocationEventListener,
// MapView.POIItemEventListener
{

	private MapView mapView;
	LocationManager manager; 
	
	double latitude, longitude;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		///////////////////다음 맵 api 사용////////////////////////////////////////////
		mapView = (MapView) findViewById(R.id.daumMapView);
		mapView.setDaumMapApiKey("f401d2bb5e8f15928c99c68e0a71f43ea29fb2e0");
		mapView.setMapViewEventListener(this);
		// mapView.setOpenAPIKeyAuthenticationResultListener(this);
		// mapView.setCurrentLocationEventListener(this);
		// mapView.setPOIItemEventListener(this);
		mapView.setMapType(MapView.MapType.Standard);

		Button tolist = (Button) findViewById(R.id.Tolist);
		tolist.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						ListActivity.class);
				startActivity(intent);
			}
		});
		/////////////////////////////////////////////////////////////////////////
		
		
		//1. 새로고침 -> 파싱만 새로 하는 기능
		//2. 자기 위치로 이동하기 기능 기능 추가 해야됨

		
		/////////////////////gps 정보 가져오기/////////////////////////////////////////
		manager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
	
		LocationListener locationListener = new LocationListener() {
		    @Override
		    public void onStatusChanged(String provider, int status, Bundle extras) {
		    }
		     
		    @Override
		    public void onProviderEnabled(String provider) {
		    }
		     
		    @Override
		    public void onProviderDisabled(String provider) {
		    }
		     
		    @Override
		    public void onLocationChanged(Location location) {
		        latitude=location.getLatitude();
		        longitude=location.getLongitude();
		    	
		    }
		};
		
		manager.requestLocationUpdates(manager.GPS_PROVIDER, 0, 0, locationListener);
		
		
		
	
		
	}

	@Override
	public void onMapViewCenterPointMoved(MapView arg0, MapPoint arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapViewDoubleTapped(MapView arg0, MapPoint arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapViewInitialized(MapView arg0) {
		// TODO Auto-generated method stub
		mapView.setMapCenterPointAndZoomLevel(
				MapPoint.mapPointWithGeoCoord(latitude, longitude), 5, true);
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
	

}
