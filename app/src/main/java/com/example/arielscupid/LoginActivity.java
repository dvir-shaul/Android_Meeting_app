package com.example.arielscupid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    EditText eMail;
    EditText ePassword;
    android.widget.Button eLogin;
    android.widget.Button eLogin2;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    ProgressDialog progressDialog;

    String emailPattern = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$" ;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eMail = findViewById(R.id.etMail);
        ePassword = findViewById(R.id.etPassword);
        eLogin = findViewById(R.id.btnLogin);
        eLogin2 = findViewById(R.id.btnLogin2);

        progressDialog = new ProgressDialog(this);

        firebaseAuth=FirebaseAuth.getInstance();

        firebaseUser=firebaseAuth.getCurrentUser();


        eLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perforLogin();
            }
        });

        eLogin2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            perforLogin();
        }
    });
}

    private void perforLogin() {

        String email = eMail.getText().toString();
        String password = ePassword.getText().toString();

        if (email.matches(emailPattern))
        {
            eMail.setError("Enter correct email pattern");
        }
        else if (password.isEmpty() || password.length() < 6)
        {
            ePassword.setError("Enter at least 6 digits to password");
        }
        else
            {
            progressDialog.setMessage("Please wait for login...");
            progressDialog.setTitle("Login as admin");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                            Intent intent= new Intent(LoginActivity.this, chatActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
            });
        }
    }
}