package com.msurvey.projectm.msurveyaod;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.msurvey.projectm.msurveyaod.Utilities.LocationUtils;
import com.msurvey.projectm.msurveyaod.Utilities.SmsBroadCastReceiver;
import com.msurvey.projectm.msurveyaod.Utilities.SmsUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private DrawerLayout mDrawerLayout;

    BottomNavigationView bottomNavigationView;

    private ViewPager viewPager;

    private CircleImageView mNavator;

    private RecyclerView recyclerView;

    private LinearLayoutManager mLayoutManager;

    private static final String TAG = "MainActivity.java";

    private TextView user_email;
    private TextView user_name;

    private String accountEmail;
    private String accountId;
    private String accountPhone;
    private String provider;

    private SmsBroadCastReceiver mSmsReceiver;
    private LocationManager locationManager;

    //Firebase
    private DatabaseReference mFeedbackDatabase;
    //Firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mUserDatabase = database.getReference("Users");

    //Permissions
    private static final int MY_PERMISSIONS_REQUEST_READ_SMS = 1;
    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 2;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        AccessToken accessToken = AccountKit.getCurrentAccessToken();

        if (accessToken != null) {
            //Handle Returning User
        } else {
            //Handle new or logged out user
        }


        // Create Navigation drawer and inlfate layout
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.inflateHeaderView(R.layout.navheader);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        user_name = headerView.findViewById(R.id.tv_user_name);
        user_email = headerView.findViewById(R.id.tv_user_email);
        mNavator = headerView.findViewById(R.id.civ_user_image);

        recyclerView = findViewById(R.id.rv_feedbackhistory);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        String userPhoneNumber = "";

        //If this user has an account
        if (accessToken != null) {


            userPhoneNumber = getSharedPreferences("my_preferences", MODE_PRIVATE).getString("phoneNumber", "");
        }


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userPhoneNumber);


        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                final String image = user.getAvatorImage();

                if (!TextUtils.isEmpty(image)) {
                    Picasso.get().load(image).resize(660, 660).centerInside().onlyScaleDown().networkPolicy(NetworkPolicy.OFFLINE).into(mNavator, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(image).resize(660, 660).onlyScaleDown().centerInside().into(mNavator);
                        }

                    });
                }

                if (!TextUtils.isEmpty(user.getName())) user_name.setText(user.getName());

                String location = user.getLocation() + ", Kenya";

                if (!TextUtils.isEmpty(user.getLocation())) user_email.setText(location);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        viewPager = findViewById(R.id.frag_viewpager);
        setupViewPager(viewPager);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectedFragment = null;

                switch (item.getItemId()) {

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
                if (bottomNavigationView.getMenu().getItem(position) != null) {
                    bottomNavigationView.getMenu().getItem(position).setChecked(false);
                } else {
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
            indicator.setTint(ContextCompat.getColor(this, R.color.white));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setElevation(0);
            supportActionBar.setTitle("");
        }


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
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

        } else {
            scrapeMpesaSms();
        }


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);

        }else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);
            Location location = locationManager.getLastKnownLocation(provider);

            locationManager.requestLocationUpdates(provider, 1000, 0, this);

            if(location!= null){
                String address = LocationUtils.getAddress(this, location);
                Log.e(TAG, address);
            }

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


                        if (id == R.id.item_about_msurvey) {

                            return true;
                        } else if (id == R.id.item_got_suggestions) {

                            Intent feedbackIntent = new Intent(MainActivity.this, AppFeedbackActivity.class);
                            startActivity(feedbackIntent);
                            return true;

                        } else if (id == R.id.item_dashboard) {

                            Intent dashboardIntent = new Intent(MainActivity.this, DashboardActivity.class);
                            startActivity(dashboardIntent);
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

                Intent fabIntent = new Intent(MainActivity.this, EditInfoActivity.class);
                startActivity(fabIntent);
            }
        });


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

        if (id == R.id.item_logout) {
            AccountKit.logOut();
            if (FirebaseAuth.getInstance() != null) {
                FirebaseAuth.getInstance().signOut();
            }

            Intent loginorsigninIntent = new Intent(this, LoginOrSignUpActivity.class);
            startActivity(loginorsigninIntent);

            unregisterReceiver(mSmsReceiver);

            finish();
        }

        if (id == R.id.item_profile) {

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
        try {

            unregisterReceiver(mSmsReceiver);

        } catch (IllegalArgumentException e) {
            Log.e(TAG, "");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {

            unregisterReceiver(mSmsReceiver);

        } catch (IllegalArgumentException e) {
            Log.e(TAG, "");
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        String address = LocationUtils.getAddress(this, location);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

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
        adapter.addFragment(new fragment_profile());
        adapter.addFragment(new fragment_surveys());
        adapter.addFragment(new fragment_cashbacks());
        viewPager.setAdapter(adapter);
    }


    public void scrapeMpesaSms() {
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

                    mpesaSMS mpesaMessage = SmsUtils.parseGeneralMpesaMessage(strbody);

                    mUserDatabase.child("mpesaData").push().setValue(mpesaMessage);

                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            } else {
                smsBuilder.append("no result!");
            } // end if
        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }

        String result = smsBuilder.toString();
        //Log.e(TAG, result);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_SMS: {
                // If request is cancelled, the result arrays are empty.
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

            case MY_PERMISSIONS_REQUEST_RECEIVE_SMS: {

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

            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the


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
