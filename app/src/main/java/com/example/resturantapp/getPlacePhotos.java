package com.example.resturantapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
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
import java.util.Arrays;
import java.util.List;

public class getPlacePhotos extends AsyncTask<Object,String,String> {



    private popularPlaceRecyclerViewAdapter popularPlaceRecyclerViewAdapter;
    private List<String> photoUrls;
    String Url;
    Context context;
    HttpURLConnection httpURLConnection=null;
    String data= "";
    InputStream inputStream=null;

    String type;

    private List<LatLng> longitudeAndLatitude;
    private List<String> Id;
    private List<String> placeName;
    private List<Boolean> isOpen;
    private List<Integer> photoHeight;
    private List<Integer> photoWidth;
    private List<String> placeId;
    private List<Double> rating;
    private List<String> reference;
    private List<String> vicinity;
    private List<String> photoHtml_attributions;
    private List<String> photo_reference;
    private RecyclerView recyclerView;


    public getPlacePhotos(Context context,RecyclerView recyclerView){
        this.recyclerView=recyclerView;
        photoUrls=new ArrayList<>();
        longitudeAndLatitude=new ArrayList<>();
        Id=new ArrayList<>();
        placeName=new ArrayList<>();
        isOpen=new ArrayList<>();
        photoHeight=new ArrayList<>();
        photoWidth=new ArrayList<>();
        placeId=new ArrayList<>();
        rating=new ArrayList<>();
        reference=new ArrayList<>();
        vicinity=new ArrayList<>();
        photoHtml_attributions=new ArrayList<>();
        photo_reference=new ArrayList<>();
        this.context=context;

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
                    "====================================\n"+jsonObject.toString() +" hai its me");

            JSONArray jsonArray=jsonObject.getJSONArray("results");
//Todo take the data ath the end and pass it to the image adapter class and some of it to the Get get Details and Get PhotoClass
            List<JSONObject> jsonObjects = new ArrayList<>();
            for(int i=0;i<jsonArray.length();i++){
                jsonObjects.add(jsonArray.getJSONObject(i));
            }
            assert jsonObjects != null;
            for(JSONObject jData:jsonObjects){


                if(jData.isNull("business_status")){
                    continue;
                }
                if(jData.getString("business_status").equals("OPERATIONAL")

                        &&jData.has("photos") && jData.has("opening_hours")
                        &&!jData.isNull("photos")&&!jData.isNull("opening_hours")
                        &&!jData.isNull("rating")){

                    longitudeAndLatitude.add(new LatLng(jData.getJSONObject("geometry")
                            .getJSONObject("location").getDouble("lat"),jData.getJSONObject("geometry")
                            .getJSONObject("location").getDouble("lng")));

                    placeId.add(jData.getString("id"));
                    placeName.add(jData.getString("name"));

                    isOpen.add(jData.getJSONObject("opening_hours").getBoolean("open_now"));

                    photoHeight.add(jData.getJSONArray("photos").getJSONObject(0).getInt("height"));

                    photoHtml_attributions.add(jData.getJSONArray("photos").getJSONObject(0).getJSONArray("html_attributions").getString(0));
                    photoWidth.add(jData.getJSONArray("photos").getJSONObject(0).getInt("width"));
                    photo_reference.add(jData.getJSONArray("photos").getJSONObject(0).getString("photo_reference"));
                    placeId.add(jData.getString("place_id"));
                    System.out.println("=================here  "+ Arrays.toString(placeId.toArray())+ " name "+Arrays.toString(placeName.toArray()));
                    rating.add(jData.getDouble("rating"));
                    reference.add(jData.getString("reference"));
                    vicinity.add(jData.getString("vicinity"));
                }
            }

            for(int i=0;i<photo_reference.size();i++){
                StringBuilder sb = new StringBuilder();
                sb.append("https://maps.googleapis.com/maps/api/place/photo?")
                        .append("maxwidth=")
                        .append(photoWidth.get(i))
                        .append("&photoreference=")
                        .append(photo_reference.get(i))
                        .append("&key=").append("AIzaSyAfSGY6_Z_zj1yyLD_fa9NuI4vVlEgECKI");

                photoUrls.add(sb.toString());
            }


//     if(type.equals("Restaurant")){
//
//
//
//     }

//            LinearLayoutManager layoutManager=new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);
//            recyclerView.setLayoutManager(layoutManager);
//
//            popularPlaceRecyclerViewAdapter=new popularPlaceRecyclerViewAdapter(context,photoUrls,placeName,vicinity,
//                    rating,placeId,longitudeAndLatitude);
//            recyclerView.setAdapter(popularPlaceRecyclerViewAdapter);

        } catch (JSONException e) {
            Toast.makeText(context, "check internet connection",Toast.LENGTH_SHORT).show();
            System.out.println("there is an error ===================================================> "+ e);
            e.printStackTrace();
        }
    }


    public void getPlaceSearch(String PlaceType, double lat , double lng) {
        type=PlaceType;
        StringBuilder sb = new StringBuilder();
        Object[] dataTransfer = new Object[1];

        try {

            sb.append("https://maps.googleapis.com/maps/api/place/nearbysearch/json?")
                    .append("location=")
                    .append(lat)
                    .append(",")
                    .append(lng)
                    .append("&radius=50000")
                    .append("&type=")
                    .append(PlaceType)
                    .append("&key=")
                    .append("AIzaSyAfSGY6_Z_zj1yyLD_fa9NuI4vVlEgECKI");



            dataTransfer[0] = sb.toString();
            this.execute(dataTransfer);

        } catch (Exception e) {
            System.out.println("=========================================================fail\n===========faail");
            e.printStackTrace();

        }

    }
    /**not in the guide*/

}
