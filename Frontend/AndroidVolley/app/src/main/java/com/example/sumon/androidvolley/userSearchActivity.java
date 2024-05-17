package com.example.sumon.androidvolley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
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
 * Search for a profile
 * @author Aaron Gienger
 */
public class userSearchActivity extends Activity {

    private Button homebutton;
    private Button searchButton;
    private Button friendsButton;
    private Button profileButton;
    private Button searchEnter;
    private TextView userText;
    private EditText searchBar;
    private LinearLayout layout;
    private String TAG = SearchActivity.class.getSimpleName();
    private LinearLayout.LayoutParams layoutParams;
    private ProgressDialog pDialog;
    private String search;
    private int listID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        //UserInfo user = UserInfo.getInstance();
        layout =  (LinearLayout) findViewById(R.id.image_container1);
        userText=(TextView)findViewById(R.id.usernameText);
        homebutton = (Button)findViewById(R.id.HomeButton);
        profileButton = (Button)findViewById(R.id.Profile);
        searchButton = (Button)findViewById(R.id.search);
        searchEnter = (Button) findViewById(R.id.searchEnter);
        searchBar = (EditText) findViewById(R.id.editSearchBar);
        friendsButton = (Button)findViewById(R.id.Friends);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int id = extras.getInt("id");
            listID = id;
        }

        homebutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){openHomePage();
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
            public void onClick(View v){openFriendsPage();}
        });
        searchEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search=searchBar.getText().toString();
                LinearLayout ll=(LinearLayout) findViewById(R.id.image_container1);
                ll.removeAllViews();
                getUser(search);

            }
        });

    }

    /**
     * Gets the users information given search
     * @param search username from search bar
     */
    private void getUser(String search){
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET,Const.URL_API + "searchUser/"+search,null,
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
                "searchUser/{term}");
    }

    /**
     * Adds user from search to display
     * @param url image URL
     * @param first user first
     * @param last user last
     * @param username user username
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
                Intent act2 = new Intent(view.getContext(), otherUserActivity.class);
                act2.putExtra("id", id);
                startActivity(act2);
            }
        });

        view.addView(imageView);
        view.addView(titleView);
        layout.addView(view);

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
     * opens Friends Page
     */
    public void openFriendsPage() {
        Intent intent = new Intent(this, FriendActivity.class);
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
