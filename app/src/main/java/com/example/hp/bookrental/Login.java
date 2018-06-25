package com.example.hp.bookrental;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private EditText emailField, passwordField;
    private Button loginBtn;
    private TextView signUp,forgotPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private String password,p;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(Login.this,BooksActivity.class));
            finish();
        }
        emailField = (EditText)findViewById(R.id.emailField);
        passwordField=(EditText)findViewById(R.id.passwordField);
        loginBtn=(Button)findViewById(R.id.btn);
        signUp = (TextView)findViewById(R.id.register);
        forgotPassword = (TextView)findViewById(R.id.forgot_pswd);
        progressDialog=new ProgressDialog(Login.this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,signUpActivity.class));
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,passwordResetActivity.class));
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString();
                 password = passwordField.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setMessage("Signing In.....");
                progressDialog.show();
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    if (password.length() < 6) {
                                        passwordField.setError("Password too Short ,enter minimum 6 charcters! atleast 1 number");
                                    } else {
                                        Toast.makeText(Login.this, "LoginFailed Please check your Email & Password", Toast.LENGTH_LONG).show();
                                    }
                                    progressDialog.dismiss();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(Login.this,"Login Succesfull",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Login.this, BooksActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });

    }

}
