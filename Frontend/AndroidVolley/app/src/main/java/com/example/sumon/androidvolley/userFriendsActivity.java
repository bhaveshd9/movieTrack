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
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Profile display for if users are friends
 * @author Aaron Gienger
 */
public class userFriendsActivity extends Activity{

    private Button homebutton;
    private Button searchButton;
    private Button friendsButton;
    private Button profileButton;
    private TextView userText;
    private TextView userName;
    private TextView username;
    private TextView movieTag;
    private LinearLayout layout;
    private LinearLayout layout1;
    private String TAG = HomeActivity.class.getSimpleName();
    private LinearLayout.LayoutParams layoutParams;
    private LinearLayout.LayoutParams layoutParams1;
    private ProgressDialog pDialog;
    private Button addUser;
    private UserInfo user;
    private long userid;
    private long currentuserid;
    private String cFn;
    private String cLn;
    private String cUn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otheruser);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        UserInfo user = UserInfo.getInstance();
        currentuserid=user.userId;
        cFn=user.fName;
        cLn=user.lName;
        cUn=user.username;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int id = extras.getInt("id");
            userid = id;
            getUser();
            getMovielog();
        }

        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout =  (LinearLayout) findViewById(R.id.profilePic);
        layout1 =  (LinearLayout) findViewById(R.id.image_container4);

        addProfilePic("https://th.bing.com/th/id/R.8a6cc9efd9dcbeea56702e596aa8275b?rik=lxlrFTa7zWgy%2bQ&pid=ImgRaw&r=0");


        addUser = (Button)findViewById(R.id.editUsername);
        addUser.setText("UnAdd");

        homebutton = (Button)findViewById(R.id.HomeButton);
        profileButton = (Button)findViewById(R.id.Profile);
        searchButton = (Button)findViewById(R.id.search);
        friendsButton =(Button)findViewById(R.id.Friends);
        movieTag = (TextView)findViewById(R.id.textView2);

        friendsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                openFriendsPage();
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
        addUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){

                removeFriend();
            }
        });

    }

    /**
     * Gets the other users profile information
     */
    private void getUser(){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET , Const.URL_API + "users/"+userid ,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {


                            int movieId=response.getInt("id");
                            String first=response.getString("firstName");
                            String last=response.getString("lastName");
                            String user= response.getString("username");
                            movieTag.setText(first + "'s Movies");
//                               String poster = "https://image.tmdb.org/t/p/w500" + movie.getString("poster_path");
//                               String title =  movie.getString("title");
                            userText=(TextView)findViewById(R.id.firstnameText);
                            userText.setText(last);
                            userName=(TextView)findViewById(R.id.usernameText);
                            userName.setText(first);
                            username=(TextView)findViewById(R.id.username);
                            username.setText("@"+user);
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
     * Adds profile picture
     * @param url URL of image
     */
    private void addProfilePic(String url){
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 20, 20, 20);
        layoutParams.gravity = Gravity.LEFT;
        layoutParams.height = 350;
        NetworkImageView imageView = new NetworkImageView(this);
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageView.setImageUrl(url, imageLoader);
        imageView.setLayoutParams(layoutParams);
        LinearLayout view = new LinearLayout(this);
        view.setOrientation(LinearLayout.HORIZONTAL);
        view.addView(imageView);
        layout.addView(view);

    }

    /**
     * Gets the other users Movie Log
     */
    private void getMovielog(){
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, Const.URL_API + "logs/"+ userid,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject movie = (JSONObject) response.get(i);
                                JSONObject log = movie.getJSONObject("movie");
                                String poster = "https://image.tmdb.org/t/p/w500" + log.getString("poster_path");
                                String title = log.getString("title");
                                int id = log.getInt("id");
                                addImage1(layout1,poster, title, id);
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
                "logs/{userid}");
    }

    /**
     * Adds movies from users log to display
     * @param layout Linear layout
     * @param url Image URL
     * @param title movie title
     * @param id movie id
     */
    private void addImage1(LinearLayout layout,String url, String title, int id){

        layoutParams.setMargins(20, 20, 20, 20);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.height = 350;
        NetworkImageView imageView = new NetworkImageView(this);
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageView.setImageUrl(url, imageLoader);
        imageView.setLayoutParams(layoutParams);
        TextView titleView = new TextView(this);
        titleView.setText(title);
        titleView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        titleView.setTextColor(Color.WHITE);
        Typeface font = ResourcesCompat.getFont(this, R.font.notosansregular);
        titleView.setTypeface(font);
        titleView.setEllipsize(TextUtils.TruncateAt.END);
        titleView.setHorizontallyScrolling(true);
        LinearLayout view = new LinearLayout(this);
        view.setOrientation(LinearLayout.VERTICAL);
        view.addView(imageView);
        view.addView(titleView);
        layout1.addView(view);

    }

    /**
     * Ability to unadd a friend
     */
    private void removeFriend(){

        JSONObject body= new JSONObject();
        try {
            body.put("id",currentuserid);
            body.put("firstName", cFn);
            body.put("lastName", cLn);
            body.put("username", cUn);
            //Store json in a string
            final String requestBody = body.toString();


            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,Const.URL_API + "users/"+userid+"/disconnect",null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                response.put("id",currentuserid);
                                response.put("firstName", cFn);
                                response.put("lastName", cLn);
                                response.put("username", cUn);
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
                    try {

                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

            };

            AppController.getInstance().addToRequestQueue(req,
                    "users/{id}/disconnect");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * opens Friends Page
     */
    public void openFriendsPage() {
        Intent intent = new Intent(this, FriendActivity.class);
        startActivity(intent);
    }
    /**
     * opens edit user Page
     */
    public void openeditUserPage() {
        Intent intent = new Intent(this, editUserActivity.class);
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
