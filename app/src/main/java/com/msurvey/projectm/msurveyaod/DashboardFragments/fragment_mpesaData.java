package com.msurvey.projectm.msurveyaod.DashboardFragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.msurvey.projectm.msurveyaod.R;
import com.msurvey.projectm.msurveyaod.Utilities.DateUtils;
import com.msurvey.projectm.msurveyaod.Utilities.EmojiUtils;
import com.msurvey.projectm.msurveyaod.mpesaSMS;

public class fragment_mpesaData extends Fragment{


    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;

    private DatabaseReference mMpesaDatabase;

    private Context context;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_mpesadata, container, false);

        recyclerView = view.findViewById(R.id.rv_mpesadata);

        context = getActivity();

        mMpesaDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("mpesaData");

        FirebaseRecyclerAdapter<mpesaSMS, MpesaViewHolder> FeedbackFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<mpesaSMS, MpesaViewHolder>(
                mpesaSMS.class,
                R.layout.feedback_row,
                MpesaViewHolder.class,
                mMpesaDatabase
        ) {
            @Override
            protected void populateViewHolder(MpesaViewHolder viewHolder, final mpesaSMS model, int position) {

                viewHolder.setMerchantName(model.getCashReceiver());
                viewHolder.setmMerchantImage(context, model.getCashReceiver());
                viewHolder.setFeedback(model.getAmountTransacted(), model.getAmountTransacted());
                viewHolder.setFeedbackDate(model.getTransactionDate());
                viewHolder.setFeedbackTime(model.getTransactionTime());


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.details_transaction);

                        TextView date = dialog.findViewById(R.id.tv_date);
                        TextView time = dialog.findViewById(R.id.tv_time);
                        TextView merchant = dialog.findViewById(R.id.tv_merchant);
                        TextView amount = dialog.findViewById(R.id.tv_amount);
                        ImageView merchantImage = dialog.findViewById(R.id.iv_merchant_image);

                        try{

                            date.setText(DateUtils.returnTransactionDate(model.getTransactionDate()));

                        } catch (Exception e){
                            e.printStackTrace();
                        }

                        time.setText(model.getTransactionTime());
                        merchant.setText(model.getCashReceiver());

                        if(!TextUtils.isEmpty(model.getAmountTransacted())) {
                            String amountTrans = "Ksh " + model.getAmountTransacted();
                            amount.setText(amountTrans);
                        }

                        ColorGenerator generator = ColorGenerator.MATERIAL;
                        String letter = model.getCashReceiver().substring(0,1);
                        int color = generator.getRandomColor();
                        TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig()
                                .endConfig().round();
                        TextDrawable drawable = builder.build(letter, color);
                        merchantImage.setImageDrawable(drawable);


                        dialog.show();

                    }
                });
            }

            @Override
            public MpesaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return super.onCreateViewHolder(parent, viewType);
            }

        };

        recyclerView.setAdapter(FeedbackFirebaseRecyclerAdapter);
        mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);


        return view;
    }

    public static class MpesaViewHolder extends RecyclerView.ViewHolder{

        View mView;

        private TextView mMerchant, mFeedback, mDate, mTime;

        private ImageView mMerchantImage, mEmoji;

        public MpesaViewHolder(View itemView) {
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
