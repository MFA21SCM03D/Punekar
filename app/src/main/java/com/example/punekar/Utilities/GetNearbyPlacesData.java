package com.example.punekar.Utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.punekar.Adapters.RecyclerViewLocationsAdapter;
import com.example.punekar.Models.Locations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    RecyclerView recyclerView;
    String url;
    Context context;
    ArrayList<Locations> mLocations;

    public GetNearbyPlacesData(){
        mLocations = new ArrayList<>();
    }

    private static final String TAG = "GetNearbyPlacesData";
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList = dataParser.parse(s);

        createRecyclerView(nearbyPlacesList,context,recyclerView);
        Log.d(TAG, "onPostExecute: nearby places" + nearbyPlacesList.toString());

    }

    private void createRecyclerView(List<HashMap<String, String>> nearbyPlacesList, Context context,
                                    RecyclerView recyclerView){
        for(int i = 0; i < nearbyPlacesList.size(); i++){
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            Locations locations = new Locations(googlePlace.get("place_name"),
                    Double.parseDouble(googlePlace.get("lat")), Double.parseDouble(googlePlace.get("lng")));
            mLocations.add(locations);

        }

        RecyclerViewLocationsAdapter recyclerViewLocationsAdapter = new RecyclerViewLocationsAdapter(context,mLocations);
        recyclerView.setAdapter(recyclerViewLocationsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));


    }

    @Override
    protected String doInBackground(Object... objects) {
        try{
            recyclerView = (RecyclerView) objects[0];
            url = (String)objects[1];
            context = (Context) objects[2];
            DownloadUrl downloadUrl = new DownloadUrl();

            googlePlacesData = downloadUrl.readUrl(url);
            Log.d(TAG, "doInBackground: " + googlePlacesData);


        }catch (Exception e){
            Log.d(TAG, "doInBackground: " + e.toString());
        }


        return googlePlacesData;
    }



}
