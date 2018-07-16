package com.msurvey.projectm.msurveyaod;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.widget.EmojiTextView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedbackActivity extends AppCompatActivity {

    private CircleImageView mStoreAvator;

    private TextView mStoreName;

    private TextView mStoreTime;

    private TextView mRateYourExperience;

    private ImageView mVeryHappyEmoji, HappyEmoji, NeutralEmoji,
    SadEmoji, AngryEmoji;

    private EditText mFeedback;

    private Button mSubmitButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feedback);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Add Photo");


        mStoreAvator = findViewById(R.id.civ_store_avator);
        mStoreName = findViewById(R.id.store_name);
        mStoreTime = findViewById(R.id.sent_time);
        mRateYourExperience = findViewById(R.id.rate);
        mVeryHappyEmoji = findViewById(R.id.veryhappy);
        HappyEmoji = findViewById(R.id.happy);
        NeutralEmoji = findViewById(R.id.neutral);
        SadEmoji = findViewById(R.id.sad);
        AngryEmoji = findViewById(R.id.angry);

        mFeedback = findViewById(R.id.etfeedback);

        mSubmitButton = findViewById(R.id.btn_submit);


        mVeryHappyEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mRateYourExperience.setText(R.string.veryhappy);

            }
        });

        HappyEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mRateYourExperience.setText(R.string.happy);

            }
        });

        NeutralEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mRateYourExperience.setText(R.string.neutral);

            }
        });

        SadEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mRateYourExperience.setText(R.string.sad);

            }
        });

        AngryEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mRateYourExperience.setText(R.string.angry);

            }
        });

        String emojiResponse = mRateYourExperience.getText().toString();

        String feedback = mFeedback.getText().toString();

    }
}
