package com.beast.collegemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.beast.collegemanagement.databinding.ActivityStaffShowBinding;
import com.beast.collegemanagement.tabfragment.AddStaffFragment;
import com.beast.collegemanagement.tabfragment.NotificationFragment;
import com.beast.collegemanagement.tabfragment.StaffListFragment;
import com.beast.collegemanagement.tabfragment.chatsFragment;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class StaffShowActivity extends AppCompatActivity {

    String userId, userName, fullName, eMail, phoneNumber, password, profilePic;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    ActivityStaffShowBinding binding;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_staff_show);
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

        StaffShowActivity.SectionsPagerAdapter adapter = new StaffShowActivity.SectionsPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new StaffListFragment(), "Friend List");
        adapter.addFragment(new AddStaffFragment(), "Add Staff");

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

        binding.navigation.setSelectedItemId(R.id.feed);
        binding.navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()){
//
                    case R.id.chat:
                        startActivity(new Intent(StaffShowActivity.this, MainActivity.class));
                        finish();
                        break;
                    case R.id.feed:
                        break;
                    case R.id.task:
                        startActivity(new Intent(StaffShowActivity.this, TasksActivity.class));
                        finish();
                        break;
                    case R.id.menuu:
                        Toast.makeText(StaffShowActivity.this, "menuu", Toast.LENGTH_SHORT).show();
                        break;


                }

                fragmentTransaction.commit();
                return true;
            }
        });


    }


}