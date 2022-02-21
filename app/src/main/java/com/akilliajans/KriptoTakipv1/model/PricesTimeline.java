package com.akilliajans.KriptoTakipv1.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;

enum Timeline {
    ONEDAY("Today"), ONEWEEK("Past week"), ONEMONTH("Past month"),
    THREEMONTHS("Past 3M"), ONEYEAR("Past Year"), FIVEYEARS("Past 5 Years");

    public String timelineText;
    Timeline(String timelineText) {
        this.timelineText = timelineText;
    }
}

public class PricesTimeline {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String[] getStartAndEndDate(Timeline timeline) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate;

        switch (timeline) {
            case ONEWEEK:
                startDate = LocalDate.now().minusDays(7);
                break;
            case ONEMONTH:
                startDate = LocalDate.now().minusDays(30);
                break;
            case THREEMONTHS:
                startDate = LocalDate.now().minusDays(90);
                break;
            case ONEYEAR:
                startDate = LocalDate.now().minusDays(365);
                break;
            case FIVEYEARS:
                startDate = LocalDate.now().minusDays(365*5);
                break;
            default:
                startDate = LocalDate.now().minusDays(1);
                break;
        }

        String time = "T00%3A00%3A00Z";
        return new String[]{startDate.toString() + time, endDate.toString() + time};
    }
}