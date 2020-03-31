package com.example.resturantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;


public class RestaurantMap extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static final String TAG="MapActivity";
    private static final String Fine_Location=Manifest.permission.ACCESS_FINE_LOCATION; // getting the location permission
    public static final String Coarse_Location=Manifest.permission.ACCESS_COARSE_LOCATION; // getting the location permissionprivate Boolean mLocationPermissionGranted=false; // check if the permissions are all true
    public static final int LOCATION_PERMISSION_REQUEST_CODE=1234;
    private boolean mLocationPermissionGranted=false;
    // even though i have written them in the AndroidManifest it needs to be checked once more if the y are present

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturant_map);
        Toast.makeText(RestaurantMap.this,"the new Intent",Toast.LENGTH_LONG).show();
        Log.d(TAG,"MapOnCreate();££££££££££££££££££££££££££££££££££££££££££££££££££££££");
        getLocationPermission();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this,"map is ready",Toast.LENGTH_SHORT).show();
        Log.d(TAG,"OnMapReady(); Map is ready");
        mMap = googleMap;

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34 , 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void getLocationPermission(){
        Log.d(TAG,"getLocationPermission(): getting permission");
        String [] permission={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),Fine_Location)== PackageManager.PERMISSION_GRANTED){

            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),Coarse_Location)== PackageManager.PERMISSION_GRANTED)
    mLocationPermissionGranted=true;
        }else{
            ActivityCompat.requestPermissions(this,
                    permission,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode , @NonNull String[] permissions , @NonNull int[] grantResults) {
        Log.d(TAG,"OnRequestPermissionResult(); requesting For Permission");
        mLocationPermissionGranted=false;
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length>0)
                    for(int i=0; i<grantResults.length;i++){
                        if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted=false;
                            Log.d(TAG,"OnRequestPermissionResult();  Permission failed");
                            return;
                        }
                    }
                Log.d(TAG,"OnRequestPermissionResult();  Permission granted");
                mLocationPermissionGranted=true;
                    initMap();
                    // initialize map
            }
        }
    }

    private void initMap() {
        Log.d(TAG,"InitMap(); Initializing Map");

        SupportMapFragment supportMapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync((OnMapReadyCallback) RestaurantMap.this);
    }
}
