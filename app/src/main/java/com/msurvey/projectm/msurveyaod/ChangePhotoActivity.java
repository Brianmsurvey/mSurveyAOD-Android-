package com.msurvey.projectm.msurveyaod;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.msurvey.projectm.msurveyaod.Utilities.PhotoUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ChangePhotoActivity extends AppCompatActivity {

    private static final String TAG = "SelectImageActivity";
    public static final String EXTRA_POSITION = "position";
    private static final int GALLERY_INTENT = 2;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int CAMERA_PERMISSION_REQUEST = 1;

    private FloatingActionButton mfabCamera;
    private Button mNextButton;
    private ImageView mTakenPhoto;
    private Uri mImageUri = null;

    private String item_image = null;
    private Bitmap bitmap = null;
    private ProgressBar mCircularProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changephoto);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Add Photo");



        mTakenPhoto = (ImageView) findViewById(R.id.civ_taken_photo);

        if(PhotoUtils.getResultImageUri() != null){

            Picasso.get().load(PhotoUtils.getResultImageUri()).resize(660, 660).centerInside().into(mTakenPhoto);

        }

        if(getIntent().getExtras()!= null)
        {

            final Context context = this;
//            Picasso.with(context).load(post_image).resize(660, 660).centerInside().onlyScaleDown().networkPolicy(NetworkPolicy.OFFLINE).into(mTakenPhoto, new Callback() {
//                @Override
//                public void onSuccess() {
//
//                }
//
//                @Override
//                public void onError() {
//                    Picasso.with(context).load(post_image).resize(660, 660).centerInside().onlyScaleDown().into(mTakenPhoto);
//                }
//            });
        }



        mTakenPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

//        mfabCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                checkForCameraPermission();
//
//                CropImage.activity()
//                        .setAspectRatio(5, 5)
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .start(ChangePhotoActivity.this);
//
//            }
//        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {


        }

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

            mImageUri = data.getData();

            PhotoUtils.setResultImageUri(data.getData());

            CropImage.activity(mImageUri)
                    .setAspectRatio(5, 5)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                mImageUri = resultUri;
                mTakenPhoto.setImageURI(resultUri);
                PhotoUtils.setResultImageUri(resultUri);
                Picasso.get().load(resultUri).resize(660, 660).centerInside().into(mTakenPhoto);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void checkForCameraPermission()
    {
        int permissionCheck = ContextCompat.checkSelfPermission(ChangePhotoActivity.this,
                android.Manifest.permission.CAMERA);


        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(ChangePhotoActivity.this, new String[]{android.Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERMISSION_REQUEST)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {

            }
        }
        else
        {

        }
        return;
    }

    public void saveCurrentProfileImage()
    {

        final String PREFS_NAME = "my_preferences";
        final String imageFound = "image_found";
        final String resultUri = PhotoUtils.getResultImageUri().toString();

        //Get the Shared Preferences
        SharedPreferences preferences = this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        preferences.edit().putString(imageFound, resultUri).apply();
        Log.e(TAG, preferences.getString(imageFound, resultUri));

        //Close the OnBoardingActivity
        finish();
    }

}