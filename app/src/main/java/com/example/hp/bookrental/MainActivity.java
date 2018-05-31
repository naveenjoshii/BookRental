package com.example.hp.bookrental;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private TextView txtv1,txtv2;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtv1=(TextView)findViewById(R.id.txtv1);
        txtv2=(TextView)findViewById(R.id.txtv2);
        imageView=(ImageView)findViewById(R.id.ImageView);
        Animation myanim= AnimationUtils.loadAnimation(this,R.anim.mytransition);
        txtv1.startAnimation(myanim);
        txtv2.startAnimation(myanim);
        imageView.startAnimation(myanim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent=new Intent(MainActivity.this,Login.class);
                startActivity(homeIntent);
                finish();
            }
        },4000);

    }

}
