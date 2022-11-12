package com.beast.collegemanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.beast.collegemanagement.adapters.StaffAdapter;
import com.beast.collegemanagement.models.StaffModel;

import java.util.ArrayList;
import java.util.List;

public class SelectStaffActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    StaffAdapter adapter;
    StaffModel model;
    List<StaffModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_staff);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        adapter = new StaffAdapter(list, SelectStaffActivity.this, "SelectStaff");
        recyclerView.setAdapter(adapter);

        for (int i = 0; i<11; i++){
            getData();
        }

    }

    private void getData() {

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