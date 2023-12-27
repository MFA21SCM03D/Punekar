package com.example.punekar;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.punekar.Utilities.GetNearbyPlacesData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class FragmentRestaurants extends Fragment {
    private static final String TAG = "FragmentNearBy";
    View v;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    private RecyclerView recyclerView;
    private LocationManager locationManager;
    private LocationListener locationListener;

    public FragmentRestaurants() {
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_nearby_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = v.findViewById(R.id.recycler_view);
        setupFirebaseAuth();
        getLocation();
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Starting");
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "onPause: Starting");
    }

    private void getLocation(){
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                getPlaces(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);



        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    private void setupFirebaseAuth() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();

                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: SIgned in");

                } else {
                    Log.d(TAG, "onAuthStateChanged: signed out");
                }
            }
        };
    }



    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }




    private void getPlaces(Location location) {
        Log.d(TAG, "getPlaces: Starting");
        Log.d(TAG, "getPlaces: location: " + location.toString());


        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                + Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude()) +
                "&radius=1500&type=hospital&key=" + getString(R.string.google_api_key);
        Log.d(TAG, "getPlaces: " + url);

        Object[] DataTransfer = new Object[3];
        DataTransfer[0] = recyclerView;
        DataTransfer[1] = url;
        DataTransfer[2] = getContext();

        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(DataTransfer);


    }





}
