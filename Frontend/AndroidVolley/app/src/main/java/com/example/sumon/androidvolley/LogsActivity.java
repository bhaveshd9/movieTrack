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
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class LogsActivity extends Activity {

    private Button homebutton;
    private Button searchButton;
    private Button friendsButton;
    private Button profileButton;
    private TextView userLast;
    private TextView userFirst;
    private TextView username;
    private EditText listName;
    private LinearLayout layout;
    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout Listlayout;
    private String TAG = HomeActivity.class.getSimpleName();
    private LinearLayout.LayoutParams layoutParams;
    private ProgressDialog pDialog;
    private Button editUser;
    private Button createListButton;
    private long userid;
    private UserInfo user;
    private int movieid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        user = UserInfo.getInstance();
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //layout = (LinearLayout) findViewById(R.id.profilePic);
        layout1 = (LinearLayout) findViewById(R.id.image_container4);
        layout2 = (LinearLayout) findViewById(R.id.image_container5);
        Listlayout = findViewById(R.id.list_Container);
        userid = user.userId;
        getAllLogs();
        getFriendsLog();
        editUser = (Button) findViewById(R.id.editUsername);
        homebutton = (Button) findViewById(R.id.HomeButton);
        profileButton = (Button) findViewById(R.id.Profile);
        searchButton = (Button) findViewById(R.id.search);
        friendsButton = (Button) findViewById(R.id.Friends);
        createListButton = (Button) findViewById(R.id.createList);


        friendsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openFriendsPage();
            }
        });
        homebutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openHomePage();
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openProfilePage();
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openSearchPage();
            }
        });

    }

    /**
     * Gets the users pending friend requests that they have sent
     */
    private void getAllLogs(){
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, Const.URL_API + "allLogs",null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject movie = (JSONObject) response.get(i);
                                String date = movie.getString("date");
                                String[] date1 = date.split("-");
                                String day = date1[1];
                                String month = date1[2];
                                String[] month1 = month.split("T");
                                String month2 = month1[0];
                                double rating = movie.getInt("rating");
                                movieid=movie.getInt("movie");
                                getMovie(movieid,rating, month2, day);
                                //addLog(layout1,movieid,rating,date);
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
                "allLogs");
    }


    private void getFriendsLog(){
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET,Const.URL_API + "feed/"+userid,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject user = response.getJSONObject(i);
                                JSONObject movie = user.getJSONObject("log");
                                String title = movie.getString("movie");
                                double rating = movie.getInt("rating");
                                int id = movie.getInt("movie");
                                String date = movie.getString("date");
                                String[] date1 = date.split("-");
                                String day = date1[1];
                                String month = date1[2];
                                String[] month1 = month.split("T");
                                String month2 = month1[0];
                                JSONObject otherUser = user.getJSONObject("user");
                                String username = otherUser.getString("username");
                                int otherid = otherUser.getInt("id");
                                getMovieFriends(id,rating,username,title,day,month2, otherid);

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
                "feed/[{id}");
    }
    private void getMovieFriends(int movieid,double rating,String username,String title,String day,String month,int otherid) {
        JsonObjectRequest req = new JsonObjectRequest(Const.URL_API + "getMovie/" + movieid,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject movie) {
                        try {


                            String poster = "https://image.tmdb.org/t/p/w500" + movie.getString("poster_path");
                            String title = movie.getString("title");
                            int id = movie.getInt("id");
                            addImage1( layout2,poster, id, rating, day, month, username, title,otherid);

                        } catch (Exception e) {
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
                "getMovie/{id}");
    }
private void getMovie(int movieid,double rating,String month,String day) {
    JsonObjectRequest req = new JsonObjectRequest(Const.URL_API + "getMovie/" + movieid,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject movie) {
                    try {


                            String poster = "https://image.tmdb.org/t/p/w500" + movie.getString("poster_path");
                            String title = movie.getString("title");
                            int id = movie.getInt("id");
                            addImage( layout1,poster, title, id,rating, month,day);

                    } catch (Exception e) {
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
            "getMovie/{id}");
}
    private void addImage(LinearLayout layout,String url, String title, int id,double rating,String month,String day){

        layoutParams.setMargins(20, 20, 20, 20);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.height = 350;
        NetworkImageView imageView = new NetworkImageView(this);
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageView.setImageUrl(url, imageLoader);
        imageView.setLayoutParams(layoutParams);
        TextView titleView = new TextView(this);
        titleView.setText(title+"\nRating: "+rating+"/5\nDate: "+day+"/"+month);
        //titleView.setTextAlignment(View.TEXT_ALIGNMENT_LEFT);
        titleView.setTextColor(Color.WHITE);
        Typeface font = ResourcesCompat.getFont(this, R.font.notosansregular);
        titleView.setTypeface(font);
        titleView.setEllipsize(TextUtils.TruncateAt.END);
        titleView.setHorizontallyScrolling(true);
        LinearLayout view = new LinearLayout(this);
        view.setOrientation(LinearLayout.VERTICAL);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent act2 = new Intent(view.getContext(), MovieInfoActivity.class);
                act2.putExtra("id", id);
                startActivity(act2);
            }
        });

        view.addView(imageView);
        view.addView(titleView);
        layout.addView(view);

    }
    private void addImage1(LinearLayout layout,String url, int id,double rating,String day,String month,String username,String title,int otherid){
//, id,rating, username,title
        layoutParams.setMargins(20, 20, 20, 20);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.height = 350;
        NetworkImageView imageView = new NetworkImageView(this);
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageView.setImageUrl(url, imageLoader);
        imageView.setLayoutParams(layoutParams);
        TextView titleView = new TextView(this);
        titleView.setText(title+"\nRating: "+rating+"/5\nDate: "+day+"/"+month+"\nUser: "+username);
        //titleView.setTextAlignment(View.TEXT_ALIGNMENT_LEFT);
        titleView.setTextColor(Color.WHITE);
        Typeface font = ResourcesCompat.getFont(this, R.font.notosansregular);
        titleView.setTypeface(font);
        titleView.setEllipsize(TextUtils.TruncateAt.END);
        titleView.setHorizontallyScrolling(true);
        LinearLayout view = new LinearLayout(this);
        view.setOrientation(LinearLayout.VERTICAL);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent act2 = new Intent(view.getContext(), MovieInfoActivity.class);
                act2.putExtra("id", id);
                startActivity(act2);
            }
        });
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent act2 = new Intent(view.getContext(), otherUserActivity.class);
                act2.putExtra("id", otherid);
                startActivity(act2);
            }
        });
        view.addView(imageView);
        view.addView(titleView);
        layout.addView(view);

    }


    /**
     *  displays the users outgoing friend requests
     * @param layout linear layout
     * @param id friends id
     * @param user friends username
     * @param first friends first name
     * @param last friends last name
     */
//    private void addImage(LinearLayout layout, int id,String user,String first,String last){
//        layoutParams.setMargins(20, 20, 20, 20);
//        layoutParams.gravity = Gravity.CENTER;
//        layoutParams.height = 200;
//        TextView titleView = new TextView(this);
//        titleView.setText(user+"\n"+first+"\n"+last);
//        titleView.setLayoutParams(layoutParams);
//        //titleView.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
//        titleView.setTextColor(Color.WHITE);
//        Typeface font = ResourcesCompat.getFont(this, R.font.notosansregular);
//        titleView.setTypeface(font);
//        titleView.setEllipsize(TextUtils.TruncateAt.END);
//        titleView.setHorizontallyScrolling(true);
//        LinearLayout view = new LinearLayout(this);
//        view.setOrientation(LinearLayout.VERTICAL);
//        //Add global profile pages for other users to view and add as friend
//
//        titleView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent act2 = new Intent(view.getContext(), otherUserActivity.class);
//                act2.putExtra("id",id);
//                startActivity(act2);
//            }
//        });
//        view.addView(titleView);
//        layout.addView(view);
//
//    }

    /**
     * if user selects approves adds those users to friends status
     */

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
