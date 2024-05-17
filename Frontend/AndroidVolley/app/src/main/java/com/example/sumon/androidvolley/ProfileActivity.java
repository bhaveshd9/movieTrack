package com.example.sumon.androidvolley;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Retrieves the info of a user, and displays said info
 * @author Bradley McClellan
 * @author Aaron Greiner
 *
 */
public class ProfileActivity extends Activity {
    private Button homebutton;
    private Button searchButton;
    private Button friendsButton;
    private Button profileButton;
    private Button notif;
    private TextView userLast;
    private TextView userFirst;
    private TextView username;
    private EditText listName;
    private ImageView layout;
    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout Listlayout;
    private String TAG = HomeActivity.class.getSimpleName();
    private LinearLayout.LayoutParams layoutParams;
    private ProgressDialog pDialog;
    private Button editUser;
    private Button createListButton;
    private Button Admin;
    private Button Mod;
    private Button logout;
    private long userid;
    private UserInfo user;
    private int role;
    private long otheruserid;
    private String url;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        user = UserInfo.getInstance();
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout =  findViewById(R.id.profilePic);
        layout1 =  (LinearLayout) findViewById(R.id.image_container4);
        layout2 =  (LinearLayout) findViewById(R.id.image_container5);
        Listlayout = findViewById(R.id.list_Container);
        userLast=(TextView)findViewById(R.id.firstnameText);
        url= "https://www.clipartkey.com/mpngs/m/198-1988954_staff-profile-photo-facebook-blank-profile-picture-male.png";
        new GetImageFromUrl(layout).execute(url);
        userLast.setText(user.lName);
        userid= user.userId;
        role = user.role;
        Admin = (Button)findViewById(R.id.adminButton);
        Mod = (Button) findViewById(R.id.modButton);
        if(role==1){
            Admin.setVisibility(View.VISIBLE);
        }
        if(role==2){
            Mod.setVisibility(View.VISIBLE);
        }

        getMovielog();
        getMovieLists();
       // getProfileImage();
        //getFriends();
        userFirst=(TextView)findViewById(R.id.usernameText);
        userFirst.setText(user.fName);
        username=(TextView)findViewById(R.id.username);
        username.setText("@" + user.username);
        notif = (Button)findViewById(R.id.notifications);
        editUser = (Button)findViewById(R.id.editUsername);
        homebutton = (Button)findViewById(R.id.HomeButton);
        profileButton = (Button)findViewById(R.id.Profile);
        searchButton = (Button)findViewById(R.id.search);
        friendsButton =(Button)findViewById(R.id.Friends);
        createListButton = (Button) findViewById(R.id.createList);
        logout = (Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                logout();
            }
        });
        Admin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                adminPage();
            }
        });
        Mod.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                ModPage();
            }
        });
        notif.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                Intent act2 = new Intent(v.getContext(), notificationsActivity.class);
                act2.putExtra("role", role);
                startActivity(act2);


            }
        });

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
        editUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){

                openeditUserPage();
            }
        });
        createListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    /**
     *  takes an image URL and finds the image to be displayed in a linearLayout
     * @param url the url which leads to a specific image
     */
    private void addImage(String url){
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
        //layout.addView(view);

    }
    public class GetImageFromUrl extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        /**
         *  Takes in a url to the image, and converts it to a BitMap to be displayed in an imageView
         * @param img imageview container to which the image will be placed.
         */
        public GetImageFromUrl(ImageView img){
            this.imageView = img;
        }
        @Override
        protected Bitmap doInBackground(String... url) {
            String stringUrl = url[0];
            bitmap = null;
            InputStream inputStream;
            try {
                inputStream = new java.net.URL(stringUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap){
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }
    private void getProfileImage(){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Const.URL_API + "image/"+user.username,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                                if(response.getString("url")!=null) {
                                    url = response.getString("url");
                                    new GetImageFromUrl(layout).execute(url);
//                                }else{
//                                    url = "https://www.nailseatowncouncil.gov.uk/wp-content/uploads/blank-profile-picture-973460_1280.jpg";
//                                    new GetImageFromUrl(layout).execute(url);
//                                }
                                //addImage(url);
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
                "image/{username}");
    }


    /**
     *  Gets the users logged movie history
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
                                    addImage(layout1,poster, title, id);
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
     *  takes a URL and creates a linear layout view to display said image
     * @param id the ID of a particular movie to display
     * @param layout  a layout for the image to be placed in
     * @param title   a title to be displayed with the image
     * @param url   the url which leads to the poster of the movie
     */
    private void addImage(LinearLayout layout,String url, String title, int id){

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

    /**
     * opens the notifications page
     */
    public void notifPage() {
        Intent intent = new Intent(this, notificationsActivity.class);
        startActivity(intent);
    }

    /**
     * opens the friends page
     */
    public void openFriendsPage() {
        Intent intent = new Intent(this, FriendActivity.class);
        startActivity(intent);
    }

    /**
     * opens the EditUser page
     */
    public void openeditUserPage() {
        Intent intent = new Intent(this, editUserActivity.class);
        startActivity(intent);
    }

    /**
     * opens the Home page
     */
    public void openHomePage() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    /**
     * opens the Profile page
     */
    public void openProfilePage() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    /**
     * opens the Search page
     */
    public void openSearchPage() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    /**
     * opens the List page
     */
    public void openListPage() {
        Intent intent = new Intent(this, MovieListActivity.class);
        startActivity(intent);
    }

    /**
     * opens the admin page
     */
    public void adminPage() {
        Intent intent = new Intent(this,AdminActivity.class);
        startActivity(intent);
    }
    public void ModPage() {
        Intent intent = new Intent(this,ModeratorActivity.class);
        startActivity(intent);
    }
    public void logout() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


    /**
     *  Creates a new list for users to add movies to
     */
    private void CreateList() {

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("listName", listName.getText());

            //Store json in a string
            final String requestBody = jsonBody.toString();

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    Const.URL_API + "list/" +  userid, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //show success if response is correct
                                Log.d("CREATE", response.toString());

                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("CREATE", "Error: " + error.getMessage());

                }
            }) {

                /**
                 * Passing some request headers
                 * */
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
                public byte[] getBody()  {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (Exception uee) {
                        return null;
                    }
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq, "create_list");

        } catch (Exception e) {
            e.printStackTrace();
        }
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(jsonObjReq,
//                tag_login);
//
//        // Cancelling request
//        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);

    }

    /**
     *  Shows a dialog prompting the user to enter a name for their list
     */
    public void showDialog() {      // pops up dialog and listens for back button
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.create_list_popup);

        Button closeDialog = dialog.findViewById(R.id.backFromCreateList);
        listName = (EditText)dialog.findViewById(R.id.editTextListName);

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CreateList();
                dialog.dismiss();
                openListPage();

            }
        });
        
        dialog.show();
    }

    /**
     *  Gets the lists tied to the users ID
     */
    private void getMovieLists(){
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, Const.URL_API + "lists/" + userid,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject list = (JSONObject) response.get(i);
                                String title = "  " + list.getString("listName") + "  ";
                                int id = list.getInt("id");
                                addList(title, id);
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
                "lists/{userId}");
    }

    /**
     *  Adds the lists of a users profile to a scroll view, which can be clicked to enter the list
     * @param id the ID of the list to be added to the view
     * @param title the title of the list to be displayed
     */
    private void addList(String title, long id){

        layoutParams.setMargins(20, 20, 20, 20);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.height = 350;
//        NetworkImageView imageView = new NetworkImageView(this);
//        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
//        imageView.setImageUrl(url, imageLoader);
//        imageView.setLayoutParams(layoutParams);
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

        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MovieListActivity.class);
                intent.putExtra("listName", title);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        view.addView(titleView);
        Listlayout.addView(view);

    }

}
