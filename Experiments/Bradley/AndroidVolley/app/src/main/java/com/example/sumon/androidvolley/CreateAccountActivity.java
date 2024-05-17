package com.example.sumon.androidvolley;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.sumon.androidvolley.app.AppController;
import com.example.sumon.androidvolley.utils.Const;
import com.example.sumon.androidvolley.utils.UserInfo;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {

    private Button return_to_login;
    private Button createAccount;
    private EditText firstName;
    private EditText lastName;
    private EditText username;
    private EditText password;
    private EditText passwordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);


        firstName = findViewById(R.id.editTextFirstName);
        lastName = findViewById(R.id.editTextLastName);
        username = findViewById(R.id.editTextUsername);
        password = findViewById(R.id.editTextCreatePassword);
        passwordConfirm = findViewById(R.id.editTextPasswordConfirm);

        createAccount = findViewById(R.id.CreateAccountButton);
        return_to_login = findViewById(R.id.ReturnToLoginButton);

        createAccount.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                CreateUser();
            }
        });


        return_to_login.setOnClickListener(new View.OnClickListener()
        {

                @Override
                public void onClick(View v) {
                    ReturnToLogin();
                }
        });
    }

    public void ReturnToLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void CreateUser() {
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
//                Const.URL_API + "login/"+ editUsername.getText() + "/" + editPassword.getText(), null,
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            showProgressDialog();
//                            errorMessage.setText("");
//                            Log.d(TAG, response.toString());
//                            Log.d(TAG, response.getString("firstName"));
//                            UserInfo user = UserInfo.getInstance();
//                            user.fName = response.getString("firstName");
//                            user.lName = response.getString("lastName");
//                            user.userId = response.getInt("id");
//                            user.password = response.getString("password");
//                            user.role = response.getInt("role");
//                            hideProgressDialog();
//                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
//                        }
//                        catch (Exception e){
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d(TAG, "Error: " + error.getMessage());
//                errorMessage.setText("There was a problem logging you in, please check your Username and Password");
//            }
//        }) {
//
//            /**
//             * Passing some request headers
//             * */
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json");
//                return headers;
//            }
//
//
//        };
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(jsonObjReq,
//                tag_login);
//
//        // Cancelling request
//        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
   }

    }
