package com.jhm69.farhad_fishingapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jhm69.farhad_fishingapp.Model.Angler;
import com.jhm69.farhad_fishingapp.R;
import com.jhm69.farhad_fishingapp.utils.CustomDialogeClass;

import java.util.concurrent.TimeUnit;

public class VerifyActivity extends AppCompatActivity {
    private String verificationId;
    private FirebaseAuth firebaseAuth;
    private EditText otpEt;
    private ProgressBar progressBar;
    private DatabaseReference mainDB;
    private CustomDialogeClass cdd;
    private String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_verify );
        Window window = this.getWindow();
        window.clearFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor( ContextCompat.getColor(this,R.color.SRA));

        firebaseAuth = FirebaseAuth.getInstance();
        otpEt = findViewById( R.id.editText );
        final Button next = findViewById( R.id.button );
        cdd = new CustomDialogeClass( this );
        mainDB = FirebaseDatabase.getInstance().getReference();
        progressBar = findViewById(R.id.progressBar);

        number = getIntent().getStringExtra( "mob" );
        sendVerificationCode(number);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = otpEt.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    otpEt.setError("code must be 6 character long...");
                    otpEt.requestFocus();
                    return;
                }
                try {
                    verifyCode(code);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText( VerifyActivity.this, "en error occurs. go back and try again", Toast.LENGTH_SHORT ).show();
                }

            }
        });


    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        cdd.show();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseAuth = FirebaseAuth.getInstance();
                            DatabaseReference userDB = mainDB.child("user").child(firebaseAuth.getCurrentUser().getUid());
                            Angler angler = new Angler( "","","","https://firebasestorage.googleapis.com/v0/b/fishingproject-ca953.appspot.com/o/PROFILE%2F1589553179306.png?alt=media&token=075a48c7-0f66-40f0-95cc-f8781a02a36f","",number , "",firebaseAuth.getCurrentUser().getUid());
                            userDB.setValue(angler);
                            cdd.dismiss();
                            Intent intent = new Intent(VerifyActivity.this, EditProfile.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(VerifyActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
        progressBar.setVisibility(View.GONE);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            Toast.makeText( VerifyActivity.this, "check message.", Toast.LENGTH_SHORT ).show();
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                otpEt.setText(code);
                verifyCode(code);
            }
        }


        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }
    };

}
