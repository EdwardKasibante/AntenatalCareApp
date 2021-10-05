package com.example.maternalinfolive.Activity;

import static com.example.maternalinfolive.Utils.HttpUrls.RECOMMENDATION_PATH;
import static com.example.maternalinfolive.Utils.HttpUrls.VIEW_POST;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maternalinfolive.Adapter.Info_Adapter;
import com.example.maternalinfolive.Lists.Info_List;
import com.example.maternalinfolive.R;
import com.example.maternalinfolive.Utils.HttpParse;
import com.example.maternalinfolive.Utils.StorageSense;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RecommendedActivity extends AppCompatActivity {
    private int type;
    private HashMap<String, String> ResultHash = new HashMap<>();
    private FloatingActionButton floatingActionButton;
    HttpParse httpParse = new HttpParse();
    String finalResult;
    HashMap<String,String> hashMap = new HashMap<>();
    java.util.List<Info_List> List;
    private Info_Adapter adapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended);
        floatingActionButton=findViewById(R.id.add_fab);
        hide_add_btn();
        List = new ArrayList<>();
        adapter = new Info_Adapter(RecommendedActivity.this, List);



        recyclerView =findViewById(R.id.info_recycle);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(RecommendedActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        view_all_posts();

    }

    private void hide_add_btn(){
        //        saloon_name.setText(String.valueOf(sharedPreferences.getString("saloon_name",null)));
        SharedPreferences sharedPreferences = getSharedPreferences(StorageSense.onHealthSense(), Context.MODE_PRIVATE);
        type= Integer.parseInt(sharedPreferences.getString("user_role",null));
        switch (type){
            case 1:
                floatingActionButton.setVisibility(View.GONE);
                break;
            default:
                floatingActionButton.setVisibility(View.VISIBLE);

                break;
        }

    }

    public void view_all_posts(){
        final ProgressDialog dialog = new ProgressDialog(RecommendedActivity.this);
        dialog.setMessage("Finding Information.......");
        dialog.show();
        dialog.setCancelable(true);
        class AsyncTaskUploadClass extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.setCancelable(true);
                dialog.show();
            }
            @Override
            protected void onPostExecute(String string1) {
                super.onPostExecute(string1);
                // Dismiss the progress dialog after done uploading.
                dialog.dismiss();


                try {
                    if(string1.equals("found") || string1.contains("found")){
                        String final_results=string1.replace("found","");
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(final_results);
                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {
                                //getting product object from json array
                                JSONObject jsonObject = array.getJSONObject(i);
                                List.add(new Info_List(
                                        jsonObject.getInt("id"),
                                        jsonObject.getString("subject"),
                                        jsonObject.getString("body"),
                                        jsonObject.getString("image"),
                                        jsonObject.getString("posted_on"),
                                        jsonObject.getInt("post_category_id"),
                                        jsonObject.getString("post_category_name"),
                                        jsonObject.getString("name"),
                                        jsonObject.getInt("profile_id"),
                                        jsonObject.getString("first_name"),
                                        jsonObject.getString("last_name"),
                                        jsonObject.getString("phone_no"),
                                        jsonObject.getInt("Trisemster_id"),
                                        jsonObject.getString("Trimester_Name")
                                ));
                                recyclerView.setAdapter(adapter);
                                dialog.dismiss();
                                //adding the product to product list
                            }
                            //creating adapter object and setting it to recyclerview
                        } catch (Exception e) {
                            Toast.makeText(RecommendedActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(RecommendedActivity.this, ""+string1, Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(ViewInformation.this,Dashboard.class));
//                            finish();
                        dialog.dismiss();

                    }
                }catch (Exception e){
                    Toast.makeText(RecommendedActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            protected String doInBackground(String... params) {
                ResultHash.put("time",getIntent().getStringExtra("time"));
                finalResult = httpParse.ImageHttpRequest(RECOMMENDATION_PATH, ResultHash);
                return finalResult;
            }
        }
        AsyncTaskUploadClass dataUploadClass = new AsyncTaskUploadClass();
        dataUploadClass.execute();
    }
    public void add_staff_fab(View view) {
        startActivity(new Intent(RecommendedActivity.this,NewPostActivity.class));
    }

    public void onBack(View view) {
        onBackPressed();
    }
}