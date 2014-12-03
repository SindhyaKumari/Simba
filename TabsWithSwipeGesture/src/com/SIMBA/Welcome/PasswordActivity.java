package com.SIMBA.Welcome;

import info.androidhive.tabsswipe.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PasswordActivity  extends Activity
{
	private Button continuePassword;
	private EditText password;
	private TextView errorMsg;
	private String email_,username_,password_;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password);
		
		password = (EditText)findViewById(R.id.password);
		continuePassword = (Button)findViewById(R.id.continue_password);
		errorMsg = (TextView)findViewById(R.id.passwordError);

		password_= password.getText().toString();
		Intent i = getIntent();
		username_ = i.getExtras().getString("username");
		email_ = i.getExtras().getString("email");
		//Log.e("user name", username_ + email_);
		
		continuePassword.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//Checking whether input is null or not
				if(password.length() > 5)
				{
					errorMsg.setVisibility(View.GONE);
					//Calling password activity intent
					Intent signUpIntent = new Intent(PasswordActivity.this,SignUpActivity.class);
					signUpIntent.putExtra("username",username_);
					signUpIntent.putExtra("email",email_);
					signUpIntent.putExtra("password",password_);
					startActivity(signUpIntent);
					 
					finish();
				 }
				else
				{
					errorMsg.setVisibility(View.VISIBLE);
					errorMsg.setText("Your password must have atleast 6 characters.");
				}
			}
		});
	}
}