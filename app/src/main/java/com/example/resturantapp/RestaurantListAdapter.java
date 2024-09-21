package com.example.resturantapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;

import java.util.ArrayList;
import java.util.Objects;

class RestaurantListAdapter extends ArrayAdapter<Restaurants> {

private Context context;
int Resource;
LayoutInflater Inflater;
DataBaseDatahandler dataBaseDatahandler;
    ImageView imageView;
    String id;
    int position;
    public RestaurantListAdapter(@NonNull Context context , int resource , @NonNull ArrayList<Restaurants> objects) {
        super(context , resource , objects);
        Resource=resource;
         Inflater=LayoutInflater.from(context);
        this.context=context;
        dataBaseDatahandler=new DataBaseDatahandler(context);

    }

    @NonNull
    @Override
    public View getView(int position , @Nullable View convertView , @NonNull ViewGroup parent) {
        this.position=position;
        String name= Objects.requireNonNull(getItem(position)).getName();
//        double longitude= Objects.requireNonNull(getItem(position)).getLongitude();
//        double latitude= Objects.requireNonNull(getItem(position)).getLatitude();
//        int rating= Objects.requireNonNull(getItem(position)).getRating();
        boolean isActive= Objects.requireNonNull(getItem(position)).isActive();
        id=Objects.requireNonNull(getItem(position)).getID();

//        RequestForPhoto(id);

        convertView=Inflater.inflate(Resource, parent,false); // more to be done to have a dynamic scrolling

        TextView textView=(TextView) convertView.findViewById(R.id.restaurant_name);
        ImageView online_offline=(ImageView) convertView.findViewById(R.id.online_offline);
        imageView=(ImageView)  convertView.findViewById(R.id.res_img);




        RequestForPhoto2(Objects.requireNonNull(getItem(position)).getPhotoMetadata());
    //        RequestForPhoto(Objects.requireNonNull(getItem(position)).getID(),position);


        if(!isActive) {
            online_offline.setImageResource(R.drawable.ic_off);
        }
        textView.setText(name);



return convertView;
    }




//    private void RequestForPhoto(String placeId,int position) {
//        FetchPlaceRequest request= FetchPlaceRequest.builder(placeId, Collections.singletonList(Place.Field.PHOTO_METADATAS)).build();
//
//        my_mainActivity.placesClient.fetchPlace(request)
//                .addOnSuccessListener(fetchPlaceResponse -> {
//                    Place place=fetchPlaceResponse.getPlace();
//
//                    System.out.println("   === \n ======place data"+place.getPhotoMetadatas()+"=== \n ======"+ Objects.requireNonNull(getItem(position)).getPhotoMetadata()+"===== \n ====== "+placeId+"=yes==== \n =======yes== \n ===");
//                    if(place.getPhotoMetadatas()==null){
//                        imageView.setImageResource(R.drawable.demo);
//                        return;
//                    }
//                    PhotoMetadata photoMetadata= place.getPhotoMetadatas().get(0);
//
//                    FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata).build();
//                    my_mainActivity.placesClient.fetchPhoto(photoRequest)
//                            .addOnSuccessListener( fetchPhotoResponse -> {
//                                Bitmap bitmap=fetchPhotoResponse.getBitmap();
//                                RequestAndRespondsHolder.setPHOTO(bitmap);
//                                    imageView.setImageBitmap(bitmap);
//                            }).addOnFailureListener(e -> System.out.println("Fail to load........................"));
//
//                });
//
//
//    }

    private void RequestForPhoto2(PhotoMetadata data) {
        if(data==null){
//            imageView.setImageResource(R.drawable.demo);
            System.out.println("==================================this is fetch photo======>"+data);
            return;
        }
        FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(data).build();
        System.out.println("==================================this is fetch photo======>"+photoRequest);
        my_mainActivity.placesClient.fetchPhoto(photoRequest)
                .addOnSuccessListener(new OnSuccessListener<FetchPhotoResponse>() {
                    @Override
                    public void onSuccess(FetchPhotoResponse fetchPhotoResponse) {
                        Bitmap bitmap=fetchPhotoResponse.getBitmap();
//                        RequestAndRespondsHolder.setPHOTOBitmap(bitmap);
                        imageView.setImageBitmap(bitmap);

//                        dataBaseDatahandler.insertImage(bitmap, id);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imageView.setImageResource(R.drawable.demo);
                System.out.println("Fail to load........................");
            }
        });



    }



}

