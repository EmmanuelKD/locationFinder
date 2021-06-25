package com.example.resturantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class more_place_info extends AppCompatActivity {
private String PlaceID;
private TextView resturantName,details,telephone_num,openingHours,webUrl;
private ImageView placeImageView ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_place_info);


        Intent intent=getIntent();
        ParsableDataStructure parsableDataStructure = intent.getParcelableExtra("values");


        Bundle bundle=intent.getExtras();



        PlaceID=parsableDataStructure.getPlaceId();


        resturantName=(TextView) findViewById(R.id.resturantName);
        details=(TextView) findViewById(R.id.details);
        telephone_num=(TextView) findViewById(R.id.telephone_num);
        openingHours=(TextView) findViewById(R.id.openingHours);
        webUrl=(TextView) findViewById(R.id.webUrl);
        placeImageView= (ImageView) findViewById(R.id.placeImageView);

        if(bitmapHelper.getInstance().getBitmap()!=null){
            Toast.makeText(this,"image pass",Toast.LENGTH_SHORT).show();
            placeImageView.setImageBitmap(bitmapHelper.getInstance().getBitmap());
        }else{
            assert parsableDataStructure != null;
            Glide.with(this)
                    .asBitmap()
                    .load(parsableDataStructure.getPlaceImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(placeImageView);
        }
        RecyclerView recyclerView= findViewById(R.id.CommentReviews);

        getPlaceMoreDetails getPlaceMoreDetails=new getPlaceMoreDetails(this,recyclerView,resturantName,details,telephone_num,openingHours,webUrl);
        getPlaceMoreDetails.getPlaceMoreDetails(PlaceID);


    }



}
