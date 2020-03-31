package com.example.resturantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST =9001;


    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
if(isServicesOK()){
    init();
}
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
        Log.d(TAG,"isServicesOK:checking checking google services version");
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
}
