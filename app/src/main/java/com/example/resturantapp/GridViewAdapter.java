package com.example.resturantapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.util.Strings;

import java.util.List;

public class GridViewAdapter extends BaseAdapter {
private List<Strings> imageUrls;
private Context context;

    public GridViewAdapter(List<Strings> imageUrls , Context context) {
        this.imageUrls = imageUrls;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position , View convertView , ViewGroup parent) {
        ImageView imageView=(ImageView) convertView;
        if(imageView==null){
            imageView=new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(350,450));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
                Glide.with(context)
                .asBitmap()
                .load("https://www.google.com/url?sa=i&url=https%3A%2F%2Fhomepages.cae.wisc.edu%2F~ece533%2Fimages%2F&psig=AOvVaw0Zra_YBAa5l50pdVLFzm8b&ust=1590035477199000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCKCz7_jNwekCFQAAAAAdAAAAABAD")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        return null;
    }
}
