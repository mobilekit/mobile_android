package kr.ac.kumoh.Amobile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class MyAdapter extends ArrayAdapter<Data> {

	private Context context;
	private int layoutId;
	private ArrayList<Data> datalist;
	private LayoutInflater Inflater;
	private Bitmap imgBitmap = null;
	private Data data;
	private ImageView list_row_photo;

	public MyAdapter(Context _context, int _layoutId, ArrayList<Data> _Datalist) {
		super(_context, _layoutId, _Datalist);
		context = _context;
		layoutId = _layoutId;
		datalist = _Datalist;
		Inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return datalist.size();

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		data = datalist.get(position);

		if (convertView == null)
			convertView = Inflater.inflate(layoutId, parent, false);

		new GetImage().execute(data.getimg()
				+ "?cmd=thumb&width=50&height=50&fit=false");
		list_row_photo = (ImageView) convertView.findViewById(R.id.listimage);

		/*
		 * 
		 * 
		 * 
		 * 3 list_row_photo = (ImageView)
		 * convertView.findViewById(R.id.listimage);
		 * list_row_photo.setImageBitmap(BitmapFactory.decodeResource(this
		 * .getContext().getResources(), R.drawable.ic_launcher));
		 */

		TextView dataname = (TextView) convertView.findViewById(R.id.dataname);
		dataname.setText(data.getname());

		TextView price1 = (TextView) convertView.findViewById(R.id.price1);
		price1.setText(data.getprice1());

		TextView price2 = (TextView) convertView.findViewById(R.id.price2);
		price2.setText(data.getprice2());

		return convertView;
	}

	private class GetImage extends AsyncTask<String, String, Void> {

		InputStream is = null;

		@Override
		protected Void doInBackground(String... url) {
			// TODO Auto-generated method stub

			try {
				HttpURLConnection connection = (HttpURLConnection) new URL(
						url[0]).openConnection();
				connection.connect();
				is = connection.getInputStream();
				imgBitmap = BitmapFactory.decodeStream(is);
				is.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void c) {

			list_row_photo.setImageBitmap(imgBitmap);
		}
	}
}
