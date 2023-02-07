package com.beast.collegemanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beast.collegemanagement.databinding.ActivitySignuppageBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Signuppage extends AppCompatActivity {

    private ActivitySignuppageBinding binding;
    
    String fullName, userName, profileString = "null", phoneNumber, eMail, password;
    boolean profilePicSelect = false;
    Uri imagePath;
    ProgressDialog progressDialog;
    String API_SIGNUP_COLLEGEMANAGEMENT = Common.getBaseUrl() + "singupincollegemanagement.php";
//    String USER_CHECK = Common.getBaseUrl() + "checkusername.php";
    String USER_CHECK ="https://www.zocarro.net/task_manager/Management%20of%20College/checkusername.php";
    String API_USERID = Common.getBaseUrl() + "fetchuserid.php";
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String signStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signuppage);

        progressDialog = new ProgressDialog(this);

        sp = getSharedPreferences("FILE_NAME", MODE_PRIVATE);
        editor = sp.edit();

        signStatus = sp.getString("SIGN_STATUS", null);

        binding.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "please select your profile pic"), 1);

            }
        });

        binding.login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Loginpage.class));
                finish();
            }
        });

        binding.signup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(Signuppage.this, "sign up", Toast.LENGTH_SHORT).show();
                if (binding.userName.getText().toString().trim().contains(" ")){
                    Toast.makeText(Signuppage.this, "spaces are not allowed in Username", Toast.LENGTH_SHORT).show();
                }
                else {

                    fullName = binding.fullName.getText().toString().trim();
                    userName = binding.userName.getText().toString().trim().toLowerCase();
                    phoneNumber = binding.phoneNumber.getText().toString().trim();
                    eMail = binding.eMail.getText().toString().trim();
                    password = binding.password.getText().toString().trim();

                    checkData();
                }
                
            }
        });
    }

    private void checkData() {

        if (fullName.length() < 5 || !fullName.contains(" ")){
            Toast.makeText(this, "Please enter FULLNAME for ex :\nHimanshu Pandey", Toast.LENGTH_SHORT).show();
        }else if (userName.length() < 5){
            Toast.makeText(this, "please enter proper Username", Toast.LENGTH_SHORT).show();
        }else if (profileString.equalsIgnoreCase("null") || !profilePicSelect){
            Toast.makeText(this, "please select profile picture", Toast.LENGTH_SHORT).show();
        }else if (phoneNumber.length() < 10){
            Toast.makeText(this, "please enter proper phone number", Toast.LENGTH_SHORT).show();
        }else if (eMail.length() < 12 || !eMail.contains("@") || !eMail.contains(".com")){
            Toast.makeText(this, "please enter proper E-Mail for ex :\nhimanshu@gmail.com", Toast.LENGTH_SHORT).show();
        }else if (password.length() < 8){
            Toast.makeText(this, "please choose strong password of minimum 8 length", Toast.LENGTH_SHORT).show();
        }else {

            progressDialog.setMessage("checking data...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            checkUserName();
        }

    }

    private void checkUserName() {

        StringRequest request = new StringRequest(Request.Method.POST, USER_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase("user exist") || response.contains("user exist")){

                            Toast.makeText(Signuppage.this, "Select different Username", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }else {

                            uploadData();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Signuppage.this, "something went wrong, try again later", Toast.LENGTH_SHORT).show();
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

    private void uploadData() {

        StringRequest request = new StringRequest(Request.Method.POST, API_SIGNUP_COLLEGEMANAGEMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase("Data Inserted") || response.contains("Data Inserted")){

                            binding.fullName.setText("");
                            binding.userName.setText("");
                            binding.phoneNumber.setText("");
                            binding.eMail.setText("");
                            binding.password.setText("");

                            editor.putString("fullName", fullName);
                            editor.putString("userName", userName);
                            editor.putString("phoneNumber", phoneNumber);
                            editor.putString("eMail", eMail);
                            editor.putString("password", password);
                            editor.putString("SIGN_STATUS", "SIGNED");

                            editor.apply();

                            progressDialog.dismiss();

//                            startActivity(new Intent(Signuppage.this, MainActivity.class));
//                            finish();
                            progressDialog.setMessage("Getting Info...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            fetchUserId();

                        }else {

                            Toast.makeText(Signuppage.this, "failed", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Signuppage.this, "something went wrong, try again later", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("fullName", fullName);
                params.put("userName", userName);
                params.put("email", eMail);
                params.put("number", phoneNumber);
                params.put("password", password);
                params.put("profilepic", profileString);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

    private void fetchUserId() {

        StringRequest request = new StringRequest(Request.Method.POST, API_USERID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String success =jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            if (success.equals("1")){

                                JSONObject object = jsonArray.getJSONObject(0);
                                String userId = object.getString("id");
                                String profilePic = object.getString("profilepic");

                                editor.putString("userId", userId);
                                editor.putString("profilePic", profilePic);
                                editor.putString("LOG_STATUS", "NONE");
                                editor.apply();

                                progressDialog.dismiss();

                                startActivity(new Intent(getApplicationContext(), PrincipleStaffActivity.class));
                                finish();

                            }

                        }catch (Exception e){
                            Toast.makeText(Signuppage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Signuppage.this, "something went wrong", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1
        && resultCode == RESULT_OK
        && data.getData() != null){

            imagePath = data.getData();
            binding.profileImageView.setImageURI(imagePath);

            converToString(imagePath);

        }else {
            Toast.makeText(this, "something went wrong try to select a picture try again", Toast.LENGTH_SHORT).show();
            imagePath = null;
        }

    }

    private void converToString(Uri imagePath) {

        try {

            InputStream in = getContentResolver().openInputStream(imagePath);
            byte[] bytes = getBytes(in);

            profileString = Base64.encodeToString(bytes, Base64.DEFAULT);
            profilePicSelect = true;

        } catch (Exception e) {
            Toast.makeText(this, "Please select a image\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    private static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

}