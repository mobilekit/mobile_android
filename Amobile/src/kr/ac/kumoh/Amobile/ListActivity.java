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

	private ArrayList<Data> data;
	private MyAdapter adapter;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		// ////////////////button////////////////////////
		Button tomap = (Button) findViewById(R.id.Tomap);
		tomap.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		// //////////////////////////////////////////////

		// //////////////액티비티 데이터 교환///////////////////

		Intent intent = getIntent();
		data = intent.getParcelableArrayListExtra("data");
		listView = (ListView) findViewById(R.id.ListView1);
		adapter = new MyAdapter(this, R.layout.datalist, data);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

		// TODO Auto-generated method stub
		String href = data.get(position).gethref();
		Uri urihref = Uri.parse(href);
		Intent intent = new Intent(Intent.ACTION_VIEW, urihref);
		startActivity(intent);
	}
}