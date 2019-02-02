package com.chooloo.www.callmanager.database;

import java.util.Arrays;
import java.util.List;

import androidx.room.TypeConverter;

public class Converters {

    @TypeConverter
    public static String listToString(List<String> list) {
        final String SEPARATOR = ";";
        StringBuilder builder = new StringBuilder();
        for (String str : list) {
            builder.append(str);
            builder.append(";");
        }
        String result = builder.toString();
        return result.substring(0, result.length() - SEPARATOR.length());
    }

    @TypeConverter
    public static List<String> stringToList(String str) {
        String[] arr = str.split(";");
        return Arrays.asList(arr);
    }
}
