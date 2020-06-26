package com.jhm69.farhad_fishingapp.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.jhm69.farhad_fishingapp.Model.Angler;
import com.jhm69.farhad_fishingapp.Model.Collection;
import com.jhm69.farhad_fishingapp.Model.UserPost;
import com.jhm69.farhad_fishingapp.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import static com.jhm69.farhad_fishingapp.Admin.FragmentAddPost.getRandomString;

public class addCollection extends DialogFragment implements View.OnClickListener {

    private EditText model, sub;
    private Spinner addcoll;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mainDB;
    private String type;
    private Button add;
    public static addCollection newInstance() {
        addCollection newaddCollection = new addCollection();
        return newaddCollection;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate( R.layout.add_collection, container, false );
        firebaseAuth = FirebaseAuth.getInstance();
        mainDB = FirebaseDatabase.getInstance().getReference();
        model = rootView.findViewById( R.id.modelET );
        sub = rootView.findViewById( R.id.subtitleET );
        addcoll = rootView.findViewById( R.id.addcollsp );
        add = rootView.findViewById( R.id.postBtn );

        ArrayAdapter<CharSequence> bloodAdapter = ArrayAdapter.createFromResource( getActivity(),
                R.array.type, android.R.layout.simple_spinner_item );
        addcoll.setAdapter( bloodAdapter );
        bloodAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        addcoll.setPrompt( "Select Type" );



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

        add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = addcoll.getSelectedItem().toString();
                Collection collection = new Collection( model.getText().toString(), sub.getText().toString(),type );
                uploadToDatabase( collection );
            }
        } );


       // (getView().findViewById( R.id.postBtn )).setOnClickListener( this );
        //(getView().findViewById( R.id.cancelBtn )).setOnClickListener( this );
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
    }
    private void uploadToDatabase(Collection collection) {
        DatabaseReference mainPosts = mainDB.child( "user" );
        mainPosts.child( firebaseAuth.getCurrentUser().getUid()).child("collection").push().setValue( collection )
                .addOnCompleteListener( new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getActivity(), "Added :)", Toast.LENGTH_SHORT).show();
                }
            }
        } );
    }
}