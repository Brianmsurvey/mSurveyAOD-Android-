package com.msurvey.projectm.msurveyaod;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AppFeedbackActivity extends AppCompatActivity{

    private EditText feedback;

    private Button submitBtn;

    private ImageView mSuccessIcon;

    private TextView mThankYou;

    private FirebaseDatabase mDatabase;

    private DatabaseReference mFeedbackDatabase;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_appfeedback);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Give Suggestion");

        feedback = findViewById(R.id.et_appfeedback);

        mDatabase = FirebaseDatabase.getInstance();

        mFeedbackDatabase = mDatabase.getReference().child("AppFeedback");

        submitBtn = findViewById(R.id.btn_submitFeedback);

        mSuccessIcon = findViewById(R.id.iv_successicon);

        mThankYou = findViewById(R.id.tv_thankyou);


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date c = Calendar.getInstance().getTime();

                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                String formattedDate = df.format(c);

                if(!feedback.getText().toString().equals("")){

                    mFeedbackDatabase.child(formattedDate).push().setValue(feedback.getText().toString());
                    feedback.setText("");
                    feedback.setVisibility(View.INVISIBLE);
                    mSuccessIcon.setVisibility(View.VISIBLE);
                    mThankYou.setVisibility(View.VISIBLE);
                    submitBtn.setVisibility(View.INVISIBLE);


                }else{
                    Toast.makeText(AppFeedbackActivity.this, "Tell us something", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
