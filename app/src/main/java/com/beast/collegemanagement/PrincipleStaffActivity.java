package com.beast.collegemanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.beast.collegemanagement.databinding.ActivityPrincipleStaffBinding;

import java.util.HashMap;
import java.util.Map;

public class PrincipleStaffActivity extends AppCompatActivity {

    private ActivityPrincipleStaffBinding binding;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String positionStatus;
    String API_CHANGE_POSITION = "https://biochemical-damping.000webhostapp.com/Management%20of%20College/updateposition.php";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_principle_staff);
        progressDialog = new ProgressDialog(this);
        sp = getSharedPreferences("FILE_NAME", MODE_PRIVATE);
        editor = sp.edit();

        positionStatus = sp.getString("POSITION", null);

        if (positionStatus != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        binding.principle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertBuilder("principle");
            }
        });

        binding.staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertBuilder("staff");
            }
        });

    }

    private void showAlertBuilder(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(PrincipleStaffActivity.this);
        View view = getLayoutInflater().inflate(R.layout.alert_profile_icon, null);
        builder.setView(view);
        ImageView imageView = view.findViewById(R.id.profile_pic);

        if (msg.equals("principle"))
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.color_primary_person));
        else if(msg.equals("staff"))
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.color_primary_person));
        builder.setMessage(msg + " : is your final selection?");
        builder.setCancelable(false);
        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        changePosition(msg);
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

    private void changePosition(String position) {

        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, API_CHANGE_POSITION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase("Position Updated") || response.contains("Position Updated")){

                            editor.putString("POSITION", position);
                            editor.apply();

                            progressDialog.dismiss();

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();

                        }else {
                            Toast.makeText(PrincipleStaffActivity.this, "failed", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("position", position);
                params.put("userName", sp.getString("userName", null));

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }
}