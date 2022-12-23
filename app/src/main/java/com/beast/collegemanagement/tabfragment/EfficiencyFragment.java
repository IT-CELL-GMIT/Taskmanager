package com.beast.collegemanagement.tabfragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beast.collegemanagement.R;
import com.beast.collegemanagement.databinding.FragmentEfficiencyBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EfficiencyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EfficiencyFragment extends Fragment {

    private FragmentEfficiencyBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EfficiencyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EfficiencyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EfficiencyFragment newInstance(String param1, String param2) {
        EfficiencyFragment fragment = new EfficiencyFragment();
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
        binding = FragmentEfficiencyBinding.inflate(inflater, container, false);

        binding.ll.setBackgroundColor(getResources().getColor(R.color.icons));

        return binding.getRoot();
    }
}