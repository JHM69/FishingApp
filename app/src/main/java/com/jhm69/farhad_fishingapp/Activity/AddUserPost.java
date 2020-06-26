package com.jhm69.farhad_fishingapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jhm69.farhad_fishingapp.Model.UserPost;
import com.jhm69.farhad_fishingapp.R;
import com.jhm69.farhad_fishingapp.utils.CustomDialogeClass;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static com.jhm69.farhad_fishingapp.fragments.UserPostFragment.refreshFragment;

public class AddUserPost extends AppCompatActivity {
    private DatabaseReference mainDB;
    private TextView userNameTV, dateTimeTV;
    private Button addPhotoBtn, cancelBtn, postBtn;
    private EditText postET, locationET;
    private ImageView userImg, addImg;
    String location;
    private String username, userimg;
    String timestamp;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference NoticesReference;
    Bitmap bitmap;
    CustomDialogeClass cdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.add_user_post );
        timestamp = getCurrentTimeStamp();
        cdd = new CustomDialogeClass(this);
       // userID = firebaseAuth.getCurrentUser().getUid();
        userNameTV = findViewById(R.id.userNameTV);
        dateTimeTV = findViewById(R.id.dateTimeTV);
        NoticesReference =  FirebaseStorage.getInstance().getReference();

        mainDB = FirebaseDatabase.getInstance().getReference();
        userImg = findViewById(R.id.userImg);
        addImg = findViewById(R.id.addImg);
        addPhotoBtn= findViewById(R.id.addPhotoBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        postBtn = findViewById(R.id.postBtn);
        postET = findViewById(R.id.postTextET);
        locationET = findViewById(R.id.locationTextET);

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Toast.makeText( this, "Create an account to Comment", Toast.LENGTH_SHORT ).show();
        }else{
            LoadData();
        }



        addPhotoBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChoseImage();
            }
        } );
        postBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UploadPost();

            }
        } );
    }

    private void UploadPost() {
        cdd.show();
        if(locationET.getText().toString().equals( null )){
            location="0";
        }else{
            location = locationET.getText().toString();
        }
        final String keyID = getRandomString( 13 );
        if(filePath == null){
            UserPost myPost = new UserPost( FirebaseAuth.getInstance().getCurrentUser().getUid() ,userimg,username,getCurrentTimeStamp(), postET.getText().toString(),"0",location,keyID);
            uploadToDatabase( myPost );
        }else{

            final StorageReference fileReference = NoticesReference.child( "IMAGES/" ).child( System.currentTimeMillis()
                    + "." + "png" );
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress( Bitmap.CompressFormat.PNG, 20, baos );
            byte[] fileInBytes = baos.toByteArray();
            UploadTask uploadTask = (UploadTask) fileReference.putBytes( fileInBytes )
                    .addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(final Uri uri) {
                                    UserPost myPost = new UserPost(FirebaseAuth.getInstance().getCurrentUser().getUid() ,userimg,username,"645827931",postET.getText().toString(), uri.toString().trim() ,location, keyID);
                                    uploadToDatabase( myPost );
                                }
                            } );
                        }
                    } );
        }

    }

    private void uploadToDatabase(UserPost userPost) {
        DatabaseReference mainPosts = mainDB.child( "user_post" );
        mainPosts.child(userPost.getId()).setValue( userPost ).addOnCompleteListener( new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Posted :)", Toast.LENGTH_SHORT).show();
                    cdd.dismiss();
                    finish();
                }
            }
        } );
    }
    private void LoadData() {

        storage = FirebaseStorage.getInstance();
        mainDB.child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username = dataSnapshot.child("name").getValue().toString();
                userimg = dataSnapshot.child("img").getValue().toString();
                userNameTV.setText(username);
                Picasso.get().load( userimg ).into( userImg );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        dateTimeTV.setText( getCurrentTimeStamp());
    }

    private void ChoseImage() {
        ImagePicker.Companion.with( AddUserPost.this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(512)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start(1971);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1971 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
            filePath = data.getData();
        {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                bitmap = BitmapFactory.decodeStream(getApplicationContext().getContentResolver().openInputStream(filePath));
                addImg.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static String getRandomString(int length) {
        final String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJLMNOPQRSTUVWXYZ1234567890";
        StringBuilder result = new StringBuilder();
        while(length > 0) {
            Random rand = new Random();
            result.append(characters.charAt(rand.nextInt(characters.length())));
            length--;
        }
        return result.toString();
    }

    public static String getCurrentTimeStamp() {
        Long currentTimeSt = System.currentTimeMillis()/1000;
        String ts = currentTimeSt.toString();
        return ts;
    }



}
