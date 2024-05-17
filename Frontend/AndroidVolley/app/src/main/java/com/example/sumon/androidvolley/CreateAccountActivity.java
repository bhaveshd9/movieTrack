package com.example.sumon.androidvolley;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.sumon.androidvolley.app.AppController;
import com.example.sumon.androidvolley.utils.Const;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Allows for the creation of a new account
 * @author Bradley McClellan
 *
 */
public class CreateAccountActivity extends AppCompatActivity {

    private Button return_to_login;
    private Button createAccount;
    private Button exitDialog;
    private EditText firstName;
    private EditText lastName;
    private EditText username;
    private EditText password;
    private EditText passwordConfirm;
    private TextView textViewInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);


        firstName = findViewById(R.id.editTextFirstName);
        lastName = findViewById(R.id.editTextLastName);
        username = findViewById(R.id.editTextUsername);
        password = findViewById(R.id.editTextCreatePassword);
        passwordConfirm = findViewById(R.id.editTextPasswordConfirm);
        textViewInfo = findViewById(R.id.textViewFeedback);
        createAccount = findViewById(R.id.CreateAccountButton);
        return_to_login = findViewById(R.id.ReturnToLoginButton);
        exitDialog = findViewById(R.id.backFromDialog);

        createAccount.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                CreateUser();
            }
        });


        return_to_login.setOnClickListener(new View.OnClickListener()
        {

                @Override
                public void onClick(View v) {
                    ReturnToLogin();
                }
        });


// Use the Builder class for convenient dialog construction
    }

    /**
     * sends the user back to the login page
     */
    public void ReturnToLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     *  Creates a User and sends te user info to backend
     */
    private void CreateUser() {
        //Logic to check passwords
        //Logic to check for special chars in username, fistname, lastname
        //Show a warning and retrun if any errors/mismatch
        JSONObject jsonBody = new JSONObject();
        try {
            boolean passwordMatch = checkPasswords();
            boolean userNameChars = checkUserName();
            if(passwordMatch == false)  // return and display message if passwords do not match
            {
                textViewInfo.setText("Passwords do not match");
                return;
            }
            else if(userNameChars == false) // return and display message if username has illigal chars
            {
                textViewInfo.setText("UserName cannot contain special characters");
                return;
            }

            jsonBody.put("username", username.getText());
            jsonBody.put("firstName", firstName.getText());
            jsonBody.put("lastName", lastName.getText());
            jsonBody.put("password", password.getText());

            //Store json in a string
            final String requestBody = jsonBody.toString();



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Const.URL_API + "users/" , null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //show success if response is correct
                            Log.d("CREATE", response.toString());
                            showDialog();   // pop up dialog and wait for response
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("CREATE", "Error: " + error.getMessage());

                if (error instanceof TimeoutError || error instanceof NoConnectionError)
                {
                    textViewInfo.setText("Connection Error");
                }
                else if (error instanceof AuthFailureError)
                {
                    textViewInfo.setText("Authorization Failed");
                }
                else if (error instanceof ServerError)  // usually username that already exists
                {
                    textViewInfo.setText("Server Failure (Try different username)");
                }
                else if (error instanceof NetworkError)
                {
                    textViewInfo.setText("Network Error");
                }
                else if (error instanceof ParseError)
                {
                    textViewInfo.setText("Server response invalid");
                }
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
        AppController.getInstance().addToRequestQueue(jsonObjReq, "create_user");

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
     *  Checks if the passwords match
     * @return returns true if passwords match
     */
   public boolean checkPasswords()
   {
       String pass1 = password.getText().toString();
       String pass2 = passwordConfirm.getText().toString();
       if(pass1.equals(pass2))
       {
           return true;
       }
       else
       {
           return false;
       }
   }

    /**
     *  Makes sure the username doesn't contain illegal chars
     * @return returns true or false based on username limitations
     */
    public boolean checkUserName()
    {
        String s = username.getText().toString();
        char[] cArr = s.toCharArray();

        for(char c : cArr)
        {
            if(!Character.isLetter(c) && !Character.isDigit(c))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * sends the user back to the login page
     */
    public void backToLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     *  Shows a dialog that communicates successful account creation
     */
    public void showDialog() {      // pops up dialog and listens for back button
    Dialog dialog = new Dialog(this);
    dialog.setContentView(R.layout.pop_up_dialog);

    Button closeDialog = dialog.findViewById(R.id.backFromDialog);

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                backToLogin();
            }
        });


    dialog.show();
    }

}
