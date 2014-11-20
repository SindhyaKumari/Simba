package com.SIMBA.Welcome;

import info.androidhive.tabsswipe.R;

import java.util.ArrayList;
import java.util.List;



import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;



import dataAccessPackage.JSONParser;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationActivity  extends Activity{
    
	private Button signUpButton;
	private EditText name;
	private EditText email;
	private EditText password;
	private EditText contact;
	private EditText dob;
	private EditText address;
	private TextView registerErrorMsg;	
	private static String registerURL_ = "http://eblueberry.hostoi.com/blueberry/";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		//Passing reference to layout resources
		name = (EditText) findViewById(R.id.regname);
		email = (EditText) findViewById(R.id.regemail);
		password = (EditText) findViewById(R.id.regpassword);
		contact = (EditText) findViewById(R.id.regcontact);
		dob = (EditText) findViewById(R.id.regdob);
		address = (EditText) findViewById(R.id.regaddress);
		registerErrorMsg = (TextView)findViewById(R.id.registerError);
		signUpButton = (Button)findViewById(R.id.registerbtn);
		
		registerErrorMsg.setText("Note : No field should be empty");
		
		signUpButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new RegistrationTask().execute(registerURL_);

			}
		});
	}
	
	boolean checkValueOfTextField(String name_,String email_,String contact_,String dob_,String address_){
        
		boolean flag = true;
		if ((name_.isEmpty())&&  (email_.isEmpty()) &&  (contact_.isEmpty()) &&  (dob_.isEmpty()) 
				&&  (address_.isEmpty())){	
		   flag = false;
		}
		return flag;
   }

	/*
	 * Asyntask for registration purpose making request to global(mysql) database
	 * for storing user details 
	 */
   class RegistrationTask extends AsyncTask<String,String,String>{

	private ProgressDialog pDialog;
	private static final String successKey  = "success";
	private static final String errorKey  = "error_msg";
	private static final String emailKey  = "email";
	private static final String nameKey  = "name";
	private static final String passwordKey  = "password";
	private static final String contactKey  = "contact";
	private static final String dobKey  = "dob";
	private static final String addressKey  = "address";
    private static final String registerTag = "register";
    private boolean checkFlag = false;
    JSONParser jsonParser;
    
	
    //Before starting background thread show progress dailog
	@Override
	protected void onPreExecute() {
		jsonParser = new JSONParser();
	    pDialog = new ProgressDialog(RegistrationActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();     
	}

	
    //Login request is being passed to global database
	@Override
	protected String doInBackground(String... params) {
		
		String email_ = email.getText().toString();
        String password_ = password.getText().toString();
        String name_ = name.getText().toString();
        String contact_ = contact.getText().toString();
        String dob_ = dob.getText().toString();
        String address_ = address.getText().toString();
        String message = null;
        
        //checking value of inputs
        checkFlag = checkValueOfTextField(name_,email_,contact_,dob_,address_);
        
        if(checkFlag){
        // Building Parameters
        List<NameValuePair> registerParams_ = new ArrayList<NameValuePair>();
        registerParams_.add(new BasicNameValuePair("tag", registerTag));
        registerParams_.add(new BasicNameValuePair(nameKey, name_));
        registerParams_.add(new BasicNameValuePair(emailKey, email_));
        registerParams_.add(new BasicNameValuePair(passwordKey, password_));
        registerParams_.add(new BasicNameValuePair(contactKey, contact_));
        registerParams_.add(new BasicNameValuePair(dobKey, dob_));
        registerParams_.add(new BasicNameValuePair(addressKey, address_));
        
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl("POST",params[0], registerParams_);
        
        Log.e("json",json.toString());

        // check for register response
        try {
            if (json.getString(successKey) != null) {
       	        
                String res = json.getString(successKey); 
                if(Integer.parseInt(res) == 1){
                    
                	// user successfully registered
                    JSONObject json_user = json.getJSONObject("user");
  
                }else{
                    // Error in registration
                   message = json.getString(errorKey);
                   
                }      
            }
         }catch (JSONException e) {
            e.printStackTrace();
      }
    }else {
    	  // Error in input
        message = "One of field is empty do fill it";    	   
    }        
        return message;
}
	   
	
	@SuppressLint("ShowToast") @Override
	protected void onPostExecute(String result) {
		// dismiss the dialog on  uupdated
		pDialog.dismiss();
		if(result != null){
			Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(getApplicationContext(), "Your account has been created , please login", Toast.LENGTH_LONG).show();
			//Opening login activity after successful registration
            Intent registerIntent = new Intent(RegistrationActivity.this,LoginActivity.class);
    		startActivity(registerIntent);
    		
            // Close Registration Activity
            finish();
		}
	}
   }	

    
}
