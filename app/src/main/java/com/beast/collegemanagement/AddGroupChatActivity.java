package com.beast.collegemanagement;

import static com.beast.collegemanagement.Common.getUserName;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beast.collegemanagement.adapters.AddGroupActivity;
import com.beast.collegemanagement.adapters.AddGroupChatAdapter;
import com.beast.collegemanagement.databinding.ActivityAddGroupChatBinding;
import com.beast.collegemanagement.models.ChatListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddGroupChatActivity extends AppCompatActivity {

    String fechFrindList = Common.getBaseUrl() + "fetchFriendList.php";
    String fetchUserdata = Common.getBaseUrl() + "fetchuserdata.php";

    private ActivityAddGroupChatBinding binding;
    private Context context;
    
    List<ChatListModel> groupList;
    AddGroupChatAdapter groupChatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_group_chat);
        context = AddGroupChatActivity.this;


        
        groupList = new ArrayList<>();
        RecyclerView groupChatRecyclerview = findViewById(R.id.groupChatRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        groupChatRecyclerview.setLayoutManager(layoutManager);
        groupChatAdapter = new AddGroupChatAdapter(groupList, context, "AddGroupChatActivity");
        groupChatRecyclerview.setAdapter(groupChatAdapter);
        
        
        
        getFriends();
        
        binding.mainLL.setBackgroundColor(getResources().getColor(R.color.icons));
        
        binding.addGroupChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.commonList.size() < 2){
                    Toast.makeText(context, "select minimum 2 people", Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(context, AddGroupActivity.class));
                    finish();
                }
            }
        });
        

    }



    private void getFriends() {

        StringRequest request = new StringRequest(Request.Method.POST, fechFrindList,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String success = jsonObject.getString("success");

                            if (success.equalsIgnoreCase("1")){

                                Common.commonList.clear();

                                for (int i=0; i<jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    getUserInfoBTN(object.getString("friendname"));

                                }

                            }

                        } catch (JSONException e) {
                            Toast.makeText(context, "format error", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "connetction error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("username", getUserName(context));

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }


    private void getUserInfoBTN(String friendname) {

        StringRequest request = new StringRequest(Request.Method.POST, fetchUserdata,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String success = jsonObject.getString("success");

                            if (success.equalsIgnoreCase("1")){

                                JSONObject object = jsonArray.getJSONObject(0);
                                
                                groupList.add(new ChatListModel(object.getString("id"),
                                        object.getString("username"),
                                        object.getString("fullname"),
                                        object.getString("profilepic"),
                                        object.getString("phonenumber"),
                                        object.getString("email"),
                                        "recently",
                                        object.getString("position"),
                                        "0",
                                        "IMG",
                                        "TEXT"));
                                
                                groupChatAdapter.notifyDataSetChanged();

                            }

                        } catch (JSONException e) {
                            Toast.makeText(context, "format error", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
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

                params.put("username", friendname);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }


}