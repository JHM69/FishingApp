package com.jhm69.farhad_fishingapp.Activity;

import androidx.annotation.NonNull;
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
import com.jhm69.farhad_fishingapp.Adapters.PostAdapter;
import com.jhm69.farhad_fishingapp.Model.MyPost;
import com.jhm69.farhad_fishingapp.R;

import java.util.ArrayList;
import java.util.List;

public class FishIntroduction extends AppCompatActivity {

    private RecyclerView mRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private PostAdapter postAdapter;
    private List<MyPost> myPosts;
    private ShimmerFrameLayout shimmerFrameLayout;
    private TextView marquree;
    private DatabaseReference mainDB;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_fish_introduction );
        Window window = this.getWindow();
        window.clearFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor( ContextCompat.getColor(this,R.color.status));
        marquree = findViewById( R.id.marqueeTV );
        mRecycler = findViewById(R.id.rcv_f_i);

        key  = getIntent().getStringExtra( "key" );


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle( "Angler's Dream of BD" );
        toolbar.setTitleTextColor( ContextCompat.getColor(this, R.color.textColor) );
        setSupportActionBar(toolbar);

        shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmerAnimation();
        mainDB = FirebaseDatabase.getInstance().getReference();
        postAdapter = new PostAdapter( myPosts, getApplicationContext() );
        layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecycler.setLayoutManager(layoutManager);
        myPosts = new ArrayList<>();
        getMyPosts();
        loadData();
    }

    private void getMyPosts() {

        mainDB.child( "MyPost" ).child(key).addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @com.google.firebase.database.annotations.Nullable String s) {
                MyPost angler = dataSnapshot.getValue(MyPost.class);
                myPosts.add(angler);
                postAdapter = new PostAdapter(myPosts,getApplicationContext());
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
                postAdapter.getFilter().filter(newText);
                return true;
            }
        });



        return  true;
    }
 }
