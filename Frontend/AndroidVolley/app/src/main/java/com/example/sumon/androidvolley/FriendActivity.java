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

import java.util.HashMap;
import java.util.Map;

import com.example.sumon.androidvolley.utils.UserInfo;

/**
 * Displays friends and ability to search for users
 * @author Aaron Gienger
 */
public class FriendActivity extends Activity{
    private Button feed;
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
        setContentView(R.layout.activity_friends);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        UserInfo user = UserInfo.getInstance();
        userid=user.userId;
        url="https://th.bing.com/th/id/R.8a6cc9efd9dcbeea56702e596aa8275b?rik=lxlrFTa7zWgy%2bQ&pid=ImgRaw&r=0";
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //layout = (LinearLayout) findViewById(R.id.image_container3);
        layout1 = (LinearLayout) findViewById(R.id.image_container2);
        //layout2 = (LinearLayout) findViewById(R.id.image_container8);
        userText=(TextView)findViewById(R.id.firstnameText);
        //userText.setText(user.lName);
        userName=(TextView)findViewById(R.id.usernameText);
        //userName.setText(user.fName);
        getFriends();
        //getUsers();
        //getPotentialUsers();
        homebutton = (Button)findViewById(R.id.HomeButton);
        feed = (Button)findViewById(R.id.feed);
        profileButton = (Button)findViewById(R.id.Profile);
        searchButton = (Button)findViewById(R.id.search);
        friendsButton =(Button)findViewById(R.id.Friends);
        searchUser= (Button)findViewById(R.id.searchUser);
        searchUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                searchUserPage();
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
        feed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                feedPage();
            }
        });
    }

    /**
     *  Gets users friends information to be displayed
      */
    private void getFriends(){
        JsonArrayRequest req = new JsonArrayRequest(Const.URL_API + "users/"+userid+"/friends",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject movie = (JSONObject) response.get(i);
                                String user = movie.getString("username");
                                String first =  movie.getString("firstName");
                                String last = movie.getString("lastName");
                                int id=movie.getInt("id");
                                addImage(layout1,id,user,first,last);
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
                "users/{id}/friends");
    }
    /**
     * Displays users friends and gives button to access friends profile
     * @param layout which linear layout object being put into
     * @param id friends id
     * @param user friends username
     * @param first friends first name
     * @param last friends last name
     */
    private void addImage(LinearLayout layout, int id,String user,String first,String last){
        layoutParams.setMargins(20, 20, 20, 20);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.height = 200;
        NetworkImageView imageView = new NetworkImageView(this);
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageView.setImageUrl(url, imageLoader);
        imageView.setLayoutParams(layoutParams);
        TextView titleView = new TextView(this);
        titleView.setText(user+"\n"+first+"\n"+last);
        titleView.setLayoutParams(layoutParams);
        //titleView.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
        titleView.setTextColor(Color.WHITE);
        Typeface font = ResourcesCompat.getFont(this, R.font.notosansregular);
        titleView.setTypeface(font);
        titleView.setEllipsize(TextUtils.TruncateAt.END);
        titleView.setHorizontallyScrolling(true);

        LinearLayout view = new LinearLayout(this);
        view.setOrientation(LinearLayout.HORIZONTAL);
        //Add global profile pages for other users to view and add as friend
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent act2 = new Intent(view.getContext(), userFriendsActivity.class);
                act2.putExtra("id",id);
                startActivity(act2);
            }
        });
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent act2 = new Intent(view.getContext(), userFriendsActivity.class);
                act2.putExtra("id",id);
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
    public void feedPage() {
        Intent intent = new Intent(this, LogsActivity.class);
        startActivity(intent);
    }
}
