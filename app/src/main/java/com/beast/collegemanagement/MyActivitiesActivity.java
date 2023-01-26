package com.beast.collegemanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beast.collegemanagement.adapters.FeedsAdapter;
import com.beast.collegemanagement.databinding.ActivityMyActivitiesBinding;
import com.beast.collegemanagement.models.FeedsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyActivitiesActivity extends AppCompatActivity {

    private ActivityMyActivitiesBinding binding;
    private Context context;

    List<FeedsModel> list;
    FeedsAdapter adapter;

    String fetchTasksHistoryApi = Common.getBaseUrl() + "FetchUserActivities.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_activities);
        context = MyActivitiesActivity.this;
        binding.mainLL.setBackgroundColor(getResources().getColor(R.color.icons));

        list = new ArrayList<>();
        RecyclerView recyclerView = binding.getRoot().findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FeedsAdapter(list, context, "HistoryFragment");
        recyclerView.setAdapter(adapter);

        fetchActivities();

    }

    private void fetchActivities() {

        StringRequest request = new StringRequest(Request.Method.POST, fetchTasksHistoryApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String success = jsonObject.getString("success");

                            if (success.equalsIgnoreCase("1")){

                                for (int i=0; i<jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                            list.add(new FeedsModel(object.getString("task_id"),
                                                    object.getString("username"),
                                                    Common.getPosition(context),
                                                    Common.getProfilePic(context),
                                                    object.getString("action_text"),
                                                    object.getString("time_date"),
                                                    ""));

                                            adapter.notifyDataSetChanged();

                                }

                            }

                        } catch (JSONException e) {
                            Toast.makeText(context, "format error", Toast.LENGTH_SHORT).show();
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

                params.put("username", Common.getUserName(context));

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }
}