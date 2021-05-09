package com.jasonkim2020.android.b0427firechat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestFragment extends Fragment {


    //Requests List
    private RecyclerView mReqList;

    //Requests list
    private DatabaseReference mReqDatabase;

    private DatabaseReference mUserDatabase;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    // to inflate main view
    private View mMainView;

    private FirebaseRecyclerOptions<Requests> options;
    private FirebaseRecyclerAdapter<Requests, RequestFragment.ReqViewHolder> reqRecyclerViewAdapter;

    public RequestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // to inflate main view
        mMainView = inflater.inflate(R.layout.fragment_request, container, false);

        mReqList = (RecyclerView) mMainView.findViewById(R.id.req_list);
        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();
        //Request list
        mReqDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req").child(mCurrent_user_id);

        mReqDatabase.keepSynced(true);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUserDatabase.keepSynced(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        //Recycler View
        mReqList.setHasFixedSize(true);
        mReqList.setLayoutManager(linearLayoutManager);

        //inflate the layout for this fragment.
        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        //set options having chat room information
        options = new FirebaseRecyclerOptions.Builder<Requests>().setQuery(mReqDatabase, Requests.class).build();
        reqRecyclerViewAdapter = new FirebaseRecyclerAdapter<Requests, ReqViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ReqViewHolder reqholder, int position, @NonNull Requests requests) {
                //Display Requests

                final String list_user_id = getRef(position).getKey();
                mReqDatabase.child(list_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String requesType = snapshot.child("request_type").getValue().toString();
                        reqholder.setType(requesType);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                // Retrieving User name, and image.
                mUserDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String userName = dataSnapshot.child("name").getValue().toString();
                        String userThumb = dataSnapshot.child("thumb_image").getValue().toString();

                        if (dataSnapshot.hasChild("online")) {
                            String userOnline = dataSnapshot.child("online").getValue().toString();
                            reqholder.setUserOnline(userOnline);
                        }
                        //// Setting User name, and image.
                        reqholder.setName(userName);
                        reqholder.setUserImage(userThumb, getContext());


                        // Onclick view hoder -> send to ChatActivity.
                        reqholder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                chatIntent.putExtra("user_id", list_user_id);
                                chatIntent.putExtra("user_name", userName);
                                startActivity(chatIntent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }


            @NonNull
            @Override
            public RequestFragment.ReqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_single_layout, parent, false);
                return new RequestFragment.ReqViewHolder(v);
            }
        };

        reqRecyclerViewAdapter.startListening();
        mReqList.setAdapter(reqRecyclerViewAdapter);
    }

    public static class ReqViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public ReqViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mView = itemView;
        }

        public void setType(String type) {
            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
            userStatusView.setText(type);
        }

        public void setName(String name) {
            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);
        }

        public void setUserImage(String thumb_image, Context ctx) {
            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.default_avatar).into(userImageView);
        }

        public void setUserOnline(String online_status) {
            ImageView userOnlineView = (ImageView) mView.findViewById(R.id.user_single_online_icon);
            if (online_status.equals("true")) {
                userOnlineView.setVisibility(View.VISIBLE);
            } else {
                userOnlineView.setVisibility(View.INVISIBLE);
            }
        }
    }
}