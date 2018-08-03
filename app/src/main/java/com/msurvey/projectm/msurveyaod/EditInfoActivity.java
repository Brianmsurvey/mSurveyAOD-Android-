package com.msurvey.projectm.msurveyaod;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.msurvey.projectm.msurveyaod.Utilities.PhotoUtils;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditInfoActivity extends AppCompatActivity {

    private EditText mFirstName, mLastName;
    private FloatingActionButton mFab;

    private CircleImageView mAvator;
    private Uri mImageUri = null;
    private static final int GALLERY_INTENT = 2;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int CAMERA_PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editinfo);

        mFirstName = findViewById(R.id.et_firstName);
        mLastName = findViewById(R.id.et_lastName);
        mAvator = findViewById(R.id.civ_avator_edit);
        mFab = findViewById(R.id.fabSend);

        //Get the Shared Preferences
        final SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
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

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent, GALLERY_INTENT);

            }
        });


        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                final String userName = "name";

                if(!TextUtils.isEmpty(mFirstName.getText()) && !TextUtils.isEmpty(mLastName.getText())){

                    String name = mFirstName.getText() + " " + mLastName.getText();
                    preferences.edit().putString(userName, name).apply();

                    Toast.makeText(EditInfoActivity.this, "Changes saved", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(EditInfoActivity.this, BottomNavActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(EditInfoActivity.this, "Enter Details", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();

        if(PhotoUtils.getResultImageUri() != null){

            Picasso.get().load(PhotoUtils.getResultImageUri()).resize(660, 660).centerInside().into(mAvator);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
                mAvator.setImageURI(resultUri);
                PhotoUtils.setResultImageUri(resultUri);
                Picasso.get().load(resultUri).resize(660, 660).centerInside().into(mAvator);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
