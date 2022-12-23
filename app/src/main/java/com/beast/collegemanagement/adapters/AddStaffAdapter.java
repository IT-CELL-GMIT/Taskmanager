package com.beast.collegemanagement.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.beast.collegemanagement.models.AddStaffModel;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddStaffAdapter extends RecyclerView.Adapter<AddStaffAdapter.AddStaffHoder> {

    List<AddStaffModel> list;
    Context context;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String sendFriendRequest = Common.getBaseUrl() + "SendFriendRequest.php";
    ProgressDialog progressDialog;

    public AddStaffAdapter(List<AddStaffModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AddStaffAdapter.AddStaffHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_stafflist_layout, parent, false);

        return new AddStaffHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddStaffAdapter.AddStaffHoder holder, int position) {

        String id, userName, fullName, profilePic;

        sp = context.getSharedPreferences("FILE_NAME", Context.MODE_PRIVATE);
        editor = sp.edit();

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(true);

        id = list.get(position).getId();
        userName = list.get(position).getUserName();
        fullName = list.get(position).getFullName();
        profilePic = list.get(position).getProfilePic();

        holder.addBtn.setVisibility(View.INVISIBLE);

        Glide.with(context).load(profilePic).into(holder.profilePic);
        holder.userName.setText(userName);
        holder.position.setText(fullName);

        holder.llStaffList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet1(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AddStaffHoder extends RecyclerView.ViewHolder {

        ImageView profilePic;
        TextView userName, position;
        LinearLayout llStaffList;
        ImageView addBtn;

        public AddStaffHoder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profilePicStaff);
            userName = itemView.findViewById(R.id.userName);
            position = itemView.findViewById(R.id.position);
            llStaffList = itemView.findViewById(R.id.llStaffList);
            addBtn = itemView.findViewById(R.id.addBtn);

        }
    }

    private void showBottomSheet1(int position){

        new AlertDialog.Builder(context)
                .setTitle("Send friend request to " + list.get(position).getFullName() + "?")
                .setPositiveButton("send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        sendFriendRequest(sp.getString("userName", null), list.get(position).getUserName());
                    }
                }).setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();

    }

    private void sendFriendRequest(String userName, String friendUserName) {

        progressDialog.setMessage("sending friend request...");
        progressDialog.show();

        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        String currentDate = new SimpleDateFormat("dd-MM_yyyy", Locale.getDefault()).format(new Date());

        String timeDate = currentTime + "xxx" + currentDate;
        String content = userName + " has sent you a friend request";
        String type = "FRIEND_REQUEST";

        StringRequest request = new StringRequest(Request.Method.POST, sendFriendRequest,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        
                        if (response.equalsIgnoreCase("Data Inserted") || response.contains("Data Inserted")){
                            Toast.makeText(context, "friend request has sent", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "network error", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("sender", userName);
                params.put("reciever", friendUserName);
                params.put("content", content);
                params.put("timedate", timeDate);
                params.put("type", type);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

}
