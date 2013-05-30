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
import android.widget.Toast;

public class ListActivity extends Activity {

	private ArrayList<Data> data;
	private ArrayAdapter<Data> adapter;
	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);		

		//액티비티 데이터 교환
		Intent intent = getIntent();
		data = intent.getParcelableArrayListExtra("data");
		
		for(int i= 0 ; i < data.size(); i++)
			Toast.makeText(this, data.get(i).getname() +
					" "+ data.get(i).geturl(), Toast.LENGTH_SHORT).show();
		
	/*
		adapter = new ArrayAdapter<Data>(this, R.layout.datalist, data);
		listView = (ListView)findViewById(R.id.ListView1);
		listView.setAdapter(adapter);
	*/	
			
		Button tomap = (Button) findViewById(R.id.Tomap);
		tomap.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
	}
}
