package com.beast.collegemanagement;

import static com.beast.collegemanagement.Common.getBaseUrl;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.display.DeviceProductInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beast.collegemanagement.databinding.ActivityLoginpageBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Loginpage extends AppCompatActivity {

    private ActivityLoginpageBinding binding;
    String userName, password;
    ProgressDialog progressDialog;
    String API_LOGIN = getBaseUrl() +  "logincollegemanagement.php";
    String API_FETCHDATA = getBaseUrl() +  "fetchuserdata.php";
    String singStatus = "", logStatus = "";
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        binding = DataBindingUtil.setContentView(this, R.layout.activity_loginpage);

        sp = getSharedPreferences("FILE_NAME", MODE_PRIVATE);
        editor = sp.edit();

        singStatus = sp.getString("SIGN_STATUS", null);
        logStatus = sp.getString("LOG_STATUS", "NONE");

        if (logStatus.equals("LOGGEDOUT")){

            showBottomSheet();

        }else if (singStatus != null){
            startActivity(new Intent(getApplicationContext(), PrincipleStaffActivity.class));
            finish();
        }

        progressDialog = new ProgressDialog(this);

        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Loginpage.this,Signuppage.class));
            }
        });

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userName = binding.userName.getText().toString().trim().toLowerCase();
                password = binding.password.getText().toString().trim();
                
                binding.userName.setText("");
                binding.password.setText("");
                
                progressDialog.setMessage("Checking Details...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                
                checkLogin(userName, password);
                
            }
        });
    }

    private void showBottomSheet() {

        bottomSheetDialog = new BottomSheetDialog(this);

        View view = getLayoutInflater().inflate(R.layout.bottomsheet_login_details, null);

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.show();

        EditText userName, password;

        userName = view.findViewById(R.id.userName);
        password = view.findViewById(R.id.password);

        String userId = sp.getString("userName", null);

        if (userId != null){

            userName.setText(userId);
            password.setText(sp.getString("password", null));

            view.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.setMessage("Checking Details...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    checkLogin(userId, sp.getString("password", null));
                }
            });

        }else {
            bottomSheetDialog.dismiss();
        }

    }

    private void checkLogin(String userName, String password) {

        StringRequest request = new StringRequest(Request.Method.POST, API_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase("Loged in successfully") || response.contains("Loged in successfully")){
                            progressDialog.dismiss();

                            progressDialog.setMessage("Getting Info...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            
                            fetchUserData(userName, password);
                            
                        }else {
                            Toast.makeText(Loginpage.this, "wrong login details", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Loginpage.this, "anable to login, try again later", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("username", userName);
                params.put("password", password);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

    private void fetchUserData(String userName, String password) {

        StringRequest request = new StringRequest(Request.Method.POST, API_FETCHDATA,
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

                                if (userName.equals(username)){

                                    editor.putString("userId", userId);
                                    editor.putString("userName", username);
                                    editor.putString("fullName", fullName);
                                    editor.putString("profilePic", profilePic);
                                    editor.putString("phoneNumber", phoneNumber);
                                    editor.putString("eMail", eMail);
                                    editor.putString("password", password);
                                    editor.putString("SIGN_STATUS", "SIGNED");
                                    editor.putString("LOG_STATUS", "NONE");
                                    editor.putString("POSITION", position);
                                    editor.apply();

                                    progressDialog.dismiss();

                                    startActivity(new Intent(getApplicationContext(), PrincipleStaffActivity.class));
                                    finish();

                                }else {
                                    progressDialog.dismiss();
                                    Toast.makeText(Loginpage.this, "Username of database is not matching try later", Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(Loginpage.this, "not succeed try later", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(Loginpage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Loginpage.this, "failed to retrive old data, try again later", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("username", userName);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }
}