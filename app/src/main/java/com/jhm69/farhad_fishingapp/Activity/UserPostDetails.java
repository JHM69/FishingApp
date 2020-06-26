package com.jhm69.farhad_fishingapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jhm69.farhad_fishingapp.Adapters.CommentAdapter;
import com.jhm69.farhad_fishingapp.Model.Comment;
import com.jhm69.farhad_fishingapp.Model.UserPost;
import com.jhm69.farhad_fishingapp.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.jhm69.farhad_fishingapp.Adapters.UserPostAdapter.getDateFromTimeStamp;

public class UserPostDetails extends AppCompatActivity {
    private DatabaseReference mainDB;
    private UserPost userPost;
    private ImageView myImage;
    private TextView nameTV, body, date, location;
    private FirebaseAuth firebaseAuth;
    private ImageView profile, postimg;
    private String userimg, username;
    private TextView titel;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CommentAdapter recycleAdapter;
    private List<Comment> commentList;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_user_post_details );
        firebaseAuth = FirebaseAuth.getInstance();
        mainDB = FirebaseDatabase.getInstance().getReference();
        nameTV = findViewById( R.id.text_name );
        date = findViewById( R.id.date );
        body = findViewById( R.id.bodyTV );
        postimg = findViewById( R.id.image_url );
        myImage = findViewById( R.id.proImg );
        profile = findViewById( R.id.pro_imagen );
        location = findViewById( R.id.text_location );
        Window window = this.getWindow();

        window.clearFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor( ContextCompat.getColor(this,R.color.status));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        titel = toolbar.findViewById( R.id.toolbar_title );

        recyclerView = findViewById( R.id.rcv_com );
        layoutManager = new LinearLayoutManager( this.getApplicationContext());
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize( false );
        commentList = new ArrayList<>();

        Intent i = getIntent();
        userPost = (UserPost) i.getSerializableExtra("mm");

        if(userPost.getImage()=="0"){
            postimg.setVisibility( View.GONE );
        }else{
            Picasso.get().load( userPost.getImage() ).into( postimg);
        }
        if(userPost.getLocation().equals( "" )){
            location.setVisibility( View.GONE );
        }else {
            location.setVisibility( View.VISIBLE );
            location.setText( "is at "+userPost.getLocation() );
        }

        location.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapView( userPost.getLocation() );
            }
        } );

        loadProfile();

        nameTV.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), MyProfileActivity.class );
                in.putExtra("uid",userPost.getUserId() );
                startActivity(in);
            }
        } );
        date.setText( getDateFromTimeStamp( userPost.getDate() ));
        body.setText( userPost.getText() );

        Picasso.get().load( userPost.getUserImg() ).fit().into( profile );
        if(firebaseAuth.getCurrentUser() == null){
            Toast.makeText( this, "Create a account to comment.", Toast.LENGTH_SHORT ).show();
        }else{
            LoadUserData();
        }
        loadComment();
        final EditText commentText = findViewById( R.id.commentText );
        CardView cmnt = findViewById( R.id.commentBtn );
        cmnt.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firebaseAuth.getCurrentUser() == null){
                    Intent intent = new Intent( getApplicationContext(), AthenticationActivity.class );
                    startActivity( intent );
                }else{
                    String time = new SimpleDateFormat( "HH:mm", Locale.getDefault() ).format( new Date() );
                    String todaysDate = new SimpleDateFormat( "dd-MM-YYYY", Locale.getDefault() ).format( new Date() );
                    String comntText = commentText.getText().toString();
                    Comment comment = new Comment(firebaseAuth.getCurrentUser().getUid() , username, userimg, time + ", " + todaysDate, comntText );
                    mainDB.child( "user_post" ).child( userPost.getId() ).child( "comments" ).push().setValue( comment ).addOnCompleteListener( new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            commentText.setText( "" );
                        }
                    } );

                }
            }
        } );
    }

    private void loadProfile() {
        String uid =userPost.getUserId();
        mainDB.child("user").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String img = dataSnapshot.child("img").getValue().toString();
                Picasso.get().load( img ).into( profile );
                nameTV.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void loadComment() {
        mainDB.child("user_post").child( userPost.getId() ).child( "comments" ).addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                commentList.add(comment);
                titel.setText( comment.getName()+"'s Post" );
                recycleAdapter = new CommentAdapter(commentList,getApplicationContext());
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

    private void LoadUserData() {
           mainDB.child( "user" ).child( firebaseAuth.getCurrentUser().getUid() ).addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username = dataSnapshot.child( "name" ).getValue().toString();
                userimg = dataSnapshot.child( "img" ).getValue().toString();
                Picasso.get().load(userimg) .into( myImage );
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    private void openMapView(String locationName){
        String map = "http://maps.google.com.bd/maps?q=" + locationName;
        try {
            Intent sharingIntent = new Intent(Intent.ACTION_VIEW);
            sharingIntent.setData( Uri.parse(map));
            sharingIntent.setPackage("com.google.android.apps.maps");
            startActivity(sharingIntent);
        } catch (ActivityNotFoundException e) {
            Uri uri = Uri.parse(map);
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(goToMarket);
        }


    }

}