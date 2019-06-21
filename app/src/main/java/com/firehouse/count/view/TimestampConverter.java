package com.firehouse.count.view;

import androidx.room.TypeConverter;

import java.util.Date;

public final class TimestampConverter {

    // reference: https://developer.android.com/training/data-storage/room/referencing-data

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
