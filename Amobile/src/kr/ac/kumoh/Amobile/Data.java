package kr.ac.kumoh.Amobile;

import android.os.Parcel;
import android.os.Parcelable;



public class Data implements Parcelable{

	private String name, url;
	//private String img, price1, price2; 꼭 필요한거.
	//private String day, area; !!?
	private double lati, longti;
	
	
	public Data(){}
	
	public void setdata(String name, String url, double lati, double longti){
		this.name =name;
		this.url = url;
		this.lati = lati;
		this.longti = longti;
	}

	public void getdata(String name, String url, double lati, double longti){
		name = this.name;
		url = this.url;
		lati = this.lati;
		longti = this.longti;
	}
	public String getname(){return name;}
	public String geturl(){return url;}
	public double getlati(){return lati;}
	public double getlongti(){return longti;}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeString(name);
		arg0.writeString(url);
		//arg0.writeString(img);
		//arg0.writeString(price1);
		//arg0.writeString(price2);		
	}
	public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data> (){
			public Data createFromParcel(Parcel in){
				return new Data(in);
			}

			@Override
			public Data[] newArray(int size) {
				return new Data[size];
			}
			
		};
	private Data(Parcel in){
		name = in.readString();
		url = in.readString();
		//img = in.readString();
		//price1 = in.readString();
		//price2 = in.readString();
	}

}
