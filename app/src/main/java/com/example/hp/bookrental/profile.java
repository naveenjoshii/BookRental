package com.example.hp.bookrental;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class profile extends AppCompatActivity {
   EditText pName,pPhone;
   TextView pEmail,pPassword;
   private String cu;
   private Context c;
   private FirebaseAuth mAuth;
   private DatabaseReference mDef;
   private Button save,cancel;
   private ProgressDialog dp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        pName = (EditText)findViewById(R.id.etpname);
        pEmail = (TextView)findViewById(R.id.etpmail);
        pPassword = (TextView)findViewById(R.id.etPswd);
        save = (Button)findViewById(R.id.Save);
        cancel = (Button)findViewById(R.id.cancel);
        pPhone = (EditText)findViewById(R.id.etpPone);
        dp  = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
         cu = mAuth.getCurrentUser().getUid();
        mDef = FirebaseDatabase.getInstance().getReference().child("Users_info");
        mDef.child(cu).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              String name = (String)dataSnapshot.child("u_name").getValue(String.class);
               String phone  = (String)dataSnapshot.child("u_phone").getValue(String.class);
               String mail  = (String)dataSnapshot.child("u_mail").getValue(String.class);
               String psd = (String)dataSnapshot.child("u_password").getValue(String.class);
               pName.setText(name, TextView.BufferType.EDITABLE);
               pEmail.setText(mail);
               pPhone.setText(phone, TextView.BufferType.EDITABLE);
               pPassword.setText(psd);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dp.setMessage("Saving Your changes");
                dp.show();
                mDef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String n = pName.getText().toString();
                        String p = pPassword.getText().toString();
                        String e = pEmail.getText().toString();
                        String phn = pPhone.getText().toString();
                        UserDetails us = new UserDetails();
                        us.setmUserId(cu);
                        us.setU_name(n);
                        us.setU_password(p);
                        us.setU_mail(e);
                        us.setU_phone(phn);
                        mDef.child(cu).setValue(us);
                        Toast.makeText(profile.this,"Profile Succesfully Updated",Toast.LENGTH_SHORT).show();
                        dp.dismiss();
                        startActivity(new Intent(profile.this,BooksActivity.class));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    dp.dismiss();
                    }
                });
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(profile.this,BooksActivity.class));
            }
        });
        pPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(profile.this,"Not Able to change your Password",Toast.LENGTH_SHORT).show();

            }
        });
        pEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(profile.this,"As you Logged through gmail not able to change your mail",Toast.LENGTH_SHORT).show();
            }
        });


    }

}
