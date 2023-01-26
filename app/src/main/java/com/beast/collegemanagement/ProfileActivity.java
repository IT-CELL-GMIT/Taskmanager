package com.beast.collegemanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beast.collegemanagement.databinding.ActivityProfileBinding;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private Context context;
    Uri imagePath;
    String profileString = "";

    String API_UPDATE_PROFILEPIC = "https://biochemical-damping.000webhostapp.com/Management%20of%20College/updateprofilepic.php";
    String API_CHANGE_FULLNAME = "https://biochemical-damping.000webhostapp.com/Management%20of%20College/changefullname.php";
    String API_USERID = "https://biochemical-damping.000webhostapp.com/Management%20of%20College/fetchuserid.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        context = ProfileActivity.this;
        binding.mainBody.setBackgroundColor(getResources().getColor(R.color.icons));

        Glide.with(context).load(Common.getProfilePic(context)).into(binding.profilePic);
        binding.fullName.setText(Common.getFullName(context));
        
        binding.btnCencle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFullName(binding.fullName.getText().toString());
            }
        });

        binding.changeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalary();
            }
        });

    }

    private void openGalary() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select profile pic"), 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 &&
                resultCode == RESULT_OK &&
                data.getData() != null){

            imagePath = data.getData();
            giveUpdateProfileWarning();

        }

    }

    private void giveUpdateProfileWarning() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = getLayoutInflater().inflate(R.layout.alert_profile_icon, null);
        builder.setView(view);
        ImageView imageView = view.findViewById(R.id.profile_pic);
        imageView.setImageURI(imagePath);
        builder.setMessage("do you really want to change profile pic");
        builder.setCancelable(false);
        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        profileString = Common.ConvertToString(imagePath, context);
                        changeProfilePic();
                    }
                });

        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.setOnShowListener(arg0 -> {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.grey));
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        });
        alert.show();

    }

    private void changeProfilePic() {

        StringRequest request = new StringRequest(Request.Method.POST, API_UPDATE_PROFILEPIC,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase("Image Uploaded") || response.contains("Image Uploaded")){

                            getNewProfilePic();

                        }else {
                            Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("profilePic", profileString);
                params.put("userName", Common.getUserName(context));

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

    private void getNewProfilePic() {

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
                                String profilePic2 = object.getString("profilepic");

                                Toast.makeText(context, "profile updated", Toast.LENGTH_SHORT).show();
//                                Glide.with(getApplicationContext()).load(profilePic2).into(binding.toolBarProfilePic);

                                Common.setNameToSharedPref("profilePic", profilePic2, context);

                                finish();

                            }

                        }catch (Exception e){
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
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

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }


    private void changeFullName(String fullname) {

        StringRequest request = new StringRequest(Request.Method.POST, API_CHANGE_FULLNAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        if (response.equalsIgnoreCase("Name Updated") || response.contains("Name Updated")){

                            Common.setNameToSharedPref("fullName", fullname, context);

                        }else {

                        }
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("userName", Common.getUserName(context));
                params.put("fullName", fullname);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

}