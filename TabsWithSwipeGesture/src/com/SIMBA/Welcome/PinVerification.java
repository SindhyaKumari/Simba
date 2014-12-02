package com.SIMBA.Welcome;

import info.androidhive.tabsswipe.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.SIMBA.Welcome.SignUpActivity.RegistrationTask;
import com.email.backgroundmaillibrary.BackgroundMail;
import com.email.backgroundmaillibrary.Utils;

import dataAccessPackage.JSONParser;

public class PinVerification extends Activity
{
	private Button btverify_;
	private EditText useremail_, pinuser_;
	private TextView errorMsg;
	private String email_, pin_, verify_;
	private static String registerURL_ = "http://eblueberry.hostoi.com/simba/";
	//private int PIN;
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verification);
		context = this;

		pinuser_ = (EditText)findViewById(R.id.pin);
		useremail_ = (EditText)findViewById(R.id.email);
		btverify_ = (Button)findViewById(R.id.verify_pin);
		errorMsg = (TextView)findViewById(R.id.usernameError);
				
		btverify_.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//Checking whether input is null or not
				if (isNetworkAvailable())
				{
					if(!(pinuser_.getText().toString().equals("")) && !(useremail_.getText().toString().equals("")))
					{
						new RegistrationTask().execute(registerURL_);
					}
					else
					{
						errorMsg.setText("Enter the PIN and Email!");
					}
				}
				else
				{
					errorMsg.setText("No Internet Connection!");
				}
			}
		});
	}
	
	private boolean isNetworkAvailable()
	{
		boolean isConnected = false;
		ConnectivityManager connectManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectManager.getActiveNetworkInfo();
		
		//if no network is available networkInfo will be null else it will be connected
		if(networkInfo != null && networkInfo.isConnected())
		{
			isConnected = true;
		}
		return isConnected;		 
	}
	
	/*
	 * Asyntask for registration purpose making request to global(mysql) database
	 * for storing user details 
	 */
	
   class RegistrationTask extends AsyncTask<String,String,String>
   {
	   private ProgressDialog pDialog;
	   private static final String successKey  = "success";
	   private static final String errorKey  = "error_msg";
	   private static final String emailKey  = "email";
	   private static final String nameKey  = "name";
	   private static final String passwordKey  = "password";
	   private static final String answerKey  = "answer";
	   private static final String registerTag = "verify";
	   private static final String pinnumber = "pin_number";
	   private static final String verify = "verification";
	   private boolean checkFlag = false;
	   JSONParser jsonParser;
	   
	   //Before starting background thread show progress dailog
	   @Override
	   protected void onPreExecute()
	   {
		   if (isNetworkAvailable())
		   {
			   jsonParser = new JSONParser();
			   pDialog = new ProgressDialog(PinVerification.this);
			   pDialog.setMessage("Please wait...");
			   pDialog.setIndeterminate(false);
			   pDialog.setCancelable(false);
			   pDialog.show();
		   }
		   else
		   {
			   errorMsg.setText("No Internet Connection!");
		   }
	   }
	   
	   //Login request is being passed to global database
	   
	   @Override
	   protected String doInBackground(String... params)
	   {
		   if (isNetworkAvailable())
		   {
			   String message = null;
			   email_= useremail_.getText().toString();
			   pin_= pinuser_.getText().toString();
 
			   // Building Parameters
			   List<NameValuePair> registerParams_ = new ArrayList<NameValuePair>();
			   registerParams_.add(new BasicNameValuePair("tag", registerTag));
		       registerParams_.add(new BasicNameValuePair(emailKey, email_));
		       registerParams_.add(new BasicNameValuePair(pinnumber, pin_));
		       registerParams_.add(new BasicNameValuePair(verify, verify_));
		       // System.out.println();
        
		       // getting JSON Object
		       JSONObject json = jsonParser.getJSONFromUrl("POST",params[0], registerParams_);
        
		       Log.e("json",json.toString());

		       // check for register response
		       try
		       {
		    	   if (json.getString(successKey) != null)
		    	   {
		    		   String res = json.getString(successKey); 
		    		   if(Integer.parseInt(res) == 1)
		    		   {
		    			   // user successfully registered
		    			   JSONObject json_user = json.getJSONObject("user");
		    		   }
		    		   else
		    		   {
		    			   // Error in registration
		    			   message = json.getString(errorKey);
                   
		    		   }      
		    	   }
		       }
		       catch (JSONException e)
		       {
		    	   e.printStackTrace();
		       }
		       return message;
		   }
		   else
		   {
			   pDialog.dismiss();
			   errorMsg.setText("No Internet Connection!");
			   return null;
		   }
	   }
	   
	   @Override
	   protected void onPostExecute(String result)
	   {
		   // dismiss the dialog on  updated
		   if (isNetworkAvailable())
		   {
			   pDialog.dismiss();
			   if(result != null)
			   {
				   Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
			   }
			   else
			   {
				   Toast.makeText(getApplicationContext(), "Your account has been verified , please login", Toast.LENGTH_LONG).show();
				   //Opening login activity after successful registration
				   Intent registerIntent = new Intent(PinVerification.this,LoginActivity.class);
				   startActivity(registerIntent);
				   // Close Registration Activity
				   finish();
			   }
		   }
		   else
		   {
			   pDialog.dismiss();
			   errorMsg.setText("No Internet Connection!");
		   }
	   }
   }
}