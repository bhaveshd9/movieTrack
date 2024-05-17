package com.example.sumon.androidvolley;

import static android.app.PendingIntent.getActivity;

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

/**
 * Displays popular movies, and allows for mobility between main pages
 * @author Bradley McClellan
 * @author Aaron Greiner
 */
public class HomeActivity extends Activity {
    private Button homebutton;
    private Button searchButton;
    private Button friendsButton;
    private Button profileButton;
    private TextView helloText;
    private LinearLayout layout;
    private LinearLayout layout1;
    private String TAG = HomeActivity.class.getSimpleName();
    private LinearLayout.LayoutParams layoutParams;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        showProgressDialog();
        UserInfo user = UserInfo.getInstance();
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        helloText = (TextView) findViewById(R.id.helloText);
        layout = (LinearLayout) findViewById(R.id.image_container);
        layout1 = (LinearLayout) findViewById(R.id.image_container1);
        helloText.setText("Hello, " + user.fName);
        getPopular();
        getUpcoming();
        hideProgressDialog();

        homebutton = (Button)findViewById(R.id.HomeButton);
        profileButton = (Button)findViewById(R.id.Profile);
        searchButton = (Button)findViewById(R.id.search);
        friendsButton =(Button)findViewById(R.id.Friends);
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
     * opens home page
     */
    public void openHomePage() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    /**
     * opens profile page
     */
    public void openProfilePage() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    /**
     * opens search page
     */
    public void openSearchPage() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    /**
     * opens friends page
     */
    public void openFriendsPage() {
        Intent intent = new Intent(this, FriendActivity.class);
        startActivity(intent);
    }

    /**
     *  Sends a request for a JSON array of movie objects for display
     */
    private void getPopular(){
        JsonArrayRequest req = new JsonArrayRequest(Const.URL_API + "getPopular",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject movie = (JSONObject) response.get(i);
                                String poster = "https://image.tmdb.org/t/p/w500" + movie.getString("poster_path");
                                String title =  movie.getString("title");
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
                "getPopular");
    }

    private void getUpcoming(){
        JsonArrayRequest req = new JsonArrayRequest(Const.URL_API + "getUpcoming",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject movie = (JSONObject) response.get(i);
                                String poster = "https://image.tmdb.org/t/p/w500" + movie.getString("poster_path");
                                String title =  movie.getString("title");
                                int id = movie.getInt("id");
                                addImageUpcoming(poster, title, id);
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
                "getPopular");
    }

    /**
     *  Takes an image URL, and displays said image in a linear layout
     *  @param url url that leads to the movie poster image
     *  @param title title of the movie to be displayed along with the image
     *  @param id movieId which leads to movie info page for said movie
     */
    private void addImage(String url, String title, int id){

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

    private void addImageUpcoming(String url, String title, int id){

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
        layout1.addView(view);

    }

    /**
     * shows dialog indicating loading
     */
    private void showProgressDialog() {
        if (!pDialog.isShowing())

            pDialog.show();
    }
    /**
     * hides dialog indicating loading
     */
    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }


}
