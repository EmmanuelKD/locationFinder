package com.example.resturantapp;

import android.os.Parcel;
import android.os.Parcelable;

public class ParsableDataStructure implements Parcelable {

    private String name;
    private double Lat;
    private double Lng;
    private String placeId;
    private String placeImage;
private String photo_reference;

    public String getPhoto_reference() {
        return photo_reference;
    }

    public void setPhoto_reference(String photo_reference) {
        this.photo_reference = photo_reference;
    }

    ParsableDataStructure(){
        photo_reference="";
        name="";
        Lat=.0;
        Lng=.0;
        placeId="";
        placeImage=null;
    }

    protected ParsableDataStructure(Parcel in) {
        name = in.readString();
        Lat = in.readDouble();
        Lng = in.readDouble();
        placeId = in.readString();
        photo_reference=in.readString();
        placeImage=in.readString();
    }

    public static final Creator<ParsableDataStructure> CREATOR = new Creator<ParsableDataStructure>() {
        @Override
        public ParsableDataStructure createFromParcel(Parcel in) {
            return new ParsableDataStructure(in);
        }

        @Override
        public ParsableDataStructure[] newArray(int size) {
            return new ParsableDataStructure[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLng() {
        return Lng;
    }

    public void setLng(double lng) {
        Lng = lng;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceImage() {
        return placeImage;
    }

    public void setPlaceImage(String placeImage) {
        this.placeImage = placeImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest , int flags) {
        dest.writeString(name);
        dest.writeDouble(Lat);
        dest.writeDouble(Lng);
        dest.writeString(placeId);
        dest.writeString(photo_reference);
        dest.writeString(placeImage);
    }
}
