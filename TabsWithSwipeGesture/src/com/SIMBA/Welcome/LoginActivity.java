package com.SIMBA.Welcome;



import info.androidhive.tabsswipe.MainActivity;
import info.androidhive.tabsswipe.R;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import dataAccessPackage.JSONParser;
import dataAccessPackage.LoginSession;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity{

	private Button loggedInButton;
	private Button forgotButton;
	private EditText emailAddress;
	private EditText password;
	private TextView loginErrorMsg;
	
	// using  http://eblueberry.hostoi.com/blueberry/
	private static String loginURL_ = "http://eblueberry.hostoi.com/blueberry/";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		//Passing reference to layout resources
		loggedInButton = (Button)findViewById(R.id.loggedinbtn);
		forgotButton = (Button)findViewById(R.id.forgotbtn);
		emailAddress = (EditText)findViewById(R.id.emailedittext);
		password = (EditText)findViewById(R.id.passwordedittext);
		loginErrorMsg = (TextView)findViewById(R.id.errortextview);
		
		/* Login Button  ClickListener  */
		loggedInButton.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				new LoginTask().execute(loginURL_);
			}
		});
		
		/* Forgot Button  ClickListener  */
		forgotButton.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				
				    // Forgot intent created
                	Intent forgotIntent = new Intent(LoginActivity.this,ForgotActivity.class);
                	startActivity(forgotIntent);				
			}
		});
		
		
				
   }
	
	 void setLoginErrorMsg(String message_){
		  loginErrorMsg.setText(message_);
	}
	/*
	 * Asyntask for login purpose making request to global(mysql) database
	 * for getting user email and password 
	 */
   class LoginTask extends AsyncTask<String,String,String>{

	private static final String successKey  = "success";
	private static final String errorKey  = "error_msg";
	private static final String emailKey  = "email";
	private static final String nameKey  = "name";
    private static final String loginTag = "login";
    ProgressDialog dialog;
    JSONParser jsonParser;
	
    //Before starting background thread show progress dailog
	@Override
	protected void onPreExecute() {
		dialog = ProgressDialog.show( LoginActivity.this, "Wait..", "Logging In", true, false);
		jsonParser = new JSONParser();
		loginErrorMsg.setText(" ");
	}

	
    //Login request is being passed to global database
	@Override
	protected String doInBackground(String... params) {
		
		String email_ = emailAddress.getText().toString();
        String password_ = password.getText().toString();
        String message = null;
       
        
        // Building Parameters
        List<NameValuePair> loginParams_ = new ArrayList<NameValuePair>();
        loginParams_.add(new BasicNameValuePair("tag", loginTag));
        loginParams_.add(new BasicNameValuePair("email", email_));
        loginParams_.add(new BasicNameValuePair("password", password_));
        JSONObject json = jsonParser.getJSONFromUrl("POST",params[0], loginParams_);


        // check for login response
        try {
            if (json.getString(successKey) != null) {               
                String res = json.getString(successKey); 
                
                if(Integer.parseInt(res) == 1){ 
                 
                 // Store user details in shared preferences
                 LoginSession loginSession = new LoginSession(LoginActivity.this);
                 JSONObject jsonUser = json.getJSONObject("user");
                 
                 
                 // Clear all previous data in sharedpreferences
                 loginSession.clearDataAfterLogout();
                 loginSession.createLoginSession(jsonUser.getString(emailKey),jsonUser.getString(nameKey));
                 
                 
                 // Launch Dashboard Screen
                 Intent dashBoardIntent = new Intent(LoginActivity.this,MainActivity.class);
            	 startActivity(dashBoardIntent);
                    // Close all views before launching Dashboard
                  //  dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  //  startActivity(dashboard);
                     
                    // Close Login Screen
                 finish();
                }else{
                   // Error in login
                   message = json.getString(errorKey) ;
              }
            }
        } catch (JSONException e) {
            e.printStackTrace();
       }
        
        return message;
	}
	   
	
	@Override
	protected void onPostExecute(String result) {
		// display message if error occurred while login 
		if(result != null){
			dialog.dismiss();
			AlertDialog builder = new AlertDialog.Builder(LoginActivity.this)
			.setTitle("Incorrect Username or Password")
			.setMessage("The Username or Password you entered is incorrect. Please try again.")
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int which) { 
			        // Your code
			    }
			 }).show();
			//loginErrorMsg.setText(result);
		}		
	}
   }	

}

