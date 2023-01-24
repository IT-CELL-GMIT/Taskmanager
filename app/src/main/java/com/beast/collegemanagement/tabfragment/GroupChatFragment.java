package com.beast.collegemanagement.tabfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.JsonReader;
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
import com.beast.collegemanagement.AddGroupChatActivity;
import com.beast.collegemanagement.Common;
import com.beast.collegemanagement.R;
import com.beast.collegemanagement.adapters.AddGroupChatAdapter;
import com.beast.collegemanagement.databinding.FragmentGroupChatBinding;
import com.beast.collegemanagement.models.ChatListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupChatFragment extends Fragment {

    private FragmentGroupChatBinding binding;

    String fetchGroupsApi = Common.getBaseUrl() + "fetchGroups.php";

    private Context context;

    List<ChatListModel> groupList;
    AddGroupChatAdapter groupChatAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GroupChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupChatFragment newInstance(String param1, String param2) {
        GroupChatFragment fragment = new GroupChatFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_group_chat, container, false);
        binding.ll.setBackgroundColor(getResources().getColor(R.color.icons));
        context = binding.getRoot().getContext();


        groupList = new ArrayList<>();
        RecyclerView groupChatRecyclerview = binding.getRoot().findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        groupChatRecyclerview.setLayoutManager(layoutManager);
        groupChatAdapter = new AddGroupChatAdapter(groupList, context, "GroupChatFragment");
        groupChatRecyclerview.setAdapter(groupChatAdapter);


        fetchGroups();

        binding.addChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, AddGroupChatActivity.class));
            }
        });


        return binding.getRoot();
    }

    private void fetchGroups() {

        StringRequest request = new StringRequest(Request.Method.POST, fetchGroupsApi,
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

                                    groupList.add(0, new ChatListModel("",
                                            object.getString("group_id"),
                                            object.getString("groupName"),
                                            object.getString("profilePic"),
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            ""));

                                    groupChatAdapter.notifyDataSetChanged();

                                }

                            }

                        } catch (JSONException e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
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

                params.put("username", Common.getUserName(context));

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }
}