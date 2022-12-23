package com.beast.collegemanagement.tabfragment;

import static com.beast.collegemanagement.Common.getBaseUrl;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beast.collegemanagement.R;
import com.beast.collegemanagement.adapters.AddStaffAdapter;
import com.beast.collegemanagement.adapters.StaffAdapter;
import com.beast.collegemanagement.databinding.FragmentAddStaffBinding;
import com.beast.collegemanagement.models.AddStaffModel;
import com.beast.collegemanagement.models.StaffModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddStaffFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddStaffFragment extends Fragment {

    private FragmentAddStaffBinding binding;
    private List<AddStaffModel> list;
    private AddStaffAdapter adapter;
    StaffModel model;
    String searchUser = getBaseUrl() + "fetchUserDatabyKey.php";
    ProgressDialog progressDialog;

    String id, userName, fullName, profilePic;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddStaffFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddStaffFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddStaffFragment newInstance(String param1, String param2) {
        AddStaffFragment fragment = new AddStaffFragment();
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
        binding = FragmentAddStaffBinding.inflate(inflater, container, false);
        binding.ll.setBackgroundColor(getResources().getColor(R.color.icons));
        binding.edUserName.setBackgroundColor(getResources().getColor(R.color.icons));

        sp = getContext().getSharedPreferences("FILE_NAME", Context.MODE_PRIVATE);
        editor = sp.edit();

        progressDialog = new ProgressDialog(binding.getRoot().getContext());
        progressDialog.setCancelable(true);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        adapter = new AddStaffAdapter(list, getActivity());
        binding.recyclerView.setAdapter(adapter);

        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!binding.edUserName.getText().toString().isEmpty()){

                    getStaff(binding.edUserName.getText().toString().trim().toLowerCase());

                }else {
                    Toast.makeText(getContext(), "please enter atleast one charachter", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return binding.getRoot();
    }

    private void getStaff(String key) {

        list.clear();

        progressDialog.setMessage("please wait...");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, searchUser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();

                        try {


                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            if (success.equalsIgnoreCase("1")){
                                
                                if (jsonArray.length() == 0){
                                    Toast.makeText(getContext(), "no search result", Toast.LENGTH_SHORT).show();
                                }
                                
                                for (int i=0; i<jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    id = object.getString("id");
                                    userName = object.getString("username");
                                    fullName = object.getString("fullname");
                                    profilePic = object.getString("profilepic");

                                    if (!userName.equalsIgnoreCase(sp.getString("userName", null))) {

                                        list.add(new AddStaffModel(id,
                                                userName,
                                                fullName,
                                                profilePic));

                                        adapter.notifyDataSetChanged();

                                    }

                                }

                            }else {

                                Toast.makeText(getContext(), "unable to success!", Toast.LENGTH_SHORT).show();

                            }

                        }catch (Exception e){
                            Toast.makeText(binding.getRoot().getContext(), "something went wrong!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(binding.getRoot().getContext(), "Connection Problem", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("key", key);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(binding.getRoot().getContext());
        queue.add(request);

    }

//    private void getStaff() {
//
//        model = new StaffModel("20",
//                "Sanjay1",
//                "Sanjay Parmar",
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
//                "555555555555",
//                "laudabhencho@gmail.com",
//                "xxxxxxxxxxxxx",
//                "");
//
//        list.add(model);
//        adapter.notifyDataSetChanged();model = new StaffModel("20",
//                "Sanjay1",
//                "Sanjay Parmar",
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
//                "555555555555",
//                "laudabhencho@gmail.com",
//                "xxxxxxxxxxxxx",
//                "HOD");
//
//        list.add(model);
//        adapter.notifyDataSetChanged();model = new StaffModel("20",
//                "Sanjay1",
//                "Sanjay Parmar",
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
//                "555555555555",
//                "laudabhencho@gmail.com",
//                "xxxxxxxxxxxxx",
//                "SPEC");
//
//        list.add(model);
//        adapter.notifyDataSetChanged();
//
//    }

}