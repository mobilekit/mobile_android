package kr.ac.kumoh.Amobile;

import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ListActivity extends Activity implements OnItemClickListener {

	private ArrayList<Data> datalist;
	private ListView listView = null;
	private MyAdapter adapter = null;
	public static ListActivity listActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		listActivity = this;
		setContentView(R.layout.activity_list);

		Button tomap = (Button) findViewById(R.id.Tomap);
		tomap.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ListActivity.this,
						MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
		});

		Intent intent = getIntent();
		datalist = intent.getParcelableArrayListExtra("data");

		listView = (ListView) findViewById(R.id.ListView1);
		adapter = new MyAdapter(this, R.layout.datalist, datalist, listView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long id) {

		String href = datalist.get(position).gethref();
		Uri urihref = Uri.parse(href);
		Intent intent = new Intent(Intent.ACTION_VIEW, urihref);
		startActivity(intent);
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		adapter.cancel_async();
	}

}