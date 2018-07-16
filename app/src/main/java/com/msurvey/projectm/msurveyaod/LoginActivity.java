package com.msurvey.projectm.msurveyaod;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.msurvey.projectm.msurveyaod.Utilities.HTTPDataHandler;
import com.msurvey.projectm.msurveyaod.Utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity{

    private TextInputLayout phoneNoInputLayout;

    private TextInputLayout passwordInputLayout;

    private EditText phoneNoEditText;

    private EditText passwordEditText;

    private Button signInButton;

    private ProgressBar progressBar;

    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        //UI
        phoneNoInputLayout = findViewById(R.id.phoneno_input_layout_name);
        passwordInputLayout = findViewById(R.id.password_input_layout_name);
        phoneNoEditText = findViewById(R.id.et_phoneno);
        passwordEditText = findViewById(R.id.et_password);
        progressBar = findViewById(R.id.progress_bar);
        signInButton = findViewById(R.id.login_button);

        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                login();

            }
        });

    }


    public void login(){

        String phoneNumber = phoneNoEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if(validatePhoneNumber(phoneNumber)){

            phoneNumber = "254" + phoneNumber.substring(1);
            NetworkUtils.setPhoneNumber(phoneNumber);
            NetworkUtils.setCurrent_db_url(phoneNumber);
            if(!NetworkUtils.getPhoneNumber().equals("0")){
                //Log.e(TAG, NetworkUtils.getCurrent_db_url());
                new ProfileAsyncTask().execute(NetworkUtils.getCurrent_db_url());
            }

        }

    }


    public boolean validatePhoneNumber(String PhoneNumber)
    {

        String firstNos = PhoneNumber.substring(0, 2);

        if(!firstNos.equals("07")){
            phoneNoInputLayout.setErrorEnabled(true);
            phoneNoInputLayout.setError("Not a valid phone number");
            return false;
        }

        if(PhoneNumber.length() <10){
            phoneNoInputLayout.setError("Not long enough");
            return false;
        }else if(PhoneNumber.length() > 10){
            phoneNoInputLayout.setError("Too long");
        }else{
            return true;
        }

        return true;
    }

    private class ProfileAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            signInButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

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




                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(mainIntent);

                    progressBar.setVisibility(View.INVISIBLE);
                    signInButton.setVisibility(View.VISIBLE);

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
