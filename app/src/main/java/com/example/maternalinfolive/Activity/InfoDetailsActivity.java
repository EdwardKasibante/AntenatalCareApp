package com.example.maternalinfolive.Activity;

import static com.example.maternalinfolive.Utils.HttpUrls.AUTHENTICATION_SERVER_PATH;
import static com.example.maternalinfolive.Utils.HttpUrls.DELETE_PATH;
import static com.example.maternalinfolive.Utils.HttpUrls.FIND_RANKING;
import static com.example.maternalinfolive.Utils.HttpUrls.SAVE_RANKING;
import static com.example.maternalinfolive.Utils.StorageSense.onHealthSense;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.maternalinfolive.R;
import com.example.maternalinfolive.Utils.StorageSense;
import com.hsalf.smilerating.SmileRating;
import com.hsalf.smileyrating.SmileyRating;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class InfoDetailsActivity extends AppCompatActivity {
    private TextView name;
    private TextView date, subject, body, usefulltime, user_type;
    private SmileyRating smileyRating;
    int rating;
    private TextView points;
    private Button delete;

    int type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_details);
        name = findViewById(R.id.name);
        date = findViewById(R.id.date);
        subject = findViewById(R.id.subject);
        body = findViewById(R.id.body);
        usefulltime = findViewById(R.id.userfull_time);
        user_type = findViewById(R.id.user_type);
        smileyRating = findViewById(R.id.smile_rating);
        points = findViewById(R.id.points);
        rating = 0;
        delete = findViewById(R.id.delete);
        hide_add_btn();
        find_rankings();

        if (getIntent().getStringExtra("poster_type").equals("doctor")) {
            name.setText("Dr. " + getIntent().getStringExtra("poster_Fname") + " " + getIntent().getStringExtra("poster_lname"));

        } else {
            name.setText(getIntent().getStringExtra("poster_Fname") + " " + getIntent().getStringExtra("poster_lname"));
        }

        date.setText(getIntent().getStringExtra("date_posted"));
        subject.setText(getIntent().getStringExtra("subject"));
        body.setText(getIntent().getStringExtra("body"));
        usefulltime.setText("Useful Time: " + getIntent().getStringExtra("trimester"));
        user_type.setText(getIntent().getStringExtra("category"));

        smileyRating.setSmileySelectedListener(type -> {
            // You can compare it with rating Type
            rating = type.getRating();
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(InfoDetailsActivity.this)
                        .setMessage("Are you sure you want to delete this post.? Remember this operation cannot be reversed")
                        .setCancelable(false)
                        .setTitle("Delete Confirmation")
                        .setNegativeButton("Not Now", (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("Yes, Save", (dialog, which) -> {
                            delete_post();
                        });
                AlertDialog dialog= builder.create();
                dialog.show();
            }
        });
    }

    public void onBack(View view) {
        onBackPressed();
    }

    public void submit_ranking(View view) {
        if (rating == 0) {
            Toast.makeText(InfoDetailsActivity.this, "You have not selected anything", Toast.LENGTH_SHORT).show();
        } else {
            make_ranking();
        }
    }




    private void find_rankings() {
        StringRequest request = new StringRequest(Request.Method.POST, FIND_RANKING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("points") == "null") {
                        points.setText("0 pts");
                    } else {
                        points.setText(Integer.parseInt(jsonObject.getString("points")) + " Pts");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> Toast.makeText(InfoDetailsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("post_id", getIntent().getStringExtra("id"));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(InfoDetailsActivity.this);
        requestQueue.add(request);
    }


    private void make_ranking() {
        StringRequest request = new StringRequest(Request.Method.POST, SAVE_RANKING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(InfoDetailsActivity.this, "" + response, Toast.LENGTH_SHORT).show();
                if (response.equalsIgnoreCase("Submitted. Thanks for Your Contribution")) {
                    recreate();
                }
            }
        }, error -> Toast.makeText(InfoDetailsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sharedPreferences = getSharedPreferences(StorageSense.onHealthSense(), Context.MODE_PRIVATE);
                Map<String, String> params = new HashMap<String, String>();
                params.put("post_id", getIntent().getStringExtra("id"));
                params.put("user_id", sharedPreferences.getString("user_id", null));
                params.put("points", "" + rating);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(InfoDetailsActivity.this);
        requestQueue.add(request);
    }

    private void hide_add_btn() {
        SharedPreferences sharedPreferences = getSharedPreferences(StorageSense.onHealthSense(), Context.MODE_PRIVATE);
        type = Integer.parseInt(sharedPreferences.getString("user_role", null));
        switch (type) {
            case 1:
                delete.setVisibility(View.GONE);
                break;
            default:
                delete.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void delete_post() {
        final ProgressDialog dialog = new ProgressDialog(InfoDetailsActivity.this);
        dialog.setMessage("Deleting in progress.......");
        dialog.show();
        dialog.setCancelable(true);
        StringRequest request = new StringRequest(Request.Method.POST, DELETE_PATH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Toast.makeText(InfoDetailsActivity.this, "" + response, Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(InfoDetailsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sharedPreferences = getSharedPreferences(StorageSense.onHealthSense(), Context.MODE_PRIVATE);
                Map<String, String> params = new HashMap<String, String>();
                params.put("post_id", getIntent().getStringExtra("id"));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(InfoDetailsActivity.this);
        requestQueue.add(request);
    }
}