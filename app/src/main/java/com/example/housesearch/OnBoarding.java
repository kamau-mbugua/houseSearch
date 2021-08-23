package com.example.housesearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class OnBoarding extends AhoyOnboarderActivity {
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        user = FirebaseAuth.getInstance().getCurrentUser();

        AhoyOnboarderCard ahoyOnboarderCard1 = new AhoyOnboarderCard("Search a rental House", "Search for all available houses within a Localityof your wish.", R.drawable.house_search);
        AhoyOnboarderCard ahoyOnboarderCard2 = new AhoyOnboarderCard("Rent a House", "Add a House to Rent or Let.", R.drawable.house_search);
        AhoyOnboarderCard ahoyOnboarderCard3 = new AhoyOnboarderCard("Satisfied?", "Lets Go.", R.drawable.house_search);

        ahoyOnboarderCard1.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard2.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard3.setBackgroundColor(R.color.black_transparent);

        List<AhoyOnboarderCard> pages = new ArrayList<>();

        pages.add(ahoyOnboarderCard1);
        pages.add(ahoyOnboarderCard2);
        pages.add(ahoyOnboarderCard3);

        for (AhoyOnboarderCard page : pages) {
            page.setTitleColor(R.color.white);
            page.setDescriptionColor(R.color.grey_200);
            //page.setTitleTextSize(dpToPixels(12, this));
            //page.setDescriptionTextSize(dpToPixels(8, this));
            //page.setIconLayoutParams(width, height, marginTop, marginLeft, marginRight, marginBottom);
        }

        setFinishButtonTitle("Finish");
        showNavigationControls(true);
        setGradientBackground();

        //set the button style you created
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setFinishButtonDrawableStyle(ContextCompat.getDrawable(this, R.drawable.rounded_button));
        }

        /*Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        setFont(face);*/

        setOnboardPages(pages);
    }

    @Override
    public void onFinishButtonPressed() {
        if(user != null){
            startActivity(new Intent(getApplicationContext(), HouseListActivity.class));
        }
        else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
        finish();
    }
}