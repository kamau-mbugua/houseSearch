package com.example.housesearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class HouseDetailActivity extends AppCompatActivity {

    private static final int CALL_PERMISSION = 30;

    ImageView ivhotelImage, imageView_back;
    TextView tvratings,tvHotelEmail,tvHotelPhone,tvhotelLocation,
            tvhotelNames,tvtagsList, tvHotelPrice,tvMapUrlLoaccation, tvHotelWebsite;

    String mratings,mtvHotelEmail,mtvHotelPhone,mhotelLocation,
            mhotelNames,mtagsList, mhotelImage, mhotelPrice, mhotelMapUrl,mhotelWebsite;
    WebView tvHotelDirection;

    CardView cardHotelPhone, cardHotelEmail, cardHotelLocation, cardHotelWebsite;



    private void initializeWidgets(){
        cardHotelPhone        = findViewById(R.id.phoneCard);
        cardHotelEmail        = findViewById(R.id.mailCard);
        cardHotelLocation        = findViewById(R.id.webCard);
        cardHotelWebsite        = findViewById(R.id.mapCard);


        ivhotelImage        = findViewById(R.id.ivHotelImage);
        tvratings           = findViewById(R.id.tvHotelRatings);
        tvHotelEmail        = findViewById(R.id.tvHotelEmail);
        tvHotelPhone        = findViewById(R.id.tvHotelPhone);
        //tvHotelDirection    = findViewById(R.id.googleMapView);
        tvhotelLocation     = findViewById(R.id.tvHotelLocation);
        tvhotelNames        = findViewById(R.id.tvHotelName);
        tvtagsList          = findViewById(R.id.tvHotelTagList);
        tvHotelPrice        = findViewById(R.id.tvHotelPrice);
        tvMapUrlLoaccation  = findViewById(R.id.tvMapUrlLoaccation);
        tvHotelWebsite      = findViewById(R.id.tvHotelWebsite);
        imageView_back = findViewById(R.id.imageView_back);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_detail);
        initializeWidgets();
        recieveIntents();
        cardHotelEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailing();

            }
        });

        cardHotelPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoning();

            }
        });

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HouseListActivity.class));
                finish();
            }
        });



    }

    private void phoning() {
        if (Build.VERSION.SDK_INT >= 23){
            if (checkedPermission()){
//                Permission Already Granted
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                phoneIntent.setData(Uri.parse("tel:" + mtvHotelPhone));
                startActivity(phoneIntent);
            }
            else {
                requestPermission();
            }
        }
        if (checkedPermission()){
            Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
            phoneIntent.setData(Uri.parse("tel:" + mtvHotelPhone));
            startActivity(phoneIntent);
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(HouseDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION);
    }

    private boolean checkedPermission() {
        int callPermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE);

        return callPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void mailing() {
        Intent mailIntent = new Intent(Intent.ACTION_SEND);
        mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {mtvHotelEmail});
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, "REQUESTING TO BOOK FOR A House AT "+ " "+ tvhotelNames);
        mailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(mailIntent, "Choose Mail Client"));
    }



    private void recieveIntents() {
        if (getIntent().hasExtra("hotelLocation1")
                && getIntent().hasExtra("hotelName1")
                && getIntent().hasExtra("hotelRating1")
                && getIntent().hasExtra("hotelListTag1")
                && getIntent().hasExtra("imageUri1")
                && getIntent().hasExtra("email1")
                && getIntent().hasExtra("phone1")
                && getIntent().hasExtra("mapUrl1")
                && getIntent().hasExtra("websiteUrl1")
                && getIntent().hasExtra("hotelPricePerHour1")) {

            mhotelLocation = getIntent().getStringExtra("hotelLocation1");
            mhotelNames = getIntent().getStringExtra("hotelName1");
            mratings = getIntent().getStringExtra("hotelRating1");
            mtagsList = getIntent().getStringExtra("hotelListTag1");
            mhotelImage = getIntent().getStringExtra("imageUri1");
            mtvHotelEmail = getIntent().getStringExtra("email1");
            mtvHotelPhone = getIntent().getStringExtra("phone1");
            mhotelMapUrl = getIntent().getStringExtra("hotelLocation1");//mapUrl1
            mhotelWebsite = getIntent().getStringExtra("websiteUrl1");
            mhotelPrice = getIntent().getStringExtra("hotelPricePerHour1");

            provision(mhotelLocation,mhotelNames,mratings,mtagsList,
                    mhotelImage,mtvHotelEmail, mtvHotelPhone, mhotelMapUrl,  mhotelWebsite,mhotelPrice);
        }
    }

    private void provision(String mhotelLocation, String mhotelNames, String mratings,
                           String mtagsList, String mhotelImage,
                           String mtvHotelEmail, String mtvHotelPhone,
                           String mhotelMapUrl, String mhotelWebsite, String mhotelPrice) {

        tvratings .setText(mratings);
        tvHotelEmail .setText(mtvHotelEmail);
        tvHotelPhone .setText(mtvHotelPhone);
        tvhotelLocation.setText(mhotelLocation); ;
        tvhotelNames .setText(mhotelNames);
        tvtagsList .setText(mtagsList);
        tvHotelPrice .setText("Ksh:"+mhotelPrice);
        tvMapUrlLoaccation.setText("http://maps.google.com/maps/place/"+mhotelMapUrl+"/"); //http://maps.google.com/maps/place/Kakamega/
        tvHotelWebsite.setText(mhotelWebsite);


        // String url = tvMapUrlLoaccation.toString();

        Picasso.get().load(mhotelImage).fit().placeholder(R.drawable.placeholder).into(ivhotelImage);
        Toast.makeText(this, "Url : " + mhotelImage, Toast.LENGTH_SHORT).show();

        cardHotelLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(tvMapUrlLoaccation.toString()));
                startActivity(browserIntent);*/

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps/place/"+mhotelMapUrl+"/")));
            }
        });

        cardHotelWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mhotelWebsite)));
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CALL_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission For Calling has been Accepted...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission For Calling has been Denied...", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}