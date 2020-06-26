package com.jhm69.farhad_fishingapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jhm69.farhad_fishingapp.Adapters.AnglerRecycleAdapter;
import com.jhm69.farhad_fishingapp.Adapters.StoreAapter;
import com.jhm69.farhad_fishingapp.Model.Angler;
import com.jhm69.farhad_fishingapp.Model.Store;
import com.jhm69.farhad_fishingapp.R;

import java.util.ArrayList;
import java.util.List;

public class FishingStore extends AppCompatActivity {
    private RecyclerView shimmerRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private StoreAapter storeAapter;
    private List<Store> storeList;
    private DatabaseReference mainDB;
    private TextView marquree;
    private ShimmerFrameLayout shimmerFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_fishing_store );
        Window window = this.getWindow();
        window.clearFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor( ContextCompat.getColor(this,R.color.status));
        mainDB = FirebaseDatabase.getInstance().getReference();
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmerAnimation();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle( "Angler's Dream of BD" );
        toolbar.setTitleTextColor( ContextCompat.getColor(this, R.color.textColor) );
        marquree = findViewById( R.id.marqueeTV );
        setSupportActionBar(toolbar);

        shimmerRecycler = findViewById(R.id.rcv_shop);

        mainDB = FirebaseDatabase.getInstance().getReference();
        storeAapter = new StoreAapter( storeList, getApplicationContext() );
        layoutManager = new LinearLayoutManager(this);

        shimmerRecycler.setLayoutManager(new LinearLayoutManager(this));
        shimmerRecycler.setLayoutManager(layoutManager);
        shimmerRecycler.setHasFixedSize( true );
        storeList = new ArrayList<>();
        getStoresData();
        loadData();

    }

    private void getStoresData() {
        mainDB.child("store").addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Store store = dataSnapshot.getValue(Store.class);
                storeList.add(store);
                storeAapter = new StoreAapter(storeList,getApplicationContext());
                shimmerRecycler.setAdapter( storeAapter );
                storeAapter.notifyDataSetChanged();

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
                storeAapter.getFilter().filter(newText);
                return true;
            }
        });



        return  true;
    }

    private void loadData() {
        mainDB.child( "Admin" ).addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String text = dataSnapshot.child( "text" ).getValue().toString();

                marquree.setText( text );
                marquree.setSelected( true );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
}
