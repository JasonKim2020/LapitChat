package com.jasonkim2020.android.b0427firechat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Messages> mMessageList;
    private FirebaseAuth mAuth;

    public MessageAdapter(List<Messages> mMessageList) {
        this.mMessageList = mMessageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout, parent, false);
        mAuth = FirebaseAuth.getInstance();
        return new MessageViewHolder(v);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public CircleImageView profileImage;
        public Context context;
        public TextView displayName;
        public ImageView messageImage;

        public MessageViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            messageText = (TextView) itemView.findViewById(R.id.message_text_layout);
            profileImage = (CircleImageView) itemView.findViewById(R.id.message_profile_layout);
            displayName = (TextView) itemView.findViewById(R.id.name_text_layout);
            messageImage = (ImageView) itemView.findViewById(R.id.message_image_layout);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder viewHolder, int i) {
        String current_user_id = mAuth.getCurrentUser().getUid();
        Messages c = mMessageList.get(i);
        String from_user = c.getFrom();
        String message_type = c.getType();
        if (from_user.equals(current_user_id)) {
//            viewHolder.messageText.setBackgroundColor(Color.WHITE);
//            viewHolder.messageText.setTextColor(Color.BLACK);

        } else {
//            viewHolder.messageText.setBackgroundResource(R.drawable.message_text_background);
//            viewHolder.messageText.setTextColor(Color.WHITE);


        }

        //if it is text message or not
        if(message_type.equals("text")){
            viewHolder.messageText.setText(c.getMessage());
            viewHolder.messageImage.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.messageText.setVisibility(View.INVISIBLE);
            Picasso.with(viewHolder.profileImage.getContext()).load(c.getMessage())
                    .placeholder(R.drawable.default_avatar).into(viewHolder.messageImage);
        }

        // ------------   Sender profile image ------------
        String thumb_image = c.getThumb_image();

        //because there can be a user that have not uploaded any image yet.
        //then image url has "default"
        //when it is not, the imageview shows the pointed image.
        if (!thumb_image.equals("noImage")) {

            //Retrieve image data from the local device
            // by "networkPolicy(NetworkPolicy.OFFLINE)"
            Picasso.with(viewHolder.context).load(thumb_image)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.default_avatar).into(viewHolder.profileImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                //Retrieve image data from the internet
                @Override
                public void onError() {
                    Picasso.with(viewHolder.context).load(thumb_image).placeholder(R.drawable.default_avatar).into(viewHolder.profileImage);
                }
            });
        } else {
            viewHolder.profileImage.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}