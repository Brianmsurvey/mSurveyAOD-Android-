package com.msurvey.projectm.msurveyaod.Utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.msurvey.projectm.msurveyaod.MainActivity;
import com.msurvey.projectm.msurveyaod.R;
import com.msurvey.projectm.msurveyaod.User;

import java.util.Random;

public class SmsBroadCastReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsBroadCastReceiver";

    NotificationHelper helper;

    private String serviceProviderNumber;

    private String serviceProviderSmsCondition;

    private Listener listener;

    //Firebase
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mUserDatabase = database.getReference("Users");

    public SmsBroadCastReceiver(String serviceProviderNumber, String serviceProviderSmsCondition){
        this.serviceProviderNumber = serviceProviderNumber;
        this.serviceProviderSmsCondition = serviceProviderSmsCondition;
    }

    public SmsBroadCastReceiver(){};

    final SmsManager sms = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e(TAG, "Message detected");
        //Toast.makeText(context, "SMS received", Toast.LENGTH_SHORT).show();

        StringBuilder sb  = new StringBuilder();
        sb.append("Action: " + intent.getAction() + "\n");
        sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME));

        String log = sb.toString();


        helper = new NotificationHelper(context);

        //Get Message Contents
        final Bundle bundle = intent.getExtras();

        try{

            //If that message is not empty
            if(bundle != null){

                Log.e(TAG, "Message not empty");


                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for(int i=0; i<pdusObj.length; i++){

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    //Sender number/address
                    String senderNum = phoneNumber;

                    //Message that was received
                    String message = currentMessage.getDisplayMessageBody();

                    Log.e(TAG, message);

                    //String notificationTitle = SmsUtils.returnSmsSender(message);
                    String notificationTitle = "Java Coffee House";
                    String howWasYourExperience = "How was your experience at " + notificationTitle + "?";
                    Notification.Builder builder = helper.getChanelNotification(notificationTitle, howWasYourExperience);
                    helper.getManager().notify(new Random().nextInt(), builder.build());


                }
            }
        }catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }

    void setListener(Listener listener){
        this.listener = listener;
    }

    interface Listener{
        void onTextReceived(String text);
    }

    private void addNotification(Context context) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("Notifications Example")
                        .setContentText("This is a test notification");



        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
