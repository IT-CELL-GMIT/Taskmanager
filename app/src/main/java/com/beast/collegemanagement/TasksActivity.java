package com.beast.collegemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.beast.collegemanagement.databinding.ActivityTasksBinding;
import com.beast.collegemanagement.tabfragment.AddProjectFragment;
import com.beast.collegemanagement.tabfragment.AddTaskFragment;
import com.beast.collegemanagement.tabfragment.EfficiencyFragment;
import com.beast.collegemanagement.tabfragment.HistoryFragment;
import com.beast.collegemanagement.tabfragment.NotificationFragment;
import com.beast.collegemanagement.tabfragment.TaskEfficiencyFragment;
import com.beast.collegemanagement.tabfragment.chatsFragment;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class TasksActivity extends AppCompatActivity {

    String userId, userName, fullName, eMail, phoneNumber, password, profilePic;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private ActivityTasksBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tasks);
        sp = getSharedPreferences("FILE_NAME", MODE_PRIVATE);
        editor = sp.edit();

        setUpWithViewPager(binding.viewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        reFressData();

    }

    private void setToolBar() {
        String[] splits = fullName.split(" ");

        if (userId != null){
            binding.toolBarUserName.setText(splits[0] + " >");
            Glide.with(getApplicationContext()).load(profilePic).into(binding.toolBarProfilePic);
        }else {
            Toast.makeText(this, "some error occured", Toast.LENGTH_SHORT).show();
        }

//        binding.toolBar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showBottomSheet();
//            }
//        });

    }

    private void setUpWithViewPager(ViewPager viewPager){

        TasksActivity.SectionsPagerAdapter adapter = new TasksActivity.SectionsPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new AddTaskFragment("TasksActivity"), "tasks");
        adapter.addFragment(new AddProjectFragment(), "project");
        adapter.addFragment(new HistoryFragment(), "history");
//        adapter.addFragment(new TaskEfficiencyFragment(), "efficiency");

        viewPager.setAdapter(adapter);

    }

    private static class SectionsPagerAdapter extends FragmentPagerAdapter {


        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager manager){

            super(manager);
        }

        @Override
        public Fragment getItem(int positon){

            return mFragmentList.get(positon);
        }
        @Override
        public int getCount(){

            return mFragmentList.size();

        }
        public void addFragment(Fragment fragment, String title){

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        }
        @Override
        public CharSequence getPageTitle(int position){

            return mFragmentTitleList.get(position);
        }
    }

    private void reFressData() {

        userId = sp.getString("userId", null);
        userName = sp.getString("userName", null);
        fullName = sp.getString("fullName", null);
        eMail = sp.getString("eMail", null);
        phoneNumber = sp.getString("phoneNumber", null);
        password = sp.getString("password", null);
        profilePic = sp.getString("profilePic", null);

        setToolBar();

        binding.navigation.setSelectedItemId(R.id.task);
        binding.navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()){
//
                    case R.id.chat:
                        startActivity(new Intent(TasksActivity.this, MainActivity.class));
                        finish();
                        break;
                    case R.id.feed:
                        startActivity(new Intent(TasksActivity.this, FeedsActivity.class));
                        finish();
                        break;
                    case R.id.task:
                        break;
                    case R.id.menuu:
                        startActivity(new Intent(TasksActivity.this, SettingsActivity.class));
                        finish();
                        break;


                }

                fragmentTransaction.commit();
                return true;
            }
        });


    }

}