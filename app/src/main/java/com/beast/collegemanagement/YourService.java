package com.beast.collegemanagement;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class YourService extends Service{
    public int counter=0;
    double latitude, longitude;


    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String userName;

    String fetchNotificationApi = Common.getBaseUrl() + "FetchNotification.php";
    String disableNotificaion = Common.getBaseUrl() + "DisableFriendRequestNotificationById.php";
    String fetchChatNotificationApi = Common.getBaseUrl() + "FetchChatNotification.php";
    String disableChatNotification = Common.getBaseUrl() + "DisableChatNotification.php";

    @Override
    public void onCreate() {
        super.onCreate();

        sp = getSharedPreferences("DATA_NAME", MODE_PRIVATE);
        userName = sp.getString("userName", null);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background_Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder
                .setContentTitle("App is running in background")
                .setPriority(Notification.PRIORITY_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setOngoing(false)
                .build();

        startForeground(2, notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Log.i("tag", "start command");
        startTimer();
        if(intent != null){

        }
        return START_REDELIVER_INTENT;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        stoptimertask();

        Log.i("tag", "destroyed");

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
    }

    Boolean trueFalse = true;

    private Timer timer;
    private TimerTask timerTask;
    public void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        while (true){
//                            Log.i("1", "run: first");
//                        }
//                    }
//                }).start();


                Log.i("tag", "xxxxxxxxxx");


                checkForNotification();

                        getChatNotification();

//                stoptimertask();
//
//                        Intent broadcastIntent = new Intent();
//                        broadcastIntent.setAction("restartservice");
//                        broadcastIntent.setClass(YourService.this, Restarter.class);
//                        YourService.this.sendBroadcast(broadcastIntent);


            }
        };
        timer.schedule(timerTask, 3000, 3000); //
    }

    private void getChatNotification() {
        Log.i("tag", "getChatNotification");

        Context context = YourService.this;



        StringRequest request = new StringRequest(Request.Method.POST, fetchChatNotificationApi,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String success = jsonObject.getString("success");

                            if (success.equalsIgnoreCase("1")){

                                for (int i=0; i<jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    if (!Common.chatList.contains(object.getString("id"))){

                                        Common.chatList.add(object.getString("id"));

                                        if (object.getString("content").equalsIgnoreCase("IMG")){

                                            disableChatNotification(object.getString("id"));

                                            String id = Common.chatNotificationID;
                                            String name = Common.chatNotificationName;
                                            Log.i("tag", "chat notification");



                                            int importance = NotificationManager.IMPORTANCE_HIGH;

                                            NotificationChannel mChannel = new NotificationChannel(id, name, importance);



                                            mChannel.enableLights(true);

                                            mChannel.setLightColor(Color.RED);

                                            mChannel.enableVibration(true);
                                            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

                                            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                            mNotificationManager.createNotificationChannel(mChannel);

                                            PendingIntent contentIntent =
                                                    PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class)
                                                            .setAction(Intent.ACTION_INSERT).putExtra(Common.notification,
                                                                    Common.chatNotification + "xxx" + object.getString("sender")), 0);

                                            NotificationManager notificationManager =
                                                    (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
                                            Notification notification = new NotificationCompat.Builder(getBaseContext(),id)
                                                    .setSmallIcon(IconCompat.createWithBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logo)))
                                                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.noti_img_icon))
                                                    .setContentTitle(object.getString("sender"))
                                                    .setContentText("Image Chat")
                                                    .setPriority(NotificationManager.IMPORTANCE_MAX)
                                                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                                    .setDefaults(NotificationCompat.DEFAULT_SOUND)
                                                    .setContentIntent(contentIntent)
                                                    .build();
                                            notificationManager.notify(99, notification);


                                        }else if (object.getString("content").equalsIgnoreCase("DOC")){

                                            String id = Common.chatNotificationID;
                                            String name = Common.chatNotificationName;
                                            Log.i("tag", "chat notification");

                                            disableChatNotification(object.getString("id"));


                                            int importance = NotificationManager.IMPORTANCE_HIGH;

                                            NotificationChannel mChannel = new NotificationChannel(id, name, importance);



                                            mChannel.enableLights(true);

                                            mChannel.setLightColor(Color.RED);

                                            mChannel.enableVibration(true);
                                            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

                                            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                            mNotificationManager.createNotificationChannel(mChannel);

                                            PendingIntent contentIntent =
                                                    PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class)
                                                            .setAction(Intent.ACTION_INSERT).putExtra(Common.notification,
                                                                    Common.chatNotification + "xxx" + object.getString("sender")), 0);

                                            NotificationManager notificationManager =
                                                    (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
                                            Notification notification = new NotificationCompat.Builder(getBaseContext(),id)
                                                    .setSmallIcon(IconCompat.createWithBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logo)))
                                                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.noti_doc_icon))
                                                    .setContentTitle(object.getString("sender"))
                                                    .setContentText("Document Chat")
                                                    .setPriority(NotificationManager.IMPORTANCE_MAX)
                                                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                                    .setDefaults(NotificationCompat.DEFAULT_SOUND)
                                                    .setContentIntent(contentIntent)
                                                    .build();
                                            notificationManager.notify(99, notification);


                                        }else {

                                            disableChatNotification(object.getString("id"));
                                            String id = Common.chatNotificationID;
                                            String name = Common.chatNotificationName;
                                            Log.i("tag", "chat notification");



                                            int importance = NotificationManager.IMPORTANCE_HIGH;

                                            NotificationChannel mChannel = new NotificationChannel(id, name, importance);



                                            mChannel.enableLights(true);

                                            mChannel.setLightColor(Color.RED);

                                            mChannel.enableVibration(true);
                                            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

                                            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                            mNotificationManager.createNotificationChannel(mChannel);

                                            PendingIntent contentIntent =
                                                    PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class)
                                                            .setAction(Intent.ACTION_INSERT).putExtra(Common.notification,
                                                                    Common.chatNotification + "xxx" + object.getString("sender")), 0);

                                            NotificationManager notificationManager =
                                                    (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
                                            Notification notification = new NotificationCompat.Builder(getBaseContext(),id)
                                                    .setSmallIcon(IconCompat.createWithBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logo)))
                                                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                                                    .setContentTitle(object.getString("sender"))
                                                    .setContentText(object.getString("content"))
                                                    .setPriority(NotificationManager.IMPORTANCE_MAX)
                                                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                                    .setDefaults(NotificationCompat.DEFAULT_SOUND)
                                                    .setContentIntent(contentIntent)
                                                    .build();
                                            notificationManager.notify(99, notification);


                                        }

                                    }

                                }

                            }

                        } catch (JSONException e) {
                            Log.i("tag", "getChat Format Error");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("tag", "getChatError");
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                map.put("receiver", Common.getUserName(context));

                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    private void disableChatNotification(String id) {

        StringRequest request = new StringRequest(Request.Method.POST, disableChatNotification,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("tag", response + "disableChatNotification");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("tag", "disable chat notification error");
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                map.put("id", id);

                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(YourService.this);
        queue.add(request);

    }

    private void checkForNotification() {

        Context context = YourService.this;

        StringRequest request = new StringRequest(Request.Method.POST, fetchNotificationApi,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String success = jsonObject.getString("success");

                            if (success.equalsIgnoreCase("1")){

                                for (int i=0; i<jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    if (jsonArray.length() != 0){

                                        if (!Common.friendList.contains(object.getString("id"))){
                                            Common.friendList.add(object.getString("id"));
                                            Log.i("notification_tag", "notification");

                                            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                            PendingIntent contentIntent =
                                                    PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class)
                                                            .setAction(Intent.ACTION_INSERT).putExtra(Common.notification,
                                                                    Common.friendNotification + "xxx" + object.getString("sender")), 0);


                                            String id = Common.friendNotificationID;
                                            String name = Common.friendNotificationName;



                                            int importance = NotificationManager.IMPORTANCE_HIGH;

                                            NotificationChannel mChannel = new NotificationChannel(id, name, importance);



                                            mChannel.enableLights(true);

                                            mChannel.setLightColor(Color.RED);

                                            mChannel.enableVibration(true);
                                            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

                                            mNotificationManager.createNotificationChannel(mChannel);

                                            NotificationManager notificationManager =
                                                    (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
                                            Notification notification = new NotificationCompat.Builder(getBaseContext(),Common.friendNotificationID)
                                                    .setSmallIcon(IconCompat.createWithBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logo)))
                                                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                                                    .setContentTitle("Friend Request")
                                                    .setContentText(object.getString("sender") + " has sent you a friend request")
                                                    .setPriority(NotificationManager.IMPORTANCE_MAX)
                                                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                                    .setContentIntent(contentIntent)
                                                    .setDefaults(NotificationCompat.DEFAULT_SOUND)
                                                    .build();
                                            notificationManager.notify(100, notification);

                                            disableNotification(object.getString("id"));

                                        }

                                    }

                                }

                            }

                        } catch (JSONException e) {
                            Log.i("tag", "format error");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("tag", "connection error");
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                map.put("receiver", Common.getUserName(YourService.this));

                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(YourService.this);
        queue.add(request);

    }

    private void disableNotification(String id) {

        StringRequest request = new StringRequest(Request.Method.POST, disableNotificaion,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("tag", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("tag", "unable to disable notificaion");
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                map.put("id", id);

                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(YourService.this);
        queue.add(request);

    }


    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
