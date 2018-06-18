package com.example.hp.bookrental;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class sellerDetails extends AppCompatActivity {
    private TextView sPhone,sEmail;
    private String seller_phone,seller_mail;
    private FloatingActionButton fab2,fab3;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_details);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users_info");
        sPhone = (TextView) findViewById(R.id.seller_phone);
        fab2 = (FloatingActionButton)findViewById(R.id.fab2);
        fab3 = (FloatingActionButton)findViewById(R.id.fab3);
        sEmail = (TextView) findViewById(R.id.seller_mail);
        Intent in = getIntent();
        final String book_userId = in.getExtras().getString("userID").toString();
        mDatabase.child(book_userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 seller_mail = (String)dataSnapshot.child("u_mail").getValue();
                 seller_phone = (String)dataSnapshot.child("u_phone").getValue();
                sEmail.setText(seller_mail);
                sPhone.setText(seller_phone);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri call = Uri.parse("tel:" + seller_phone);
                Intent surf = new Intent(Intent.ACTION_DIAL, call);
                startActivity(surf);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto: "+ seller_mail));
                startActivity(Intent.createChooser(emailIntent, "Send Mail"));
            }
        });

    }


}
