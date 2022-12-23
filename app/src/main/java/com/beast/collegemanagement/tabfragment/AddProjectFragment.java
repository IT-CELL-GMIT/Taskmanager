package com.beast.collegemanagement.tabfragment;

import static com.beast.collegemanagement.Common.getProfilePic;
import static com.beast.collegemanagement.Common.getUserName;

import android.content.Context;
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
import com.beast.collegemanagement.adapters.ProjectListAdapter;
import com.beast.collegemanagement.databinding.FragmentAddProjectBinding;
import com.beast.collegemanagement.models.ProjectListModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        list = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProjectListAdapter(list, binding.getRoot().getContext());
        binding.recyclerView.setAdapter(adapter);

        getProject();

        binding.addProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomsheet();
            }
        });

        return binding.getRoot();
    }

    private void showBottomsheet() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bottomsheet_create_project, null, false);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        EditText edProjectName = bottomSheetDialog.findViewById(R.id.edProjectName);
        TextView btnCreateProject = bottomSheetDialog.findViewById(R.id.createProject);

        btnCreateProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String projectName = edProjectName.getText().toString().trim();

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

                }

            }
        });

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