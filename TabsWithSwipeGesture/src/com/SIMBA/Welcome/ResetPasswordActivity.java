package com.SIMBA.Welcome;

import info.androidhive.tabsswipe.R;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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
import dataAccessPackage.JSONParser;

public class ResetPasswordActivity  extends Activity{
  
	EditText password;
	EditText retypePassword;
	Button confirmButton;
	TextView errorMsg;
	private String pass_,name_,repass_;
	private static String updateURL_ = "http://eblueberry.hostoi.com/simba/resetPassword.php";
	private static final String successKey  = "success";
	private static final String errorKey  = "error_msg";
    private static final String updateTag = "update";
	private static final String nameKey  = "name";
	private static final String passwordKey  = "password";
	JSONParser jsonParser;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resetpassword);
		
		//Passing reference to layout resources
		password       = (EditText)findViewById(R.id.passwordupdate);
		retypePassword = (EditText)findViewById(R.id.confirmPassword);
		confirmButton  = (Button)findViewById(R.id.confirmbtn);
		errorMsg = (TextView) findViewById(R.id.error);
		jsonParser = new JSONParser();
		
		Intent i = getIntent();
		name_ = i.getExtras().getString("name");
		confirmButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				new ResetPasswordTask().execute(updateURL_,name_);
			}
		});
	}
	

	 //function for checking whether any field is empty or not 
   private boolean checkFieldIsEmpty(String pass,String repass){
   	 if(((pass!=null) && !(pass.isEmpty())) && ((repass!=null) && !(repass.isEmpty()))){
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
	
 class ResetPasswordTask extends AsyncTask<String , String,String>{

	    private boolean check = false;
		@Override
		protected void onPreExecute() {
			   if(isNetworkAvailable())
			   {
				   pass_ = password.getText().toString();
				   repass_ = retypePassword.getText().toString();
				   if (checkFieldIsEmpty(pass_,repass_)){
					   if(pass_.length() > 5) { 
					   if(pass_.equals(repass_)){
					   jsonParser = new JSONParser();
					   errorMsg.setText(" "); 
					   }else{
						 errorMsg.setText("Password and ConfirmPassword  does not match..Please type again");
						 password.setText(" ");
						 retypePassword.setText(" ");
						 check = true;
			       }
			     }else{
					 errorMsg.setText("Your password must have atleast 6 characters...Please type again");
					 password.setText(" ");
					 retypePassword.setText(" ");
					 check = true;
		       }
			  }else{
					   errorMsg.setText("One of the field is empty Please fill.."); 
					   check = true;
				   }
			   }else{
				   errorMsg.setText("No Internet Connection!");
				   check = true;
			   }
		}

		@Override
		protected String doInBackground(String... params) {
			// Building Parameters
	        List<NameValuePair> resetPasswordParams_ = new ArrayList<NameValuePair>();
	        resetPasswordParams_.add(new BasicNameValuePair("tag", updateTag));
	        resetPasswordParams_.add(new BasicNameValuePair(nameKey, params[1]));
	        resetPasswordParams_.add(new BasicNameValuePair(passwordKey, pass_));
	        String message = null;
		
	        if(!check){
	        // getting JSON Object
	        JSONObject json = jsonParser.getJSONFromUrl("POST",params[0],resetPasswordParams_);

	        // check for register response
	        try
	        {
	        	if (json.getString(successKey) != null)
	        	{
	        		String res = json.getString(successKey); 
	                if(Integer.parseInt(res) == 1){
	                	
	                    //login intent created for calling login activity
	                    Intent loginIntent = new Intent(ResetPasswordActivity.this,LoginActivity.class);
	    	    		startActivity(loginIntent);
	    	    		
	    	    	    // Close CheckUser Activity
	                    finish();
	                }else{
	                	message =json.getString(errorKey);
	                }
	            
	            }
	        } catch (JSONException e){
	        	e.printStackTrace();
	        }
	    }  
	        return message;
		}     
		
		@Override
		protected void onPostExecute(String result) {
			
			if (isNetworkAvailable())
		    {
			    if(result!= null){
				   errorMsg.setText(result);
			}
			}else{
				errorMsg.setText("No internet connection");
			}
		}
   	
   }
	
}
