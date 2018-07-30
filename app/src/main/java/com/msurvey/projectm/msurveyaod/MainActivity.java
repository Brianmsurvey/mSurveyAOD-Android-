package com.msurvey.projectm.msurveyaod;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.msurvey.projectm.msurveyaod.Utilities.DateUtils;
import com.msurvey.projectm.msurveyaod.Utilities.HTTPDataHandler;
import com.msurvey.projectm.msurveyaod.Utilities.MpesaUtils;
import com.msurvey.projectm.msurveyaod.Utilities.NetworkUtils;
import com.msurvey.projectm.msurveyaod.Utilities.SmsBroadCastReceiver;
import com.msurvey.projectm.msurveyaod.Utilities.SmsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Provides UI for the main screen.
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private CircleImageView mAvator;
    private Profile profile;
    private SmsBroadCastReceiver mSmsReceiver;

    private TextView user_email;
    private TextView user_name;

    private String accountEmail;
    private String accountId;
    private String accountPhone;

    //Firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mUserDatabase = database.getReference("Users");


    private static final String TAG = "MainActivity.java";

    private static final int MY_PERMISSIONS_REQUEST_READ_SMS = 1;
    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "We here now");


        AccessToken accessToken = AccountKit.getCurrentAccessToken();

        if (accessToken != null) {
            //Handle Returning User
        } else {
            //Handle new or logged out user
        }


        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);


        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        // Create Navigation drawer and inlfate layout
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);


        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator
                    = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            indicator.setTint(ContextCompat.getColor(this ,R.color.white));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.RECEIVE_SMS},
                    MY_PERMISSIONS_REQUEST_RECEIVE_SMS);
        }


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted

            Log.e(TAG, "This where we at");
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_SMS},
                    MY_PERMISSIONS_REQUEST_READ_SMS);

        }



        // Set behavior of Navigation drawer
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Set item in checked state
                        menuItem.setChecked(true);

                        int id = menuItem.getItemId();

                        if(id == R.id.item_profile){

                            return true;
                        }

                        else if(id == R.id.item_about_msurvey){

                            return true;
                        }else if(id == R.id.item_got_suggestions){

                            Intent feedbackIntent = new Intent(MainActivity.this, AppFeedbackActivity.class);
                            startActivity(feedbackIntent);
                            return true;
                        }else if(id == R.id.item_history){

                            Intent historyIntent = new Intent(MainActivity.this, FeedbackHistoryTest.class);
                            startActivity(historyIntent);
                            return true;
                        }else if(id == R.id.item_newdesign){
                            Intent historyIntent = new Intent(MainActivity.this, BottomNavActivity.class);
                            startActivity(historyIntent);
                            return true;
                        }

                        // Closing drawer on item click
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
        // Adding Floating Action Button to bottom right of main view
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Hello there!",
                        Snackbar.LENGTH_LONG).show();
            }
        });


        user_name = findViewById(R.id.tv_user_name);
        user_email = findViewById(R.id.tv_user_email);

        //user_email.setText(NetworkUtils.getPhoneNumber());

    }


    @Override
    protected void onResume() {
        super.onResume();

        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(Account account) {

                accountId = account.getId();
                accountEmail = account.getEmail();
                accountPhone = account.getPhoneNumber().toString();

            }

            @Override
            public void onError(AccountKitError accountKitError) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{

            unregisterReceiver(mSmsReceiver);

        }catch (IllegalArgumentException e){
            Log.e(TAG, "");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try{

            unregisterReceiver(mSmsReceiver);

        }catch (IllegalArgumentException e){
            Log.e(TAG, "");
        }

    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new fragment_profile(), "Profile");
        adapter.addFragment(new fragment_surveys(), "Surveys");
        adapter.addFragment(new fragment_cashbacks(), "Cashbacks");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        if(id == R.id.item_logout){
            AccountKit.logOut();
            if(FirebaseAuth.getInstance() != null){
                FirebaseAuth.getInstance().signOut();
            }

            Intent loginorsigninIntent = new Intent(this, LoginOrSignUpActivity.class);
            startActivity(loginorsigninIntent);

            unregisterReceiver(mSmsReceiver);

            finish();
        }

        if(id == R.id.item_profile){

        }

        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    private class ProfileAsyncTask extends AsyncTask<String, Void, String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... url) {

            if(url.length == 0) {
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

            Log.e(TAG, s);

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

                    //Set profile information
                    profile.setCommId(profileJSON.getString("commId"));


                    for(int i=0; i<docs.length; i++){
                        JSONObject current = new JSONObject(docs[i]);

                        //Log.e(TAG, current.getString("surveyIncentive"));

                        if(current.getString("surveyIncentive") != null){
                            profile.setSurveyIncentives(current.getString("surveyIncentive"));
                        }

                    }


                    int incentivesNo = profile.getSurveyIncentives().size();
                    int totalAirtimeEarned = 0;
//
                    for(int i=0; i<incentivesNo; i++){

                        totalAirtimeEarned = totalAirtimeEarned + Integer.parseInt(profile.getSurveyIncentives().get(i));
                    }
//
                    String value = String.valueOf(totalAirtimeEarned);

                    profile.setAirtimeEarned(value);


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


    public void scrapeMpesaSms(){
        StringBuilder smsBuilder = new StringBuilder();
        ArrayList<String> allSms = new ArrayList<>();
        final String SMS_URI_INBOX = "content://sms/inbox";
        final String SMS_URI_ALL = "content://sms/";
        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
            Cursor cur = getContentResolver().query(uri, projection, "address='MPESA'", null, "date desc");
            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");
                do {
                    String strAddress = cur.getString(index_Address);
                    int intPerson = cur.getInt(index_Person);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int int_Type = cur.getInt(index_Type);

                    smsBuilder.append("[ ");
                    smsBuilder.append(strAddress + ", ");
                    smsBuilder.append(intPerson + ", ");
                    smsBuilder.append(strbody + ", ");
                    smsBuilder.append(longDate + ", ");
                    smsBuilder.append(int_Type);
                    smsBuilder.append(" ]\n\n");
                    allSms.add("[ " + strAddress + ", " + intPerson + ", " + strbody + ", " + longDate + ", " + int_Type + " ]");
                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            } else {
                smsBuilder.append("no result!");
            } // end if
        }catch(SQLiteException ex){
            Log.d("SQLiteException", ex.getMessage());
        }

        String result = smsBuilder.toString();
        Log.e(TAG, result);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the

                    scrapeMpesaSms();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_RECEIVE_SMS: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the

                    //scrapeMpesaSms();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;

            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


}
