package com.beast.collegemanagement;

import static com.beast.collegemanagement.Common.ConvertToString;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beast.collegemanagement.adapters.FreeDriveAdapter;
import com.beast.collegemanagement.databinding.ActivityFreeDriveBinding;
import com.beast.collegemanagement.models.FreeDriveModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreeDriveActivity extends AppCompatActivity {

    private ActivityFreeDriveBinding binding;
    private Context context;

    List<FreeDriveModel> list;
    FreeDriveAdapter adapter;

    String uploadFileToDriveApi = Common.getBaseUrl() + "uploadFileToDrive.php";
    String fetchDriveApi = Common.getBaseUrl() + "fetchFreeDriveFiles.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_free_drive);
        context = FreeDriveActivity.this;
        binding.mainLL.setBackgroundColor(getResources().getColor(R.color.icons));

        list = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FreeDriveAdapter(list, context, "FreeDriveActivity");
        recyclerView.setAdapter(adapter);

        fetchDriveLink();

        binding.addDriveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 0);
            }
        });

    }

    private void fetchDriveLink() {

        StringRequest request = new StringRequest(Request.Method.POST, fetchDriveApi,
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

                                    list.add(new FreeDriveModel(object.getString("url"),
                                            object.getString("time_date"),
                                            Common.getUserName(context)));

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
                Map<String, String> map = new HashMap<>();

                map.put("username", Common.getUserName(context));

                return map;
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
                data.getData() != null){

            Uri imageUri = data.getData();

            Cursor mCursor =
                    context.getContentResolver().query(imageUri, null, null, null, null);
            int indexedname = mCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            mCursor.moveToFirst();
            String filename = mCursor.getString(indexedname);
            mCursor.close();

            String fileString = ConvertToString(imageUri, context);
            String ext = Common.getExtension(context, imageUri);
            
            filename = String.valueOf(System.currentTimeMillis()) + filename.replace(" ", "_");

            uploadFile(fileString, filename);

        }
        
    }

    private void uploadFile(String fileString, String filename) {

        list.clear();

        StringRequest request = new StringRequest(Request.Method.POST, uploadFileToDriveApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("failed") || response.contains("failed")){
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        }
                        fetchDriveLink();
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

                params.put("file_name", filename);
                params.put("userName", Common.getUserName(context));
                params.put("file_string", fileString);
                params.put("time_date", Common.getTimeDate());

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }
}