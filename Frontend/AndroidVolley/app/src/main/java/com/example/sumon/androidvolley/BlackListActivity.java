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


public class BlackListActivity extends Activity{


    private Button homebutton;
    private Button searchButton;
    private Button friendsButton;
    private Button profileButton;
    private TextView userText;
    private LinearLayout layout;
    private String TAG = editUserActivity.class.getSimpleName();
    private LinearLayout.LayoutParams layoutParams;
    private ProgressDialog pDialog;
    private Button enter;
    private EditText movieId;
    private long userid;
    private UserInfo user;
    private int role;
    private EditText profileURL;
    private Button urlEnter;
    private int movieblackid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklist);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        user = UserInfo.getInstance();
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        userText=(TextView)findViewById(R.id.usernameText);
        //userText.setText(user.fName);
        userid=user.userId;
        role = user.role;
        //getMovie();
        homebutton = (Button)findViewById(R.id.HomeButton);
        profileButton = (Button)findViewById(R.id.Profile);
        searchButton = (Button)findViewById(R.id.search);
        enter = (Button)findViewById(R.id.blackListEnter);
        movieId = (EditText) findViewById(R.id.movieId);

        enter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                blacklistMovie();
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



    }
    private void getMovie(){
        JsonArrayRequest req = new JsonArrayRequest(Const.URL_API + "blacklist",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                                for(int i=0;i<response.length();i++) {
                                    JSONObject movie = (JSONObject)response.get(i);
                                    int id = movie.getInt("movieId");

                                    addImage(id);
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
     * @param id Movie Id
     */
    private void addImage( int id){
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 20, 20, 20);
        layoutParams.gravity = Gravity.LEFT;
        layoutParams.height = 350;

        TextView titleView = new TextView(this);
        titleView.setText(id);
        titleView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        titleView.setTextColor(Color.WHITE);
        Typeface font = ResourcesCompat.getFont(this, R.font.notosansregular);
        titleView.setTypeface(font);
        titleView.setTextSize(35);
        titleView.setEllipsize(TextUtils.TruncateAt.END);
        titleView.setVerticalScrollBarEnabled(true);
        LinearLayout view = new LinearLayout(this);
        view.setOrientation(LinearLayout.HORIZONTAL);

        view.addView(titleView);
        layout.addView(view);

    }
    public void blacklistMovie(){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Const.URL_API + "blacklist/"+movieId.getText(),null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

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
                "blacklist/{movieId}");
    }
    /**
     * Users ability to change their username
     */

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
