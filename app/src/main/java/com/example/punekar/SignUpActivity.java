package com.example.punekar;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.punekar.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private Uri imageUri;
    //defining view objects
    private static final int PICK_IMAGE_REQUEST = 1;

    private static final String TAG = "SignUpActivity";
    
    private EditText signup_email, signup_password, username, signup_contact;
    private Button create_account;
    private CircleImageView profileImageView;
    private RadioButton mRadioButton,fRadioButton;
     ScrollView signup_scrollview;

    private User user1;

    private ProgressDialog progressDialog;
     FirebaseFirestore mRef;
    private CollectionReference usersCollection;
    private StorageReference storageReference;

    private boolean imagePicked;
    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;
     FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        imagePicked = false;
        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();
        mRef = FirebaseFirestore.getInstance();
        usersCollection = mRef.collection("Users");

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        //if getCurrentUser does not returns null
        if(firebaseAuth.getCurrentUser() != null){
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        //initializing views
        username =  findViewById(R.id.signup_name);
        signup_email = findViewById(R.id.signup_email);
        signup_password = findViewById(R.id.signup_password);
        signup_contact = findViewById(R.id.signup_contact);
        profileImageView = findViewById(R.id.profile_image);
        create_account = findViewById(R.id.btn_signup);
        mRadioButton = findViewById(R.id.male);
        fRadioButton = findViewById(R.id.female);
        signup_scrollview = findViewById(R.id.signup_scrollview);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        create_account.setOnClickListener(this);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openGallery();
            }
        });
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFile(String imageUrl){
        if(imageUri != null) {
            final StorageReference fileReference  = storageReference.child(imageUrl);

            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    user1.setUrl(fileReference.getDownloadUrl().toString());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUpActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(this,"Please Select a profile photo",Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode != RESULT_CANCELED && data != null && data.getData() != null){
            imageUri = data.getData();
            imagePicked = true;
            //Glide.with(this).load(imageUri).override(50, 50).into(profileImageView);

            Picasso.get().load(imageUri).resize(360, 360).centerCrop().into(profileImageView);
        }
    }


    private void registerUser(){

        //getting email and password from edit texts
        final String name = username.getText().toString().trim();
        final String email = signup_email.getText().toString().trim();
        final String contact = signup_contact.getText().toString().trim();
        String password  = signup_password.getText().toString().trim();

        //checking if name,email and passwords are empty
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Please enter Name",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter Email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter Password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    String gender = "";
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            sendVerificationEmail();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String imageUrl = System.currentTimeMillis() + "." + getFileExtension(imageUri);
                            if (mRadioButton.isChecked()){
                                gender = "Male";
                            }
                            else if(fRadioButton.isChecked()){
                                gender = "Female";
                            }
                            user1 = new User(name,email,gender,contact,imageUrl,user.getUid(),
                                    "", "", "",
                                    "","",
                                    "", "", "", "",0,0);
                            usersCollection.document(user1.getUserID()).set(user1);
                            uploadFile(imageUrl);
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                            user.updateProfile(profileUpdates);
                            finish();
                        }
                        progressDialog.dismiss();


                    }
                });

    }
    private void sendVerificationEmail()
    {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // email sent
                                finish();
                                Toast.makeText(SignUpActivity.this,"Please verify your E-mail",Toast.LENGTH_LONG).show();
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));

                            }
                            else
                            {
                                // email not sent, so display message and restart the activity or do whatever you wish to do

                                //restart this activity
                                overridePendingTransition(0, 0);
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());

                            }
                        }
                    });
        }
    }

    @Override
    public void onClick(View view) {

        if(view == create_account){
            if(!imagePicked){
                Toast.makeText(this,"Please select a profile photo",Toast.LENGTH_SHORT).show();
            }else {
                registerUser();
            }
            }

//        if(view == textViewLogin){
//            //open login activity when user taps on the already registered textview
//            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
//        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
