package com.beast.collegemanagement.tabfragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.beast.collegemanagement.Common;
import com.beast.collegemanagement.R;
import com.beast.collegemanagement.adapters.FeedsAdapter;
import com.beast.collegemanagement.databinding.FragmentHistoryBinding;
import com.beast.collegemanagement.models.FeedsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private Context context;

    List<FeedsModel> list;
    List<Integer> intList;
    FeedsAdapter adapter;
    List<String> checkList;

    String fetchPrincipleTasks = Common.getBaseUrl() + "fetchPrincipleTasks.php";
    String fetchObserversTask = Common.getBaseUrl() + "fetchObsTasks.php";
    String fetchResponsible = Common.getBaseUrl() + "fetchResponisbleTasks.php";
    String fetchParticipantTask = Common.getBaseUrl() + "fetchParticipantTasks.php";
    String fetchTasksHistoryApi = Common.getBaseUrl() + "FetchTaskHistory.php";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_history, container, false);
        context = binding.getRoot().getContext();
        binding.ll.setBackgroundColor(getResources().getColor(R.color.icons));


        intList = new ArrayList<>();
        list = new ArrayList<>();
        checkList = new ArrayList<>();
        RecyclerView recyclerView = binding.getRoot().findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FeedsAdapter(list, context, "HistoryFragment");
        recyclerView.setAdapter(adapter);

        getHistory();

        return binding.getRoot();
    }

    private void getHistory() {

        checkList.clear();

        if (Common.getPosition(context).equalsIgnoreCase("principle")){
            getPrincipleTaskChat();
        }else if (Common.getPosition(context).equalsIgnoreCase("observer")){
            getObserversTaskChat();
        }else if (Common.getPosition(context).equalsIgnoreCase("HOD")){
            getHODPrincipleTaskChat();
        }else if (Common.getPosition(context).equalsIgnoreCase("staff")){
            getStaffTaskChat();
        }

    }

    private void fetchHistory(){

        String taskIds = "";

        for (int i=0; i<checkList.size(); i++){

            taskIds = taskIds + checkList.get(i) + "xxx";

            if (i == checkList.size() - 1){
                finalFetchHistory(taskIds);
            }

        }

    }

    private void finalFetchHistory(String taskIds) {

        StringRequest request = new StringRequest(Request.Method.POST, fetchTasksHistoryApi,
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

                                    intList.add(Integer.parseInt(object.getString("id")));

                                    Collections.sort(intList, Collections.reverseOrder());

                                    for (int j = 0; j<intList.size(); j++){

                                        if (Integer.parseInt(object.getString("id")) == intList.get(j)){

                                            list.add(j, new FeedsModel(object.getString("task_id"),
                                                    object.getString("username"),
                                                    object.getString("position"),
                                                    object.getString("profilePic"),
                                                    object.getString("action_text"),
                                                    object.getString("time_date"),
                                                    ""));

                                            adapter.notifyDataSetChanged();

                                        }

                                    }


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

                params.put("usernames", taskIds);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void getStaffTaskChat() {

        StringRequest request = new StringRequest(Request.Method.POST, fetchParticipantTask,
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

                                    if (object.getString("task_name") != "null") {

                                        if (!checkList.contains(object.getString("task_id"))) {

                                            checkList.add(object.getString("task_id"));

                                        }

                                    }

                                    if (i == jsonArray.length() - 1){
                                        getStaffResponsibleTaskChat();
                                    }

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

                params.put("username", Common.getUserName(context));

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void getStaffResponsibleTaskChat() {

        StringRequest request = new StringRequest(Request.Method.POST, fetchResponsible,
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

                                    if (object.getString("task_name") != "null") {

                                        if (!checkList.contains(object.getString("task_id"))) {

                                            checkList.add(object.getString("task_id"));

                                        }
                                    }

                                    if (i == jsonArray.length() -1){
                                        fetchHistory();
                                    }

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

                params.put("username", Common.getUserName(context));

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void getResponsibleTaskChat() {

        StringRequest request = new StringRequest(Request.Method.POST, fetchResponsible,
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

                                    if (object.getString("task_name") != "null") {

                                        if (!checkList.contains(object.getString("task_id"))) {

                                            checkList.add(object.getString("task_id"));

                                        }
                                    }

                                    if (i == jsonArray.length() -1){
                                        fetchHistory();
                                    }

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

                params.put("username", Common.getUserName(context));

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void getObserversTaskChat() {

        StringRequest request = new StringRequest(Request.Method.POST, fetchObserversTask,
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

                                    if (object.getString("task_name") != "null") {

                                        if (!checkList.contains(object.getString("task_id"))) {

                                            checkList.add(object.getString("task_id"));

                                        }
                                    }

                                    if (i == jsonArray.length() -1){
                                        fetchHistory();
                                    }

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

                params.put("username", Common.getUserName(context));

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void getPrincipleTaskChat() {

        StringRequest request = new StringRequest(Request.Method.POST, fetchPrincipleTasks,
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

                                    if (!checkList.contains(object.getString("task_id"))) {

                                        checkList.add(object.getString("task_id"));

                                    }

                                    if (i == jsonArray.length() -1){
                                        fetchHistory();
                                    }

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

                params.put("username", Common.getUserName(context));

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void getHODPrincipleTaskChat() {

        StringRequest request = new StringRequest(Request.Method.POST, fetchPrincipleTasks,
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

                                    if (!checkList.contains(object.getString("task_id"))) {

                                        checkList.add(object.getString("task_id"));

                                    }

                                    if (i == jsonArray.length() -1){
                                        getResponsibleTaskChat();
                                    }

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

                params.put("username", Common.getUserName(context));

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

}