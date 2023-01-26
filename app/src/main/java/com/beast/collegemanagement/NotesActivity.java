package com.beast.collegemanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
import com.beast.collegemanagement.adapters.FreeDriveAdapter;
import com.beast.collegemanagement.databinding.ActivityNotesBinding;
import com.beast.collegemanagement.models.FreeDriveModel;
import com.beast.collegemanagement.models.TaskListModel;
import com.beast.collegemanagement.tabfragment.AddTaskFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotesActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    String addNoteApi = Common.getBaseUrl() + "AddReminder.php";
    String fetchReminderApi = Common.getBaseUrl() + "FetchUserReminder.php";

    private ActivityNotesBinding binding;
    private Context context;

    List<FreeDriveModel> list;
    FreeDriveAdapter adapter;
    TextView deadLine;
    String taskName;

    private DatePickerDialog dpd;
    private TimePickerDialog tpd;

    String selectedDate, selectedTime, selectedDateTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notes);
        context = NotesActivity.this;
        binding.mainLL.setBackgroundColor(getResources().getColor(R.color.icons));

        list = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FreeDriveAdapter(list, context, "NotesActivity");
        recyclerView.setAdapter(adapter);

        binding.addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet();
            }
        });

        fetchReminders();

    }

    private void fetchReminders() {

        StringRequest request = new StringRequest(Request.Method.POST, fetchReminderApi,
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

                                    list.add(new FreeDriveModel(object.getString("text"),
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

    private void showBottomSheet() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bottomsheet_reminder_layout, null, false);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
        bottomSheetDialog.setDismissWithAnimation(true);

        EditText edTaskName = bottomSheetDialog.findViewById(R.id.edTaskName);
        TextView btnCreateTask = bottomSheetDialog.findViewById(R.id.createTask);
        deadLine = bottomSheetDialog.findViewById(R.id.deadLine);
        LinearLayout deadLineBtn = bottomSheetDialog.findViewById(R.id.deadLineLL);

        edTaskName.setBackgroundColor(context.getResources().getColor(R.color.icons));


        btnCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskName = edTaskName.getText().toString().trim();

                if (taskName.length() < 3){

                    bottomSheetDialog.dismiss();
                    Toast.makeText(context, "invalid task name{minimum 3 chars required}", Toast.LENGTH_SHORT).show();

                }else if (deadLine.getText().toString().trim().equalsIgnoreCase("select deadline")){
                    Toast.makeText(context, "deadline of task is required", Toast.LENGTH_SHORT).show();
                }
                else {

                    taskName = edTaskName.getText().toString().trim();

                    bottomSheetDialog.dismiss();

                    AddNewTask();

                }

            }
        });

        deadLineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar now = Calendar.getInstance();

                if (dpd == null) {
                    dpd = DatePickerDialog.newInstance(
                            (DatePickerDialog.OnDateSetListener) context,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                } else {
                    dpd.initialize(
                            (DatePickerDialog.OnDateSetListener) context,
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
                dpd.show(getSupportFragmentManager(), "Datepickerdialog");

            }
        });



    }


    private void AddNewTask() {

        uploadReminder();

    }

    private void uploadReminder() {

        StringRequest request = new StringRequest(Request.Method.POST, addNoteApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fetchReminders();
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

                map.put("text", taskName);
                map.put("username", Common.getUserName(context));
                map.put("time_date", Common.getTimeDate());

                return map;
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
                    (TimePickerDialog.OnTimeSetListener) context,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    true
            );
        } else {
            tpd.initialize(
                    (TimePickerDialog.OnTimeSetListener) context,
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
        tpd.show(getSupportFragmentManager(), "Timepickerdialog");

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