package com.beast.collegemanagement.tabfragment;

import static android.app.Activity.RESULT_OK;
import static com.beast.collegemanagement.Common.ConvertToString;
import static com.beast.collegemanagement.Common.getDateTime;
import static com.beast.collegemanagement.Common.getUserName;

import android.app.ProgressDialog;
import android.content.ContentProvider;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.beast.collegemanagement.adapters.ChatListAdapter;
import com.beast.collegemanagement.adapters.ObsShowAdapter;
import com.beast.collegemanagement.adapters.ObserverTaskAdapter;
import com.beast.collegemanagement.adapters.ParticipantsAdapter;
import com.beast.collegemanagement.adapters.ProjectListAdapter;
import com.beast.collegemanagement.adapters.SubTaskAdapter;
import com.beast.collegemanagement.databinding.FragmentProjectBinding;
import com.beast.collegemanagement.models.AddStaffModel;
import com.beast.collegemanagement.models.ChatListModel;
import com.beast.collegemanagement.models.ObsShowModel;
import com.beast.collegemanagement.models.ObserverTaskModel;
import com.beast.collegemanagement.models.ParticipantsModel;
import com.beast.collegemanagement.models.ProjectListModel;
import com.beast.collegemanagement.models.SubTaskModel;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.SNIHostName;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private FragmentProjectBinding binding;

    Context context;

    ProgressDialog progressDialog;

    String uniqueID, title,
            leaderName,
            leaderProfileImg,
            coLeaderName,
            coLeaderProfileImg,
            time,
            date,
            status,
            completeDate, timeLeft;

    List<ChatListModel> btsList;
    ChatListAdapter btsAdapter;
    List<String> btnCheckList;

    String fechFrindList = Common.getBaseUrl() + "fetchFriendList.php";
    String fetchUserdata = Common.getBaseUrl() + "fetchuserdata.php";
    String addDescriptionApi = Common.getBaseUrl() + "addDescription.php";
    String fetchDescriptionApi = Common.getBaseUrl() + "fetchDescription.php";
    String fetchTaskObserversApi = Common.getBaseUrl() + "fetchTaskObservers.php";
    String updateObserversApi = Common.getBaseUrl() + "updateObservers.php";
    String addParticipantsApi = Common.getBaseUrl() + "AddParticipants.php";
    String fetchParticipantApi = Common.getBaseUrl() + "fetchParticipants.php";
    String updateResponsibleApi = Common.getBaseUrl() + "updateResponsible.php";
    String fetchStatusApi = Common.getBaseUrl() + "fetchStatus.php";
    String updateStatusApi = Common.getBaseUrl() + "updateStatus.php";
    String updateDeadlineApi = Common.getBaseUrl() + "updateDeadline.php";
    String fetchProjectPrinciple = Common.getBaseUrl() + "fetchProjectByLeader.php";
    String updateProjectApi = Common.getBaseUrl() + "updateProject.php";
    String fetchProjectApi = Common.getBaseUrl() + "fetchTaskProject.php";
    String fetchProjectInfoApi = Common.getBaseUrl() + "fetchProjectById.php";
    String updloadFile = Common.getBaseUrl() + "addFileToTask.php";
    String insertSubtaskApi = Common.getBaseUrl() + "InsertSubtask.php";
    String fetchSubtasksApi = Common.getBaseUrl() + "fetchSubtasks.php";
    String addTaskHistoryApi = Common.getBaseUrl() + "AddTaskHistory.php";


    public static TextView respoNameTv;
    public static CircleImageView respoProfile;
    public static BottomSheetDialog bottomSheetDialog;


    List<ObsShowModel> obsShowList;
    ObsShowAdapter obsShowAdapter;
    List<AddStaffModel> savedObs;


    List<ObserverTaskModel> friendObserversList;
    ObserverTaskAdapter friendObserverAdapter;
    List<String> obsCheck;


    List<ParticipantsModel> participantList;
    ParticipantsAdapter participantsAdapter;
    List<String> participantCheck;


    List<ObsShowModel> participantShowList;
    ObsShowAdapter participantShowAdapter;
    List<String> participantShowCheck;

    Boolean statusBtnIsChange = false;



    String selectedDate, selectedTime, selectedDateTime;
    Boolean isDateTimeChanged = false;



    private DatePickerDialog dpd;
    private TimePickerDialog tpd;
    
    
    
    List<ProjectListModel> projectList;
    ProjectListAdapter projectAdater;
    List<String> projectListCheck;


    List<SubTaskModel> subTaskList;
    SubTaskAdapter subTaskAdapter;
    List<String> subTaskCheck;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectFragment newInstance(String param1, String param2) {
        ProjectFragment fragment = new ProjectFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project, container, false);
        binding.ll.setBackgroundColor(getContext().getResources().getColor(R.color.icons));
        binding.fl.setBackgroundColor(getContext().getResources().getColor(R.color.icons));

        context = binding.getRoot().getContext();
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);

        uniqueID = Common.getSharedPrf("uniqueID", context);
        title = Common.getSharedPrf("title", context);
        leaderName = Common.getSharedPrf("leaderName", context);
        leaderProfileImg = Common.getSharedPrf("leaderProfileImg", context);
        coLeaderName = Common.getSharedPrf("coLeaderName", context);
        coLeaderProfileImg = Common.getSharedPrf("coLeaderProfileImg", context);
        time = Common.getSharedPrf("time", context);
        date = Common.getSharedPrf("date", context);
        status = Common.getSharedPrf("status", context);
        completeDate = Common.getSharedPrf("completeDate", context);
        timeLeft = Common.getSharedPrf("timeLeft", context);

        respoNameTv = binding.getRoot().findViewById(R.id.responsibleName);
        respoProfile = binding.getRoot().findViewById(R.id.responsibleProfilePic);

        binding.edProjectName.setText(title);
        binding.createrName.setText(leaderName);
        Glide.with(context).load(leaderProfileImg).into(binding.createrProfilePic);
        Glide.with(context).load(coLeaderProfileImg).into(binding.responsibleProfilePic);
        binding.responsibleName.setText(coLeaderName);
        binding.tvStatus.setText(status);
        binding.deadLine.setText(date + " " + time);

        obsShowList = new ArrayList<>();
        savedObs = new ArrayList<>();
        RecyclerView obsShowRecyclerView = binding.getRoot().findViewById(R.id.observerRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        obsShowRecyclerView.setLayoutManager(layoutManager);
        obsShowAdapter = new ObsShowAdapter(context, obsShowList);
        obsShowRecyclerView.setAdapter(obsShowAdapter);

        showTaskObservers();


        binding.editObsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.obsRecyclerViewLL.getVisibility() == View.VISIBLE){

                    binding.obsRecyclerViewLL.setVisibility(View.GONE);

                    updateObservers(Common.getCommonList());


                }else {

                    binding.obsRecyclerViewLL.setVisibility(View.VISIBLE);
                    showFriendObservers();
                    Common.commonList.clear();

                }

            }
        });



        binding.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.startBtn.setVisibility(View.GONE);
                binding.completeBtn.setVisibility(View.VISIBLE);
                binding.tvStatus.setText("COMPLETED");
//                binding.tvStatus.setText("COMPLETED");

            }
        });

        binding.completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.completeBtn.setVisibility(View.GONE);
                binding.startBtn.setVisibility(View.VISIBLE);
                binding.tvStatus.setText("RUNNING");
//                binding.tvStatus.setText("RUNNING");

            }
        });

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveDescription();
                saveStatus();
                saveDeadline();

                addTaskHistory(uniqueID, "Changed settings of " + title);

            }
        });

        binding.addIntoProjectLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.responsibleLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRespoBottomSheet();
            }
        });


        binding.editParticipantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showParticipantsBottomSheet();
            }
        });

        getDescription();






        participantShowList = new ArrayList<>();
        participantShowCheck = new ArrayList<>();
        RecyclerView participantShowRecyclerView = binding.getRoot().findViewById(R.id.participantsRecyclerView);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(context);
        layoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        participantShowRecyclerView.setLayoutManager(layoutManager1);
        participantShowAdapter = new ObsShowAdapter(context, participantShowList);
        participantShowRecyclerView.setAdapter(participantShowAdapter);

        getParticipants();





        getStatus();



        binding.editDateTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Calendar now = Calendar.getInstance();

                if (dpd == null) {
                    dpd = DatePickerDialog.newInstance(
                            ProjectFragment.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                } else {
                    dpd.initialize(
                            ProjectFragment.this,
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



        binding.addIntoProjectLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProjectsBottomSheet();
            }
        });

        getTaskProject();




        binding.addFilesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomsheet();
            }
        });


        binding.addSubTasksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGetSubtaskBottomSheet();
            }
        });

        subTaskList = new ArrayList<>();
        subTaskCheck = new ArrayList<>();
        RecyclerView subTaskRecyclerview = binding.getRoot().findViewById(R.id.subTaskRecycler);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(context);
        subTaskRecyclerview.setLayoutManager(layoutManager2);
        subTaskAdapter = new SubTaskAdapter(subTaskList, context, "ProjectFragment");
        subTaskRecyclerview.setAdapter(subTaskAdapter);

        getSubTasks();


        return binding.getRoot();
    }


    private void addTaskHistory(String taskId, String actionText){

        StringRequest request = new StringRequest(Request.Method.POST, addTaskHistoryApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase("Failed") || response.contains("Failed")){
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

                params.put("task_id", taskId);
                params.put("action_text", actionText);
                params.put("time_date", Common.getTimeDate());
                params.put("username", Common.getUserName(context));

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }


    private void showGetSubtaskBottomSheet() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bottomsheet_getsubtask_layout, null, false);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
        bottomSheetDialog.setDismissWithAnimation(true);
        
        CardView doneBtn, cancleBtn;
        EditText edSubTask = bottomSheetDialog.findViewById(R.id.fullName);

        doneBtn = bottomSheetDialog.findViewById(R.id.doneBtn);
        cancleBtn = bottomSheetDialog.findViewById(R.id.cancleBtn);

        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();

                if (edSubTask.getText().toString().length() < 4){
                    bottomSheetDialog.show();
                    Toast.makeText(context, "invalid name", Toast.LENGTH_SHORT).show();
                }else {

                    setSubTask(edSubTask.getText().toString());

                }

            }
        });

    }

    private void setSubTask(String subtaskName) {

        StringRequest request = new StringRequest(Request.Method.POST, insertSubtaskApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("Failed") || response.contains("Failed")){
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        }
                        getSubTasks();

                        addTaskHistory(uniqueID, "Changed sub tasks in " + title);

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

                params.put("subtask_id", getUserName(context) + "_subtask_" + uniqueID + "_" + String.valueOf(System.currentTimeMillis()));
                params.put("task_id", uniqueID);
                params.put("title", subtaskName);
                params.put("start_date", getDateTime());
                params.put("status", "RUNNING");
                params.put("complete_date", "");

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void getSubTasks() {

        StringRequest request = new StringRequest(Request.Method.POST, fetchSubtasksApi,
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

                                    if (!subTaskCheck.contains(object.getString("subtask_id"))){

                                        subTaskCheck.add(object.getString("subtask_id"));

                                        subTaskList.add(new SubTaskModel(object.getString("subtask_id"),
                                                object.getString("task_id"),
                                                object.getString("title"),
                                                object.getString("start_date"),
                                                object.getString("status"),
                                                object.getString("complete_date")));

                                        subTaskAdapter.notifyDataSetChanged();

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

                params.put("task_id", uniqueID);

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

        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        StringRequest request = new StringRequest(Request.Method.POST, updloadFile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        addTaskHistory(Common.getSharedPrf("uniqueID", context), "Uploaded a file in" + title);
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
                params.put("fullname", Common.getFullName(context));
                params.put("profilepic", Common.getProfilePic(context));
                params.put("timedate", Common.getTimeDate());
                params.put("content", s);
                params.put("type", fileType);
                params.put("task_file", imageString);
                params.put("task_id", Common.getSharedPrf("uniqueID", context));
                params.put("file_ext", ext);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void getTaskProject() {

        StringRequest request = new StringRequest(Request.Method.POST, fetchProjectApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String success = jsonObject.getString("success");

                            if (success.equalsIgnoreCase("1")){

                                JSONObject object = jsonArray.getJSONObject(0);

                                getProjectInfo(object.getString("project_name"));

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

                params.put("task_id", uniqueID);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void getProjectInfo(String projectId) {

        StringRequest request = new StringRequest(Request.Method.POST, fetchProjectInfoApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String success = jsonObject.getString("success");

                            if (success.equalsIgnoreCase("1")){

                                JSONObject object = jsonArray.getJSONObject(0);

                                binding.projectName.setText(object.getString("project_title"));
                                binding.addToProjectBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_edit_24));

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

                params.put("project_id", projectId);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void showProjectsBottomSheet() {
        
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bottomsheet_project_list, null, false);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
        bottomSheetDialog.setDismissWithAnimation(true);
        
        projectList = new ArrayList<>();
        projectListCheck = new ArrayList<>();
        RecyclerView projectListRecyclerView = bottomSheetDialog.findViewById(R.id.projectListRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        projectListRecyclerView.setLayoutManager(layoutManager);
        projectAdater = new ProjectListAdapter(projectList, context, "ProjectFragment");
        projectListRecyclerView.setAdapter(projectAdater);

        Common.commonList.clear();

        if (Common.getPosition(context).equalsIgnoreCase("principle")) {
            getProjects();
        }else {
            Toast.makeText(context, "you dont have authority to edit", Toast.LENGTH_SHORT).show();
        }


        Button doneBtn = bottomSheetDialog.findViewById(R.id.doneBtn);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Common.commonList.size() > 1){
                    Toast.makeText(context, "you can select only one project", Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();
                    bottomSheetDialog.show();
                }else if (Common.commonList.size() == 1){

                    setProject(Common.commonList.get(0));
                    bottomSheetDialog.dismiss();

                }else {
                    bottomSheetDialog.dismiss();
                }

            }
        });

        
    }

    private void setProject(String projectId) {

        if (projectId.equalsIgnoreCase("none")){

            updateProject("");

            binding.projectName.setText("Not in a project");
            binding.addToProjectBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_add_circle_outline_24));
            binding.projectFolderImg.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_create_new_folder_24));
            Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();

        }else {

            updateProject(projectId);

        }

    }

    private void updateProject(String projectId) {

        StringRequest request = new StringRequest(Request.Method.POST, updateProjectApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("failed") || response.equalsIgnoreCase("failed")){
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "Project updated", Toast.LENGTH_SHORT).show();
                        }
                        getTaskProject();
                        addTaskHistory(uniqueID, "Changed project settings in " + title);
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

                params.put("task_id", uniqueID);
                params.put("project_Id", projectId);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }


    private void getProjects() {

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

                                    if (!object.getString("status").equalsIgnoreCase("CLOSED")) {

                                        projectList.add(new ProjectListModel(object.getString("project_id"),
                                                object.getString("project_title"),
                                                object.getString("opening_date"),
                                                object.getString("leader_profile"),
                                                object.getString("leader_name"),
                                                object.getString("status"),
                                                object.getString("completiton_date")));

                                        projectAdater.notifyDataSetChanged();

                                    }

                                }

                                projectList.add(new ProjectListModel("none",
                                        "remove",
                                        "",
                                        "",
                                        "none",
                                        "",
                                        ""));

                                projectAdater.notifyDataSetChanged();

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

    private void saveDeadline() {

        if (isDateTimeChanged && selectedDateTime.length()>4 && selectedDateTime.contains("xxx")){

            StringRequest request = new StringRequest(Request.Method.POST, updateDeadlineApi,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("failed") || response.equalsIgnoreCase("failed")){

                                Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                                
                            }
                            addTaskHistory(uniqueID, "Changed dead line to " + selectedDateTime + " in " + uniqueID);
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

                    params.put("task_id", uniqueID);
                    params.put("deadline", selectedDateTime);

                    return params;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(request);

        }

    }

    private void saveStatus() {

        StringRequest request = new StringRequest(Request.Method.POST, updateStatusApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("failed") || response.equalsIgnoreCase("failed")){

                            Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();

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

                params.put("status", binding.tvStatus.getText().toString());
                params.put("task_id", uniqueID);
                params.put("dateTime", Common.getDateTime());

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void getStatus() {


        StringRequest request = new StringRequest(Request.Method.POST, fetchStatusApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String success = jsonObject.getString("success");

                            if (success.equalsIgnoreCase("1")){

                                for (int i=0; i<jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(0);

                                    String status = object.getString("status");
                                    binding.tvStatus.setText(status);

                                    if (status.equalsIgnoreCase("RUNNING")){

                                        binding.startBtn.setVisibility(View.VISIBLE);
                                        binding.completeBtn.setVisibility(View.GONE);

                                    }else {

                                        binding.startBtn.setVisibility(View.GONE);
                                        binding.completeBtn.setVisibility(View.VISIBLE);

                                    }

                                }

                            }

                        } catch (JSONException e) {
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

                params.put("task_id", uniqueID);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);


    }


    private void getParticipants() {

        participantShowList.clear();
        participantShowCheck.clear();

        StringRequest request = new StringRequest(Request.Method.POST, fetchParticipantApi,
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

                                    if (!participantShowCheck.contains(object.getString("username"))){

                                        participantShowCheck.add(object.getString("username"));

                                        participantShowList.add(new ObsShowModel(object.getString("profilePic")));
                                        participantShowAdapter.notifyDataSetChanged();

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

                params.put("project_id",uniqueID);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void showParticipantsBottomSheet() {

        Common.commonList.clear();

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);

        View view = LayoutInflater.from(context).inflate(R.layout.bottomsheet_participants, null, false);

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
        bottomSheetDialog.setDismissWithAnimation(true);
        
        RecyclerView participantRecyclerView = bottomSheetDialog.findViewById(R.id.participantsRecyclerView);
        participantList = new ArrayList<>();
        participantCheck = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        participantRecyclerView.setLayoutManager(layoutManager);
        participantsAdapter = new ParticipantsAdapter(participantList,"bottomsheetParticipant", context);
        participantRecyclerView.setAdapter(participantsAdapter);

        showParticipantFriends();

        Button doneBtn = bottomSheetDialog.findViewById(R.id.doneBtn);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Common.commonList.size() == 0){
                    Toast.makeText(context, "no participant selected", Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();
                }else {

                    addParticipants(Common.commonList);
                    bottomSheetDialog.dismiss();

                }

            }
        });

    }

    private void addParticipants(List<String> commonList) {

        String participantsString = "";

        for (int i=0; i<commonList.size(); i++){

            participantsString = participantsString + commonList.get(i).toString() + "xxx";

        }

        String finalParticipantsString = participantsString;

        StringRequest request = new StringRequest(Request.Method.POST, addParticipantsApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("failed")){

                            Toast.makeText(context, "failed to execute", Toast.LENGTH_SHORT).show();

                        }

                        addTaskHistory(uniqueID, "Changed participants of " + title);

                        getParticipants();

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

                params.put("usernames", finalParticipantsString);
                params.put("project_id", uniqueID);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void showParticipantFriends() {

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

                                    getParticipantInfo(object.getString("friendname"));

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

    private void getParticipantInfo(String friendname) {

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

//                                btsList.add(new ChatListModel(object.getString("id"),
//                                        object.getString("username"),
//                                        object.getString("fullname"),
//                                        object.getString("profilepic"),
//                                        object.getString("phonenumber"),
//                                        object.getString("email"),
//                                        "recently",
//                                        object.getString("position"),
//                                        "0",
//                                        "IMG",
//                                        "TEXT"));
//
//                                btsAdapter.notifyDataSetChanged();

                                if (!participantCheck.contains(object.getString("username"))){

                                    participantCheck.add(object.getString("username"));

                                    participantList.add(new ParticipantsModel(object.getString("username"),
                                            object.getString("profilepic"),
                                            object.getString("position"),
                                            object.getString("fullname"),
                                            "no"));

                                    participantsAdapter.notifyDataSetChanged();

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

    private void updateObservers(List<String> commonList) {

        String obsString = "";


        for (int i=0; i<commonList.size(); i++){

            obsString = obsString + commonList.get(i) + "xxx";

        }


        String finalObsString = obsString;
        StringRequest request = new StringRequest(Request.Method.POST, updateObserversApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("failed")){

                            Toast.makeText(context, "failed to execute", Toast.LENGTH_SHORT).show();

                        }

                        addTaskHistory(uniqueID, "Changed observers of " + title);

                        showTaskObservers();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "connection failed", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("task_id", uniqueID);
                params.put("usernames", finalObsString);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void showFriendObservers() {

        friendObserversList = new ArrayList<>();
        RecyclerView friendObserverRecyclerView = binding.getRoot().findViewById(R.id.observersRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        friendObserverRecyclerView.setLayoutManager(layoutManager);
        friendObserverAdapter = new ObserverTaskAdapter(context, friendObserversList);
        friendObserverRecyclerView.setAdapter(friendObserverAdapter);

        getObsFriends();

    }

    private void getObsFriends() {

        obsCheck = new ArrayList<>();
        Common.commonList.clear();

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

                                    getUserInfoObsFriend(object.getString("friendname"));

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


    private void showTaskObservers() {

        savedObs.clear();
        obsShowList.clear();

        StringRequest request = new StringRequest(Request.Method.POST, fetchTaskObserversApi,
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

                                    obsShowList.add(new ObsShowModel(object.getString("profilepic")));
                                    obsShowAdapter.notifyDataSetChanged();

                                    savedObs.add(new AddStaffModel(object.getString("id"),
                                            object.getString("username"),
                                            "",
                                            object.getString("profilepic")));

                                }

                            }

                        } catch (JSONException e) {
                            Toast.makeText(context, "format error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "connection failed", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("task_id", uniqueID);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void getDescription() {

        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, fetchDescriptionApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String success = jsonObject.getString("success");

                            for (int i=0; i< jsonObject.length(); i++){

                                JSONObject object = jsonArray.getJSONObject(i);
                                binding.description1.setText(object.getString("description"));

                            }


                        } catch (JSONException e) {
                            Toast.makeText(context, "format error", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(context, "connection problem", Toast.LENGTH_SHORT).show();
            }
        }){

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("project_id", uniqueID);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void saveDescription() {

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, addDescriptionApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(context, "connecton eroor", Toast.LENGTH_SHORT).show();
            }
        }){

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("project_id", uniqueID);
                params.put("description", binding.description1.getText().toString().trim());

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }


    private void showRespoBottomSheet() {

        bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_friends, null, false);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        RecyclerView recyclerView = view.findViewById(R.id.btsRecyclerview);
        btsList = new ArrayList<>();
        btnCheckList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        btsAdapter = new ChatListAdapter(btsList, context, "PROJECTFRAGMENT");
        recyclerView.setAdapter(btsAdapter);

        bottomSheetDialog.setDismissWithAnimation(true);

        getFriendsBTS();


        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

                if (Common.commonList.size() != 0) {

                    updateResponsible(Common.commonList.get(0));
                }
                
            }
        });


    }

    private void updateResponsible(String toString) {

        StringRequest request = new StringRequest(Request.Method.POST, updateResponsibleApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        
                        if (response.contains("Position Updated") || response.equalsIgnoreCase("Position Updated")){

                            Toast.makeText(context, "responsible updated", Toast.LENGTH_SHORT).show();
                            addTaskHistory(uniqueID, "Changed responsibles of " + title);
                            
                        }else {
                            Toast.makeText(context, "failed to update responsible", Toast.LENGTH_SHORT).show();
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

                params.put("task_id", uniqueID);
                params.put("userName", toString);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

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

                                    if (!btnCheckList.contains(object.getString("friendname"))) {

                                        btnCheckList.add(object.getString("friendname"));
                                        getUserInfoBTN(object.getString("friendname"));
                                    }

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


    private void getUserInfoObsFriend(String friendname) {

        friendObserversList.clear();

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

                                if (!obsCheck.contains(object.getString("username"))) {

                                    if (object.getString("position").equalsIgnoreCase("observer")) {

                                        obsCheck.add(object.getString("username"));

                                        friendObserversList.add(new ObserverTaskModel(object.getString("username"),
                                                object.getString("fullname"),
                                                object.getString("profilepic")));

                                        friendObserverAdapter.notifyDataSetChanged();
                                    }

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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String y = String.valueOf(year);
        String month = String.valueOf(monthOfYear + 1);
        String day = String.valueOf(dayOfMonth);

        selectedDate = day + "-" + month + "_" + y;

        Calendar now = Calendar.getInstance();

        if (tpd == null) {
            tpd = TimePickerDialog.newInstance(
                    ProjectFragment.this,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    true
            );
        } else {
            tpd.initialize(
                    ProjectFragment.this,
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
        isDateTimeChanged = true;

        binding.deadLine.setText(selectedDate + "  " + selectedTime);

    }

    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }
}