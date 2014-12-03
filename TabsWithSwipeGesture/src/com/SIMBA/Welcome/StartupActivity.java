package com.SIMBA.Welcome;

import dataAccessPackage.LoginSession;
import info.androidhive.tabsswipe.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class StartupActivity extends Activity
{
	private Button loginButton;
    private Button signupButton;
    LoginSession loginObj;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        
        //Passing reference to layout resources
        loginButton = (Button)findViewById(R.id.signinbtn);
        signupButton = (Button)findViewById(R.id.singupbtn);
        loginObj = new LoginSession(StartupActivity.this);
        
        /*Checking login status whether user already logged in or not
        if(loginObj.checkLoginStatus()){
        	Intent dashIntent = new Intent(StartupActivity.this,DashBoard.class);
			startActivity(dashIntent);
        }
        */
                
        /*Performing action on login button */
        loginButton.setOnClickListener(new View.OnClickListener()
        {
        	@Override
			public void onClick(View v)
        	{
        		//Calling login activity by using intent functionality
				Intent loginIntent = new Intent(StartupActivity.this,LoginActivity.class);
				startActivity(loginIntent);								
			}
		});
        
        /*Performing action on sign up button */
        signupButton.setOnClickListener(new View.OnClickListener()
        {
        	@Override
			public void onClick(View v)
        	{
        		//Calling signup activity by using intent functionality
				Intent signupIntent = new Intent(StartupActivity.this,UserNameActivity.class);
				startActivity(signupIntent);					
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.startup, menu);
        return true;
    }
  
}