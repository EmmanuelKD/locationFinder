package com.example.resturantapp;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;


public class RestaurantMap extends AppCompatActivity {

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
    private double lat, lng;
    private String name;
    private Double RestLat;
    private Double RestLong;
private String placeName;
private ImageView direction;

    private MapComponents mapComponents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturant_map);

        ImageView location = (ImageView) findViewById(R.id.location);
        direction=(ImageView) findViewById(R.id.direction);

        gpsLocation=(ImageView) findViewById(R.id.gpsLocation);



        Intent intent=getIntent();
        ParsableDataStructure parsableDataStructure = intent.getParcelableExtra("values");

        lat= parsableDataStructure.getLat();
        lng=parsableDataStructure.getLng();
        name=parsableDataStructure.getName();
        /** map importing starts here */


        SupportMapFragment supportMapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        mapComponents=new MapComponents(mMap,RestaurantMap.this,autocompleteFragment,supportMapFragment);

        mapComponents.initAutoComplete().setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                try{
                    mapComponents.gettingFoundPlaceLocation(place.getLatLng(),place.getName());
                }catch (Exception e){
                    Toast.makeText(RestaurantMap.this,e.toString() ,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                System.out.println("An error occurred:**************************************************** " + status);
            }
        });


        mapComponents.initAutoComplete().setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                lat=place.getLatLng().latitude;
                lng=place.getLatLng().longitude;
                name=place.getName();
                mapComponents.gettingFoundPlaceLocation(place.getLatLng(),place.getName());
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
        /** map importing starts here */

        gpsLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapComponents.gettingDeviceLocation();
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lat=parsableDataStructure.getLat();
                lng=parsableDataStructure.getLng();
                mapComponents.gettingFoundPlaceLocation(new LatLng(parsableDataStructure.getLat(),parsableDataStructure.getLng()),parsableDataStructure.getName());

            }
        });

        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RestaurantMap.this,"Click button",Toast.LENGTH_SHORT).show();

                PopupMenu popupMenu=new PopupMenu(RestaurantMap.this,direction);
                popupMenu.getMenuInflater().inflate(R.menu.direction_by,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.byDriving:

                                 mapComponents.requestDirection(lat,lng,name,"DRIVING");
                                break;

                            case R.id.byWalking:
                                Toast.makeText(RestaurantMap.this,"Foot",Toast.LENGTH_SHORT).show();
                                mapComponents.requestDirection(lat,lng,name,"WALKING");
                                break;

                        }
                        return true;
                    }
                });

popupMenu.show();
/**
 * bicycling
 * walking
 * walking
 * driving
 * */


            }
        });
    }

}