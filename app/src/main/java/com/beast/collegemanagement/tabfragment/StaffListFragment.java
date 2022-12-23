package com.beast.collegemanagement.tabfragment;

import android.content.Context;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beast.collegemanagement.Common;
import com.beast.collegemanagement.R;
import com.beast.collegemanagement.adapters.StaffAdapter;
import com.beast.collegemanagement.databinding.FragmentStaffListBinding;
import com.beast.collegemanagement.models.StaffModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StaffListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StaffListFragment extends Fragment {

    private FragmentStaffListBinding binding;
    private List<StaffModel> list;
    private StaffAdapter adapter;
    StaffModel model;

    String fechFrindList = Common.getBaseUrl() + "fetchFriendList.php";
    String fetchUserdata = Common.getBaseUrl() + "fetchuserdata.php";

    Context context;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StaffListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StaffListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StaffListFragment newInstance(String param1, String param2) {
        StaffListFragment fragment = new StaffListFragment();
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
       binding =  FragmentStaffListBinding.inflate(inflater, container, false);
       binding.ll.setBackgroundColor(getResources().getColor(R.color.icons));

       binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
       list = new ArrayList<>();
       adapter = new StaffAdapter(list, getActivity(), "AddStaff");
       binding.recyclerView.setAdapter(adapter);

       context = binding.getRoot().getContext();

//       getStaff();

        getFriends();
       return binding.getRoot();
    }

    private void getFriends() {

        StringRequest request = new StringRequest(Request.Method.POST, fechFrindList,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String success = jsonObject.getString("success");

                            if (success.equalsIgnoreCase("1")){

                                for (int i=0; i<jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    getUserInfo(object.getString("friendname"));
                                    Toast.makeText(context, object.getString("friendname"), Toast.LENGTH_SHORT).show();

                                }

                            }

                        } catch (JSONException e) {
                            Toast.makeText(context, "format error", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "connetction error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("username", Common.getUserName(context));

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void getUserInfo(String friendname) {

        StringRequest request = new StringRequest(Request.Method.POST, fetchUserdata,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String success = jsonObject.getString("success");

                            if (success.equalsIgnoreCase("1")){

                                JSONObject object = jsonArray.getJSONObject(0);

                                list.add(new StaffModel("unknown",
                                        object.getString("username"),
                                        object.getString("fullname"),
                                        object.getString("profilepic"),
                                        object.getString("phonenumber"),
                                        object.getString("email"),
                                        "unknwon",
                                        object.getString("position")));

                                adapter.notifyDataSetChanged();

                            }

                        } catch (JSONException e) {
                            Toast.makeText(context, "format error", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, "connection error", Toast.LENGTH_SHORT).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("username", friendname);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void getStaff() {

        model = new StaffModel("20",
                "Sanjay1",
                "Sanjay Parmar",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
                "555555555555",
                "laudabhencho@gmail.com",
                "xxxxxxxxxxxxx",
                "");

        list.add(model);
        adapter.notifyDataSetChanged();model = new StaffModel("20",
                "Sanjay1",
                "Sanjay Parmar",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
                "555555555555",
                "laudabhencho@gmail.com",
                "xxxxxxxxxxxxx",
                "HOD");

        list.add(model);
        adapter.notifyDataSetChanged();model = new StaffModel("20",
                "Sanjay1",
                "Sanjay Parmar",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQwEcb78mt0Dxay3RaI6yRBLY34JlKT1eQgUAFnvNXww&s",
                "555555555555",
                "laudabhencho@gmail.com",
                "xxxxxxxxxxxxx",
                "SPEC");

        list.add(model);
        adapter.notifyDataSetChanged();

    }
}