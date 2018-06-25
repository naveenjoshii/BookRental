package com.example.hp.bookrental;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BooksActivity extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    private RecyclerView mRecyclerView;
    private BooksAdapter booksAdapter;
    FirebaseRecyclerAdapter<BooksDetails,BooksAdapter.ViewHolder> firebaseRecyclerAdapter;
    private DatabaseReference mDatabaseRef;
    private ProgressBar mProgressCircle;
    private int len,rel;
    private SearchView searchView;
    private List<BooksDetails> mBooks = new ArrayList<>();
@Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_books);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("BOOK RENTAL");
    setSupportActionBar(toolbar);
    floatingActionButton = (FloatingActionButton) findViewById(R.id.fab1);
    mProgressCircle = findViewById(R.id.progress_circle);





    floatingActionButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(BooksActivity.this, uploadBook.class));
        }
    });
    mRecyclerView = findViewById(R.id.recycler_view);
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setItemViewCacheSize(150);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mDatabaseRef = FirebaseDatabase.getInstance().getReference("Books_info");
    booksAdapter = new BooksAdapter(BooksActivity.this, mBooks);
    mRecyclerView.setAdapter(booksAdapter);
   mDatabaseRef.addValueEventListener(new ValueEventListener() {
       @Override
       public void onDataChange(DataSnapshot dataSnapshot) {
           for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
               BooksDetails booksDetails = postSnapshot.getValue(BooksDetails.class);
               mBooks.add(booksDetails);
           }

           booksAdapter.notifyDataSetChanged();

           mProgressCircle.setVisibility(View.INVISIBLE);
       }

       @Override
       public void onCancelled(DatabaseError databaseError) {

       }
   });



}
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menuFeedback:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto: gdsc.gvpce@gmail.com"));
                startActivity(Intent.createChooser(emailIntent, "Send feedback"));
                break;
            case R.id.menuProfile:
                Toast.makeText(this,"YOU Clicked Profile",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(BooksActivity.this,profile.class));
                break;

            case R.id.menuLogout:
                Toast.makeText(this, "Successfully logged Out", Toast.LENGTH_SHORT).show();
                logout();
                finish();
                break;
            case R.id.menuBooks:
            break;

        }
        return true;
    }
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            findViewById(R.id.action_search).setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    private void logout() {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(BooksActivity.this,Login.class));
    }
}
