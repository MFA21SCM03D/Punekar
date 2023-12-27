package com.example.punekar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

public class EditProfileActivity extends AppCompatActivity {
    Button save;
    private TextView fullname, email,contact,gender, views_count, collaboration_count;
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
    EditText institution, certification_institute, institution_degree, certification_name, institution_duration,
            certification_duration, skills, interests, qualification;

    public EditProfileActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        save = findViewById(R.id.save);
        fullname = findViewById(R.id.profile_username);
        email = findViewById(R.id.profile_email);
        contact = findViewById(R.id.profile_contact);
        gender = findViewById(R.id.profile_gender);
        profile_photo = findViewById(R.id.profile_photo);
        institution = findViewById(R.id.institution);
        institution_duration = findViewById(R.id.duration);
        institution_degree = findViewById(R.id.degree);
        certification_institute = findViewById(R.id.certification_institute);
        certification_name = findViewById(R.id.certification_name);
        certification_duration = findViewById(R.id.certification_duration);
        skills = findViewById(R.id.skill_name);
        interests = findViewById(R.id.interests);
        views_count = findViewById(R.id.views_count);
        collaboration_count = findViewById(R.id.collaboration_count);
        qualification = findViewById(R.id.qualification);
        scrollView = findViewById(R.id.profile_ScrollView);

        user_info = new User();



        mRef = FirebaseFirestore.getInstance();
        usersCollection = mRef.collection("Users");

        mAuth = FirebaseAuth.getInstance();
        currentuser = mAuth.getCurrentUser();
        setupFirebaseAuth();


        profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT >= 22){

                    requestforpermission();

                }else{

                    openGallery();

                }

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String qualifications = qualification.getText().toString().trim();
                String institute = institution.getText().toString().trim();
                String institute_degree = institution_degree.getText().toString().trim();
                String institute_duration = institution_duration.getText().toString().trim();
                String certificate = certification_name.getText().toString().trim();
                String certificate_institute = certification_institute.getText().toString().trim();
                String certificate_duration = certification_duration.getText().toString().trim();
                String skill = skills.getText().toString().trim();
                String interest = interests.getText().toString().trim();
                System.out.println(" \n Qualification : "+qualifications+" \n Institute Name : "+institute+
                        " \n Institute Degree : "+institute_degree+"\n Institute Duration"+
                        institute_duration+" \n Certificate : "+certificate+
                        " \n Certificate Institute : "+certificate_institute+
                        " \n Certificate Duration : "+certificate_duration+
                        " \n Interests : "+interest+" \n Skills : "+ skill);

                if (user_info.getInstitution()==null ||
                        user_info.getInstitutionDegree()==null ||
                        user_info.getInstitutionDuration()==null ||
                        user_info.getCertificationName()==null ||
                        user_info.getCertificationDuration()==null ||
                        user_info.getCertificationInstitution()==null ||
                        user_info.getSkills()==null ||
                        user_info.getInterests()==null || user_info.getQualification() ==null ) {


                    institution.setHint("Institute/University");
                    institution_duration.setHint("Degree");
                    institution_degree.setHint("Duration(DD/MM/YY");
                    certification_institute.setHint("Certificate Name");
                    certification_name.setHint("Certificate Institute");
                    certification_duration.setHint("Certificate Validity");
                    skills.setHint("Skills");
                    interests.setHint("Interests");
                    qualification.setHint("Qualification");

                }else {

                    user_info.setInstitution(institute);
                    user_info.setInstitutionDegree(institute_degree);
                    user_info.setInstitutionDuration(institute_duration);
                    user_info.setCertificationName(certificate);
                    user_info.setCertificationInstitution(certificate_institute);
                    user_info.setCertificationDuration(certificate_duration);
                    user_info.setSkills(skill);
                    user_info.setInterests(interest);
                    user_info.setQualification(qualifications);
                    System.out.println("User Info : \n Qualification : \n"+
                            user_info.getQualification()+
                            "Institution : \n "+ user_info.getInstitution()+
                            "InstitutionDegree : \n "+ user_info.getInstitutionDegree()+
                            "InstitutionDuration : \n "+ user_info.getInstitutionDuration()+
                            "CertificationName : \n "+ user_info.getCertificationName()+
                            "CertificationInstitution : \n "+ user_info.getCertificationInstitution()+
                            "CertificationDuration : \n "+ user_info.getCertificationDuration()+
                            "Skills : \n "+ user_info.getSkills()+ "Interests : \n "+user_info.getInterests());
                    usersCollection.document(user_info.getUserID()).update("institution", user_info.getInstitution(),
                            "institutionDegree", user_info.getInstitutionDegree(),
                            "institutionDuration", user_info.getInstitutionDuration(),
                            "certificationName", user_info.getCertificationName(),
                            "certificationInstitution", user_info.getCertificationInstitution(),
                            "certificationDuration", user_info.getCertificationDuration(),
                            "skills", user_info.getSkills(), "interests", user_info.getInterests(),
                            "qualification",
                            user_info.getQualification());

                }

                finish();

            }
        });

        mRef = FirebaseFirestore.getInstance();
        usersReference = mRef.collection("Users");
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        setupFirebaseAuth();

    }

    private void requestforpermission() {

        if(ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){

                Toast.makeText(EditProfileActivity.this, "Please grant permission", Toast.LENGTH_SHORT).show();

            }
            else{

                ActivityCompat.requestPermissions(EditProfileActivity.this, new String []{Manifest.permission.READ_EXTERNAL_STORAGE},PReqcode);

            }
        }
        else{

            openGallery();

        }

    }

    private void openGallery(){

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, Reqcode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == Reqcode && data != null){

            ImagePicker = data.getData();
            profile_photo.setImageURI(ImagePicker);

        }


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
                    fullname.setText(user_info.getName());
                    email.setText(user_info.getEmail());
                    contact.setText(user_info.getContact());
                    gender.setText(user_info.getGender());
                    views_count.setText(""+user_info.getViews());
                    collaboration_count.setText(""+user_info.getCollab());
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
