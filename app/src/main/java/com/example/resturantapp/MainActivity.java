package com.example.resturantapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST =9001;
    private static double radius=5000;// 1 meter equiv
    public static final String THEME_RES_ID_EXTRA = "widget_theme";
    ListView listView;
    // Create a new Places client instance
//    PlacesClient placesClient;
    RestaurantMap restaurantMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView=(ListView) findViewById(R.id.list_View);

        RestaurantListAdapter adapter=new RestaurantListAdapter(this,R.layout.restaurent_item,my_mainActivity.getRestaurants());
            listView.setAdapter(adapter);
//  GetListOfRestaurants();// after fetching data from
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent , View view , int position , long id) {
                Toast.makeText(MainActivity.this," "+ position +" id "+ id +" ", Toast.LENGTH_SHORT).show();
            }
        });


        hideKeyboard();
        if(isServicesOK()){
            init();
        }
        restaurantMap=new RestaurantMap();
//        if (!Places.isInitialized()) {
//            Places.initialize(getApplicationContext(), getString(R.string.api_key), Locale.UK);
//            placesClient = Places.createClient(this);
//        }
//        placesClient = Places.createClient(this);

    }

    private void init(){
        Button buttonMap=(Button) findViewById(R.id.map_button);
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"intent new intent");
                Intent intent=new Intent(MainActivity.this,RestaurantMap.class);
                startActivity(intent);
            }
        });



    }
    public boolean isServicesOK(){

        int available= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if(available== ConnectionResult.SUCCESS){
//every thing works well Google play service is available
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
// if true an error occurs but can be fixed
            Dialog dialog=GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,available,ERROR_DIALOG_REQUEST);
            dialog.show(); // this shows the dialog that there is an error and google will provide ways to resolve it
        }else{
// finally if non of the above
            Toast.makeText(this,"you can't make map request", Toast.LENGTH_SHORT).show();// this is a small popup that will show this message
        }
        return false;
    }


    private void hideKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


}


