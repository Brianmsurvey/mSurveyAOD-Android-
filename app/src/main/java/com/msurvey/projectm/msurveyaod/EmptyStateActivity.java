package com.msurvey.projectm.msurveyaod;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmptyStateActivity extends AppCompatActivity{

    private static final String TAG = "EmptyStateActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_emptystate);

        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    //System.out.println("connected");
                    Intent splashActivityIntent = new Intent(EmptyStateActivity.this, SplashActivity.class);
                    getApplicationContext().startActivity(splashActivityIntent);

                } else {
                    //System.out.println("not connected");


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    //System.out.println("connected");
                    Intent splashActivityIntent = new Intent(EmptyStateActivity.this, SplashActivity.class);
                    getApplicationContext().startActivity(splashActivityIntent);
                    finish();

                } else {
                    //System.out.println("not connected");


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
