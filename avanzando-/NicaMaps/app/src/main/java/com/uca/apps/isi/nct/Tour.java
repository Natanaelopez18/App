package com.uca.apps.isi.nct;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.uca.apps.isi.nct.activities.SignInActivity;
import com.vlonjatg.android.apptourlibrary.AppTour;
import com.vlonjatg.android.apptourlibrary.MaterialSlide;

public class Tour extends AppTour {



    @Override
    public void init( Bundle savedInstanceState) {

        int firstColor = Color.parseColor("#0097A7");
        int secondColor = Color.parseColor("#FFA000");
        //int customSlideColor = Color.parseColor("#009866");
        String text1 = getResources().getString(R.string.welcome_text);
        String text2 = getResources().getString(R.string.publish_complaints);
        String text3 = getResources().getString(R.string.publish_photos);
        String text4 = getResources().getString(R.string.geographic_location);





        //Create pre-created fragments
        Fragment firstSlide = MaterialSlide.newInstance(R.drawable.logouca, "",
                text1, Color.WHITE, Color.WHITE);


        Fragment secondSlide = MaterialSlide.newInstance(R.drawable.denuncias, "",
                text2, Color.WHITE, Color.WHITE);
        Fragment thirdSlide = MaterialSlide.newInstance(R.drawable.ic_menu_camera, "",
                text3, Color.WHITE, Color.WHITE);
        Fragment fourSlide = MaterialSlide.newInstance(R.drawable.mapsgoogle, "",
                text4, Color.WHITE, Color.WHITE);




        //Add slides
        addSlide(firstSlide, secondColor);
        addSlide(secondSlide, firstColor);
        addSlide(thirdSlide, secondColor);
        addSlide(fourSlide, firstColor);





        //Customize tour
        setSkipButtonTextColor(Color.WHITE);
        setNextButtonColorToWhite();
        setDoneButtonTextColor(Color.WHITE);
        setSkipText("Saltar");
        setDoneText("Siguiente");

        
    }

    @Override
    public void onSkipPressed() {

        Intent intent = new Intent(Tour.this, SignInActivity.class);
        finish();
       startActivity(intent);

    }

    @Override
    public void onDonePressed() {

        
        Intent intent = new Intent(Tour.this, SignInActivity.class);
        finish();
        startActivity(intent);

    }
}
