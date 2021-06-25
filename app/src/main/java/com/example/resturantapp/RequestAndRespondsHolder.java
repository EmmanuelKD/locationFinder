package com.example.resturantapp;


import android.graphics.Bitmap;

import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestAndRespondsHolder {


   private final List<Place.Field> requestField=
           Arrays.asList(  Place.Field.ID,
                           Place.Field.ADDRESS,
                           Place.Field.LAT_LNG,
                           Place.Field.PHOTO_METADATAS,
                           Place.Field.USER_RATINGS_TOTAL, // todo user rating should be removed
                           Place.Field.PRICE_LEVEL, //todo price level should be removed
                           Place.Field.TYPES,
                           Place.Field.NAME);

    RequestAndRespondsHolder() {
      NAME = new ArrayList<>();
      TYPES =  new ArrayList<>();
      ID =  new ArrayList<>();
      ADDRESS =  new ArrayList<>();
      LAT =  new ArrayList<>();
      LNG = new ArrayList<>();
      RATING = new ArrayList<>();
      PRICE_LEVEL =  new ArrayList<>();
      PHOTO =  new ArrayList<>();
      m_photoMetadata=new ArrayList<>();
      PHOTO2=new ArrayList<>();
      PlaceTypeInString=new ArrayList<>();
   }


   public  List<String> getNAME() {
      return NAME;
   }

   public  void setNAME(String NAME) {
      this.NAME.add(NAME);
   }

   private  List<String>NAME;
   private  List<List<Place.Type>>TYPES;
   private  List<String>ID;
   private  List<String>ADDRESS;
   private  List<Double>LAT;
   private  List<Double>LNG;
   private  List<Integer>RATING;
   private  List<Integer>PRICE_LEVEL;
   
   private  List<byte[]>PHOTO;
   private  List<Bitmap>PHOTO2;
 
   private  List<PhotoMetadata>  m_photoMetadata;
    private   List<List<String>> PlaceTypeInString;


    public  List<List<String>> getPlaceTypeInString() {
        return PlaceTypeInString;
    }

    public  void setPlaceTypeInString( List<String> placeTypeInString) {
        PlaceTypeInString.add(placeTypeInString);
    }



    public List<PhotoMetadata> getPhotoMetadata() {
      return m_photoMetadata;
   }

   public  void setPhotoMetadata(PhotoMetadata photoMadata) {
      m_photoMetadata.add(photoMadata);
   }
   
   public  List<List<Place.Type>> getTYPES() {
      return TYPES;
   }

   public  void setTYPES(List<Place.Type> TYPES) {
      this.TYPES.add(TYPES);
   }

   public  List<String> getID() {
      return ID;
   }

   public  void setID(String ID) {
      this.ID.add(ID);
   }

   public  List<String> getADDRESS() {
      return ADDRESS;
   }

   public  void setADDRESS(String ADDRESS) {
      this.ADDRESS.add(ADDRESS);
   }

   public  List<Double> getLAT() {
      return LAT;
   }

   public  void setLAT(double LAT) {
       this.LAT.add(LAT);
   }

   public  List<Double> getLNG() {
      return LNG;
   }

   public  void setLNG(double LNG) {
      this.LNG.add(LNG);
   }

   public  List<Integer> getRATING() {
      return RATING;
   }

   public  void setRATING(Integer RATING) {
      this.RATING.add(RATING);
   }



   public   List<Integer> getPRICE_LEVEL() {
      return PRICE_LEVEL;
   }

   public  void setPRICE_LEVEL(Integer PRICE_LEVEL) {
      this.PRICE_LEVEL.add(PRICE_LEVEL);
   }

   public  List<byte[]> getPHOTO() {
      return PHOTO;
   }

   public  void setPHOTOByte(byte[] p) {
      PHOTO.add(p);
   }

   public  void setPHOTOBitmap(Bitmap p) {
      PHOTO2.add(p);
   }

   public  List<Bitmap> getPHOTOBitmap() {
      return PHOTO2;
   }

// this is the request made

   public  List<Place.Field> getRequestField() {
      return requestField;
   }



}