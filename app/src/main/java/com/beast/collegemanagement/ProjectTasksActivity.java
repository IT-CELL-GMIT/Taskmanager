package com.beast.collegemanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.beast.collegemanagement.databinding.ActivityProjectBinding;
import com.beast.collegemanagement.databinding.ActivityProjectTasksBinding;
import com.beast.collegemanagement.tabfragment.AddTaskFragment;
import com.bumptech.glide.Glide;

public class ProjectTasksActivity extends AppCompatActivity {

    private ActivityProjectTasksBinding binding;

    String title,
            dateTime,
            leaderProfileImage,
            leaderUsername,
            projectStatus,
            closeDate,
                    projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_project_tasks);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        dateTime = intent.getStringExtra("dateTime");
        leaderProfileImage = intent.getStringExtra("leaderProfileImage");
        leaderUsername = intent.getStringExtra("leaderUsername");
        projectStatus = intent.getStringExtra("projectStatus");
        closeDate = intent.getStringExtra("closeDate");
        projectId = intent.getStringExtra("project_id");

        Glide.with(ProjectTasksActivity.this).load(leaderProfileImage).into(binding.toolBarProfilePic);
        binding.toolBarUserName.setText(leaderUsername);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainLL, new AddTaskFragment("ProjectTasksActivityxxx" + title + "xxx" + dateTime + "xxx" + leaderProfileImage
                + "xxx" + leaderUsername + "xxx" + projectStatus + "xxx" + closeDate + "xxx" + projectId));
        fragmentTransaction.commit();

    }
}