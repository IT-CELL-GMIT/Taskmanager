package com.beast.collegemanagement.tabfragment;

import static android.app.Activity.RESULT_OK;
import static com.beast.collegemanagement.Common.getBytes;
import static com.beast.collegemanagement.Common.getUserName;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.beast.collegemanagement.adapters.ChatListAdapter;
import com.beast.collegemanagement.adapters.ObsShowAdapter;
import com.beast.collegemanagement.adapters.ObserverTaskAdapter;
import com.beast.collegemanagement.adapters.TaskListAdapter;
import com.beast.collegemanagement.databinding.FragmentAddTaskBinding;
import com.beast.collegemanagement.models.ChatListModel;
import com.beast.collegemanagement.models.ObsShowModel;
import com.beast.collegemanagement.models.ObserverTaskModel;
import com.beast.collegemanagement.models.TaskListModel;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTaskFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private FragmentAddTaskBinding binding;
    private List<TaskListModel> list;
    private TaskListAdapter adapter;

    Context context;

    List<ChatListModel> btsList;
    ChatListAdapter btsAdapter;

    String fechFrindList = Common.getBaseUrl() + "fetchFriendList.php";
    String fetchUserdata = Common.getBaseUrl() + "fetchuserdata.php";
    String createTaskApi = Common.getBaseUrl() + "newtask.php";
    String fetchTaskApi = Common.getBaseUrl() + "fetchTasks.php";
    String updateObserversApi = Common.getBaseUrl() + "AddObserversToTask.php";
    String fetchObsTasksApi = Common.getBaseUrl() + "fetchObsTasks.php";
    String fetchTaskByIdApi = Common.getBaseUrl() + "fetchTaskByTaskId.php";
    String addTaskWithProjectNameApi = Common.getBaseUrl() + "AddTaskWithProject.php";
    String fetchTaskByProjectName = Common.getBaseUrl() + "FetchTaskByProject.php";
    
    public static BottomSheetDialog responsibleBSD;

    String bsdResponsibleUserName;

    CircleImageView responsibleImageView;
    TextView responsibleName;

    List<ObserverTaskModel> obsList;
    ObserverTaskAdapter obsAdapter;
    List<ObserverTaskModel> obsNameList;

    List<ObsShowModel> obsShowList;
    ObsShowAdapter obsShowAdapter;

    private DatePickerDialog dpd;
    private TimePickerDialog tpd;

    String selectedDate, selectedTime, selectedDateTime;

    String fileString = "", fileExtentsion = "", taskName, taskId, respoName;

    TextView deadLine;

    ProgressDialog progressDialog;

    String updateObsname;

    List<String> commonCheck;
    List<String> commonTaskIdCheck;

    static String activityName;

    String title,
            date, time,
            leaderProfileImage,
            leaderUsername,
            projectStatus,
            closeDate,
            projectId;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddTaskFragment(String ActivityName) {
        // Required empty public constructor
        activityName = ActivityName;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddTaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddTaskFragment newInstance(String param1, String param2) {
        AddTaskFragment fragment = new AddTaskFragment(activityName);
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
        binding = FragmentAddTaskBinding.inflate(inflater, container, false);
        binding.ll.setBackgroundColor(getResources().getColor(R.color.icons));

        context = binding.getRoot().getContext();
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);

        obsNameList = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        adapter = new TaskListAdapter(list, binding.getRoot().getContext());
        binding.recyclerView.setAdapter(adapter);

//        Intent intent = getActivity().getIntent();
//        String from = intent.getStringExtra("from");
//
//        if (from != null && from.equalsIgnoreCase("ProjectListAdapter")){
//            getProjectTask();
//        }else {
//            getTask();
//        }

        if (activityName.equalsIgnoreCase("TasksActivity")) {

            if (Common.getPosition(context).equalsIgnoreCase("observer")) {

                getObsProjects();

            } else {
                getProjectTask();
            }


            binding.addTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAddTaskBottomsheet();
                }
            });

        }else if (activityName.contains("ProjectTasksActivity")){

            String [] strSplits = activityName.split("xxx");

            title = strSplits[1];
            date = strSplits[2];
            title = strSplits[3];
            leaderProfileImage = strSplits[4];
            leaderUsername = strSplits[5];
            projectStatus = strSplits[6];
            closeDate = strSplits[7];
            projectId = strSplits[8];

            getTasksByProjectId();

            binding.addTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAddTaskBottomsheet();
                }
            });

        }

        return binding.getRoot();
    }

    private void getTasksByProjectId() {

        list.clear();

        StringRequest request = new StringRequest(Request.Method.POST, fetchTaskByProjectName,
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

                                    String[] splits = object.getString("deadline").split("xxx");

                                    if (object.getString("project_name").length() > 1){
                                        list.add(new TaskListModel(object.getString("task_id"),
                                                object.getString("task_name"),
                                                object.getString("leader_name"),
                                                object.getString("leader_profile"),
                                                object.getString("responsible_profile"),
                                                splits[1],
                                                splits[0],
                                                object.getString("status"),
                                                object.getString("complete_date"),
                                                object.getString("responsible_name"),
                                                object.getString("project_name")));

                                        adapter.notifyDataSetChanged();
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

                params.put("project_name", projectId);

                return params;
            }
        };


        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void getObsProjects() {

        commonTaskIdCheck = new ArrayList<>();

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, fetchObsTasksApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String success = jsonObject.getString("success");

                            if (success.equalsIgnoreCase("1")){

                                for (int i=0; i<jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String taskIdd = object.getString("task_id");

                                    if (!commonTaskIdCheck.contains(taskIdd)){

                                        commonTaskIdCheck.add(taskIdd);
                                        getProjectById(taskIdd);

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
                Toast.makeText(context, "connetion error", Toast.LENGTH_SHORT).show();
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

    private void getProjectById(String taskIdd) {

        progressDialog.setMessage("Little Moments...");
        progressDialog.dismiss();

        StringRequest request = new StringRequest(Request.Method.POST, fetchTaskByIdApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String success = jsonObject.getString("success");

                            if (success.equalsIgnoreCase("1")){

                                for (int i=0; i<jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String[] splits = object.getString("deadline").split("xxx");

                                    if (object.getString("project_name").length() < 2){

                                        list.add(new TaskListModel(object.getString("task_id"),
                                                object.getString("task_name"),
                                                object.getString("leader_name"),
                                                object.getString("leader_profile"),
                                                object.getString("responsible_profile"),
                                                splits[1],
                                                splits[0],
                                                object.getString("status"),
                                                object.getString("complete_date"),
                                                object.getString("responsible_name"),
                                                object.getString("project_name")));

                                        adapter.notifyDataSetChanged();

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

                params.put("task_id", taskIdd);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }


    private void showAddTaskBottomsheet() {

        final boolean[] obs = {false};

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bottomsheet_create_task, null, false);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setDismissWithAnimation(true);

        EditText edTaskName = bottomSheetDialog.findViewById(R.id.edTaskName);
        TextView btnCreateTask = bottomSheetDialog.findViewById(R.id.createTask);
        responsibleImageView = bottomSheetDialog.findViewById(R.id.responsibleProfilePic);
        responsibleName = bottomSheetDialog.findViewById(R.id.responsibleName);
        ImageView showObsBtn =bottomSheetDialog.findViewById(R.id.showObserversImg);
        LinearLayout obsLayout = bottomSheetDialog.findViewById(R.id.obsRecyclerView);
        RecyclerView obsRecyclerView = bottomSheetDialog.findViewById(R.id.observersRecyclerView);
        LinearLayout deadLineBtn = bottomSheetDialog.findViewById(R.id.deadLineLL);
        deadLine = bottomSheetDialog.findViewById(R.id.deadLine);
        CircleImageView selectTaskFileBtn = bottomSheetDialog.findViewById(R.id.selectTaskFile);

        obsShowList = new ArrayList<>();
        RecyclerView obsShowRecyclerView = bottomSheetDialog.findViewById(R.id.observerRecyclerView);
        LinearLayoutManager obsShowLLM = new LinearLayoutManager(context);
        obsShowLLM.setOrientation(RecyclerView.HORIZONTAL);
        obsShowRecyclerView.setLayoutManager(obsShowLLM);
        obsShowAdapter = new ObsShowAdapter(context, obsShowList);
        obsShowRecyclerView.setAdapter(obsShowAdapter);

        edTaskName.setBackgroundColor(context.getResources().getColor(R.color.icons));

        selectTaskFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 1);

            }
        });

        btnCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskName = edTaskName.getText().toString().trim();

                if (taskName.length() < 3){

                    bottomSheetDialog.dismiss();
                    Toast.makeText(context, "invalid task name{minimum 3 chars required}", Toast.LENGTH_SHORT).show();

                }else if(responsibleName.getText().toString().trim().equalsIgnoreCase("Responsible Name")){
                    Toast.makeText(context, "responsible required", Toast.LENGTH_SHORT).show();
                }else if (deadLine.getText().toString().trim().equalsIgnoreCase("select deadline")){
                    Toast.makeText(context, "deadline of task is required", Toast.LENGTH_SHORT).show();
                }
                else {

                    taskName = edTaskName.getText().toString().trim();
                    taskId = Common.getUserName(context).trim() + "_" + taskName + String.valueOf(System.currentTimeMillis());
                    respoName = Common.getSharedPrf("RESPONSIBLEBSD", context);

                    bottomSheetDialog.dismiss();

                    AddNewTask();

                }

            }
        });

        LinearLayout selectResponsibleBtn = bottomSheetDialog.findViewById(R.id.selectResponsibleLL);

        selectResponsibleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResponsibles();
            }
        });

        showObsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (responsibleName.getText().toString().equalsIgnoreCase("Responsible Name")){
                    Toast.makeText(context, "select responsible person first", Toast.LENGTH_SHORT).show();
                }else {
                    obs[0] = !obs[0];

                    if (obs[0]){

                        obsRecyclerView.removeAllViews();
                        Common.commonPickList = new ArrayList<>();
                        obsLayout.setVisibility(View.VISIBLE);
                        showObsBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.arrow_up));
                        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
                        obsRecyclerView.setLayoutManager(layoutManager);
                        obsAdapter = new ObserverTaskAdapter(context, obsNameList);
                        obsRecyclerView.setAdapter(obsAdapter);

                        obsAdapter.notifyDataSetChanged();
                        obsShowList.clear();
                        obsShowAdapter.notifyDataSetChanged();

                    }else {

                        obsLayout.setVisibility(View.GONE);
                        showObsBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_keyboard_double_arrow_down_24));

                        for (int i=0; i<Common.getCommonPickList().size(); i++){

                            obsShowList.add(new ObsShowModel(Common.getCommonPickList().get(i)));
                            obsShowAdapter.notifyDataSetChanged();

                        }

                    }
                }

            }
        });

        deadLineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar now = Calendar.getInstance();

                if (dpd == null) {
                    dpd = DatePickerDialog.newInstance(
                            AddTaskFragment.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                } else {
                    dpd.initialize(
                            AddTaskFragment.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                }
                dpd.setThemeDark(true);
                dpd.setCancelable(false);

                dpd.setOnCancelListener(dialog -> {
                    Log.d("DatePickerDialog", "Dialog was cancelled");
                    dpd = null;
                });
                dpd.show(requireFragmentManager(), "Datepickerdialog");

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 &&
        resultCode == RESULT_OK &&
        data.getData() != null){

            Uri uri = data.getData();
            fileString = Common.ConvertToString(uri, context);
            fileExtentsion = Common.getExtension(context, uri);

        }else {
            Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
        }

    }

    private void AddNewTask() {

        progressDialog.setMessage("please wait...");
        progressDialog.show();

        if (activityName.equalsIgnoreCase("TasksActivity")){
            list.add(new TaskListModel(taskId,
                    taskName,
                    Common.getUserName(context),
                    Common.getProfilePic(context),
                    "",
                    selectedTime,
                    selectedDate,
                    "RUNNING",
                    "",
                    respoName,
                    ""));
        }else if (activityName.contains("ProjectTasksActivity")){

            list.add(new TaskListModel(taskId,
                    taskName,
                    Common.getUserName(context),
                    Common.getProfilePic(context),
                    "",
                    selectedTime,
                    selectedDate,
                    "RUNNING",
                    "",
                    respoName,
                    projectId));

        }

        adapter.notifyDataSetChanged();


        if (activityName.equalsIgnoreCase("TasksActivity")){
            uploadTaskWithoutProject();
        }else if (activityName.contains("ProjectTasksActivity")){
            uploadTaskWithProject();
        }



    }

    private void uploadTaskWithProject() {

        StringRequest request = new StringRequest(Request.Method.POST, addTaskWithProjectNameApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase("Data Inserted") || response.contains("Data Inserted")){
                            progressDialog.dismiss();

                            Toast.makeText(context, "task created", Toast.LENGTH_SHORT).show();

                            updateObsname = "";

                            for (int i=0; i<Common.getCommonList().size(); i++){

                                updateObsname = updateObsname + Common.getCommonList().get(i).trim() + "xxx";

                            }

                            updateObservers();

                        }else {
                            Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "connection error", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("task_name", taskName);
                params.put("task_id", taskId);
                params.put("creation_date", Common.getDateTime());
                params.put("deadline", selectedDateTime);
                params.put("task_file", fileString);
                params.put("file_ext", fileExtentsion);
                params.put("leader_name", Common.getUserName(context));
                params.put("leader_profile", Common.getProfilePic(context));
                params.put("responsible_name", respoName);
                params.put("project_name", projectId);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void uploadTaskWithoutProject() {

        StringRequest request = new StringRequest(Request.Method.POST, createTaskApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase("Data Inserted") || response.contains("Data Inserted")){
                            progressDialog.dismiss();

                            Toast.makeText(context, "task created", Toast.LENGTH_SHORT).show();

                            updateObsname = "";

                            for (int i=0; i<Common.getCommonList().size(); i++){

                                updateObsname = updateObsname + Common.getCommonList().get(i).trim() + "xxx";

                            }

                            updateObservers();

                        }else {
                            Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "connection error", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("task_name", taskName);
                params.put("task_id", taskId);
                params.put("creation_date", Common.getDateTime());
                params.put("deadline", selectedDateTime);
                params.put("task_file", fileString);
                params.put("file_ext", fileExtentsion);
                params.put("leader_name", Common.getUserName(context));
                params.put("leader_profile", Common.getProfilePic(context));
                params.put("responsible_name", respoName);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void updateObservers() {

        progressDialog.setMessage("Little Moment...");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, updateObserversApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        if (response.contains("failed") || response.equalsIgnoreCase("failed")){
                            Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
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

                params.put("usernames", updateObsname);
                params.put("task_id", taskId);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void showResponsibles() {

        obsNameList.clear();

        responsibleBSD = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_friends, null, false);
        responsibleBSD.setContentView(view);
        responsibleBSD.show();

        RecyclerView recyclerView = view.findViewById(R.id.btsRecyclerview);
        btsList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        btsAdapter = new ChatListAdapter(btsList, context, "ADDTASKFRAGMENT");
        recyclerView.setAdapter(btsAdapter);

        responsibleBSD.setDismissWithAnimation(true);

        getFriendsBTS();

        responsibleBSD.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

                bsdResponsibleUserName = Common.getSharedPrf("RESPONSIBLEBSD", context);
                Glide.with(context).load(Common.getSharedPrf("RESPONSIBLEBSDPROFILE", context)).into(responsibleImageView);
                responsibleName.setText(Common.getSharedPrf("RESPONSIBLEBSDNAME", context));

                Toast.makeText(context, bsdResponsibleUserName, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void getFriendsBTS() {

        StringRequest request = new StringRequest(Request.Method.POST, fechFrindList,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            commonCheck = new ArrayList<>();

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

                                if (object.getString("position").equalsIgnoreCase("observer")){

                                    if (!commonCheck.contains(object.getString("username"))) {

                                        commonCheck.add(object.getString("username"));

                                        obsNameList.add(new ObserverTaskModel(object.getString("username"),
                                                object.getString("fullname"),
                                                object.getString("profilepic")));

                                    }

                                }
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

    private void getProjectTask() {

        list.clear();

        StringRequest request = new StringRequest(Request.Method.POST, fetchTaskApi,
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

                                    if (object.getString("project_name").length() < 2){

                                        String[] splits = object.getString("deadline").split("xxx");

                                        list.add(new TaskListModel(object.getString("task_id"),
                                                object.getString("task_name"),
                                                object.getString("leader_name"),
                                                object.getString("leader_profile"),
                                                object.getString("responsible_profile"),
                                                splits[1],
                                                splits[0],
                                                object.getString("status"),
                                                object.getString("complete_date"),
                                                object.getString("responsible_name"),
                                                object.getString("project_name")));

                                        adapter.notifyDataSetChanged();

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

//        list.add(new TaskListModel("1","Legit",
//                "Sanjay Parmar",
//                Common.getProfilePic(getContext()),
//                "",
//                "14:40",
//                "28-13_2022",
//                "COMPLETED",
//                "01-11_2022"));
//
//        adapter.notifyDataSetChanged();

//        list.add(new TaskListModel("2","Legit",
//                "Iron Man",
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
//                "14:40",
//                "28-12_2022",
//                "PENDING",
//                ""));
//
//        list.add(new TaskListModel("3","Legit",
//                "Thor",
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
//                "14:40",
//                "01-11_2022",
//                "PENDING",
//                ""));
//
//        adapter.notifyDataSetChanged();
//
//    }
//
//    private void getTask() {
//
//        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
//        String currentDate = new SimpleDateFormat("dd-MM_yyyy", Locale.getDefault()).format(new Date());
//
//        list.add(new TaskListModel("4","Legit",
//                "Sanjay Parmar",
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
//                "14:40",
//                "28-13_2022",
//                "COMPLETED",
//                "01-11_2022"));
//
//        adapter.notifyDataSetChanged();
//
//        list.add(new TaskListModel("4","Legit",
//                "Tony Stark",
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
//                "14:40",
//                "28-13_2022",
//                "COMPLETED",
//                "01-12_2022"));
//
//        adapter.notifyDataSetChanged();
//
//        list.add(new TaskListModel("5","Legit",
//                "Iron Man",
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
//                "14:40",
//                "28-12_2022",
//                "PENDING",
//                ""));
//
//        adapter.notifyDataSetChanged();
//
//        list.add(new TaskListModel("6","Legit",
//                "Thor",
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
//                "14:40",
//                "03-12_2022",
//                "PENDING",
//                ""));
//
//        adapter.notifyDataSetChanged();
//
//        list.add(new TaskListModel("7", "Legit",
//                "Thor",
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
//                "14:40",
//                "02-12_2022",
//                "PENDING",
//                ""));
//
//        adapter.notifyDataSetChanged();
//
//        list.add(new TaskListModel("8", "Legit",
//                "Thor",
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
//                "14:40",
//                "01-12_2022",
//                "PENDING",
//                ""));
//
//        adapter.notifyDataSetChanged();
//
//        list.add(new TaskListModel("9","Legit",
//                "Thor",
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
//                "14:40",
//                "01-11_2022",
//                "PENDING",
//                ""));
//
//        adapter.notifyDataSetChanged();

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String y = String.valueOf(year);
        String month = String.valueOf(monthOfYear);
        String day = String.valueOf(dayOfMonth);

        selectedDate = day + "-" + month + "_" + y;

        Calendar now = Calendar.getInstance();

        if (tpd == null) {
            tpd = TimePickerDialog.newInstance(
                    AddTaskFragment.this,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    true
            );
        } else {
            tpd.initialize(
                    AddTaskFragment.this,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    now.get(Calendar.SECOND),
                    true
            );
        }
        tpd.setThemeDark(true);
        tpd.vibrate(true);
        tpd.setCancelable(false);
        tpd.setOnCancelListener(dialogInterface -> {
            Log.d("TimePicker", "Dialog was cancelled");
            tpd = null;
        });
        tpd.show(requireFragmentManager(), "Timepickerdialog");

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        String hour = String.valueOf(hourOfDay);
        String min = String.valueOf(minute);
        String sec = String.valueOf(second);

        selectedTime = hour + ":" + min;

        selectedDateTime = selectedDate + "xxx" + selectedTime;

        deadLine.setText(selectedDate + "  " + selectedTime);

    }
}