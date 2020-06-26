package com.jhm69.farhad_fishingapp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jhm69.farhad_fishingapp.R;

import java.util.HashMap;
import java.util.Map;

public class AdminMainPage extends AppCompatActivity {
    private  Button addPost, addVideo, addMarquree, addShop;
    private EditText marquree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_admin_main_page );
        final DatabaseReference mainDB = FirebaseDatabase.getInstance().getReference();
        addPost = findViewById( R.id.addpost );
        addVideo = findViewById( R.id.addvideo );
        addShop = findViewById( R.id.addShop );
        marquree = findViewById( R.id.editTMq );
        addMarquree = findViewById( R.id.buttonTMq );
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentAddPost addPost = FragmentAddPost.newInstance();
                addPost.show( getSupportFragmentManager(), "" );
            }
        });
        addVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentAddVideo addVideo = FragmentAddVideo.newInstance();
                addVideo.show( getSupportFragmentManager(), "" );
            }
        });
        addMarquree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> marqure = new HashMap<>(  );
                marqure.put( "text", marquree.getText().toString() );
                mainDB.child( "Admin" ).setValue( marqure ).addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText( AdminMainPage.this, "Done", Toast.LENGTH_SHORT ).show();
                    }
                } );
            }
        });
        addShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity( new Intent( AdminMainPage.this,AddShop.class  ) );
            }
        });

    }
}
