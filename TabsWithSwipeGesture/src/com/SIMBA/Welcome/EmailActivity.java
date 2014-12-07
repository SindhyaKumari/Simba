package com.SIMBA.Welcome;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import dataAccessPackage.JSONParser;

import info.androidhive.tabsswipe.R;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EmailActivity extends Activity
{
	private Button continueEmail;
	private EditText email;
	private TextView errorMsg;
	private String email_,username_, name_;
	private static String userExistURL_ = "http://eblueberry.hostoi.com/simba/";
	private static final String successKey  = "success";
	private static final String errorKey  = "error_msg";
    private static final String userExistTag = "userExist";
	private static final String emailKey  = "email";
	JSONParser jsonParser;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email);

		email = (EditText)findViewById(R.id.emailaddress);
		continueEmail = (Button)findViewById(R.id.continue_email);
		errorMsg = (TextView)findViewById(R.id.emailError);
		
		email_ = getEmailID();
		if(email_ != null){
		email.setText(email_, TextView.BufferType.EDITABLE);
		}
		
		Intent i = getIntent();
		name_ = i.getExtras().getString("name");
		
		continueEmail.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//Checking whether input is null or not
				email_= email.getText().toString();
				username_ = getUsername(email_);
				if(isEmailValid(email_) && !(email_.equals("")))
				{
					errorMsg.setVisibility(View.GONE);
					new UserExistingTask().execute(userExistURL_,email_); 
				}
				else
				{
					errorMsg.setVisibility(View.VISIBLE);
					errorMsg.setText("Please enter a valid email address.");
				}
			}
		});
	}
	
	public static boolean isEmailValid(String email)
	{
		boolean isValid = false;

	    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	    CharSequence inputStr = email;

	    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (matcher.matches())
	    {
	    	isValid = true;
	    }
	    return isValid;
	}
	
	public String getUsername(String email)
	{
		String[] parts = email.split("@");
		if (parts.length > 0 && parts[0] != null)
		{
			return parts[0];
		}
		else
		{
			return null;
		}
	}
	
	public String getEmailID()
	{
		AccountManager manager = AccountManager.get(this);
		Account[] accounts = manager.getAccountsByType("com.google");
		List<String> possibleEmails = new LinkedList<String>();
		
		for (Account account : accounts)
		{
			// TODO: Check possibleEmail against an email regex or treat
		    // account.name as an email address only for certain account.type
		    // values.
			possibleEmails.add(account.name);
		}
		if (!(possibleEmails.isEmpty()) && possibleEmails.get(0) != null)
		{
			String email = possibleEmails.get(0);
			Log.e("ffff hyey getemailid", email);
		    return email;
		}
		else
		{
			return null;
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
	class UserExistingTask extends AsyncTask<String , String,String>{

		@Override
		protected void onPreExecute() {
			   if(isNetworkAvailable())
			   {
				  jsonParser = new JSONParser();
			   }else{
				   errorMsg.setText("No Internet Connection!");
			   }
		}

		@Override
		protected String doInBackground(String... params) {
			// Building Parameters
	        List<NameValuePair> userExistParams_ = new ArrayList<NameValuePair>();
	        userExistParams_.add(new BasicNameValuePair("tag", userExistTag));
	        userExistParams_.add(new BasicNameValuePair(emailKey,params[1]));
	        String message = null;
		
	        // getting JSON Object
	        JSONObject json = jsonParser.getJSONFromUrl("POST",params[0],userExistParams_);

	        // check for register response
	        try
	        {
	        	if (json.getString(successKey) != null)
	        	{
	        		String res = json.getString(successKey); 
	                if(Integer.parseInt(res) == 1){
	                	
	                	//Calling password activity intent
						Intent passwordIntent = new Intent(EmailActivity.this,PasswordActivity.class);
						passwordIntent.putExtra("name", name_);
						passwordIntent.putExtra("username",username_);
						passwordIntent.putExtra("email",email_);
						startActivity(passwordIntent);
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
			    	errorMsg.setVisibility(View.VISIBLE);
			    	errorMsg.setText(result);
			    	email.setText(" ");
			}
			}else{
				errorMsg.setVisibility(View.VISIBLE);
				errorMsg.setText("No internet connection");
			}
		}
    	
    }
		
}
