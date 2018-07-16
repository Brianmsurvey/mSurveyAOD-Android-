package com.msurvey.projectm.msurveyaod;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.msurvey.projectm.msurveyaod.Utilities.NetworkUtils;
import com.msurvey.projectm.msurveyaod.Utilities.PhotoUtils;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class fragment_profile extends Fragment {

    private RecyclerView recyclerView;
    private TextView AirtimeEarned;
    private TextView SurveysCompleted;
    private CircleImageView mAvator;

    private static final String TAG = "Fragment profile says: ";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View profileFragmentView = inflater.inflate(R.layout.fragment_profile, container, false);

        recyclerView = profileFragmentView.findViewById(R.id.rv_recents);

        AirtimeEarned = profileFragmentView.findViewById(R.id.airtime_earned_no);

        SurveysCompleted = profileFragmentView.findViewById(R.id.questions_completed_no);

        mAvator = profileFragmentView.findViewById(R.id.civ_profile_avator);

        AirtimeEarned.setText(NetworkUtils.getAirtimeEarned());

        SurveysCompleted.setText(NetworkUtils.getSurveysCompletedNo());

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

        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return profileFragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(PhotoUtils.getResultImageUri() != null){

            Picasso.get().load(PhotoUtils.getResultImageUri()).resize(660, 660).centerInside().into(mAvator);

        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView question;
        public TextView responses;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.recents_row, parent, false));

            question = itemView.findViewById(R.id.tv_question);
            responses = itemView.findViewById(R.id.tv_response);

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

        private final String[] mQuestions;
        private final String[] mResponses;


        public ContentAdapter(Context context) {
            Resources resources = context.getResources();
            mQuestions = resources.getStringArray(R.array.questions);
            mResponses = resources.getStringArray(R.array.responses);
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
            holder.question.setText(mQuestions[position % mQuestions.length]);
            holder.responses.setText(mResponses[position % mResponses.length]);
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }

}
