package com.jasonkim2020.android.b0427firechat;

import android.content.Context;
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


public class FriendsFragment extends Fragment {

    private RecyclerView mFriendsList;

    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;


    private FirebaseRecyclerOptions<Friends> options;
    private FirebaseRecyclerAdapter<Friends, FriendsViewHolder> friendsRecyclerViewAdapter;

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_friends, container, false);

        mAuth = FirebaseAuth.getInstance();
        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        //refer root > friends folder in realtime database
        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurrent_user_id);
        mFriendsDatabase.keepSynced(true);

        //refer root > Users folder in realtime database
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);

        //Set RecyclerView
        mFriendsList = (RecyclerView) mMainView.findViewById(R.id.friends_list);
        mFriendsList.setHasFixedSize(true);
        mFriendsList.setLayoutManager(new LinearLayoutManager(getContext()));

        //Inflate the layout for this fragment
        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        options = new FirebaseRecyclerOptions.Builder<Friends>().setQuery(mFriendsDatabase, Friends.class).build();
        friendsRecyclerViewAdapter = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(options) {
            //manipulate data
            @Override
            protected void onBindViewHolder(@NonNull FriendsViewHolder friendsViewHolder, int position, @NonNull Friends friends) {
                friendsViewHolder.setDate(friends.getDate());

                String list_user_id = getRef(position).getKey();

                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String userName = dataSnapshot.child("name").getValue().toString();
                        String userThumb = dataSnapshot.child("thumb_image").getValue().toString();

                        if(dataSnapshot.hasChild("online")){
                            Boolean userOnline = (boolean) dataSnapshot.child("online").getValue();
                            friendsViewHolder.setUserOnline(userOnline);
                        }
                        friendsViewHolder.setName(userName);
                        friendsViewHolder.setUserImage(userThumb, getContext());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            //inflate layout.
            @NonNull
            @Override
            public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_single_layout, parent, false);
                return new FriendsViewHolder(v);
            }
        };
        friendsRecyclerViewAdapter.startListening();
        mFriendsList.setAdapter(friendsRecyclerViewAdapter);
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public FriendsViewHolder(View itemView){
            super(itemView);

            mView = itemView;
        }

        public void setDate(String date){
            TextView userStautsView = (TextView) mView.findViewById(R.id.user_single_status);
            userStautsView.setText(date);
        }
        public void setName(String name){
            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);
        }

        public void setUserImage(String thumb_image, Context ctx) {
            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
            //Picasso is a easy component to show image with url in a imageview
            Picasso.with(ctx)
                    .load(thumb_image)
                    .placeholder(R.drawable.default_avatar)
                    .into(userImageView);
        }

        public void setUserOnline(Boolean online_status){
            ImageView userOnlineView = (ImageView) mView.findViewById(R.id.user_single_online_icon);
            if(online_status == true){
                userOnlineView.setVisibility(View.VISIBLE);
            }else{
                userOnlineView.setVisibility(View.INVISIBLE);
            }

        }

    }
}