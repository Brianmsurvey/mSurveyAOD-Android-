package com.msurvey.projectm.msurveyaod;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class fragment_surveys extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView surveysView = (RecyclerView) inflater.inflate(R.layout.fragment_surveys, container, false);

        ContentAdapter adapter = new ContentAdapter(surveysView.getContext());
        surveysView.setAdapter(adapter);
        surveysView.setHasFixedSize(true);
        surveysView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return surveysView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView avator;
        public TextView survey;
        public TextView questions_no;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.survey_row, parent, false));

            avator = itemView.findViewById(R.id.civ_survey_avatar);
            survey = itemView.findViewById(R.id.tv_survey);;
            questions_no = itemView.findViewById(R.id.tv_questions_no);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }


    }


    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {

        // Set numbers of List in RecyclerView.
        private static final int LENGTH = 5;

        private final String[] mSurveys;
        private final String[] mQuestionsNo;


        public ContentAdapter(Context context) {
            Resources resources = context.getResources();
            mSurveys = resources.getStringArray(R.array.surveys);
            mQuestionsNo = resources.getStringArray(R.array.no_questions);
//            TypedArray a = resources.obtainTypedArray(R.array.place_avator);
//            mPlaceAvators = new Drawable[a.length()];
//            for (int i = 0; i < mPlaceAvators.length; i++) {
//                mPlaceAvators[i] = a.getDrawable(i);
//            }
//            a.recycle();
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.survey.setText(mSurveys[position % mSurveys.length]);
            holder.questions_no.setText(mQuestionsNo[position % mQuestionsNo.length]);
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}
