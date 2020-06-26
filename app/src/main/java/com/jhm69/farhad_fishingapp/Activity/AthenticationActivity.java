package com.jhm69.farhad_fishingapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jhm69.farhad_fishingapp.R;

public class AthenticationActivity extends AppCompatActivity {

    EditText numberEt;
    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_athentication );

        Window window = this.getWindow();
        window.clearFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor( ContextCompat.getColor(this,R.color.SRA));

        numberEt = findViewById( R.id.editText );
        next = findViewById( R.id.button );
        next.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = numberEt.getText().toString().trim();
                if(mobile.isEmpty() || mobile.length() < 10){
                    numberEt.setError("Enter a valid mobile");
                    numberEt.requestFocus();
                    return;
                }

                Intent intent = new Intent(AthenticationActivity.this, VerifyActivity.class);
                intent.putExtra("mob", "+88"+mobile);
                startActivity(intent);
            }
        } );
    }
}
