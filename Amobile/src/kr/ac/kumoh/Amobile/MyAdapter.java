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
	//private ArrayList<GetImage> asynclist ;
	private GetImage[] asynclist ;

	public MyAdapter(Context _context, int _layoutId, ArrayList<Data> _Datalist, ListView _listView) {
		super(_context, _layoutId, _Datalist);
		context = _context;
		layoutId = _layoutId;
		datalist = _Datalist;
		listView = _listView;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//asynclist = new ArrayList<GetImage>();
		asynclist = new GetImage[datalist.size()];
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

		stop_async(position);
		if (data.getbmp() == null) {
			GetImage getImage = new GetImage();
			getImage.execute(data);
			asynclist[position] = getImage;
			vh.photo.setImageBitmap(data.getbmp());
		} else {
			vh.photo.setImageBitmap(data.getbmp());
		}

		vh.name.setText(data.getname());
		vh.price1.setText(data.getprice1());
		vh.price2.setText(data.getprice2());

		return convertView;
	}

	private class GetImage extends AsyncTask<Object, String, Void> {

		InputStream is = null;
		Data data = null;

		@Override
		protected Void doInBackground(Object... url) {
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

			return null;
		}

		protected void onPostExecute(Void unused) {
			listView.invalidateViews();
		}
		
	}
	public void alloff_async(){
		/*for(int i = 0 ; i < asynclist.size(); i ++)
		{
			if(asynclist.get(i).getStatus() == AsyncTask.Status.RUNNING)
				asynclist.get(i).cancel(true);	
		}
		*/
		for(int i = 0 ; i < datalist.size(); i ++)
		{
			if(asynclist[i].getStatus() == AsyncTask.Status.RUNNING)
				asynclist[i].cancel(true);
		}
	}

	public void stop_async(int position) {
		int up_posi = position - 4;
		int down_posi = position + 4 ;
		
		if (up_posi >= 0 && asynclist[up_posi] != null)
			if (asynclist[up_posi].getStatus() == AsyncTask.Status.RUNNING)
				asynclist[up_posi].cancel(true);

		if (down_posi < datalist.size() && asynclist[down_posi] != null)
			if (asynclist[down_posi].getStatus() == AsyncTask.Status.RUNNING)
				asynclist[down_posi].cancel(true);
	}

}
