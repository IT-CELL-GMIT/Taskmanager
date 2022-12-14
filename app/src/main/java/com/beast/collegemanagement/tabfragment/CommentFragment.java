package com.beast.collegemanagement.tabfragment;

import static android.app.Activity.RESULT_OK;
import static com.beast.collegemanagement.Common.getFullName;
import static com.beast.collegemanagement.Common.getProfilePic;
import static com.beast.collegemanagement.Common.getTimeDate;
import static com.beast.collegemanagement.Common.getUserName;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.beast.collegemanagement.Common;
import com.beast.collegemanagement.R;
import com.beast.collegemanagement.adapters.CommentAdapter;
import com.beast.collegemanagement.databinding.FragmentCommentBinding;
import com.beast.collegemanagement.models.CommentModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentFragment extends Fragment {

    private FragmentCommentBinding binding;

    Context context;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<CommentModel> list;
    CommentAdapter adapter;

    String userName, fullName, profilePic;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CommentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommentFragment newInstance(String param1, String param2) {
        CommentFragment fragment = new CommentFragment();
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
       binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comment, container, false);
       binding.fl.setBackgroundColor(getContext().getResources().getColor(R.color.icons));

       context = binding.getRoot().getContext();

       sp = context.getSharedPreferences("FILE_NAME", Context.MODE_PRIVATE);
       editor = sp.edit();

       recyclerView = binding.getRoot().findViewById(R.id.commentRecyclerView);
       layoutManager = new LinearLayoutManager(context);
       recyclerView.setLayoutManager(layoutManager);
       list = new ArrayList<>();
       adapter = new CommentAdapter(list, context);
       recyclerView.setAdapter(adapter);

       binding.sendBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if (binding.edComment.getText().toString().trim().isEmpty()){
                   Toast.makeText(context, "empty text is not allowed", Toast.LENGTH_SHORT).show();
               }else {
                   sendComment();
               }

           }
       });

       binding.attachmentBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               showBottomSheet();
           }
       });

       return binding.getRoot();
    }

    private void showBottomSheet() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);

        View view = LayoutInflater.from(context).inflate(R.layout.bottomsheet_attachments, null , false);

        bottomSheetDialog.setContentView(view);

        bottomSheetDialog.show();

        view.findViewById(R.id.ln_gallary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 0);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getData() != null){

            Uri imageUri = data.getData();

            try {
                Common.IMAGE_BITMAP = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {
                   list.add(new CommentModel(getUserName(context),
                           getFullName(context),
                           getProfilePic(context),
                           getTimeDate(),
                           "",
                           "IMAGE",
                           imageUri.toString(),
                           "UNKNOWN"));

                   adapter.notifyDataSetChanged();
               }
           }, 1000);

        }else {
            Toast.makeText(context, "error : reselect image again", Toast.LENGTH_SHORT).show();
        }

    }

    private void sendComment() {

        String content = binding.edComment.getText().toString().trim();
        binding.edComment.setText("");

        list.add(new CommentModel(getUserName(context),
                getFullName(context),
                getProfilePic(context),
                getTimeDate(),
                content,
                "TEXT",
                "",
                "unknown"));

        adapter.notifyDataSetChanged();

    }
}