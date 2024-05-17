package com.example.sumon.androidvolley;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.example.sumon.androidvolley.app.AppController;
import com.example.sumon.androidvolley.utils.Const;
import com.example.sumon.androidvolley.utils.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Users page to edit their information
 * @author Aaron Gienger
 */
public class editUserActivity extends Activity {
    private Button homebutton;
    private Button searchButton;
    private Button friendsButton;
    private Button profileButton;
    private TextView userText;
    private LinearLayout layout;
    private String TAG = editUserActivity.class.getSimpleName();
    private LinearLayout.LayoutParams layoutParams;
    private ProgressDialog pDialog;
    private Button newUser;
    private Button newfirst;
    private Button newlast;
    private Button newPass;
    private EditText searchBar;
    private EditText searchBarfirst;
    private EditText searchBarlast;
    private EditText passText;
    private long userid;
    private UserInfo user;
    private int role;
    private EditText profileURL;
    private Button urlEnter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edituser);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        user = UserInfo.getInstance();
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        userText=(TextView)findViewById(R.id.usernameText);
        //userText.setText(user.fName);
        userid=user.userId;
        role = user.role;
        homebutton = (Button)findViewById(R.id.HomeButton);
        profileButton = (Button)findViewById(R.id.Profile);
        searchButton = (Button)findViewById(R.id.search);
        newUser = (Button)findViewById(R.id.userEnter);
        newfirst = (Button)findViewById(R.id.firstEnter);
        newlast = (Button)findViewById(R.id.lastEnter);
        newPass = (Button)findViewById(R.id.passEnter);
        searchBar = (EditText) findViewById(R.id.editUserBar);
        searchBarfirst = (EditText) findViewById(R.id.editfirstBar);
        searchBarlast = (EditText) findViewById(R.id.editlastBar);
        passText = (EditText)findViewById(R.id.editpassBar);
        profileURL = (EditText)findViewById(R.id.imageURL);
        profileButton = (Button)findViewById(R.id.imageEnter);
        profileButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){

                setURL();
            }
        });
        newPass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                setPassword();
            }
        });
        homebutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                openHomePage();
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                openProfilePage();
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                openSearchPage();
            }
        });
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String username=searchBar.getText().toString();
                setUsername();
            }
        });
        newfirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String username=searchBar.getText().toString();
                setFirstName();
            }
        });
        newlast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String username=searchBar.getText().toString();
                setLastname();
            }
        });

    }
    public void setURL(){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,Const.URL_API + "image/"+user.username+"/upload",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(profileURL.getText()!=null){
                                response.put("url", profileURL.getText());
                            }
                            openProfilePage();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "TESTF:");
                Log.d(TAG, "Error: " + error.getMessage());
            }}) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                JSONObject jsonBody = new JSONObject();

                try {
                    jsonBody.put("url", profileURL.getText());

                    //Store json in a string
                    final String requestBody = jsonBody.toString();
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

        };
        AppController.getInstance().addToRequestQueue(req,
                "image/{username}/upload");
    }
    /**
     * Users ability to change their username
     */
    public void setUsername(){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT,Const.URL_API + "users/"+userid,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(searchBar.getText()!=null){
                                response.put("username", searchBar.getText());
                            }
                            UserInfo user = UserInfo.getInstance();
                            user.username = response.getString("username");
                            openProfilePage();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "TESTF:");
                Log.d(TAG, "Error: " + error.getMessage());
            }}) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                JSONObject jsonBody = new JSONObject();

                try {
                        jsonBody.put("username", searchBar.getText());
                        jsonBody.put("firstName",user.fName);
                        jsonBody.put("lastName", user.lName);
                        jsonBody.put("role", user.role);
                        jsonBody.put("password", user.password);
                    //Store json in a string
                    final String requestBody = jsonBody.toString();
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

        };
        AppController.getInstance().addToRequestQueue(req,
                "users/{id}");
    }
    public void setPassword(){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT,Const.URL_API + "userPassword/"+userid,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(passText.getText()!=null){
                                response.put("password", passText.getText());
                            }
                            UserInfo user = UserInfo.getInstance();
                            user.password = response.getString("password");
                            openProfilePage();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "TESTF:");
                Log.d(TAG, "Error: " + error.getMessage());
            }}) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                JSONObject jsonBody = new JSONObject();

                try {
                    jsonBody.put("username", user.username);
                    jsonBody.put("firstName",user.fName);
                    jsonBody.put("lastName", user.lName);
                    jsonBody.put("role", user.role);
                    jsonBody.put("password", passText.getText());
                    //Store json in a string
                    final String requestBody = jsonBody.toString();
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

        };
        AppController.getInstance().addToRequestQueue(req,
                "userPassword/{id}");
    }
    /**
     * Users ability to change their first name
     */
    public void setFirstName(){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT,Const.URL_API + "users/"+userid,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(searchBarfirst.getText()!=null){
                                response.put("firstName", searchBarfirst.getText());
                            }
                            UserInfo user = UserInfo.getInstance();
                            user.fName = response.getString("firstName");
                            openProfilePage();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "TESTF:");
                Log.d(TAG, "Error: " + error.getMessage());
            }}) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() {
                JSONObject jsonBody = new JSONObject();
                try {

                    jsonBody.put("username", user.username);
                    jsonBody.put("firstName",searchBarfirst.getText());
                    jsonBody.put("lastName", user.lName);
                    jsonBody.put("role", user.role);
                    jsonBody.put("password", user.password);


                    //Store json in a string
                    final String requestBody = jsonBody.toString();
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

        };

        AppController.getInstance().addToRequestQueue(req,
                "users/{id}");


    }

    /**
     * Users ability to change their last name
     */
    public void setLastname(){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT,Const.URL_API + "users/"+userid,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if(searchBarlast.getText()!=null){
                                response.put("lastName", searchBarlast.getText());
                            }
                            UserInfo user = UserInfo.getInstance();
                            user.lName = response.getString("lastName");
                            openProfilePage();


                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "TESTF:");
                Log.d(TAG, "Error: " + error.getMessage());
            }}) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                JSONObject jsonBody = new JSONObject();

                try {

                    jsonBody.put("username", user.username);
                    jsonBody.put("firstName",user.fName);
                    jsonBody.put("role", user.role);
                    jsonBody.put("lastName", searchBarlast.getText());
                    jsonBody.put("password", user.password);


                    //Store json in a string
                    final String requestBody = jsonBody.toString();
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

        };

        AppController.getInstance().addToRequestQueue(req,
                "users/{id}");


    }
    /**
     * opens Home Page
     */
    public void openHomePage() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
    /**
     * opens Profile Page
     */
    public void openProfilePage() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
    /**
     * opens Search Page
     */
    public void openSearchPage() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}
