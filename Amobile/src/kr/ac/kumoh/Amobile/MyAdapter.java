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
import android.util.*;
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
	//private Bitmap imgBitmap = null;
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
		
		Log.d("adapter", data.getimg());
		
		if (convertView == null)
			convertView = Inflater.inflate(layoutId, parent, false);
			//convertView = Inflater.inflate(layoutId, parent, false);

		//final String uniquePath = data.getimg();
		
		//list_row_photo = (ImageView) convertView.findViewById(R.id.list_row_photo);
		//list_row_photo.setTag(data.getimg());
		
		//new GetImage().execute(data.getimg(),list_row_photo);
		
		new GetImage().execute(data, convertView); 
		

		
		
		/*
		 * 
		 * 
		 * 
		 * 3 list_row_photo = (ImageView)
		 * convertView.findViewById(R.id.listimage);
		 * list_row_photo.setImageBitmap(BitmapFactory.decodeResource(this
		 * .getContext().getResources(), R.drawable.ic_launcher));
		 */

		TextView dataname = (TextView) convertView.findViewById(R.id.list_row_txt_1);
		dataname.setText(data.getname());
		
		Log.d("name", data.getname());
		Log.d("name", data.getname().length() + "");

		TextView price1 = (TextView) convertView.findViewById(R.id.list_row_price1);
		price1.setText(data.getprice1());
		
		Log.d("price1", data.getprice1());

		TextView price2 = (TextView) convertView.findViewById(R.id.list_row_price2);
		price2.setText(data.getprice2());
		
		Log.d("price2", data.getprice2());

		return convertView;
	}

	
	private class GetImage extends AsyncTask<Object, String, View> {

		InputStream is = null;
		Bitmap bmp = null;

		@Override
		protected View doInBackground(Object... url) {
			// TODO Auto-generated method stub
			try {
				HttpURLConnection connection = (HttpURLConnection) new URL(
						((Data) url[0]).getimg()).openConnection();
				connection.connect();
				is = connection.getInputStream();
				bmp = BitmapFactory.decodeStream(is);
				is.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return (View) url[1];
		}

		protected void onPostExecute(View c) {
			list_row_photo = (ImageView) c.findViewById(R.id.list_row_photo);
			list_row_photo.setImageBitmap(bmp);

		}
	}
	
	/*
	private class GetImage extends AsyncTask<Object, String, View> {

		InputStream is = null;
		//
		Bitmap bmp = null;
		

		@Override
		protected ImageView doInBackground(Object... parm) {
			// TODO Auto-generated method stub

			//imagePath = (String) parm[0];
			
			try {
				HttpURLConnection connection = (HttpURLConnection) new URL(
						(String) parm[0]).openConnection();
				connection.connect();
				is = connection.getInputStream();
				//imgBitmap = BitmapFactory.decodeStream(is);
				imgBitmap1 = BitmapFactory.decodeStream(is);
				
				is.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return (ImageView)parm[1];
		}

		protected void onPostExecute(ImageView c) {

			if (((String)c.getTag()).equals(imagePath))
			{ 
				c.setImageBitmap(imgBitmap1);
			}
			
			
			
			//list_row_photo.setImageBitmap(imgBitmap);
		}
	}
	*/
}
