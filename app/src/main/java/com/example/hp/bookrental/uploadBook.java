package com.example.hp.bookrental;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.BinderThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
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
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class uploadBook extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_CAMERA = 0;
    private int a, b;
    private Button bChoose, bUpload;
    private EditText bName, bAuthor, bEdition, bPrice;
    private RadioButton bSell, bRent;
    Uri mImageUri;
    private ImageView mImageView;
    private AlertDialog alertDialog;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    private ProgressDialog progressDialog;
    private String bookDate, generatedFilePath;
    Bitmap map;
    private String Status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_book);
        bChoose = (Button) findViewById(R.id.choose);
        bUpload = (Button) findViewById(R.id.upload);
        bName = (EditText) findViewById(R.id.etBname);
        bAuthor = (EditText) findViewById(R.id.etAname);
        bEdition = (EditText) findViewById(R.id.etEno);
        bPrice = (EditText) findViewById(R.id.etPrice);
        mImageView = (ImageView) findViewById(R.id.imagee);
        bSell = (RadioButton) findViewById(R.id.rSell);
        bRent = (RadioButton) findViewById(R.id.rRent);
        progressDialog = new ProgressDialog(uploadBook.this);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Books_info");

        bChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
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
        if(a==1){
            uploadImage(map);
        }else if(b==2){
            UploadImageGallery();
        }else{
            Toast.makeText(uploadBook.this,"Enter Your all details",Toast.LENGTH_SHORT).show();
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
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                mImageUri = data.getData();
                Picasso.with(uploadBook.this)
                        .load(mImageUri)
                        .fit()
                        .centerCrop()
                        .into(mImageView);
            } else if (requestCode == REQUEST_CAMERA) {
                Bundle bundle = data.getExtras();
                 map = (Bitmap) bundle.get("data");
                mImageView.setImageBitmap(map);
            }
        }
    }
    private void selectImage() {
                    final CharSequence[] items = {"Take Photo", "Choose from Library",
                            "Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(uploadBook.this);
                    builder.setTitle("Add Photo!");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (items[item].equals("Take Photo")) {
                                a=1;
                                camera();
                            } else if (items[item].equals("Choose from Library")) {
                                openFileChooser();
                                b=2;
                            } else if (items[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                }

                private void camera() {
                    Intent intt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intt,REQUEST_CAMERA);
                }
    public void uploadImage(Bitmap bitmap) {
        if(bitmap!=null) {
            if (validate()) {
                progressDialog.setMessage("Uploading Your Book....");
                progressDialog.show();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReferenceFromUrl("gs://bookrental-77a54.appspot.com");
                StorageReference imagesRef = storageRef.child("uploads/" + System.currentTimeMillis() + ".jpg");

                final UploadTask uploadTask = imagesRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressDialog.dismiss();
                        Toast.makeText(uploadBook.this, "Connection Failed!", Toast.LENGTH_SHORT).show();

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (bSell.isChecked()) {
                            Status = "Sell";
                        } else if (bRent.isChecked()) {
                            Status = "Rent";
                        }
                        SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yy hh:mm a");
                        bookDate = sd.format(Calendar.getInstance().getTime());
                        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String uploadId = mDatabaseRef.push().getKey();
                        BooksDetails booksDetails = new BooksDetails(uploadId, currentuser, taskSnapshot.getDownloadUrl().toString(),
                                bName.getText().toString(),
                                bAuthor.getText().toString(),
                                bEdition.getText().toString(), Status, bPrice.getText().toString(),
                                bookDate
                        );
                        mDatabaseRef.child(uploadId).setValue(booksDetails);
                        startActivity(new Intent(uploadBook.this, BooksActivity.class));
                        progressDialog.dismiss();

                    }
                });
            } else {
                Toast.makeText(uploadBook.this, "Enter All the details", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }

        }
    }
    public void UploadImageGallery(){

        if(validate()) {
            if (mImageUri != null) {
                progressDialog.setMessage("Uploading Your Book....");
                progressDialog.show();
                StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                        + "." + getFileExtension(mImageUri));

                mUploadTask = fileReference.putFile(mImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Toast.makeText(uploadBook.this, "Upload successful", Toast.LENGTH_LONG).show();
                                if (bSell.isChecked()) {
                                    Status = "Sell";
                                } else if (bRent.isChecked()) {
                                    Status = "Rent";
                                }
                                SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                bookDate = sd.format(Calendar.getInstance().getTime());
                                String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                String uploadId = mDatabaseRef.push().getKey();
                                BooksDetails booksDetails = new BooksDetails(uploadId, currentuser, taskSnapshot.getDownloadUrl().toString(),
                                        bName.getText().toString(),
                                        bAuthor.getText().toString(),
                                        bEdition.getText().toString(), Status, bPrice.getText().toString(),
                                        bookDate
                                );
                                mDatabaseRef.child(uploadId).setValue(booksDetails);
                                startActivity(new Intent(uploadBook.this, BooksActivity.class));
                                progressDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(uploadBook.this, "Connection Failed!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
            } else {
                Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Enter all the details", Toast.LENGTH_SHORT).show();

        }
    }

    private boolean validate() {
        boolean result =true;
       if(bName.getText().toString().isEmpty()||bAuthor.getText().toString().isEmpty()||bEdition.getText().toString().isEmpty()||bPrice.getText().toString().isEmpty()){
           result = false;
       }else{
           result =true;
        }
        return result;

    }

}
