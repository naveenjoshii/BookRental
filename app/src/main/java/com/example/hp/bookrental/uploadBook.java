package com.example.hp.bookrental;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class uploadBook extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button bChoose,bUpload;
    private EditText bName,bAuthor,bEdition,bPrice;
    private RadioButton bSell,bRent;
    private Uri mImageUri;
    private ImageView mImageView;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    private ProgressDialog progressDialog;
    private  String bookDate;

    private String Status = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_book);
        bChoose = (Button)findViewById(R.id.choose);
        bUpload = (Button)findViewById(R.id.upload);
        bName = (EditText)findViewById(R.id.etBname);
        bAuthor = (EditText)findViewById(R.id.etAname);
        bEdition =(EditText)findViewById(R.id.etEno);
        bPrice = (EditText)findViewById(R.id.etPrice);
        mImageView =(ImageView)findViewById(R.id.imagee);
        bSell = (RadioButton)findViewById(R.id.rSell);
        bRent = (RadioButton)findViewById(R.id.rRent);
        progressDialog=new ProgressDialog(uploadBook.this);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Books_info");
        bChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        bUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(uploadBook.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });

    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        progressDialog.setMessage("Uploading Your Book....");
        progressDialog.show();
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(uploadBook.this, "Upload successful", Toast.LENGTH_LONG).show();
                            if(bSell.isChecked()){
                                Status = "Sell";
                            }else if(bRent.isChecked()){
                                Status = "Rent";
                            }
                            SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            bookDate = sd.format(Calendar.getInstance().getTime());
                            String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            String uploadId = mDatabaseRef.push().getKey();
                            BooksDetails booksDetails = new BooksDetails(uploadId,currentuser,taskSnapshot.getDownloadUrl().toString(),
                                    bName.getText().toString(),
                                    bAuthor.getText().toString(),
                                    bEdition.getText().toString(),Status,bPrice.getText().toString(),
                                    bookDate
                                    );
                            //Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),
                                  //  taskSnapshot.getDownloadUrl().toString());
                            mDatabaseRef.child(uploadId).setValue(booksDetails);
                            startActivity(new Intent(uploadBook.this,BooksActivity.class));
                            progressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(uploadBook.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            //Picasso.with(this).load(mImageUri).into(mImageView);
            Picasso.with(uploadBook.this)
                    .load(mImageUri)
                    .fit()
                    .centerCrop()
                    .into(mImageView);
        }
    }
}
