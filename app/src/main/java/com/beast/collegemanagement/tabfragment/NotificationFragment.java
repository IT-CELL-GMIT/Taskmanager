package com.beast.collegemanagement.tabfragment;

import static com.beast.collegemanagement.Common.getBaseUrl;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beast.collegemanagement.R;
import com.beast.collegemanagement.adapters.NotificationAdapter;
import com.beast.collegemanagement.databinding.FragmentNotificationBinding;
import com.beast.collegemanagement.models.NotificationModel;

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
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {

    private FragmentNotificationBinding binding;
    public static List<NotificationModel> list;
    public static NotificationAdapter adapter;

    String getNotification = getBaseUrl() + "getNotification.php";
    String setNotificationState = getBaseUrl() + "setNotificationState.php";
    String getNotificationState = getBaseUrl() + "getNotificationState.php";

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    ProgressDialog progressDialog;

    Boolean connectionBool = true;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        binding.ll.setBackgroundColor(getResources().getColor(R.color.icons));

        sp = getContext().getSharedPreferences("FILE_NAME", Context.MODE_PRIVATE);
        editor = sp.edit();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(true);

        list = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationAdapter(binding.getRoot().getContext(), list);
        binding.recyclerView.setAdapter(adapter);

//        getNotification();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getAllNotification();
            }
        }, 1000);

        return binding.getRoot();
    }

    private void getAllNotification() {

        progressDialog.setMessage("Getting Notification...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        list.clear();

        StringRequest request = new StringRequest(Request.Method.POST, getNotification,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        
                        try{

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String success = jsonObject.getString("success");

                            if (success.equalsIgnoreCase("1")){
                                
                                if (jsonArray.length() == 0){
                                    Toast.makeText(getContext(), "you have no notifications", Toast.LENGTH_SHORT).show();
                                }

                                for (int i=0; i<jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    if (!object.getString("isdisabled").equalsIgnoreCase("yes")){

                                        list.add(0, new NotificationModel(String.valueOf(object.getInt("id")),
                                                object.getString("timedate"),
                                                object.getString("sender"),
                                                object.getString("content"),
                                                object.getString("isseen"),
                                                object.getString("isdisabled"),
                                                object.getString("type")));


                                        adapter.notifyDataSetChanged();
                                        
                                        setNotificationState();

                                    }

                                }

                            }else {
                                Toast.makeText(getContext(), "no notifications", Toast.LENGTH_SHORT).show();
                            }
                            
                        }catch (Exception e){
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "formation error", Toast.LENGTH_SHORT).show();
                        }
                        
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "network error", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("receiver", sp.getString("userName", null));

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(binding.getRoot().getContext());
        queue.add(request);

    }

    private void setNotificationState() {

        progressDialog.setMessage("setting up...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        
        StringRequest request = new StringRequest(Request.Method.POST, setNotificationState,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        
                        if (response.equalsIgnoreCase("Data Inserted") || response.contains("Data Inserted")){
//                            getNotificationState();
                        }else {
                            Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                        }
                        
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "connection failed!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("userName", sp.getString("userName", null));
                params.put("state", "no");

                return params;
            }
        };
        
        RequestQueue queue = Volley.newRequestQueue(binding.getRoot().getContext());
        queue.add(request);

    }

    private void getNotificationState() {
        StringRequest request = new StringRequest(Request.Method.POST, getNotificationState,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String success = jsonObject.getString("success");

                            if (success.equalsIgnoreCase("1")){

                                JSONObject object = jsonArray.getJSONObject(0);

                                if (object.getString("notify").equalsIgnoreCase("yes") || object.getString("notify").contains("yes")){
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            getAllNotification();
                                        }
                                    }, 2000);
                                }else {

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            getNotificationState();
                                        }
                                    }, 5000);

                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (connectionBool){
                    Toast.makeText(getContext(), "connection problem", Toast.LENGTH_SHORT).show();
                    connectionBool = !connectionBool;
                }

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("userName", sp.getString("userName", null));

                return  params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(binding.getRoot().getContext());
        queue.add(request);

    }

    public static void getNotification() {

        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        String currentDate = new SimpleDateFormat("dd:MM:yyyy", Locale.getDefault()).format(new Date());

        list.add(new NotificationModel("1",
                currentTime + ", " + currentDate,
                "Tanishq Kumar",
                "sent you a freind request",
                "NO",
                "NO",
                "FRIEND_REQUEST"));

        adapter.notifyDataSetChanged();

        list.add(new NotificationModel("2",
                currentTime + ", " + currentDate,
                "Himanshu Pandey",
                "New task has been added tap to view",
                "NO",
                "NO",
                "NORMAL"));

        adapter.notifyDataSetChanged();

    }

    public static void getNewNotification(List<NotificationModel> list){
        adapter.notifyDataSetChanged();
    }

}