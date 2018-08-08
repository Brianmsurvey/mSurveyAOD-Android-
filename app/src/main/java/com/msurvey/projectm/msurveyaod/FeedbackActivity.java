package com.msurvey.projectm.msurveyaod;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.msurvey.projectm.msurveyaod.Utilities.FeedbackUtils;
import com.msurvey.projectm.msurveyaod.Utilities.SmsBroadCastReceiver;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedbackActivity extends AppCompatActivity {

    private static final String TAG = "FeedbackActivity.java";

    private CircleImageView mStoreAvator;

    private TextView mStoreName;

    private TextView mStoreTime;

    private TextView mRateYourExperience;

    private ImageView mVeryHappyEmoji, HappyEmoji, NeutralEmoji,
    SadEmoji, AngryEmoji;

    private ImageView mAvator;

    private ImageView mCircleVeryHappy, mCircleHappy, mCircleNeutral,
    mCircleSad, mCircleAngry;

    private EditText mFeedback;

    private DatabaseReference mFeedbackDatabase;

    private Button mSubmitButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feedback);

        mFeedbackDatabase = FirebaseDatabase.getInstance().getReference().child("TestCustomerFeedback");
        mFeedbackDatabase.keepSynced(true);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Feedback");

        mAvator = findViewById(R.id.iv_store_avator);

        mStoreName = findViewById(R.id.store_name);
        mStoreTime = findViewById(R.id.sent_time);
        mRateYourExperience = findViewById(R.id.rate);


        mVeryHappyEmoji = findViewById(R.id.veryhappy);
        HappyEmoji = findViewById(R.id.happy);
        NeutralEmoji = findViewById(R.id.neutral);
        SadEmoji = findViewById(R.id.sad);
        AngryEmoji = findViewById(R.id.angry);

        mCircleAngry = findViewById(R.id.iv_bluecircle_angry);
        mCircleSad = findViewById(R.id.iv_bluecircle_sad);
        mCircleNeutral = findViewById(R.id.iv_bluecircle_neutral);
        mCircleHappy = findViewById(R.id.iv_bluecircle_happy);
        mCircleVeryHappy = findViewById(R.id.iv_bluecircle_veryhappy);

        mFeedback = findViewById(R.id.etfeedback);

        mSubmitButton = findViewById(R.id.btn_submit);

        mStoreName.setText(FeedbackUtils.cashReceiver);

        mStoreTime.setText(FeedbackUtils.transactionDateTime);

        //Avator letter
        ColorGenerator generator = ColorGenerator.MATERIAL;

        String letter = FeedbackUtils.cashReceiver.substring(0,1);

        int color = generator.getRandomColor();

        TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig()
                .endConfig().round();

        TextDrawable drawable = builder.build(letter, color);

        mAvator.setImageDrawable(drawable);


        mVeryHappyEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mRateYourExperience.setText(R.string.veryhappy);
                mCircleAngry.setVisibility(View.INVISIBLE);
                mCircleSad.setVisibility(View.INVISIBLE);
                mCircleNeutral.setVisibility(View.INVISIBLE);
                mCircleHappy.setVisibility(View.INVISIBLE);
                mCircleVeryHappy.setVisibility(View.VISIBLE);


            }
        });

        HappyEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mRateYourExperience.setText(R.string.happy);
                mCircleAngry.setVisibility(View.INVISIBLE);
                mCircleSad.setVisibility(View.INVISIBLE);
                mCircleNeutral.setVisibility(View.INVISIBLE);
                mCircleHappy.setVisibility(View.VISIBLE);
                mCircleVeryHappy.setVisibility(View.INVISIBLE);

            }
        });

        NeutralEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mRateYourExperience.setText(R.string.neutral);
                mCircleAngry.setVisibility(View.INVISIBLE);
                mCircleSad.setVisibility(View.INVISIBLE);
                mCircleNeutral.setVisibility(View.VISIBLE);
                mCircleHappy.setVisibility(View.INVISIBLE);
                mCircleVeryHappy.setVisibility(View.INVISIBLE);

            }
        });

        SadEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mRateYourExperience.setText(R.string.sad);
                mCircleAngry.setVisibility(View.INVISIBLE);
                mCircleSad.setVisibility(View.VISIBLE);
                mCircleNeutral.setVisibility(View.INVISIBLE);
                mCircleHappy.setVisibility(View.INVISIBLE);
                mCircleVeryHappy.setVisibility(View.INVISIBLE);

            }
        });

        AngryEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mRateYourExperience.setText(R.string.angry);
                mCircleAngry.setVisibility(View.VISIBLE);
                mCircleSad.setVisibility(View.INVISIBLE);
                mCircleNeutral.setVisibility(View.INVISIBLE);
                mCircleHappy.setVisibility(View.INVISIBLE);
                mCircleVeryHappy.setVisibility(View.INVISIBLE);

            }
        });


        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String feedback = "";
                feedback = mFeedback.getText().toString();

                String emojiResponse = "";
                emojiResponse = mRateYourExperience.getText().toString();



                if(emojiResponse.equals("") || emojiResponse.equals("Rate your experience")){

                    Snackbar.make(view, "Rate your experience", Snackbar.LENGTH_SHORT).show();

                }else if(feedback.equals("")){

                    Feedback newFeedback = new Feedback(emojiResponse, FeedbackUtils.transactionDate, FeedbackUtils.transactionTime, FeedbackUtils.merchantName, SmsBroadCastReceiver.userPhoneNumber);

                    mFeedback.setText("");

                    mFeedbackDatabase.push().setValue(newFeedback);

                    Intent successIntent = new Intent(FeedbackActivity.this, SuccessActivity.class);
                    startActivity(successIntent);
                    finish();

                }else{
                    Feedback newFeedback = new Feedback(emojiResponse, feedback, FeedbackUtils.transactionDate, FeedbackUtils.transactionTime, FeedbackUtils.merchantName, SmsBroadCastReceiver.userPhoneNumber);

                    mFeedback.setText("");

                    mFeedbackDatabase.push().setValue(newFeedback);

                    Intent successIntent = new Intent(FeedbackActivity.this, SuccessActivity.class);
                    startActivity(successIntent);
                    finish();
                }


            }
        });



    }
}
