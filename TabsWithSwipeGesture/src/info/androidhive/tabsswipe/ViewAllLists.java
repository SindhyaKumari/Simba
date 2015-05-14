package info.androidhive.tabsswipe;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import dataAccessPackage.InvoiceAndStoreNamePreferences;
import dataAccessPackage.JSONParser;
import dataAccessPackage.LoginSession;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


public class ViewAllLists extends ListActivity{

    int counter;
	AlertDialog.Builder alert;
	String data = "";
	int itemPosition,userid;
    String  itemValue = "";	
    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.viewalllists);
        Intent i = getIntent();
        String name = i.getExtras().getString("userName");
        
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        setListAdapter(adapter);
 
        new UserIdAsynTask().execute(name);

         
       /* Button createlist;
		createlist = (Button) findViewById(R.id.bcreate);
		createlist.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				finish();
				Intent createListIntent = new Intent(getApplicationContext(), CreateList.class);
				startActivity(createListIntent);
			}
		});*/
		
		 getListView().setOnItemLongClickListener(new OnItemLongClickListener(){
			    public boolean onItemLongClick(AdapterView<?> arg0, View v, int position, long id){
			       
			 /*   	Dialog d = new Dialog(ViewAllLists.this);
					d.setTitle("list created. item added.");
					TextView display = new TextView (ViewAllLists.this);
					display.setText("Success!!"+"\nview: "+v+"\nposition: "+position+"\nid: "+id);
					d.setContentView(display);
					d.show();
			 
				/*	String options[] = {"Delete List"};
					setListAdapter(new ArrayAdapter<String>(ViewAllLists.this, android.R.layout.simple_list_item_1, options));
			    	
					PopupWindow window;
					window = new PopupWindow(ViewAllLists.this);
					window.setFocusable(true);
					ListView listViewOptions = new ListView(ViewAllLists.this);
					listViewOptions.setAdapter(dogsAdapter(popUpContents));
			    */	
					
					final CharSequence[] items = {"Delete List", "Add an Item"};

					AlertDialog.Builder builder = new AlertDialog.Builder(ViewAllLists.this);

					builder.setTitle("Select an Option");
					builder.setItems(items, new DialogInterface.OnClickListener() {

					   public void onClick(DialogInterface dialog, int item) {

						   	int opt_num;
							opt_num= item ;
	
					        if (opt_num == 0)
					        {
					        	ItemsData info = new ItemsData(ViewAllLists.this);
					    		info.open();
					    		info.deleteList(itemValue);
					    		info.close();
					   }
					   }
					});

					AlertDialog alert = builder.create();
					alert.show();
			        return false;
			      }
			    }
			  );
		 adapter.notifyDataSetChanged();
    }
    
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		super.onListItemClick(l, v, position, id);
	    itemPosition = position;
	    
        itemValue = (String) l.getItemAtPosition(position);
        ItemsData info = new ItemsData(this);
		info.open();
		data = info.getDataItems(itemValue );
		info.close();
		
		Intent myIntent = new Intent(getApplicationContext(), ViewItemsList.class);
		myIntent.putExtra("items", data);
		startActivity(myIntent);
	}
		
	protected void display() {
		
        ItemsData info1 = new ItemsData(this);
		info1.open();
		int s = counter + 1;
		String data1 = info1.getDataList(s);
		list.add(data1);
        adapter.notifyDataSetChanged();
		info1.close();	
		

	}
	
	public class UserIdAsynTask extends AsyncTask<String,String,String> {
    	private static final String successKey  = "success";
    	private static final String errorKey  = "error_msg";
    	private static final String nameKey  = "name";
    	private static final String getUserTag = "getuser";
    	private static final String useridandstoreURL_ = "http://eblueberry.hostoi.com/simba/UserIDAndStoreName.php";
        InvoiceAndStoreNamePreferences storePref;
        LoginSession loginPref;
    	JSONParser jsonParser = new JSONParser();

	@Override
	protected String doInBackground(String... params) {
		 List<NameValuePair> userParams_ = new ArrayList<NameValuePair>();
		    userParams_ .add(new BasicNameValuePair("tag", getUserTag));
		    userParams_ .add(new BasicNameValuePair(nameKey,params[0]));
		    String message =null;
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
				   userid = jsonUser.getInt("UID");
				  // updateid(userid);
					
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
		
		      return String.valueOf(userid);
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		String[] listnames;
		 ItemsData datacount = new ItemsData(ViewAllLists.this);
	        datacount.open();
	//  datacount.deleteAllItems();
	  
	        int  counter = datacount.getCountDataList(Integer.parseInt(result));
	        Log.e("counter",String.valueOf( counter)+ Integer.parseInt(result));
	                if(counter > 0){
	        		String data = datacount.getDataList(Integer.parseInt(result));
	        		Log.e("data", data);
	        		listnames = data.split("\n");
	        		for(int i = 0; i < listnames.length ; i++){
	        		list.add(listnames[i]);
	        		adapter.notifyDataSetChanged();
	        		}
	                }
	        datacount.close();
	}

	
}     
}
