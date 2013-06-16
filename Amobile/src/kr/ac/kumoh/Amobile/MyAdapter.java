package kr.ac.kumoh.Amobile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

class MyAdapter extends ArrayAdapter<Data> {

	private Context context;
	private int layoutId;
	private ArrayList<Data> datalist;
	private ListView listView ;
	private LayoutInflater inflater;

	public MyAdapter(Context _context, int _layoutId, ArrayList<Data> _Datalist, ListView _listView) {
		super(_context, _layoutId, _Datalist);
		context = _context;
		layoutId = _layoutId;
		datalist = _Datalist;
		listView = _listView;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		class ViewHolder {
			ImageView photo;
			TextView name;
			TextView price1;
			TextView price2;
		}
		ViewHolder vh = null;

		Data data;
		data = datalist.get(position);

		if (convertView == null) {
			vh = new ViewHolder();
			convertView = inflater.inflate(layoutId, parent, false);
			vh.photo = (ImageView) convertView
					.findViewById(R.id.list_row_photo);
			vh.name = (TextView) convertView.findViewById(R.id.list_row_txt_1);
			vh.price1 = (TextView) convertView
					.findViewById(R.id.list_row_price1);
			vh.price2 = (TextView) convertView
					.findViewById(R.id.list_row_price2);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		if (data.getbmp() == null) {
			new GetImage().execute(data, vh.photo);
			vh.photo.setImageBitmap(data.getbmp());
		} else {
			vh.photo.setImageBitmap(data.getbmp());
		}

		vh.name.setText(data.getname());
		vh.price1.setText(data.getprice1());
		vh.price2.setText(data.getprice2());

		return convertView;
	}

	private class GetImage extends AsyncTask<Object, String, View> {

		InputStream is = null;
		Data data = null;

		@Override
		protected View doInBackground(Object... url) {
			// TODO Auto-generated method stub
			try {
				data = (Data) url[0];
				HttpURLConnection connection = (HttpURLConnection) new URL(
						data.getimg()).openConnection();
				connection.connect();
				is = connection.getInputStream();
				data.setbmp(BitmapFactory.decodeStream(is));
				is.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return (View) url[1];
		}

		protected void onPostExecute(View photo) {
			listView.invalidateViews();
		}
	}
}
