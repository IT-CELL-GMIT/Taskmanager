package com.beast.collegemanagement.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.collegemanagement.R;
import com.beast.collegemanagement.models.FreeDriveModel;

import java.util.List;

public class FreeDriveAdapter extends RecyclerView.Adapter<FreeDriveAdapter.DriveHolder> {

    List<FreeDriveModel> list;
    Context context;
    String activityName;

    public FreeDriveAdapter(List<FreeDriveModel> list, Context context, String activityName) {
        this.list = list;
        this.context = context;
        this.activityName = activityName;
    }

    @NonNull
    @Override
    public FreeDriveAdapter.DriveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_freedrive_layout, parent, false);

        return new DriveHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FreeDriveAdapter.DriveHolder holder, int position) {

        holder.link.setText(list.get(position).getLink());
        String[] splits = list.get(position).getTimeDate().split("xxx");
        holder.timeDate.setText(splits[0] + "  " + splits[1]);

        if (activityName.equalsIgnoreCase("FreeDriveActivity")) {

            holder.copyBtn.setVisibility(View.VISIBLE);

            holder.fullLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Copied Text", list.get(position).getLink());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            });
        }else if (activityName.equalsIgnoreCase("NotesActivity")){
            holder.copyBtn.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DriveHolder extends RecyclerView.ViewHolder {

        TextView link, timeDate;
        LinearLayout fullLayout;
        ImageView copyBtn;

        public DriveHolder(@NonNull View itemView) {
            super(itemView);

            link = itemView.findViewById(R.id.tvLink);
            timeDate = itemView.findViewById(R.id.tvTimeDate);
            fullLayout = itemView.findViewById(R.id.fullLayout);
            copyBtn = itemView.findViewById(R.id.copyBtn);

        }
    }
}
