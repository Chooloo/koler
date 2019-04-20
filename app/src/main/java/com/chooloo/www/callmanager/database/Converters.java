package com.chooloo.www.callmanager.database;

import com.chooloo.www.callmanager.util.bla.Utilities;

import java.util.Arrays;
import java.util.List;

import androidx.room.TypeConverter;

public class Converters {

    @TypeConverter
    public static String listToString(List<String> list) {
        return Utilities.joinStringsWithSeparator(list, ";");
    }

    @TypeConverter
    public static List<String> stringToList(String str) {
        String[] arr = str.split(";");
        return Arrays.asList(arr);
    }
}
