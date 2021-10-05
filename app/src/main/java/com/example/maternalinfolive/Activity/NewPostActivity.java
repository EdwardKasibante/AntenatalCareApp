package com.example.maternalinfolive.Activity;

import static com.example.maternalinfolive.Utils.HttpUrls.NEW_POST;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.maternalinfolive.R;
import com.example.maternalinfolive.Utils.HttpParse;
import com.example.maternalinfolive.Utils.StorageSense;

import java.util.ArrayList;
import java.util.HashMap;



public class NewPostActivity extends AppCompatActivity {
    private Spinner category_spinner;
    private RadioButton first,second,third,anytime;
    private ArrayList<String> category;
    private Integer time=0;
    private Button save;
    private EditText subject,body;

    private HashMap<String, String> ResultHash = new HashMap<>();
    private HttpParse httpParse = new HttpParse();
    private String finalResult;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        first=findViewById(R.id.first);
        second=findViewById(R.id.second);
        third=findViewById(R.id.third);
        anytime=findViewById(R.id.anytime);
        save=findViewById(R.id.btn_save);
        subject=findViewById(R.id.subject);
        body=findViewById(R.id.body);
        category_spinner=findViewById(R.id.category_spinner);
        category=new ArrayList<>();
        category.add("Pick Category From List");
        category.add("Food and Nutrition");
        category.add("Sexual Affairs");
        category.add("Health Information");
        category_spinner.setAdapter(new ArrayAdapter<>(NewPostActivity.this,
                android.R.layout.simple_spinner_dropdown_item,category
        ));
        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time=1;
                if(second.isChecked()){
                    second.setChecked(false);
                }
                if(third.isChecked()){
                    third.setChecked(false);
                }
                if(anytime.isChecked()){
                    anytime.setChecked(false);
                }
            }
        });

        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time=2;
                if(first.isChecked()){
                    first.setChecked(false);
                }
                if(third.isChecked()){
                    third.setChecked(false);
                }
                if(anytime.isChecked()){
                    anytime.setChecked(false);
                }
            }
        });

        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time=3;
                if(second.isChecked()){
                    second.setChecked(false);
                }
                if(first.isChecked()){
                    first.setChecked(false);
                }
                if(anytime.isChecked()){
                    anytime.setChecked(false);
                }
            }
        });

        anytime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time=4;
                if(second.isChecked()){
                    second.setChecked(false);
                }
                if(third.isChecked()){
                    third.setChecked(false);
                }
                if(first.isChecked()){
                    first.setChecked(false);
                }
            }
        });

        save.setOnClickListener(v -> {
            final String cat_selected = (String) category_spinner.getSelectedItem();
            if (TextUtils.isEmpty(subject.getText().toString().trim()) || subject.getText().toString().trim().length() < 5){
                Toast.makeText(NewPostActivity.this, "Subjects is not sufficient", Toast.LENGTH_SHORT).show();
            }
            else if (TextUtils.isEmpty(subject.getText().toString().trim()) || subject.getText().toString().trim().length() < 5){
                Toast.makeText(NewPostActivity.this, "Body is not not sufficient. ", Toast.LENGTH_SHORT).show();
            }
            else if (time == 0){
                Toast.makeText(NewPostActivity.this, "Select The post useful time", Toast.LENGTH_SHORT).show();
            }

            else  if (cat_selected == "Pick Category From List" || cat_selected.isEmpty() || cat_selected==null){
                Toast.makeText(NewPostActivity.this, "Select Post Category", Toast.LENGTH_SHORT).show();
            }
            else {
                int cat=0;
                SharedPreferences sharedPreferences = getSharedPreferences(StorageSense.onHealthSense(), Context.MODE_PRIVATE);
                String user= sharedPreferences.getString("user_id",null);
                switch (cat_selected){
                    case "Food and Nutrition":
                        cat=1;
                        break;
                    case "Sexual Affairs":
                        cat=2;
                        break;
                    case "Health Information":
                        cat=3;
                        break;
                    default:
                        cat=3;
                }
     create_account_now(
             subject.getText().toString().trim(),
             body.getText().toString().trim(),
             String.valueOf(cat),
             user,
             String.valueOf(time)
     );
            }
        });
    }

    public void create_account_now(String subjects,String bodys, String post_category_id , String posted_by , String Trisemster_id){
        final ProgressDialog dialog = new ProgressDialog(NewPostActivity.this);
        dialog.setMessage("Saving Post.......");
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
                    if(string1.equals("added") || string1.contains("added")){
                        String final_results=string1.replace("added","");
                        Toast.makeText(NewPostActivity.this, "Post Added Successfully", Toast.LENGTH_LONG).show();
                        category_spinner.setSelection(0);
                        subject.setText("");
                        body.setText("");
                        first.setChecked(false);
                        second.setChecked(false);
                        third.setChecked(false);
                        anytime.setChecked(false);
                        recreate();

                    }
                    else{
                        Toast.makeText(NewPostActivity.this, ""+string1, Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(NewPostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            protected String doInBackground(String... params) {
                ResultHash.put("subject", params[0]);
                ResultHash.put("body", params[1]);
                ResultHash.put("category", params[2]);
                ResultHash.put("user", params[3]);
                ResultHash.put("time", params[4]);
                finalResult = httpParse.ImageHttpRequest(NEW_POST,ResultHash);
                return finalResult;
            }
        }
        AsyncTaskUploadClass dataUploadClass = new AsyncTaskUploadClass();
        dataUploadClass.execute(subjects,bodys,post_category_id,posted_by,Trisemster_id);
    }

    public void onBack(View view) {
        onBackPressed();
    }
}