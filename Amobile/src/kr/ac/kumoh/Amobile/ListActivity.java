package kr.ac.kumoh.Amobile;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListActivity extends Activity {

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
		

	}
}