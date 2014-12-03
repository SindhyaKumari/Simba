package com.SIMBA.Welcome;

import info.androidhive.tabsswipe.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserNameActivity extends Activity
{
	private Button continueUsername;
	private EditText firstName;
	private EditText lastName;
	private TextView errorMsg;
	String name,firstname,lastname;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_username);
		
		firstName = (EditText)findViewById(R.id.firstname);
		lastName = (EditText)findViewById(R.id.lastname);
		continueUsername = (Button)findViewById(R.id.continue_username);
		errorMsg = (TextView)findViewById(R.id.usernameError);
		
		continueUsername.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//Checking whether input is null or not
				firstname = firstName.getText().toString();
				lastname = lastName.getText().toString();
				if(!(firstname.equals(""))  &&  !(lastname.equals("")))
				{
					errorMsg.setVisibility(View.GONE);
					name = firstname + lastname;
					//Calling email activity intent
					Intent emailIntent = new Intent(UserNameActivity.this,EmailActivity.class);
					emailIntent.putExtra("name", name);
					startActivity(emailIntent);	 
				}
				else
				{
					errorMsg.setVisibility(View.VISIBLE);
					errorMsg.setText("Please enter your first and last name");
				}
			}
		});
	}
}