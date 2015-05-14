package com.SIMBA.Welcome;

import info.androidhive.tabsswipe.R;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import dataAccessPackage.JSONParser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CheckUserActivity  extends Activity
{
	EditText emailAddress;
	EditText answer;
	Button continueButton;
	TextView checkUserError;
	private String answer_,email_;
	private static String updateURL_ = "http://eblueberry.hostoi.com/simba/resetPassword.php";
	private static final String successKey  = "success";
	private static final String errorKey  = "error_msg";
    private static final String checkUserTag = "checkUser";
	private static final String emailKey  = "email";
	private static final String answerKey  = "answer";
	JSONParser jsonParser;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkemail);
		
		//Passing reference to layout resources
		emailAddress   = (EditText)findViewById(R.id.emailupdate);
		answer         = (EditText)findViewById(R.id.enteranswer);
		continueButton  = (Button)findViewById(R.id.continuebtn);
		checkUserError = (TextView)findViewById(R.id.error);
		
		continueButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				new CheckUserTask().execute(updateURL_);
			}	
		});
	}
	

	 //function for checking whether any field is empty or not 
    private boolean checkFieldIsEmpty(String email,String answer){
    	 if(((email!=null) && !(email.isEmpty()) )&& ((answer!=null) && !(answer.isEmpty()))){
    	  return true;
      }
    	else {
    		 return false;
    	}
    }
    

	//Function for connection whether internet connection is avaiable or not
	private boolean isNetworkAvailable(){
		 boolean isConnected = false;
		 
		 ConnectivityManager connectManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		 NetworkInfo networkInfo = connectManager.getActiveNetworkInfo();
		 
		 //if no network is available networkInfo will be null else it will be connected
		 if(networkInfo != null && networkInfo.isConnected()){
			   isConnected = true;
		 }
		
		 return isConnected;		 
	}
	
  class CheckUserTask extends AsyncTask<String , String,String>{

		@Override
		protected void onPreExecute() {
			   if(isNetworkAvailable())
			   {
				   email_ = emailAddress.getText().toString();
				   answer_ = answer.getText().toString();
				   if (checkFieldIsEmpty(email_,answer_)){
					   jsonParser = new JSONParser();
					   checkUserError.setText(" "); 
				   }else{
					   checkUserError.setText("Please enter your email and answer "); 
				   }
			   }else{
				   checkUserError.setText("No Internet Connection!");
			   }
		}

		@Override
		protected String doInBackground(String... params) {
			// Building Parameters
	        List<NameValuePair> checkUserParams_ = new ArrayList<NameValuePair>();
	        checkUserParams_.add(new BasicNameValuePair("tag", checkUserTag));
	        checkUserParams_.add(new BasicNameValuePair(emailKey, email_));
	        checkUserParams_.add(new BasicNameValuePair(answerKey, answer_));
	        String message = null;
		
	        // getting JSON Object
	        JSONObject json = jsonParser.getJSONFromUrl("POST",params[0],checkUserParams_);

	        // check for register response
	        try
	        {
	        	if (json.getString(successKey) != null)
	        	{
	        		String res = json.getString(successKey); 
	                if(Integer.parseInt(res) == 1){
	                	
	                	String[] splitEmail = email_.split("@");
	                    //Reset Password intent created for calling reset password activity
	                    Intent resetPasswordIntent = new Intent(CheckUserActivity.this,ResetPasswordActivity.class);
	                    resetPasswordIntent.putExtra("name", splitEmail[0]);
	    	    		startActivity(resetPasswordIntent);
	    	    		
	    	    	    // Close CheckUser Activity
	                    finish();
	                }else{
	                	message =json.getString(errorKey);
	                }
	            
	            }
	        } catch (JSONException e){
	        	e.printStackTrace();
	        }
	        
	        return message;
		}     
		
		@Override
		protected void onPostExecute(String result) {
			
			if (isNetworkAvailable()){
			    if(result!= null){
			    	checkUserError.setText(result);
			}
			}else{
				checkUserError.setText("No internet connection");
			}
		}
    	
    }
	
}
