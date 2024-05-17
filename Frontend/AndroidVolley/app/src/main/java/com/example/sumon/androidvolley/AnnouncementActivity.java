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
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;


public class AnnouncementActivity extends Activity {
    private Button homebutton;
    private Button searchButton;
    private Button friendsButton;
    private Button profileButton;
    private String TAG = AnnouncementActivity.class.getSimpleName();
    private LinearLayout.LayoutParams layoutParams;
    private ProgressDialog pDialog;

    private UserInfo user;
    private long otheruserid;
    private EditText title;
    private EditText body1;
    private Button post;
    private int annID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ann);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        user = UserInfo.getInstance();
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        homebutton = (Button) findViewById(R.id.HomeButton);
        profileButton = (Button) findViewById(R.id.Profile);
        searchButton = (Button) findViewById(R.id.search);
        friendsButton = (Button) findViewById(R.id.Friends);

        post = (Button)findViewById(R.id.postButton);
        title = (EditText)findViewById(R.id.titleText);
        body1 = (EditText)findViewById(R.id.bodyText);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title1 = extras.getString("title");
            String body2 = extras.getString("description");
            int annId = extras.getInt("id");
            title.setText(title1);
            body1.setText(body2);
            annID = annId;
            post.setText("Edit");
        }

        post.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               if(extras==null) {
                   postAnnouncementByObject();
                   openNotif();
               }else{
                   editAnnText();
                   editAnnTitle();
               }
            }
        });

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
    private void po() {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,Const.URL_API + "ann/newPost",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                                    String title = response.getString("title");
                                    String body = response.getString("description");

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
                "ann/newPost");
    }
    public void editAnnText(){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT,Const.URL_API + "ann/editText/"+annID+"/"+body1.getText(),null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            response.put("description", body1.getText().toString());
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

                    jsonBody.put("description",body1.getText());
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
                "ann/editText/{id}/{newText}");
    }
    public void editAnnTitle(){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT,Const.URL_API + "ann/editTitle/"+annID+"/"+title.getText(),null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                             response.put("title", title.getText().toString());
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

                    jsonBody.put("title",title.getText());
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
                "ann/editTitle/{id}/{newTitle}");
    }
    /**
     * if user selects approves adds those users to friends status
     */
    private void postAnnouncementByObject(){

        JSONObject body= new JSONObject();
        try {

            body.put("title", title.getText());
            body.put("description",body1.getText());
            //Store json in a string
            final String requestBody = body.toString();


            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,Const.URL_API + "ann/newPost",null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                response.put("title", title.getText().toString());
                                response.put("description",body1.getText().toString());
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
                    "ann/newPost");
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
    public void openNotif() {
        Intent intent = new Intent(this, notificationsActivity.class);
        startActivity(intent);
    }
}

