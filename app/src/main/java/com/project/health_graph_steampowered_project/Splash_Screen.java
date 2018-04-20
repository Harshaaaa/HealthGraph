package com.project.health_graph_steampowered_project;

/**
 * Created by apiiit-rkv on 18/4/18.
 */

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash_Screen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
        else {
            new CountDownTimer(2000,800) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    final Intent i=new Intent(Splash_Screen.this,Sign_In.class);
                    startActivity(i);
                }
            }.start();
        }
    }
}
