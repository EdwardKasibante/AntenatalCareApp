package com.example.maternalinfolive.Activity;

import static com.example.maternalinfolive.Utils.HttpUrls.VIEW_POST;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maternalinfolive.Adapter.Info_Adapter;
import com.example.maternalinfolive.Lists.Info_List;
import com.example.maternalinfolive.R;
import com.example.maternalinfolive.Utils.HttpParse;
import com.example.maternalinfolive.Utils.StorageSense;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Dashboard extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    private Context context;
    private View notificationBadge;
    private int count =0;
    private TextView show_name;
    SharedPreferences sharedPreferences;
    private int type;
    private HashMap<String, String> ResultHash = new HashMap<>();
    private FloatingActionButton floatingActionButton;
    HttpParse httpParse = new HttpParse();
    String finalResult;
    HashMap<String,String> hashMap = new HashMap<>();
    java.util.List<Info_List> List;
    private Info_Adapter adapter;
    SharedPreferences.Editor editor;
    private RecyclerView recyclerView;
    private LinearLayout admin_view_chat,client_view_chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        show_name= findViewById(R.id.show_name);
        admin_view_chat= findViewById(R.id.admin_view_chat);
        client_view_chat= findViewById(R.id.client_view_chat);
        sharedPreferences=getSharedPreferences(StorageSense.onHealthSense(),MODE_PRIVATE);

        show_name.setText(sharedPreferences.getString("last_name",null) + " "+ sharedPreferences.getString("first_name",null));

        List = new ArrayList<>();
        adapter = new Info_Adapter(Dashboard.this, List);

hide_add_btn();

        recyclerView =findViewById(R.id.info_recycle);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(Dashboard.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        view_all_posts();


    }

    public void open_all_updates(View view) {
        startActivity(new Intent(Dashboard.this,ViewInformation.class));
    }

    private void hide_add_btn(){
        SharedPreferences sharedPreferences = getSharedPreferences(StorageSense.onHealthSense(), Context.MODE_PRIVATE);
        type= Integer.parseInt(sharedPreferences.getString("user_role",null));
        switch (type){
            case 1:
                client_view_chat.setVisibility(View.VISIBLE);
                admin_view_chat.setVisibility(View.GONE);
                break;
            default:
                client_view_chat.setVisibility(View.GONE);
                admin_view_chat.setVisibility(View.VISIBLE);
                break;
        }

    }

    public void view_all_posts(){
        final ProgressDialog dialog = new ProgressDialog(Dashboard.this);
        dialog.setMessage("Fetching latest updates.......");
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
                                if (i<5){
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
                                }
                                //adding the product to product list
                            }
                            //creating adapter object and setting it to recyclerview
                        } catch (Exception e) {
                            Toast.makeText(Dashboard.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(Dashboard.this, ""+string1, Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(ViewInformation.this,Dashboard.class));
//                            finish();
                        dialog.dismiss();

                    }
                }catch (Exception e){
                    Toast.makeText(Dashboard.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            protected String doInBackground(String... params) {
                finalResult = httpParse.ImageHttpRequest(VIEW_POST, ResultHash);
                return finalResult;
            }
        }
        AsyncTaskUploadClass dataUploadClass = new AsyncTaskUploadClass();
        dataUploadClass.execute();
    }

    public void openChats(View view) {
        startActivity(new Intent(Dashboard.this,ChatActivity.class));
    }



    public void view_users(View view) {
        startActivity(new Intent( Dashboard.this,ViewClient.class));
    }

    public void openTracker() {
        startActivity(new Intent( Dashboard.this,TrackerActivity.class));
    }

    public void see_more(View view) {
        String items[]={"Pregnancy Tracer", "Sign Out"};
        AlertDialog.Builder builder=new AlertDialog.Builder(Dashboard.this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                openTracker();
                                break;
                            case 1:
                              logout_user();
                                break;
                            default:

                        }

                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    public void logout_user() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this)
                .setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setTitle("Exit Confirmation")
                .setNegativeButton("No Keep On", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Yes, Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor pref = getSharedPreferences(StorageSense.onHealthSense(), 0).edit();
                        pref.clear();
                        pref.commit();
                        startActivity(new Intent(Dashboard.this, SplashScreen.class));
                        finish();
                    }
                });
        AlertDialog dialog= builder.create();
        dialog.show();
    }

    @Override
    protected void onRestart() {
        recreate();
        super.onRestart();
    }
}