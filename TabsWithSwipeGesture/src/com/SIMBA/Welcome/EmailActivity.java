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

		email_= email.getText().toString();
		Intent i = getIntent();
		username_ = i.getExtras().getString("name");
		Log.e("user name", username_);
		
		continueEmail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Checking whether input is null or not
				 if(!(email_.equals(" "))){
				
					 //Calling password activity intent
					 Intent passwordIntent = new Intent(EmailActivity.this,PasswordActivity.class);
					 passwordIntent.putExtra("username",username_);
					 passwordIntent.putExtra("email",email_);
					 startActivity(passwordIntent);	 
				 }else{
					 errorMsg.setText("Email address is empty ..Do fill it");
				 }
			}
		});
	}

}
