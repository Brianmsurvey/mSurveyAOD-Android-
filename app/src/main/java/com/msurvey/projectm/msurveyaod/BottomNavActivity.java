package com.msurvey.projectm.msurveyaod;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.msurvey.projectm.msurveyaod.Utilities.SmsBroadCastReceiver;

import java.util.ArrayList;
import java.util.List;

public class BottomNavActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    BottomNavigationView bottomNavigationView;

    private ViewPager viewPager;

    private RecyclerView recyclerView;

    private LinearLayoutManager mLayoutManager;

    private static final String TAG = "MainActivity.java";

    private TextView user_email;
    private TextView user_name;

    private String accountEmail;
    private String accountId;
    private String accountPhone;

    private SmsBroadCastReceiver mSmsReceiver;

    //Firebase
    private DatabaseReference mFeedbackDatabase;
    //Firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mUserDatabase = database.getReference("Users");

    //Permissions
    private static final int MY_PERMISSIONS_REQUEST_READ_SMS = 1;
    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.frag_profile);

        AccessToken accessToken = AccountKit.getCurrentAccessToken();

        if (accessToken != null) {
            //Handle Returning User
        } else {
            //Handle new or logged out user
        }

        // Adding Toolbar to Main screen
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setElevation(0);

        // Create Navigation drawer and inlfate layout
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        recyclerView = findViewById(R.id.rv_feedbackhistory);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        //loadFragment(new fragment_profile());


        viewPager = findViewById(R.id.frag_viewpager);
        setupViewPager(viewPager);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectedFragment = null;

                switch (item.getItemId()){

                    case R.id.item_profile:
                        viewPager.setCurrentItem(0);
                        break;

                    case R.id.item_surveys:
                        viewPager.setCurrentItem(1);
                        break;

                    case R.id.item_cashbacks:
                        viewPager.setCurrentItem(2);
                        break;

                }


                return false;
            }

        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(bottomNavigationView.getMenu().getItem(position) != null){
                    bottomNavigationView.getMenu().getItem(position).setChecked(false);
                }else{
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }

                bottomNavigationView.getMenu().getItem(position).setChecked(true);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator
                    = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            indicator.setTint(ContextCompat.getColor(this ,R.color.white));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setElevation(0);
            supportActionBar.setTitle("");
        }


        if (ContextCompat.checkSelfPermission(BottomNavActivity.this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(BottomNavActivity.this,
                    new String[]{Manifest.permission.RECEIVE_SMS},
                    MY_PERMISSIONS_REQUEST_RECEIVE_SMS);
        }


        if (ContextCompat.checkSelfPermission(BottomNavActivity.this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted

            Log.e(TAG, "This where we at");
            ActivityCompat.requestPermissions(BottomNavActivity.this,
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

                            Intent feedbackIntent = new Intent(BottomNavActivity.this, AppFeedbackActivity.class);
                            startActivity(feedbackIntent);
                            return true;
                        }else if(id == R.id.item_history){

                            Intent historyIntent = new Intent(BottomNavActivity.this, FeedbackHistoryTest.class);
                            startActivity(historyIntent);
                            return true;
                        }else if(id == R.id.item_newdesign){
                            Intent historyIntent = new Intent(BottomNavActivity.this, ScrollTestActivity.class);
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
//                Snackbar.make(v, "Hello there!",
//                        Snackbar.LENGTH_LONG).show();

                Intent fabIntent = new Intent(BottomNavActivity.this, EditInfoActivity.class);
                startActivity(fabIntent);
            }
        });


        user_name = findViewById(R.id.tv_user_name);
        user_email = findViewById(R.id.tv_user_email);


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


    private boolean loadFragment(Fragment fragment){

//        if(fragment != null){
//
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragment_container, fragment)
//                    .commit();
//            return true;
//        }

        return false;
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        //private final List<String> mFragmentTitleList = new ArrayList<>();

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

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new frag_profile());
        adapter.addFragment(new fragment_surveys());
        adapter.addFragment(new fragment_cashbacks());
        viewPager.setAdapter(adapter);
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
