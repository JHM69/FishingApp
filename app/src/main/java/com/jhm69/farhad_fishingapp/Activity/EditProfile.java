package com.jhm69.farhad_fishingapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jhm69.farhad_fishingapp.Model.Angler;
import com.jhm69.farhad_fishingapp.R;
import com.jhm69.farhad_fishingapp.utils.CustomDialogeClass;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class EditProfile extends AppCompatActivity {
    private StorageReference storageReference;
    private String name, number, address, blood, pesha, fblink, img;
    private EditText nameET, numberET, peshaET, fblinkET;
    private Spinner addressET, bloodET;
    private ImageView profieImg;
    private Button save;
    private String uid;
    private CircleImageView imageView;
    private Angler angler;
    private DatabaseReference userDB;
    private Uri filePath;
    private Bitmap bitmap;
    private DatabaseReference mainDB;
    private FirebaseAuth firebaseAuth;
    private CustomDialogeClass cdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.edit_profile );
        cdd = new CustomDialogeClass(this);
        firebaseAuth = FirebaseAuth.getInstance();
        mainDB = FirebaseDatabase.getInstance().getReference();
        nameET = findViewById( R.id.name );
        numberET = findViewById( R.id.number );
        addressET = findViewById( R.id.home );
        bloodET = findViewById( R.id.blood );
        peshaET = findViewById( R.id.pesha );
        fblinkET = findViewById( R.id.facebook );
        imageView = findViewById( R.id.editProfileImage );
        save = findViewById( R.id.fab_submit_profile );
        userDB = FirebaseDatabase.getInstance().getReference();
        Intent intent = new Intent();
        String nameG = null;
        nameG = intent.getStringExtra( "1232" );

        if (nameG == null) {
            try {
                LoadData();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText( this, "Your Data loading failed.", Toast.LENGTH_SHORT ).show();
            }
        } else {
            nameET.setText( nameG );
        }

        ArrayAdapter<CharSequence> bloodAdapter = ArrayAdapter.createFromResource( getApplicationContext(),
                R.array.blood, android.R.layout.simple_spinner_item );
        bloodAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        bloodET.setAdapter( bloodAdapter );

        ArrayAdapter<CharSequence> jelaAdapter = ArrayAdapter.createFromResource( getApplicationContext(),
                R.array.district, android.R.layout.simple_spinner_item );
        bloodAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        addressET.setAdapter( jelaAdapter );

        storageReference = FirebaseStorage.getInstance().getReference();
        save.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameET.getText().toString().trim();
                number = numberET.getText().toString().trim();
                blood = bloodET.getSelectedItem().toString();
                pesha = peshaET.getText().toString().trim();
                address = addressET.getSelectedItem().toString();
                fblink = fblinkET.getText().toString().trim();
                uid = firebaseAuth.getCurrentUser().getUid();
                UploadToDataBase();

            }
        } );


        ImageView editImgnew = findViewById( R.id.editProfileNewImage );

        editImgnew.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with( EditProfile.this )
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress( 512 )
                        .cropSquare()//Final image size will be less than 1 MB(Optional)
                        .maxResultSize( 512, 512 )    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start( 101 );
            }
        } );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if (requestCode == 101 && resultCode == RESULT_OK
                && data != null && data.getData() != null)
            filePath = data.getData();
        {
            try {
                bitmap = MediaStore.Images.Media.getBitmap( getApplicationContext().getContentResolver(), data.getData() );
                bitmap = BitmapFactory.decodeStream( getApplicationContext().getContentResolver().openInputStream( filePath ) );
                //imageEncoded = encodeImage(bitmap, Bitmap.CompressFormat.PNG, 20);
                imageView.setImageBitmap( bitmap );
                //addPhotoBtn.setText("Change the Image");
                Bitmap resizedBitmap = Bitmap.createScaledBitmap( bitmap, 512, 512, false );
                imageView.setImageBitmap( resizedBitmap );
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    private void UploadToDataBase() {
        cdd.show();
        final StorageReference fileReference = storageReference.child( "PROFILE/" ).child( System.currentTimeMillis()
                + "." + "png" );
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if (filePath == null) {
            try {
                Intent in = new Intent( );
                String img = in.getStringExtra( "img" );
                angler = new Angler( blood, fblink, address, "https://firebasestorage.googleapis.com/v0/b/fishingproject-ca953.appspot.com/o/PROFILE%2F1589553179306.png?alt=media&token=075a48c7-0f66-40f0-95cc-f8781a02a36f", name, number, pesha, uid );
                DatabaseReference UserDB = mainDB.child( "user" ).child( firebaseAuth.getCurrentUser().getUid() );
                UserDB.setValue( angler );
                Toast.makeText( EditProfile.this, "Data Saved successfully", Toast.LENGTH_SHORT ).show();
                finishAffinity();
                startActivity( new Intent( EditProfile.this, MainActivity.class) );
                cdd.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText( this, "Error...", Toast.LENGTH_SHORT ).show();
                cdd.dismiss();
            }
        } else {
            bitmap.compress( Bitmap.CompressFormat.PNG, 20, baos );
            byte[] fileInBytes = baos.toByteArray();

            UploadTask uploadTask = (UploadTask) fileReference.putBytes( fileInBytes )
                    .addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileReference.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(final Uri uri) {
                                    angler = new Angler( blood, fblink, address, uri.toString().trim(), name, number, pesha, uid );
                                    DatabaseReference UserDB = mainDB.child( "user" ).child( firebaseAuth.getCurrentUser().getUid() );
                                    UserDB.setValue( angler );
                                    Toast.makeText( EditProfile.this, "Data Saved successfully", Toast.LENGTH_SHORT ).show();
                                    cdd.dismiss();
                                    finishAffinity();
                                    Intent goToMarke = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(goToMarke);
                                }
                            } );


                        }
                    } );
        }
    }

    private void LoadData() {
        String userID = firebaseAuth.getCurrentUser().getUid();
        mainDB.child( "user" ).child( userID ).addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nameET.setText( dataSnapshot.child( "name" ).getValue().toString() );
                peshaET.setText( dataSnapshot.child( "pesha" ).getValue().toString() );
                numberET.setText( dataSnapshot.child( "number" ).getValue().toString() );
                bloodET.setPrompt( dataSnapshot.child( "blood" ).getValue().toString() );
                fblinkET.setText( dataSnapshot.child( "fb" ).getValue().toString() );
                Picasso.get().load((dataSnapshot.child( "img" ).getValue().toString()) ).into( imageView );
                addressET.setPrompt( dataSnapshot.child( "home" ).getValue().toString() );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        } );
    }

}
