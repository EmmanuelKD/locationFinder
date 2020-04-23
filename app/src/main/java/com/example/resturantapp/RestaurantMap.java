package com.example.resturantapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


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
    private static boolean mLocationPermissionGranted=false;
    private  FusedLocationProviderClient mFusedLocationProviderClient;
    // even though i have written them in the AndroidManifest it needs to be checked once more if the y are present
    private static final float DEFAULT_ZOOM=15f;
    // widgets/ buttons ,cards and all
    private AutoCompleteTextView searchEditText;
    private ImageView gpsLocation;
    int AUTOCOMPLETE_REQUEST_CODE=1;
    private LatLng mLatlong;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturant_map);

//searchEditText=(AutoCompleteTextView) findViewById(R.id.input_search);
        gpsLocation=(ImageView) findViewById(R.id.gpsLocation);

        getLocationPermission();

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key), Locale.US);
        }

    }




    // initializing
    private void init(){
        Log.d(TAG,"init: initializing");


        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        assert autocompleteFragment != null;
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                System.out.println("Place:***********************************************=====================* " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                System.out.println( "An error occurred:**************************************************** " + status);
            }
        });


//        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v , int actionId , KeyEvent event) {
//                if(
//                        actionId== EditorInfo.IME_ACTION_SEARCH
//                        || actionId==EditorInfo.IME_ACTION_DONE
//                        ||event.getAction()== KeyEvent.ACTION_DOWN
//                        || event.getAction()== KeyEvent.KEYCODE_ENTER){// searching method Exe here
//
//                          geoLocate();
//                }
//             return false;
//            }
//        });

        gpsLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingDeviceLocation();
            }
        });
    }
    private void geoLocate(){
        Log.d(TAG,"geoLocate: geoLocateing");

        String searchString=searchEditText.getText().toString();

        Geocoder geocoder=new Geocoder(RestaurantMap.this);
        List<Address> list=new ArrayList<>();
        try{
            list=geocoder.getFromLocationName(searchString,1);
        }catch (IOException e){
            Log.e(TAG,"this is the error: "+ e);
        }

        if(list.size()>0){
            Address address=list.get(0);
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,
                    address.getAddressLine(0));
        }
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
            if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=
                    PackageManager.PERMISSION_GRANTED){

                return;
            }

            mMap.setMyLocationEnabled((true));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            init();
        }

    }

    public void getLocationPermission(){
        Log.d(TAG,"getLocationPermission(): getting permission");
        String [] permission={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(RestaurantMap.this.getApplicationContext(),Fine_Location)== PackageManager.PERMISSION_GRANTED){

            if(ContextCompat.checkSelfPermission(RestaurantMap.this.getApplicationContext(),Coarse_Location)== PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted=true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(RestaurantMap.this,
                        permission,
                        LOCATION_PERMISSION_REQUEST_CODE);}
        }else{
            ActivityCompat.requestPermissions(RestaurantMap.this,
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
                            mLatlong=new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
                            moveCamera(mLatlong, DEFAULT_ZOOM,"my location");
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


    private void moveCamera(LatLng latlng,float zoom, String title){
        Log.d(TAG,"moveCamera: moving the camera to: lat"+latlng.latitude+ ",lng "+latlng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom));


        //    drop a marker

        MarkerOptions options=new MarkerOptions().position(latlng).title(title);
        mMap.addMarker(options);
    }

    // Auto complete section


    @Override
    protected void onActivityResult(int requestCode , int resultCode , @Nullable Intent data) {

        super.onActivityResult(requestCode , resultCode , data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE)
            if (requestCode == RESULT_OK) {
                assert data != null;
                Place place = Autocomplete.getPlaceFromIntent(data);
                System.out.println(place.getName() +"  "+place.getId()+"WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                assert data != null;
                Status status = Autocomplete.getStatusFromIntent(data);

            } else if (requestCode == RESULT_CANCELED) {

            }
    }






}