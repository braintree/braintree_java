package com.braintreegateway.testhelpers;

import com.braintreegateway.util.NodeWrapper;
import org.junit.jupiter.api.Disabled;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

@Disabled("Testing utility class")
public abstract class CalendarTestUtils {
    public static final String UTC = "UTC";

    public static Calendar today() throws ParseException {
        return today(UTC);
    }

    public static Calendar today(String timeZoneName) {
        return Calendar.getInstance(TimeZone.getTimeZone(timeZoneName));
    }

    public static Calendar date(String dateString) throws ParseException {
        return getCalendar(dateString, NodeWrapper.DATE_FORMAT, UTC);
    }

    public static Calendar dateTime(String dateString) throws ParseException {
        return dateTime(dateString, UTC);
    }

    public static Calendar dateTime(String dateString, String timeZoneName) throws ParseException {
        return getCalendar(dateString, NodeWrapper.DATE_TIME_FORMAT, timeZoneName);
    }

    public static Calendar getCalendar(String dateString, String dateTimeFormat, String timeZoneName) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(dateTimeFormat);
        dateFormat.setTimeZone(TimeZone.getTimeZone(timeZoneName));
        Calendar disbursementCalendar = Calendar.getInstance(TimeZone.getTimeZone(timeZoneName));
        disbursementCalendar.setTime(dateFormat.parse(dateString));
        return disbursementCalendar;
    }
}
