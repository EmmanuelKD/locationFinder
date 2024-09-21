package com.example.resturantapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

public class DataBaseDatahandler  extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "PlaceData.db";
    public static final String TABLE_NAME = "PlaceDataTable";


    //    public static final String COL_1_ID = "ID";
    public static final String COL_2_ADDRESS = "ADDRESS";
    public static final String COL_3_LAT = "LAT";
    public static final String COL_4_LNG = "LNG";
    public static final String COL_5_PHOTO_METADATAS = "PHOTO_METADATAS";
    public static final String COL_6_RATINGS_TOTAL="RATINGS_TOTAL";
    public static final String COL_7_PRICE_LEVEL = "PRICE_LEVEL";
    public static final String COL_8_TYPES = "PLACE_TYPES";
    public static final String COL_9_PLACE_NAME = "PLACE_NAME";
    public static final String COL_10_PLACE_ID = "PLACE_ID";



    private ByteArrayOutputStream objectBiteArrayOutputStream;
    private byte[] imageInBytes;


    public DataBaseDatahandler(@Nullable Context context) {
        super(context , DATABASE_NAME , null , 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("   === \n ==db created======= \n ====db created======= \n =========== \n ========= \n ===");

        db.execSQL("create table " + TABLE_NAME + "(" +
                "ADDRESS STRING,LAT DOUBLE,LNG DOUBLE, " +
                "PHOTO_METADATAS BLOB,RATINGS_TOTAL INTEGER," +
                "PRICE_LEVEL INTEGER, PLACE_TYPES TEXT, " +
                "PLACE_NAME TEXT ,PLACE_ID STRING primary key)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db , int oldVersion , int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // ,
    public boolean insertImage(Bitmap COL_5_PHOTO_METADATAS_in,String id){
        Bitmap getImage;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues objectContentValues = new ContentValues();

        try {
            getImage = COL_5_PHOTO_METADATAS_in;
            objectBiteArrayOutputStream = new ByteArrayOutputStream();
            getImage.compress(Bitmap.CompressFormat.JPEG , 100 , objectBiteArrayOutputStream);
            imageInBytes = objectBiteArrayOutputStream.toByteArray();

            objectContentValues.put(COL_5_PHOTO_METADATAS,imageInBytes);
//            String Query="update "+TABLE_NAME+" set "+COL_5_PHOTO_METADATAS+"="+(imageInBytes)+" where "+COL_10_PLACE_ID+" ="+id;
//            db.rawQuery(Query,null);

        } catch (Exception e) {
            System.out.println("fail " + e);
        }
        long result= db.insert(TABLE_NAME,null,objectContentValues);

        if(result==-1){
            System.out.println("   ahar=== \n ====img===== \n ====img======= \n ====img======= \n ========= \n ===");
            return false;
        }else{
            System.out.println("   shar=== \n ====no===== \n =====no====== \n ===no======== \n ====no===== \n ===");
            return true;
        }


    }



    public boolean insertData(String COL_10_PLACE_ID_in , String COL_2_ADDRESS_in , double COL_3_LAT_in , double COL_4_LNG_in
            , int COL_6_USER_RATINGS_TOTAL_in , int COL_7_PRICE_LEVEL_in , String COL_8_TYPES_in , String COL_9_PLACE_NAME_in) {

        System.out.println("   === \n ==inserting======= \n ====inserting======= \n ======inserting===== \n ====inserting===== \n ===");

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues objectContentValues = new ContentValues();

        objectContentValues.put(COL_2_ADDRESS,COL_2_ADDRESS_in);
        objectContentValues.put(COL_3_LAT,COL_3_LAT_in);
        objectContentValues.put(COL_4_LNG,COL_4_LNG_in);
        objectContentValues.put(COL_6_RATINGS_TOTAL,COL_6_USER_RATINGS_TOTAL_in);
        objectContentValues.put(COL_7_PRICE_LEVEL,COL_7_PRICE_LEVEL_in);
        objectContentValues.put(COL_8_TYPES,COL_8_TYPES_in);
        objectContentValues.put(COL_9_PLACE_NAME,COL_9_PLACE_NAME_in);
        objectContentValues.put(COL_10_PLACE_ID,COL_10_PLACE_ID_in);

        long result= db.insert(TABLE_NAME,null,objectContentValues);

        if(result==-1){
            System.out.println("   ahar=== \n ========= \n =========== \n =========== \n ========= \n ===");
            return false;
        }else{
            System.out.println("   shar=== \n ====no===== \n =====no====== \n ===no======== \n ====no===== \n ===");
            return true;
        }

    }


    public Cursor getAllData(){
        SQLiteDatabase db =this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from "+ TABLE_NAME ,null);

        return res;
    }

//    public void deleteTable(){
//        SQLiteDatabase db =this.getWritableDatabase();
//        Cursor res = db.rawQuery("select * from "+ TABLE_NAME ,null);
//    }
}
