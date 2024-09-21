/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.resturantapp;

import android.Manifest.permission;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.Place.Field;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;

/**
 * Activity for testing {@link PlacesClient#findCurrentPlace(FindCurrentPlaceRequest)}.
 */
public class my_mainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

                     /** Declaration start **/

                     private double lat, lng;
                     private String name;
    public static PlacesClient placesClient;
    private TextView responseView;
    private BottomAppBar bar_toggle;
    private DrawerLayout drawerLayout;
    int count = 0;

    private NavigationView navigationView;
    static private boolean isExtraOpen = false;
    boolean isInternetAccessible;
    DataBaseDatahandler dataBaseDatahandler;
    private ImageButton bottom_sheet_button,bottom_sheet_button2,bottom_sheet_button3;
    private ImageView gpsLocation;
    static ArrayList<Restaurants> restaurants = new ArrayList<>();
    private RelativeLayout bottom_sheet_Relative;
    private BottomSheetBehavior bottomSheetBehavior,bottomSheetBehavior2;
    private ImageView location, direction, mPlaceInfo;
    private MapComponents mapComponents;
    private NestedScrollView popularPlaces;
    private GoogleMap mMap;
    private int REQUEST_LOCATION_PERMISSiON=1;
    private RelativeLayout bottom_sheet_Relative2;
    private RequestAndRespondsHolder requestAndRespondsHolder;
    private TextView TotalRestaurant,TotalHotels,TotalCarWash,
            TotalGasStations,TotalHospital,TotalSuperMarket,TotalBanks,TotalAmusementPark,
    TotalPlaceOfPrayer,TotalRandomPlace,TotalBeaches;
private int screenHeight,screenWidth;
           /** Declaration end **/

          private CardView restaurant_card;
    private CardView hotels_card ;
    private CardView carWash_card;
    private  CardView gasStations_card;
    private CardView hospital_card ;
    private CardView superMarkets_card ;
    private CardView RandomPlace_card;
    private  CardView bank_card ;
    private  CardView beaches_card ;
    private CardView placeOfWorship_card;
    private CardView AmusementParks_card ;
    private ParsableDataStructure parsableDataStructure =new ParsableDataStructure();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_main_activity);

        /** get windows height*/

        DisplayMetrics displayMetrics=new DisplayMetrics();
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        if(windowManager!=null){
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);

            screenHeight=displayMetrics.heightPixels; // for me it is 720
            screenWidth=displayMetrics.widthPixels; // width is 1280
        }


        /** Instantiation start **/
        TotalRestaurant=(TextView) findViewById(R.id.TotalRestaurants);
        TotalHotels=(TextView) findViewById(R.id.TotalHotels);
        TotalCarWash=(TextView) findViewById(R.id.TotalCarWash);
        TotalGasStations=(TextView) findViewById(R.id.TotalGasStation);
        TotalHospital=(TextView) findViewById(R.id.TotalHospitals);
        TotalSuperMarket=(TextView) findViewById(R.id.TotalFoodStores);
        TotalBanks=(TextView) findViewById(R.id.TotalBank);
        TotalAmusementPark=(TextView) findViewById(R.id.TotalAmusementPark);
        TotalPlaceOfPrayer=(TextView) findViewById(R.id.TotalPlaceOfWorship);
        TotalRandomPlace=(TextView) findViewById(R.id.TotalRandomPhone);
        TotalBeaches=(TextView) findViewById(R.id.TotalBeaches);

         restaurant_card = (CardView) findViewById(R.id.Restaurant_card);
         hotels_card = (CardView) findViewById(R.id.Hotels_card);
         carWash_card = (CardView) findViewById(R.id.carWash_card);
         gasStations_card = (CardView) findViewById(R.id.gasStations_card);
         hospital_card = (CardView) findViewById(R.id.Hospital_card);
         superMarkets_card = (CardView) findViewById(R.id.superMarkets_card);
         RandomPlace_card = (CardView) findViewById(R.id.RandomPlace_card);
         bank_card = (CardView) findViewById(R.id.bank_card);
         beaches_card = (CardView) findViewById(R.id.beaches_card);
        placeOfWorship_card = (CardView) findViewById(R.id.placeOfWorship_card);
       AmusementParks_card = (CardView) findViewById(R.id.AmusementParks_card);

        requestAndRespondsHolder=new RequestAndRespondsHolder();

        isInternetAccessible = isConnected(this);
        mPlaceInfo = (ImageView) findViewById(R.id.mPlaceInfo);
        location = (ImageView) findViewById(R.id.location);
        direction = (ImageView) findViewById(R.id.direction);

        bottom_sheet_Relative = (RelativeLayout) findViewById(R.id.bottom_sheet_Relative);
        bottom_sheet_button = (ImageButton) findViewById(R.id.bottom_sheet_button); // here bottom sheet
        bottom_sheet_button2 = (ImageButton) findViewById(R.id.bottom_sheet_button2);
        bottom_sheet_button3 = (ImageButton) findViewById(R.id.bottom_sheet_button3);
        bottom_sheet_Relative2=(RelativeLayout) findViewById(R.id.bottom_sheet_Relative2);
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_Relative);
        bottomSheetBehavior2= BottomSheetBehavior.from(bottom_sheet_Relative2);

        drawerLayout = (DrawerLayout) findViewById(R.id.my_drawer);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        bar_toggle = (BottomAppBar) findViewById(R.id.bottom_bar);
        dataBaseDatahandler = new DataBaseDatahandler(my_mainActivity.this);

        /** Use whatever
         * theme was
         * set from the
         * MainActivity.*/
        int theme = getIntent().getIntExtra(MainActivity.THEME_RES_ID_EXTRA , 0);
        if (theme != 0) {
            setTheme(theme);
        }
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext() , getString(R.string.api_key) , Locale.UK);
            placesClient = Places.createClient(this);
        }

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)getSupportFragmentManager()
                .findFragmentById(R.id.autocomplete_fragment);

        mapComponents = new MapComponents(mMap , my_mainActivity.this , autocompleteFragment , supportMapFragment);

        bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_EXPANDED);

        gpsLocation = (ImageView) findViewById(R.id.gpsLocation);

        responseView = findViewById(R.id.response);

        /** Instantiation end **/



        /** create Listeners start **/

        hotels_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetListOfPlace("LODGING");
                Intent intent=new Intent(my_mainActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        carWash_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetListOfPlace("CAR_WASH");
                Intent intent=new Intent(my_mainActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        gasStations_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetListOfPlace("GAS_STATION");
                Intent intent=new Intent(my_mainActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        hospital_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetListOfPlace("HOSPITAL");
                Intent intent=new Intent(my_mainActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        superMarkets_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    GetListOfPlace("SHOPPING_MALL");//"FOOD_STORE" "SHOPPING_MALL" "SUPERMARKET"
                Intent intent=new Intent(my_mainActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        RandomPlace_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetListOfPlace("SCHOOL");//"AIRPORT" "POST_OFFICE" "POLICE" "STADIUM" "UNIVERSITIES"
                Intent intent=new Intent(my_mainActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        bank_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetListOfPlace("BANK");
                Intent intent=new Intent(my_mainActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        beaches_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetListOfPlace("EMBASSY");
                Intent intent=new Intent(my_mainActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        placeOfWorship_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetListOfPlace("PLACE_OF_WORSHIP");//"CHURCH" "PLACE_OF_WORSHIP"

                Intent intent=new Intent(my_mainActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        AmusementParks_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MUSEUM

                GetListOfPlace("AMUSEMENT_PARK");//"ZOO"  "MUSEUM"  "TOURIST_ATTRACTION"
                Intent intent=new Intent(my_mainActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        restaurant_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetListOfPlace("RESTAURANT");
                Intent intent=new Intent(my_mainActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


        bottom_sheet_button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetBehavior2.getState()==BottomSheetBehavior.STATE_COLLAPSED){
                    bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_EXPANDED);

                }else
                    if(bottomSheetBehavior2.getState()==BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        bottom_sheet_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        bottom_sheet_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_HIDDEN){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else  if(bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }

            }
        });
        bottomSheetBehavior2.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet , int newState) {
                switch (newState){
                    case BottomSheetBehavior.STATE_EXPANDED:
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
//                        bottom_sheet_button2.setVisibility(View.INVISIBLE);
//                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                        break;
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet , float slideOffset) {
                bottom_sheet_button3.setRotation(180*slideOffset);
            }
        });
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {

            @Override
            public void onStateChanged(@NonNull View bottomSheet , int newState) {
                switch (newState){


                    case BottomSheetBehavior.STATE_HIDDEN:
                        bottomSheetBehavior2.setPeekHeight((int) (screenHeight-(screenHeight*.83)));
                        bottom_sheet_Relative2.setVisibility(View.VISIBLE);
                        bottom_sheet_button.setVisibility(View.VISIBLE);

                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        bottom_sheet_Relative2.setVisibility(View.INVISIBLE);
                        bottom_sheet_button.setVisibility(View.INVISIBLE);


                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        bottom_sheet_Relative2.setVisibility(View.VISIBLE);
                        bottomSheetBehavior2.setPeekHeight((int) (screenHeight-(screenHeight*.76)));
                        break;
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onSlide(@NonNull View bottomSheet , float slideOffset) {
                bottom_sheet_button2.setRotation(180*slideOffset);

            }
        });

        bar_toggle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.END);

                } else {
                    drawerLayout.openDrawer(GravityCompat.START);

                }
            }
        });




        mapComponents.initAutoComplete().setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                lat=place.getLatLng().latitude;
                lng=place.getLatLng().longitude;
                name=place.getName();
                try{
                    mapComponents.gettingFoundPlaceLocation(place.getLatLng(),place.getName());

                    location.setVisibility(View.VISIBLE);
                    direction.setVisibility(View.VISIBLE);
                    mPlaceInfo.setVisibility(View.VISIBLE);
                    AnimateImage();
                    mPlaceInfo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //todo get data for this info button to parse
//                            parsableDataStructure.setPlaceId();
                            parsableDataStructure.setLat(place.getLatLng().latitude);
                            parsableDataStructure.setLng(place.getLatLng().longitude);
                            parsableDataStructure.setName(place.getName());
//                            parsableDataStructure.setPhoto_reference(place.getPhotoMetadatas().get(0));
//                            parsableDataStructure.setPlaceImage();

//
                            try{


                                Intent intent=new Intent(my_mainActivity.this,placeDetails.class);


                                intent.putExtra("values", (Parcelable) parsableDataStructure);//todo mmust set opening hours and more details
                                startActivity(intent);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    });


                    direction.setOnClickListener((new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PopupMenu popupMenu=new PopupMenu(my_mainActivity.this,direction);
                            popupMenu.getMenuInflater().inflate(R.menu.direction_by,popupMenu.getMenu());
                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()){
                                        case R.id.byDriving:

                                            mapComponents.requestDirection(lat,lng,name,"DRIVING");

                                            break;

                                        case R.id.byWalking:

                                            mapComponents.requestDirection(lat,lat,name,"WALKING");
                                            break;

                                    }
                                    return true;
                                }
                            });

                            popupMenu.show();
                        }
                    }));

                    location.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            mapComponents.gettingFoundPlaceLocation(place.getLatLng(),place.getName());
                        }
                    });

                }catch (Exception e){
                    Toast.makeText(my_mainActivity.this,e.toString() ,Toast.LENGTH_SHORT).show();
                }

            }


            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                System.out.println("An error occurred:**************************************************** " + status);
            }
        });

//        gpsLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mapComponents.gettingDeviceLocation();
//            }
//        });
//
//        direction.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                mapComponents.requestDirection()
//            }
//        });
//
//
//        mPlaceInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        /** create Listeners end **/







        /** implementation start */

        //Todo map component here
        if (!isInternetAccessible) {

            loadDataFromDatabase();
            buildDialog(this).show();

        } else {
            if (isLat_long_the_same()) {

            } else {

                findCurrentPlace();

                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {

                        RecyclerView recyclerView2= findViewById(R.id.recHotels);

                        RecyclerView recyclerView= findViewById(R.id.recRestaurants);
                        GetPlaceSearchDetails getPlaceDetails=new GetPlaceSearchDetails(my_mainActivity.this, recyclerView);
                        getPlaceDetails.getPlaceSearch("Restaurant",8.484444, -13.234444);

                        GetPlaceSearchDetails getPlaceDetails2=new GetPlaceSearchDetails(my_mainActivity.this, recyclerView2);
                        getPlaceDetails2.getPlaceSearch("lodging",8.484444, -13.234444);
                    }
                });

                thread.start();
            }

        }

        location.setVisibility(View.INVISIBLE);
        direction.setVisibility(View.INVISIBLE);
        mPlaceInfo.setVisibility(View.INVISIBLE);
        bottom_sheet_button.setVisibility(View.INVISIBLE);

        // todo this is rung i will like the structure this whole request and responds thing


        /** implementation ends */


    }

    private void AnimateImage() {
        Animation animation = AnimationUtils.loadAnimation(this , R.anim.anim);
        mPlaceInfo.startAnimation(animation);

    }


    public void BackupData() {
        //Todo needs refacturing some data has no need to be retrived and some to be stored

        if (!requestAndRespondsHolder.getTYPES().isEmpty()) {
            for (int i = 0; i < requestAndRespondsHolder.getTYPES().size(); i++) {

                if (dataBaseDatahandler.insertData(
                        requestAndRespondsHolder.getID().get(i) ,
                        requestAndRespondsHolder.getADDRESS().get(i) , requestAndRespondsHolder.getLAT().get(i) ,
                        requestAndRespondsHolder.getLNG().get(i)
                        , requestAndRespondsHolder.getRATING().get(i) != null ? requestAndRespondsHolder.getRATING().get(i) : 0
                        , requestAndRespondsHolder.getPRICE_LEVEL().get(i) != null ? requestAndRespondsHolder.getPRICE_LEVEL().get(i) : 0
                        , requestAndRespondsHolder.getTYPES().get(i).toString() , requestAndRespondsHolder.getNAME().get(i))) {

                    Toast.makeText(my_mainActivity.this , "Data Inserted" , Toast.LENGTH_SHORT).show();
                    System.out.println("   === \n ====no===== \n =====no====== \n ===no======== \n ====no===== \n ===");

                } else {
                    Toast.makeText(my_mainActivity.this , "Datbase Error not Inserted" , Toast.LENGTH_SHORT).show();
                    System.out.println("   === \n ========= \n =========== \n =========== \n ========= \n ===");


                }
            }
        }

    }


    private boolean isLat_long_the_same() {
        return false;
    }

    /**
     * Fetches a list of {@link PlaceLikelihood} instances that represent the Places the user is
     * most
     * likely to be at currently.
     */
    private void findCurrentPlace() {
        if (ContextCompat.checkSelfPermission(this , permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this , permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(
                    my_mainActivity.this ,
                    "Both ACCESS_WIFI_STATE & ACCESS_FINE_LOCATION permissions are required" ,
                    Toast.LENGTH_SHORT)
                    .show();
        }

        // Note that it is not possible to request a normal (non-dangerous) permission from
        // ActivityCompat.requestPermissions(), which is why the checkPermission() only checks if
        // ACCESS_FINE_LOCATION is granted. It is still possible to check whether a normal permission
        // is granted or not using ContextCompat.checkSelfPermission().

        if (checkPermission(ACCESS_FINE_LOCATION)) {
            findCurrentPlaceWithPermissions();
        }else{
            requestWifiPermission();

            if(checkPermission(ACCESS_FINE_LOCATION)){
                findCurrentPlaceWithPermissions();
            }
        }
    }

    void requestWifiPermission(){
ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},REQUEST_LOCATION_PERMISSiON);
    }

    private boolean checkPermission(String permission) {
        boolean hasPermission = ContextCompat.checkSelfPermission(this , permission) == PackageManager.PERMISSION_GRANTED;

        if (!hasPermission) {
            ActivityCompat.requestPermissions(this , new String[]{permission} , 0);
        }
        return hasPermission;
    }

    /**
     * Fetches a list of {@link PlaceLikelihood} instances that represent the Places the user is
     * most
     * likely to be at currently.
     */
    @RequiresPermission(allOf = {ACCESS_FINE_LOCATION , ACCESS_WIFI_STATE})
    private void findCurrentPlaceWithPermissions() {

        setLoading(true);

        FindCurrentPlaceRequest currentPlaceRequest =
                FindCurrentPlaceRequest.newInstance(getPlaceFields());
        Task<FindCurrentPlaceResponse> currentPlaceTask =
                placesClient.findCurrentPlace(currentPlaceRequest);
        currentPlaceTask.addOnSuccessListener(
                (response) -> {                   // filter responces
                    responseView.setText(loadData(response));
                }
                                             );

        currentPlaceTask.addOnFailureListener(
                (exception) -> {
                    exception.printStackTrace();
                    responseView.setText(exception.getMessage());
                });

        currentPlaceTask.addOnCompleteListener(task -> setLoading(false));
    }


    private List<Field> getPlaceFields() { // change made here

        return requestAndRespondsHolder.getRequestField();
    }


    private void setLoading(boolean loading) {
//        findViewById(R.id.loading).bringToFront();
        findViewById(R.id.loading).setVisibility(loading ? View.VISIBLE : View.INVISIBLE);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.find:
                Toast.makeText(getApplicationContext() , "find" , Toast.LENGTH_SHORT).show();
//               bar_toggle.setBackgroundColor(R.color.colorAccent);

                Toast.makeText(getApplicationContext() , "find" , Toast.LENGTH_SHORT).show();
                break;
            case R.id.plan:
//                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                    drawerLayout.closeDrawers();
//                }
                if (!isInternetAccessible) {
                    loadDataFromDatabase();
                    buildDialog(this).show();

                } else {
                    if (isLat_long_the_same()) {

                    } else {
                        setLoading(true);
                        findCurrentPlace();
                    }
                }

                break;
            case R.id.settings:

                Toast.makeText(getApplicationContext() , "settings" , Toast.LENGTH_SHORT).show();
                break;
            case R.id.contact:

                Toast.makeText(getApplicationContext() , "contact" , Toast.LENGTH_SHORT).show();
                break;
            case R.id.discover:

                if (!isExtraOpen) {
                    Toast.makeText(getApplicationContext() , "change plann to enable content" , Toast.LENGTH_SHORT).show();
                } else {
                    count = 1;
                }
                break;
            case R.id.sales:

                if (!isExtraOpen) {
                    Toast.makeText(getApplicationContext() , "change plan to enable content" , Toast.LENGTH_SHORT).show();
                } else {
                    count = 2;
                }
                break;
            case R.id.direction:

                if (!isExtraOpen) {
                    Toast.makeText(getApplicationContext() , "change plan to enable content" , Toast.LENGTH_SHORT).show();
                } else {
                    count = 3;
                }
                break;
            case R.id.more:

                Toast.makeText(getApplicationContext() , "more" , Toast.LENGTH_SHORT).show();
                break;

        }

        return false;
    }


     void GetListOfPlace(String getPlaceType) {
        System.out.println();

        if (requestAndRespondsHolder.getTYPES().isEmpty()) { /** i will in some day use this to check if the location
         is the same you fetch fro a cash
         **/

            if (requestAndRespondsHolder.getPlaceTypeInString().isEmpty()) {

            } else {

                restaurants = new ArrayList<>();
                for (int i = 0; i < requestAndRespondsHolder.getPlaceTypeInString().size(); i++) {
                    if (requestAndRespondsHolder.getPlaceTypeInString().get(i).toString().contains(getPlaceType)) {

                        restaurants.add(new Restaurants(
                                requestAndRespondsHolder.getNAME().get(i) ,
                                requestAndRespondsHolder.getLNG().get(i) ,
                                requestAndRespondsHolder.getLAT().get(i) ,
                                0 , false ,
                                requestAndRespondsHolder.getID().get(i))); // for now  we are getting no images but will work on it
                    }


                }

            }
        } else {

            restaurants = new ArrayList<>();
            for (int i = 0; i < requestAndRespondsHolder.getTYPES().size(); i++) {

                if (requestAndRespondsHolder.getTYPES().get(i).toString().contains(getPlaceType)) {

                    restaurants.add(new Restaurants(
                            requestAndRespondsHolder.getNAME().get(i) ,
                            requestAndRespondsHolder.getLNG().get(i) ,
                            requestAndRespondsHolder.getLAT().get(i) ,
                            0 , false , requestAndRespondsHolder.getID().get(i) ,
                            requestAndRespondsHolder.getPhotoMetadata().get(i)));

                }
            }
        }


    }


    public static ArrayList<Restaurants> getRestaurants() {
        return restaurants;
    }


    void loadDataFromDatabase() {
        Cursor cursorData = dataBaseDatahandler.getAllData();
        System.out.println("================================ " + cursorData.getCount());
        if (cursorData.getCount() == 0) {
            Toast.makeText(my_mainActivity.this , "no data to display" , Toast.LENGTH_LONG).show();
            return;
        }

        while (cursorData.moveToNext()) {
            try {
                System.out.println(cursorData.getString(7));
                System.out.println(stringToListConverter(cursorData.getString(6)));

                requestAndRespondsHolder.setPlaceTypeInString(stringToListConverter(cursorData.getString(6)));
                requestAndRespondsHolder.setNAME(cursorData.getString(7) == null ? "empty" : cursorData.getString(7));
                requestAndRespondsHolder.setLNG(cursorData.getDouble(2));
                requestAndRespondsHolder.setLAT(cursorData.getDouble(1));
                requestAndRespondsHolder.setADDRESS(cursorData.getString(0) == null ? "empty" : cursorData.getString(0));
                requestAndRespondsHolder.setPHOTOByte(cursorData.getBlob(3));
                requestAndRespondsHolder.setPRICE_LEVEL(cursorData.getInt(5));
                requestAndRespondsHolder.setRATING(cursorData.getInt(4));
                requestAndRespondsHolder.setID(cursorData.getString(8) == null ? "empty" : cursorData.getString(8));
//             Toast.makeText(my_mainActivity.this,"inseting to memory",Toast.LENGTH_LONG).show();


            }catch(Exception e){
                dataBaseDatahandler.close();

            }
        }


    }


    String loadData(FindCurrentPlaceResponse response) {
      int  TotalRes = 0, TotalHot = 0,TotalCar = 0,TotalGas = 0,
                    TotalHos = 0,TotalSup = 0, TotalBan = 0,
                    TotalAmus = 0,TotalPlac = 0,TotalRan = 0,
                    TotalBea = 0;

        StringBuilder builder = new StringBuilder();

        builder.append(response.getPlaceLikelihoods().size()).append(" Current Place Results:");
        for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
            System.out.println(placeLikelihood.getPlace().getPhotoMetadatas());
            if (placeLikelihood.getPlace().getPhotoMetadatas() == null) {

                System.out.println("   === \n =====no==== \n =====no====== \n ========no=== \n ========no= \n ===");

                requestAndRespondsHolder.setPhotoMetadata(null);

            } else {
                requestAndRespondsHolder.setPhotoMetadata(placeLikelihood.getPlace().getPhotoMetadatas().get(0));
                System.out.println("   === \n =====photo==== \n =====meterdater====== \n ========photo=== \n ========meterdater= \n === "+placeLikelihood.getPlace().getPhotoMetadatas().get(0));
                System.out.println(placeLikelihood.getPlace().getTypes());
            }



            requestAndRespondsHolder.setTYPES(placeLikelihood.getPlace().getTypes());
            requestAndRespondsHolder.setADDRESS(placeLikelihood.getPlace().getAddress() == null ? "empty" : placeLikelihood.getPlace().getAddress());
            requestAndRespondsHolder.setID(placeLikelihood.getPlace().getId() == null ? "empty" : placeLikelihood.getPlace().getId());
            requestAndRespondsHolder.setNAME(placeLikelihood.getPlace().getName() == null ? "empty" : placeLikelihood.getPlace().getName());
            requestAndRespondsHolder.setLAT(Objects.requireNonNull(placeLikelihood.getPlace().getLatLng()).latitude);
            requestAndRespondsHolder.setLNG(placeLikelihood.getPlace().getLatLng().longitude);
            requestAndRespondsHolder.setRATING(placeLikelihood.getPlace().getUserRatingsTotal());
            requestAndRespondsHolder.setPRICE_LEVEL(placeLikelihood.getPlace().getPriceLevel());

String value= Arrays.toString(placeLikelihood.getPlace().getTypes().toArray());

if(value.contains("RESTAURANT")){

    TotalRes+=1;
}
else if(value.contains("BANK")){

    TotalBan+=1;
}else if(value.contains("CAR_WASH")){

    TotalCar+=1;
}else if(value.contains("GAS_STATION")){

    TotalGas+=1;
}else if(value.contains("HOSPITAL")){

    TotalHos+=1;
}else if(value.contains("AMUSEMENT_PARK")
        ||value.contains("ZOO")
        ||value.contains("MUSEUM")||
        value.contains("TOURIST_ATTRACTION")){

    TotalAmus+=1;
}else if(value.contains("EMBASSY")){

    TotalBea+=1;
}
else if(value.contains("FOOD_STORE")
        ||value.contains("SHOPPING_MALL")
        ||value.contains("SUPERMARKET")){

    TotalSup+=1;
}
else if(value.contains("CHURCH")||
        value.contains("PLACE_OF_WORSHIP")){

    TotalPlac+=1;
}
else if(value.contains("UNIVERSITIES")||
        value.contains("STADIUM")||
        value.contains("POLICE")
||value.contains("POST_OFFICE")
||value.contains("AIRPORT")
||value.contains("SCHOOL")){

    TotalRan+=1;
}
else if(placeLikelihood.getPlace().getTypes().toString().contains("LODGING")){

    TotalHot+=1;
}


            System.out.println("=========Types================>"+placeLikelihood.getPlace().getTypes());
            System.out.println("=======Address==================>"+placeLikelihood.getPlace().getAddress());
            System.out.println("==========Id===============>"+placeLikelihood.getPlace().getId());
            System.out.println("===========Name==============>"+placeLikelihood.getPlace().getName() );
            System.out.println("==========RatingTotal===============>"+placeLikelihood.getPlace().getUserRatingsTotal());
            System.out.println("===========PriceLevel==============>"+placeLikelihood.getPlace().getPriceLevel());
            System.out.println("================Meterdater=========>"+placeLikelihood.getPlace().getPhotoMetadatas());

        }

if(TotalHos>0){
    TotalHotels.setText(String.valueOf(TotalHos));
    hotels_card.setVisibility(View.VISIBLE);
}
        if(TotalCar>0){
            TotalCarWash.setText(String.valueOf(TotalCar));
            carWash_card.setVisibility(View.VISIBLE);
        }if(TotalGas>0){
            TotalGasStations.setText(String.valueOf(TotalGas));
            gasStations_card.setVisibility(View.VISIBLE);
        }if(TotalHot>0){
            TotalHospital.setText(String.valueOf(TotalHot));
            hospital_card.setVisibility(View.VISIBLE);
        }if(TotalSup>0){
            TotalSuperMarket.setText(String.valueOf(TotalSup));
            superMarkets_card.setVisibility(View.VISIBLE);
        }if(TotalBan>0){
            TotalBanks.setText(String.valueOf(TotalBan));
            bank_card.setVisibility(View.VISIBLE);
        }if(TotalAmus>0){
            TotalAmusementPark.setText(String.valueOf(TotalAmus));
            AmusementParks_card.setVisibility(View.VISIBLE);
        }if(TotalPlac>0){
            TotalPlaceOfPrayer.setText(String.valueOf(TotalPlac));
            placeOfWorship_card.setVisibility(View.VISIBLE);
        }if(TotalRan>0){
            TotalRandomPlace.setText(String.valueOf(TotalRan));
            RandomPlace_card.setVisibility(View.VISIBLE);
        }if(TotalBea>0){
            TotalBeaches.setText(String.valueOf(TotalBea));
            beaches_card.setVisibility(View.VISIBLE);
        }if(TotalRes>0){
            TotalRestaurant.setText(String.valueOf(TotalRes));
            restaurant_card.setVisibility(View.VISIBLE);
        }


        BackupData();
        return builder.toString();
    }


    static String stringify(Place place) {
        return place.getName()
                + " ("
                + place.getAddress()
                + ")"
                + " is open now? "
                + place.isOpen(System.currentTimeMillis());
    }


    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else return false;
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to get new updates. Press ok to Exit");

        builder.setPositiveButton("Ok" , new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog , int which) {
                Toast.makeText(my_mainActivity.this , "no data to display" , Toast.LENGTH_LONG).show();
//                finish(); // this will ent the activity or fragment the dialog pup up on
            }
        });

        return builder;
    }

    static List<String> stringToListConverter(String data) {
        System.out.println(data + "===================================");
        List<String> StringList;
        List<String> StringListnew = new ArrayList<>();
        if (data != null) {
            int first = data.indexOf("[");
            int second = data.indexOf("]");
            String sub = data.substring(first + 1 , second);
            sub = sub.replaceAll("\"" , " ");
            StringList = Arrays.asList(sub.split(","));
            for (String get : StringList) {
                System.out.println("get old" + get);
                get = get.trim();
                System.out.println("get new" + get);
                StringListnew.add(get);
            }

            return StringList;
        } else {
            return null;
        }
    }





}
//    @Override
//    public void bottomBarButton(boolean isBottomBarExpanded) {
//
//
//    }
//}
//else{
//        new AlertDialog.Builder(this).setTitle("permission needed")
//        .setMessage("we need your location to find you places").setPositiveButton("ok" ,
//        new DialogInterface.OnClickListener() {
//@Override
//public void onClick(DialogInterface dialog , int which) {
//        requestWifiPermission();
//        }
//        }).setNegativeButton("cancel" , new DialogInterface.OnClickListener() {
//@Override
//public void onClick(DialogInterface dialog , int which) {
//        dialog.cancel();
//        }
//        }).create().show();
//        }
/**
 * the map going here
 * */