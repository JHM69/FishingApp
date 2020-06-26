package com.jhm69.farhad_fishingapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jhm69.farhad_fishingapp.Adapters.CollectionAdapter;
import com.jhm69.farhad_fishingapp.Model.Collection;
import com.jhm69.farhad_fishingapp.R;
import com.jhm69.farhad_fishingapp.fragments.addCollection;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CollectionAdapter recycleAdapter;
    private List<Collection> collectionList;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mainDB;
    private String uid, uidMy;
    private TextView titel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_my_profile );
        Window window = this.getWindow();

        window.clearFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor( ContextCompat.getColor(this,R.color.status));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar( toolbar );
        titel = toolbar.findViewById( R.id.toolbar_title );
        //toolbar.setTitle( "" );
        firebaseAuth = FirebaseAuth.getInstance();

        uidMy = firebaseAuth.getCurrentUser().getUid();
        Intent intent = getIntent();
        uid = intent.getExtras().getString("uid");
        Intent in = getIntent();
        //uid = in.getStringExtra( "uid" );
        mainDB = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById( R.id.rcv_myColl );
        layoutManager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize( true );
        collectionList = new ArrayList<>();

        DatabaseReference mainDb = FirebaseDatabase.getInstance().getReference();

        final ImageView pro_pic = findViewById( R.id.pro );
        final FloatingActionButton edit = findViewById( R.id.edit );
        final TextView nameTV = findViewById( R.id.name );
        final TextView bloodTV = findViewById( R.id.blood_group );
        final TextView pesaTV = findViewById( R.id.pesa );
        final TextView addTV = findViewById( R.id.homehg );
        final TextView numTV = findViewById( R.id.age );
        CardView addColl = findViewById( R.id.addColl );
        if(uid.equals(uidMy)){
            addColl.setVisibility( View.VISIBLE );
            edit.setVisibility( View.VISIBLE );

        }else {
            addColl.setVisibility( View.GONE );
            edit.setVisibility( View.GONE );
        }
        loadCollection();
        mainDb.child("user").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String img = dataSnapshot.child("img").getValue().toString();
                titel.setText( name  );
                Picasso.get().load( img ).into( pro_pic );
                nameTV.setText(name);
                bloodTV.setText( dataSnapshot.child("blood").getValue().toString() );
                pesaTV.setText( dataSnapshot.child("pesha").getValue().toString() );
                numTV.setText( dataSnapshot.child("number").getValue().toString() );
                addTV.setText( dataSnapshot.child("home").getValue().toString() );


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addColl.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCollection addPost = addCollection.newInstance();
                addPost.show( getSupportFragmentManager(), "" );
            }
        } );

        edit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent( getApplicationContext(), EditProfile.class );
                startActivity( intent1 );
            }
        } );
    }

    private void loadCollection() {
            mainDB.child("user").child(uid).child( "collection" ).addChildEventListener( new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Collection collection = dataSnapshot.getValue(Collection.class);
                    collectionList.add(collection);
                    recycleAdapter = new CollectionAdapter(collectionList,getApplicationContext());
                    recyclerView.setAdapter(recycleAdapter);
                    recycleAdapter.notifyDataSetChanged();
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


}
