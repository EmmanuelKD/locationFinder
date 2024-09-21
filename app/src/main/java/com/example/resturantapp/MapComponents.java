package com.example.resturantapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class MapComponents extends Activity implements OnMapReadyCallback  {
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private Location mLastKnownLocation;
    private GoogleMap mMap;
    public static final String TAG = "MapActivity";
    private static final String Fine_Location = Manifest.permission.ACCESS_FINE_LOCATION; // getting the location permission
    public static final String Coarse_Location = Manifest.permission.ACCESS_COARSE_LOCATION;
    // getting the location permissionprivate // check if the permissions are all true
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    // even though i have written them in the AndroidManifest it needs to be checked once more if the y are present
    private static final float DEFAULT_ZOOM = 15f;
//    // widgets/ buttons ,cards and all
//    private AutoCompleteTextView searchEditText;
//    private ImageView gpsLocation;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
   private double mlat=0, mlon=0;
private String mtitle="destination";
    private LatLng mLatlong;

    private Context context;
   private  AutocompleteSupportFragment autocompleteFragment;
   private  SupportMapFragment supportMapFragment;

    Polyline currentPolyline;
    public MapComponents(GoogleMap map , Context context,  AutocompleteSupportFragment autocompleteFragment  ,SupportMapFragment supportMapFragment) {
        this.autocompleteFragment=autocompleteFragment;
        this.context = context;
        this.mMap=map;

        this.supportMapFragment=supportMapFragment;

        getLocationPermission();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(context , "map is ready" , Toast.LENGTH_SHORT).show();
        Log.d(TAG , "OnMapReady(); Map is ready");
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false); // i can set it to true if i want google to add their fancy navigation
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key), Locale.US);
        }

//        updateLocationUi(); this will likely not be implimented
        if (mLocationPermissionGranted) {
            gettingDeviceLocation();
            if (ActivityCompat.checkSelfPermission(context , Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context , Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {

                return;
            }

            mMap.setMyLocationEnabled((true));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

        }
    }

    public AutocompleteSupportFragment initAutoComplete() {
        Log.d(TAG , "init: initializing");

        assert autocompleteFragment != null;
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID , Place.Field.NAME , Place.Field.TYPES,Place.Field.LAT_LNG));

        autocompleteFragment.setCountry("SL");

      return  autocompleteFragment;
    }
        private void geoLocate ( AutoCompleteTextView searchEditText) {
            Log.d(TAG , "geoLocate: geoLocateing");

            String searchString = searchEditText.getText().toString();

            Geocoder geocoder = new Geocoder(context);
            List<Address> list = new ArrayList<>();
            try {
                list = geocoder.getFromLocationName(searchString , 1);
            } catch (IOException e) {
                Log.e(TAG , "this is the error: " + e);
            }

            if (list.size() > 0) {
                Address address = list.get(0);
                moveCamera(new LatLng(address.getLatitude() , address.getLongitude()) , DEFAULT_ZOOM ,
                        address.getAddressLine(0));
            }
        }


        public void getLocationPermission () {
            Log.d(TAG , "getLocationPermission(): getting permission");
            String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.ACCESS_COARSE_LOCATION};
            if (ContextCompat.checkSelfPermission(context.getApplicationContext() , Fine_Location) == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(context.getApplicationContext() , Coarse_Location) == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    initMap();
                } else {
                    ActivityCompat.requestPermissions((Activity) context ,
                            permission ,
                            LOCATION_PERMISSION_REQUEST_CODE);
                }
            } else {
                ActivityCompat.requestPermissions((Activity) context ,
                        permission ,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }

        }


        private void initMap () {
            Log.d(TAG , "InitMap(); Initializing Map");

//            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            supportMapFragment.getMapAsync(this);

        }

        public void gettingDeviceLocation () {
//        Log.d(TAG , "getDeviceLocation(): getting Device Location");


            try {
                if (mLocationPermissionGranted) {

                    mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
                    Task location = mFusedLocationProviderClient.getLastLocation();
                    location.addOnCompleteListener((Activity) context , new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            try {
                                if (task.isSuccessful()) {
                                    Log.d(TAG , "onComplete: current location found !");

                                    Toast.makeText(context , "location found" , Toast.LENGTH_SHORT).show();
                                    mLastKnownLocation = (Location) task.getResult(); // there was a bug here i look change the scope of the variable

                                    assert mLastKnownLocation != null;
                                    mLatlong = new LatLng(mLastKnownLocation.getLatitude() , mLastKnownLocation.getLongitude());
                                    moveCamera(mLatlong , DEFAULT_ZOOM , "my location");
                                } else {
                                    Log.d(TAG , "onComplete: current location is null !");
                                    Toast.makeText(context , "unable to get location" , Toast.LENGTH_SHORT).show();

                                }
                            }catch( Exception e){
                                Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            } catch (SecurityException e) {
                Log.d(TAG , "getDeviceLocation: SecurityException: " + e.getMessage());
            }
        }


        private void updateLocationUi () {

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
            } catch (SecurityException e) {
                Log.e("Exception: %s" , e.getMessage());
            }

        }


        private void moveCamera (LatLng latlng,float zoom, String title){

            try{
                Log.d(TAG , "moveCamera: moving the camera to: lat" + latlng.latitude + ",lng " + latlng.longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng , zoom));


                //    drop a marker

                MarkerOptions options = new MarkerOptions().position(latlng).title(title);
                mMap.addMarker(options);

            }catch (Exception e){
                Toast.makeText(context,"no Lat Lng" ,Toast.LENGTH_SHORT).show();
            }

        }


        void moveCameraToPlaceFound ( double Lat, double Lng, String name){
            moveCamera(new LatLng(Lat , Lng) , DEFAULT_ZOOM ,
                    name);
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


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }


    public void gettingFoundPlaceLocation (LatLng latLng, String name) {
//        Log.d(TAG , "getDeviceLocation(): getting Device Location");


        try {
            if (mLocationPermissionGranted) {

                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener((Activity) context , new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(context , "new location found" , Toast.LENGTH_SHORT).show();

//                            mLatlong = new LatLng(mLastKnownLocation.getLatitude() , mLastKnownLocation.getLongitude());
                            moveCamera(latLng , DEFAULT_ZOOM , name);
                        } else {

                            Toast.makeText(context , "unable to get location" , Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        } catch (SecurityException e) {
            Log.d(TAG , "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    void checkAndClear(){

        mMap.clear();
        if(mLatlong!=null){
            MarkerOptions options = new MarkerOptions().position(mLatlong).title("my Location");
            mMap.addMarker(options);
        }
       if(mlat!=0||mlon!=0){
           MarkerOptions options = new MarkerOptions().position(new LatLng(mlat,mlon)).title(mtitle);
           mMap.addMarker(options);
       }
    }

    /**
     * drawing path start
     * */
    public void requestDirection(double lat , double lng,String name,String RoutBy){
        mlat=lat;
        mlon=lng;
        mtitle=name;
        checkAndClear();
        StringBuilder sb=new StringBuilder();

        Object[] dataTransfer=new Object[4];
//
        try {
//

            sb.append("https://maps.googleapis.com/maps/api/directions/json?")
                    .append("origin=")
                    .append(mLastKnownLocation.getLatitude())
                    .append(",")
                    .append(mLastKnownLocation.getLongitude())
                    .append("&destination=")
                    .append(lat)
                    .append(",")
                    .append(lng)
                    .append("&mode=")
                    .append(RoutBy)
                    .append("&region=sl")
                    .append("&key=")
                    .append("AIzaSyAfSGY6_Z_zj1yyLD_fa9NuI4vVlEgECKI");


            System.out.println("======================================================>this is my lat lng "+mLastKnownLocation.getLatitude()+" "+mLastKnownLocation.getLatitude()+
                    " found lat lng"+lat +" "+lng);

            GetDirectionData getDirectionData=new GetDirectionData(context);
            dataTransfer[0]=mMap;
            dataTransfer[1]=sb.toString();
            dataTransfer[2]=new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
            dataTransfer[3]=new LatLng(lat,lng);
            getDirectionData.execute(dataTransfer);

        }catch (Exception e){
            System.out.println("=========================================================fail\n===========faail");
            e.printStackTrace();
        }





    }

    public void requestDirectionAvoid2(double lat , double lng,String RoutBy){

        StringBuilder sb=new StringBuilder();

        Object[] dataTransfer=new Object[4];
//
        try {
//

            sb.append("https://maps.googleapis.com/maps/api/directions/json?")
                    .append("origin=")
                    .append(mLastKnownLocation.getLatitude())
                    .append(",")
                    .append(mLastKnownLocation.getLongitude())
                    .append("&destination=")
                    .append(lat).append(",").append(lng)
                    .append("&mode=").append(RoutBy)
                    .append("&region=sl")
                    .append("&avoid=highways")
                    .append("&key=")
                    .append("AIzaSyAfSGY6_Z_zj1yyLD_fa9NuI4vVlEgECKI");

//            String url="https://maps.googleapis.com/maps/api/directions/json?origin="+mLastKnownLocation.getLatitude()+","+mLastKnownLocation.getLongitude()+"&destination="+lat+","+lng+"&key=AIzaSyAfSGY6_Z_zj1yyLD_fa9NuI4vVlEgECKI";


//
//                    .append("&alternatives=true");
//                    .append(getString(R.string.api_key));


            GetDirectionData getDirectionData=new GetDirectionData(context);
            dataTransfer[0]=mMap;
            dataTransfer[1]=sb.toString();
//            dataTransfer[1]=url;
            dataTransfer[2]=new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
            dataTransfer[3]=new LatLng(lat,lng);
            getDirectionData.execute(dataTransfer);

        }catch (Exception e){
            System.out.println("=========================================================fail\n===========faail");
            e.printStackTrace();
        }





    }


//    public void requestPopularPlaces(String placeID){
//
//        StringBuilder sb=new StringBuilder();
//String Fields="icon,name,rating,formatted_phone_number";
//        Object[] dataTransfer=new Object[1];
//
//        try {
//
//
//            sb.append("https://maps.googleapis.com/maps/api/place/details/json?")
//                    .append("&fields=")
//                    .append(Fields)
//                    .append("place_id=")
//                    .append(placeID)
//                    .append("&region=sl")
//                    .append("&key=")
//                    .append("AIzaSyAfSGY6_Z_zj1yyLD_fa9NuI4vVlEgECKI");
//
//
//            GetPlaceDetails getPlaceData=new GetPlaceDetails(context);
//            dataTransfer[0]=sb.toString();
//
//            getPlaceData.execute(dataTransfer);
//
//        }catch (Exception e){
//            System.out.println("=========================================================fail\n===========faail");
//            e.printStackTrace();
//        }
//    }










    public LatLng getmLatlong() {
        return mLatlong;
    }

    /**
     * drawing path start
     * */
}