package com.beast.collegemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.beast.collegemanagement.databinding.ActivityFeedsBinding;
import com.beast.collegemanagement.databinding.ActivitySettingsBinding;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        context = SettingsActivity.this;
        binding.ll.setBackgroundColor(getResources().getColor(R.color.icons));

        Glide.with(context).load(Common.getProfilePic(context)).into(binding.mainProfilePic);
        binding.username.setText(Common.getUserName(context));
        binding.tvPosition.setText(Common.getPosition(context));

        binding.profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ProfileActivity.class));
            }
        });

        binding.freeDriveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, FreeDriveActivity.class));
            }
        });

        binding.activityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, MyActivitiesActivity.class));
            }
        });

        binding.employeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, StaffShowActivity.class));
            }
        });

        binding.logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Common.setNameToSharedPref("LOG_STATUS", "LOGGEDOUT", context);

                startActivity(new Intent(getApplicationContext(), Loginpage.class));
                finish();
            }
        });

        binding.addNotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, NotesActivity.class));
            }
        });

        reFressData();

    }

    private void reFressData() {

        binding.navigation.setSelectedItemId(R.id.menuu);
        binding.navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()){
//
                    case R.id.chat:
                        startActivity(new Intent(context, MainActivity.class));
                        finish();
                        break;
                    case R.id.feed:
                        startActivity(new Intent(context, FeedsActivity.class));
                        finish();
                        break;
                    case R.id.task:
                        startActivity(new Intent(context, TasksActivity.class));
                        finish();
                        break;
                    case R.id.menuu:
                        break;


                }

                fragmentTransaction.commit();
                return true;
            }
        });


    }


}