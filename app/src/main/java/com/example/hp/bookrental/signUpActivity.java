package com.example.hp.bookrental;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signUpActivity extends AppCompatActivity {

        private EditText userName,userPassword,UserEmail,userPhone,userAadhar;
        private Button regButton;
        private TextView userLogin;
        private FirebaseAuth firebaseAuth;

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sign_up);
            setupUIViews();
            firebaseAuth=FirebaseAuth.getInstance();
            regButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if(Validate()) {
                        String user_email = UserEmail.getText().toString().trim();
                        String user_password = userPassword.getText().toString().trim();
                        firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    sendEmailVerification();
                                }else{
                                    Toast.makeText(signUpActivity.this,"Registration Failed",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }
                }
            });
            userLogin.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    startActivity(new Intent(signUpActivity.this,Login.class));
                }
            });
        }
        private void setupUIViews() {
            userName =(EditText)findViewById(R.id.etUser);
            userPassword=(EditText)findViewById(R.id.etPassword);
            UserEmail =(EditText)findViewById(R.id.etUserEmail);
            regButton=(Button)findViewById(R.id.Register);
            userLogin=(TextView)findViewById(R.id.tv3);
            userPhone=(EditText)findViewById(R.id.etPhone);
            userAadhar=(EditText)findViewById(R.id.etAadhar);
        }
        private Boolean Validate() {
            Boolean result =false;
            String name = userName.getText().toString();
            String passwordd = userPassword.getText().toString();
            String email = userPassword.getText().toString();
            String phone  = userPhone.getText().toString();
            String Aadhar  = userAadhar.getText().toString();
            if(name.isEmpty()||passwordd.isEmpty()||email.isEmpty()||phone.isEmpty()||Aadhar.isEmpty()) {
                Toast.makeText(this, "Please Enter all the details...", Toast.LENGTH_SHORT).show();
            }else {
                result =true;
            }
            return result;
        }
        private void sendEmailVerification(){
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if(firebaseUser!=null){
                firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Successfully Registered Verification Email sent",Toast.LENGTH_SHORT).show();
                            firebaseAuth.signOut();
                            finish();
                            startActivity(new Intent(signUpActivity.this,Login.class));

                        }else{
                            Toast.makeText(signUpActivity.this,"Verification has'nt sent",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
