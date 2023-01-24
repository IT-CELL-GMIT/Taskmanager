package com.beast.collegemanagement.adapters;

import static com.beast.collegemanagement.Common.getTimeDate;
import static com.beast.collegemanagement.Common.getUserName;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.model.layer.BaseLayer;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beast.collegemanagement.Common;
import com.beast.collegemanagement.R;
import com.beast.collegemanagement.databinding.ActivityAddGroupBinding;
import com.beast.collegemanagement.databinding.BottomsheetLoginDetailsBinding;
import com.beast.collegemanagement.models.ChatListModel;
import com.google.android.gms.common.internal.StringResourceValueReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddGroupActivity extends AppCompatActivity {

    private ActivityAddGroupBinding binding;
    private Context context;

    Boolean isGroupPicSelected = false;
    String groupImgString = "";
    String ext = "";

    String addGroupApi = Common.getBaseUrl() + "AddGroupChat.php";
    String addGroupParticipantsApi = Common.getBaseUrl() + "AddGroupParticipants.php";
    String changeStatusGPApi = Common.getBaseUrl() + "ChangeStatusGP.php";
    String fetchUserdata = Common.getBaseUrl() + "fetchuserdata.php";
    String sendMsgOn = Common.getBaseUrl() + "insertchat.php";

    List<ChatListModel> groupList;
    AddGroupChatAdapter groupChatAdapter;

    String groupId;
    String userNames = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_group);
        context = AddGroupActivity.this;

        binding.mainLL.setBackgroundColor(getResources().getColor(R.color.icons));

        binding.groupDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.edGroupName.getText().toString().trim().length() < 4){
                    Toast.makeText(context, "enter valid group name", Toast.LENGTH_SHORT).show();
                }else if (!isGroupPicSelected){
                    Toast.makeText(context, "please select group icon first", Toast.LENGTH_SHORT).show();
                }else {
                    addGroup();
                }

            }
        });

        binding.groupProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 0);
            }
        });

        for (int i=0; i<Common.commonList.size(); i++){

            userNames = userNames + Common.commonList.get(i) + "xxx";

        }


        groupList = new ArrayList<>();
        RecyclerView groupChatRecyclerview = findViewById(R.id.groupMembersRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        groupChatRecyclerview.setLayoutManager(layoutManager);
        groupChatAdapter = new AddGroupChatAdapter(groupList, context, "AddGroupActivity");
        groupChatRecyclerview.setAdapter(groupChatAdapter);



        getMembers();

    }

    private void getMembers() {

        for (int i=0; i<Common.commonList.size(); i++){

            getUserInfoBTN(Common.commonList.get(i));

        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getData() != null) {

            Uri imageUri = data.getData();

            groupImgString = Common.ConvertToString(imageUri, context);
            ext = Common.getExtension(context, imageUri);
            isGroupPicSelected = true;

            binding.groupProfile.setImageURI(imageUri);

        }

    }

    private void addGroup() {

        Common.showProgressDialog(context, "Creating a group...");

        groupId = "group_" + Common.getUserName(context) + "_" + String.valueOf(System.currentTimeMillis());

        StringRequest request = new StringRequest(Request.Method.POST, addGroupApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("failed") || response.contains("failed")){
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                            Common.dismissProgressDialog();
                        }else {
                            addGroupParticipants();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "connection error", Toast.LENGTH_SHORT).show();
                Common.dismissProgressDialog();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("group_id", groupId);
                params.put("group_profile", groupImgString);
                params.put("creater", Common.getUserName(context));
                params.put("group_name", binding.edGroupName.getText().toString().trim());
                params.put("time_date", Common.getTimeDate());
                params.put("extension", ext);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void addGroupParticipants() {


        StringRequest request = new StringRequest(Request.Method.POST, addGroupParticipantsApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){

                        if (response.equalsIgnoreCase("failed") || response.contains("failed")){
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                            Common.dismissProgressDialog();
                        }else {
                            makeUserAdmin();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "connection error", Toast.LENGTH_SHORT).show();
                Common.dismissProgressDialog();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("usernames", userNames + Common.getUserName(context));
                params.put("status", "");
                params.put("group_id", groupId);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);


    }

    private void makeUserAdmin() {

        StringRequest request = new StringRequest(Request.Method.POST, changeStatusGPApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase("failed") || response.contains("failed")){
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        }else {
                            setMsgOnline();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "connection error", Toast.LENGTH_SHORT).show();
                Common.dismissProgressDialog();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("group_id", groupId);
                params.put("userName", Common.getUserName(context));
                params.put("status", "Admin");

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }


    private void setMsgOnline() {

        StringRequest request = new StringRequest(Request.Method.POST, sendMsgOn,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                        if (response.contains("Data Inserted") || response.equalsIgnoreCase("Data Inserted")) {
                            Common.dismissProgressDialog();
                            finish();
                        } else {
                            Common.dismissProgressDialog();
                            Toast.makeText(context, "failed to send{Initial Error}", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Common.dismissProgressDialog();
                Toast.makeText(context, "unable send msg{Network Error}", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("sender", Common.getUserName(context));
                params.put("receiver", groupId);
                params.put("content", Common.getUserName(context) + " has created a group");
                params.put("datetime", getTimeDate());
                params.put("url", "none");
                params.put("type", "TEXT");
                params.put("name", "none");
                params.put("extension", "none");

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }



}