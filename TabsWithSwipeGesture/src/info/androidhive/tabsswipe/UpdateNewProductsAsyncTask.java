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
import android.os.AsyncTask;
import android.util.Log;


public class UpdateNewProductsAsyncTask extends AsyncTask<ArrayList<String>,String ,String>{


	JSONParser jsonParser;
	private String updateURL_ = "http://eblueberry.hostoi.com/simba/UpdateNewTables.php";
	private static final String updateTag = "updateNewProduct";
	private static final String successKey  = "success";
	private static final String errorKey  = "error_msg";
	private static final String productnameKey  = "ProductName[]";
	private ArrayList<String> Product = new ArrayList<String>();
	private ProgressDialog dialog;
	private Context context;
	
	public UpdateNewProductsAsyncTask(Context context){
		this.context = context;
	}
 
	@Override
	protected void onPreExecute() {
		dialog = ProgressDialog.show(context, "Wait..", "Invoices are being inserted", true, false);
	}



	@Override
	protected String doInBackground(ArrayList<String>... params) {
		
		String message = null;
		Product = params[0];
		List<NameValuePair> updateParams_ = new ArrayList<NameValuePair>();
		updateParams_.add(new BasicNameValuePair("tag",updateTag));
		jsonParser = new JSONParser();
		//Add all items in basicNameValuePair
		for(int i = 0 ; i < Product.size() ; i++){	
			  
	        	updateParams_.add(new BasicNameValuePair(productnameKey,Product.get(i)));
		}
		JSONObject json = jsonParser.getJSONFromUrl("POST",updateURL_, updateParams_);
		// check for login response
		try
		{
			if (json.getString(successKey) != null)
			{
				String res = json.getString(successKey);
				if(Integer.parseInt(res) == 1)
				{
					//dialog.dismiss();
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
			dialog.dismiss();
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return message;
	}

	
	

}

