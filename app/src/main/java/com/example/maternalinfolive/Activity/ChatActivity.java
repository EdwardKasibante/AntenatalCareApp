package com.example.maternalinfolive.Activity;

import static com.example.maternalinfolive.Utils.HttpUrls.SAVE_MESSAGE_TO_SINGLE_USER;
import static com.example.maternalinfolive.Utils.HttpUrls.URL_FETCH_MESSAGES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.widget.PullRefreshLayout;
import com.example.maternalinfolive.Adapter.Chat_Adapter;
import com.example.maternalinfolive.Lists.Chat_Modal;
import com.example.maternalinfolive.R;
import com.example.maternalinfolive.Utils.HttpParse;
import com.example.maternalinfolive.Utils.StorageSense;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayout progress;
    java.util.List<Chat_Modal> List;
    private Chat_Adapter adapter;
    private ImageView mesg_img,load_img,send_message_btn;
    private Uri imageUri;
    private static final int MY_CAMERA_PERMISSION_CODE = 12;
    private LinearLayout image_lay;
    private TextView msg;
    private int count=0;
    private PullRefreshLayout refreshLayout;
    HashMap<String,String> hashMap = new HashMap<>();
    String finalResult;
    HttpParse httpParse = new HttpParse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView =findViewById(R.id.message_recycle);
        mesg_img =findViewById(R.id.selected_img);
        load_img =findViewById(R.id.load_img);
        image_lay =findViewById(R.id.image_lay);
        send_message_btn =findViewById(R.id.send_msg_btn);
        refreshLayout=findViewById(R.id.refresh);
        msg=findViewById(R.id.msg);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));

        List = new ArrayList<>();
        adapter = new Chat_Adapter(List, ChatActivity.this);

        try {
            fetch_messages();
        }catch (Exception e){

        }

        try {
            refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshLayout.setRefreshing(true);
                    refreshLayout.setDurations(2000,2000);
                    List.clear();
                    fetch_messages();

                }
            });

        }catch (Exception e){

        }
//        load_img.setOnClickListener(v -> getMessageImage());

        send_message_btn.setOnClickListener(v -> {
           if (msg.getText().toString().trim().length() < 2){
                Toast.makeText(this, "Message Body is empty", Toast.LENGTH_SHORT).show();
            }
            else{
                uploadWithImage();
            }
        });

    }

    public  void content(){
        count++;
        refresh(1000);
       fetch_messages();
    }

    private void refresh(int seconds) {
        final Handler handler=new Handler();
        final  Runnable runnable=new Runnable() {
            @Override
            public void run() {
                content();
            }
        };
        handler.postDelayed(runnable,seconds);
    }

    private void fetch_messages() {
//        List.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URL_FETCH_MESSAGES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equalsIgnoreCase("empty")) {
                    Toast.makeText(ChatActivity.this, "Empty Message List", Toast.LENGTH_SHORT).show();
                }
                else {
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(response);

                        JSONObject jsonObject;

                        for(int i=0; i<jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);

                            List.add(new Chat_Modal(jsonObject.getString("message"),
                                    jsonObject.getString("sender"),
                                    jsonObject.getString("receiver"),
                                    jsonObject.getString("id"),
                                    jsonObject.getString("date_sent"),
                                    jsonObject.getString("file_path")
                            ));
                        }
                        recyclerView.setAdapter(adapter);
                        refreshLayout.setRefreshing(false);
                    }
                    catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        }, error -> {

            Toast.makeText(ChatActivity .this, error.getMessage(), Toast.LENGTH_LONG).show();
        }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences(StorageSense.onHealthSense(), Context.MODE_PRIVATE);
                String user_id = sharedPreferences.getString("user_id", null);
                params.put("user_id",user_id);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ChatActivity.this);
        requestQueue.add(request);
    }

    private void getMessageImage() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
            }
            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(ChatActivity.this);

        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK ) {
                Uri resultUri = result.getUri();
                mesg_img.setImageURI(resultUri);
                imageUri = resultUri;
                image_lay.setVisibility(View.VISIBLE);
            }
            else{
                image_lay.setVisibility(View.GONE);
            }
        }
    }

    public void cancenImage(View view) {
        mesg_img.setImageURI(null);
        imageUri = null;
        image_lay.setVisibility(View.GONE);
    }
    public void uploadWithImage(){
        try {
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.show();
//            progressDialog.setCancelable(true);
//            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
//            final StorageReference filepath = storageReference.child("Messages").child("Attachment").child(""+imageUri.getLastPathSegment());
//            filepath.putFile(imageUri).addOnSuccessListener(taskSnapshot -> filepath.getDownloadUrl().addOnSuccessListener(uri -> {
//                progressDialog.dismiss();
                SharedPreferences sharedPreferences = getSharedPreferences(StorageSense.onHealthSense(), Context.MODE_PRIVATE);
                String user_id = sharedPreferences.getString("user_id", null);

//                send_message_now(String.valueOf(uri),msg.getText().toString());
                new_save_message(user_id,msg.getText().toString(),String.valueOf("empty"));
//                Toast.makeText(ChatActivity.this, "Uploaded ", Toast.LENGTH_SHORT).show();
//            }).addOnFailureListener(e -> {
//                progressDialog.dismiss();
//                Toast.makeText(ChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//            })).addOnProgressListener(taskSnapshot -> {
//                int progress= (int)((taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount())*100);
//                progressDialog.setMessage("Uploading Image "+progress+"%");
//            }).addOnFailureListener(e -> {
//                progressDialog.dismiss();
//                Toast.makeText(ChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//            });

        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void send_message_now(String file,String msgs) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending Message......");
        progressDialog.show();
        progressDialog.setCancelable(true);

        StringRequest request = new StringRequest(Request.Method.POST, SAVE_MESSAGE_TO_SINGLE_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response.equalsIgnoreCase("Saved")){
                    Toast.makeText(ChatActivity.this, "Message Successfully", Toast.LENGTH_LONG).show();
                    msg.setText("");
                    imageUri=null;
                    mesg_img.setImageURI(null);
                    image_lay.setVisibility(View.GONE);
                    recyclerView.removeAllViews();
                    List.clear();
                    fetch_messages();


                }
                else{
                    Toast.makeText(ChatActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                }
            }
        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(ChatActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
        }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences(StorageSense.onHealthSense(), Context.MODE_PRIVATE);
                String user_id = sharedPreferences.getString("user_id", null);
                params.put("user_id",user_id);
                params.put("message",msgs);
                params.put("file_path",file);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ChatActivity.this);
        requestQueue.add(request);
    }

    public void new_save_message(String user,String msgs,String file){
        class AsyncTaskUploadClass extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String string1) {
                super.onPostExecute(string1);
                // Dismiss the progress dialog after done uploading.
                if(string1.equals("Saved") || string1.contains("Saved")){
                    Toast.makeText(ChatActivity.this, "Message Successfully", Toast.LENGTH_LONG).show();
                    msg.setText("");
                    imageUri=null;
                    mesg_img.setImageURI(null);
                    image_lay.setVisibility(View.GONE);
                    recyclerView.removeAllViews();
                    List.clear();
                    fetch_messages();
                }
                else{
                    Toast.makeText(ChatActivity.this, ""+string1, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            protected String doInBackground(String... params) {
                hashMap.put("user_id", params[0]);
                hashMap.put("message", params[1]);
                hashMap.put("file_path", params[2]);
                finalResult = httpParse.ImageHttpRequest(SAVE_MESSAGE_TO_SINGLE_USER,hashMap);
                return finalResult;
            }
        }
        AsyncTaskUploadClass userRegisterFunctionClass = new AsyncTaskUploadClass();
        userRegisterFunctionClass.execute(user,msgs,file);
    }

    public void onBack(View view) {
        onBackPressed();
    }
}