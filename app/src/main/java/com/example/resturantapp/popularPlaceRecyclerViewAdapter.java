package com.example.resturantapp;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class popularPlaceRecyclerViewAdapter extends RecyclerView.Adapter<popularPlaceRecyclerViewAdapter.PlaceCards> {

private List<String> PlaceImageMeterDater;
private List<String>PlaceName;
private List<String>placeLocation;
private List<Double>PlaceRatings;
private List<String>PlaceID;
private List<LatLng> mLatLng;
private List<String> photo_reference;
private Context mContext;

private ParsableDataStructure parsableDataStructure =new ParsableDataStructure();

    public popularPlaceRecyclerViewAdapter(Context context,List<String> placeImageMeterDater
            , List<String> placeName , List<String> placeLocation
            , List<Double> placeRatings,List<String>PlaceID, List<LatLng> mLatLng,List<String> photo_reference) {
        this.photo_reference=photo_reference;
        PlaceImageMeterDater = placeImageMeterDater;
        PlaceName = placeName;
        this.placeLocation = placeLocation;
        PlaceRatings = placeRatings;
        this.mLatLng=mLatLng;
        this.PlaceID=PlaceID;
        mContext=context;

    }


    @NonNull
    @Override
    public PlaceCards onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent,false);
        return new PlaceCards(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PlaceCards holder , int position) {

        Glide.with(mContext)
                .asBitmap().error(R.drawable.error)
                .load(PlaceImageMeterDater.get(position))
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.mainImage);

        holder.name.setText(PlaceName.get(position));
        holder.Rating=PlaceRatings.get(position);
        holder.Info.setText(placeLocation.get(position));


        holder.cardMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,placeDetails.class);


                parsableDataStructure.setPlaceId(PlaceID.get(position));
                parsableDataStructure.setLat(mLatLng.get(position).latitude);
                parsableDataStructure.setLng(mLatLng.get(position).longitude);
                parsableDataStructure.setName(PlaceName.get(position));
                parsableDataStructure.setPhoto_reference(photo_reference.get(position));
                parsableDataStructure.setPlaceImage(PlaceImageMeterDater.get(position));

                intent.putExtra("values", (Parcelable) parsableDataStructure);//todo mmust set opening hours and more details
                mContext.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return PlaceName.size();
    }

    public class PlaceCards extends RecyclerView.ViewHolder {
        ImageView mainImage,RateImage;
        Double Rating;
        TextView name;
        TextView Info;
        CardView cardMain;
//        ImageView imageView;
        public PlaceCards( View itemView) {
            super(itemView);
            mainImage=itemView.findViewById(R.id.cardPlaceImage);
            name=itemView.findViewById(R.id.cardPlaceName);
            Info=itemView.findViewById(R.id.cardPlacedescription);
            RateImage=itemView.findViewById(R.id.ratingImage);
            cardMain=itemView.findViewById(R.id.cardMain);
//            imageView= itemView.findViewById(R.id.placeImg);
        }

    }



}
