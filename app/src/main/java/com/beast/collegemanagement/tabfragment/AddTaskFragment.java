package com.beast.collegemanagement.tabfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beast.collegemanagement.Common;
import com.beast.collegemanagement.R;
import com.beast.collegemanagement.adapters.TaskListAdapter;
import com.beast.collegemanagement.databinding.FragmentAddTaskBinding;
import com.beast.collegemanagement.models.TaskListModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTaskFragment extends Fragment {

    private FragmentAddTaskBinding binding;
    private List<TaskListModel> list;
    private TaskListAdapter adapter;

    Context context;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddTaskFragment() {
        // Required empty public constructor
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
        AddTaskFragment fragment = new AddTaskFragment();
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

        getProjectTask();

        binding.addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTaskBottomsheet();
            }
        });

        return binding.getRoot();
    }

    private void showAddTaskBottomsheet() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bottomsheet_create_task, null, false);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
        bottomSheetDialog.setDismissWithAnimation(true);

        EditText edTaskName = bottomSheetDialog.findViewById(R.id.edTaskName);
        TextView btnCreateTask = bottomSheetDialog.findViewById(R.id.createTask);

        edTaskName.setBackgroundColor(context.getResources().getColor(R.color.icons));

        btnCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskName = edTaskName.getText().toString().trim();

                if (taskName.length() < 3){

                    bottomSheetDialog.dismiss();
                    Toast.makeText(context, "invalid task name", Toast.LENGTH_SHORT).show();

                }else {

                    bottomSheetDialog.dismiss();
                    list.add(new TaskListModel("1",taskName,
                            Common.getFullName(context),
                            Common.getProfilePic(context),
                            "",
                            "14:40",
                            "01-01_2023",
                            "PENDING",
                            ""));

                    adapter.notifyDataSetChanged();

                }

            }
        });

    }

    private void getProjectTask() {

        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        String currentDate = new SimpleDateFormat("dd-MM_yyyy", Locale.getDefault()).format(new Date());

        list.add(new TaskListModel("1","Legit",
                "Sanjay Parmar",
                Common.getProfilePic(getContext()),
                "",
                "14:40",
                "28-13_2022",
                "COMPLETED",
                "01-11_2022"));

        adapter.notifyDataSetChanged();

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
}