package com.msurvey.projectm.msurveyaod.Utilities;

import android.net.Uri;

import java.net.URI;

public class PhotoUtils {

    private static Uri resultImageUri = null;


    public static void setResultImageUri(Uri resultImageUri) {
        PhotoUtils.resultImageUri = resultImageUri;
    }

    public static Uri getResultImageUri() {
        return resultImageUri;
    }
}
