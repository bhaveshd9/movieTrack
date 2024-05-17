package com.example.sumon.androidvolley;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * Retrieves the info of a particular movie, and displays said info
 * @author Bradley McClellan
 */
public class MovieInfoActivity extends AppCompatActivity {

    private String TAG = MovieInfoActivity.class.getSimpleName();
    private ProgressDialog pDialog;

    private ImageView posterImage;
    private ImageView backdropImage;

    private int movieID;
    private Button listButton;
    private long userid;

    private Button homebutton;
    private Button searchButton;
    private Button friendsButton;
    private Button profileButton;

    private Button commentButton;

    private TextView titleText;
    private TextView overViewText;

    private LinearLayout layout;
    private LinearLayout.LayoutParams layoutParams;

    private RatingBar movieRatingBar;
    private Button submitRating;
    private TextView stars;

    private long listID;
    private String listName;

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movieinfo);

        layout = (LinearLayout) findViewById(R.id.image_container);
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        listButton = (Button) findViewById(R.id.listMovie);
        listButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                listMovie();
            }
        });
        UserInfo user = UserInfo.getInstance();
        userid = user.userId;

        posterImage = findViewById(R.id.poster);
        backdropImage = findViewById(R.id.backdrop);

        titleText = findViewById(R.id.TitleText);
        overViewText = findViewById(R.id.OverViewText);
        overViewText.setMovementMethod(new ScrollingMovementMethod());

        movieRatingBar = findViewById(R.id.ratingBar);
        submitRating = findViewById(R.id.rateMovie);
        stars = findViewById(R.id.MovieRating);

        commentButton = findViewById(R.id.commentButton);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openComments();
            }
        });

        submitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stars.setText("You Rated " + movieRatingBar.getRating() + " Stars!");
                logMovie();
            }
        });

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        hideProgressDialog();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int id = extras.getInt("id");
            long listId = extras.getLong("listId");
            String listTitle = extras.getString("listTitle");
            movieID = id;
            listID = listId;
            listName = listTitle;
            getMovie();
            getSimilar();
        }

        getRating();

    }

    public void openComments() {
        Intent intent = new Intent(this, commentsActivity.class);
        intent.putExtra("roomID", movieID);
        intent.putExtra("userID", userid);
        startActivity(intent);
    }


    /**
     * Sends a request for an array of similar movies to diplay on the page
     */
    private void getSimilar(){
        JsonArrayRequest req = new JsonArrayRequest(Const.URL_API + "getSimilar/" + movieID,
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
                "getSimilar/{id}");
    }

    /**
     * Sends a request to retrieve a movie by the movie ID
     */
    private void getMovie(){
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET, Const.URL_API + "getMovie/" + movieID, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject movie = (JSONObject) response;
                            String poster = "https://image.tmdb.org/t/p/w500" + movie.getString("poster_path");
                            String backdrop = "https://image.tmdb.org/t/p/w500" + movie.getString("backdrop_path");
                            String overview = movie.getString("overview");
                            String title =  movie.getString("title");
                            addTitle(title);
                            addOverview(overview);
                            new GetImageFromUrl(posterImage).execute(poster);
                            new GetImageFromUrl(backdropImage).execute(backdrop);

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
                "getMovie/{id}");
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

    /**
     * adds title to the view
     * @param text title of movie
     */
    private void addTitle(String text)
    {
        titleText.setText(text);
    }
    /**
     * adds overview text to the view
     * @param text overview of movie
     */
    private void addOverview(String text)
    {
        overViewText.setText(text);
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


    /**
     *  takes an image URL and finds the image to be displayed in a linearLayout
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

    /**
     *  Logs the movie to the users profile page
     */
    public void logMovie(){
        JsonObjectRequest req = new JsonObjectRequest
                (Request.Method.POST,Const.URL_API + "log/"+userid,null,
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
                    jsonBody.put("movie", movieID);
                    jsonBody.put("rating", movieRatingBar.getRating());

                    //Store json in a string
                    final String requestBody = jsonBody.toString();
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };

        AppController.getInstance().addToRequestQueue(req, "users/{id}");
    }

    /**
     *  Adds a movie to a list created by the user
     */
    public void listMovie() {
        JSONObject jsonBody = new JSONObject();
        try {

            jsonBody.put("movieID", movieID);

            //Store json in a string
            final String requestBody = jsonBody.toString();

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    Const.URL_API + "addEntry/" +  listID, null,
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
            AppController.getInstance().addToRequestQueue(jsonObjReq, "addEntry/{listId}");

            openListPage();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * opens the List page if user came to add to list
     */
    public void openListPage() {
        if(listName != null)
        {
            Intent intent = new Intent(this, MovieListActivity.class);
            intent.putExtra("listName", listName);
            intent.putExtra("id", listID);
            startActivity(intent);
        }
    }

    /**
     *  gets and displays the rating if the user has rated the movie before
     */
    public void getRating() {
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET, Const.URL_API + "hasLogged/" + movieID + "/" + userid, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject log = (JSONObject) response;
                            double rating = log.getDouble("rating");
                            float r = (float) rating;
                            movieRatingBar.setRating(r);
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
                "hasLogged/{movieID}/{userID}");
    }
}
