package com.beast.collegemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.contentcapture.ContentCaptureContext;

import com.beast.collegemanagement.databinding.ActivityFeedsBinding;
import com.beast.collegemanagement.tabfragment.FeedsFragment;
import com.beast.collegemanagement.tabfragment.GroupChatFragment;
import com.beast.collegemanagement.tabfragment.NotificationFragment;
import com.beast.collegemanagement.tabfragment.PublicFeedsFragment;
import com.beast.collegemanagement.tabfragment.TaskChatFragment;
import com.beast.collegemanagement.tabfragment.chatsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class FeedsActivity extends AppCompatActivity {

    private ActivityFeedsBinding binding;
    private Context context;

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feeds);
        context = FeedsActivity.this;

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        setUpWithViewPager(binding.viewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        reFressData();


    }

    private void reFressData() {


        binding.navigation.setSelectedItemId(R.id.feed);
        binding.navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()){
//
                    case R.id.chat:
                        startActivity(new Intent(context, MainActivity.class));
                        break;
                    case R.id.feed:
                        break;
                    case R.id.task:
                        startActivity(new Intent(context, TasksActivity.class));
                        finish();
                        break;
                    case R.id.menuu:
                        startActivity(new Intent(context, SettingsActivity.class));
                        break;


                }

                fragmentTransaction.commit();
                return true;
            }
        });


    }


    private void setUpWithViewPager(ViewPager viewPager){

        FeedsActivity.SectionsPagerAdapter adapter = new FeedsActivity.SectionsPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new PublicFeedsFragment(), "feeds");
        adapter.addFragment(new FeedsFragment(), "public");

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