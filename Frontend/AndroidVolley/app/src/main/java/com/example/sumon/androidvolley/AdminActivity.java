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
public class AdminActivity extends Activity{


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
    private Button delAllAnn;
    private Button blacklist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        UserInfo user = UserInfo.getInstance();
        userid=user.userId;
        url="https://th.bing.com/th/id/R.8a6cc9efd9dcbeea56702e596aa8275b?rik=lxlrFTa7zWgy%2bQ&pid=ImgRaw&r=0";
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout = (LinearLayout) findViewById(R.id.image_containerAdmin);
        userText=(TextView)findViewById(R.id.firstnameText);
        userName=(TextView)findViewById(R.id.usernameText);
        homebutton = (Button)findViewById(R.id.HomeButton);
        profileButton = (Button)findViewById(R.id.Profile);
        searchButton = (Button)findViewById(R.id.search);
        friendsButton =(Button)findViewById(R.id.Friends);
        getAllUsers();
        delAllAnn = (Button)findViewById(R.id.deleteAnnAll);
        blacklist = (Button)findViewById(R.id.blacklistedMovies);
        delAllAnn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                AlertDialog.Builder builder= new AlertDialog.Builder(AdminActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Confirm");
                builder.setPositiveButton("No",null);

                builder.setNeutralButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAllAnn();
                        recreate();
                    }
                });


                builder.show();

            }
        });
        blacklist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                openBlackListPage();
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
        friendsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                openFriendsPage();
            }
        });
    }
    private void deleteAllAnn(){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.DELETE , Const.URL_API + "ann/delete/all",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

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
                "ann/delete/all");
    }
    /**
     * Gets all the users profiles including first and last name username id and calls add image to display their profile
     */
    public void getAllUsers(){
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET,Const.URL_API + "users",null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for(int i=0;i< response.length();i++) {
                                JSONObject user = response.getJSONObject(i);
                                String last= user.getString("lastName");
                                String first = user.getString("firstName");
                                int id = user.getInt("id");
                                String username= user.getString("username");
                                String url = "https://th.bing.com/th/id/R.8a6cc9efd9dcbeea56702e596aa8275b?rik=lxlrFTa7zWgy%2bQ&pid=ImgRaw&r=0";
                                addImage(url, first, last,username, id);
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
                "users");
    }

    /**
     * Displays the users profile picture with name and username and clickable mapped to their profile page
     * @param url the profile picture URL
     * @param first users first name
     * @param last users lastname
     * @param username users username
     * @param id user id
     */
    private void addImage(String url,String first, String last,String username,int id){
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 20, 20, 20);
        layoutParams.gravity = Gravity.LEFT;
        layoutParams.height = 350;
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
        titleView.setTextSize(20);
        titleView.setEllipsize(TextUtils.TruncateAt.END);
        titleView.setVerticalScrollBarEnabled(true);
        LinearLayout view = new LinearLayout(this);
        view.setOrientation(LinearLayout.HORIZONTAL);

        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent act2 = new Intent(view.getContext(), adminEditActivity.class);
                act2.putExtra("id", id);
                startActivity(act2);
            }
        });

        view.addView(imageView);
        view.addView(titleView);
        layout.addView(view);

    }
    /**
     * opens SearchUser Page
     */
    public void searchUserPage() {
        Intent intent = new Intent(this, userSearchActivity.class);
        startActivity(intent);
    }
    /**
     * opens Home Page
     */
    public void openHomePage() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
    /**
     * opens Friends Page
     */
    public void openFriendsPage() {
        Intent intent = new Intent(this, FriendActivity.class);
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
    public void openBlackListPage() {
        Intent intent = new Intent(this, BlackListActivity.class);
        startActivity(intent);
    }
}
