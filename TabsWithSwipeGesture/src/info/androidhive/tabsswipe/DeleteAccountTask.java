package info.androidhive.tabsswipe;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.SIMBA.Welcome.EmailActivity;
import com.SIMBA.Welcome.LoginActivity;
import com.SIMBA.Welcome.PasswordActivity;

import dataAccessPackage.JSONParser;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

public class DeleteAccountTask  extends AsyncTask<String,String,String>{

	private static String deleteAccountURL_ = "http://eblueberry.hostoi.com/simba/deleteAccount.php";
	private static final String successKey  = "success";
	private static final String errorKey  = "error_msg";
    private static final String deleteAccountTag = "deleteAccount";
	private static final String emailKey  = "email";
	JSONParser jsonParser;

	@Override
	protected void onPreExecute() {
		jsonParser = new JSONParser(); 
	}


	@Override
	protected String doInBackground(String... params) {
		// Building Parameters
        List<NameValuePair> deleteAccountParams_ = new ArrayList<NameValuePair>();
        deleteAccountParams_.add(new BasicNameValuePair("tag", deleteAccountTag));
        deleteAccountParams_.add(new BasicNameValuePair(emailKey,params[0]));
        String message = null;
	
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl("POST",deleteAccountURL_,deleteAccountParams_);

        // check for register response
        try
        {
        	if (json.getString(successKey) != null)
        	{
        		String res = json.getString(successKey); 
                if(Integer.parseInt(res) == 1){
                       message =json.getString("message");
                }else{
                	message =json.getString(errorKey);
                }
            
            }
        } catch (JSONException e){
        	e.printStackTrace();
        }
             
		
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {

		super.onPostExecute(result);
	}





}
