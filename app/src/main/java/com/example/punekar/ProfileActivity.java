package com.example.punekar;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.punekar.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class ProfileActivity extends AppCompatActivity {

    private TextView full_name, email,contact,gender, views_count, collaboration_count ,skills,
            institution, certification_institute, institution_degree, certification_name, institution_duration,
            certification_duration, interests, qualification;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
     FirebaseFirestore mRef;
    private CollectionReference usersCollection;
    private User user_info;
    ImageView profile_photo;
    static int PReqcode = 1;
    static int Reqcode = 1;
    Uri ImagePicker;
     ScrollView scrollView;
     FirebaseUser currentuser;
    private Context mContext;
    private static final String TAG = "ProfileActivity";
     CollectionReference usersReference;
     StorageReference storageReference;

    public ProfileActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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


        user_info = new User();


        System.out.println("Profile View Counter 1 : "+user_info.getViews()+" Collaboration Count 1 : "+user_info.getCollab());


        mRef = FirebaseFirestore.getInstance();
        usersCollection = mRef.collection("Users");

        mAuth = FirebaseAuth.getInstance();
        currentuser = mAuth.getCurrentUser();
        setupFirebaseAuth();

        mRef = FirebaseFirestore.getInstance();
        usersReference = mRef.collection("Users");
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        setupFirebaseAuth();

    }

    private void setupFirebaseAuth(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();

                if(user != null){
                    Log.d(TAG, "onAuthStateChanged: Signed in");
                    getData(user);


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

    private void getData(FirebaseUser user1){
        usersCollection.document(user1.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
//                    if (user_info.getInstitution()==null ||
//                            user_info.getInstitutionDegree()==null ||
//                            user_info.getInstitutionDuration()==null ||
//                            user_info.getCertificationName()==null ||
//                            user_info.getCertificationDuration()==null ||
//                            user_info.getCertificationInstitution()==null ||
//                            user_info.getSkills()==null ||
//                            user_info.getInterests()==null || user_info.getQualification() ==null) {
//
//
//                        institution.setHint("Institute/University");
//                        institution_duration.setHint("Degree");
//                        institution_degree.setHint("Duration(DD/MM/YY");
//                        certification_institute.setHint("Certificate Name");
//                        certification_name.setHint("Certificate Institute");
//                        certification_duration.setHint("Certificate Validity");
//                        skills.setHint("Skills");
//                        interests.setHint("Interests");
//                        qualification.setHint("Qualification");
//
//                    }else {
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
//                    }

                }
                else{
                    Log.d(TAG, "onComplete: failed to retrieve data");
                }
            }
        });
    }

    private void getImage(String url){
        Log.d(TAG, "getImage: " + url);
        StorageReference fileReference = storageReference.child(url);
        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Glide.with(ProfileActivity.this).load(uri).override(50, 50).into(profile_photo);
                Picasso.get().load(uri).resize(360, 360).centerCrop().into(profile_photo);

            }
        });


    }

}
