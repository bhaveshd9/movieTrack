package com.example.sumon.androidvolley;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.Dialog;
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
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
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
 * Displays all users on the page //Only available to Admins
 * Gives admins ability to edit users connected with admin edit activity
 * @author Aaron Gienger
 */
public class ModeratorActivity extends Activity{


    private Button homebutton;
    private Button searchButton;
    private Button friendsButton;
    private Button profileButton;
    private TextView userText;
    private TextView userName;
    private long userid;
    private LinearLayout layout;
    private LinearLayout layout1;
    private LinearLayout layout2;
    private Button searchUser;
    private String TAG = HomeActivity.class.getSimpleName();
    private LinearLayout.LayoutParams layoutParams;
    private ProgressDialog pDialog;
    private Button editUser;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        UserInfo user = UserInfo.getInstance();
        userid=user.userId;
        url="https://th.bing.com/th/id/R.8a6cc9efd9dcbeea56702e596aa8275b?rik=lxlrFTa7zWgy%2bQ&pid=ImgRaw&r=0";
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout = (LinearLayout) findViewById(R.id.image_containermod);
        layout1 = (LinearLayout) findViewById(R.id.image_containeradmin);
        layout2 = (LinearLayout) findViewById(R.id.image_containerusers);

        userText=(TextView)findViewById(R.id.firstnameText);
        userName=(TextView)findViewById(R.id.usernameText);
        homebutton = (Button)findViewById(R.id.HomeButton);
        profileButton = (Button)findViewById(R.id.Profile);
        searchButton = (Button)findViewById(R.id.search);
        friendsButton =(Button)findViewById(R.id.Friends);
        getMods();
        getAdmins();
        getusers();
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
        friendsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                openFriendsPage();
            }
        });
    }

    /**
     * Gets all the users profiles including first and last name username id and calls add image to display their profile
     */
    public void getMods(){
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET,Const.URL_API + "role/mod",null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for(int i=0;i< response.length();i++) {
                                JSONObject user = response.getJSONObject(i);
                                String last= user.getString("lastName");
                                String first = user.getString("firstName");
                                int id = user.getInt("id");
                                int role = user.getInt("role");
                                String username= user.getString("username");
                                String url = "https://th.bing.com/th/id/R.8a6cc9efd9dcbeea56702e596aa8275b?rik=lxlrFTa7zWgy%2bQ&pid=ImgRaw&r=0";
                                addImage( layout, url, first, last,username, id,role);
                            }
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
                "role/mod");
    }
    public void getAdmins(){
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET,Const.URL_API + "role/admin",null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for(int i=0;i< response.length();i++) {
                                JSONObject user = response.getJSONObject(i);
                                String last= user.getString("lastName");
                                String first = user.getString("firstName");
                                int id = user.getInt("id");
                                int role = user.getInt("role");
                                String username= user.getString("username");
                                String url = "https://th.bing.com/th/id/R.8a6cc9efd9dcbeea56702e596aa8275b?rik=lxlrFTa7zWgy%2bQ&pid=ImgRaw&r=0";
                                addImage( layout1, url, first, last,username, id,role);
                            }
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
                "role/admin");
    }
    public void getusers(){
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET,Const.URL_API + "role/user",null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for(int i=0;i< response.length();i++) {
                                JSONObject user = response.getJSONObject(i);
                                String last= user.getString("lastName");
                                String first = user.getString("firstName");
                                int id = user.getInt("id");
                                int role = user.getInt("role");
                                String username= user.getString("username");
                                String url = "https://th.bing.com/th/id/R.8a6cc9efd9dcbeea56702e596aa8275b?rik=lxlrFTa7zWgy%2bQ&pid=ImgRaw&r=0";
                                addImage(layout2, url, first, last,username, id, role);
                            }
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
                "role/user");
    }
    /**
     * Displays the users profile picture with name and username and clickable mapped to their profile page
     * @param url the profile picture URL
     * @param first users first name
     * @param last users lastname
     * @param username users username
     * @param id user id
     */
    private void addImage(LinearLayout layout,String url,String first, String last,String username,int id, int role){
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(15, 15, 15, 15);
        layoutParams.gravity = Gravity.LEFT;
        layoutParams.height = 250;
        NetworkImageView imageView = new NetworkImageView(this);
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageView.setImageUrl(url, imageLoader);
        imageView.setLayoutParams(layoutParams);
        TextView titleView = new TextView(this);
        titleView.setText(first+"\n"+last+"\n"+username);
        titleView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        titleView.setTextColor(Color.WHITE);
        Typeface font = ResourcesCompat.getFont(this, R.font.notosansregular);
        titleView.setTypeface(font);
        titleView.setTextSize(15);
        titleView.setEllipsize(TextUtils.TruncateAt.END);
        titleView.setVerticalScrollBarEnabled(true);
        LinearLayout view = new LinearLayout(this);
        view.setOrientation(LinearLayout.HORIZONTAL);

        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(role==0) {
                    Intent act2 = new Intent(view.getContext(), adminEditActivity.class);
                    act2.putExtra("id", id);
                    startActivity(act2);
                }else{
                    Intent act2 = new Intent(view.getContext(), otherUserActivity.class);
                    act2.putExtra("id", id);
                    startActivity(act2);
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(role==0) {
                    Intent act2 = new Intent(view.getContext(), adminEditActivity.class);
                    act2.putExtra("id", id);
                    startActivity(act2);
                }else{
                    Intent act2 = new Intent(view.getContext(), otherUserActivity.class);
                    act2.putExtra("id", id);
                    startActivity(act2);
                }
            }
        });

        view.addView(imageView);
        view.addView(titleView);
        layout.addView(view);

    }
    public void searchUserPage() {
        Intent intent = new Intent(this, userSearchActivity.class);
        startActivity(intent);
    }
    public void openHomePage() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
    public void openFriendsPage() {
        Intent intent = new Intent(this, FriendActivity.class);
        startActivity(intent);
    }
    public void openProfilePage() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
    public void openSearchPage() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}
