package kr.ac.kumoh.Amobile;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Data implements Parcelable {

	private String name, href;
	private String img, price1, price2;
	private Bitmap bmp;
	private double lati, longti;

	public Data() {
	}

	public void setdata(String name, String href, String img, String price1,
			String price2, double lati, double longti) {
		this.name = name;
		this.href = href;
		this.img = img;
		this.price1 = price1;
		this.price2 = price2;
		this.lati = lati;
		this.longti = longti;
		this.bmp = null;
	}

	public void setbmp(Bitmap bmp) {
		this.bmp = bmp;
	}

	public Bitmap getbmp() {
		return bmp;
	}

	public void getdata(String name, String href, String img, String price1,
			String price2, double lati, double longti) {
		name = this.name;
		href = this.href;
		this.img = img;
		this.price1 = price1;
		this.price2 = price2;
		lati = this.lati;
		longti = this.longti;
	}

	public String getname() {
		return name;
	}

	public String gethref() {
		return href;
	}

	public String getimg() {
		return img;
	}

	public String getprice1() {
		return price1;
	}

	public String getprice2() {
		return price2;
	}

	public double getlati() {
		return lati;
	}

	public double getlongti() {
		return longti;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeString(name);
		arg0.writeString(href);
		arg0.writeString(img);
		arg0.writeString(price1);
		arg0.writeString(price2);
	}

	public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
		public Data createFromParcel(Parcel in) {
			return new Data(in);
		}

		@Override
		public Data[] newArray(int size) {
			return new Data[size];
		}

	};

	private Data(Parcel in) {
		name = in.readString();
		href = in.readString();
		img = in.readString();
		price1 = in.readString();
		price2 = in.readString();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
