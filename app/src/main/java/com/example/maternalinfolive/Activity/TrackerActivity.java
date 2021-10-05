package com.example.maternalinfolive.Activity;

import static com.example.maternalinfolive.Utils.HttpUrls.TRACKER_PATH;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maternalinfolive.R;
import com.example.maternalinfolive.Utils.HttpParse;
import com.example.maternalinfolive.Utils.StorageSense;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class TrackerActivity extends AppCompatActivity {
    private TextView lnmp_date,lnmp_datetime,weeks,months,trimster,delivery;
    private DatePicker date_picker;
    private TextView selectDate;
    private EditText selected;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    String dateSelected;
    private Button save;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    String tv;

    private LinearLayout info_layout,set_layout;

    private HashMap<String, String> ResultHash = new HashMap<>();
    private HttpParse httpParse = new HttpParse();
    private String finalResult;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        lnmp_date=findViewById(R.id.lnmp_date);
        lnmp_datetime=findViewById(R.id.lnmp_datetime);
        weeks=findViewById(R.id.weeks);
        months=findViewById(R.id.months);
        trimster=findViewById(R.id.trimster);
        delivery=findViewById(R.id.delivery);
        date_picker=findViewById(R.id.date_picker);
        tv="";

        selectDate=findViewById(R.id.select);
        selected=findViewById(R.id.date_selected);
        save=findViewById(R.id.save);

        info_layout=findViewById(R.id.info_layout);
        set_layout=findViewById(R.id.set_layout);


        sharedPreferences = getSharedPreferences(StorageSense.onHealthSense(),MODE_PRIVATE);
        editor =sharedPreferences.edit();
       try {
           selected.setText(sharedPreferences.getString("lnmp_date",null));
       }catch (Exception e){
           Toast.makeText(TrackerActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
       }

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        TrackerActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
//                Toast.makeText(Profile.this, "DOB", Toast.LENGTH_SHORT).show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d("tag", "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                int currentYear=Calendar.getInstance().get(Calendar.YEAR);
                int currentMonth=Calendar.getInstance().get(Calendar.MONTH);
                int currentDay=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                if (year <= currentYear){
                    String  m;
                    String d;
                    if (month < 10){
                        m=0+""+month;
                    }
                    else {
                        m= String.valueOf(month);
                    }
                    if (day < 10){
                        d=0+""+day;
                    }
                    else {
                        d= String.valueOf(day);
                    }
                    selectDate.setError(null);
                    String date = year + "-" + m + "-" + d;
                    selected.setText(date);
                }
                else {
                    Toast.makeText(TrackerActivity.this, "Present date cannot be accepted", Toast.LENGTH_SHORT).show();
                    selectDate.setError("Invalid Date");
                }
            }
        };

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(selected.getText().toString().trim())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(TrackerActivity.this)
                            .setMessage("Do you want to save this date?")
                            .setCancelable(false)
                            .setTitle("Save Confirmation")
                            .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    check_lnmp(selected.getText().toString().trim());
                                    editor.putString("lnmp_date","");
                                    editor.apply();
                                }
                            })
                            .setPositiveButton("Yes, Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                   editor.putString("lnmp_date",selected.getText().toString().trim());
                                   editor.apply();
                                    check_lnmp(selected.getText().toString().trim());
                                }
                            });
                    AlertDialog dialog= builder.create();
                    dialog.show();
                }
                else {
                    Toast.makeText(TrackerActivity.this, "Select LNMP Date", Toast.LENGTH_SHORT).show();
                }
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(StorageSense.onHealthSense(), Context.MODE_PRIVATE);
        String info=sharedPreferences.getString("user_id","");

    }

    public void check_lnmp(String date){
        final ProgressDialog dialog = new ProgressDialog(TrackerActivity.this);
        dialog.setMessage("Validating LNMP Information.......");
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
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                // Dismiss the progress dialog after done uploading.
                dialog.dismiss();
                try {
                    if (response.contains("existing")) {
                        String final_results = response.replace("existing", "");
                        try {
                            JSONObject jsonObject = new JSONObject(final_results);
                            lnmp_date.setText(jsonObject.getString("actual_date"));
                            lnmp_datetime.setText(jsonObject.getString("date_uploaded"));
                            weeks.setText(jsonObject.getString("weeks"));
                            months.setText(jsonObject.getString("months"));
                            trimster.setText(jsonObject.getString("trimester"));
                            tv=jsonObject.getString("tri_value");
                            delivery.setText(jsonObject.getString("delivery_date"));
                            int year= Integer.parseInt(jsonObject.getString("delivery_date").substring(0,4));
                            int month= Integer.parseInt(jsonObject.getString("delivery_date").substring(6,7));
                            int day= Integer.parseInt(jsonObject.getString("delivery_date").substring(8,10));
                            set_layout.setVisibility(View.GONE);
                            info_layout.setVisibility(View.VISIBLE);

                            date_picker.updateDate(year,month-1,day);
                        } catch (JSONException e) {
                            Toast.makeText(TrackerActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.contains("new_lnmp")) {
                        Toast.makeText(TrackerActivity.this, "New LNMP", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(TrackerActivity.this, "" + response, Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(TrackerActivity.this,HomeScreen.class));
                    }
                }
                catch (Exception ez){
//                    startActivity(new Intent(TrackerActivity.this,HomeScreen.class));
                    Toast.makeText(TrackerActivity.this, ""+ez.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            protected String doInBackground(String... params) {
                ResultHash.put("lnmp_date", params[0]);
                finalResult = httpParse.ImageHttpRequest(TRACKER_PATH,ResultHash);
                return finalResult;
            }
        }
        AsyncTaskUploadClass dataUploadClass = new AsyncTaskUploadClass();
        dataUploadClass.execute(date);
    }

    public void onBack(View view) {
        onBackPressed();
    }

    public void close_saved(View view) {
        set_layout.setVisibility(View.VISIBLE);
        info_layout.setVisibility(View.GONE);
    }

    public void view_recommended_info(View view) {
        if (tv == "") {
            Toast.makeText(TrackerActivity.this, "We cannot get Selection", Toast.LENGTH_SHORT).show();
        }
        else{

            startActivity(new Intent(TrackerActivity.this,RecommendedActivity.class)
                    .putExtra("time",tv)
            );
        }
    }
}