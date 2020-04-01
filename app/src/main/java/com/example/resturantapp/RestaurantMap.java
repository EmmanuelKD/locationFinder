package com.example.resturantapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static android.provider.SettingsSlicesContract.KEY_LOCATION;


public class RestaurantMap extends AppCompatActivity implements OnMapReadyCallback {

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private Location mLastKnownLocation;
    private GoogleMap mMap;
    public static final String TAG="MapActivity";
    private static final String Fine_Location=Manifest.permission.ACCESS_FINE_LOCATION; // getting the location permission
    public static final String Coarse_Location=Manifest.permission.ACCESS_COARSE_LOCATION;
    // getting the location permissionprivate // check if the permissions are all true
    public static final int LOCATION_PERMISSION_REQUEST_CODE=1234;
    private boolean mLocationPermissionGranted=false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    // even though i have written them in the AndroidManifest it needs to be checked once more if the y are present
   private static final float DEFAULT_ZOOM=15f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturant_map);

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


//        updateLocationUi(); this will likely not be implimented
        if(mLocationPermissionGranted){
            gettingDeviceLocation();
            mMap.setMyLocationEnabled((true));
        }
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34 , 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void getLocationPermission(){
        Log.d(TAG,"getLocationPermission(): getting permission");
        String [] permission={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),Fine_Location)== PackageManager.PERMISSION_GRANTED){

            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),Coarse_Location)== PackageManager.PERMISSION_GRANTED){
             mLocationPermissionGranted=true;
            initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permission,
                        LOCATION_PERMISSION_REQUEST_CODE);}
        }else{
            ActivityCompat.requestPermissions(this,
                    permission,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode , @NonNull String[] permissions , @NonNull int[] grantResults) {
        Log.d(TAG,"OnRequestPermissionResult(); requesting For Permission");
//        mLocationPermissionGranted=false;
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
    private void gettingDeviceLocation() {
//        Log.d(TAG , "getDeviceLocation(): getting Device Location");


        try{
            if(mLocationPermissionGranted){

                mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
                 Task location=mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(this,new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG,"onComplete: current location found !");

                            Toast.makeText(RestaurantMap.this,"location found", Toast.LENGTH_SHORT).show();
                            mLastKnownLocation=(Location)task.getResult(); // there was a bug here i look change the scope of the variable

                            assert mLastKnownLocation != null;
                            LatLng mLatlong=new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
                            moveCamera(mLatlong, DEFAULT_ZOOM);
                        }else{
                            Log.d(TAG,"onComplete: current location is null !");
                            Toast.makeText(RestaurantMap.this,"unable to get location", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        }catch(SecurityException e){
Log.d(TAG,"getDeviceLocation: SecurityException: "+ e.getMessage());
        }
    }
private void updateLocationUi(){

        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }

}

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }


    private void moveCamera(LatLng latlng,float zoom){
        Log.d(TAG,"moveCamera: moving the camera to: lat"+latlng.latitude+ ",lng "+latlng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom));

    }
}
