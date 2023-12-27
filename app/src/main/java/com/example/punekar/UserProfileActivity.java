package com.example.punekar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.punekar.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserProfileActivity extends AppCompatActivity {


    private TextView full_name, email,contact,gender,views_count, collaboration_count, skills,
            institution, certification_institute, institution_degree, certification_name, institution_duration,
            certification_duration, interests, qualification;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseFirestore mRef;
    private CollectionReference usersCollection;
     private User user_info;
    ImageView profile_photo;
    ScrollView scrollView;
    private static final String TAG = "ProfileActivity";
    StorageReference storageReference;
    Intent intent;
    String UserID;
    int ViewCount, count1, CollabCount, count2;

    public UserProfileActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_userprofile);


        full_name = findViewById(R.id.profile_username);
        email = findViewById(R.id.profile_email);
        contact = findViewById(R.id.profile_contact);
        gender = findViewById(R.id.profile_gender);
        profile_photo = findViewById(R.id.profile_photo);
        views_count = findViewById(R.id.views_count);
        collaboration_count = findViewById(R.id.collaboration_count);
        qualification = findViewById(R.id.qualification);
        institution = findViewById(R.id.institution);
        institution_duration = findViewById(R.id.duration);
        institution_degree = findViewById(R.id.degree);
        certification_institute = findViewById(R.id.certification_institute);
        certification_name = findViewById(R.id.certification_name);
        certification_duration = findViewById(R.id.certification_duration);
        skills = findViewById(R.id.skill_name);
        interests = findViewById(R.id.interests);
        scrollView = findViewById(R.id.profile_ScrollView);

        intent = getIntent();

        UserID = intent.getStringExtra("UserID");
        ViewCount = intent.getIntExtra("ViewCount", 0);
        CollabCount = intent.getIntExtra("CollaborationCount", 0);
        System.out.println("View Count 1 : "+ViewCount+" Collab Count 1 : "+CollabCount);
//        count1 = ViewCount++;
//        count2 = CollabCount++;
        System.out.println("View Count 2 : "+ViewCount+" Collab Count 2 : "+CollabCount);
        user_info = new User();

//            user_info.setViews(++ViewCount);
//            user_info.setCollab(++CollabCount);


        System.out.println("Profile View Counter 1 : "+user_info.getViews()+" Collaboration Count 1 : "+user_info.getCollab());
        mAuth = FirebaseAuth.getInstance();

        mRef = FirebaseFirestore.getInstance();
        usersCollection = mRef.collection("Users");

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        setupFirebaseAuth();

    }

    private void setupFirebaseAuth(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = mAuth.getCurrentUser();

                if(user != null){
                    Log.d(TAG, "onAuthStateChanged: Signed in");
                    //getUserInfo(user);
                    System.out.println("Profile View Counter 2 : "+user_info.getViews()+" Collaboration Count 2 : "+user_info.getCollab());
                    getData(user);
//                    usersCollection.document(UserID).update("views",user_info.getViews(),"collab", user_info.getCollab());

                }
                else{
                    Log.d(TAG, "onAuthStateChanged: Signed out");
                }
            }
        };
    }

    private void getData(FirebaseUser user1){
        usersCollection.document(UserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    user_info = task.getResult().toObject(User.class);
                    Log.d(TAG, "onComplete: " + user_info.getName());
                    full_name.setText(user_info.getName());
                    email.setText(user_info.getEmail());
                    contact.setText(user_info.getContact());
                    gender.setText(user_info.getGender());
                    System.out.println("Profile View Counter 3 : "+user_info.getViews()+" Collaboration Count 3 : "+user_info.getCollab());
                    views_count.setText("" + user_info.getViews());
                    collaboration_count.setText("" + user_info.getCollab());
                    institution.setText("" + user_info.getInstitution());
                    institution_degree.setText("" + user_info.getInstitutionDegree());
                    institution_duration.setText("" + user_info.getInstitutionDuration());
                    certification_name.setText("" + user_info.getCertificationName());
                    certification_institute.setText("" + user_info.getCertificationInstitution());
                    certification_duration.setText("" + user_info.getCertificationDuration());
                    skills.setText("" + user_info.getSkills());
                    interests.setText("" + user_info.getInterests());
                    qualification.setText("" + user_info.getQualification());
                    getImage(user_info.getUrl());
                }
                else{
                    Log.d(TAG, "onComplete: failed to retrieve data");
                }
            }
        });
    }

    private void getUserInfo(final FirebaseUser user1) {

        usersCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    User user2 = documentSnapshot.toObject(User.class);
                    for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots) {
                        User user3 = documentSnapshot1.toObject(User.class);

                        if (user1.getUid().equalsIgnoreCase(user2.getUserID())) {
                            System.out.println("Comparing ID and printing the Name in UserProfileActivity \n"
                                    + user1.getUid() + "\n" + user2.getName());

                        } else if (user2.getUserID().equalsIgnoreCase(user3.getUserID())) {

                            System.out.println("Comparing ID and printing the Name \n" + user2.getUserID() + "\n" + user3.getUserID());
                            System.out.println("Comparing ID and printing the Name \n" + user2.getName() + "\n" + user3.getName());

                            full_name.setText(user3.getName());
                            email.setText(user3.getEmail());
                            contact.setText(user3.getContact());
                            gender.setText(user3.getGender());
                            getImage(user3.getUrl());

                            System.out.println(user2);
                            System.out.println(user2.getName());
                            System.out.println(user2.getUrl());

                        }
                    }


                }
            }
        });

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
//        Intent intent = new Intent(getApplicationContext(), UsersListActivity.class);
//        startActivity(intent);
          finish();
    }

    private void getImage(String url){
        Log.d(TAG, "getImage: " + url);
        StorageReference fileReference = storageReference.child(url);
        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(UserProfileActivity.this)
                        .load(uri).override(360, 360).
                        diskCacheStrategy(DiskCacheStrategy.ALL).into(profile_photo);
                //Picasso.get().load(uri).resize(360, 360).centerCrop()
                // .placeholder(R.drawable.userphoto).error(R.drawable.userphoto).into(profile_photo);

            }
        });


    }
}
