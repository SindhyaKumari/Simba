package info.androidhive.tabsswipe;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import dataAccessPackage.JSONParser;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class Invoices extends AsyncTask<int[],String,String> {
	
	private static final String successKey  = "success";
	private static final String errorKey  = "error_msg";
	private static final String UIDKey  = "UID";
	private static final String SIDKey  = "PID";
	private static final String PIDKey  = "PID";
	private static final String emailKey  = "email";
	private static final String insertInvoices = "insertInvoices";
	private static final String getUserTag = "getuser";
	private static final String getStoreTag = "getstore";
	public static final String MyPREFERENCES = "MyPrefs" ;
	private static String invoiceURL_ = "http://eblueberry.hostoi.com/simba/Invoices.php";
	private static String useridandstoreURL_ = "http://eblueberry.hostoi.com/simba/UserIDAndStoreName.php";
	
	SharedPreferences settings;
	JSONParser jsonParser = new JSONParser();
	
	public Invoices(Context context)
    {
		settings = context.getSharedPreferences(MyPREFERENCES, context.MODE_PRIVATE);
    }

	@Override
	protected String doInBackground(int[]... params) {
		// TODO Auto-generated method stub
		String InvoiceNo = settings.getString("InvoiceNumber","");
		String StoreName = settings.getString("StoreName", "");
		Log.e("InvoiceNumber: ",InvoiceNo);
		Log.e("StoreNumber: ",StoreName);
		String message = null;
		int [] array  = new int[params[0].length];
		array= params[0];
		// Building Parameters
		List<NameValuePair> invoiceParams_ = new ArrayList<NameValuePair>();
		invoiceParams_.add(new BasicNameValuePair("tag", insertInvoices));
		invoiceParams_.add(new BasicNameValuePair(UIDKey, String.valueOf(getDataFromAccount())));
		invoiceParams_.add(new BasicNameValuePair("SerialNo", "403553"));
		invoiceParams_.add(new BasicNameValuePair(SIDKey,String.valueOf(getDataFromStoreTable())));
		invoiceParams_.add(new BasicNameValuePair(PIDKey,String.valueOf(array[0])));
		
		JSONObject json = jsonParser.getJSONFromUrl("POST",invoiceURL_, invoiceParams_);
		// check for login response
		try
		{
			if (json.getString(successKey) != null)
			{
				String res = json.getString(successKey);
				if(Integer.parseInt(res) == 1)
				{
					// Store user details in shared preferences
					JSONObject jsonUser = json.getJSONObject("insertInvoices");
					String uid = jsonUser.getString("UID");
					String store = jsonUser.getString("SID");
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