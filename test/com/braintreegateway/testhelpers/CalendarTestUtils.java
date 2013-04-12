package com.braintreegateway.testhelpers;

import com.braintreegateway.util.NodeWrapper;
import org.junit.Ignore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

@Ignore("Testing utility class")
public abstract class CalendarTestUtils {
    public static final String UTC = "UTC";

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
        Calendar depositCalendar = Calendar.getInstance(TimeZone.getTimeZone(timeZoneName));
        depositCalendar.setTime(dateFormat.parse(dateString));
        return depositCalendar;
    }
}
