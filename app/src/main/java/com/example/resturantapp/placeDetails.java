package com.example.resturantapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class placeDetails extends AppCompatActivity{
    private ImageView imageView;
    private TextView Resname;
    private Integer mIndex;

    Button menu, map, more;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_details);
        menu=(Button) findViewById(R.id.menu) ;
        map=(Button) findViewById((R.id.map));
        more=(Button) findViewById(R.id.moreInfo);
        TextView placeName=(TextView) findViewById(R.id.placeName);
        imageView=(ImageView) findViewById(R.id.placeImg);

        Intent intent=getIntent();
        ParsableDataStructure parsableDataStructure = intent.getParcelableExtra("values");


     assert parsableDataStructure != null;
     Toast.makeText(this,"Not pass",Toast.LENGTH_SHORT).show();
     Glide.with(this)
             .asBitmap().error(R.drawable.error)
             .load(parsableDataStructure.getPlaceImage())
             .diskCacheStrategy(DiskCacheStrategy.ALL)
             .into(imageView);




            assert parsableDataStructure != null;
        placeName.setText(parsableDataStructure.getName());


        menu.setEnabled(false);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  Intent intent=new Intent(placeDetails.this,menu_like.class);
                intent.putExtra("values",(Parcelable) parsableDataStructure);
                 startActivity(intent);
            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(placeDetails.this,RestaurantMap.class);
                intent.putExtra("values",(Parcelable) parsableDataStructure);

               startActivity(intent);
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent=new Intent(placeDetails.this,more_place_info.class);
                intent.putExtra("values",(Parcelable) parsableDataStructure);
                   startActivity(intent);
            }
        });

    }
}


