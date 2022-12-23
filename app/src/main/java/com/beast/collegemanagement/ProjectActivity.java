package com.beast.collegemanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.beast.collegemanagement.databinding.ActivityProjectBinding;
import com.beast.collegemanagement.tabfragment.CommentFragment;
import com.beast.collegemanagement.tabfragment.FilesFragment;
import com.beast.collegemanagement.tabfragment.NotificationFragment;
import com.beast.collegemanagement.tabfragment.ProjectFragment;
import com.beast.collegemanagement.tabfragment.chatsFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ProjectActivity extends AppCompatActivity {

    private ActivityProjectBinding binding;

    String uniqueID, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_project);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        setUpWithViewPager(binding.viewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        Intent intent = getIntent();
        uniqueID = intent.getStringExtra("uniqueID");
        title = intent.getStringExtra("title");

        if (uniqueID == null || title == null){
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
            finish();
        }

        binding.projectName.setText(title);

    }

    private void setUpWithViewPager(ViewPager viewPager){

        ProjectActivity.SectionsPagerAdapter adapter = new ProjectActivity.SectionsPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new ProjectFragment(), "Task");
        adapter.addFragment(new FilesFragment(), "Files");
        adapter.addFragment(new CommentFragment(), "Comments");

        viewPager.setAdapter(adapter);

    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

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

}