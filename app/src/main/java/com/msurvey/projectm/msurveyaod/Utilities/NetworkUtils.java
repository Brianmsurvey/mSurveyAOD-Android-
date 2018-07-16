package com.msurvey.projectm.msurveyaod.Utilities;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.msurvey.projectm.msurveyaod.fragment_profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static String phoneNumber = "0";

    private static String current_db_url = "https://dev.msurvey.co.ke:8984/solr/leen/select?q=commId:%22%2B" + NetworkUtils.getPhoneNumber() + "%22&fq=-surveyIncentive:0&wt=json&rows=40";

    private static final String testUrl = "https://dev.msurvey.co.ke:8984/solr/leen/select?q=commId:%22%2B254713740504%22&fq=-surveyIncentive:0&wt=json&rows=40";

    private static final String BASE_URL = "https://dev.msurvey.co.ke:8984/solr/leen/select?";


    private static String airtimeEarned = "0";

    private static String surveysCompletedNo = "0";


    //Output
//    q: query (see below)
//    wt: output format. It can be xml, csv, json, xlsx, xslt and etc
//    rows: number of records to return
//    start: specify the offset where it will start to return data.
//            sort: sort by specific fields (sort=<field name>+<direction>,<field name>+<direction>]) (eg: sort=sentTime desc)
//    fl: limit number of fields in return

    //https://dev.msurvey.co.ke:8984/solr/leen/select?q=commId:%22%2B254713740504%22&fq=-surveyIncentive:0&wt=json

    private static final String format = "json";
    private static final String rows = "10";
    private static final String commId = "commId:%22%@B";
    private static final String commSuffix = "%22";
    private static final String incentiveConstraint = "-surveyIncentive:0";


    final static String QUERY_PARAM = "q";
    final static String FILTER_PARAM = "fq";
    final static String FORMAT_PARAM = "wt";
    final static String ROWS_PARAM = "rows";
    final static String FIELDS_PARAM = "fl";

    public static URL buildUrl(String phoneNumberQuery) {

        String finalPhoneNoQuery = commId + phoneNumberQuery.substring(1) + commSuffix;

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, finalPhoneNoQuery)
                .appendQueryParameter(FILTER_PARAM, incentiveConstraint)
                .appendQueryParameter(FORMAT_PARAM, format)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;

    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }

        } finally {
            urlConnection.disconnect();
        }
    }

    public static class ProfileAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... url) {

            if (url.length == 0) {
                //no url
                return null;
            }


            String stream;
            String urlString = url[0];
            Log.e(TAG, urlString);

            HTTPDataHandler httpDatahandler = new HTTPDataHandler();
            stream = httpDatahandler.GetHTTPData(urlString);
            Log.e(TAG, stream);

            return stream;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //Log.e(TAG, s);

            if (s != null) {

                try {
                    //Get HTTP as JSONObject
                    JSONObject reader = new JSONObject(s);


                    //Get JSON Object Results
                    String response = reader.getString("response");
                    JSONObject outerDocs = new JSONObject(response);
                    JSONArray docsArray = outerDocs.getJSONArray("docs");
                    String[] docs = new String[docsArray.length()];
                    for (int i = 0; i < docsArray.length(); i++) {
                        docs[i] = docsArray.getString(i);
                    }

                    JSONObject profileJSON = new JSONObject(docs[0]);

                    //Set profile information
                    //profile.setCommId(profileJSON.getString("commId"));
                    String phoneNum = profileJSON.getString("commId");

                    ArrayList<String> survIncentives = new ArrayList<>();

//                    String mes = "The length of docs is : " + String.valueOf(docs.length);
//                    //Log.e(TAG, mes);
//                    JSONObject current = new JSONObject(docs[3]);
//                    mes = "The current incentive is : " + current.getString("surveyIncentive");
//                    Log.e(TAG, mes);

                    for (int i = 0; i < docs.length; i++) {
                        JSONObject current = new JSONObject(docs[i]);

                        //Log.e(TAG, current.getString("surveyIncentive"));

                        if (current.getString("surveyIncentive") != null) {
                            survIncentives.add(current.getString("surveyIncentive"));
                        }

                    }


                    int incentivesNo = survIncentives.size();
                    int totalAirtimeEarned = 0;
//
                    for (int i = 0; i < incentivesNo; i++) {

                        totalAirtimeEarned = totalAirtimeEarned + Integer.parseInt(survIncentives.get(i));
                    }
//
                    String value = String.valueOf(totalAirtimeEarned);

                    //profile.setAirtimeEarned(value);

                    fragment_profile f = new fragment_profile();
                    Bundle args = new Bundle();
                    args.putString("airtime", value);
                    f.setArguments(args);
                    String mess = "The bundle contains some of that: " + args.get("airtime");
                    Log.e(TAG, mess);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (s != null) {

            }

        }

    }



public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getTestUrl() {
        return testUrl;
    }

    public static void setCurrent_db_url(String phoneNumber) {
        NetworkUtils.current_db_url = "https://dev.msurvey.co.ke:8984/solr/leen/select?q=commId:%22%2B"+ phoneNumber +"%22&fq=-surveyIncentive:0&wt=json&rows=40";
    }


    public static String getCurrent_db_url() {
        return current_db_url;
    }

    public static String getAirtimeEarned() {
        return airtimeEarned;
    }

    public static void setAirtimeEarned(String airtimeEarned) {
        NetworkUtils.airtimeEarned = airtimeEarned;
    }

    public static String getPhoneNumber() {
        return phoneNumber;
    }

    public static void setPhoneNumber(String phoneNumber) {
        NetworkUtils.phoneNumber = phoneNumber;
    }

    public static String getSurveysCompletedNo() {
        return surveysCompletedNo;
    }

    public static void setSurveysCompletedNo(String surveysCompletedNo) {
        NetworkUtils.surveysCompletedNo = surveysCompletedNo;
    }
}
