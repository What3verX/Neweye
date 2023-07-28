package com.osmanlioglu.neweye.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.osmanlioglu.neweye.Models.MessageModel;
import com.osmanlioglu.neweye.Models.Users;
import com.osmanlioglu.neweye.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<MessageModel> messageModels;
    Context context;
    String recID;
    HashMap<String, String> userImagesMap;
    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;
    public static FirebaseStorage storage = FirebaseStorage.getInstance();
    static StorageReference storageRef = storage.getReference();

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context, HashMap<String, String> userImagesMap) {
        this.messageModels = messageModels;
        this.context = context;
        this.userImagesMap = userImagesMap;
    }

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context, String recID) {
        this.messageModels = messageModels;
        this.context = context;
        this.recID = recID;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDER_VIEW_TYPE) {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender, parent, false);
            return new SendViewHolder(view);
        } else if (viewType == RECEIVER_VIEW_TYPE) {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_receiver, parent, false);
            return new ReceiverViewHolder(view);
        } else {
            throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel messageModel = messageModels.get(position);
        if (!Objects.equals(messageModel.getuId(), Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
            return RECEIVER_VIEW_TYPE;
        } else {
            return SENDER_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {



        MessageModel messageModel = messageModels.get(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Delete the message (For me)")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String senderRoom = FirebaseAuth.getInstance().getUid() + recID;
                                database.getReference().child("chats").child(senderRoom)
                                        .child(messageModel.getMessageId())
                                        .setValue(null);
                            }
                        }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
                return false;
            }
        });

        if (holder instanceof SendViewHolder) {
            ((SendViewHolder) holder).senderMsg.setText(messageModel.getMessage());
            ((SendViewHolder) holder).senderTime.setText(getFormattedTime(messageModel.getTimestamp()));

        } else if (holder instanceof ReceiverViewHolder) {
            ((ReceiverViewHolder) holder).receiverMsg.setText(messageModel.getMessage());
            ((ReceiverViewHolder) holder).receiverTime.setText(getFormattedTime(messageModel.getTimestamp()));
            ((ReceiverViewHolder) holder).setProfileImage(recID);
        }
    }


    private String getFormattedTime(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
        return simpleDateFormat.format(date);
    }

    public void setMessageModels(ArrayList<MessageModel> messageModels) {
        this.messageModels = messageModels;
    }

    @Override
    public int getItemCount() {
        return messageModels.size();


    }
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();


    public static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView receiverMsg, receiverTime;
        CircleImageView profileImage;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            receiverMsg = itemView.findViewById(R.id.receiverText);
            receiverTime = itemView.findViewById(R.id.receiverTime);
            profileImage = itemView.findViewById(R.id.profile_image);
        }

        public void setProfileImage(String url) {
            StorageReference profileRef = storageRef.child("profile_pic/" + url);

            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(profileImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("TAG", "Error loading image", exception);
                }
            });
        }

    }

    public static class SendViewHolder extends RecyclerView.ViewHolder {
        TextView senderMsg, senderTime;


        public SendViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.senderText);
            senderTime = itemView.findViewById(R.id.senderTime);

        }
    }
}




