package com.example.sumon.androidvolley;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
 * Gives admins ability to edit a users profile
 * @author Aaron Gienger
 */
public class adminEditActivity extends Activity{
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
    private Button newRole;
    private EditText searchBar;
    private EditText searchBarfirst;
    private EditText searchBarlast;
    private EditText searchBarRole;
    private int otheruserid;
    //private UserInfo user;
    private String first;
    private String last;
    private String user;
    private Button deleteUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        userText=(TextView)findViewById(R.id.usernameText);
        homebutton = (Button)findViewById(R.id.HomeButton);
        profileButton = (Button)findViewById(R.id.Profile);
        searchButton = (Button)findViewById(R.id.search);
        newUser = (Button)findViewById(R.id.userEnter);
        newfirst = (Button)findViewById(R.id.firstEnter);
        newlast = (Button)findViewById(R.id.lastEnter);
        newRole = (Button)findViewById(R.id.roleEnter);
        searchBar = (EditText) findViewById(R.id.editUserBar);
        searchBarfirst = (EditText) findViewById(R.id.editfirstBar);
        searchBarlast = (EditText) findViewById(R.id.editlastBar);
        searchBarRole = (EditText) findViewById(R.id.editRoleBar);
        otheruserid=0;
        deleteUser = (Button) findViewById(R.id.deleteAccount);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int id = extras.getInt("id");
            otheruserid = id;
            getUser();

        }
        deleteUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){

                AlertDialog.Builder builder= new AlertDialog.Builder(adminEditActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Confirm Delete");
                builder.setPositiveButton("Accept",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteUser();
                    }
                });
                builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       openadminPage();

                    }
                });
                builder.show();

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
                getUser();
            }
        });
        newfirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String username=searchBar.getText().toString();
                setFirstName();
                getUser();
            }
        });
        newlast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String username=searchBar.getText().toString();
                setLastname();
                getUser();
            }
        });
        newRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String username=searchBar.getText().toString();
                setRole();
                getUser();
            }
        });

    }

    /**
     * Admin control to delete a users account
     */
    private void deleteUser(){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.DELETE , Const.URL_API + "users/"+otheruserid ,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

//                            first=response.getString("firstName");
//                            last=response.getString("lastName");
//                            user= response.getString("username");
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
            }
        });

        AppController.getInstance().addToRequestQueue(req,
                "users/{id}");
    }

    /**
     * Gets the users info and is passed and updated after something is changed
     */
    private void getUser(){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET , Const.URL_API + "users/"+otheruserid ,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            first=response.getString("firstName");
                            last=response.getString("lastName");
                            user= response.getString("username");
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
            }
        });

        AppController.getInstance().addToRequestQueue(req,
                "users/{id}");
    }

    /**
     * Admin ablility to change other users username
     */
    public void setUsername(){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT,Const.URL_API + "users/"+otheruserid,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(searchBar.getText()!=null) {
                                response.put("username", searchBar.getText());
                            }
                            openadminPage();
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
                    jsonBody.put("firstName",first);
                    jsonBody.put("lastName", last);
                    //jsonBody.put("password", user.password);


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
     * Admins ability to change first name
     */
    public void setFirstName(){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT,Const.URL_API + "users/"+otheruserid,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(searchBarfirst.getText()!=null){
                                response.put("firstName", searchBarfirst.getText());
                            }
                            openadminPage();
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

                    jsonBody.put("username", user);
                    jsonBody.put("firstName",searchBarfirst.getText());
                    jsonBody.put("lastName", last);
                    //jsonBody.put("password", user.password);


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
     * Admin ability to change last name
     */
    public void setLastname(){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT,Const.URL_API + "users/"+otheruserid,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if(searchBarlast.getText()!=null){
                                response.put("lastName", searchBarlast.getText());
                            }
                            openadminPage();
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

                    jsonBody.put("username", user);
                    jsonBody.put("firstName",first);
                    jsonBody.put("lastName", searchBarlast.getText());
                    //jsonBody.put("password", user.password);


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
     * Admin ability to change the role of users to moderators or admins
     */
    public void setRole(){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT,Const.URL_API + "users/"+otheruserid,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if(searchBarlast.getText()!=null){
                                response.put("role", searchBarRole.getText());
                            }
                            openadminPage();
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
                    jsonBody.put("username", user);
                    jsonBody.put("firstName",first);
                    jsonBody.put("lastName", last);
                    jsonBody.put("role", searchBarRole.getText());


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
     * opens admin Page
     */
    public void openadminPage() {
        Intent intent = new Intent(this, AdminActivity.class);
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
