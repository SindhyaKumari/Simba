package info.androidhive.tabsswipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import dataAccessPackage.InvoiceAndStoreNamePreferences;
import dataAccessPackage.JSONParser;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class InsertNewProductsAsyncTask extends AsyncTask<ArrayList<String>,String,String>{

	private static final String successKey  = "success";
	private static final String errorKey  = "error_msg";
	private static final String UIDKey  = "UID";
	private static final String StoreKey  = "StoreName";
	private static final String ValidKey  = "isValid";
	private static final String ProductNameKey  = "ProductName[]";
	private static final String nameKey  = "name";
	private static final String insertNewProduct= "newProduct";
	private static final String getUserTag = "getuser";
	private static final String getStoreTag = "getstore";
	private HashMap<String,String>  temp;
    InvoiceAndStoreNamePreferences pref;
	private static String newProductURL_ = "http://eblueberry.hostoi.com/simba/NewProducts.php";
	private static String useridandstoreURL_ = "http://eblueberry.hostoi.com/simba/UserIDAndStoreName.php";
    private String userName;
	SharedPreferences settings;
	JSONParser jsonParser = new JSONParser();
	
	public  InsertNewProductsAsyncTask (Context context,String username)
    {
		pref = new InvoiceAndStoreNamePreferences(context);
		temp = new HashMap<String,String> ();
		userName= username;
    }
	@Override
	protected String doInBackground(ArrayList<String>... params) {
		// TODO Auto-generated method stub
				temp = pref.getStoreAndInvoiceDetails();
				String message = null, tempStore;
				ArrayList<String> ProductArray = params[0];
				// Building Parameters
				List<NameValuePair> newProductParams_ = new ArrayList<NameValuePair>();
				newProductParams_.add(new BasicNameValuePair("tag", insertNewProduct));
				newProductParams_.add(new BasicNameValuePair("SerialNo",temp.get("InvoiceNumber")));
				newProductParams_.add(new BasicNameValuePair(UIDKey, String.valueOf(getDataFromAccount())));
				tempStore =temp.get("StoreName");
				Log.e("storename", tempStore);
				if(tempStore !=null){
				tempStore = tempStore.replaceAll("[\t ]+","");
				Log.e("storename",tempStore);
				}
				newProductParams_.add(new BasicNameValuePair(StoreKey,tempStore));
				newProductParams_.add(new BasicNameValuePair("StoreLocation",temp.get("StoreLocation")));
				newProductParams_.add(new BasicNameValuePair(ValidKey,String.valueOf(0)));
				
				//Add all items in basicNameValuePair
				for(int i = 0 ; i < ProductArray.size() ; i++){	
					Log.e("prductnew ", ProductArray.get(i));  
					newProductParams_.add(new BasicNameValuePair(ProductNameKey ,ProductArray.get(i)));
				}
				
				JSONObject json = jsonParser.getJSONFromUrl("POST",newProductURL_ , newProductParams_);
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
		userParams_ .add(new BasicNameValuePair(nameKey, userName));
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
