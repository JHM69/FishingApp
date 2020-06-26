package com.jhm69.farhad_fishingapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jhm69.farhad_fishingapp.Activity.FishIntroduction;
import com.jhm69.farhad_fishingapp.Activity.FishingStore;
import com.jhm69.farhad_fishingapp.R;
import com.jhm69.farhad_fishingapp.Activity.AnglersList;

public class CategoryFragment extends Fragment {
    private DatabaseReference mainDB ;
    private CardView anglersList , fishI, spot, component, notis;
    private TextView marquree;
    private AdView mAdView;
    private CardView knowledge ;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mainDB = FirebaseDatabase.getInstance().getReference();
        anglersList = root.findViewById(R.id.anglerslist);
        fishI = root.findViewById(R.id.cardVe22);
        knowledge = root.findViewById(R.id.cardView2);
        marquree =  root.findViewById(R.id.marqueeTV);
        spot =  root.findViewById(R.id.soptCV);
        notis =  root.findViewById(R.id.notis);
        component = root.findViewById(R.id.cardView22);
        mAdView =  root.findViewById(R.id.adView);

        AdView adView = new AdView(getActivity());
        adView.setAdSize( AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-1654931311089326/3447689878");
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        component.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FishIntroduction.class);
                intent.putExtra( "key",  "component" );
                startActivity(intent);
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        anglersList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AnglersList.class);
                startActivity(intent);
            }
        });
        fishI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FishIntroduction.class);
                intent.putExtra( "key",  "মাছ পরিচিতি" );
                startActivity(intent);
            }
        });
        knowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FishingStore.class);
                startActivity(intent);
            }
        });
        spot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FishIntroduction.class);
                intent.putExtra( "key",  "spot" );
                startActivity(intent);
            }
        });
        notis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FishIntroduction.class);
                intent.putExtra( "key",  "নোটিশ বোর্ড" );
                startActivity(intent);
            }
        });
        loadData();

    }

    private void loadData() {

        mainDB.child("Admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String text = dataSnapshot.child("text").getValue().toString();
                marquree.setText( text );
                marquree.setSelected( true );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

