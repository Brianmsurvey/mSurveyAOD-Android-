package com.msurvey.projectm.msurveyaod.Utilities;

public class MpesaUtils {

    //Regex
    public static final String transactionIdRegex = "(?<=0, )(.*)(?= Confirmed)";

    public static final String amountTransactionRegex = "[0-9]{1,}[.][0-9]{2}";

    public static final String mpesaBalanceRegex = "(?<=balance is Ksh)[0-9]{1,}[.][0-9]{2}";

    public static final String cashReceiverRegex = "(?<=paid to )(.*)(?= on )";

    public static final String transactionTimeRegex = "[0-9]{1,2}:[0-9]{2}\\s(AM|PM)";

    public static final String transactionDateRegex = "[0-9]{1,2}/[0-9]{1,2}/[0-9]{1,2}";

    public static final String transactionCostRegex = "(?<=Transaction cost, )(.*)(?=\\.)";


    //Mpesa SMS Variables

    public static String transactionId;

    public static String amountTransacted;

    public static String mpesaBalance;

    public static String cashReceiver;

    public static String transactionTime;

    public static String transactionDate;

    public static String transactionCost;


}
