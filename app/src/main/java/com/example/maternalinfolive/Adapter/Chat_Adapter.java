package com.example.maternalinfolive.Adapter;

import static com.example.maternalinfolive.Utils.HttpUrls.MESSAGE_IMAGE_FILE_PATH;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.maternalinfolive.Lists.Chat_Modal;
import com.example.maternalinfolive.R;
import com.example.maternalinfolive.Utils.StorageSense;
import com.squareup.picasso.Picasso;

import java.util.List;


public class Chat_Adapter extends RecyclerView.Adapter<Chat_Adapter.MyViewHolder> {

    private List<Chat_Modal> chatList;
    private final Context context;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    String get_user_id;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView message;
        public TextView date;
        public LinearLayout open;
        public ImageView msg_img;
        private LinearLayout image_lay;
        public MyViewHolder(View view) {
            super(view);
            message = view.findViewById(R.id.message_txt);
            date = view.findViewById(R.id.msg_date);
            open = view.findViewById(R.id.now_here);
            image_lay = view.findViewById(R.id.image_lay);
            msg_img = view.findViewById(R.id.msg_img);
        }
    }


    public Chat_Adapter(List<Chat_Modal> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_out, parent, false);
        }
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_in, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Chat_Modal chat = chatList.get(position);
        preferences = context.getSharedPreferences(StorageSense.onHealthSense(),Context.MODE_PRIVATE);
        get_user_id = preferences.getString("user_id","");
        editor = preferences.edit();
        if(chat.getMessage().equals("")){
            holder.message.setVisibility(View.GONE);
            holder.date.setVisibility(View.VISIBLE);
        }
        else {
            holder.message.setVisibility(View.VISIBLE);
            holder.message.setText(chat.getMessage());
            holder.date.setText(chat.getDate_sent());

        }

        if (chat.getFile_path().equals("empty")){
            holder.image_lay.setVisibility(View.GONE);
        }
        else{
            holder.image_lay.setVisibility(View.VISIBLE);
            Picasso.with(context).load(MESSAGE_IMAGE_FILE_PATH+chat.getFile_path())
                    .into(holder.msg_img);
        }
         holder.open.setOnLongClickListener(new View.OnLongClickListener() {
             @Override
             public boolean onLongClick(View v) {
                 Spanned msg= Html.fromHtml("<b>"+chat.getMessage().toString()+"</b>");
                 AlertDialog.Builder builder=new AlertDialog.Builder(context)

                         .setTitle("Message Info")
                         .setMessage("Message Date: "+chat.getDate_sent()+"\n Body: "+msg+"\nSender: "+chat.getSender()+"\nReceiver: "+chat.getReceiver())
                         .setCancelable(false)
                         .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 dialog.dismiss();
                             }
                         }).setNeutralButton ("Hide", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         dialog.dismiss();
                     }
                 }).setNegativeButton("Copy to Clipboard", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                 ClipData data =ClipData.newPlainText("label",chat.getMessage());
                                 clipboardManager.setPrimaryClip(data);
                                 Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show();

                             }
                         });
                 AlertDialog dialog =builder.create();
                 dialog.show();

                 return false;
             }
         });

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
    @Override
    public int getItemViewType(int position) {
        preferences = context.getSharedPreferences(StorageSense.onHealthSense(),0);
        get_user_id = preferences.getString("user_id","");
        final Chat_Modal chat = chatList.get(position);
        if(chat.getReceiver().equals(get_user_id)){
            return MSG_TYPE_LEFT;
        }
        else
            return MSG_TYPE_RIGHT;
    }
}