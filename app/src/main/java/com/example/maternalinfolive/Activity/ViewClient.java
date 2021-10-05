package com.example.maternalinfolive.Activity;

import static com.example.maternalinfolive.Utils.HttpUrls.VIEW_USERS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.maternalinfolive.Adapter.User_Adapter;
import com.example.maternalinfolive.Lists.User_List;
import com.example.maternalinfolive.R;
import com.example.maternalinfolive.Utils.HttpParse;
import com.example.maternalinfolive.Utils.StorageSense;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewClient extends AppCompatActivity {
    private int  type;
    private FloatingActionButton floatingActionButton;
    HttpParse httpParse = new HttpParse();
    String finalResult;
    HashMap<String,String> hashMap = new HashMap<>();
    java.util.List<User_List> List;
    private User_Adapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_client);
        List = new ArrayList<>();
        adapter = new User_Adapter(ViewClient.this, List);

        recyclerView =findViewById(R.id.user_recycle);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ViewClient.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        return_all_users();

    }



    private void return_all_users() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Contacting server for the information.... please wait");
        progressDialog.show();
        progressDialog.setCancelable(true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, VIEW_USERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {
                                //getting product object from json array
                                JSONObject jsonObject = array.getJSONObject(i);


                                   List.add(new User_List(
                                           jsonObject.getInt("id"),
                                           jsonObject.getString("first_name"),
                                           jsonObject.getString("last_name"),
                                           jsonObject.getString("phone_no"),
                                           jsonObject.getString("gender"),
                                           jsonObject.getString("gender"),
                                           jsonObject.getString("date_created"),
                                           jsonObject.getInt("role_id")
                                   ));

                                recyclerView.setAdapter(adapter);
                                progressDialog.dismiss();


                                //adding the product to product list
//                                Toast.makeText(JobActivity.this, ""+jsonObject.getString("name"), Toast.LENGTH_SHORT).show();
                            }
                            //creating adapter object and setting it to recyclerview


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        String e= error.getMessage();
                        if (e == null){
                            Toast.makeText(ViewClient.this, "Network Error. Please check your connection", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(ViewClient.this, "Error: "+e, Toast.LENGTH_SHORT).show();
                        }
//                        startActivity(new Intent(ViewClient.this,DashboardActivity.class));
//                        finish();
                    }
                });
        //adding our stringrequest to queue
        Volley.newRequestQueue(ViewClient.this).add(stringRequest);
    }

    public void onBack(View view) {
        onBackPressed();
    }

    @Override
    protected void onRestart() {
        recreate();
        super.onRestart();
    }
}