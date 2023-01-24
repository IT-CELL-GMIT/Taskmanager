package com.beast.collegemanagement.tabfragment;

import static com.beast.collegemanagement.Common.getUserName;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Freezable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beast.collegemanagement.AddGroupChatActivity;
import com.beast.collegemanagement.AddStaffActivity;
import com.beast.collegemanagement.Common;
import com.beast.collegemanagement.R;
import com.beast.collegemanagement.adapters.ChatListAdapter;
import com.beast.collegemanagement.databinding.FragmentChatfragmentBinding;
import com.beast.collegemanagement.models.ChatListModel;
import com.beast.collegemanagement.models.StaffModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link chatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class chatsFragment extends Fragment {

    private FragmentChatfragmentBinding binding;
    List<ChatListModel> list;
    ChatListAdapter adapter;

    String fechFrindList = Common.getBaseUrl() + "fetchFriendList.php";
    String fetchUserdata = Common.getBaseUrl() + "fetchuserdata.php";
    String apiChatList = Common.getBaseUrl() + "getChatList.php";

    Context context;

    List<ChatListModel> btsList;
    ChatListAdapter btsAdapter;

    List<String> stringList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public chatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment chatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static chatsFragment newInstance(String param1, String param2) {
        chatsFragment fragment = new chatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatfragmentBinding.inflate(inflater, container, false);

        binding.ll.setBackgroundColor(getResources().getColor(R.color.icons));

        list = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChatListAdapter(list, binding.getRoot().getContext(), "CHATSFRAGMENT");
        binding.recyclerView.setAdapter(adapter);

        context = binding.getRoot().getContext();

        stringList = new ArrayList<>();

//        addUser();


        getChats();
        
        binding.addChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetFriends();
            }
        });

        return binding.getRoot();
    }

    private void getChats() {

        stringList = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.POST, apiChatList,
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

                                    if (object.getString("sender").equals(getUserName(context))){
                                        getUserInfo(object.getString("receiver"));
                                    }else if (object.getString("receiver").equalsIgnoreCase(getUserName(context))){
                                        getUserInfo(object.getString("sender"));
                                    }

                                }

                            }

                        } catch (JSONException e) {
                            Toast.makeText(context, "format error{JSON}", Toast.LENGTH_SHORT).show();
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

                params.put("username", getUserName(context));

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void BottomSheetFriends() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_friends, null, false);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        RecyclerView recyclerView = view.findViewById(R.id.btsRecyclerview);
        btsList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        btsAdapter = new ChatListAdapter(btsList, context, "CHATSFRAGMENT");
        recyclerView.setAdapter(btsAdapter);

        bottomSheetDialog.setDismissWithAnimation(true);

        getFriendsBTS();


        LinearLayout addGroupChatBtn = bottomSheetDialog.findViewById(R.id.addGroupChatLL);

        addGroupChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                startActivity(new Intent(context, AddGroupChatActivity.class));
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

                                for (int i=0; i<jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    getUserInfo(object.getString("friendname"));
                                    Toast.makeText(context, object.getString("friendname"), Toast.LENGTH_SHORT).show();

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

    private void getUserInfo(String friendname) {

        if (stringList.contains(friendname)){

        }else {

            stringList.add(friendname);

            StringRequest request = new StringRequest(Request.Method.POST, fetchUserdata,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                String success = jsonObject.getString("success");

                                if (success.equalsIgnoreCase("1")) {

                                    JSONObject object = jsonArray.getJSONObject(0);

                                    list.add(new ChatListModel(object.getString("id"),
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

                                    adapter.notifyDataSetChanged();

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
            }) {
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


    private void getFriendsBTS() {

        StringRequest request = new StringRequest(Request.Method.POST, fechFrindList,
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

                                btsList.add(new ChatListModel(object.getString("id"),
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

                                btsAdapter.notifyDataSetChanged();

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

    private void addUser() {

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        list.add(new ChatListModel("1",
                "sanjay@123",
                "Himanshu Pandey",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
                "7435954425",
                "sanjay@123gmail.com",
                "29-11_2022" + "xxx" + currentTime,
                "OBSERVER",
                "9",
                "get the fucked up",
                "PERSON"));

        adapter.notifyDataSetChanged();

        list.add(new ChatListModel("1",
                "sanjay@123",
                "Tanishq Kumar",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
                "7435954425",
                "sanjay@123gmail.com",
                "27-11_2022" + "xxx" + currentTime,
                "STAFF",
                "10",
                "get the fucked up",
                "PERSON"));

        adapter.notifyDataSetChanged();

        list.add(new ChatListModel("1",
                "sanjay@123",
                "Satyam Chutyam",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
                "7435954425",
                "sanjay@123gmail.com",
                "ONLINE",
                "HEAD",
                "2",
                "get the fucked up",
                "PERSON"));

        adapter.notifyDataSetChanged();

        list.add(new ChatListModel("1",
                "sanjay@123",
                "Hamza Lodha",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
                "7435954425",
                "sanjay@123gmail.com",
                "29-10_2022" + "xxx" + currentTime,
                "H.O.D",
                "0",
                "get the fucked up",
                "PERSON"));

        adapter.notifyDataSetChanged();

    }
}