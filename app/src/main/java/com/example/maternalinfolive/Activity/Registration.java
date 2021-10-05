package com.example.maternalinfolive.Activity;

import static com.example.maternalinfolive.Utils.HttpUrls.AUTHENTICATION_SERVER_PATH;
import static com.example.maternalinfolive.Utils.HttpUrls.SAVE_PROFILE_PATH;
import static com.example.maternalinfolive.Utils.HttpUrls.VERIFY_CODE_PATH;
import static com.example.maternalinfolive.Utils.HttpUrls.VERIFY_CODE__FINAL_PATH;
import static com.example.maternalinfolive.Utils.StorageSense.onHealthSense;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.maternalinfolive.R;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
 private LinearLayout sign_in_layout;
    private LinearLayout sign_up_layout, code_layout;
    private ImageView img;
    private Spinner gender_spinner;
    private ArrayList<String> gender;
    private EditText user_name,pass_word,code_phone,code_code;

    private Button reg_submit;
    private EditText reg_fname,reg_lname,reg_phone;
    private CountryCodePicker reg_cpp;
    private MaterialCheckBox reg_terms;
    private String user_phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_acivity);
        sign_in_layout=findViewById(R.id.login_layout);
        sign_up_layout=findViewById(R.id.register_layout);
        code_layout=findViewById(R.id.code_layout);
        img=findViewById(R.id.icon_here);
        gender_spinner=findViewById(R.id.gender_spinner);

        user_name=findViewById(R.id.username);
        pass_word=findViewById(R.id.password);

        code_code=findViewById(R.id.code_code);
        code_phone=findViewById(R.id.code_number);

        reg_submit=findViewById(R.id.sign_up_btn);
        reg_cpp=findViewById(R.id.reg_ccp);
        reg_fname=findViewById(R.id.reg_fname);
        reg_lname=findViewById(R.id.reg_lname);
        reg_phone=findViewById(R.id.reg_phone);
        reg_terms=findViewById(R.id.reg_terms);



        reg_terms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if (ischecked){
                   reg_submit.setEnabled(true);
                   reg_submit.setText("SIGN UP");
                    reg_submit.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
                else{
                    reg_submit.setText("Consent Required");
                    reg_submit.setEnabled(false);
                    reg_submit.setBackgroundColor(getResources().getColor(R.color.gray));
                }
            }
        });

        img.setAnimation(AnimationUtils.loadAnimation(Registration.this, R.anim.from_top));
//      sign_in_layout.setAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.from_bottom));

        gender=new ArrayList<>();
        gender.add("Pick Gender");
        gender.add("Male");
        gender.add("Female");
        gender_spinner.setAdapter(new ArrayAdapter<>(Registration.this,
                android.R.layout.simple_spinner_dropdown_item,gender
        ));


        sign_in_layout.setVisibility(View.VISIBLE);
        sign_up_layout.setVisibility(View.GONE);
        code_layout.setVisibility(View.GONE);

        reg_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    private void registerUser() {

       try {
           final String gender = (String) gender_spinner.getSelectedItem();
           if (reg_fname.getText().toString().trim().isEmpty() || reg_fname.getText().toString().trim().length() < 3){
               reg_fname.setError("First name at least 3 characters");
           }
           else if (reg_lname.getText().toString().trim().isEmpty() || reg_lname.getText().toString().trim().length() < 3){
               reg_lname.setError("First name at least 3 characters");
           }
         else if (reg_phone.getText().toString().trim().isEmpty()){
             reg_phone.setError("Phone number is required");
         }
          else if (reg_phone.getText().toString().trim().length() < 9
                 || reg_phone.getText().toString().trim().length() > 10){
             reg_phone.setError("Phone number must be valid");
         }
         else  if (gender =="Pick Gender" || gender.isEmpty() || gender==null){
             Toast.makeText(this, "Pick Gender From the List", Toast.LENGTH_SHORT).show();
         }
         else if (!reg_terms.isChecked()){
               Toast.makeText(this, "You must agree to terms and conditions", Toast.LENGTH_SHORT).show();
           }
             else{
                  user_phone_number= reg_cpp.getSelectedCountryCodeWithPlus()+ reg_phone.getText().toString().trim().replaceFirst("^0+(?!$)", "");
               AlertDialog.Builder builder=new AlertDialog.Builder(Registration.this)
                       .setMessage("Confirm if the information provided is true and the your phone number is  "+user_phone_number)
                       .setCancelable(false)
                       .setTitle("Phone Number Confirmation")
                       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
//                              verify phonenmber
                               perform_verification();
                           }
                       })
                       .setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();
                           }
                       });
               AlertDialog dialog=builder.create();
               dialog.show();

             }
       }catch (Exception e){
           Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
       }


    }

    private void perform_verification() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Generating verification code");
        progressDialog.show();
        progressDialog.setCancelable(true);

        StringRequest request = new StringRequest(Request.Method.POST, VERIFY_CODE_PATH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response.equalsIgnoreCase("code_sent_tag_new")) {
                    code_phone.setText(user_phone_number);
                    sign_in_layout.setVisibility(View.GONE);
                    sign_up_layout.setVisibility(View.GONE);
                    code_layout.setVisibility(View.VISIBLE);


                } else {
                    Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Registration.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("user_phone",user_phone_number);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Registration.this);
        requestQueue.add(request);
    }

    public void openSignUp(View view) {
        sign_in_layout.setVisibility(View.GONE);
        sign_up_layout.setVisibility(View.VISIBLE);
    }

    public void closeThis(View view) {
        sign_in_layout.setVisibility(View.VISIBLE);
        sign_up_layout.setVisibility(View.GONE);
    }

    public void authenticateUser(View view) {
       if (TextUtils.isEmpty(user_name.getText().toString().trim())){
         user_name.setError("This is required");
       }
       else if (TextUtils.isEmpty(pass_word.getText().toString().trim())){
           pass_word.setError("This is required");
       }
       else {
           verifyCredentials();
       }
    }

    private void  verifyCredentials(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait.....");
        progressDialog.show();
        progressDialog.setCancelable(true);

        StringRequest request = new StringRequest(Request.Method.POST, AUTHENTICATION_SERVER_PATH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response.contains("logged-in-successfully")) {
                    String final_results=response.replace("logged-in-successfully","");

                    try {
                        JSONObject jsonObject=new JSONObject(final_results);
                        SharedPreferences sharedPreferences = getSharedPreferences(onHealthSense(), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("profile", "saved");
                        editor.putString("user_id", jsonObject.getString("user_id"));
                        editor.putString("last_name", jsonObject.getString("last_name"));
                        editor.putString("first_name", jsonObject.getString("first_name"));
                        editor.putString("gender", jsonObject.getString("gender"));
                        editor.putString("gender", jsonObject.getString("gender"));
                        editor.putString("user_role",jsonObject.getString("role_id"));
                        editor.apply();
                        Toast.makeText(Registration.this,"Login Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Registration.this, Dashboard.class));
                        finish();

                    } catch (JSONException e) {
                        Toast.makeText(Registration.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Registration.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("username",user_name.getText().toString().trim());
                params.put("password",pass_word.getText().toString().trim());
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Registration.this);
        requestQueue.add(request);


    }
    public void verify_code_now(View view) {
        if (code_code.getText().toString().trim().length() < 5){
            code_code.setError("Verification code required");
        }
        else{
            verify_user_code();
        }
    }
    private void verify_user_code() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Verifying Code....");
        progressDialog.show();
        progressDialog.setCancelable(true);

        StringRequest request = new StringRequest(Request.Method.POST, VERIFY_CODE__FINAL_PATH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response.equalsIgnoreCase("success_tag_on")) {
                    Toast.makeText(Registration.this, "Code Verification completed", Toast.LENGTH_SHORT).show();
//                    save user profile
                    save_user_profile();

                }
              else  if (response.equalsIgnoreCase("invalid_tag_off")) {
                    Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Registration.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("user_phone",user_phone_number);
                params.put("code_sent",code_code.getText().toString().toString().trim());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Registration.this);
        requestQueue.add(request);
    }

    private void save_user_profile() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating profile.....");
        progressDialog.show();
        progressDialog.setCancelable(false);

        StringRequest request = new StringRequest(Request.Method.POST, SAVE_PROFILE_PATH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response.equalsIgnoreCase("profile_created")) {
                    Toast.makeText(Registration.this, "Profile Created Successfully. Login Now", Toast.LENGTH_SHORT).show();
startActivity(new Intent(Registration.this, SplashScreen.class));
finish();
                }
                else  if (response.equalsIgnoreCase("invalid_tag_off")) {
                    Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(Registration.this, error.getMessage(), Toast.LENGTH_LONG).show();
        }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                final String gender = (String) gender_spinner.getSelectedItem();
                params.put("user_phone",user_phone_number);
                params.put("fname",reg_fname.getText().toString().toString().trim());
                params.put("lname",reg_lname.getText().toString().toString().trim());
                params.put("gender",gender);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Registration.this);
        requestQueue.add(request);
    }

    public void resend_code(View view) {
        code_code.setText("");
     perform_verification();
    }
}