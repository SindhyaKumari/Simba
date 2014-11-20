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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotActivity  extends Activity{

	EditText emailAddress;
	EditText password;
	EditText retypePassword;
	Button confirmButton;
	private String pass_,email_,repass_;
	private static String updateURL_ = "http://eblueberry.hostoi.com/blueberry/";
	private static final String successKey  = "success";
	private static final String errorKey  = "error_msg";
    private static final String updateTag = "update";
	private static final String emailKey  = "email";
	private static final String passwordKey  = "password";
	JSONParser jsonParser;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot);
		
		//Passing reference to layout resources
		emailAddress   = (EditText)findViewById(R.id.emailupdate);
		password       = (EditText)findViewById(R.id.passwordupdate);
		retypePassword = (EditText)findViewById(R.id.confirmPassword);
		confirmButton  = (Button)findViewById(R.id.confirmbtn);
		jsonParser = new JSONParser();
		
		confirmButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				email_  = emailAddress.getText().toString();
			    pass_   = password.getText().toString(); 
				repass_ = retypePassword.getText().toString();
				if(pass_.equals(retypePassword.getText().toString())){
					
					// Building Parameters
			        List<NameValuePair> updateParams_ = new ArrayList<NameValuePair>();
			        updateParams_.add(new BasicNameValuePair("tag", updateTag));
			        updateParams_.add(new BasicNameValuePair(emailKey, email_));
			        updateParams_.add(new BasicNameValuePair(passwordKey, pass_));
				
			        // getting JSON Object
			        JSONObject json = jsonParser.getJSONFromUrl("POST",updateURL_, updateParams_);

			        // check for register response
			        try {
			            if (json.getString(successKey) != null) {
			       	        
			                String res = json.getString(successKey); 
			                if(Integer.parseInt(res) == 1){
			                    
			                	// user successfully updated
			                    Toast.makeText(getApplicationContext(), "your password has been  reset", Toast.LENGTH_LONG).show();
			                    
			                    //login intent created for calling login activity
			                    Intent loginIntent = new Intent(ForgotActivity.this,LoginActivity.class);
			    	    		startActivity(loginIntent);
			    	    		
			    	    	    // Close Forgot Activity
			                    finish();
			  
			                }else{
			                    // Error in updation
			                	 Toast.makeText(getApplicationContext(), json.getString(errorKey)+ "has been  updated", Toast.LENGTH_LONG).show();
			                }
			            
			            }
			         }catch (JSONException e) {
			            e.printStackTrace();
			      }
			    }     
			}
		});
	}
	
	

}
