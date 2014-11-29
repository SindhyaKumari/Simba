package com.SIMBA.Welcome;


import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.androidhive.tabsswipe.R;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EmailActivity extends Activity{
	
	private Button continueEmail;
	private EditText email;
	private TextView errorMsg;
	private String email_,username_;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email);
		

		email = (EditText)findViewById(R.id.emailaddress);
		continueEmail = (Button)findViewById(R.id.continue_email);
		errorMsg = (TextView)findViewById(R.id.emailError);
		
		email_ = getEmailID();
		email.setText(email_, TextView.BufferType.EDITABLE);
		//email_= email.getText().toString();
		
		Intent i = getIntent();
		username_ = i.getExtras().getString("name");
		username_ = getUsername(email_);
		//Log.e("user name", username_);
		
		continueEmail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Checking whether input is null or not
				 email_= email.getText().toString();
				 if(isEmailValid(email_)){
					 errorMsg.setVisibility(View.GONE);
					 //Calling password activity intent
					 Intent passwordIntent = new Intent(EmailActivity.this,PasswordActivity.class);
					 passwordIntent.putExtra("username",username_);
					 passwordIntent.putExtra("email",email_);
					 startActivity(passwordIntent);	 
				 }else{
					 errorMsg.setVisibility(View.VISIBLE);
					 errorMsg.setText("Please enter a valid email address.");
				 }
			}
		});
	}
	
	public static boolean isEmailValid(String email) {
	    boolean isValid = false;

	    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	    CharSequence inputStr = email;

	    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (matcher.matches()) {
	        isValid = true;
	    }
	    return isValid;
	}
	
	public String getUsername(String email) {
		String[] parts = email.split("@");
		if (parts.length > 0 && parts[0] != null)
			return parts[0];
		else
			return null;
	}
	
	public String getEmailID() {
		AccountManager manager = AccountManager.get(this);
		Account[] accounts = manager.getAccountsByType("com.google");
		List<String> possibleEmails = new LinkedList<String>();
		
		for (Account account : accounts) {
			// TODO: Check possibleEmail against an email regex or treat
		    // account.name as an email address only for certain account.type
		    // values.
			possibleEmails.add(account.name);
		}
		if (!(possibleEmails.isEmpty()) && possibleEmails.get(0) != null) {
			String email = possibleEmails.get(0);
			Log.e("ffff hyey getemailid", email);
		        return email;
		    } else
		        return null;
	}
	
}
