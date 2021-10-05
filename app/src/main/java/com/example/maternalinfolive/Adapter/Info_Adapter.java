package com.example.maternalinfolive.Adapter;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.maternalinfolive.Activity.InfoDetailsActivity;
import com.example.maternalinfolive.Lists.Info_List;
import com.example.maternalinfolive.R;

import java.util.ArrayList;
import java.util.List;


public class Info_Adapter extends RecyclerView.Adapter<Info_Adapter.DataViewHolder> implements Filterable {

    private Context mCtx;
    String status;
    private int doll;
    List<Info_List> mData;
    List<Info_List> mDataFiltered;

    public Info_Adapter(Context mCtx, List<Info_List> mData) {
        this.mCtx = mCtx;
        this.mData = mData;
        this.mDataFiltered = mData;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.row_posts, parent,false);
//        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        view.setLayoutParams(layoutParams);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DataViewHolder holder, final int position) {
        final Info_List info = mDataFiltered.get(position);
        holder.body.setText(""+info.getBody());
        holder.subject.setText(""+info.getSubject());
        holder.user_type.setText(""+info.getPoster_type().toUpperCase());
        holder.date.setText(""+info.getDate_posted());
        holder.userfull_time.setText("**"+info.getPost_category_name()+"**");
        if (info.getPoster_type().equals("doctor")){
            holder.name.setText(""+info.getPoster_fname()+" "+info.getPoster_lname());
        }
        else {
            holder.name.setText(""+info.getPoster_fname()+" "+info.getPoster_lname());
        }

        holder.car_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mCtx, InfoDetailsActivity.class);
                intent.putExtra("id", ""+info.getId());
                intent.putExtra("subject", info.getSubject());
                intent.putExtra("body", info.getBody());
                intent.putExtra("poster_Fname", info.getPoster_fname());
                intent.putExtra("poster_lname", info.getPoster_lname());
                intent.putExtra("poster_type", info.getPoster_type());
                intent.putExtra("poster_phone", info.getPoster_phone());
                intent.putExtra("date_posted", info.getDate_posted());
                intent.putExtra("trimester", info.getTrisemster_name());
                intent.putExtra("category", info.getPost_category_name());
                mCtx.startActivity(intent);
            }
        });
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mCtx, InfoDetailsActivity.class);
                intent.putExtra("id", ""+info.getId());
                intent.putExtra("subject", info.getSubject());
                intent.putExtra("body", info.getBody());
                intent.putExtra("poster_Fname", info.getPoster_fname());
                intent.putExtra("poster_lname", info.getPoster_lname());
                intent.putExtra("poster_type", info.getPoster_type());
                intent.putExtra("poster_phone", info.getPoster_phone());
                intent.putExtra("date_posted", info.getDate_posted());
                intent.putExtra("trimester", info.getTrisemster_name());
                intent.putExtra("category", info.getPost_category_name());
                mCtx.startActivity(intent);
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
                List<Info_List> FilteredList=new ArrayList<>();
                for (Info_List row: mData){
                    if (row.getSubject().toString().contains(key) || String.valueOf(row.getBody()).contains(key) || row.getPoster_lname().toUpperCase().contains(key)|| row.getPoster_fname().toLowerCase().contains(key) || String.valueOf(row.getPoster_type()).contains(key)){
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

            mDataFiltered=(List<Info_List>)results.values;
//            mData.clear();
//            mData.addAll((Collection<? extends Home_Objects>) results.values);
            notifyDataSetChanged();
        }
    };
    class DataViewHolder extends RecyclerView.ViewHolder {
        private CardView car_here;
        private TextView body,subject,date,name,user_type,userfull_time,more;
        public DataViewHolder(View itemView) {
            super(itemView);
            car_here=itemView.findViewById(R.id.car_here);
            body=itemView.findViewById(R.id.body);
            subject=itemView.findViewById(R.id.subject);
            date=itemView.findViewById(R.id.date);
            name=itemView.findViewById(R.id.name);
            user_type=itemView.findViewById(R.id.user_type);
            userfull_time=itemView.findViewById(R.id.userfull_time);
            more=itemView.findViewById(R.id.more);


        }
    }

}