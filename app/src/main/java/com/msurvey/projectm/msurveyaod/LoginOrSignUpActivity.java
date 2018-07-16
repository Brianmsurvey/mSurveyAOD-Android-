package com.msurvey.projectm.msurveyaod;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.msurvey.projectm.msurveyaod.Utilities.HTTPDataHandler;
import com.msurvey.projectm.msurveyaod.Utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class LoginOrSignUpActivity extends AppCompatActivity {

    private static final String EMAIL = "email";

    private static final String TAG = "Log in says this: ";

    private final static int REQUEST_CODE = 999;

    Button signup, loginButton;

    private LinearLayout layoutRest;

    private TextView SigningIn;

    private ProgressBar progressBar;

    CallbackManager callbackManager = CallbackManager.Factory.create();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loginorsignup);


        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();


        loginButton = findViewById(R.id.login_button);
        //loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        signup = findViewById(R.id.btn_signup);

        layoutRest = findViewById(R.id.layout_rest);

        SigningIn = findViewById(R.id.tv_signing_in);

        progressBar = findViewById(R.id.progress_bar);


        // Callback registration
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

                NetworkUtils.setPhoneNumber("254713740504");
                new ProfileAsyncTask().execute(NetworkUtils.getTestUrl());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code

                Log.e(TAG, exception.toString());
                Log.e(TAG, "The fblogin is definitey breaking");
            }
        });




        //User has no AOD profile associated with this device.
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if(NetworkUtils.getPhoneNumber().equals("0")){
//                    new ProfileAsyncTask().execute(NetworkUtils.getTestUrl());
//                }else{
//                    Intent mainIntent = new Intent(LoginOrSignUpActivity.this, MainActivity.class);
//                    startActivity(mainIntent);
//                }

                startLoginPage(LoginType.PHONE);


            }
        });



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent loginIntent = new Intent(LoginOrSignUpActivity.this, LoginActivity.class);
//                startActivity(loginIntent);

                //phoneLogin(loginButton);

                //Facebook auth logic



                LoginManager.getInstance().logInWithReadPermissions(LoginOrSignUpActivity.this, Arrays.asList("public_profile"));

            }
        });

    }



    private class ProfileAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            SigningIn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            layoutRest.setVisibility(View.INVISIBLE);

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




                    Intent mainIntent = new Intent(LoginOrSignUpActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();

                    SigningIn.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    layoutRest.setVisibility(View.VISIBLE);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE){

            AccountKitLoginResult result = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if(result.getError() != null){
                Toast.makeText(this, "" + result.getError().getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            else if(result.wasCancelled()){

                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
                return;
            }else{

                if(result.getAccessToken() != null){
                    Toast.makeText(this, "Success  !  %s" + result.getAccessToken().getAccountId(), Toast.LENGTH_SHORT).show();

                    com.facebook.accountkit.AccessToken accessToken = AccountKit.getCurrentAccessToken();


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
                                    NetworkUtils.setCurrent_db_url(phoneNumberString);

                                    new ProfileAsyncTask().execute(NetworkUtils.getTestUrl());
                                }

                            }

                            @Override
                            public void onError(final AccountKitError error) {
                                // Handle Error
                                Log.e(TAG, error.toString());


                            }

                        });
                }else {
                    //Toast.makeText(this, "Result ! %s" + result.getAuthorizationCode(), Toast.LENGTH_SHORT).show();
                }


                //The person has no signed in and given us their number so we need to use that

//                if(NetworkUtils.getPhoneNumber().equals("0")){
//
//                    new ProfileAsyncTask().execute(NetworkUtils.getCurrent_db_url());
//                }
//
//                startActivity(new Intent(this, MainActivity.class));
            }


        }
    }

    public void startLoginPage(LoginType type){

        if(type == LoginType.PHONE) {

            Intent intent = new Intent(this, AccountKitActivity.class);
            AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                    new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE,
                            AccountKitActivity.ResponseType.TOKEN);

            intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());

            startActivityForResult(intent, REQUEST_CODE);
        }

    }

}
