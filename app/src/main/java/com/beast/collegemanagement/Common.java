package com.beast.collegemanagement;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.lights.LightsManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Common {

    public static List<String> commonList = new ArrayList<>();
    public static List<String> commonPickList = new ArrayList<>();

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

        ////55:55xxx55-55_5555

        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date())
                + "xxx" +
                new SimpleDateFormat("dd-MM_yyyy", Locale.getDefault()).format(new Date());

    }

    public static String getDateTime(){

        ////55-55_5555xxx55:55
        return
                new SimpleDateFormat("dd-MM_yyyy", Locale.getDefault()).format(new Date())
                        + "xxx" +
        new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

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

    public static void setNameToSharedPref(String name, String value, Context context){

        SharedPreferences sp = context.getSharedPreferences("FILE_NAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(name, value);
        editor.apply();

    }

    public static String getSharedPrf(String name, Context context){

        SharedPreferences sp = context.getSharedPreferences("FILE_NAME", Context.MODE_PRIVATE);

        String value = sp.getString(name, null);

        if (value!=null){
            return value;
        }

        return null;

    }

    public static void addToCommonList(String value){
        commonList.add(value);
    }

    public static void addToCommonProfileList(String url){
        commonPickList.add(url);
    }

    public static void removeFromBoth(String value, String url){

        commonList.remove(value);
        commonPickList.remove(url);

    }

    public static List<String> getCommonList(){
        return commonList;
    }

    public static List<String> getCommonPickList(){
        return commonPickList;
    }

    public static String ConvertToString(Uri uri, Context context) {

        String Document = "xyz";
        try {

            InputStream in = context.getContentResolver().openInputStream(uri);
            byte[] bytes = getBytes(in);

            Document = Base64.encodeToString(bytes, Base64.DEFAULT);

        } catch (Exception e) {
            Toast.makeText(context, "Please select a image\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
            return "xyz";
        }

        return Document;

    }
    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static String getExtension(Context context, Uri uri) {
        String extension;

        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
        }

        if (extension.equalsIgnoreCase(null)){
            return "non";
        }else {
            return extension;
        }
    }

}
