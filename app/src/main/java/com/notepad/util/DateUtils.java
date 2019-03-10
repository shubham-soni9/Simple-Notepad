package com.notepad.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static final DateUtils ourInstance = new DateUtils();

    private DateUtils() {
    }

    public static DateUtils getInstance() {
        return ourInstance;
    }

    public String getFormattedDate(Date date, String format) {
        return new SimpleDateFormat(format, Locale.ENGLISH).format(date);
    }

    public String getFormattedDate(long time, String format) {
        return getFormattedDate(new Date(time), format);
    }
}
