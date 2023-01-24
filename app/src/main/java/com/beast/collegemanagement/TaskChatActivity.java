package com.beast.collegemanagement;

import static com.beast.collegemanagement.Common.ConvertToString;
import static com.beast.collegemanagement.Common.getUserName;

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
import android.provider.ContactsContract;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beast.collegemanagement.adapters.GroupChatAdapter;
import com.beast.collegemanagement.databinding.ActivityTaskChatBinding;
import com.beast.collegemanagement.models.ChatsModel;
import com.beast.collegemanagement.models.FilesModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskChatActivity extends AppCompatActivity {

    String taskId, taskName;

    private ActivityTaskChatBinding binding;
    private Context context;

    List<ChatsModel> list;
    GroupChatAdapter adapter;

    String updloadFile = Common.getBaseUrl() + "addFileToTask.php";
    String fetchFilesFromTaksApi = Common.getBaseUrl() + "fetchFilesTask.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task_chat);
        context = TaskChatActivity.this;
        binding.mainLL.setBackgroundColor(getResources().getColor(R.color.icons));

        Intent intent = getIntent();
        taskId = intent.getStringExtra("taskId");
        taskName = intent.getStringExtra("taskName");

        binding.taskId.setText(taskId);
        binding.taskName.setText(taskName);


        list = new ArrayList<>();
        RecyclerView chatsRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setReverseLayout(true);
        chatsRecyclerView.setLayoutManager(layoutManager);
        adapter = new GroupChatAdapter(list, context, "ChatsGroupActivity");
        chatsRecyclerView.setAdapter(adapter);

        getFIles();

        binding.showAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomsheet();
            }
        });




        binding.edMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (i2 == 0) {
                    binding.micBtn.setVisibility(View.VISIBLE);
                    binding.sendMsgBtn.setVisibility(View.GONE);
                } else {
                    binding.micBtn.setVisibility(View.GONE);
                    binding.sendMsgBtn.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMsg(binding.edMsg.getText().toString().trim());
                binding.edMsg.setText("");
            }
        });




    }

    private void sendMsg(String trim) {

        list.add(0, new ChatsModel(Common.getTimeDate(),
                trim,
                "",
                "TEXT",
                getUserName(this),
                "unknown",
                ""));
        adapter.notifyDataSetChanged();

        setMsgOnline(trim);

    }

    private void setMsgOnline(String trim) {

        StringRequest request = new StringRequest(Request.Method.POST, updloadFile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        getFIles();
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
                params.put("fullname", Common.getFullName(context));
                params.put("profilepic", Common.getProfilePic(context));
                params.put("timedate", Common.getTimeDate());
                params.put("content", trim);
                params.put("type", "TEXT");
                params.put("task_file", "");
                params.put("task_id", taskId);
                params.put("file_ext", "");

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }


    private void showBottomsheet() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bottomsheet_attachments, null, false);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        view.findViewById(R.id.ln_gallary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 0);

            }
        });

        view.findViewById(R.id.ln_document).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();

                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select PDF"), 1);

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getData() != null){

            Uri imageUri = data.getData();

            String imageString = Common.ConvertToString(imageUri, context);
            String ext = Common.getExtension(context, imageUri);
            String fileType = "IMAGE";

            uploadFileTo(imageString, ext, fileType, "");

//            try {
//                Common.IMAGE_BITMAP = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    list.add(new FilesModel(getUserName(context),
//                            getFullName(context),
//                            getProfilePic(context),
//                            getTimeDate(),
//                            "",
//                            "IMAGE",
//                            imageUri.toString(),
//                            "UNKNOWN"));
//
//                    adapter.notifyDataSetChanged();
//                }
//            }, 1000);

        }

        if (requestCode == 1 &&
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

            String pdfString = ConvertToString(imageUri, context);
            String ext = Common.getExtension(context, imageUri);
            String fileType = "PDF";

            uploadFileTo(pdfString, ext, fileType, filename);

        }

    }

    private void uploadFileTo(String imageString, String ext, String fileType, String s) {

        Common.showProgressDialog(context, "Please wait...");


        StringRequest request = new StringRequest(Request.Method.POST, updloadFile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Common.dismissProgressDialog();
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        getFIles();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Common.dismissProgressDialog();
                Toast.makeText(context, "connection error", Toast.LENGTH_SHORT).show();
            }
        }){

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("username", Common.getUserName(context));
                params.put("fullname", Common.getFullName(context));
                params.put("profilepic", Common.getProfilePic(context));
                params.put("timedate", Common.getTimeDate());
                params.put("content", s);
                params.put("type", fileType);
                params.put("task_file", imageString);
                params.put("task_id", taskId);
                params.put("file_ext", ext);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }



    private void getFIles() {

        StringRequest request = new StringRequest(Request.Method.POST, fetchFilesFromTaksApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            for (int i=0; i< jsonArray.length(); i++){

                                JSONObject object = jsonArray.getJSONObject(i);

//                                list.add(new FilesModel(object.getString("username"),
//                                        object.getString("fullname"),
//                                        object.getString("profilepic"),
//                                        object.getString("timedate"),
//                                        object.getString("content"),
//                                        object.getString("type"),
//                                        object.getString("link"),
//                                        object.getString("task_id")));

                                if (object.getString("type").equalsIgnoreCase("image")){

                                    String[] nameSplits = object.getString("link").split("/");
                                    String[] nameSplit = nameSplits[nameSplits.length - 1].split("-");
                                    String name = nameSplit[0] + object.getString("timedate");
//

                                    list.add(0, new ChatsModel(object.getString("timedate"),
                                            object.getString("link"),
                                            object.getString("link"),
                                            object.getString("type"),
                                            object.getString("username"),
                                            taskId,
                                            name));

                                    adapter.notifyDataSetChanged();

                                }else if (object.getString("type").equalsIgnoreCase("pdf")){


                                    list.add(0, new ChatsModel(object.getString("timedate"),
                                            object.getString("content"),
                                            object.getString("link"),
                                            object.getString("type"),
                                            object.getString("username"),
                                            taskId,
                                            object.getString("content")));

                                    adapter.notifyDataSetChanged();

                                }else {
                                    list.add(0, new ChatsModel(object.getString("timedate"),
                                            object.getString("content"),
                                            object.getString("link"),
                                            object.getString("type"),
                                            object.getString("username"),
                                            taskId,
                                            object.getString("content")));

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

                params.put("task_id", taskId);

                return params;

            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }


}