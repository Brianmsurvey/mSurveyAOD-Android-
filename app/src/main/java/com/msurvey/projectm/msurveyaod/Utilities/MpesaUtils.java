package com.msurvey.projectm.msurveyaod.Utilities;

public class MpesaUtils {

    //Buy Goods and Services Regex
    public static final String transactionIdRegex = "(.*)(?= Confirmed)";

    public static final String amountTransactionRegex = "[0-9,]{1,}[.][0-9]{2}";

    public static final String mpesaBalanceRegex = "(?<=balance is Ksh)[0-9,]{1,}[.][0-9]{2}";

    public static final String cashReceiverRegex = "(?<=paid to )(.*)(?=. on)";

    public static final String transactionTimeRegex = "[0-9]{1,2}:[0-9]{2}\\s(AM|PM)";

    public static final String transactionDateRegex = "[0-9]{1,2}/[0-9]{1,2}/[0-9]{1,2}";

    public static final String transactionCostRegex = "(?<=Transaction cost, Ksh)(.*)(?=\\.)";

    //Paybill Regex
    public static final String paybillCashReceiverRegex = "(?<=sent to )(.*)(?= for)";



    //P2P Regex
    public static final String p2pCashReceiverRegex = "(?<=sent to )(.*)(?= 0)";


    //Mpesa SMS Variables

    public static String transactionId;

    public static String amountTransacted;

    public static String mpesaBalance;

    public static String cashReceiver;

    public static String transactionTime;

    public static String transactionDate;

    public static String transactionCost;

    public static String transactionType;


    //Mpesa TransactionTypes

    public static final String paybill = "paybill";

    public static final String P2P = "p2p";

    public static final String buygoodsandservices = "buygoodsandservices";
}
