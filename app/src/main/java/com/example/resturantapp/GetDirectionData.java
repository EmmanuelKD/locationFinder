package com.example.resturantapp;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

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
import java.util.Arrays;
import java.util.List;

public class GetDirectionData extends AsyncTask<Object,String,String> {
    GoogleMap mMap;
    String Url;
    LatLng MyLocation,MyDestination;


    Context context;

    HttpURLConnection httpURLConnection=null;
    String data= "";
    InputStream inputStream=null;

    private static final int PATTERN_GAP_LENGTH_PX = 10;  // 1
    private static final Gap GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    private static final Dot DOT = new Dot();
    private static final List<PatternItem> PATTERN_DOTTED = Arrays.asList(DOT, GAP);  // 2



    public GetDirectionData(Context context){

        this.context=context;
    }

    @Override
    protected String doInBackground(Object... objects) {

        this.mMap=(GoogleMap) objects[0];
        Url=(String) objects[1];
        MyLocation=(LatLng)objects[2];
        MyDestination=(LatLng)objects[3];

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
            System.out.println("====================================\n"+"====================================\n"+"====================================\n"+jsonObject.toString());
            JSONArray jsonArray=jsonObject.getJSONArray("routes").getJSONObject(0)
                    .getJSONArray("legs").getJSONObject(0).getJSONArray("steps");

            int count= jsonArray.length();

            String[] polyLine_array=new String[count];


            JSONObject jsonObject1;
            for(int i=0; i<count;i++){
                jsonObject1=jsonArray.getJSONObject(i);
                String polygon=jsonObject1.getJSONObject("polyline").getString("points");
                polyLine_array[i]=polygon;
            }

            int count1=polyLine_array.length;


            for(int i=0; i<count1;i++){
                PolylineOptions options2 = new PolylineOptions();
                options2.color(Color.BLUE);
                options2.width(10);
                options2.pattern(PATTERN_DOTTED);
                options2.addAll(PolyUtil.decode(polyLine_array[i]));

                System.out.println("adding poly lines ====="+count1+"==================================> ");
                this.mMap.addPolyline(options2);
            }

        } catch (JSONException e) {
            Toast.makeText(context, "check internet connection",Toast.LENGTH_SHORT).show();
            System.out.println("this is direction error =======================================> "+ e);
            e.printStackTrace();
        }
    }
    /**not in the guide*/



}
