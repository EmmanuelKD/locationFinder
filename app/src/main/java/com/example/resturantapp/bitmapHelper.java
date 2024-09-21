package com.example.resturantapp;

import android.graphics.Bitmap;

public class bitmapHelper {
private Bitmap bitmap=null;

private static final bitmapHelper instance=new bitmapHelper();

    public bitmapHelper() {
    }

    public static bitmapHelper getInstance(){
        return instance;
    }
    public Bitmap getBitmap(){
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap){
         this.bitmap=bitmap;
    }
//
//    public Bitmap loadBitmapFromUrl(String Url){
//        try{
//            URL mURL=new URL(Url);
//            HttpURLConnection connection=(HttpURLConnection)mURL.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//
//            InputStream inputStream=connection.getInputStream();
//            Bitmap mbitmap= BitmapFactory.decodeStream(inputStream);
//            return mbitmap;
//
//        }catch (Exception e){
//        e.printStackTrace();
//        return null;
//        }

//    }
}
