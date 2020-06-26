package com.jhm69.farhad_fishingapp.Adapters;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.jhm69.farhad_fishingapp.Activity.MainActivity;
import com.jhm69.farhad_fishingapp.Activity.MyProfileActivity;
import com.jhm69.farhad_fishingapp.Model.UserPost;
import com.jhm69.farhad_fishingapp.R;
import com.jhm69.farhad_fishingapp.Activity.UserPostDetails;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.jhm69.farhad_fishingapp.fragments.UserPostFragment.deleteUserPost;
import static com.jhm69.farhad_fishingapp.fragments.UserPostFragment.getUidString;
import static com.jhm69.farhad_fishingapp.fragments.UserPostFragment.getUserCurrent;

public class UserPostAdapter extends RecyclerView.Adapter<UserPostAdapter.MyViewHolder> {

    private List<UserPost>notices;
    private Context context;
    private FirebaseAuth firebaseAuth;

    public UserPostAdapter(List<UserPost>notices, Context context){

        this.notices = notices;
        this.context=context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.user_post_sample,viewGroup,false);
        return new MyViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        final UserPost userPost = new UserPost(notices.get( i ).getUserId(),notices.get( i ).getImage(),notices.get( i ).getName(),notices.get( i ).getDate(),notices.get( i ).getText(),notices.get( i ).getImage(), notices.get( i ).getLocation() ,notices.get( i ).getId());
        Picasso.get().load( notices.get( i ).getUserImg() ).into( viewHolder.userIMGP );
        viewHolder.Myname.setText(userPost.getName());
        viewHolder.MyDate.setText(getDateFromTimeStamp(userPost.getDate()));
        viewHolder.MyBody.setText(userPost.getText());
        firebaseAuth = FirebaseAuth.getInstance();

        if(userPost.getLocation().equals( "" )){
            viewHolder.locationTV.setVisibility( View.GONE );
        }else {
            viewHolder.locationTV.setVisibility( View.VISIBLE );
            viewHolder.locationTV.setText( "is at "+userPost.getLocation() );
        }

        final String imglink = userPost.getImage();
        if(imglink.equals("0")){
            viewHolder.Myimageurl.setVisibility( View.GONE );
        }else{
            Picasso.get().load( imglink ).into( viewHolder.Myimageurl );
        }
        viewHolder.next.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), UserPostDetails.class);
                i.putExtra("mm", userPost );
                v.getContext().startActivity(i);
            }
        } );
        viewHolder.Myname.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(v.getContext(), MyProfileActivity.class );
                in.putExtra("uid",userPost.getUserId() );
                v.getContext().startActivity(in);
            }
        } );
        viewHolder.locationTV.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openMapView( userPost.getLocation(), v.getContext() );
            }
        } );

        if(getUserCurrent()){
            if(getUidString().equals( userPost.getUserId() )){
                viewHolder.menu.setVisibility( View.VISIBLE );
                viewHolder.menu.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setMessage("Are you sure want to Delete this post?") .setTitle("Delete?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        deleteUserPost(userPost.getId(), v.getContext());
                                        notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                } );
            }else{
                viewHolder.menu.setVisibility( View.GONE );
            }
        }


    }

    @Override
    public int getItemCount() {
        return notices.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Myname,MyBody,MyDate, locationTV;
        ImageView Myimageurl, userIMGP;
        CardView next, menu;
        public MyViewHolder(View itemView) {
            super(itemView);
            next = itemView.findViewById(R.id.nextP);
            menu = itemView.findViewById(R.id.menu);
            Myname =(TextView)itemView.findViewById(R.id.text_name);
            MyDate =(TextView)itemView.findViewById(R.id.date);
            MyBody = (TextView)itemView.findViewById(R.id.bodyTV );
            Myimageurl = (ImageView) itemView.findViewById(R.id.image_url);
            userIMGP = (ImageView) itemView.findViewById(R.id.pro_imagen);
            locationTV = (TextView) itemView.findViewById(R.id.text_location);
        }
    }

    public static String getDateFromTimeStamp(String timeStamp) {
        long time = Long.parseLong( timeStamp );
        Calendar cal = Calendar.getInstance( Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("HH:mm, dd-MM-yyyy", cal).toString();
        return date;
    }
    private void openMapView(String locationName, Context context){
        String map = "http://maps.google.com.bd/maps?q=" + locationName;
        try {
            Intent sharingIntent = new Intent(Intent.ACTION_VIEW);
            sharingIntent.setData( Uri.parse(map));
            sharingIntent.setPackage("com.google.android.apps.maps");
            context.startActivity(sharingIntent);
        } catch (ActivityNotFoundException e) {
            Uri uri = Uri.parse(map);
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(goToMarket);
        }


    }

}
