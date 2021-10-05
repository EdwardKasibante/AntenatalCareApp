package com.example.maternalinfolive.Activity;

import static com.example.maternalinfolive.Utils.HttpUrls.DELETE_PATH;
import static com.example.maternalinfolive.Utils.HttpUrls.UPDATE_ROLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.maternalinfolive.R;
import com.example.maternalinfolive.Utils.StorageSense;

import java.util.HashMap;
import java.util.Map;

public class UserActivity extends AppCompatActivity {

    private TextView name,gender,phone,date,address,type;
    private CardView car_here;
    private Button call,chat,change_role;
    String new_role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        name=findViewById(R.id.name);
        type=findViewById(R.id.type);
        gender=findViewById(R.id.gender);
        address=findViewById(R.id.address);
        call=findViewById(R.id.call_phone);
        date=findViewById(R.id.date);
        car_here=findViewById(R.id.car_here);
        chat=findViewById(R.id.chat);
        change_role=findViewById(R.id.change_role);
        new_role ="";

      name.setText(""+getIntent().getStringExtra("fname")+" "+getIntent().getStringExtra("lname"));
      date.setText(""+getIntent().getStringExtra("date"));
      gender.setText(getIntent().getStringExtra("gender"));
      address.setText(""+getIntent().getStringExtra("phone"));
      String role=getIntent().getStringExtra("role_id");


        switch (role) {
            case "1":
                new_role = "3";
                change_role.setText("Grant Doctor Role");
                break;
            case "3":
                new_role = "1";
                change_role.setText("Revoke Doctor Role");
                break;
            default:
        }


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getIntent().getStringExtra("phone").toString().isEmpty()){
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "" +getIntent().getStringExtra("phone")));
                    if (ContextCompat.checkSelfPermission(UserActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) UserActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 100);
                    } else {
                       startActivity(intent);
                    }
                }
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivity.this, PersonalChat.class)
                        .putExtra("destination",""+getIntent().getStringExtra("id")));
            }
        });


        change_role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this)
                        .setMessage("Are you sure you want to change this person's role.? Remember this operation be reversed")
                        .setCancelable(false)
                        .setTitle("Grant/Revoke Confirmation")
                        .setNegativeButton("Not Now", (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("Yes, Change", (dialog, which) -> {
                            update_role();
                        });
                AlertDialog dialog= builder.create();
                dialog.show();
            }
        });
    }


    private void update_role() {
        final ProgressDialog dialog = new ProgressDialog(UserActivity.this);
        dialog.setMessage("Updating in progress.......");
        dialog.show();
        dialog.setCancelable(true);
        StringRequest request = new StringRequest(Request.Method.POST, UPDATE_ROLE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                recreate();
                Toast.makeText(UserActivity.this, "" + response, Toast.LENGTH_SHORT).show();
                if (response.equalsIgnoreCase("User Role Updated Successfully")){
                    startActivity(new Intent(UserActivity.this,ViewClient.class));
                    finish();
                }
            }
        }, error -> Toast.makeText(UserActivity.this, error.getMessage(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sharedPreferences = getSharedPreferences(StorageSense.onHealthSense(), Context.MODE_PRIVATE);
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", getIntent().getStringExtra("id"));
                params.put("new_role",new_role);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(UserActivity.this);
        requestQueue.add(request);
    }

    public void onBack(View view) {
        onBackPressed();
    }
}