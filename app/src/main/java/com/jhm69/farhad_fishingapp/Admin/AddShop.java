package com.jhm69.farhad_fishingapp.Admin;

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
import com.jhm69.farhad_fishingapp.Activity.EditProfile;
import com.jhm69.farhad_fishingapp.Activity.MainActivity;
import com.jhm69.farhad_fishingapp.Model.Angler;
import com.jhm69.farhad_fishingapp.Model.Store;
import com.jhm69.farhad_fishingapp.R;
import com.jhm69.farhad_fishingapp.utils.CustomDialogeClass;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import static com.jhm69.farhad_fishingapp.Activity.AddUserPost.getRandomString;

public class AddShop extends AppCompatActivity {
    private StorageReference storageReference;
    private String name, number, address, decs, img;
    private EditText nameET, numberET, decsET ;
    private EditText addressET;
    private ImageView profieImg;
    private Button save;
    private String uid;
    private CircleImageView imageView;
    private Store store;
    private DatabaseReference userDB;
    private Uri filePath;
    private Bitmap bitmap;
    private DatabaseReference mainDB;
    private FirebaseAuth firebaseAuth;
    private CustomDialogeClass cdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_shop );

        cdd = new CustomDialogeClass(this);
        firebaseAuth = FirebaseAuth.getInstance();
        uid = getRandomString( 13 );
        mainDB = FirebaseDatabase.getInstance().getReference();
        nameET = findViewById( R.id.name );
        numberET = findViewById( R.id.number );
        addressET = findViewById( R.id.home );
        decsET = findViewById( R.id.description );
        imageView = findViewById( R.id.editProfileImage );
        save = findViewById( R.id.fab_submit_profile );
        userDB = FirebaseDatabase.getInstance().getReference();

        storageReference = FirebaseStorage.getInstance().getReference();
        save.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameET.getText().toString().trim();
                number = numberET.getText().toString().trim();
                String decs = decsET.getText().toString();
                address = addressET.getText().toString();
                UploadToDataBase();
            }
        } );
        ImageView editImgnew = findViewById( R.id.editProfileNewImage );

        editImgnew.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with( AddShop.this )
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress( 512 )
                        .cropSquare()//Final image size will be less than 1 MB(Optional)
                        .maxResultSize( 512, 512 )    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start( 564 );
            }
        } );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if (requestCode == 564 && resultCode == RESULT_OK
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
        final StorageReference fileReference = storageReference.child( "SHOP/" ).child( System.currentTimeMillis()
                + "." + "png" );
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if (filePath == null) {
            try {

                store = new Store(name,decs,address,"https://firebasestorage.googleapis.com/v0/b/fishingproject-ca953.appspot.com/o/PROFILE%2F1589553179306.png?alt=media&token=075a48c7-0f66-40f0-95cc-f8781a02a36f",number,uid );
                DatabaseReference UserDB = mainDB.child( "store" ).child( uid );
                UserDB.setValue( store );
                Toast.makeText( AddShop.this, "Data Saved successfully", Toast.LENGTH_SHORT ).show();
                finishAffinity();
                startActivity( new Intent( AddShop.this, MainActivity.class) );
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
                                    store = new Store(name,decs,address,uri.toString().trim(),number,uid );
                                    DatabaseReference UserDB = mainDB.child( "store" ).child( uid );
                                    UserDB.setValue( store );
                                    Toast.makeText( AddShop.this, "Data Saved successfully", Toast.LENGTH_SHORT ).show();
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

}
