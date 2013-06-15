package kr.ac.kumoh.Amobile;

import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListActivity extends Activity implements OnItemClickListener {

	private ArrayList<Data> data;
	private MyAdapter adapter;
	private ListView listView;
	
	public static ListActivity listActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		listActivity = this;
		
		setContentView(R.layout.activity_list);

		// ////////////////button////////////////////////
		Button tomap = (Button) findViewById(R.id.Tomap);
		tomap.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ListActivity.this, MainActivity.class); 

				intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT); 

				startActivity(intent); 
				
			}
		});
		// //////////////////////////////////////////////

		// //////////////��Ƽ��Ƽ ������ ��ȯ///////////////////

		Intent intent = getIntent();
		data = intent.getParcelableArrayListExtra("data");
		
		Log.d("1",data.get(0).getname());
		Log.d("1",data.get(0).gethref());
		Log.d("1",data.get(0).getimg());
		Log.d("1",data.get(0).getprice1());
		Log.d("1",data.get(0).getprice2());
/*
		for (int i = 0; i < 5; i++) {
			Toast.makeText(this, data.get(i).getname(), Toast.LENGTH_SHORT)
					.show();
			Toast.makeText(this, data.get(i).gethref(), Toast.LENGTH_SHORT)
					.show();
			Toast.makeText(this, data.get(i).getimg(), Toast.LENGTH_SHORT)
					.show();
			Toast.makeText(this, data.get(i).getprice1(), Toast.LENGTH_SHORT)
					.show();
			Toast.makeText(this, data.get(i).getprice2(), Toast.LENGTH_SHORT)
					.show();
		}
*/
		 listView = (ListView)findViewById(R.id.ListView1);
		 adapter = new MyAdapter(this, R.layout.datalist, data);
		 listView.setAdapter(adapter);
		 listView.setOnItemClickListener(this);
		

	}
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String href = data.get(position).gethref();
		Uri urihref = Uri.parse(href);
		Intent intent = new Intent(Intent.ACTION_VIEW, urihref);
		startActivity(intent);
	}
	
}