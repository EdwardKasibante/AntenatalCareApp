package com.example.maternalinfolive.Adapter;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.maternalinfolive.Activity.InfoDetailsActivity;
import com.example.maternalinfolive.Activity.PersonalChat;
import com.example.maternalinfolive.Activity.UserActivity;
import com.example.maternalinfolive.Lists.User_List;
import com.example.maternalinfolive.R;
import com.example.maternalinfolive.Utils.StorageSense;

import java.util.ArrayList;
import java.util.List;


public class User_Adapter extends RecyclerView.Adapter<User_Adapter.DataViewHolder> implements Filterable {

    private Context mCtx;
    String status;
    private int doll;
    int type,role;
    List<User_List> mData;
    List<User_List> mDataFiltered;

    public User_Adapter(Context mCtx, List<User_List> mData) {
        this.mCtx = mCtx;
        this.mData = mData;
        this.mDataFiltered = mData;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.row_users, parent,false);
//        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        view.setLayoutParams(layoutParams);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DataViewHolder holder, final int position) {
        final User_List user = mDataFiltered.get(position);
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(StorageSense.onHealthSense(), Context.MODE_PRIVATE);
        type= Integer.parseInt(sharedPreferences.getString("user_id",null));
        role= Integer.parseInt(sharedPreferences.getString("user_role",null));
       holder.name.setText(""+user.getFname()+" "+user.getLname());
       holder.date.setText(""+user.getDate());
       holder.gender.setText(user.getGender());
       holder.address.setText(""+user.getPhone());

        holder.car_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                switch (user.getType()) {
                    case 2:
                        break;
                    default:
                        if (role == 2) {
                        Intent intent = new Intent(mCtx, UserActivity.class);
                        intent.putExtra("id", "" + user.getId());
                        intent.putExtra("fname", user.getFname());
                        intent.putExtra("lname", user.getLname());
                        intent.putExtra("date", user.getDate());
                        intent.putExtra("role_id", "" + user.getType());
                        intent.putExtra("gender", user.getGender());
                        intent.putExtra("phone", user.getPhone());
                        mCtx.startActivity(intent);
                }

                }
            }
        });
       switch (user.getType()){
           case 1:
               holder.type.setText("Client");
               break;
           case 2:
               holder.type.setText("Admin");
               break;
           case 3:
               holder.type.setText("Doctor");
               break;
           default:
       }

        //        saloon_name.setText(String.valueOf(sharedPreferences.getString("saloon_name",null)));

        if (user.getId() == type){
            holder.call.setVisibility(View.GONE);
            holder.chat.setVisibility(View.GONE);

        }


       holder.call.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                       if (!user.getPhone().toString().isEmpty()){
                           Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "" +user.getPhone()));
                           if (ContextCompat.checkSelfPermission(mCtx, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                               ActivityCompat.requestPermissions((Activity) mCtx, new String[]{Manifest.permission.CALL_PHONE}, 100);
                           } else {
                              mCtx.startActivity(intent);
                           }
                       }
                   }
       });
       holder.chat.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mCtx.startActivity(new Intent(mCtx, PersonalChat.class)
               .putExtra("destination",""+user.getId()));
           }
       });
    }

    @Override
    public int getItemCount() {
        return mDataFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return myFilterData;
    }


    private Filter myFilterData = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String key=constraint.toString();
            if (key.isEmpty()){
                mDataFiltered=mData;
            }
            else{
                List<User_List> FilteredList=new ArrayList<>();
                for (User_List row: mData){
                    if (row.getFname().toString().contains(key) || String.valueOf(row.getType()).contains(key) || row.getLname().toUpperCase().contains(key)|| row.getFname().toLowerCase().contains(key) || String.valueOf(row.getGender()).contains(key)){
                        FilteredList.add(row);
                    }
                }

                mDataFiltered=FilteredList;
            }
            FilterResults  filterResults=new FilterResults();
            filterResults.values=mDataFiltered;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mDataFiltered=(List<User_List>)results.values;
//            mData.clear();
//            mData.addAll((Collection<? extends Home_Objects>) results.values);
            notifyDataSetChanged();
        }
    };
    class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView name,gender,phone,date,address,type;
        private CardView car_here;
        private Button call,chat;
        public DataViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            type=itemView.findViewById(R.id.type);
            gender=itemView.findViewById(R.id.gender);
            address=itemView.findViewById(R.id.address);
            call=itemView.findViewById(R.id.call_phone);
            date=itemView.findViewById(R.id.date);
            car_here=itemView.findViewById(R.id.car_here);
            chat=itemView.findViewById(R.id.chat);
        }
    }

}