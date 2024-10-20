package com.mfa.report.endpoint.rest.controller.utils;


import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.Locale;

@Component
public class LocalDateUtils {
    public String generateReportTitleForWeek(LocalDate date) {
        int weekOfMonth = date.get(WeekFields.of(Locale.getDefault()).weekOfMonth());
        String monthName = date.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        int year = date.getYear();

        return weekOfMonth + (weekOfMonth == 1 ? "ère" : "ème") + " semaine du mois de " + monthName + " " + year;
    }

    public String getMonthName(int monthNumber) {
        String[] months = {"janvier", "février", "mars", "avril", "mai", "juin", "juillet", "août", "septembre", "octobre", "novembre", "décembre"};
        return months[monthNumber - 1];
    }

    public String getQuarterName(int quarterNumber) {
        return switch (quarterNumber) {
            case 1 -> "1er trimestre";
            case 2 -> "2ème trimestre";
            case 3 -> "3ème trimestre";
            case 4 -> "4ème trimestre";
            default -> throw new IllegalArgumentException("Invalid quarter number: " + quarterNumber);
        };
    }

}
