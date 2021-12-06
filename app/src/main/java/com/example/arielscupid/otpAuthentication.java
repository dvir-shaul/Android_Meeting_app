package com.example.arielscupid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class otpAuthentication extends AppCompatActivity {

    EditText inputEmail,inputPasswrod,inputConfirmPasword;
    android.widget.Button btnRegister;
    android.widget.Button goBack;

    String emailPattern = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$" ;
    ProgressDialog progressDialog;



    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_authentication);
        goBack=findViewById(R.id.gobacktohomepage);
        btnRegister=findViewById(R.id.verifyotp);
        inputEmail=findViewById(R.id.getEmail);
        inputPasswrod=findViewById(R.id.getpass);
        inputConfirmPasword=findViewById(R.id.getpass2);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();


        progressDialog = new ProgressDialog(this);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackFunc();
            }
        });



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PergorAuth();
            }
        });
    }

    private void goBackFunc() {
        Intent intent = new Intent(otpAuthentication.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void PergorAuth() {
        String email = inputEmail.getText().toString();
        String password = inputPasswrod.getText().toString();
        String confirmPassword = inputConfirmPasword.getText().toString();

        if(email.matches(emailPattern))
        {
            inputEmail.setError("Enter correct email pattern");
        }
        else if(password.isEmpty() || password.length()<6)
        {
            inputPasswrod.setError("Enter at least 6 digits to password");
        }
        else if(!password.equals(confirmPassword))
        {
            inputConfirmPasword.setError("Password Not match");
        }
        else{
            progressDialog.setMessage("Please wait while Registration...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(otpAuthentication.this, "Registration successfully", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(otpAuthentication.this, setProfile.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(otpAuthentication.this, ""+task.getException(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }
}