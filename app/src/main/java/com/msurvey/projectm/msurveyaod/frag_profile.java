package com.msurvey.projectm.msurveyaod;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.msurvey.projectm.msurveyaod.Utilities.EmojiUtils;
import com.msurvey.projectm.msurveyaod.Utilities.NetworkUtils;
import com.msurvey.projectm.msurveyaod.Utilities.PhotoUtils;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class frag_profile extends Fragment {

    private static String userPhoneNumber;

    private RecyclerView recyclerView;

    private ImageView mMerchantImage;

    private Toolbar toolbar;

    private TextView mUserName, mLocation, mMerchantName, mFeedback, mDate, mTime;

    private TextView AirtimeEarned;
    private TextView SurveysCompleted;
    private CircleImageView mAvator;

    private Context context;

    private LinearLayoutManager mLayoutManager;
    
    //Firebase
    private DatabaseReference mFeedbackDatabase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View profileView = inflater.inflate(R.layout.frag_test1, container, false);
        
        recyclerView = profileView.findViewById(R.id.rv_feedback);

        AirtimeEarned = profileView.findViewById(R.id.airtime_earned_no);
        SurveysCompleted = profileView.findViewById(R.id.surveys_completed_no);
        mAvator = profileView.findViewById(R.id.civ_profile_avator);
        mUserName = profileView.findViewById(R.id.tv_name);
        mLocation = profileView.findViewById(R.id.tv_location);

        AirtimeEarned.setText(NetworkUtils.getAirtimeEarned());

        SurveysCompleted.setText(NetworkUtils.getSurveysCompletedNo());


        com.facebook.accountkit.AccessToken accessToken = AccountKit.getCurrentAccessToken();

        //If this user has an account
        if (accessToken != null) {


            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(final Account account) {
                    // Get Account Kit ID
                    String accountKitId = account.getId();

                    // Get phone number
                    PhoneNumber phoneNumber = account.getPhoneNumber();
                    if (phoneNumber != null) {
                        String phoneNumberString = phoneNumber.toString().replace("+", "");
                        userPhoneNumber = phoneNumberString;
                        NetworkUtils.setPhoneNumber(phoneNumberString);
                    }
                }

                @Override
                public void onError(final AccountKitError error) {
                    // Handle Error


                }

            });
        }


        //Get the Shared Preferences
        final SharedPreferences prefs = getActivity().getSharedPreferences("my_preferences", MODE_PRIVATE);
        final String userName = "name";

        if(!prefs.getString(userName, "").equals("")) {

            String name = prefs.getString(userName, "");
            mUserName.setText(name);

        }


        //Get the Shared Preferences
        final SharedPreferences preferences = getActivity().getSharedPreferences("my_preferences", MODE_PRIVATE);
        final String imageFound = "image_found";

        if(!preferences.getString(imageFound, "").equals("")) {

            Uri image = Uri.parse(preferences.getString(imageFound, ""));
            Picasso.get().load(image).resize(660, 660).centerInside().into(mAvator);

        }

        if(PhotoUtils.getResultImageUri() != null){

            Picasso.get().load(PhotoUtils.getResultImageUri()).resize(660, 660).centerInside().into(mAvator);

        }

        mAvator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent changePhotoIntent = new Intent(getActivity(), ChangePhotoActivity.class);
                startActivity(changePhotoIntent);

            }
        });

        context = getActivity();

        Query mUserFeedbackDatabase = FirebaseDatabase.getInstance().getReference().child("TestCustomerFeedback").orderByChild("userNumber").equalTo(userPhoneNumber);

        FirebaseRecyclerAdapter<Feedback, FeedbackViewHolder> FeedbackFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Feedback, FeedbackViewHolder>(
                Feedback.class,
                R.layout.feedback_row,
                FeedbackViewHolder.class,
                mUserFeedbackDatabase
        ) {
            @Override
            protected void populateViewHolder(FeedbackViewHolder viewHolder, Feedback model, int position) {

                viewHolder.setMerchantName(model.getmerchantName());
                viewHolder.setmMerchantImage(context, model.getmerchantName());
                viewHolder.setFeedback(model.getFeedbackInput(), model.getOvrResponse());
                viewHolder.setFeedbackDate(model.getTransactionDate());
                viewHolder.setFeedbackTime(model.getTransactionTime());
            }

            @Override
            public FeedbackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return super.onCreateViewHolder(parent, viewType);
            }
        };

        recyclerView.setAdapter(FeedbackFirebaseRecyclerAdapter);
        mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);


        return profileView;
    }


    @Override
    public void onStart() {
        super.onStart();

        if(PhotoUtils.getResultImageUri() != null){

            Picasso.get().load(PhotoUtils.getResultImageUri()).resize(660, 660).centerInside().into(mAvator);

        }
    }


    public static class FeedbackViewHolder extends RecyclerView.ViewHolder{

        View mView;

        private TextView mMerchant, mFeedback, mDate, mTime;

        private ImageView mMerchantImage, mEmoji;

        public FeedbackViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setmMerchantImage(final Context context, final String image){

            mMerchantImage = mView.findViewById(R.id.civ_merchant);

            ColorGenerator generator = ColorGenerator.MATERIAL;

            String letter = image.substring(0,1);

            int color = generator.getRandomColor();

            TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig()
                    .endConfig().round();

            TextDrawable drawable = builder.build(letter, color);

            mMerchantImage.setImageDrawable(drawable);

        }

        public void setMerchantName(String merchantName){

            mMerchant = mView.findViewById(R.id.tv_merchant);
            mMerchant.setText(merchantName);
        }

        public void setFeedback(String feedback, String ovrResponse){

            mEmoji = mView.findViewById(R.id.iv_emoji);

            mFeedback = mView.findViewById(R.id.tv_feedback);

            if(!TextUtils.isEmpty(feedback)){
                feedback = '"' + feedback + '"';
                mFeedback.setText(feedback);
            }else{

                mEmoji.setVisibility(View.VISIBLE);
                mFeedback.setVisibility(View.INVISIBLE);
                EmojiUtils.parseResponseToEmoji(ovrResponse, mEmoji);
            }
        }

        public void setFeedbackDate(String date){

            mDate = mView.findViewById(R.id.tv_date);
            mDate.setText(date);
        }

        public void setFeedbackTime(String time){

            mTime = mView.findViewById(R.id.tv_time);
            mTime.setText(time);
        }
    }
}
