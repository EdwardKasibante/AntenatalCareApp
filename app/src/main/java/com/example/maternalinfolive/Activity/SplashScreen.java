package com.example.maternalinfolive.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maternalinfolive.R;
import com.example.maternalinfolive.Utils.StorageSense;


public class SplashScreen extends AppCompatActivity {
    private ImageView img;
    private TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img=findViewById(R.id.splash_img);
        txt=findViewById(R.id.txt);
        img.setAnimation(AnimationUtils.loadAnimation(SplashScreen.this, R.anim.from_top));
        txt.setAnimation(AnimationUtils.loadAnimation(SplashScreen.this, R.anim.from_bottom));


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    SharedPreferences sharedPreferences = getSharedPreferences(StorageSense.onHealthSense(), Context.MODE_PRIVATE);
                    String profile = sharedPreferences.getString("profile", null);
                    String code = sharedPreferences.getString("user_id", null);

                    if (profile == null || profile == "") {
                        startActivity(new Intent(SplashScreen.this, Registration.class));
                        finish();
                    }
                    else{
                        startActivity(new Intent(SplashScreen.this, Dashboard.class));
                        SplashScreen.this.finish();
                    }

                }catch (Exception e){

                }
            }
        },2000);
    }
}