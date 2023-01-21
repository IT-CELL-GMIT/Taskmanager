package com.beast.collegemanagement.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beast.collegemanagement.Common;
import com.beast.collegemanagement.R;
import com.beast.collegemanagement.models.NotificationModel;
import com.beast.collegemanagement.tabfragment.NotificationFragment;

import java.io.PushbackReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {

    private Context context;
    private List<NotificationModel> list;

    ProgressDialog progressDialog;

    String acceptFriendRequest = Common.getBaseUrl() + "acceptFriendRequest.php";
    String friendRequestAccept = Common.getBaseUrl() + "friendRequestAccept.php";
    String declineFriendRequest = Common.getBaseUrl() + "declineFriendRequest.php";
    String disableNotification = Common.getBaseUrl() + "disableFriendrequest.php";

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public NotificationAdapter(Context context, List<NotificationModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NotificationAdapter.NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_notification_layout, parent, false);

        return new NotificationHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NotificationHolder holder, int position) {

        sp = context.getSharedPreferences("FILE_NAME", Context.MODE_PRIVATE);
        editor = sp.edit();

        progressDialog = new ProgressDialog(context);

        if (!list.get(position).getIsDisabled().equalsIgnoreCase("yes")) {

            holder.username.setText(sp.getString("fullName", "Not Identified"));
            holder.senderUsername.setText(list.get(position).getFromUsername());
            holder.content.setText(list.get(position).getContent());

            String timeDateSplits[] = list.get(position).getTimeDate().split("xxx");

            holder.timeDate.setText(timeDateSplits[0] + " " + timeDateSplits[1]);

            if (list.get(position).getType().equalsIgnoreCase("FRIEND_REQUEST")) {

                holder.acceptDeclineLL.setVisibility(View.VISIBLE);

                holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        freindRequestAccepted(Common.getUserName(context), list.get(position).getFromUsername(), holder);

                    }
                });

                holder.declineBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        new AlertDialog.Builder(context)
                                .setTitle("Friend Request")
                                .setMessage("Do you really want to decline freind request?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        holder.acceptDeclineLL.setVisibility(View.GONE);
                                        holder.freindDeclineText.setVisibility(View.VISIBLE);
                                        declineFriendRequest(list.get(position).getFromUsername());
                                    }
                                }).setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                }).show();

                    }
                });

            } else if (list.get(position).getType().equalsIgnoreCase("FRIEND_REQUEST_ACCEPTED")) {

                holder.freindAcceptText.setVisibility(View.VISIBLE);
                holder.acceptDeclineLL.setVisibility(View.GONE);

            } else if (list.get(position).getType().equalsIgnoreCase("FRIEND_REQUEST_DECLINED")) {

                holder.acceptDeclineLL.setVisibility(View.GONE);
                holder.freindDeclineText.setVisibility(View.VISIBLE);

            } else {
                holder.acceptDeclineLL.setVisibility(View.GONE);
                holder.freindAcceptText.setVisibility(View.GONE);
                holder.freindDeclineText.setVisibility(View.GONE);
            }

            holder.removeNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    list.remove(position);
                    NotificationFragment.getNewNotification(list);

                    disableNotification(list.get(position).getFromUsername(), list.get(position).getTimeDate(), list.get(position).getType());

                }
            });

        }

    }

    private void disableNotification(String fromUsername, String timeDate, String type) {

        StringRequest request = new StringRequest(Request.Method.POST, disableNotification,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, fromUsername, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "connection error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("reciever", Common.getUserName(context));
                params.put("sender", fromUsername);
                params.put("timedate", timeDate);
                params.put("type", type);

                return params;

            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void declineFriendRequest(String fromUsername) {

        StringRequest request = new StringRequest(Request.Method.POST, declineFriendRequest,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("failed") || response.contains("failed")){
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "connection error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("reciever", Common.getUserName(context));
                params.put("sender", fromUsername);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }


    private void freindRequestAccepted(String userName, String friednID, NotificationHolder holder) {

        progressDialog.setMessage("accepting...");
        progressDialog.show();
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(true);

        StringRequest request = new StringRequest(Request.Method.POST, friendRequestAccept,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        if (response.equalsIgnoreCase("Data Inserted") || response.contains("Data Inserted")){

                            Toast.makeText(context, "friend request accepted", Toast.LENGTH_SHORT).show();
                            holder.freindAcceptText.setVisibility(View.VISIBLE);
                            holder.acceptDeclineLL.setVisibility(View.GONE);

                            setFriendRequestNotification(userName, friednID);

                        }else {
                            Toast.makeText(context, "error while accepting friend request", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Connection Error " + error.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("userName", userName);
                params.put("friendName", friednID);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void setFriendRequestNotification(String userName, String friednID) {

        StringRequest request = new StringRequest(Request.Method.POST, acceptFriendRequest,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("failed") || response.contains("failed")){
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "connection error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("reciever", Common.getUserName(context));
                params.put("sender", friednID);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {

        TextView username, content, timeDate, senderUsername, freindAcceptText, freindDeclineText, acceptBtn, declineBtn;
        LinearLayout removeNotification, fullLayout, acceptDeclineLL;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.tv_name);
            content = itemView.findViewById(R.id.tv_content);
            timeDate = itemView.findViewById(R.id.tv_notification_datetime);
            senderUsername = itemView.findViewById(R.id.tv_notification_username);
            removeNotification = itemView.findViewById(R.id.llRemoveNotification);
            fullLayout = itemView.findViewById(R.id.notificationLL);
            freindAcceptText = itemView.findViewById(R.id.tv_friendrequest_accepted);
            freindDeclineText = itemView.findViewById(R.id.tv_friendrequest_declined);
            acceptBtn = itemView.findViewById(R.id.tv_accept_freindrequest);
            declineBtn = itemView.findViewById(R.id.tv_decline_friendrequest);
            acceptDeclineLL = itemView.findViewById(R.id.accept_decline_ll);

        }
    }
}
