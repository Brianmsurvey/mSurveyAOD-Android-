package com.msurvey.projectm.msurveyaod;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.msurvey.projectm.msurveyaod.Utilities.HTTPDataHandler;
import com.msurvey.projectm.msurveyaod.Utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1500;

    private final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e(TAG, "We here now");

        setContentView(R.layout.activity_splashscreen2);

        final SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        final String onboardingComplete = "onboarding_complete";

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

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
                                String phoneNumberString = phoneNumber.toString().substring(1);
                                NetworkUtils.setPhoneNumber(phoneNumberString);
                                Log.e(TAG, phoneNumberString);
                            }

                            // Get email
                            String email = account.getEmail();


                            //We're assuming this person already has an AOD profile
                            //We get their phone number and then set the current db url accordingly.
                            NetworkUtils.setCurrent_db_url(NetworkUtils.getPhoneNumber());

                            new ProfileAsyncTask().execute(NetworkUtils.getCurrent_db_url());


//                            if(NetworkUtils.getPhoneNumber().equals("0")){
//                                new ProfileAsyncTask().execute(NetworkUtils.getCurrent_db_url());
//                            }

//                            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
//                            startActivity(mainIntent);
//                            finish();
                        }

                        @Override
                        public void onError(final AccountKitError error) {
                            // Handle Error
                            Log.e(TAG, error.toString());


                        }

                    });
                }else{

                    //If the user has no account registered to this device, take them to the login or sign in screen
                    Log.e(TAG, "Not really actually");
                    Intent loginorsignupIntent = new Intent(SplashActivity.this, LoginOrSignUpActivity.class);
                    startActivity(loginorsignupIntent);
                    finish();
                }
            }

        }, SPLASH_TIME_OUT);
    }


    private class ProfileAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            SigningIn.setVisibility(View.VISIBLE);
//            progressBar.setVisibility(View.VISIBLE);
//            layoutRest.setVisibility(View.INVISIBLE);

        }

        @Override
        protected String doInBackground(String... url) {

            if(url.length == 0) {
                //no url
                return null;
            }


            String stream;
            String urlString = url[0];

            HTTPDataHandler httpDatahandler = new HTTPDataHandler();
            stream = httpDatahandler.GetHTTPData(urlString);

            return stream;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if(s != null){

                try{
                    //Get HTTP as JSONObject
                    JSONObject reader = new JSONObject(s);


                    //Get JSON Object Results
                    String response = reader.getString("response");
                    JSONObject outerDocs = new JSONObject(response);

                    JSONArray docsArray = outerDocs.getJSONArray("docs");
                    String[] docs = new String[docsArray.length()];
                    for(int i = 0; i<docsArray.length(); i++){
                        docs[i] = docsArray.getString(i);
                    }

                    JSONObject profileJSON = new JSONObject(docs[0]);

//                    Retrieve Phone Number
                    String phoneNum = profileJSON.getString("commId");

//                    String Array to capture financial incentive so far;
                    ArrayList<String> survIncentives = new ArrayList<>();


                    //Retrieve the current incentive from every row entry
                    for(int i=0; i<docs.length; i++){
                        JSONObject current = new JSONObject(docs[i]);

//                      Add incentives to the String array
                        if(current.getString("surveyIncentive") != null){
                            survIncentives.add(current.getString("surveyIncentive"));
                        }

                    }

                    //Retrieve Number of surveys done
                    int incentivesNo = survIncentives.size();

                    //Compute total airtime earned
                    int totalAirtimeEarned = 0;
//
                    for(int i=0; i<incentivesNo; i++){

                        totalAirtimeEarned = totalAirtimeEarned + Integer.parseInt(survIncentives.get(i));
                    }
//
                    String airtime = String.valueOf(totalAirtimeEarned);
                    //

                    //Pass the values to the profile fragment via the NetworkUtils class
                    NetworkUtils.setAirtimeEarned(airtime);
                    NetworkUtils.setPhoneNumber(phoneNum);
                    NetworkUtils.setSurveysCompletedNo(String.valueOf(incentivesNo));




                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();

//                    SigningIn.setVisibility(View.INVISIBLE);
//                    progressBar.setVisibility(View.INVISIBLE);
//                    layoutRest.setVisibility(View.VISIBLE);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            if(s != null){

            }

        }

    }
}
