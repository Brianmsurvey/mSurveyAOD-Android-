package com.msurvey.projectm.msurveyaod;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class frag_profile extends Fragment {

    private RecyclerView recyclerView;

    private CircleImageView mMerchantImage;

    private Toolbar toolbar;

    private TextView mMerchantName, mFeedback, mDate, mTime;

    private Context context;

    private LinearLayoutManager mLayoutManager;
    
    //Firebase
    private DatabaseReference mFeedbackDatabase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View profileView = inflater.inflate(R.layout.frag_test1, container, false);
        
        recyclerView = profileView.findViewById(R.id.rv_feedback);

        context = getActivity();

        mFeedbackDatabase = FirebaseDatabase.getInstance().getReference().child("TestCustomerFeedback");

        FirebaseRecyclerAdapter<Feedback, FeedbackViewHolder> FeedbackFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Feedback, FeedbackViewHolder>(
                Feedback.class,
                R.layout.feedback_row,
                FeedbackViewHolder.class,
                mFeedbackDatabase
        ) {
            @Override
            protected void populateViewHolder(FeedbackViewHolder viewHolder, Feedback model, int position) {

                viewHolder.setMerchantName(model.getmerchantName());
                viewHolder.setFeedback(model.getFeedbackInput());
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


    public static class FeedbackViewHolder extends RecyclerView.ViewHolder{

        View mView;

        private TextView mMerchant, mFeedback, mDate, mTime;

        private CircleImageView mMerchantImage;

        public FeedbackViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setmMerchantImage(final Context context, final String image){

            mMerchantImage = mView.findViewById(R.id.civ_merchant);

        }

        public void setMerchantName(String merchantName){

            mMerchant = mView.findViewById(R.id.tv_merchant);
            mMerchant.setText(merchantName);
        }

        public void setFeedback(String feedback){

            mFeedback = mView.findViewById(R.id.tv_feedback);
            mFeedback.setText(feedback);
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
