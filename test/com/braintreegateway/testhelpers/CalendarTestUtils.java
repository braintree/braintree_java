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
        return getCalendar(dateString, NodeWrapper.DATE_FORMAT);
    }

    public static Calendar dateTime(String dateString) throws ParseException {
        return getCalendar(dateString, NodeWrapper.DATE_TIME_FORMAT);
    }

    public static Calendar getCalendar(String dateString, String dateTimeFormat) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(dateTimeFormat);
        dateFormat.setTimeZone(TimeZone.getTimeZone(UTC));
        Calendar depositCalendar = Calendar.getInstance(TimeZone.getTimeZone(UTC));
        depositCalendar.setTime(dateFormat.parse(dateString));
        return depositCalendar;
    }
}
