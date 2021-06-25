package com.example.resturantapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AdapterforResturantMenu  extends ArrayAdapter<Restaurants> {
    LayoutInflater Inflater;
    int Resource;
    Context context;
    public AdapterforResturantMenu(@NonNull Context context , int resource , @NonNull ArrayList<Restaurants> objects) {
        super(context , resource , objects);
        Resource=resource;
        Inflater=LayoutInflater.from(context);
        this.context=context;
    }




    @NonNull
    @Override
    public View getView(int position , @Nullable View convertView , @NonNull ViewGroup parent) {


        convertView=Inflater.inflate(Resource, parent,false); // more to be done to have a dynamic scrolling




        return convertView;
    }
}
