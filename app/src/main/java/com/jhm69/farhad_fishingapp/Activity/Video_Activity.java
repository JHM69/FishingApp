package com.jhm69.farhad_fishingapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.jhm69.farhad_fishingapp.Model.Video;
import com.jhm69.farhad_fishingapp.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.jhm69.farhad_fishingapp.Adapters.UserPostAdapter.getDateFromTimeStamp;

public class Video_Activity extends AppCompatActivity {
    private Video video ;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mainDB;
    private String username, userimg;
    private ImageView userImg;
    private TextView TitleTV,dateTV,commentCount,body;
    private RecyclerView recyclerViewPost;
    private RecyclerView.LayoutManager layoutManagerPost;
    private CommentAdapter recycleAdapterPost;
    private List<Comment> commentListPost;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_video );
        Intent i = getIntent();
        video = (Video) i.getSerializableExtra("37");

        firebaseAuth = FirebaseAuth.getInstance();
        mainDB = FirebaseDatabase.getInstance().getReference();

        recyclerViewPost = findViewById( R.id.rcv_my_video );
        layoutManagerPost = new LinearLayoutManager( this.getApplicationContext());
        recyclerViewPost.setLayoutManager( layoutManagerPost );
        recyclerViewPost.setHasFixedSize( false );
        commentListPost = new ArrayList<>();
        userImg = findViewById(R.id.proImgPw);

        TitleTV = findViewById(R.id.postTitleTV);
        dateTV = findViewById(R.id.dateTV);
        commentCount = findViewById(R.id.commentCountTV);

        body = findViewById(R.id.body);
        TitleTV.setText( video.getTitle() );
        dateTV.setText(getDateFromTimeStamp(  video.getDate() ));
        commentCount.setText( "0" );
        body.setText( video.getText() );



        if(firebaseAuth.getCurrentUser() == null){
            Toast.makeText( this, "Create a account to comment.", Toast.LENGTH_SHORT ).show();
        }else{
            LoadUserData();
        }
        loadComment();
        final YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = video.getLink();
                youTubePlayer.loadVideo(videoId,0);

            }

        });

        final EditText commentTextP = findViewById( R.id.commentTextPw );
        CardView cmnt = findViewById( R.id.commentBtnPw );
        cmnt.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firebaseAuth.getCurrentUser() == null){
                    Intent intent = new Intent( getApplicationContext(), AthenticationActivity.class );
                    startActivity( intent );
                }else{
                    String time = new SimpleDateFormat( "HH:mm", Locale.getDefault() ).format( new Date() );
                    String todaysDate = new SimpleDateFormat( "dd-MM-YYYY", Locale.getDefault() ).format( new Date() );
                    String comntText = commentTextP.getText().toString();
                    Comment comment = new Comment(firebaseAuth.getCurrentUser().getUid() , username, userimg, time + ", " + todaysDate, comntText );
                    mainDB.child( "Video" ).child( "All" ).child( video.getId() ).child( "comments" ).push().setValue( comment ).addOnCompleteListener( new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            commentTextP.setText( "" );
                        }
                    } );

                }
            }
        } );


    }


    private void loadComment() {
        mainDB.child("Video").child( "All" ).child( video.getId() ).child( "comments" ).addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                commentListPost.add(comment);
                recycleAdapterPost = new CommentAdapter(commentListPost,getApplicationContext());
                recyclerViewPost.setAdapter(recycleAdapterPost);
                recycleAdapterPost.notifyDataSetChanged();
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
                Picasso.get().load( userimg ).into( userImg );
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

}
