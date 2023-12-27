package com.example.punekar;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.punekar.Adapters.MessageAdapter;
import com.example.punekar.Models.Chat;
import com.example.punekar.Models.Message;
import com.example.punekar.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    ImageView chat_icon, back_page;
    FloatingActionButton send;
    TextView chat_handle;
    EditText text_message;
    String UserID;
    String UserName;
    Toolbar toolbar;
    User user_info;
    Message msg_info;
    RecyclerView recyclerView;
    private Message msg1;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseFirestore mRef;
    FirebaseUser user1;
    FirebaseUser user;
    private CollectionReference usersCollection;
    private CollectionReference chatCollection;
    private CollectionReference messageCollection;
    private static final String TAG = "MessageActivity";
    StorageReference storageReference;

    Intent intent;

    MessageAdapter messageAdapter;
    List<Message> mMessage;
    Chat chat_info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        chat_icon = findViewById(R.id.chat_user_photo);
        chat_handle = findViewById(R.id.chat_user);
        back_page = findViewById(R.id.back_page);
        send = findViewById(R.id.send);
        text_message = findViewById(R.id.text_message);
        recyclerView = findViewById(R.id.message_recyclerView);

        mAuth = FirebaseAuth.getInstance();

        messageAdapter = new MessageAdapter(mAuth.getCurrentUser().getUid());
        mMessage = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView = findViewById(R.id.message_recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        intent = getIntent();

        UserID = intent.getStringExtra("UserID");
        UserName = intent.getStringExtra("UserName");


        user_info = new User();
        msg_info = new Message();
        chat_info = new Chat();

        user1 = mAuth.getCurrentUser();
        System.out.println("Message Activity User ID"+user1);
        user = mAuth.getCurrentUser();

        mRef = FirebaseFirestore.getInstance();
        usersCollection = mRef.collection("Users");
        chatCollection = mRef.collection("Chats");
        messageCollection = mRef.collection("Messages");
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        setupFirebaseAuth();

        back_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text_message.getText().toString().trim();

                if (!msg.equals("")){

                    Message message = new Message(user1.getUid(), UserID, msg,user1.getDisplayName(), UserName);
                    Chat chat = new Chat(user1.getDisplayName(),UserName);
                    System.out.println(chat.getRecipientName()+"\n"+ chat.getSenderName());
                    sendMessage(message);

                }
                text_message.setText("");
            }
        });
    }

    private void setupFirebaseAuth(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = mAuth.getCurrentUser();
                User user1 = new User();

                if(user != null){
                    Log.d(TAG, "onAuthStateChanged: Signed in");
                    getChatUser(UserID);
//                    readMessages(user.getUid(), UserID);

                }
                else{
                    Log.d(TAG, "onAuthStateChanged: Signed out");
                }
            }
        };
    }

    private void getChatUser(final String user1) {

        usersCollection.document(UserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    user_info = task.getResult().toObject(User.class);
                    Log.d(TAG, "onComplete: " + user_info.getName());
                    chat_handle.setText(user_info.getName());
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
                Glide.with(MessageActivity.this)
                        .load(uri).override(360, 360).
                        diskCacheStrategy(DiskCacheStrategy.ALL).into(chat_icon);
                //Picasso.get().load(uri).resize(360, 360).centerCrop()
                // .placeholder(R.drawable.userphoto).error(R.drawable.userphoto).into(profile_photo);

            }
        });


    }

//    private void sendMessage(final String sender, final String recipient, final String msg) {
//
////        chatCollection.document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
////            @Override
////            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
////                if(task.isSuccessful()){
////
////                    msg_info = task.getResult().toObject(Message.class);
////                    Log.d(TAG, "onSend: " + user_info.getName());
////                    msg1 = new Message(sender, recipient, msg);
////                    chatCollection.document(msg1.getSender()).set(msg1);
////                }
////                else{
////                    Log.d(TAG, "onComplete: failed to retrieve data");
////                }
////            }
////        });
//        chatCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
//                    Message message = documentSnapshot.toObject(Message.class);
//
//                    if (user.getUid().equals(sender)) {
//
//                        System.out.println(message.getSender());
//                        System.out.println(message.getRecipient());
//                        msg1 = new Message(sender, recipient, msg);
//                        chatCollection.document(msg1.getSender()).set(msg1);
//                    }
//
////                    MessageAdapter adapter1 = new MessageAdapter();
////                    recyclerView.setAdapter(adapter1);
//                }
//
//
//
//            }
//        });
//
//
//    }

    private void sendMessage(Message message){

//        chatCollection.add(message).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentReference> task) {
//                if(!task.isSuccessful()){
//
//                    Toast.makeText(MessageActivity.this, "Cannot send empty msg", Toast.LENGTH_SHORT).show();
//
//                }
//                else{
//
//                    text_message.setText("");
//
//                }
//            }
//        });

        usersCollection.document(user.getUid()).collection("Chats").document(message.getRecipientName())
                .collection("Messages").add(message).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(!task.isSuccessful()){

                    Toast.makeText(MessageActivity.this, "Cannot send empty msg", Toast.LENGTH_SHORT).show();

                }
                else{

                    text_message.setText("");

                }
            }
        });

    }

    private void readMessages(final String senderID, final String recipientID){

//            ADD PARAMETER THAT ARE REQUIRED TO FETCH DATA FOR INSTANCE ADD USER ID USER NAME

//        mMessage = new ArrayList<>();
//
//        chatCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//
//
//                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
//                    Message message = documentSnapshot.toObject(Message.class);
////                    List<Message> mMessage = queryDocumentSnapshots.toObjects(Message.class);
//                    if (message.getSender().equals(senderID) && message.getRecipient().equals(recipientID) ||
//                            message.getSender().equals(recipientID) && message.getRecipient().equals(senderID)) {
//
//                        System.out.println(message.getSender());
//                        System.out.println(message.getRecipient());
//                        mMessage.add(message);
//
//                        messageAdapter.setData(mMessage);
//                        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() + 1);
////                        readMessage();
//                    }
//
////                        MessageAdapter adapter1 = new MessageAdapter(MessageActivity.this, mMessage);
////                        recyclerView.setAdapter(adapter1);
//                }
//
//
//
//            }
//        });

//        usersCollection.orderBy("name").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                if (e != null){
//
//                    Log.e(TAG, "Error while reading messages");
//
//                }
//                else {
//                    List<Message> mMessage = queryDocumentSnapshots.toObjects(Message.class);
//
//                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                       Message message = documentSnapshot.toObject(Message.class);
//                       // User user = documentSnapshot.toObject(User.class);
//                        if (user.getUid().equals( senderID )|| user.getUid().equals(recipientID)) {
//                            chatCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
//                                @Override
//                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                                    if (e != null) {
//
//                                        Log.e(TAG, "Error while reading messages");
//
//                                    } else {
//
//                                        for (QueryDocumentSnapshot documentSnapshot1: queryDocumentSnapshots) {
//                                            Message message = documentSnapshot1.toObject(Message.class);
//                                            User user1 = documentSnapshot1.toObject(User.class);
//                                            if (user.getUid().equals( senderID )|| user.getUid().equals(recipientID)
//                                                    ||user1.getName().equals(user_info.getName())
//                                                    ) {
//                                                messageCollection.orderBy("date").addSnapshotListener(new EventListener<QuerySnapshot>() {
//                                                    @Override
//                                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                                                        if (e != null) {
//
//                                                            Log.e(TAG, "Error while reading messages");
//
//                                                        } else {
//                                                            assert queryDocumentSnapshots != null;
//                                                            for (QueryDocumentSnapshot documentSnapshot2: queryDocumentSnapshots){
//                                                                Message message = documentSnapshot2.toObject(Message.class);
//                                                                    if (message.getSender().equals(senderID) && message.getRecipient().equals(recipientID) ||
//                                                                    message.getSender().equals(recipientID) && message.getRecipient().equals(senderID)) {
//                                                                    List<Message> mMessage = queryDocumentSnapshots.toObjects(Message.class);
//                                                                    messageAdapter.setData(mMessage);
//                                                                    recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
//                                                                }
//                                                            }
//                                                        }
//                                                    }
//                                                });
//                                            }
//                                        }
//                                    }
//                                }
//                            });
//                        }
//                    }
//                }
//            }
//        });

        usersCollection.document(user.getUid()).collection("Chats")
                .document(UserName).collection("Messages").orderBy("date")
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){

                    Log.e(TAG, "Error while reading messages");

                }
                else {
                    List<Message> mMessage = queryDocumentSnapshots.toObjects(Message.class);
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                        Message message = documentSnapshot.toObject(Message.class);
                        //message.setRecipientName(UserName);
//                   if (message.getSender().equals(senderID) && message.getRecipient().equals(recipientID) ||
//                        message.getSender().equals(recipientID) && message.getRecipient().equals(senderID)) {

//                        System.out.println(message.getSender());
//                        System.out.println(message.getRecipient());

                        //mMessage.add(message);
                        messageAdapter.setData(mMessage);
                        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
//                        }
                    }
                }

            }
        });


    }

//    private void readMessage(){
//
//        chatCollection.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//
//                if (e != null){
//
//                    Log.e(TAG, "Error while reading messages");
//
//                }
//                else{
//
//                    assert queryDocumentSnapshots != null;
//                    List<Message> mMessage = queryDocumentSnapshots.toObjects(Message.class);
//                    messageAdapter.setData(mMessage);
//                    recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
//
//                }
//
//            }
//        });
//
//    }

    private void status(String status){

        //Status Online or Offline

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
        if (user1 != null){
            readMessages(user1.getUid(), UserID);
        }
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
//        Intent intent2 = new Intent(getApplicationContext(), UsersListActivity.class);
//        startActivity(intent2);
        finish();
    }

}
