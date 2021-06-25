package com.example.resturantapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class getPlaceMoreDetails  extends AsyncTask<Object,String,String> {

    private ReviewAdapter reviewAdapter;
    private List<String> photoUrls;
    String Url;
    Context context;
    HttpURLConnection httpURLConnection=null;
    String data= "";
    InputStream inputStream=null;


    private TextView resturantName,details,telephone_num,openingHours,webUrl;

         private ParsableDataStructure parsableDataStructure;

    RecyclerView recyclerView;


    public getPlaceMoreDetails(Context context, RecyclerView recyclerView,TextView resturantName, TextView details, TextView telephone_num, TextView openingHours, TextView webUrl){

this.recyclerView=recyclerView;
        this.resturantName=resturantName;
        this.details=details;
        this.telephone_num=telephone_num;
        this.openingHours=openingHours;
        this.webUrl=webUrl;

        this.context=context;
        parsableDataStructure=new ParsableDataStructure();

    }




    @Override
    protected String doInBackground(Object... objects) {

        Url=(String) objects[0];

        try{
            URL myUrl = new URL(Url);
            httpURLConnection=(HttpURLConnection) myUrl.openConnection();
            httpURLConnection.connect();

            inputStream=httpURLConnection.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer=new StringBuffer();
            String line="";
            while((line=bufferedReader.readLine())!=null){
                stringBuffer.append(line);
            }
            data=stringBuffer.toString();
            bufferedReader.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
    @Override
    protected void onPostExecute(String s){
        try{

            JSONObject jsonObject=new JSONObject(s);
            System.out.println("====================================\n"+
                    "====================================\n"+
                    "====================================\n"+jsonObject.toString() +" hai its me details");

            JSONObject getResult=jsonObject.getJSONObject("result");



            resturantName.setText(getResult.getString("formatted_address"));
            telephone_num.setText(getResult.getString("formatted_phone_number"));


          boolean isopen=  getResult.getJSONObject("opening_hours").getBoolean("open_now");

          StringBuilder stringBuilder=new StringBuilder();
          for(int i =0 ;i<getResult.getJSONObject("opening_hours").getJSONArray("weekday_text").length();i++){


              stringBuilder.append(getResult.getJSONObject("opening_hours").getJSONArray("weekday_text").getString(i)).append("\n");

          }


            openingHours.setText(stringBuilder.toString());
            webUrl.setText(getResult.getString("url"));
//todo reviews
            List<JSONObject> reviewsObjects = new ArrayList<>();
            for(int i=0;i<getResult.getJSONArray("reviews").length();i++){
                reviewsObjects.add(getResult.getJSONArray("reviews").getJSONObject(i));
            }

            List<String> reviewer=new ArrayList<>();
            List<String> Date=new ArrayList<>();
            List<Double> rating=new ArrayList<>();
            List<String> text=new ArrayList<>();
            List<String> profile_photo_url=new ArrayList<>();
            for( JSONObject reviews : reviewsObjects){

                reviewer.add(reviews.getString("author_name"));
                Date.add(reviews.getString("relative_time_description"));
                rating.add(reviews.getDouble("rating"));
                text.add(reviews.getString("text"));
                profile_photo_url.add(reviews.getString("profile_photo_url"));

            }
            LinearLayoutManager layoutManager=new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);
            recyclerView.setLayoutManager(layoutManager);

            reviewAdapter=new ReviewAdapter(context,profile_photo_url,reviewer,text,rating);
            recyclerView.setAdapter(reviewAdapter);


        } catch (JSONException e) {
            Toast.makeText(context, "check internet connection",Toast.LENGTH_SHORT).show();
            System.out.println("there is an error ===================================================> "+ e);
            e.printStackTrace();
        }
    }
    public void getPlaceMoreDetails(String PlaceID) {

        StringBuilder sb = new StringBuilder();
        Object[] dataTransfer = new Object[1];

        try {

            sb.append("https://maps.googleapis.com/maps/api/place/details/json?")
                    .append("place_id=")
                    .append(PlaceID)
                    .append("&fields=")
                    .append("formatted_address,opening_hours,formatted_phone_number,review,url")
                    .append("&key=")
                    .append("AIzaSyAfSGY6_Z_zj1yyLD_fa9NuI4vVlEgECKI");



            dataTransfer[0] = sb.toString();
            this.execute(dataTransfer);

        } catch (Exception e) {
            System.out.println("=========================================================fail\n===========faail");
            e.printStackTrace();

        }

    }
}
