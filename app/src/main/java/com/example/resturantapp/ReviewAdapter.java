package com.example.resturantapp;

import android.content.Context;
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

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewCard> {
    private List<String> ReviewerMeterDater;
    private List<String>ReviewerName;
    private List<String>ReviewTexts;
    private List<Double>ReviewRatings;

    private Context mContext;


    public ReviewAdapter(Context context,List<String> ReviewerMeterDater
            , List<String> ReviewerName , List<String> ReviewTexts
            , List<Double> ReviewRatings){


       this. ReviewerMeterDater = ReviewerMeterDater;
        this.ReviewerName = ReviewerName;
        this.ReviewTexts = ReviewTexts;
        this.ReviewRatings = ReviewRatings;
        mContext=context;
    }

    @NonNull
    @Override
    public ReviewCard onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.review_adapter,parent,false);
        return new ReviewCard(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewCard holder , int position) {
        Glide.with(mContext)
                .asBitmap()
                .load(ReviewerMeterDater.get(position))
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.mainImage);

        holder.Info.setText(ReviewTexts.get(position));
        holder.name.setText(ReviewerName.get(position));

    }

    @Override
    public int getItemCount() {
        return ReviewerName.size();
    }

    public class ReviewCard extends RecyclerView.ViewHolder{
        ImageView mainImage,RateImage;
        Double Rating;
        TextView name;
        TextView Info;
        CardView cardMain;
        //        ImageView imageView;
        public ReviewCard( View itemView) {
            super(itemView);
            mainImage=itemView.findViewById(R.id.cardPlaceImage2);
            name=itemView.findViewById(R.id.ReviewersName);
            Info=itemView.findViewById(R.id.ReviewText);
//            RateImage=itemView.findViewById(R.id.Rating);
            cardMain=itemView.findViewById(R.id.cardMain2);
//            imageView= itemView.findViewById(R.id.placeImg);
        }


    }

}
