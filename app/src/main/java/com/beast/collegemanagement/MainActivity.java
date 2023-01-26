package com.beast.collegemanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beast.collegemanagement.databinding.ActivityMainBinding;
import com.beast.collegemanagement.models.ChatListModel;
import com.beast.collegemanagement.models.ObserverTaskModel;
import com.beast.collegemanagement.tabfragment.GroupChatFragment;
import com.beast.collegemanagement.tabfragment.NotificationFragment;
import com.beast.collegemanagement.tabfragment.TaskChatFragment;
import com.beast.collegemanagement.tabfragment.chatsFragment;
import com.bumptech.glide.Glide;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    String userId, userName, fullName, eMail, phoneNumber, password, profilePic;

    private BottomSheetDialog bottomSheetDialog;

    private TabLayout tabLayout;

    String API_UPDATE_PROFILEPIC = "https://biochemical-damping.000webhostapp.com/Management%20of%20College/updateprofilepic.php";
    String API_USERID = "https://biochemical-damping.000webhostapp.com/Management%20of%20College/fetchuserid.php";
    String API_CHANGE_FULLNAME = "https://biochemical-damping.000webhostapp.com/Management%20of%20College/changefullname.php";
    String API_FETCHDATA = "https://biochemical-damping.000webhostapp.com/Management%20of%20College/fetchuserdata.php";
    Uri imagePath;

    ProgressDialog progressDialog;

    String profileString = "";

    int count;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        progressDialog = new ProgressDialog(this);
        sp = getSharedPreferences("FILE_NAME", MODE_PRIVATE);
        editor = sp.edit();

        context = MainActivity.this;

        setUpWithViewPager(binding.viewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        reFreshFromDataBase();

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 3){
                    tabLayout.getTabAt(3).setText("Notification");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        binding.addStaff.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), AddStaffActivity.class));
//            }
//        });
//
//        binding.addtask.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), SelectStaffActivity.class));
//            }
//        });

                getNotification();

    }

    private void reFreshFromDataBase() {

        userId = sp.getString("userId", null);
        userName = sp.getString("userName", null);
        fullName = sp.getString("fullName", null);
        eMail = sp.getString("eMail", null);
        phoneNumber = sp.getString("phoneNumber", null);
        password = sp.getString("password", null);
        profilePic = sp.getString("profilePic", null);

        updateUserDataOff(Common.getUserName(context));

    }

    private void getNotification() {

        count = 2;

        tabLayout.getTabAt(3).setText("Notificaton" + "(" + String.valueOf(count) + ")");

    }


    private void setUpWithViewPager(ViewPager viewPager){

        MainActivity.SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new chatsFragment(), "chats");
        adapter.addFragment(new GroupChatFragment(), "group");
        adapter.addFragment(new TaskChatFragment(), "task chat");
        adapter.addFragment(new NotificationFragment(), "Notification");

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

    private void reFressData() {

        userId = sp.getString("userId", null);
        userName = sp.getString("userName", null);
        fullName = sp.getString("fullName", null);
        eMail = sp.getString("eMail", null);
        phoneNumber = sp.getString("phoneNumber", null);
        password = sp.getString("password", null);
        profilePic = sp.getString("profilePic", null);

        setToolBar();

        binding.navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()){
//
                    case R.id.chat:
                        break;
                    case R.id.feed:
                        startActivity(new Intent(MainActivity.this, FeedsActivity.class));
                        finish();
                        break;
                    case R.id.task:
                        startActivity(new Intent(MainActivity.this, TasksActivity.class));
                        finish();
                        break;
                    case R.id.menuu:
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        finish();
                        break;


                }

                fragmentTransaction.commit();
                return true;
            }
        });


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

    @SuppressLint("MissingInflatedId")
    private void showBottomSheet() {
        
        bottomSheetDialog = new BottomSheetDialog(MainActivity.this);

        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.bottomsheet_toolbar, null);

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.setTitle("BottomSheet_Profile");
        bottomSheetDialog.show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            Objects.requireNonNull(bottomSheetDialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }

        EditText userName = view.findViewById(R.id.userName);
        userName.setText(fullName);
        Glide.with(getApplicationContext()).load(profilePic).into((ImageView) view.findViewById(R.id.image_profile));

        view.findViewById(R.id.btn_cencle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        
        view.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                progressDialog.setMessage("Please Wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                changeFullName(userName.getText().toString().trim());
            }
        });

        view.findViewById(R.id.changeProfilePic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalary();
            }
        });
        
        view.findViewById(R.id.LogOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("LOG_STATUS", "LOGGEDOUT");
                editor.apply();

                startActivity(new Intent(getApplicationContext(), Loginpage.class));
                finish();

            }
        });
        
    }

    private void changeFullName(String fullname) {

        StringRequest request = new StringRequest(Request.Method.POST, API_CHANGE_FULLNAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("Name Updated") || response.contains("Name Updated")){

                            progressDialog.dismiss();

                            updateUserDataOff(userName);

                        }else {
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("userName", userName);
                params.put("fullName", fullname);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

    private void updateUserDataOff(String userName) {

        progressDialog.setMessage("Getting Info...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, API_FETCHDATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            if (success.equals("1")){
                                String userId, username, fullName, profilePic, phoneNumber, eMail, password, position;

                                JSONObject object = jsonArray.getJSONObject(0);

                                userId = object.getString("id");
                                username = object.getString("username");
                                fullName = object.getString("fullname");
                                profilePic = object.getString("profilepic");
                                phoneNumber = object.getString("phonenumber");
                                eMail = object.getString("email");
                                password = object.getString("password");
                                position = object.getString("position");

                                if (userName.equals(username)){

                                    editor.putString("userId", userId);
                                    editor.putString("userName", username);
                                    editor.putString("fullName", fullName);
                                    editor.putString("profilePic", profilePic);
                                    editor.putString("phoneNumber", phoneNumber);
                                    editor.putString("eMail", eMail);
                                    editor.putString("password", password);
                                    editor.putString("SIGN_STATUS", "SIGNED");
                                    editor.putString("LOG_STATUS", "NONE");
                                    editor.putString("POSITION", position);
                                    editor.apply();

                                    progressDialog.dismiss();

                                    reFressData();

                                }else {
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Username of database is not matching try later", Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "not succeed try later", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "failed to retrive old data, try again later", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("username", userName);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

    private void openGalary() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select profile pic"), 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 &&
        resultCode == RESULT_OK &&
        data.getData() != null){

            bottomSheetDialog.dismiss();
            imagePath = data.getData();
            giveUpdateProfileWarning();

        }

    }

    private void giveUpdateProfileWarning() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.alert_profile_icon, null);
        builder.setView(view);
        ImageView imageView = view.findViewById(R.id.profile_pic);
        imageView.setImageURI(imagePath);
        builder.setMessage("do you really want to change profile pic");
        builder.setCancelable(false);
        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        converToString(imagePath);
                    }
                });

        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.setOnShowListener(arg0 -> {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.grey));
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        });
        alert.show();

    }

    private void changeProfilePic() {

        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        
        StringRequest request = new StringRequest(Request.Method.POST, API_UPDATE_PROFILEPIC,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase("Image Uploaded") || response.contains("Image Uploaded")){

                            progressDialog.dismiss();

                            progressDialog.setMessage("Getting Info...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            
                            getNewProfilePic();

                        }else {
                            Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("profilePic", profileString);
                params.put("userName", userName);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

    private void getNewProfilePic() {

        StringRequest request = new StringRequest(Request.Method.POST, API_USERID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String success =jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            if (success.equals("1")){

                                JSONObject object = jsonArray.getJSONObject(0);
                                String profilePic2 = object.getString("profilepic");

                                Toast.makeText(MainActivity.this, "profile updated", Toast.LENGTH_SHORT).show();
//                                Glide.with(getApplicationContext()).load(profilePic2).into(binding.toolBarProfilePic);

                                editor.putString("profilePic", profilePic2);
                                profilePic = profilePic2;

                                editor.apply();

                                progressDialog.dismiss();


                            }

                        }catch (Exception e){
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("username", userName);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

    private void converToString(Uri imagePath) {

        try {

            InputStream in = getContentResolver().openInputStream(imagePath);
            byte[] bytes = getBytes(in);

            profileString = Base64.encodeToString(bytes, Base64.DEFAULT);
            changeProfilePic();

        } catch (Exception e) {
            Toast.makeText(this, "Please select a image\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    private static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

}