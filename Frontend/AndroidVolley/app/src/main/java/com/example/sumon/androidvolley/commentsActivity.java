package com.example.sumon.androidvolley;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.sumon.androidvolley.app.AppController;
import com.example.sumon.androidvolley.utils.Const;
import com.example.sumon.androidvolley.utils.UserInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class commentsActivity extends AppCompatActivity {

    private TextView chatRoom;

    private EditText editComment;

    private ImageView backdropImage;
    private TextView titleText;
    private TextView chatText;

    private int movieID;
    private long userID;

    private TextView text;

    private Button commentButton;

    private String TAG = MovieInfoActivity.class.getSimpleName();
    Bitmap bitmap;

    private WebSocketClient cc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        UserInfo i = UserInfo.getInstance();
        userID = i.userId;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            movieID = extras.getInt("roomID");
        }

        chatRoom = (TextView)findViewById(R.id.chatRoom);
        editComment = (EditText)findViewById(R.id.editComment);
        commentButton = (Button)findViewById(R.id.commentButton);

        chatText = (TextView)findViewById(R.id.chatText);

        backdropImage = findViewById(R.id.commentBackdrop);

        getMovie();

        String path = "ws://coms-309-042.class.las.iastate.edu:8080/chat/" + userID + "/" + movieID;

try {
    cc = new WebSocketClient(new URI(path)) {
        @Override
        public void onOpen(ServerHandshake serverHandshake) {
            Log.d("OPEN", "run() returned: " + "is connecting");
        }

        @Override
        public void onMessage(String s) {
            Log.d("", "run() returned: " + s);
            if(!s.startsWith("[")){
                String str = s;
                try{
                    JSONObject obj = new JSONObject(str);
                    String mes = "";
                    mes += obj.getString("username") + ": " + obj.getString("content");
                    chatText.setText(chatText.getText() + "\n" + mes);
                }catch(Exception e) {

                }
            }
            else {
                String str = s;
                try{
                    JSONArray arr = new JSONArray(str);
                    for (int i=0; i< arr.length(); i++){
                        JSONObject obj = arr.getJSONObject(i);
                        String mes = "";
                        mes += obj.getString("username") + ": " + obj.getString("content");
                        chatText.setText(chatText.getText() + "\n" + mes);
                    }

                }catch(Exception e) {

                }
            }



        }

        @Override
        public void onClose(int i, String s, boolean b) {
            Log.d("CLOSE", "onClose() returned: " + s);
        }

        @Override
        public void onError(Exception e) {
            Log.d("Exception:", e.toString());
        }
    };
} catch (URISyntaxException e) {
             Log.d("Exception:", e.getMessage().toString());
             e.printStackTrace();
        }
        cc.connect();

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    cc.send(editComment.getText().toString());
                } catch (Exception e) {
                    Log.d("ExceptionSendMessage:", e.getMessage().toString());
                }
            }
        });

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

                            String backdrop = "https://image.tmdb.org/t/p/w500" + movie.getString("backdrop_path");

                            String title =  movie.getString("title");
                            chatRoom.setText("Leave your thoughts on '" + title + "'!");

                            new commentsActivity.GetImageFromUrl(backdropImage).execute(backdrop);

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

}