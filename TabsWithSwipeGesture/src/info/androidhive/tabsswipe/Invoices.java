package info.androidhive.tabsswipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import dataAccessPackage.InvoiceAndStoreNamePreferences;
import dataAccessPackage.JSONParser;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class Invoices extends AsyncTask<String[],String,String> {
	private static final String successKey  = "success";
	private static final String errorKey  = "error_msg";
	private static final String UIDKey  = "UID";
	private static final String SIDKey  = "SID";
	private static final String PIDKey  = "PID[]";
	private static final String emailKey  = "email";
	private static final String insertInvoices = "insertInvoices";
	private static final String getUserTag = "getuser";
	private static final String getStoreTag = "getstore";
	private HashMap<String,String>  temp;
    InvoiceAndStoreNamePreferences pref;
	private static String invoiceURL_ = "http://eblueberry.hostoi.com/simba/Invoices.php";
	private static String useridandstoreURL_ = "http://eblueberry.hostoi.com/simba/UserIDAndStoreName.php";

	
	SharedPreferences settings;
	JSONParser jsonParser = new JSONParser();
	
	public  Invoices(Context context)
    {
		pref = new InvoiceAndStoreNamePreferences(context);
		temp = new HashMap<String,String> ();
    }

	@Override
	protected String doInBackground(String[]... params) {
		// TODO Auto-generated method stub
		temp = pref.getStoreAndInvoiceDetails();
		String message = null;
		String[] PIDArray = params[0];
		// Building Parameters
		List<NameValuePair> invoiceParams_ = new ArrayList<NameValuePair>();
		invoiceParams_.add(new BasicNameValuePair("tag", insertInvoices));
		invoiceParams_.add(new BasicNameValuePair(UIDKey, String.valueOf(getDataFromAccount())));
		invoiceParams_.add(new BasicNameValuePair("SerialNo",temp.get("InvoiceNumber")));
		invoiceParams_.add(new BasicNameValuePair(SIDKey,String.valueOf(getDataFromStoreTable())));
		//Add all items in basicNameValuePair
		for(int i = 0 ; i < PIDArray.length ; i++){	
			  
	        	invoiceParams_.add(new BasicNameValuePair(PIDKey,PIDArray[i]));
		}
		
		JSONObject json = jsonParser.getJSONFromUrl("POST",invoiceURL_, invoiceParams_);
		// check for login response
		try
		{
			if (json.getString(successKey) != null)
			{
				String res = json.getString(successKey);
				if(Integer.parseInt(res) == 1)
				{
					message = "success";
				}
				else
				{
					// Error in login
					message = json.getString(errorKey) ;
				}
			}
			else
			{
				message = json.getString(errorKey);
				Log.e("err2", message);
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return message;
	}
	
	private int getDataFromAccount(){
		
		List<NameValuePair> userParams_ = new ArrayList<NameValuePair>();
		userParams_ .add(new BasicNameValuePair("tag", getUserTag));
		userParams_ .add(new BasicNameValuePair(emailKey, "fasihtt@gmail.com"));
		String message =null;
		int uid = 0;
		JSONObject json = jsonParser.getJSONFromUrl("POST",useridandstoreURL_, userParams_);
		// check for login response
		try
		{
			if (json.getString(successKey) != null)
			{
				String res = json.getString(successKey);
				if(Integer.parseInt(res) == 1)
				{
					
					JSONObject jsonUser = json.getJSONObject("User");
					uid = jsonUser.getInt("UID");
					
				}
				else
				{
					// Error in login
					message = json.getString(errorKey) ;
				}
			}
			else
			{
				message = json.getString(errorKey);
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		return uid;
	}
	private int getDataFromStoreTable(){
	
		List<NameValuePair> storeParams_ = new ArrayList<NameValuePair>();
		storeParams_ .add(new BasicNameValuePair("tag", getStoreTag));
		storeParams_.add(new BasicNameValuePair("StoreName","Sunny Medico"));
		Log.e("storename", " " + temp.get("StoreName"));
		String message =null;
		int sid = 0;
		JSONObject json = jsonParser.getJSONFromUrl("POST",useridandstoreURL_, storeParams_);
		// check for login response
		try
		{
			if (json.getString(successKey) != null)
			{
				String res = json.getString(successKey);
				if(Integer.parseInt(res) == 1)
				{
					
					JSONObject jsonUser = json.getJSONObject("Store");
					sid = jsonUser.getInt("SID");
				
				}
				else
				{
					// Error in login
					message = json.getString(errorKey) ;
				}
			}
			else
			{
				message = json.getString(errorKey);
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		return sid;
	}


}
