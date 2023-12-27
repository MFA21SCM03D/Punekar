package com.example.punekar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.punekar.Models.Message;
import com.example.punekar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.mViewHolder> {

    private static  final int MSG_TYPE_RECIPIENT = 0;
    private static  final int MSG_TYPE_SENDER = 1;
    private Context mContext;
    private List<Message> mMessage;

    private static SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss a, dd-MM-yy");

    private FirebaseUser user;

      String currentUserID;

//    public MessageAdapter(Context mContext, List<Message> mMessage) {
//        this.mContext = mContext;
//        this.mMessage = mMessage;
//    }

    public MessageAdapter(String currentUserID){

        this.currentUserID = currentUserID;
        mMessage = new ArrayList<>();

    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == MSG_TYPE_SENDER) {
//            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//            view = inflater.inflate(R.layout.message_box_sender, parent, false);
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_box_sender, parent, false);
        }
        else {

//            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//             view = inflater.inflate(R.layout.message_box_recipient, parent, false);

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_box_recipient, parent, false);

        }
        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {

//        holder.message_box.setText(mMessage.get(position).getMessage());
          holder.bindView(mMessage.get(position));
    }

    @Override
    public int getItemCount() {
        return mMessage.size();
    }

    public void setData(List<Message> messages) {
        this.mMessage = messages;
        notifyDataSetChanged();
    }

    class mViewHolder extends RecyclerView.ViewHolder {

        TextView message_box;
        TextView date;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);

            message_box = itemView.findViewById(R.id.message_box);
            date = itemView.findViewById(R.id.date);


        }

        public void bindView(Message message) {

            message_box.setText(message.getMessage());

            if(message.getDate() != null) {
                date.setText(format.format(message.getDate()));
            }else{
                date.setText(format.format(new Date()));
            }

        }
    }

    @Override
    public int getItemViewType(int position) {

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (mMessage.get(position).getSender().equals(currentUserID)){

            return MSG_TYPE_SENDER;

        }
        else {

            return MSG_TYPE_RECIPIENT;

        }

    }
}
