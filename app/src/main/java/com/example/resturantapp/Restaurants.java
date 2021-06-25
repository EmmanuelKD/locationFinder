package com.example.resturantapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.libraries.places.api.model.PhotoMetadata;

public class Restaurants implements Parcelable {

    private String name;
    private double longitude;
    private double Latitude;
    private int rating;
    private boolean isActive;
    private String ID;
    private PhotoMetadata photoMetadata;
    private byte[] photoMetadatainBite;


    protected Restaurants(Parcel in) {
        name = in.readString();
        longitude = in.readDouble();
        Latitude = in.readDouble();
        rating = in.readInt();
        isActive = in.readByte() != 0;
        ID = in.readString();
        photoMetadata = in.readParcelable(PhotoMetadata.class.getClassLoader());
        photoMetadatainBite = in.createByteArray();
    }

    public static final Creator<Restaurants> CREATOR = new Creator<Restaurants>() {
        @Override
        public Restaurants createFromParcel(Parcel in) {
            return new Restaurants(in);
        }

        @Override
        public Restaurants[] newArray(int size) {
            return new Restaurants[size];
        }
    };

    public PhotoMetadata getPhotoMetadata() {
        return photoMetadata;
    }

    public void setPhotoMetadata(PhotoMetadata photoMetadata) {
        this.photoMetadata = photoMetadata;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Restaurants(String name , double longitude , double latitude , int rating , boolean isActive, String Id , PhotoMetadata photoMetadata) {
        this.name = name;
        this.longitude = longitude;
        Latitude = latitude;
        this.rating = rating;
        this.isActive = isActive;
       this.ID=Id;
       this.photoMetadata=photoMetadata;
    }

    public Restaurants(String name , double longitude , double latitude , int rating , boolean isActive, String Id ) {
        this.name = name;
        this.longitude = longitude;
        Latitude = latitude;
        this.rating = rating;
        this.isActive = isActive;
        this.ID=Id;
//        this.photoMetadatainBite=photoMetadata;
    }

    public Restaurants() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest , int flags) {
        dest.writeString(name);
        dest.writeDouble(longitude);
        dest.writeDouble(Latitude);
        dest.writeInt(rating);
        dest.writeByte((byte) (isActive ? 1 : 0));
        dest.writeString(ID);
        dest.writeParcelable(photoMetadata , flags);
        dest.writeByteArray(photoMetadatainBite);
    }
}
