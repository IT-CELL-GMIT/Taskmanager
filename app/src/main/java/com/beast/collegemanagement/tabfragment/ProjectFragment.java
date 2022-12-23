package com.beast.collegemanagement.tabfragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beast.collegemanagement.Common;
import com.beast.collegemanagement.R;
import com.beast.collegemanagement.databinding.FragmentProjectBinding;
import com.bumptech.glide.Glide;

import javax.net.ssl.SNIHostName;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectFragment extends Fragment {

    private FragmentProjectBinding binding;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    Context context;

    ProgressDialog progressDialog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectFragment newInstance(String param1, String param2) {
        ProjectFragment fragment = new ProjectFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project, container, false);
        binding.ll.setBackgroundColor(getContext().getResources().getColor(R.color.icons));
        binding.fl.setBackgroundColor(getContext().getResources().getColor(R.color.icons));

        context = binding.getRoot().getContext();
        progressDialog = new ProgressDialog(context);

        sp = context.getSharedPreferences("FILE_NAME", Context.MODE_PRIVATE);

        binding.edProjectName.setText(sp.getString("title", null));
        binding.createrName.setText("Sanjay Parmar");
        Glide.with(context).load(Common.getProfilePic(context)).into(binding.createrProfilePic);
        binding.responsibleName.setText("Himanshu Pandey");
        binding.tvStatus.setText(sp.getString("status", null));
        binding.deadLine.setText(sp.getString("timeLeft", null));

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Saving Info...");
                progressDialog.show();
                progressDialog.setCancelable(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 2000);
            }
        });

        return binding.getRoot();
    }
}