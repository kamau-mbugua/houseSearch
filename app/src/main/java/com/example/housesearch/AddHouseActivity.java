package com.example.housesearch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.housesearch.databinding.ActivityAddHouseBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddHouseActivity extends AppCompatActivity {

    ImageView ivBack, hotelImage;
    EditText etLocation, etHotelName, etRating, etTagList, etPrice, etPhone,etWeb,etMapUrl,etEmail;
    Button btnSave;
    TextView tvUpload;
    Uri image_uri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private static final String TAG = "AddHotelActivity";

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;


    private StorageTask mUploadTask;
    private CardView mProgress;

    House hotel;

    long maxid = 0;

    String mhotelLocation, mhotelName, mhotelRating,mhotelPricePerHour,email,phone,mapUrl,webUrl, mhotelTagList;
   /* String etLocation1 = etLocation.getText().toString();
    String etHotelName1 = etHotelName.getText().toString();
    String etRating1 = etRating.getText().toString();
    String etTagList1 = etTagList.getText().toString();
    String etPrice1 = etPrice.getText().toString();*/

    ActivityAddHouseBinding addHotelBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_house);
        addHotelBinding = ActivityAddHouseBinding.inflate(getLayoutInflater());
        View view = addHotelBinding.getRoot();
        setContentView(view);

        storageReference = FirebaseStorage.getInstance().getReference("hotelProducts");
        databaseReference = FirebaseDatabase.getInstance().getReference("hotelProducts");

        addHotelBinding.progressBar.setVisibility(View.GONE);

//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if (snapshot.exists()){
//                    maxid=(snapshot.getChildrenCount());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        etEmail = findViewById(R.id.etEmail);
        ivBack = findViewById(R.id.ivBack);
        hotelImage = findViewById(R.id.hotelImage);
        etLocation = findViewById(R.id.etLocation);
        etHotelName = findViewById(R.id.etHotelName);
        etRating = findViewById(R.id.etRating);
        etTagList = findViewById(R.id.etTagList);
        etPrice = findViewById(R.id.etPrice);
        btnSave = findViewById(R.id.btnSave);
        tvUpload = findViewById(R.id.tvUploadPhoto);
        mProgress = findViewById(R.id.progressBar);
        etPhone = findViewById(R.id.etphone);
        etWeb = findViewById(R.id.etweb);
        etMapUrl = findViewById(R.id.etmapUrl);

        hotel = new House();


        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(AddHouseActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_CODE_STORAGE_PERMISSION);
                    } else {

                        selectImage();

                    }

                } else {
                    selectImage();
                }


            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiveEntries();


            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();

            }
        });
    }

    private void uploadFile() {

        if (image_uri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(image_uri));

            /*uploadProgressBar.setVisibility(View.VISIBLE);
            uploadProgressBar.setIndeterminate(true);*/

            UploadTask uploadTask = fileReference.putFile(image_uri);
            Toast.makeText(this, "UP " + uploadTask, Toast.LENGTH_SHORT).show();

            // Register observers to listen for when the download is done or if it fails

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddHouseActivity.this, "Failed to Upload...", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddHouseActivity.this, "Successfully Uploaded...", Toast.LENGTH_LONG).show();

                    if (taskSnapshot.getMetadata() != null)
                        if (taskSnapshot.getMetadata().getReference() != null) {
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Toast.makeText(AddHouseActivity.this, "Proceed...", Toast.LENGTH_LONG).show();
                                    String sImage = uri.toString();

                                    mProgress.setVisibility(View.VISIBLE);
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            addHotelBinding.mprogressBar.setProgress(0);
                                        }
                                    }, 500);
                                    Toast.makeText(AddHouseActivity.this, "Upload Successful..." + sImage, Toast.LENGTH_SHORT).show();

                                    hotel = new House(mhotelLocation, mhotelName, mhotelRating,mhotelPricePerHour,email,phone,mapUrl,webUrl, mhotelTagList, sImage);
                                    String key = databaseReference.push().getKey();
                                    hotel.setID(key);
                                    databaseReference.child(key).setValue(hotel);

                                    Toast.makeText(AddHouseActivity.this, "Success Key retention...", Toast.LENGTH_LONG).show();
                                    mProgress.setVisibility(View.INVISIBLE);
                                    backToProfile(mhotelLocation, mhotelName, mhotelRating,mhotelPricePerHour,email,phone,mapUrl,webUrl, mhotelTagList, sImage);
                                    etHotelName.setText("");
                                    etLocation.setText("");
                                    etPrice.setText("");
                                    etTagList.setText("");
                                    etEmail.setText("");
                                    etPhone.setText("");
                                    etMapUrl.setText("");
                                    etWeb.setText("");
                                    Picasso.get().load("null").placeholder(R.drawable.placeholder).into(hotelImage);
                                }
                            });
                            result.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mProgress.setVisibility(View.INVISIBLE);
                                    Toast.makeText(AddHouseActivity.this, "Database Fail...", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                }
            });


        }
        else {
            Toast.makeText(this, "Image Url Missing", Toast.LENGTH_SHORT).show();
        }


    }

    private void backToProfile(String mhotelLocation, String mhotelName, String mhotelRating, String mhotelPricePerHour, String email, String phone, String mapUrl, String webUrl, String mhotelTagList, String sImage) {

        Intent backIntent = new Intent(this, MainActivity.class);
        backIntent.putExtra("hotelLocation1",mhotelLocation );
        backIntent.putExtra("hotelName1",mhotelName );
        backIntent.putExtra("hotelRating1",mhotelRating );
        backIntent.putExtra("hotelListTag1",mhotelTagList );
        backIntent.putExtra("imageUri1",sImage);
        backIntent.putExtra("email1", email);
        backIntent.putExtra("phone1", phone);
        backIntent.putExtra("mapUrl1",mapUrl );
        backIntent.putExtra("websiteUrl1", webUrl);
        backIntent.putExtra("hotelPricePerHour1", mhotelPricePerHour);

        // startActivity(backIntent);
    }

    /*private void backToProfile(String location, String name, String rating, String pricePerHour, String mhotelLocation, String mhotelName, String mhotelRating, String mhotelTagList, String mhotelPricePerHour, String sImage) {

        Intent passIntent = new Intent(this, ProfileActivity.class);
        passIntent.putExtra("hotelLocation1",location );
        passIntent.putExtra("hotelName1",name );
        passIntent.putExtra("hotelRating1",rating );
        passIntent.putExtra("hotelListTag1",mhotelRating );
        passIntent.putExtra("imageUri1",);
        passIntent.putExtra("email1", );
        passIntent.putExtra("phone1", );
        passIntent.putExtra("mapUrl1", );
        passIntent.putExtra("websiteUrl1", );
        passIntent.putExtra("hotelPricePerHour1", );
    }*/


    private void receiveEntries() {

        mhotelLocation = etLocation.getText().toString().trim();
        mhotelName = etHotelName.getText().toString().trim();
        mhotelRating= etRating.getText().toString().trim();
        mhotelPricePerHour= etPrice.getText().toString().trim();
        email= etEmail.getText().toString().trim();
        phone= etPhone.getText().toString().trim();
        mapUrl= etMapUrl.getText().toString().trim();
        webUrl= etWeb.getText().toString().trim();
        mhotelTagList=etTagList.getText().toString().trim();

        checkFields();
    }

    private void checkFields() {

        if (etLocation.getText().toString().isEmpty()) {
            etLocation.setError("Location of The house is required.");
        } else if (etHotelName.getText().toString().isEmpty()) {
            etHotelName.setError("Name of The house is required.");
        } else if (etRating.getText().toString().isEmpty()) {
            etRating.setError("Rating of The house is required.");
        } else if (etTagList.getText().toString().isEmpty()) {
            etTagList.setError("Tag List of The house is required.");
        } else if (etPrice.getText().toString().isEmpty()) {
            etPrice.setError("Price per Hour of The house is required.");

        } else {

            if (mUploadTask != null && mUploadTask.isInProgress()) {
                Toast.makeText(AddHouseActivity.this, "An Upload is Still in Progress", Toast.LENGTH_SHORT).show();
            } else {

                if (isNetworkConnected()) {
                    uploadFile();
                    startActivity(new Intent(getApplicationContext(), HouseListActivity.class));
                    finish();
                }
                else {

                    Toast.makeText(AddHouseActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }


            }
        }

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();

    }

    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            image_uri = data.getData();

            Picasso.get().load(image_uri).into(hotelImage);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }




    /*private void openImagesActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }*/
}