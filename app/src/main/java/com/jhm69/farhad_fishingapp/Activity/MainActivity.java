package com.jhm69.farhad_fishingapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jhm69.farhad_fishingapp.Admin.AdminMainPage;
import com.jhm69.farhad_fishingapp.R;
import com.jhm69.farhad_fishingapp.Adapters.ViewPagerAdapter;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private TextView hName, hAdd;
    private CircleImageView hImage;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private String img, userID;
    private DatabaseReference mainDb;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);
        Window window = this.getWindow();

        window.clearFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor( ContextCompat.getColor(this,R.color.status));

        final BubbleNavigationLinearView bubbleNavigationLinearView = findViewById(R.id.bottom_navigation_view_linear);
        bubbleNavigationLinearView.setTypeface(Typeface.createFromAsset(getAssets(), "medium.ttf"));

        bubbleNavigationLinearView.setBadgeValue(0, null);
        bubbleNavigationLinearView.setBadgeValue(1, null); //invisible badge
        bubbleNavigationLinearView.setBadgeValue(2, null);
        bubbleNavigationLinearView.setBadgeValue(3, null);

        firebaseAuth = FirebaseAuth.getInstance();
        mainDb = FirebaseDatabase.getInstance().getReference();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        final ViewPager viewPager = findViewById(R.id.view_pager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //create default navigation drawer toggle

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.app_name);
        drawer.setBackgroundResource(R.color.colorPrimary);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.tab_text));
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View header = navigationView.getHeaderView(0);
        Button loginBtn = header.findViewById( R.id.login_head );
        hImage = header.findViewById( R.id.userimg );
        hName= header.findViewById( R.id.user_name );
        hAdd= header.findViewById( R.id.address );


        FloatingActionButton editBtn = header.findViewById( R.id.edit_pro );
        if(firebaseAuth.getCurrentUser() == null) {
            loginBtn.setVisibility( View.VISIBLE );
            loginBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( MainActivity.this, AthenticationActivity.class );
                    startActivity( intent );
                }
            } );

        }else{
            userID = firebaseAuth.getCurrentUser().getUid();
            loadData();
            editBtn.setVisibility( View.VISIBLE );
            loginBtn.setVisibility( View.GONE );
            editBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( MainActivity.this, EditProfile.class );
                    intent.putExtra( "img",  img  );
                    startActivity( intent );
                }
            } );
        }

        ViewPagerAdapter pagerAdapter2 = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter2);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                bubbleNavigationLinearView.setCurrentActiveItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        bubbleNavigationLinearView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                viewPager.setCurrentItem(position, true);
            }
        });

    }

    public void loadData(){

        mainDb.child("user").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String present = dataSnapshot.child("home").getValue().toString();
                img = dataSnapshot.child("img").getValue().toString();
                hName.setText(name);
                hAdd.setText(present);
                hImage.setVisibility(View.VISIBLE);
                Picasso.get().load( img ).into( hImage );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.logg:
                if(firebaseAuth.getCurrentUser()==null){
                    Toast.makeText( this, "You are not logged In!", Toast.LENGTH_SHORT ).show();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder( this );
                    builder.setMessage( "Are you sure want to log out?" ).setTitle( "Log Out" )
                            .setCancelable( false )
                            .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    FirebaseAuth.getInstance().signOut();
                                    finishAffinity();
                                    Intent goToMarke = new Intent( getApplicationContext(), MainActivity.class );
                                    startActivity( goToMarke );
                                }
                            } )
                            .setNegativeButton( "No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                }
                            } );
                    AlertDialog alert = builder.create();
                    alert.setTitle( "" );
                    alert.show();
                }
                break;

            case R.id.notification:
                Intent nnt = new Intent(getApplicationContext(), AnglersList .class);
                startActivity(nnt);
                break;

            case R.id.about_dev:

                Intent intent = new Intent(getApplicationContext(), AboutDeveloper .class);
                startActivity(intent);



                break;

            case R.id.admin:
                Intent ntent = new Intent(getApplicationContext(), AdminMainPage.class);
                startActivity(ntent);
                break;
            case R.id.myprofile:
                if(firebaseAuth.getCurrentUser() == null){
                    Intent nten = new Intent(getApplicationContext(), AthenticationActivity.class);
                    startActivity(nten);
                }else{
                    Intent inten = new Intent(getApplicationContext(), MyProfileActivity.class);
                    inten.putExtra("uid",userID );
                    startActivity(inten);
                }

                break;

            case R.id.exit:
                finish();
                break;
        }
        return true;
    }



}
