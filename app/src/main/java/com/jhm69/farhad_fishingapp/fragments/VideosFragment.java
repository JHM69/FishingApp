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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jhm69.farhad_fishingapp.Adapters.VideoAdapter;
import com.jhm69.farhad_fishingapp.Model.Video;
import com.jhm69.farhad_fishingapp.R;

import java.util.ArrayList;
import java.util.List;

public class VideosFragment extends Fragment {

    private RecyclerView mRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private VideoAdapter videoAdapter;
    private List<Video> videos;
    private ShimmerFrameLayout shimmerFrameLayout;
    private DatabaseReference mainDB;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        mRecycler = root.findViewById(R.id.recyclerViewPostVideo);
        shimmerFrameLayout = (ShimmerFrameLayout) root.findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmerAnimation();
        mainDB = FirebaseDatabase.getInstance().getReference();
        videoAdapter = new VideoAdapter( videos, getActivity() );
        layoutManager = new LinearLayoutManager(getActivity());

        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setLayoutManager(layoutManager);
        videos = new ArrayList<>();
        getMyVideos();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );


    }

    private void getMyVideos() {

        mainDB.child( "Video" ).child( "All" ).addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @com.google.firebase.database.annotations.Nullable String s) {
                Video video = dataSnapshot.getValue(Video.class);
                videos.add(video);
                videoAdapter = new VideoAdapter(videos,getActivity());
                mRecycler.setAdapter( videoAdapter );
                videoAdapter.notifyDataSetChanged();
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