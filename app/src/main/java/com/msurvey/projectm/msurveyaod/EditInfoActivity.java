package com.msurvey.projectm.msurveyaod;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.msurvey.projectm.msurveyaod.Utilities.DateUtils;
import com.msurvey.projectm.msurveyaod.Utilities.NetworkUtils;
import com.msurvey.projectm.msurveyaod.Utilities.PhotoUtils;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditInfoActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener{

    private TextView mDob, mGender, mLocation;
    private Toolbar toolbar;
    private EditText mFullName;
    private FloatingActionButton mFab;

    private StorageReference storageReference;

    private String userNumber, age, provider;

    private LocationManager locationManager;

    private CircleImageView mAvator;
    private Uri mImageUri = null;
    private static final int GALLERY_INTENT = 2;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int CAMERA_PERMISSION_REQUEST = 1;

    private static final String TAG = EditInfoActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editinfo);

        mDob = findViewById(R.id.tv_dob);
        mFullName = findViewById(R.id.et_fullName);
        mLocation = findViewById(R.id.tv_location);
        mGender = findViewById(R.id.tv_gender);

        mAvator = findViewById(R.id.iv_change_avator);
        mFab = findViewById(R.id.fabSend);

        age = "";


        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageReference = storage.getReference();
        final StorageReference imagesRef = storageReference.child("images");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Edit Profile");

        //Get the Shared Preferences
        final SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        final String imageFound = "image_found";
        final String userNumberPrefs = "phoneNumber";

        if(!preferences.getString(imageFound, "").equals("")) {

            Uri image = Uri.parse(preferences.getString(imageFound, ""));
            Picasso.get().load(image).resize(660, 660).centerInside().into(mAvator);

        }


        userNumber = NetworkUtils.getPhoneNumber().replace("+", "");

        if(PhotoUtils.getResultImageUri() != null){

            mAvator.setPadding(0, 0, 0, 0);
            Picasso.get().load(PhotoUtils.getResultImageUri()).resize(660, 660).centerInside().into(mAvator);

        }


        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String day = String.valueOf(datePicker.getDayOfMonth());
                String month = String.valueOf(datePicker.getMonth() + 1);
                String year = String.valueOf(datePicker.getYear());
                String collectiveDate = day + "/" + month + "/" + year;

                try{

                    collectiveDate = DateUtils.returnDate(collectiveDate);

                }catch (Exception e){

                }

                age = DateUtils.getAge(datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth());

                mDob.setText(collectiveDate);
                mDob.setTextColor(ContextCompat.getColor(EditInfoActivity.this, R.color.colorDarkGrey));
            }
        };


        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.MyDialogTheme, listener, 1990, 1, 1);


        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLocationDialog();
            }
        });

        mGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGenderDialog();
            }
        });

        mDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePickerDialog.show();

            }
        });


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
            public void onClick(final View view) {

                final SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                final String userName = "name";

                if(!TextUtils.isEmpty(mFullName.getText())){

                    String name = mFullName.getText().toString();
                    preferences.edit().putString(userName, name).apply();

                    Snackbar.make(view, "Saving...", Snackbar.LENGTH_LONG).show();


                    //If user went with something from the gallery instead

                    if(mImageUri != null) {
                        final StorageReference filePath = imagesRef.child(mImageUri.getLastPathSegment());

                        filePath.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                //Continue with the task to get the download URL
                                return filePath.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();

                                    DatabaseReference mUserDatabase;

                                    mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userNumber);

                                    String name = mFullName.getText().toString();

                                    mUserDatabase.child("name").setValue(name);
                                    if(!TextUtils.equals(mDob.getText().toString(), "Date Of Birth")) mUserDatabase.child("userDob").setValue(mDob.getText().toString());
                                    if(!TextUtils.equals(age, "")) mUserDatabase.child("userAge").setValue(age);
                                    if(!TextUtils.equals(mGender.getText().toString(), "Gender")) mUserDatabase.child("userGender").setValue(mGender.getText().toString());
                                    if(!TextUtils.equals(mLocation.getText().toString(), "Location")) mUserDatabase.child("location").setValue(mLocation.getText().toString());
                                    mUserDatabase.child("avatorImage").setValue(downloadUri.toString());

                                    mDob.setText("Date of Birth");
                                    mLocation.setText("Location");

                                    Snackbar.make(view, "Changes saved", Snackbar.LENGTH_SHORT).show();

                                    Intent intent = new Intent(EditInfoActivity.this, MainActivity.class);
                                    startActivity(intent);

                                } else {

                                    //Handle failures
                                }
                            }
                        });




                    }else{

                        DatabaseReference mUserDatabase;

                        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userNumber);

                        name = mFullName.getText().toString();

                        mUserDatabase.child("name").setValue(name);
                        if(!TextUtils.equals(mDob.getText().toString(), "Date Of Birth")) mUserDatabase.child("userDob").setValue(mDob.getText().toString());
                        if(!TextUtils.equals(age, "")) mUserDatabase.child("userAge").setValue(age);
                        if(!TextUtils.equals(mLocation.getText().toString(), "Location")) mUserDatabase.child("location").setValue(mLocation.getText().toString());
                    }
                }else{
                    Snackbar.make(view, "Enter Details", Snackbar.LENGTH_SHORT).show();
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
                PhotoUtils.setResultImageUri(resultUri);
                mAvator.setPadding(0, 0, 0, 0);
                Picasso.get().load(mImageUri).resize(660, 660).centerInside().into(mAvator);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String day = String.valueOf(datePicker.getDayOfMonth());
        String month = String.valueOf(datePicker.getMonth() + 1);
        String year = String.valueOf(datePicker.getYear());


        String collectiveDate = day + "/" + month + "/" + year;

        try{

            collectiveDate = DateUtils.returnDate(collectiveDate);

        }catch (Exception e){

        }

        Log.e(TAG, collectiveDate);
        age = DateUtils.getAge(datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth());
        mDob.setText(collectiveDate);
    }


    public void showGenderDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(EditInfoActivity.this);
        builder.setTitle("Gender");

        final String[] items = getResources().getStringArray(R.array.genders);

        mGender.setText(items[0]);

        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Item Selected
                final String name = items[i];
                mGender.setText(name);
                mGender.setTextColor(ContextCompat.getColor(EditInfoActivity.this, R.color.colorDarkGrey));
            }
        });


        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                        final String result = mGender.getText().toString();

                        dialog.dismiss();
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();

    }


    public void showLocationDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(EditInfoActivity.this);
        builder.setTitle("Location");


        final String[] items = getResources().getStringArray(R.array.location);

        mLocation.setText(items[0]);
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener(){


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Item Selected
                final String name = items[i];
                mLocation.setText(name);
                mLocation.setTextColor(ContextCompat.getColor(EditInfoActivity.this, R.color.colorDarkGrey));
            }
        });


        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                        String thing = mLocation.getText().toString();

                        dialog.dismiss();
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();

    }
}
