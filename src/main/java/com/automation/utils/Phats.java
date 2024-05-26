package com.automation.utils;

public class Phats {
    private static String phatinputexcel;
    private static String phatoutputexcel;

    private static String excelsheetname;

    public static  void getPhats(){
        phatinputexcel = System.getProperty("PHAT_INPUT");
        phatoutputexcel = System.getProperty("PHAT_OUTPUT");
        excelsheetname = System.getProperty("SHEET_NAME");

    }

}
