package com.example.sumon.androidvolley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.sumon.androidvolley.app.AppController;
import com.example.sumon.androidvolley.utils.Const;
import com.example.sumon.androidvolley.utils.UserInfo;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Allows for login and creation of an account
 * @author Bradley McClellan
 * @author Aaron Greiner
 */
public class MainActivity extends Activity implements OnClickListener {
    private Button btnLogin;
    private Button Create_Account_Btn;
    private EditText editUsername;
    private EditText editPassword;
    private TextView errorMessage;
    private String TAG = MainActivity.class.getSimpleName();
    private String tag_login = "login_req";
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = (Button) findViewById(R.id.loginButton);
        Create_Account_Btn = (Button) findViewById(R.id.CreateAccountButton);
        editUsername = (EditText) findViewById(R.id.editTextUsername);
        editPassword = (EditText) findViewById(R.id.editTextPassword);
        errorMessage = (TextView) findViewById(R.id.errorMessage);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        hideProgressDialog();

        // button click listeners
        btnLogin.setOnClickListener(this);

        Create_Account_Btn.setOnClickListener(new View.OnClickListener() {

            @Override
           public void onClick(View v){
               openCreateAccountPage();
           }
        });

    }

    /**
     * opens a page that allows a user to register an account
     */
    public void openCreateAccountPage() {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                Log.d(TAG, "PRESS");
                logUserIn();
                break;
            default:
                break;
        }
    }

    /**
     * Logs a user in, or asks them to create an account
     */
    private void logUserIn() {
        showProgressDialog();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Const.URL_API + "login/"+ editUsername.getText() + "/" + editPassword.getText(), null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            errorMessage.setText("");
                            Log.d(TAG, response.toString());
                            Log.d(TAG, response.getString("firstName"));
                            UserInfo user = UserInfo.getInstance();
                            user.fName = response.getString("firstName");
                            user.lName = response.getString("lastName");
                            user.userId = response.getInt("id");
                            user.password = response.getString("password");
                            user.role = response.getInt("role");
                            user.username = response.getString("username");
                            hideProgressDialog();
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Log.d(TAG, "Error: " + error.getMessage());
                errorMessage.setText("There was a problem logging you in, please check your Username and Password");
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


        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                tag_login);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

}
