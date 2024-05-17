package com.example.sumon.androidvolley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Dialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.example.sumon.androidvolley.app.AppController;
import com.example.sumon.androidvolley.utils.Const;
import com.example.sumon.androidvolley.utils.UserInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Retrieves the movie list created by a user, and displays such data properly
 * @author Bradley McClellan
 *
 */
public class MovieListActivity extends AppCompatActivity {

    private TextView listName;


    private String listTitle;
    private long listID;
    EditText listEntry;

    EditText listEntry1;
    EditText listEntry2;

    private LinearLayout.LayoutParams layoutParams;
    private LinearLayout layout;
    private LinearLayout layout2;
    private TextView userLast;
    private TextView userFirst;
    private TextView username;
    private long userid;

    private Button addToListButton;
    private Button removeFromListButton;
    private Button reorderListButton;
    private ArrayList<movieListObj> movies = new ArrayList<movieListObj>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        UserInfo user = UserInfo.getInstance();

        listName = findViewById(R.id.listName);

        addToListButton = findViewById(R.id.addEntry);
        removeFromListButton = findViewById(R.id.removeEntry);
        reorderListButton = findViewById(R.id.reorderList);

        userFirst=(TextView)findViewById(R.id.usernameText);
        userFirst.setText(user.fName);
        username=(TextView)findViewById(R.id.username);
        username.setText("@" + user.username);
        layout =  (LinearLayout) findViewById(R.id.profilePic);
        layout2 = findViewById(R.id.listMovieContainer);
        userLast=(TextView)findViewById(R.id.firstnameText);
        userLast.setText(user.lName);
        addImage("https://th.bing.com/th/id/R.8a6cc9efd9dcbeea56702e596aa8275b?rik=lxlrFTa7zWgy%2bQ&pid=ImgRaw&r=0");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            long id = extras.getLong("id");
            String title = extras.getString("listName");
            listID = id;
            listTitle = title;
        }

        listName.setText(listTitle);
        getMovieLists();

        addToListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchPage();
            }
        });
        removeFromListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();

            }
        });
        reorderListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSwapDialog();
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
        layout.addView(view);

    }

    /**
     * opens the Search page
     */
    public void openSearchPage() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("id", listID);
        intent.putExtra("name", listTitle);
        startActivity(intent);
    }

    /**
     *  gets the user list to display
     */
    private void getMovieLists(){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Const.URL_API + "list/" + listID,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("TEST", response.toString());
                            JSONArray moviesInList = (JSONArray) response.getJSONArray("moviesInList");
                            JSONObject list = (JSONObject) response.getJSONObject("list");
                            JSONArray listEntries = (JSONArray) list.getJSONArray("listEntries");
//                            for (int i = 0; i < listEntries.length(); i++) {
//                                JSONObject movie = (JSONObject) listEntries.get(i);
//                                int movie_Id = movie.getInt("movieID");
//                                int position = movie.getInt("listPosition");
//                                getMovie(movie_Id, position);
//                            }
                            for (int i =0; i<moviesInList.length(); i++){
                                JSONObject entry = (JSONObject) moviesInList.getJSONObject(i);
                                JSONObject movie = (JSONObject) entry.getJSONObject("movie");
                                String poster = "https://image.tmdb.org/t/p/w500" + movie.getString("poster_path");
                                String title =  movie.getString("title");
                                JSONObject le = (JSONObject) entry.getJSONObject("listEntry");
                                movieListObj mov = new movieListObj();
                                mov.setid(le.getInt("movieID"));
                                mov.setPos(le.getInt("listPosition"));
                                movies.add(mov);
                                addImage(poster, title, le.getInt("movieID"), le.getInt("listPosition"));
                            }

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Log.d(TAG, "TESTF:");
               // Log.d(TAG, "Error: " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(req,
                "list/{listId}");
    }

    /**
     *  Sends a JSON Delete request to remove said entry from list
     */
    private void removeFromList() {
        JSONObject jsonBody = new JSONObject();
        try {

            final String requestBody = jsonBody.toString();
            Long listNum = Long.valueOf(listEntry.getText().toString());

            jsonBody.put("listId", listID);
            jsonBody.put("listPosition", listNum);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE,
                    Const.URL_API + "deleteEntry/" +  listID + "/" + listNum, null,
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
            AppController.getInstance().addToRequestQueue(jsonObjReq, "deleteEntry/{listId}/{listPosition}");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  Gets the movie by ID
     * @param id The movieID which can identify a specific movie
     * @param pos the position of the movie in the list
     */
    private void getMovie(int id, int pos){
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET, Const.URL_API + "getMovie/" + id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject movie = (JSONObject) response;
                            String poster = "https://image.tmdb.org/t/p/w500" + movie.getString("poster_path");
                            String title =  movie.getString("title");
                            movieListObj mov = new movieListObj();
                            mov.setid(id);
                            mov.setPos(pos);
                            movies.add(mov);
                            addImage(poster, title, id, pos);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d(TAG, "TESTF:");
                //Log.d(TAG, "Error: " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(req,
                "getMovie/{id}");
    }

    /**
     * helper class to create an object which contains a movies position and id in the list
     * also has set and get methods for both
     */
    public class movieListObj{
        int pos;
        int id;
        public void setPos(int pos){
            this.pos = pos;
        }

        public int getPos(){
            return this.pos;
        }

        public void setid(int id){
            this.id = id;
        }

        public int getid(){
            return this.id;
        }
    }

    /**
     *  takes an image URL and finds the image to be displayed in a linearLayout
     * @param url url of the image poster for the movie
     * @param title title of the movie to be displayed with the image
     * @param id    MovieID which maps to a specific movie
     * @param position  Position of the movie in the list
     */
    private void addImage(String url, String title, int id, int position){
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 20, 20, 20);
        layoutParams.gravity = Gravity.LEFT;
        layoutParams.height = 350;
        NetworkImageView imageView = new NetworkImageView(this);
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageView.setImageUrl(url, imageLoader);
        imageView.setLayoutParams(layoutParams);
        TextView posView = new TextView(this);
        posView.setText("" + position);
        posView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        posView.setTextColor(Color.WHITE);
        Typeface font = ResourcesCompat.getFont(this, R.font.notosansregular);
        posView.setTypeface(font);
        posView.setTextSize(35);
        posView.setEllipsize(TextUtils.TruncateAt.END);
        posView.setVerticalScrollBarEnabled(true);

        TextView titleView = new TextView(this);
        titleView.setText(title);
        titleView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        titleView.setTextColor(Color.WHITE);
        titleView.setTypeface(font);
        titleView.setTextSize(25);
        titleView.setEllipsize(TextUtils.TruncateAt.END);
        titleView.setVerticalScrollBarEnabled(true);
        LinearLayout view = new LinearLayout(this);
        view.setOrientation(LinearLayout.HORIZONTAL);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent act2 = new Intent(view.getContext(), MovieInfoActivity.class);
                act2.putExtra("id", id);
                act2.putExtra("listId", listID);
                startActivity(act2);
            }
        });
        view.addView(posView);
        view.addView(imageView);
        view.addView(titleView);
        layout2.addView(view);

    }
    /**
     *  Shows a dialog prompting which entry should be removed
     */
    public void showDialog() {      // pops up dialog and listens for back button
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.remove_from_list);

        Button closeDialog = dialog.findViewById(R.id.backFromPopUp);
        listEntry = (EditText)dialog.findViewById(R.id.EditTextEntry);

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFromList();
                refresh();
                dialog.dismiss();

            }
        });

        dialog.show();
    }

     /**
     *  Shows a dialog prompting which entries should be swapped
     */
    public void showSwapDialog() {      // pops up dialog and listens for back button
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_swap_list);

        Button closeDialog = dialog.findViewById(R.id.backFromPopUp);
        listEntry1 = (EditText)dialog.findViewById(R.id.EditTextEntry1);
        listEntry2 = (EditText)dialog.findViewById(R.id.EditTextEntry2);

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapList();
                refresh();
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    /**
     *  refreshes the page so data is displayed correctly after an entry is added or deleted
     */
    public void refresh(){
        recreate();
        finish();
        Intent intent = new Intent(this, MovieListActivity.class);
        intent.putExtra("listName", listTitle);
        intent.putExtra("id", listID);
        startActivity(intent);
    }

    /**
     * swaps two elements in the movie list and leaves the rest in original positions
     */
    public void swapList() {

       JSONArray arr = new JSONArray();
        try {

            int entry1 = Integer.valueOf(listEntry1.getText().toString());
            int entry2 = Integer.valueOf(listEntry2.getText().toString());

            for(int i = 0; i < movies.size(); i++) {
                JSONObject jsonBody = new JSONObject();

                if(movies.get(i).getPos() == entry1){
                    jsonBody.put("movieID", movies.get(i).getid());
                    jsonBody.put("listPosition", entry2);
                }
                else if(movies.get(i).getPos() == entry2) {
                    jsonBody.put("movieID", movies.get(i).getid());
                    jsonBody.put("listPosition", entry1);
                }
                else {
                    jsonBody.put("movieID", movies.get(i).getid());
                    jsonBody.put("listPosition", movies.get(i).getPos());
                }
                arr.put(i, jsonBody);
            }



            //Store json in a string
            final String requestBody = arr.toString();

            JsonArrayRequest jsonArrReq = new JsonArrayRequest(Request.Method.POST,
                    Const.URL_API + "setEntries/" +  listID, null,
                    new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
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
            AppController.getInstance().addToRequestQueue(jsonArrReq, "setEntries/{listId}");

            refresh();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    
}