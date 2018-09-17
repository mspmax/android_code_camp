package com.pandu.base.common.utils;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

public final class TimeUtils {

    @Inject
    TimeUtils() {
    }

    /**
     * Formats the passed date string following the passed format pattern.
     *
     * @param dateString    the string of the date to format
     * @param formatPattern the pattern to format date string
     */
    @NonNull
    public String formatDateString(@NonNull final String dateString,
                                   @NonNull final String formatPattern,
                                   @NonNull Locale locale) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatPattern, locale);
        Date date = parseStringToDateWithPassedPattern(dateString, "yyyy-MM-dd", locale);
        return sdf.format(date);
    }

    /**
     * Parses the passed string to a date with the passed format.
     *
     * @param dateString    the string of the date to parse
     * @param formatPattern the pattern to format date string
     * @throws RuntimeException if there is any problems while parsing
     */
    @SuppressWarnings("squid:S00112")
    @NonNull
    public Date parseStringToDateWithPassedPattern(@NonNull final String dateString,
                                                   @NonNull final String formatPattern,
                                                   @NonNull Locale locale) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatPattern, locale);
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException("Error formatting " + dateString + " with pattern " + formatPattern + " to Date");
        }
    }
}
