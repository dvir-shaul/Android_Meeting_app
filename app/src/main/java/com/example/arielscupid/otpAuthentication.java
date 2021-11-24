package com.example.arielscupid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class otpAuthentication extends AppCompatActivity {

    TextView mchangenumber;
    EditText mgetotp;
    android.widget.Button mverifyotp;
    String enterotp;

    FirebaseAuth firebaseAuth;
    ProgressBar mprogressbarofothauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_authentication);

        mchangenumber=findViewById(R.id.changenumber);
        mverifyotp=findViewById(R.id.verifyotp);
        mgetotp=findViewById(R.id.getotp);
        mprogressbarofothauth=findViewById(R.id.progressbarofotpauth);

        firebaseAuth=FirebaseAuth.getInstance();
        mchangenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(otpAuthentication.this,MainActivity.class);
                startActivity(intent);
            }
        });

        mverifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterotp=mgetotp.getText().toString();
                if(enterotp.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Enter your otp first",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mprogressbarofothauth.setVisibility(View.VISIBLE);
                    String coderecived=getIntent().getStringExtra("otp");

                    PhoneAuthCredential credential= PhoneAuthProvider.getCredential(coderecived,enterotp);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                    {
                    mprogressbarofothauth.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"Login Success", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(otpAuthentication.this, setProfile.class);
                    startActivity(intent);
                    finish();
                    }
                else{
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                        {
                            mprogressbarofothauth.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(),"Login Failed", Toast.LENGTH_SHORT).show();
                        }
                }
            }
        });
    }




}