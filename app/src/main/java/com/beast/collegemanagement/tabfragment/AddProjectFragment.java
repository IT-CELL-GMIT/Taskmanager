package com.beast.collegemanagement.tabfragment;

import static com.beast.collegemanagement.Common.getProfilePic;
import static com.beast.collegemanagement.Common.getUserName;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
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
import com.beast.collegemanagement.adapters.ProjectListAdapter;
import com.beast.collegemanagement.adapters.ProjectMemberAdapter;
import com.beast.collegemanagement.databinding.FragmentAddProjectBinding;
import com.beast.collegemanagement.models.ProjectListModel;
import com.beast.collegemanagement.models.ProjectMemberModel;
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
 * Use the {@link AddProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProjectFragment extends Fragment {

    private FragmentAddProjectBinding binding;
    List<ProjectListModel> list;
    ProjectListAdapter adapter;

    Context context;

    String fechFrindList = Common.getBaseUrl() + "fetchFriendList.php";
    String fetchUserdata = Common.getBaseUrl() + "fetchuserdata.php";
    String createProjectApi = Common.getBaseUrl() + "AddProject.php";
    String updateProjectMembersApi = Common.getBaseUrl() + "AddMemberToProject.php";
    String fetchProjectPrinciple = Common.getBaseUrl() + "fetchProjectByLeader.php";
    String fetchProjectIdApi = Common.getBaseUrl() + "fetchProjectIdByUsename.php";
    String fetchProjectById = Common.getBaseUrl() + "fetchProjectById.php";

    List<ProjectMemberModel> projectList;
    ProjectMemberAdapter projectAdapter;

    ProgressDialog progressDialog;

    String projectId, projectName;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddProjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddProjectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddProjectFragment newInstance(String param1, String param2) {
        AddProjectFragment fragment = new AddProjectFragment();
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
        binding = FragmentAddProjectBinding.inflate(inflater, container, false);
        binding.ll.setBackgroundColor(getResources().getColor(R.color.icons));

        context = binding.getRoot().getContext();

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);

        list = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProjectListAdapter(list, binding.getRoot().getContext(), "AddProjectFragment");
        binding.recyclerView.setAdapter(adapter);

//        getProject();

        if (Common.getPosition(context).equalsIgnoreCase("principle")){
            principleProjects(Common.getUserName(context));
        }else {
            getProjects();
        }

        binding.addProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.getPosition(context).equalsIgnoreCase("principle")){
                    showBottomsheet();
                }else {
                    Toast.makeText(context, "only principle can create project", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return binding.getRoot();
    }


    private void principleProjects(String userName) {

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, fetchProjectPrinciple,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String success = jsonObject.getString("success");

                            if (success.equalsIgnoreCase("1")){

                                for (int i=0; i< jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    list.add(new ProjectListModel(object.getString("project_id"),
                                            object.getString("project_title"),
                                            object.getString("opening_date"),
                                            object.getString("leader_profile"),
                                            object.getString("leader_name"),
                                            object.getString("status"),
                                            object.getString("completiton_date")));

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
                progressDialog.dismiss();
                Toast.makeText(context, "connecton error", Toast.LENGTH_SHORT).show();
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

    private void getProjects() {

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        List<String> commonCheck = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.POST, fetchProjectIdApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String success = jsonObject.getString("success");

                            if (success.equalsIgnoreCase("1")){

                                for (int i=0; i< jsonObject.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    if (!commonCheck.contains(object.getString("project_name"))){

                                        commonCheck.add(object.getString("project_name"));
                                        fetchProject(object.getString("project_name"));

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
                progressDialog.dismiss();
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

    private void fetchProject(String project_name) {

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, fetchProjectById,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String success = jsonObject.getString("success");

                            if (success.equalsIgnoreCase("1")){

                                for (int i=0; i< jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    list.add(new ProjectListModel(object.getString("project_id"),
                                            object.getString("project_title"),
                                            object.getString("opening_date"),
                                            object.getString("leader_profile"),
                                            object.getString("leader_name"),
                                            object.getString("status"),
                                            object.getString("completiton_date")));

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
                progressDialog.dismiss();
                Toast.makeText(context, "connecton error", Toast.LENGTH_SHORT).show();
            }
        }){

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("project_id", project_name);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }


    private void showBottomsheet() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bottomsheet_create_project, null, false);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        EditText edProjectName = bottomSheetDialog.findViewById(R.id.edProjectName);
        TextView btnCreateProject = bottomSheetDialog.findViewById(R.id.createProject);


        projectList = new ArrayList<>();
        RecyclerView projectRecyclerView = bottomSheetDialog.findViewById(R.id.memberRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        projectRecyclerView.setLayoutManager(layoutManager);
        projectAdapter = new ProjectMemberAdapter(projectList, context);
        projectRecyclerView.setAdapter(projectAdapter);

        getFriends();

        btnCreateProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                projectName = edProjectName.getText().toString().trim();

                if (projectName.length() < 3){

                    Toast.makeText(context, "invalid name", Toast.LENGTH_SHORT).show();

                }else {

                    list.add(new ProjectListModel("Task_manager123",projectName,
                            Common.getDate() +"xxx"+Common.getTIme(),
                            getProfilePic(context),
                            getUserName(context),
                            "RUNNING",
                            ""));

                    adapter.notifyDataSetChanged();
                    bottomSheetDialog.dismiss();

                    createProject();

                }

            }
        });

    }

    private void createProject() {

        projectId = Common.getUserName(context) + "_" + "project_" + projectName + String.valueOf(System.currentTimeMillis());

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, createProjectApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        
                        updateProjectMembers();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(context, "connection error", Toast.LENGTH_SHORT).show();
            }
        }){

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("project_id", projectId);
                params.put("project_title", projectName);
                params.put("opening_date", Common.getDateTime());
                params.put("leader_name", Common.getUserName(context));
                params.put("leader_profile", Common.getProfilePic(context));
                params.put("status", "RUNNING");
                params.put("completiton_date", "");

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void updateProjectMembers() {

        progressDialog.setMessage("Little Moments...");
        progressDialog.show();


        String memberList = "";

        for (int i=0; i<Common.commonList.size(); i++){

            memberList = memberList + Common.commonList.get(i).trim() + "xxx";

        }

        String finalMemberList = memberList;

        StringRequest request = new StringRequest(Request.Method.POST, updateProjectMembersApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        if (response.contains("failed") || response.equalsIgnoreCase("failed")){
                            Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "done", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "connection error", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("usernames", finalMemberList);
                params.put("project_id", projectId);

                return  params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

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

                params.put("username", Common.getUserName(context));

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void getUserInfo(String friendname) {

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

//                                list.add(new StaffModel("unknown",
//                                        object.getString("username"),
//                                        object.getString("fullname"),
//                                        object.getString("profilepic"),
//                                        object.getString("phonenumber"),
//                                        object.getString("email"),
//                                        "unknwon",
//                                        object.getString("position")));

                                if (!object.getString("position").equalsIgnoreCase("principle")) {

                                    projectList.add(new ProjectMemberModel(object.getString("username"),
                                            object.getString("profilepic"),
                                            object.getString("position")));

                                    projectAdapter.notifyDataSetChanged();
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

    private void getProject() {

        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        String currentDate = new SimpleDateFormat("dd-MM_yyyy", Locale.getDefault()).format(new Date());

        list.add(new ProjectListModel("Task_manager12","Task Manager",
                currentDate+"xxx"+currentTime,
                getProfilePic(context),
                "Sanjay Parmar",
                "RUNNING",
                ""));

        list.add(new ProjectListModel("backend123","Task Manager - Backend",
                currentDate+"xxx"+currentTime,
                getProfilePic(context),
                "Legend SP",
                "COMPLETED",
                currentDate + "xxx" + currentTime));

        adapter.notifyDataSetChanged();

    }
}