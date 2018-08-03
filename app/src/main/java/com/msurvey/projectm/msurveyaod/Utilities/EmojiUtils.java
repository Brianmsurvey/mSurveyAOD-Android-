package com.msurvey.projectm.msurveyaod.Utilities;

import android.widget.ImageView;

import com.msurvey.projectm.msurveyaod.R;

public class EmojiUtils {


    public static void parseResponseToEmoji(String ovrResponse, ImageView imageView){

        switch (ovrResponse){

            case "Excellent":
                imageView.setImageResource(R.drawable.veryhappy);
                break;

            case "Good":
                imageView.setImageResource(R.drawable.happy);
                break;

            case "Okay":
                imageView.setImageResource(R.drawable.neutral);
                break;

            case "Poor":
                imageView.setImageResource(R.drawable.sad);
                break;
        }
    }
}
