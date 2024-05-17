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
 * Search Movies page
 * @author Aaron Gienger
 */
public class SearchActivity extends Activity{




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
    private long listID;
    private String listName;
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
            long id = extras.getLong("id");
            String listTitle = extras.getString("name");
            listID = id;
            listName = listTitle;
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
                getMovie(search);

            }
        });

    }

    /**
     * Returns movies given a search term
     * @param search search object
     */
    private void getMovie(String search){
        JsonObjectRequest req = new JsonObjectRequest(Const.URL_API + "searchMovie/"+search,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray recs=response.getJSONArray("results");
                            for(int i=0;i< recs.length();i++) {
                                JSONObject movie = recs.getJSONObject(i);
                                String poster = "https://image.tmdb.org/t/p/w500" + movie.getString("poster_path");
                                String title = movie.getString("title");
                                int id = movie.getInt("id");
                                addImage(poster, title, id);
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
                "searchMovie/{term}");
    }

    /**
     * Adds the movies to display
     * @param url picture URL
     * @param title Movie title
     * @param id Movie Id
     */
    private void addImage(String url, String title, int id){
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 20, 20, 20);
        layoutParams.gravity = Gravity.LEFT;
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
        titleView.setTextSize(35);
        titleView.setEllipsize(TextUtils.TruncateAt.END);
        titleView.setVerticalScrollBarEnabled(true);
        LinearLayout view = new LinearLayout(this);
        view.setOrientation(LinearLayout.HORIZONTAL);
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent act2 = new Intent(view.getContext(), MovieInfoActivity.class);
                act2.putExtra("id", id);
                act2.putExtra("listId", listID);
                act2.putExtra("listTitle", listName);
                startActivity(act2);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent act2 = new Intent(view.getContext(), MovieInfoActivity.class);
                act2.putExtra("id", id);
                act2.putExtra("listId", listID);
                act2.putExtra("listTitle", listName);
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
