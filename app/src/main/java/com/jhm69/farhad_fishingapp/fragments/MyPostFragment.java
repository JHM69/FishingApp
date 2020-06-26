package com.jhm69.farhad_fishingapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jhm69.farhad_fishingapp.Adapters.AnglerRecycleAdapter;
import com.jhm69.farhad_fishingapp.Adapters.PostAdapter;
import com.jhm69.farhad_fishingapp.Model.Angler;
import com.jhm69.farhad_fishingapp.Model.MyPost;
import com.jhm69.farhad_fishingapp.R;

import java.util.ArrayList;
import java.util.List;

public class MyPostFragment extends Fragment {

    private RecyclerView mRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private PostAdapter postAdapter;
    private List<MyPost> myPosts;
    private ShimmerFrameLayout shimmerFrameLayout;
    private DatabaseReference mainDB;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mRecycler = root.findViewById(R.id.recyclerViewPost);
        shimmerFrameLayout = (ShimmerFrameLayout) root.findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmerAnimation();
        mainDB = FirebaseDatabase.getInstance().getReference();
        postAdapter = new PostAdapter( myPosts, getActivity() );
        layoutManager = new LinearLayoutManager(getActivity());

        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setLayoutManager(layoutManager);
        myPosts = new ArrayList<>();
        getMyPosts();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );


    }

    private void getMyPosts() {

        DatabaseReference ref= mainDB.child( "MyPost" ).child( "All" );
        Query query = ref.limitToLast(10);
        query.addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @com.google.firebase.database.annotations.Nullable String s) {
                MyPost angler = dataSnapshot.getValue(MyPost.class);
                myPosts.add(angler);
                postAdapter = new PostAdapter(myPosts,getActivity());
                mRecycler.setAdapter( postAdapter );
                postAdapter.notifyDataSetChanged();
                // stop animating Shimmer and hide the layout

                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @com.google.firebase.database.annotations.Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @com.google.firebase.database.annotations.Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }


}