package com.jhm69.farhad_fishingapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jhm69.farhad_fishingapp.Activity.AthenticationActivity;
import com.jhm69.farhad_fishingapp.Adapters.UserPostAdapter;
import com.jhm69.farhad_fishingapp.Model.UserPost;
import com.jhm69.farhad_fishingapp.R;
import com.jhm69.farhad_fishingapp.Activity.AddUserPost;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserPostFragment extends Fragment {
    private DatabaseReference mainDB;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private UserPostAdapter recycleAdapterUserPost;
    private List<UserPost> allNotices;
    private FloatingActionButton addNotice;
    private ShimmerFrameLayout shimmerFrameLayout;
    private static FirebaseAuth firebaseAuth;
    public static FragmentActivity myContextu;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_user_post, container, false);
        addNotice =view.findViewById(R.id.addNotice);
        firebaseAuth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById( R.id.recycle_view );
        layoutManager = new LinearLayoutManager( getActivity() );
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize( true );
        allNotices = new ArrayList<>();
        mainDB = FirebaseDatabase.getInstance().getReference();
        shimmerFrameLayout = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmerAnimation();

        getAllNotices();
        addNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firebaseAuth.getCurrentUser() == null){
                    Toast.makeText( getActivity(), "পোস্ট করতে হলে অবশ্যই একাউন্ট থাকতে হবে।", Toast.LENGTH_SHORT ).show();
                    Intent intent = new Intent( getActivity(), AthenticationActivity.class );
                    startActivity( intent );
                }else{
                    Intent intent = new Intent( getActivity(), AddUserPost.class );
                    startActivity( intent );
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        myContextu=(FragmentActivity) context;
        super.onAttach( context );
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private void getAllNotices() {

         DatabaseReference ref = mainDB.child("user_post");
         Query query = ref.orderByChild( "date" ).limitToLast(13);
         query.addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserPost newnotice = dataSnapshot.getValue(UserPost.class);
                allNotices.add(newnotice);
                recycleAdapterUserPost = new UserPostAdapter(allNotices,getActivity());
                recyclerView.setAdapter(recycleAdapterUserPost);
                recycleAdapterUserPost.notifyDataSetChanged();
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);

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
    public static void deleteUserPost(String id, final Context context){
        DatabaseReference mainPosts = FirebaseDatabase.getInstance().getReference().child( "user_post" );
        mainPosts.child(id).removeValue().addOnCompleteListener( new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText( context, "Deleted", Toast.LENGTH_SHORT ).show();
                refreshFragment();
            }
        } );
    }

    public static String getUidString(){
        return firebaseAuth.getCurrentUser().getUid();
    }
    public static boolean getUserCurrent(){
        if ( firebaseAuth.getCurrentUser() == null){
            return false;
        }else{
            return true;
        }
    }
    public static void refreshFragment(){
                UserPostFragment fragment = (UserPostFragment) myContextu.getSupportFragmentManager().getFragments().get(1);
                myContextu.getSupportFragmentManager().beginTransaction()
                        .detach(fragment)
                        .attach(fragment)
                        .commit();
    }


}
