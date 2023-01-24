package com.beast.collegemanagement;

import static com.beast.collegemanagement.Common.ConvertToString;
import static com.beast.collegemanagement.Common.commonList;
import static com.beast.collegemanagement.Common.getTimeDate;
import static com.beast.collegemanagement.Common.getUserName;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beast.collegemanagement.adapters.ChatsAdapter;
import com.beast.collegemanagement.databinding.ActivityChatsBinding;
import com.beast.collegemanagement.models.ChatsModel;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatsActivity extends AppCompatActivity {

    String userId, userName, fullName,
            profilePic, phoneNumber, eMail, lastOnline, position;

    private ActivityChatsBinding binding;

    static List<ChatsModel> list;
    ChatsAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    Toast toast;

    String sendMsgOn = Common.getBaseUrl() + "insertchat.php";
    String apiGetChat = Common.getBaseUrl() + "getChat.php";
    String imageChatApi = Common.getBaseUrl() + "insertImageChat.php";
    String docChatApi = Common.getBaseUrl() + "insertDocChat.php";

    Context context;

    Thread thread;

    int chatListSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chats);

        binding.barLL.setBackgroundColor(getResources().getColor(R.color.icons));
        binding.mainLL.setBackgroundColor(getResources().getColor(R.color.light));

        context = ChatsActivity.this;

        Intent intent = getIntent();

        userId = intent.getStringExtra("userId");
        userName = intent.getStringExtra("userName");
        fullName = intent.getStringExtra("fullName");
        profilePic = intent.getStringExtra("profilePic");
        phoneNumber = intent.getStringExtra("phoneNumber");
        eMail = intent.getStringExtra("eMail");
        lastOnline = intent.getStringExtra("lastOnline");
        position = intent.getStringExtra("position");

        if (userId == null || userName == null ||
                fullName == null || profilePic == null) {
            finish();
        }

        sp = getSharedPreferences("FILE_NAME", MODE_PRIVATE);
        editor = sp.edit();

        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        list = new ArrayList<>();
        adapter = new ChatsAdapter(list, ChatsActivity.this);
        recyclerView.setAdapter(adapter);

        Glide.with(this).load(profilePic).into(binding.profilePic);
        binding.tvFullname.setText(fullName);

        if (lastOnline.contains("xxx")) {
            String lastSeenSplits[] = lastOnline.split("xxx");
            binding.tvLastseen.setText(lastSeenSplits[0] + "   " + lastSeenSplits[1]);
        } else {
            binding.tvLastseen.setText(lastOnline);
        }

//        getMsgs();

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

        binding.showAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet();
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMsg(binding.edMsg.getText().toString().trim());
                binding.edMsg.setText("");
            }
        });

        chatListSize = 0;
        getChat();

        thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        sleep(1500);
                        getChat();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

    }

    private void getChat() {

        StringRequest request = new StringRequest(Request.Method.POST, apiGetChat,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String success = jsonObject.getString("success");

                            if (success.equalsIgnoreCase("1")) {

                                if (chatListSize != jsonArray.length()) {

                                    list.clear();
                                    chatListSize = jsonArray.length();

                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject object = jsonArray.getJSONObject(i);

                                        list.add(new ChatsModel(object.getString("datetime"),
                                                object.getString("content"),
                                                object.getString("url"),
                                                object.getString("type"),
                                                object.getString("sender"),
                                                object.getString("receiver"),
                                                object.getString("name")));

                                        adapter.notifyDataSetChanged();

                                    }


                                }

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getChat();
                                    }
                                }, 1500);

                            }

                        } catch (JSONException e) {
                            Toast.makeText(context, "format error{JSON}", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "network error", Toast.LENGTH_SHORT).show();
                finish();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("sender", getUserName(context));
                params.put("receiver", userName);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

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

    private void setMsgOnline(String msg) {

        StringRequest request = new StringRequest(Request.Method.POST, sendMsgOn,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                        if (response.contains("Data Inserted") || response.equalsIgnoreCase("Data Inserted")) {
                            Toast.makeText(context, "msg", Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(context, "failed to send{Initial Error}", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                list.remove(0);
                Toast.makeText(context, "unable send msg{Network Error}", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("sender", getUserName(context));
                params.put("receiver", userName);
                params.put("content", msg);
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

    private void getMsgs() {

        list.add(0, new ChatsModel("UNKNOW",
                "hii how are you",
                "",
                "TEXT",
                sp.getString("userName", null),
                "sanjay@123",
                ""));

        list.add(0, new ChatsModel("UNKNOW",
                "i'm fine",
                "",
                "TEXT",
                "sanjay@123",
                sp.getString("userName", null),
                ""));

        list.add(0, new ChatsModel("UNKNOW",
                "",
                profilePic,
                "IMAGE",
                "sanjay@123",
                sp.getString("userName", null),
                "tonystark.jpeg"));

        list.add(0, new ChatsModel("UNKNOW",
                "",
                profilePic,
                "IMAGE",
                sp.getString("userName", null),
                "",
                "tonystark.jpeg"));

        adapter.notifyDataSetChanged();

    }

    private void showBottomSheet() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

        View view = LayoutInflater.from(this).inflate(R.layout.bottomsheet_attachments, null, false);

        bottomSheetDialog.setContentView(view);

        bottomSheetDialog.show();

        LinearLayout gallaryBtn = bottomSheetDialog.findViewById(R.id.ln_gallary);

        gallaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 0);
                bottomSheetDialog.dismiss();
            }
        });
        
        LinearLayout documentBtn = bottomSheetDialog.findViewById(R.id.ln_document);
        
        documentBtn.setOnClickListener(new View.OnClickListener() {
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getData() != null) {

            Uri imageUri = data.getData();

            String imageString = Common.ConvertToString(imageUri, context);
            String ext = Common.getExtension(context, imageUri);
            String fileType = "IMAGE";

            uploadImg(imageString, ext, fileType);

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
            
            uploadDoc(pdfString, ext, filename, fileType);

//            Toast.makeText(context, (CharSequence) imageUri, Toast.LENGTH_SHORT).show();

//            Intent i = new Intent(Intent.ACTION_VIEW);
//            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            i.setDataAndType(imageUri, context.getContentResolver().getType(imageUri));
//            context.startActivity(i);

        }

        
    }

    private void uploadDoc(String pdfString, String ext, String filename, String fileType) {
        
        StringRequest request = new StringRequest(Request.Method.POST, docChatApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("Data Inserted") || response.equalsIgnoreCase("Data Inserted")){

                        }else {
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
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

                params.put("sender", Common.getUserName(context));
                params.put("receiver", userName);
                params.put("content", pdfString);
                params.put("datetime", Common.getTimeDate());
                params.put("url", filename);
                params.put("type", fileType);
                params.put("name", Common.getUserName(context) + "_" + userName + "_" + String.valueOf(System.currentTimeMillis()) + "." + ext);
                params.put("extension", ext);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
        
    }

    private void uploadImg(String imageString, String ext, String fileType) {

        StringRequest request = new StringRequest(Request.Method.POST, imageChatApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("Data Inserted") || response.equalsIgnoreCase("Data Inserted")){

                        }else {
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
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

                params.put("sender", Common.getUserName(context));
                params.put("receiver", userName);
                params.put("content", imageString);
                params.put("datetime", Common.getTimeDate());
                params.put("url", "none");
                params.put("type", fileType);
                params.put("name", Common.getUserName(context) + "_" + userName + "_" + String.valueOf(System.currentTimeMillis()) + "." + ext);
                params.put("extension", ext);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

}