package com.beast.collegemanagement.adapters;

import android.content.Context;
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
import com.beast.collegemanagement.models.SubTaskModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SubTaskAdapter extends RecyclerView.Adapter<SubTaskAdapter.TaskHolder> {

    List<SubTaskModel> list;
    Context context;
    String activityName;

    String complateSubtaskApi = Common.getBaseUrl() + "completeSubTask.php";
    String deleteSubtaskApi = Common.getBaseUrl() + "deleteSubTask.php";

    public SubTaskAdapter(List<SubTaskModel> list, Context context, String activityName) {
        this.list = list;
        this.context = context;
        this.activityName = activityName;
    }

    @NonNull
    @Override
    public SubTaskAdapter.TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_subtask_layout, parent, false);

        return new TaskHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull SubTaskAdapter.TaskHolder holder, int position) {

        holder.taskTitle.setText(list.get(position).getTitle());

        if (list.get(position).getStatus().equalsIgnoreCase("COMPLETED")){

            holder.checkImg.setImageDrawable(context.getResources().getDrawable(R.drawable.check));

        }

        holder.fullLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                View view1 = LayoutInflater.from(context).inflate(R.layout.bottomsheet_subtask_cc, null, false);
                bottomSheetDialog.setContentView(view1);
                bottomSheetDialog.show();

                LinearLayout completeBtn, cancleBtn, deleteBtn;

                completeBtn = bottomSheetDialog.findViewById(R.id.completedBtn);
                cancleBtn = bottomSheetDialog.findViewById(R.id.cancleBtn);
                deleteBtn = bottomSheetDialog.findViewById(R.id.deleteBtn);

                completeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();

                        holder.checkImg.setImageDrawable(context.getResources().getDrawable(R.drawable.check));

                        StringRequest request = new StringRequest(Request.Method.POST, complateSubtaskApi,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
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

                                params.put("subtask_id", list.get(position).getSubTaskId());
                                params.put("status", "COMPLETED");
                                params.put("complete_date", Common.getDateTime());

                                return params;
                            }
                        };

                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(request);

                    }
                });

                cancleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });

                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        bottomSheetDialog.dismiss();
                        holder.taskTitle.setText("DELETED");

                        StringRequest request = new StringRequest(Request.Method.POST, deleteSubtaskApi,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equalsIgnoreCase("Failed") || response.contains("Failed")){
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
                                Map<String, String> params  = new HashMap<>();

                                params.put("subtask_id", list.get(position).getSubTaskId());

                                return params;
                            }
                        };

                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(request);

                    }
                });

            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TaskHolder extends RecyclerView.ViewHolder {

        TextView taskTitle;
        CircleImageView checkImg;
        LinearLayout fullLayout;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);

            taskTitle = itemView.findViewById(R.id.taskTitle);
            checkImg = itemView.findViewById(R.id.checkImg);
            fullLayout = itemView.findViewById(R.id.fullLL);

        }
    }
}
