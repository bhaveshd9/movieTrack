
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

/**
 * All incoming or outgoing friend requests are displayed on this page
 * @author Aaron Gienger
 */
public class notificationsActivity extends Activity{

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
    private long otheruserid;
    private int request;
    private TextView textView;
    private TextView textView1;
    private TextView textView2;
    private TextView annText;
    private int role;
    private int annId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        user = UserInfo.getInstance();
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout = (LinearLayout) findViewById(R.id.ann_container);
        layout1 = (LinearLayout) findViewById(R.id.image_container4);
        layout2 = (LinearLayout) findViewById(R.id.image_container5);
        Listlayout = findViewById(R.id.list_Container);

        //addImage("https://th.bing.com/th/id/R.8a6cc9efd9dcbeea56702e596aa8275b?rik=lxlrFTa7zWgy%2bQ&pid=ImgRaw&r=0");
        annText = (TextView)findViewById(R.id.textViewAnn);
        userid = user.userId;
        getPotentialUsers();
        getFriends();
        getAnn();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int role1 = extras.getInt("role");
           role = role1;

        }
        editUser = (Button) findViewById(R.id.editUsername);
        homebutton = (Button) findViewById(R.id.HomeButton);
        profileButton = (Button) findViewById(R.id.Profile);
        searchButton = (Button) findViewById(R.id.search);
        friendsButton = (Button) findViewById(R.id.Friends);
        createListButton = (Button) findViewById(R.id.createList);
        textView = (TextView) findViewById(R.id.textView2);
        textView1 = (TextView) findViewById(R.id.textView3);
        textView2 = (TextView) findViewById(R.id.textViewAnn);
        if(role!=0){
            annText.setText("Announcement(+)");
        }
        annText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(role!=0) {

                    openAnnouncement();
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
    private void getAnn() {
        JsonArrayRequest req = new JsonArrayRequest(Const.URL_API + "ann/all",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray anns) {
                        try {

                                textView2.setVisibility(View.VISIBLE);
                                for (int i = 0; i < anns.length(); i++) {
                                    JSONObject ann = anns.getJSONObject(i);
                                    String title = ann.getString("title");
                                    String body = ann.getString("description");
                                    annId = ann.getInt("id");
                                    String date = ann.getString("date");
                                    String[] date1 = date.split("-");
                                    String day = date1[1];
                                    String month = date1[2];
                                    String[] month1 = month.split("T");
                                    String month2 = month1[0];
                                    addAnnouncement(layout, title, body, month2, day,annId);

                            }

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
                "ann/all");
    }
    /**
     * Gets the users pending friend requests that they have sent
     */
    private void getPotentialUsers(){
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET,Const.URL_API + "users/"+userid+"/potential_friends",null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if(response!=null) {
                                textView.setVisibility(View.VISIBLE);
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject movie = (JSONObject) response.get(i);
                                    String user = movie.getString("username");
                                    String first = movie.getString("firstName");
                                    String last = movie.getString("lastName");
                                    int id = movie.getInt("id");
                                    addImage(layout1, id, user, first, last);
                                }
                            }else{
                                    textView.setVisibility(View.INVISIBLE);
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
                "users/"+userid+"/potential_friends");
    }

    /**
     * Gets the users friend requests from other users
     */
    private void getFriends(){
        JsonArrayRequest req = new JsonArrayRequest(Const.URL_API + "users/"+userid+"/friend_requests",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if(response!=null) {
                                textView1.setVisibility(View.VISIBLE);
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject user = (JSONObject) response.get(i);
                                    String username = user.getString("username");
                                    String first = user.getString("firstName");
                                    String last = user.getString("lastName");
                                    int id = user.getInt("id");
                                    addfriends(layout2, id, username, first, last);
                                }
                            }else{
                                textView1.setVisibility(View.INVISIBLE);
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
    private void deleteAnn(int id){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.DELETE , Const.URL_API + "ann/delete/"+id,null,
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
                "ann/delete/{id}");
    }


    private void addAnnouncement(LinearLayout layout,String title, String body, String month, String day,int annId){
        layoutParams.setMargins(20, 20, 20, 20);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.height = 350;
        TextView titleView = new TextView(this);
        titleView.setTextSize(20);
        titleView.setText(title+"\n"+month+"/"+day+"\n"+body);
        titleView.setLayoutParams(layoutParams);
        //titleView.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
        titleView.setTextColor(Color.WHITE);
        Typeface font = ResourcesCompat.getFont(this, R.font.notosansregular);
        titleView.setTypeface(font);
        titleView.setEllipsize(TextUtils.TruncateAt.END);
        titleView.setHorizontallyScrolling(true);
        LinearLayout view = new LinearLayout(this);
        view.setOrientation(LinearLayout.VERTICAL);
        //Add global profile pages for other users to view and add as friend
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(role!=0) {
                    AlertDialog.Builder builder= new AlertDialog.Builder(notificationsActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Announcement");
                    builder.setMessage(title+"\n"+body);
                    builder.setNegativeButton("Close",null);
                    builder.setPositiveButton("Edit",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent act2 = new Intent(view.getContext(), AnnouncementActivity.class);
                            act2.putExtra("title", title);
                            act2.putExtra("description", body);
                            act2.putExtra("id",annId);
                            startActivity(act2);
                        }
                    });
                    builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AlertDialog.Builder builder= new AlertDialog.Builder(notificationsActivity.this);
                            builder.setCancelable(true);
                            builder.setTitle("Confirm");
                            builder.setPositiveButton("No",null);

                            builder.setNeutralButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteAnn(annId);
                                    recreate();
                                }
                            });


                            builder.show();

                        }
                    });


                    builder.show();

                }else{
                    AlertDialog.Builder builder= new AlertDialog.Builder(notificationsActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Announcement");
                    builder.setMessage(title+"\n"+body);
                    builder.setPositiveButton("Close",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });



                    builder.show();




                }
            }
        });
        view.addView(titleView);
        layout.addView(view);

    }


    private void addfriends(LinearLayout layout, int id,String user,String first,String last){
        layoutParams.setMargins(20, 20, 20, 20);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.height = 350;
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
        view.setOrientation(LinearLayout.VERTICAL);
        //Add global profile pages for other users to view and add as friend
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otheruserid=id;
                AlertDialog.Builder builder= new AlertDialog.Builder(notificationsActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Add Friend");
                builder.setPositiveButton("Accept",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        request=1;
                        approveRequest();
                    }
                });
                builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        request=0;
                        approveRequest();
                    }
                });
                builder.show();


            }
        });
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
    private void addImage(LinearLayout layout, int id,String user,String first,String last){
        layoutParams.setMargins(20, 20, 20, 20);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.height = 200;
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
        view.setOrientation(LinearLayout.VERTICAL);
        //Add global profile pages for other users to view and add as friend

        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent act2 = new Intent(view.getContext(), otherUserActivity.class);
                act2.putExtra("id",id);
                startActivity(act2);
            }
        });
        view.addView(titleView);
        layout.addView(view);

    }

    /**
     * if user selects approves adds those users to friends status
     */
    private void approveRequest(){

        JSONObject body= new JSONObject();
        try {
            body.put("id",user.userId);
            body.put("firstName", user.fName.toString());
            body.put("lastName", user.lName.toString());
            body.put("username", user.username.toString());
            //Store json in a string
            final String requestBody = body.toString();


            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,Const.URL_API + "users/"+otheruserid+"/approveRequest/"+request,null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                response.put("id",user.userId);
                                response.put("firstName", user.fName.toString());
                                response.put("lastName", user.lName.toString());
                                response.put("username", user.username.toString());
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
                    "users/{id}/approveRequest/{req}");
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
    public void openAnnouncement() {
        Intent intent = new Intent(this, AnnouncementActivity.class);
        startActivity(intent);
    }
}
