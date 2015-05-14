package info.androidhive.tabsswipe;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.SIMBA.Welcome.LoginActivity;
import com.SIMBA.Welcome.ResetPasswordActivity;

import dataAccessPackage.JSONParser;
import dataAccessPackage.LoginSession;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class NewProductsListActivity extends Activity{
	

    private ArrayList<String> productlist = new ArrayList<String>();
    public ListView lvMain;
	
    ArrayAdapter<String> boxAdapter;
    Button submit ;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newproductlist);
        lvMain = (ListView)findViewById(R.id.list);
        submit = (Button)findViewById(R.id.submitbtn);
       
        new GetProductAsyncTask(NewProductsListActivity.this).execute("insert");
        
        submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				productlist = new GetProductAsyncTask(NewProductsListActivity.this).getProducts();
				new UpdateNewProductsAsyncTask(NewProductsListActivity.this).execute(productlist);
			}
		});
    }
    	

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_logout, menu);
        return true;
    }
    
    @Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		//get intent 
	
		// checking which item is being selected
		LoginSession loginObj = new LoginSession(getApplicationContext());
		String str = (String) item.getTitle();
		if(str.equals("Logout"))
		{
			Toast.makeText(NewProductsListActivity.this, "Logout!", Toast.LENGTH_SHORT).show();
			loginObj.clearDataAfterLogout();
			
			//calling login activity
			Intent startUpIntent = new Intent(NewProductsListActivity.this, LoginActivity.class);
			startActivity(startUpIntent);
			
	       //closing main activity			
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}

     
   
     
   

private class GetProductAsyncTask extends AsyncTask<String,String ,ArrayList<String>>{

	JSONParser jsonParser;
	private String newproductsURL_ = "http://eblueberry.hostoi.com/simba/NewProducts.php";
	private static final String productTag = "getnewProducts";
	private static final String successKey  = "success";
	private static final String errorKey  = "error_msg";
	private static final String productnameKey  = "ProductName";
	private ArrayList<String> Product = new ArrayList<String>();
	private Context context;
	
	GetProductAsyncTask(Context c){
		context = c;
	}
	

	@Override
	protected ArrayList<String> doInBackground(String... params) {
		
		// Building Parameters
        List<NameValuePair> newproductParams_ = new ArrayList<NameValuePair>();
        newproductParams_.add(new BasicNameValuePair("tag", productTag));
        
        String message = null;
	    jsonParser = new JSONParser();
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl("POST",newproductsURL_,newproductParams_);
        // check for product response
        try
        {
        	if (json.getString(successKey) != null)
        	{
        		String res = json.getString(successKey); 
                if(Integer.parseInt(res) == 1){
                	
                	  JSONArray jsonProduct = json.getJSONArray("newProductdetails");        
                	 
                      int count = jsonProduct.length(); // get totalCount of all jsonObjects
             		  for(int i=0 ; i< count; i++){   // iterate through jsonArray 
             				 // get jsonObject @ i position
             		   JSONObject json_obj = jsonProduct.getJSONObject(i);
                  	   Product.add(json_obj.getString(productnameKey));
             		   Log.e("jsonObject "  ,": " + json_obj);
             		}
                }else{
                	message =json.getString(errorKey);
                }
            
            }
        } catch (JSONException e){
        	e.printStackTrace();
        }
		return Product;
	}


	@Override
	protected void onPostExecute(ArrayList<String> result) {
		ArrayList<String> products = new ArrayList<String>();

		 for (int i = 0; i < result.size(); i++) {
	          products.add(
	        		  result.get(i));
	        }
		 boxAdapter = new ArrayAdapter<String>(context,
		          android.R.layout.simple_list_item_multiple_choice,products);
		 lvMain.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	     lvMain.setAdapter(boxAdapter);
	     
	//     submit.setOnClickListener(new OnClickListener)
	  
	}
	
	ArrayList<String> getProducts(){
		 SparseBooleanArray checked = lvMain.getCheckedItemPositions();
	       ArrayList<String> selectedItems = new ArrayList<String>();  
	         for (int i = 0; i < checked.size() ; i++) {
	             // Item position in adapter
	             int position = checked.keyAt(i);
	                 selectedItems.add(boxAdapter.getItem(position));
	             }
		return selectedItems ;
		
	}
	
	
	
}

}
