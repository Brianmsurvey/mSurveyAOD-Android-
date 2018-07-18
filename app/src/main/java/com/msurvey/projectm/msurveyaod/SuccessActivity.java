package com.msurvey.projectm.msurveyaod;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_success);

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                finish();

            }
        }, 1500);
    }
}
