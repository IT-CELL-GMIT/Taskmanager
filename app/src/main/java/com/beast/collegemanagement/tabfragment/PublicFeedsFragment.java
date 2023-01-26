package com.beast.collegemanagement.tabfragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beast.collegemanagement.Common;
import com.beast.collegemanagement.R;
import com.beast.collegemanagement.adapters.FeedsAdapter;
import com.beast.collegemanagement.databinding.FragmentFeedsBinding;
import com.beast.collegemanagement.databinding.FragmentPublicFeedsBinding;
import com.beast.collegemanagement.models.FeedsModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PublicFeedsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublicFeedsFragment extends Fragment {

    private FragmentPublicFeedsBinding binding;
    private Context context;

    List<FeedsModel> list;
    FeedsAdapter adapter;
    List<String> checkList;
    List<Integer> intList;

    String addFeedPostApi = Common.getBaseUrl() + "AddFeedPost.php";
    String fetchFeedApi = Common.getBaseUrl() + "FetchFriendsFeed.php";
    String fechFrindList = Common.getBaseUrl() + "fetchFriendList.php";

    String feedId, feedText, feedTimeDate, visibility = "private", userNames = "";

    int Rows_Value = 0;
    int ID_VALUE = 999999999;
    int SCROLL_VALUE = 0;
    Boolean scrollState = true;
    Boolean isScrolled = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PublicFeedsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PublicFeedsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PublicFeedsFragment newInstance(String param1, String param2) {
        PublicFeedsFragment fragment = new PublicFeedsFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_public_feeds, container, false);
        context = binding.getRoot().getContext();
        binding.ll.setBackgroundColor(getResources().getColor(R.color.icons));

        intList = new ArrayList<>();
        list = new ArrayList<>();
        checkList = new ArrayList<>();
        RecyclerView recyclerView = binding.getRoot().findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FeedsAdapter(list, context, "FeedsFragment");
        recyclerView.setAdapter(adapter);


        fetchFeeds();

        binding.addFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet();
            }
        });


        return binding.getRoot();
    }


    private void showBottomSheet() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bottomsheet_add_feed_layout, null, false);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.show();

        EditText edFeedText = bottomSheetDialog.findViewById(R.id.edFeedText);
        TextView addFeedBtn = bottomSheetDialog.findViewById(R.id.addFeedBtn);

        addFeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Common.showProgressDialog(context, "Please wait...");
                bottomSheetDialog.dismiss();

                feedId = "feed_" + Common.getUserName(context) + "_" + String.valueOf(System.currentTimeMillis());
                feedText = edFeedText.getText().toString();
                feedTimeDate = Common.getTimeDate();

                list.add(0, new FeedsModel(feedId,
                        Common.getUserName(context),
                        Common.getPosition(context),
                        Common.getProfilePic(context),
                        feedText,
                        feedTimeDate,
                        visibility));

                adapter.notifyDataSetChanged();

                uploadFeed();
            }
        });

    }

    private void uploadFeed() {

        StringRequest request = new StringRequest(Request.Method.POST, addFeedPostApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Common.dismissProgressDialog();

                        if (response.equalsIgnoreCase("Failed") || response.contains("Failed")){

                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                        }

                        list.clear();
                        adapter.notifyDataSetChanged();

                        fetchFeeds();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "connection error", Toast.LENGTH_SHORT).show();
                Common.dismissProgressDialog();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("feed_id", feedId);
                params.put("username", Common.getUserName(context));
                params.put("feed_text", feedText);
                params.put("time_date", feedTimeDate);
                params.put("visibility", visibility);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void getFeeds(){

        StringRequest request = new StringRequest(Request.Method.POST, fetchFeedApi,
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

                                    if (!checkList.contains(object.getString("feed_id")) && object.getString("visibility").equalsIgnoreCase(visibility)){

                                        checkList.add(object.getString("feed_id"));

                                        list.add(new FeedsModel(object.getString("feed_id"),
                                                object.getString("username"),
                                                object.getString("position"),
                                                object.getString("profilePic"),
                                                object.getString("feed_text"),
                                                object.getString("time_date"),
                                                object.getString("visibility")));

                                        ID_VALUE = Integer.parseInt(object.getString("id"));

                                        Rows_Value = list.size();
                                        adapter.notifyDataSetChanged();

                                    }

                                    if (i == jsonArray.length() - 1){
                                        scrollState = true;
                                    }

                                }

                            }

                        } catch (JSONException e) {
                            Toast.makeText(context, "format error", Toast.LENGTH_SHORT).show();
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

                params.put("visibility", visibility);
                params.put("usernames", userNames);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void fetchFeeds() {

        userNames = "";

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

                                    userNames = userNames + object.getString("friendname") + "xxx";

                                    if (i == jsonArray.length() -1){
                                        getFeeds();
                                        Toast.makeText(context, userNames, Toast.LENGTH_SHORT).show();
                                    }

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



}