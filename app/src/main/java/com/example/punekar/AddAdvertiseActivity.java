package com.example.punekar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.punekar.Models.Advertise;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddAdvertiseActivity extends AppCompatActivity {

    Button add;
    EditText name, address, contact, roommateNeeded, tenure, totalTenants, rent;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore mRef = FirebaseFirestore.getInstance();
    private CollectionReference roommateCollection = mRef.collection("Advertise");
    private CollectionReference usersCollection = mRef.collection("Users");
    FirebaseUser user;
    private static final String TAG = "AddAdvertiseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addadvertisement);

        add = findViewById(R.id.add);
        name = findViewById(R.id.advertisername);
        tenure = findViewById(R.id.tenure);
        contact = findViewById(R.id.contactno);
        roommateNeeded = findViewById(R.id.roommate);
        address = findViewById(R.id.address);
        totalTenants = findViewById(R.id.numberoftenants);
        rent = findViewById(R.id.totalrent);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        setupFirebaseAuth();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String advertiser_name = name.getText().toString().trim();
                String room_address = address.getText().toString().trim();
                String room_tenure = tenure.getText().toString().trim();
                String roommate_needed = roommateNeeded.getText().toString().trim();
                String roommate_contact = contact.getText().toString().trim();
                String total_tenants = totalTenants.getText().toString().trim();
                String total_rent = rent.getText().toString().trim();

                if (!advertiser_name.equals("") || !room_address.equals("") ||
                        !room_tenure.equals("") || !roommate_needed.equals("") ||
                        !roommate_contact.equals("") || !total_tenants.equals("") || !total_rent.equals("")){

                    Advertise advertise = new Advertise(room_address, room_tenure, roommate_needed,
                            roommate_contact, advertiser_name, total_tenants, user.getUid(), total_rent);
                    roommateCollection.document(user.getUid()).set(advertise);

                }
                name.setText("");
                address.setText("");
                roommateNeeded.setText("");
                contact.setText("");
                tenure.setText("");
                totalTenants.setText("");
                rent.setText("");
                finish();
            }
        });
    }

    private void setupFirebaseAuth(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = mAuth.getCurrentUser();

                if(user != null){

                    Log.d(TAG, "onAuthStateChanged: Signed in");

                }
                else{
                    Log.d(TAG, "onAuthStateChanged: Signed out");

                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
