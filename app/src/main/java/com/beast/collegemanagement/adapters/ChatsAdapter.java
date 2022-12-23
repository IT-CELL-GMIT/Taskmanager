package com.beast.collegemanagement.adapters;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.collegemanagement.Common;
import com.beast.collegemanagement.R;
import com.beast.collegemanagement.models.ChatsModel;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatHolder> {

    List<ChatsModel> list;
    Context context;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String userName;

    public ChatsAdapter(List<ChatsModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setList(List<ChatsModel> list){

        this.list = list;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ChatsAdapter.ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==0){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new ChatHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_itm_right, parent, false);
            return new ChatHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsAdapter.ChatHolder holder, @SuppressLint("RecyclerView") int position) {

        sp = context.getSharedPreferences("FILE_NAME", Context.MODE_PRIVATE);
        editor = sp.edit();

        userName = sp.getString("userName", null);

        if (list.get(position).getType().equals("IMAGE")){

            holder.textMsg.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
//            String path = Environment.getExternalStoragePublicDirectory(Environment.getExternalStorageState()).getAbsolutePath();
//            Glide.with(context).load(list.get(position).getUrl()).into(holder.image);
            String urlPath = "Images/" + list.get(position).getName();
            String filePath = Environment.getExternalStorageDirectory() + "/" + Common.GetDownloadEnvironment() +
                    "/" + Common.GetMediaPath() + urlPath;
            File file = new File(filePath);
            if (file.exists()){
                holder.blurImage.setVisibility(View.GONE);
                holder.downloadBtn.setVisibility(View.GONE);
                holder.image.setVisibility(View.VISIBLE);
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                holder.image.setImageBitmap(bitmap);
            }else {
                holder.image.setVisibility(View.GONE);
                holder.blurImage.setVisibility(View.VISIBLE);
                holder.downloadBtn.setVisibility(View.VISIBLE);

            }
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(file.exists()){
                        Common.IMAGE_BITMAP = BitmapFactory.decodeFile(filePath);
//                        context.startActivity(new Intent(context, ViewImageActivity.class));
                    }else {
                        holder.blurImage.setVisibility(View.GONE);
                        holder.downloadBtn.setVisibility(View.GONE);
                        holder.image.setVisibility(View.VISIBLE);
                        Glide.with(context).load(list.get(position).getUrl()).into(holder.image);
                        downloadFile(list.get(position).getUrl(), urlPath);
                    }
                }
            });
//            downloadFile(list.get(position).getUrl(), urlPath);

        }

        if (list.get(position).getType().equals("TEXT")){

            holder.textMsg.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.GONE);
            holder.textMsg.setText(list.get(position).getTextMsg());

        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ChatHolder extends RecyclerView.ViewHolder {

        TextView textMsg, downloadSize;
        CardView imageView;
        ImageView image;
        LinearLayout downloadBtn;
        ImageView blurImage;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);

            textMsg = itemView.findViewById(R.id.tv_msg);
            imageView = itemView.findViewById(R.id.card_image);
            image = itemView.findViewById(R.id.msgSendImage);
            downloadBtn = itemView.findViewById(R.id.llDownalod);
            blurImage = itemView.findViewById(R.id.blurImage);
            downloadSize = itemView.findViewById(R.id.downlaod_txt);

        }
    }

    @Override
    public int getItemViewType(int position) {

        sp = context.getSharedPreferences("FILE_NAME", Context.MODE_PRIVATE);
        String userName = sp.getString("userName", null);

        if (list.get(position).getSender().equals(userName)){
            return 1;
        }else {
            return 0;
        }

    }

    void downloadFile(String url, String fileName){

        Uri uri2 = Uri.parse(url);

        DownloadManager.Request dRequest = new DownloadManager.Request(uri2);
        dRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        dRequest.setTitle("Download");
        dRequest.setDescription("........");
        dRequest.allowScanningByMediaScanner();
        dRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        dRequest.setDestinationInExternalPublicDir(Common.GetDownloadEnvironment(), Common.GetMediaPath()  + fileName);
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(dRequest);


    }

}
