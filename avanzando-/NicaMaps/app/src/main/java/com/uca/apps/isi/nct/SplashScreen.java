package com.uca.apps.isi.nct;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.tumblr.remember.Remember;
import com.uca.apps.isi.nct.activities.PrefManager;
import com.uca.apps.isi.nct.activities.SignInActivity;

public class SplashScreen extends AppCompatActivity {

    public void validarSesion (){
        if (Remember.getString("access_token", "").isEmpty()){
            Intent intent = new Intent(SplashScreen.this, SignInActivity.class);
            startActivity(intent);
            finish();

        }
        else {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private PrefManager prefManager;

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(true);

        startActivity(new Intent(SplashScreen.this, Tour.class));
        finish();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                prefManager = new PrefManager(SplashScreen.this);
                if (!prefManager.isFirstTimeLaunch()) {
                    launchHomeScreen();
                    finish();
                }else{
                    validarSesion();

                }

            }
        }, 4000);

    }
}
