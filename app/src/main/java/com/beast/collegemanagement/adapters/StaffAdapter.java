package com.beast.collegemanagement.adapters;

import static com.beast.collegemanagement.Common.getBaseUrl;
import static com.beast.collegemanagement.Common.getPosition;

import android.app.Activity;
import android.content.Context;
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
import com.beast.collegemanagement.R;
import com.beast.collegemanagement.models.StaffModel;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaffAdapter extends RecyclerView.Adapter<StaffHolder> {

    List<StaffModel> list;
    Activity activity;
    BottomSheetDialog bottomSheetDialog;
    String act;
    String apiChangePosition = getBaseUrl() + "updateposition.php";

    public StaffAdapter(List<StaffModel> list, Activity activity, String act) {
        this.list = list;
        this.activity = activity;
        this.act = act;
    }

    @NonNull
    @Override
    public StaffHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.custom_stafflist_layout, parent, false);

        return new StaffHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffHolder holder, int position) {
        Glide.with(activity).load(list.get(position).getProfilePic()).into(holder.profilePic);
        holder.userName.setText(list.get(position).getUsername());
        holder.position.setText(list.get(position).getPosition());

        holder.addBtn.setVisibility(View.VISIBLE);

        holder.llStaffList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getPosition(activity).equalsIgnoreCase("principle")){
                    showBottomSheet(position, holder, list.get(position).getUsername());
                }

            }
        });

    }


    private void showBottomSheet(int position, StaffHolder holder, String userName) {

        bottomSheetDialog = new BottomSheetDialog(activity);

        View view = LayoutInflater.from(activity).inflate(R.layout.bottomsheet_staff_select, null);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.show();

        view.findViewById(R.id.HOD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                holder.position.setText("HOD");

                updatePosition("HOD", userName, holder, position);

            }
        });view.findViewById(R.id.specotor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                holder.position.setText("observer");
                updatePosition("observer", userName, holder, position);
            }
        });view.findViewById(R.id.faculty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                holder.position.setText("faculty");
                updatePosition("faculty", userName, holder, position);
            }
        });view.findViewById(R.id.staff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                holder.position.setText("staff");
                updatePosition("staff", userName, holder, position);
            }
        });

    }

    private void updatePosition(String position, String userName, StaffHolder holder, int pos) {

        StringRequest request = new StringRequest(Request.Method.POST, apiChangePosition,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase("Position Updated") || response.contains("Position Updated")){

                            Toast.makeText(activity, "position updated", Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(activity, "something went wrong", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "can't update position right now", Toast.LENGTH_SHORT).show();
                holder.position.setText(list.get(pos).getPosition());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("userName", userName);
                params.put("position", position);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(activity);
        queue.add(request);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class StaffHolder extends RecyclerView.ViewHolder {

    ImageView profilePic;
    TextView userName, position;
    LinearLayout llStaffList;
    ImageView addBtn;

    public StaffHolder(@NonNull View itemView) {
        super(itemView);

        profilePic = itemView.findViewById(R.id.profilePicStaff);
        userName = itemView.findViewById(R.id.userName);
        position = itemView.findViewById(R.id.position);
        llStaffList = itemView.findViewById(R.id.llStaffList);
        addBtn = itemView.findViewById(R.id.addBtn);

    }
}
