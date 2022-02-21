package com.akilliajans.KriptoTakipv1.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NumberS {
    static Double TRILLION = 1_000_000_000_000d;
    static Double BILLION = 1_000_000_000d;
    static Double MILLION = 1_000_000d;
    static Double THOUSAND = 1_000d;

    public static String formatLargeNumber(String number) {
        Double num;
        try {
            num = Double.parseDouble(number);
        } catch (Exception e) {
            return "-";
        }
        Double formattedNum;
        String suffix = "";
        if (num > TRILLION) { formattedNum = num/TRILLION; suffix = "T"; }
        else if (num > BILLION) { formattedNum = num/BILLION; suffix = "B";}
        else if (num > MILLION) { formattedNum = num/MILLION; suffix = "M"; }
        else if (num > THOUSAND) { formattedNum = num/THOUSAND; suffix = "K"; }
        else { formattedNum = num; }
        return String.format("%.2f%s", formattedNum, suffix);
    }

    public static String formatPrice(String price) {
        Float p = Float.parseFloat(price);
        String sign = p < 0 ? "-" : "";
        p = Math.abs(p);
        return String.format("%s$%s", sign, formatLargeNumber(p.toString()));
    }

    public static String formatPercentChange(String percentChange) {
        Float p = Float.parseFloat(percentChange);
        String sign = p < 0 ? "-" : "+";
        p = Math.abs(p);
        return String.format("%s%s", sign, formatLargeNumber(p.toString())) + "%";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String formatDate(String date) {
        date = date.substring(0, 10);
        LocalDate datetime = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return datetime.format(DateTimeFormatter.ofPattern("dd LLLL yyyy"));
    }
}
