package com.jhm69.farhad_fishingapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.*;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.widget.Toolbar;
import android.widget.SearchView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jhm69.farhad_fishingapp.Adapters.AnglerRecycleAdapter;
import com.jhm69.farhad_fishingapp.Model.Angler;
import com.jhm69.farhad_fishingapp.R;

import java.util.ArrayList;
import java.util.List;

public class AnglersList extends AppCompatActivity  {
    RecyclerView shimmerRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private AnglerRecycleAdapter anglerRecycleAdapter;
    private List<Angler> anglerList;
ShimmerFrameLayout shimmerFrameLayout;
    private DatabaseReference mainDB;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        Window window = this.getWindow();
        window.clearFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor( ContextCompat.getColor(this,R.color.status));
        setContentView( R.layout.activity_anglers_list );
        mainDB = FirebaseDatabase.getInstance().getReference();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmerAnimation();


        shimmerRecycler = findViewById(R.id.recyclerView);

        mainDB = FirebaseDatabase.getInstance().getReference();
        anglerRecycleAdapter = new  AnglerRecycleAdapter( anglerList, getApplicationContext() );
        layoutManager = new LinearLayoutManager(this);

        shimmerRecycler.setLayoutManager(new LinearLayoutManager(this));
        shimmerRecycler.setLayoutManager(layoutManager);
        shimmerRecycler.setHasFixedSize( true );
        anglerList = new ArrayList<>();
        getAnglersData();
    }

    private void getAnglersData() {

        mainDB.child("user").addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Angler angler = dataSnapshot.getValue(Angler.class);
                anglerList.add(angler);
                anglerRecycleAdapter = new AnglerRecycleAdapter(anglerList,getApplicationContext());
                shimmerRecycler.setAdapter( anglerRecycleAdapter );
                anglerRecycleAdapter.notifyDataSetChanged();

                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility( View.GONE);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                anglerRecycleAdapter.getFilter().filter(newText);
                return true;
            }
        });



        return  true;
    }

}
