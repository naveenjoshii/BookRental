package com.example.hp.bookrental;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class signUpActivity extends AppCompatActivity {

        private EditText userName,userPassword,UserEmail,userPhone,userAadhar;
        private Button regButton;
        private TextView userLogin;
        private FirebaseAuth firebaseAuth;
        private ProgressDialog pb;

         private String id,name,passwordd,email,phone,Aadhar;
    public static final String Firebase_Server_URL = "https://bookrental-77a54.firebaseio.com/";
    Firebase firebase;
    DatabaseReference databaseReference;

    // Root Database Name for Firebase Database.
    public static final String Database_Path = "Users_info";

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sign_up);
            Firebase.setAndroidContext(signUpActivity.this);
            firebase = new Firebase(Firebase_Server_URL);
            databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
            setupUIViews();
            firebaseAuth=FirebaseAuth.getInstance();
            pb=new ProgressDialog(signUpActivity.this);
            regButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if(Validate()) {
                        pb.setMessage("Registering...:-)");
                        pb.show();
                        firebaseAuth.createUserWithEmailAndPassword(email,passwordd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    insert();
                                    sendEmailVerification();
                                    pb.dismiss();
                                }else{
                                    Toast.makeText(signUpActivity.this,"Registration Failed",Toast.LENGTH_SHORT).show();
                                    pb.dismiss();
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

    private void insert() {
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        UserDetails userDetails = new UserDetails();
        userDetails.setmUserId(currentuser);
        userDetails.setU_name(name);
        userDetails.setU_mail(email);
        userDetails.setU_password(passwordd);
        userDetails.setU_phone(phone);
        userDetails.setU_aadhar(Aadhar);
        String UserRecordIDFromServer = currentuser;
        databaseReference.child(UserRecordIDFromServer).setValue(userDetails);


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
             name = userName.getText().toString();
            passwordd = userPassword.getText().toString();
            email = UserEmail.getText().toString();
            phone  = userPhone.getText().toString();
            Aadhar  = userAadhar.getText().toString();
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
