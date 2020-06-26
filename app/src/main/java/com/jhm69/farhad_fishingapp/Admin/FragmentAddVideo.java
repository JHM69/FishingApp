package com.jhm69.farhad_fishingapp.Admin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jhm69.farhad_fishingapp.Model.Video;
import com.jhm69.farhad_fishingapp.R;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.jhm69.farhad_fishingapp.Activity.AddUserPost.getCurrentTimeStamp;
import static com.jhm69.farhad_fishingapp.Admin.FragmentAddPost.getRandomString;

/**
 * Created by Jahangir
 */
public class FragmentAddVideo extends DialogFragment implements View.OnClickListener {
    private ProgressDialog progressDialog;
    private DatabaseReference mainDB;
    private TextView userNameTV, dateTimeTV;
    private Button addPhotoBtn, cancelBtn, postBtn;
    private EditText postET, titleText, linkET;
    private ImageView userImg, addImg;
    private DateUtils dateUtils;
    private Date selectedDate;
    private String imageLink;
    private String time, todaysDate;
    private String username, userimg;
    private FirebaseAuth firebaseAuth;
    private Uri filePath;
    private List<String> categories;
    private String uid;
    private FirebaseStorage storage;
    private StorageReference NoticesReference;
    private final int PICK_IMAGE_REQUEST = 4;
    private Bitmap bitmap;
    private Button category;
    // private Income mIncome;
    // IncomeAdapter ad ;
    private UploadTask.TaskSnapshot downloadUri;
    private String imageEncoded;

    static FragmentAddVideo newInstance() {
        FragmentAddVideo newFragmentAddVideo = new FragmentAddVideo();
        return newFragmentAddVideo;
    }

    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate( R.layout.add_video, container, false );
        NoticesReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        mainDB = FirebaseDatabase.getInstance().getReference();

        uid = getRandomString(13);
        addImg = rootView.findViewById( R.id.addImg );
        dateTimeTV = rootView.findViewById( R.id.dateTimeTV );
        addPhotoBtn = rootView.findViewById( R.id.addPhotoBtn );
        cancelBtn = rootView.findViewById( R.id.cancelBtn );
        postBtn = rootView.findViewById( R.id.postBtn );
        category = rootView.findViewById( R.id.addCatagory );
        postET = rootView.findViewById( R.id.postTextET );
        titleText =rootView.findViewById( R.id.TitleTextET );
        linkET= rootView.findViewById( R.id.linkET );
        listItems = getResources().getStringArray( R.array.category );
        checkedItems = new boolean[listItems.length];

        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog( savedInstanceState );
        dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );


        storage = FirebaseStorage.getInstance();

        time = new SimpleDateFormat( "HH:mm", Locale.getDefault() ).format( new Date() );
        todaysDate = new SimpleDateFormat( "dd-MM-YYYY", Locale.getDefault() ).format( new Date() );
        dateTimeTV.setText( time + ", " + todaysDate );

        category.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder( getActivity());
                mBuilder.setTitle( "Select Category" );
                mBuilder.setMultiChoiceItems( listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
//                        if (isChecked) {
//                            if (!mUserItems.contains(position)) {
//                                mUserItems.add(position);
//                            }
//                        } else if (mUserItems.contains(position)) {
//                            mUserItems.remove(position);
//                        }
                        if (isChecked) {
                            mUserItems.add( position );
                        } else {
                            mUserItems.remove( (Integer.valueOf( position )) );
                        }
                    }
                } );

                mBuilder.setCancelable( false );
                mBuilder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for (int i = 0; i < mUserItems.size(); i++) {
                            item = item + listItems[mUserItems.get( i )];
                            if (i != mUserItems.size() - 1) {
                                item = item + ", ";
                            }
                        }
                        category.setText(item);
                        categories= Arrays.asList(item.split(","));
                    }
                } );

                mBuilder.setNegativeButton( "Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                } );

                mBuilder.setNeutralButton( "Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                            mUserItems.clear();
                            category.setText( "" );
                        }
                    }
                } );

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        } );


        addPhotoBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        } );


        (getView().findViewById( R.id.postBtn )).setOnClickListener( this );
        (getView().findViewById( R.id.cancelBtn )).setOnClickListener( this );
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.addPhotoBtn) {

        }else if (view.getId() == R.id.postBtn) {
            if(categories.size() == 0){
                Toast.makeText( getActivity(), "You must select a category", Toast.LENGTH_SHORT ).show();
            }else if( filePath == null ){
                Toast.makeText( getActivity(), "Thumb cant be empty", Toast.LENGTH_SHORT ).show();
            }else{
                uploadFile();
            }
        }
        else if (view.getId() == R.id.cancelBtn) {
            dismiss();
        }
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Thumb Pic"), PICK_IMAGE_REQUEST);
    }



    private void uploadToDatabase(Video video, String item) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        DatabaseReference mainPosts = mainDB.child( "Video" ).child( "All" );
        mainPosts.child( uid ).setValue( video );
        DatabaseReference classUpDB = mainDB.child( "Video" ).child(item);
        classUpDB.child(uid).setValue( video ).addOnCompleteListener( new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    dismiss();
                    progressDialog.dismiss();
                }
            }
        } );
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
            filePath = data.getData();
        {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(filePath));
                bitmap = Bitmap.createScaledBitmap(bitmap, 1280, 720, false);
                addImg.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }



    private void uploadFile() {

        final StorageReference fileReference = NoticesReference.child( "THUMBS/" ).child( System.currentTimeMillis()
                + "." + "png" );
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //here you can choose quality factor in third parameter(ex. i choosen 25)
        bitmap.compress( Bitmap.CompressFormat.PNG, 20, baos );
        byte[] fileInBytes = baos.toByteArray();
        progressDialog =new ProgressDialog( getActivity() );
        progressDialog.show();
        progressDialog.setTitle( "Uploading.... ");
        UploadTask uploadTask = (UploadTask) fileReference.putBytes( fileInBytes )
                .addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        fileReference.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri uri) {
                                Toast.makeText( getActivity(), "Upload Succesfull", Toast.LENGTH_SHORT ).show();
                                for(int i=0; i<categories.size();i++ ) {
                                    Video video = new Video(uid,titleText.getText().toString(),
                                            getCurrentTimeStamp(), postET.getText().toString(),
                                            uri.toString().trim(), linkET.getText().toString(),
                                            categories.get( i ) );
                                    uploadToDatabase( video, categories.get( i ) );
                                    progressDialog.dismiss();
                                }
                            }
                        } );


                    }
                } );
    }
}