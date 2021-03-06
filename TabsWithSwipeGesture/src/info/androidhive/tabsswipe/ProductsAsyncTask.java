package info.androidhive.tabsswipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dataAccessPackage.JSONParser;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.SIMBA.Welcome.LoginActivity;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class ProductsAsyncTask  extends AsyncTask<ArrayList<String> ,String ,String>{

	JSONParser jsonParser;
	private static String productsURL_ = "http://eblueberry.hostoi.com/simba/Products.php";
	private static final String productTag = "getallProducts";
	private static final String successKey  = "success";
	private static final String errorKey  = "error_msg";
	private static final String productnameKey  = "PName";
	private static final String productidKey  = "PID";
	private List<HashMap<Integer,String>> product_list = new ArrayList<HashMap<Integer,String>>() ;
	private ArrayList<String> Product = new ArrayList<String>();
	private Context storecontext1;
	private String userName;
	ProgressDialog dialog;
	
	

	public ProductsAsyncTask (Context context,String username)
	{
		storecontext1 = context;
		userName = username;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		dialog = ProgressDialog.show(storecontext1, "Wait..", "Invoices are being inserted", true, false);
	}

	@Override
	protected String doInBackground(ArrayList<String>... params) {
		Product = params[0];
		// Building Parameters
        List<NameValuePair> productParams_ = new ArrayList<NameValuePair>();
        productParams_.add(new BasicNameValuePair("tag", productTag));
        
        String message = null;
	    jsonParser = new JSONParser();
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl("POST",productsURL_,productParams_);
        // check for product response
        try
        {
        	if (json.getString(successKey) != null)
        	{
        		String res = json.getString(successKey); 
                if(Integer.parseInt(res) == 1){
                	 dialog.dismiss();
                	  JSONArray jsonProduct = json.getJSONArray("Productdetails");        
                	 
                      int count = jsonProduct.length(); // get totalCount of all jsonObjects
             		for(int i=0 ; i< count; i++){   // iterate through jsonArray 
             				 // get jsonObject @ i position
             			 JSONObject json_obj = jsonProduct.getJSONObject(i);
             		HashMap<Integer, String> map = new HashMap<Integer, String>();
             		
                 	    map.put(Integer.parseInt(json_obj.getString(productidKey)),json_obj.getString(productnameKey));
                  		 
                  		
                  	   product_list.add(map);
             			Log.e("jsonObject "  ,": " + json_obj);
             		}
               	  LevenshteinDistance  e = new LevenshteinDistance (Product ,product_list);
              	  new Invoices(storecontext1,userName).execute(e.findDistanceOfAllProducts());
                  new InsertNewProductsAsyncTask(storecontext1,userName).execute(e.getNewProductList());
                
               	  dialog.dismiss();
	
                }else{
                	message =json.getString(errorKey);
                    dialog.dismiss();
                } 
            
            }
        } catch (JSONException e){
        	e.printStackTrace();
        }
        
        return message;
	}

	
	
	
 
}