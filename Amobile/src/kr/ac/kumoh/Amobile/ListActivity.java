package kr.ac.kumoh.Amobile;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ListActivity extends Activity {

	private ArrayList<Data> data;
	
	private ArrayList<String> dataname;
	private ArrayList<String> dataurl;
	//private ArrayAdapter<Data> adapter;
	private ArrayAdapter<String> adapters1;
	private ArrayAdapter<String> adapters2;
	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);		
		//////////////////button////////////////////////
		Button tomap = (Button) findViewById(R.id.Tomap);
		tomap.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		////////////////////////////////////////////////
		
		////////////////액티비티 데이터 교환///////////////////
		Intent intent = getIntent();
		data = intent.getParcelableArrayListExtra("data");
		
		dataname = new ArrayList<String>();
		dataurl = new ArrayList<String>();
		for(int i= 0 ; i < data.size(); i++){
			dataname.add(data.get(i).getname());
			dataurl.add(data.get(i).geturl());
		}
		
		/*
		for(int i= 0 ; i < data.size(); i++)
			Toast.makeText(this, dataname.get(i)
				+" "+ dataurl.get(i), Toast.LENGTH_SHORT).show();
				*/
	
		listView = (ListView)findViewById(R.id.ListView1);
		adapters1 = new ArrayAdapter<String>(this, R.layout.datalist,R.id.dataname, dataname);

		adapters2 = new ArrayAdapter<String>(this, R.layout.datalist,R.id.dataurl, dataurl);
		listView.setAdapter(adapters2);
		
	
	}
}

