package com.msurvey.projectm.msurveyaod.Utilities;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.msurvey.projectm.msurveyaod.User;
import com.msurvey.projectm.msurveyaod.mpesaSMS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class SmsUtils {


    public final String TAG = "SmsUtils";


    public static ArrayList<String> scrapeMpesaSms(Context context){
        StringBuilder smsBuilder = new StringBuilder();
        ArrayList<String> allSms = new ArrayList<>();
        final String SMS_URI_INBOX = "content://sms/inbox";
        final String SMS_URI_ALL = "content://sms/";
        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
            Cursor cur = context.getContentResolver().query(uri, projection, "address='MPESA'", null, "date desc");
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
        //Log.e(TAG, result);

//        for(int i=0; i<allSms.size(); i++){
//            //Log.e(TAG, allSms.get(i));
//        }

        return allSms;
    }

    public static void parseSms(ArrayList<String> allSms){

        //Traverses arraylist which contains individual messages as strings

        String message = "[ MPESA, 0, LGE0KHDB4A Confirmed. Ksh60.00 paid to Sukari Service Station Ltd on 14/7/17 at 4:04 PM.New M-PESA balance is Ksh573.90. Transaction cost, Ksh0.00., 1500037456813, 1 ]";

        User user = new User();

        for(int i=0; i<allSms.size(); i++){



        }

        MpesaUtils.transactionId = StringUtils.regexChecker(message, MpesaUtils.transactionIdRegex);

        MpesaUtils.amountTransacted = StringUtils.regexChecker(message, MpesaUtils.amountTransactionRegex);

        MpesaUtils.mpesaBalance = StringUtils.regexChecker(message, MpesaUtils.mpesaBalanceRegex);

        MpesaUtils.cashReceiver = StringUtils.regexChecker(message, MpesaUtils.cashReceiverRegex);

        MpesaUtils.transactionTime = StringUtils.regexChecker(message, MpesaUtils.transactionTimeRegex);

        MpesaUtils.transactionDate = StringUtils.regexChecker(message, MpesaUtils.transactionDateRegex);

        String cost = StringUtils.regexChecker(message, MpesaUtils.transactionCostRegex);

        MpesaUtils.transactionCost = cost.substring(3);

    }

    public static void parseSms(User user, ArrayList<String> allSms){

        //Traverses arraylist which contains individual messages as strings

        String message;

        for(int i=0; i<allSms.size(); i++) {

            message = allSms.get(i);

            mpesaSMS mpesaSMS = new mpesaSMS();

            mpesaSMS.setTransactionId(StringUtils.regexChecker(message, MpesaUtils.transactionIdRegex));

            mpesaSMS.setAmountTransacted(StringUtils.regexChecker(message, MpesaUtils.amountTransactionRegex));

            mpesaSMS.setMpesaBalance(StringUtils.regexChecker(message, MpesaUtils.mpesaBalanceRegex));

            mpesaSMS.setCashReceiver(StringUtils.regexChecker(message, MpesaUtils.cashReceiverRegex));

            mpesaSMS.setTransactionTime(StringUtils.regexChecker(message, MpesaUtils.transactionTimeRegex));

            mpesaSMS.setTransactionDate(StringUtils.regexChecker(message, MpesaUtils.transactionDateRegex));

            String cost = StringUtils.regexChecker(message, MpesaUtils.transactionCostRegex);

            mpesaSMS.setTransactionCost(cost.substring(3));

            user.getmPesaData().put(mpesaSMS.getTransactionId(), mpesaSMS);

        }

    }

    //Default buy goods and services
    public static mpesaSMS parseSms(String message){

        mpesaSMS mpesaSMS = new mpesaSMS();

        mpesaSMS.setTransactionId(StringUtils.regexChecker(message, MpesaUtils.transactionIdRegex));

        mpesaSMS.setAmountTransacted(StringUtils.regexChecker(message, MpesaUtils.amountTransactionRegex));

        mpesaSMS.setMpesaBalance(StringUtils.regexChecker(message, MpesaUtils.mpesaBalanceRegex));

        mpesaSMS.setCashReceiver(StringUtils.regexChecker(message, MpesaUtils.cashReceiverRegex));

        mpesaSMS.setTransactionTime(StringUtils.regexChecker(message, MpesaUtils.transactionTimeRegex));

        mpesaSMS.setTransactionDate(StringUtils.regexChecker(message, MpesaUtils.transactionDateRegex));

        String cost = StringUtils.regexChecker(message, MpesaUtils.transactionCostRegex);

        mpesaSMS.setTransactionCost(cost.substring(3));

        mpesaSMS.setTransactionType(MpesaUtils.buygoodsandservices);

        return mpesaSMS;

    }

    //Parse paybill mpesa sms
    public static mpesaSMS parsePaybillSms(String message){

        mpesaSMS mpesaSMS = new mpesaSMS();

        mpesaSMS.setTransactionId(StringUtils.regexChecker(message, MpesaUtils.transactionIdRegex));

        mpesaSMS.setAmountTransacted(StringUtils.regexChecker(message, MpesaUtils.amountTransactionRegex));

        mpesaSMS.setMpesaBalance(StringUtils.regexChecker(message, MpesaUtils.mpesaBalanceRegex));

        mpesaSMS.setCashReceiver(StringUtils.regexChecker(message, MpesaUtils.paybillCashReceiverRegex));

        mpesaSMS.setTransactionTime(StringUtils.regexChecker(message, MpesaUtils.transactionTimeRegex));

        mpesaSMS.setTransactionDate(StringUtils.regexChecker(message, MpesaUtils.transactionDateRegex));

        String cost = StringUtils.regexChecker(message, MpesaUtils.transactionCostRegex);

        mpesaSMS.setTransactionCost(cost.substring(3));

        mpesaSMS.setTransactionType(MpesaUtils.paybill);

        return mpesaSMS;

    }

    //Parse paybill mpesa sms
    public static mpesaSMS parseP2PSms(String message){

        mpesaSMS mpesaSMS = new mpesaSMS();

        mpesaSMS.setTransactionId(StringUtils.regexChecker(message, MpesaUtils.transactionIdRegex));

        mpesaSMS.setAmountTransacted(StringUtils.regexChecker(message, MpesaUtils.amountTransactionRegex));

        mpesaSMS.setMpesaBalance(StringUtils.regexChecker(message, MpesaUtils.mpesaBalanceRegex));

        mpesaSMS.setCashReceiver(StringUtils.regexChecker(message, MpesaUtils.p2pCashReceiverRegex));

        mpesaSMS.setTransactionTime(StringUtils.regexChecker(message, MpesaUtils.transactionTimeRegex));

        mpesaSMS.setTransactionDate(StringUtils.regexChecker(message, MpesaUtils.transactionDateRegex));

        String cost = StringUtils.regexChecker(message, MpesaUtils.transactionCostRegex);

        mpesaSMS.setTransactionCost(cost.substring(3));

        mpesaSMS.setTransactionType(MpesaUtils.buygoodsandservices);


        return mpesaSMS;

    }

    //Buy goods and services
    public static String returnCashReceiver(String message) {

        return StringUtils.regexChecker(message, MpesaUtils.cashReceiverRegex);

    }

    public static String returnAmountTransacted(String message){

        return StringUtils.regexChecker(message, MpesaUtils.amountTransactionRegex);
    }

    //Paybill
    public static String returnPayBillCashReceiver(String message) {

        return StringUtils.regexChecker(message, MpesaUtils.paybillCashReceiverRegex);

    }

    public static String returnP2PCashReceiver(String message) {

        return StringUtils.regexChecker(message, MpesaUtils.p2pCashReceiverRegex);

    }



    public static String returnTransactionTime(String message){

        return StringUtils.regexChecker(message, MpesaUtils.transactionTimeRegex);
    }

    public static String returnTransactionDate(String message){

        return StringUtils.regexChecker(message, MpesaUtils.transactionDateRegex);
    }

    public static mpesaSMS parseGeneralMpesaMessage(String message){

        mpesaSMS mpesaSMS = new mpesaSMS();


        //Buy goods and services message
        if(!returnCashReceiver(message).equals("nothing")){

            mpesaSMS = SmsUtils.parseSms(message);

        //Paybill message
        }else if(!returnPayBillCashReceiver(message).equals("nothing")){


            mpesaSMS = SmsUtils.parsePaybillSms(message);

        //P2P message
        }else if (!returnP2PCashReceiver(message).equals("nothing")){

            mpesaSMS = SmsUtils.parseP2PSms(message);

        }

        return mpesaSMS;
    }
}

