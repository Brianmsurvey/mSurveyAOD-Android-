package com.msurvey.projectm.msurveyaod.Utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.msurvey.projectm.msurveyaod.Feedback;
import com.msurvey.projectm.msurveyaod.MainActivity;
import com.msurvey.projectm.msurveyaod.R;
import com.msurvey.projectm.msurveyaod.SplashActivity;
import com.msurvey.projectm.msurveyaod.User;
import com.msurvey.projectm.msurveyaod.mpesaSMS;

import java.util.Random;

public class SmsBroadCastReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsBroadCastReceiver";

    NotificationHelper helper;

    private String serviceProviderNumber;

    private String serviceProviderSmsCondition;

    public static String userPhoneNumber;

    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference mUserDatabase;

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

        database = FirebaseDatabase.getInstance();

        mUserDatabase = database.getReference("Users");
        mUserDatabase.keepSynced(true);

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
                        String phoneNumberString = phoneNumber.toString().replace("+", "");
                        userPhoneNumber = phoneNumberString;
                        Log.e(TAG, userPhoneNumber);
                        NetworkUtils.setPhoneNumber(phoneNumberString);
                    }
                }

                @Override
                public void onError(final AccountKitError error) {
                    // Handle Error
                    Log.e(TAG, error.toString());


                }

            });
        }


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

                    String theSenderIs = "The sender is " + senderNum;
//                    Log.e(TAG, theSenderIs);
//                    Log.e(TAG, message);

                    //Parse message
                    //See if Sender is MPESA
                    //If it is, decompose the message to see who cash is being sent to
                    //Push that information to an MPESA sms object


                    if(senderNum.equals("MPESA")){

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
                                        String phoneNumberString = phoneNumber.toString().replace("+", "");
                                        NetworkUtils.setPhoneNumber(phoneNumberString);
                                    }
                                }

                                @Override
                                public void onError(final AccountKitError error) {
                                    // Handle Error
                                    Log.e(TAG, error.toString());


                                }

                            });
                        }

                        String userNumber = NetworkUtils.getPhoneNumber().replace("+", "");

                        //Checks to see if its a buy goods and services message
                        if(!SmsUtils.returnCashReceiver(message).equals("nothing")){

                            final String CashReceiver = SmsUtils.returnCashReceiver(message);

                            mpesaSMS mpesaSMS;
                            mpesaSMS = SmsUtils.parseSms(message);

                            mUserDatabase.child(userPhoneNumber.replace("+", "")).child("mpesaData").child(mpesaSMS.getTransactionId()).setValue(mpesaSMS);


                            String transactionTime = SmsUtils.returnTransactionTime(message);
                            String transactionDate = SmsUtils.returnTransactionDate(message);
                            String amountTransacted = SmsUtils.returnAmountTransacted(message);
                            final String transactionDateTime = DateUtils.returnFormalDate(transactionDate, transactionTime);

                            FeedbackUtils.cashReceiver = CashReceiver;
                            FeedbackUtils.transactionDate = transactionDate;
                            FeedbackUtils.transactionTime = transactionTime;
                            FeedbackUtils.transactionDateTime = transactionDateTime;
                            FeedbackUtils.userNumber = userPhoneNumber;
                            FeedbackUtils.merchantName = CashReceiver;
                            FeedbackUtils.amountTransacted = amountTransacted;

                            final String howWasYourExperience = "How was your experience at " + CashReceiver + "?";


                            Notification.Builder builder = helper.getChanelNotification(CashReceiver, howWasYourExperience, transactionDateTime);
                            helper.getManager().notify(new Random().nextInt(), builder.build());


                            //Checks to see if its a paybill message
                        }else if(!SmsUtils.returnPayBillCashReceiver(message).equals("nothing")){

                            String CashReceiver = SmsUtils.returnPayBillCashReceiver(message);

                            mpesaSMS mpesaSMS;
                            mpesaSMS = SmsUtils.parsePaybillSms(message);
                            mUserDatabase.child(userPhoneNumber.replace("+", "")).child("mpesaData")
                                    .child(mpesaSMS.getTransactionId()).setValue(mpesaSMS);

                            String transactionTime = SmsUtils.returnTransactionTime(message);
                            String transactionDate = SmsUtils.returnTransactionDate(message);
                            String transactionDateTime = DateUtils.returnFormalDate(transactionDate, transactionTime);

                            FeedbackUtils.cashReceiver = CashReceiver;
                            FeedbackUtils.transactionDate = transactionDate;
                            FeedbackUtils.transactionTime = transactionTime;
                            FeedbackUtils.transactionDateTime = transactionDateTime;
                            FeedbackUtils.userNumber = userNumber;
                            FeedbackUtils.merchantName = CashReceiver;

                            String howWasYourExperience = "How was your experience at " + CashReceiver + "?";
                            Notification.Builder builder = helper.getChanelNotification(CashReceiver, howWasYourExperience, transactionDateTime);
                            helper.getManager().notify(new Random().nextInt(), builder.build());

                            //Checks to see if its a P2P mpesa message
                        }else if (!SmsUtils.returnP2PCashReceiver(message).equals("nothing")){

                            mpesaSMS mpesaSMS;
                            mpesaSMS = SmsUtils.parseP2PSms(message);
                            mUserDatabase.child(userPhoneNumber.replace("+", "")).child("mpesaData")
                                    .child(mpesaSMS.getTransactionId()).setValue(mpesaSMS);
                        }
                    }

                }
            }
        }catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }


}
