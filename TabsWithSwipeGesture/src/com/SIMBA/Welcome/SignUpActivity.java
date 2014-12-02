package com.SIMBA.Welcome;

import info.androidhive.tabsswipe.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.email.backgroundmaillibrary.BackgroundMail;
import com.email.backgroundmaillibrary.Utils;

import dataAccessPackage.JSONParser;

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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends Activity
{
	private Button signIn, verify;
	private EditText secret_answer;
	private TextView errorMsg;
	private String email_,username_,password_, answer_;
	private static String registerURL_ = "http://eblueberry.hostoi.com/simba/";
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_secret_question);
		context = this;

		secret_answer = (EditText)findViewById(R.id.answer);
		signIn = (Button)findViewById(R.id.signin);
		errorMsg = (TextView)findViewById(R.id.registerError);
		
		Intent i = getIntent();
		username_ = i.getExtras().getString("username");
		email_ = i.getExtras().getString("email");
		password_ = i.getExtras().getString("password");
		
		signIn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//Checking whether input is null or not
				if (isNetworkAvailable())
				{
					if(!(secret_answer.getText().toString().equals("")))
					{
						//random number
						Random r = new Random();
						int PIN = r.nextInt(50000 - 10000) + 10000;
						//Email Verification
						BackgroundMail bm = new BackgroundMail(context);
						bm.setGmailUserName("simba.nuces@gmail.com");
		                //"V8Y3TPndPfWYKh/I0BanRg==" is crypted "password"
						bm.setGmailPassword(Utils.decryptIt("TOLzqeBD151kRsyLSEVvLg==")); 
						bm.setMailTo(email_);
						bm.setFormSubject("SIMBA Account: Email Confirmation");
						bm.setFormBody("Dear Sir/Madam,\n Welcome to the SIMBA community! \n\n Your account details:\n\n  Your login is: " + email_ + "\n  Your Secert Answer is: " + secret_answer.getText().toString() + "\n\n  To verify your e-mail address, please enter the following pin number: \n  " + PIN + "\n\nThank you for registering!\n\nBest Regards,\nThe SIMBA Team" );
						bm.send();
						//End of Email Verification
						new RegistrationTask().execute(registerURL_);
					}
					else
					{
						errorMsg.setText("Answer is empty.. Do fill it!");
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
	   private static final String registerTag = "register";
	   private boolean checkFlag = false;
	   JSONParser jsonParser;
	   
	   //Before starting background thread show progress dailog
	   @Override
	   protected void onPreExecute()
	   {
		   if (isNetworkAvailable())
		   {
			   jsonParser = new JSONParser();
			   pDialog = new ProgressDialog(SignUpActivity.this);
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
			   answer_= secret_answer.getText().toString();
 
			   // Building Parameters
			   List<NameValuePair> registerParams_ = new ArrayList<NameValuePair>();
			   registerParams_.add(new BasicNameValuePair("tag", registerTag));
		       registerParams_.add(new BasicNameValuePair(nameKey, username_));
		       registerParams_.add(new BasicNameValuePair(emailKey, email_));
		       registerParams_.add(new BasicNameValuePair(passwordKey, password_));
		       registerParams_.add(new BasicNameValuePair(answerKey, answer_));
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
				   Toast.makeText(getApplicationContext(), "Your account has been created , please login", Toast.LENGTH_LONG).show();
				   //Opening login activity after successful registration
				   Intent registerIntent = new Intent(SignUpActivity.this,LoginActivity.class);
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
