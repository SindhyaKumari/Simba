package info.androidhive.tabsswipe;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import dataAccessPackage.JSONParser;
import dataAccessPackage.LoginSession;

import android.os.AsyncTask;
import android.util.Log;

public class InsertDataInListAsyncTask extends AsyncTask<ArrayList<String>,String,String> {

	private static final String successKey  = "success";
	private static final String errorKey  = "error_msg";
	private static final String UIDKey  = "Userid";
	private static final String ListKey  = "Listid";
	private static final String ListNameKey  = "Listname";
	private static final String ItemsKey  = "Items[]";
	private static final String insertlist  = "insertlistdata";
	private ArrayList<String> ItemList;
	private ArrayList<String> dataList;
	private static String listdataURL_ = "http://eblueberry.hostoi.com/simba/Listdata.php";
    LoginSession loginPref;
	JSONParser jsonParser = new JSONParser();
	@Override
	protected String doInBackground(ArrayList<String>... params) {
		
		String message = null;
		ItemList = params[0];
		dataList = params[1];
		
		
		// Building Parameters
		List<NameValuePair> listParams_ = new ArrayList<NameValuePair>();
		listParams_.add(new BasicNameValuePair("tag", insertlist));
		listParams_.add(new BasicNameValuePair(UIDKey,dataList.get(0)));
		listParams_.add(new BasicNameValuePair(ListKey,dataList.get(1)));
		listParams_.add(new BasicNameValuePair(ListNameKey,dataList.get(2)));
		//Add all items in basicNameValuePair
		
		if(ItemList.size() > 0){
		for(int i = 0 ; i < ItemList.size(); i++){	
			Log.e("itemset",ItemList.get(i));
			listParams_.add(new BasicNameValuePair(ItemsKey,ItemList.get(i)));
		}
		
		JSONObject json = jsonParser.getJSONFromUrl("POST",listdataURL_ , listParams_);
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
		}else{
			message = "no item needed to insert";
		}
		return message;
	}

}
