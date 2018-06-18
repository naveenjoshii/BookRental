package com.example.hp.bookrental;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.core.Context;
import com.squareup.picasso.Picasso;

public class BookInfo extends AppCompatActivity {
    private ImageView im;
    private TextView bk_name,bk_Author,bk_Edition,bk_status,bk_Price;
    private Button contact;
    private Intent in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        contact = (Button)findViewById(R.id.contact_owner);
        im = (ImageView)findViewById(R.id.b_image);
        bk_name = (TextView)findViewById(R.id.b_name);
        bk_Author = (TextView)findViewById(R.id.b_author);
        bk_Edition = (TextView)findViewById(R.id.b_edition);
        bk_status = (TextView)findViewById(R.id.b_status);
        bk_Price = (TextView)findViewById(R.id.b_price);
        Intent i =getIntent();
        final String book_name = i.getExtras().getString("Book Name");
        final String book_author = i.getExtras().getString("Book Author");
        final String book_Edition = i.getExtras().getString("Book Edition");
        final String book_Status = i.getExtras().getString("Book Status");
        final String book_price = i.getExtras().getString("Book Price");
        final String book_img = i.getExtras().getString("Book image");
        final String book_uid = i.getExtras().getString("Book userid");
        Picasso.with(this).load(book_img).into(im);
        bk_name.setText("BOOK NAME - "+book_name);
        bk_Author.setText("BOOK AUTHOR - "+book_author);
        bk_Edition.setText("BOOK EDITION - "+book_Edition);
        bk_status.setText("BOOK STATUS - "+book_Status);
        bk_Price.setText("BOOK PRICE - "+book_price);
         in = new Intent(getApplicationContext(),sellerDetails.class);
        in.putExtra("userID",book_uid);

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(in);
            }
        });
     }
}
