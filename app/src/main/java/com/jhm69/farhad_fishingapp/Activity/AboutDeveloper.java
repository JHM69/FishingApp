package com.jhm69.farhad_fishingapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jhm69.farhad_fishingapp.R;

import java.util.HashMap;
import java.util.Map;

public class AboutDeveloper extends AppCompatActivity {
    private ImageView fb, git, link;
    private CardView send;
    private ProgressDialog progressDialog;
    private EditText messegeET;
    private FirebaseAuth firebaseAuth;
    private String  name="Anonymous";
    private DatabaseReference mainDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_developer);
        Window window = this.getWindow();
        window.clearFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor( ContextCompat.getColor(this,R.color.status));

        mainDB = FirebaseDatabase.getInstance().getReference();
        Toolbar toolbar = (Toolbar) findViewById(R.id.bahr);
        toolbar.setTitle( "About Developer" );
        toolbar.setTitleTextColor( ContextCompat.getColor(this,R.color.tab_text) );
        firebaseAuth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        fb = findViewById(R.id.facebook);
        git = findViewById(R.id.github);
        link = findViewById(R.id.linkedin);
        send = findViewById(R.id.send);
        messegeET = findViewById(R.id.messege);

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent sharingIntent = new Intent(Intent.ACTION_VIEW);
                    sharingIntent.setType("URL");
                    sharingIntent.setData( Uri.parse("fb://facewebmodal/f?href=https://m.facebook.com/jhm69"));
                    sharingIntent.setPackage("com.facebook.katana");
                    startActivity(sharingIntent);
                } catch (ActivityNotFoundException e) {
                    Uri urin = Uri.parse("https://m.facebook.com/jhm69");
                    Intent goToMarketn = new Intent(Intent.ACTION_VIEW, urin);
                    startActivity(goToMarketn);
                }

            }
        });
        git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri urin = Uri.parse("https://m.https://github.com/JHM69");
                Intent goToMarketn = new Intent(Intent.ACTION_VIEW, urin);
                startActivity(goToMarketn);

            }
        });
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri urin = Uri.parse("https://www.linkedin.com/in/jahangir-hossain-b8325017b/");
                Intent goToMarketn = new Intent(Intent.ACTION_VIEW, urin);
                startActivity(goToMarketn);

            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog
                        .show(AboutDeveloper.this,
                                "Sending...",
                                "");
                        String messege = messegeET.getText().toString();
                        Map<String,String> feedback = new HashMap<>();
                        feedback.put("messege", messege);
                        mainDB.child("Public Feedback").push().setValue(feedback).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    messegeET.setText("");
                                    progressDialog.dismiss();
                                    Toast.makeText(AboutDeveloper.this, "successfully sent", Toast.LENGTH_SHORT).show();
                                }else{
                                    progressDialog.dismiss();
                                    Toast.makeText(AboutDeveloper.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
}