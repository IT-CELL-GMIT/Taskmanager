package com.beast.collegemanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beast.collegemanagement.adapters.StaffAdapter;
import com.beast.collegemanagement.databinding.ActivityAddStaffBinding;
import com.beast.collegemanagement.models.StaffModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddStaffActivity extends AppCompatActivity {

    private ActivityAddStaffBinding binding;

    LinearLayout linearLayout;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    StaffAdapter adapter;
    StaffModel model;
    List<StaffModel> list;

    String API_FETCH_ALLDATA = "https://biochemical-damping.000webhostapp.com/Management%20of%20College/fetchalldata.php";

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_staff);

        binding.ll.setBackgroundColor(getResources().getColor(R.color.icons));

        progressDialog = new ProgressDialog(this);

        recyclerView = findViewById(R.id.recylerStaffList);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        adapter = new StaffAdapter(list, AddStaffActivity.this, "AddStaff");
        recyclerView.setAdapter(adapter);
        
//        getUserData();

            getData();

    }

    private void getData() {

        model = new StaffModel("20",
                "Sanjay1",
                "Sanjay Parmar",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
                "555555555555",
                "laudabhencho@gmail.com",
                "xxxxxxxxxxxxx",
                "");

        list.add(model);
        adapter.notifyDataSetChanged();

        model = new StaffModel("20",
                "Tanishq_Kumar",
                "Tanishq Kumar",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
                "555555555555",
                "laudabhencho@gmail.com",
                "xxxxxxxxxxxxx",
                "staff");

        list.add(model);
        adapter.notifyDataSetChanged();


    }

    private void getUserData() {

        progressDialog.setMessage("Getting Info...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, API_FETCH_ALLDATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            if (success.equals("1")){
                                String userId, username, fullName, profilePic, phoneNumber, eMail, password, position;

                                JSONObject object = jsonArray.getJSONObject(0);

                                userId = object.getString("id");
                                username = object.getString("username");
                                fullName = object.getString("fullname");
                                profilePic = object.getString("profilepic");
                                phoneNumber = object.getString("phonenumber");
                                eMail = object.getString("email");
                                password = object.getString("password");
                                position = object.getString("position");

                                progressDialog.dismiss();

                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(AddStaffActivity.this, "not succeed try later", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(AddStaffActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddStaffActivity.this, "failed to retrive old data, try again later", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }
}