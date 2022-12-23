package com.beast.collegemanagement;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Common {
    static String MainUrl = "https://biochemical-damping.000webhostapp.com/";

    public static String getMainUrl() {
        return MainUrl;
    }
    public static String getBaseUrl() {
        return MainUrl + "Management%20of%20College/";
    }

    public static Bitmap IMAGE_BITMAP;

    public static String GetDownloadFilePath(){
        return "TaskManager/";
    }
    public static String GetMediaPath(){
        return GetDownloadFilePath() + "Media/";
    }

    public static String GetDownloadEnvironment(){
        return Environment.DIRECTORY_DOWNLOADS;
    }

    public static String GetExternalStorage(){
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static String GetFullMediaPath(){
        return GetExternalStorage() + "/" + GetDownloadEnvironment() + "/" + GetMediaPath();
    }

    public static String getTIme(){

        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
    }

    public static String getDate(){

        return new SimpleDateFormat("dd-MM_yyyy", Locale.getDefault()).format(new Date());

    }

    public static String getTimeDate(){

        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date())
                + "xxx" +
                new SimpleDateFormat("dd-MM_yyyy", Locale.getDefault()).format(new Date());

    }

    public static String getUserName(Context context){

        SharedPreferences sp = context.getSharedPreferences("FILE_NAME", Context.MODE_PRIVATE);
        return sp.getString("userName", null);

    }

    public static String getFullName(Context context){

        SharedPreferences sp = context.getSharedPreferences("FILE_NAME", Context.MODE_PRIVATE);
        return sp.getString("fullName", null);

    }

    public static String getProfilePic(Context context){

        SharedPreferences sp = context.getSharedPreferences("FILE_NAME", Context.MODE_PRIVATE);
        return sp.getString("profilePic", null);

    }

    public static String getPosition(Context context){

        SharedPreferences sp = context.getSharedPreferences("FILE_NAME", Context.MODE_PRIVATE);
        return sp.getString("POSITION", null);

    }


}
