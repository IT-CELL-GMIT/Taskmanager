package com.beast.collegemanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.collegemanagement.Common;
import com.beast.collegemanagement.R;
import com.beast.collegemanagement.models.FilesModel;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.FilesHolder> {

    List<FilesModel> list;
    Context context;

    String profilePic,
            fullName,
            time,
            content,
            type,
            link;

    String todayTime, todayDate;
    String commentTime, commentDate;

    public FilesAdapter(List<FilesModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public FilesAdapter.FilesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_file_list, parent, false);

        return new FilesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilesAdapter.FilesHolder holder, int position) {

        profilePic = list.get(position).getProfilePic();
        fullName = list.get(position).getFullName();
        time = list.get(position).getTimeDate();
        content = list.get(position).getContent();
        type = list.get(position).getType();
        link = list.get(position).getLink();

        todayTime = Common.getTIme();
        todayDate = Common.getDate();

        Glide.with(context).load(profilePic).into(holder.profilePic);

        String [] splits = time.split("xxx");

        commentTime = splits[0];
        commentDate = splits[1];

        if (type.equalsIgnoreCase("IMAGE")){

            holder.pdfCard.setVisibility(View.GONE);
            holder.imageCard.setVisibility(View.VISIBLE);

//            holder.contentImg.setImageBitmap(Common.IMAGE_BITMAP);

            Glide.with(context).load(list.get(position).getLink()).into(holder.contentImg);
            holder.fullNameImg.setText(fullName);

            if (commentDate.equalsIgnoreCase(todayDate)){
                holder.timeImg.setText(commentTime);
            }else {
                holder.timeImg.setText(commentDate);
            }

        }else if (type.equalsIgnoreCase("PDF")){

            holder.imageCard.setVisibility(View.GONE);
            holder.pdfCard.setVisibility(View.VISIBLE);

            holder.fullNamePdf.setText(fullName);
            holder.pdfName.setText(content);

            if (commentDate.equalsIgnoreCase(todayDate)){
                holder.timePdf.setText(commentTime);
            }else {
                holder.timePdf.setText(commentDate);
            }

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FilesHolder extends RecyclerView.ViewHolder {

        CardView imageCard, pdfCard;
        TextView fullNameImg, fullNamePdf, timeImg, timePdf;
        ImageView contentImg;
        TextView pdfName;
        CircleImageView profilePic;

        public FilesHolder(@NonNull View itemView) {
            super(itemView);

            imageCard = itemView.findViewById(R.id.card_image);
            pdfCard = itemView.findViewById(R.id.card_pdf);
            fullNameImg = itemView.findViewById(R.id.fullNameImg);
            fullNamePdf = itemView.findViewById(R.id.fullNamePdf);
            timeImg = itemView.findViewById(R.id.timeImg);
            timePdf = itemView.findViewById(R.id.timePdf);
            contentImg = itemView.findViewById(R.id.contentImg);
            pdfName = itemView.findViewById(R.id.pdfName);
            profilePic = itemView.findViewById(R.id.profilePic);

        }
    }
}
