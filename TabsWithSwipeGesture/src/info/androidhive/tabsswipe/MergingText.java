package info.androidhive.tabsswipe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.SIMBA.Welcome.LoginActivity;

import dataAccessPackage.InvoiceAndStoreNamePreferences;
import dataAccessPackage.JSONParser;
import dataAccessPackage.LoginSession;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;
import info.androidhive.tabsswipe.MainActivity;
import info.androidhive.tabsswipe.R;

public class MergingText extends Activity{

	ListView listView;
    ArrayAdapter<String> dataAdapter;
    Button mergeUpbtn,undobtn,mergeDownbtn,submit;
    ArrayList<String> Items = new ArrayList<String>();
    ArrayList<String> data = new ArrayList<String>();
    
    int position1= -1 , position2 = -1;
    String item1, item2, itemlist,storeName,InvoiceNumber,userName,StoreLocation;
    String[] itemslistsplit;
    SharedPreferences sharedpreferences;
    String MY_PREFS_NAME = "myprefs";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mergingtext);
        
        listView = (ListView) findViewById(R.id.listView1);
        mergeUpbtn = (Button) findViewById(R.id.mergeupbtn);
        mergeDownbtn = (Button) findViewById(R.id.mergedownbtn);
        undobtn = (Button) findViewById(R.id.UndoBtn);
        submit = (Button) findViewById(R.id.submitbtn);
        undobtn.setEnabled(false);
        
        //get Intent
        Intent intent = getIntent();
        itemlist = intent.getExtras().getString("SplittedString");
        storeName = intent.getExtras().getString("StoreName");
        InvoiceNumber = intent.getExtras().getString("InvoiceNumber");
        userName = intent.getExtras().getString("name");
        StoreLocation = intent.getExtras().getString("StoreLocation");
        
        if(!(InvoiceNumber.matches("[.0-9]+"))){
        	Random rn = new Random();
	    	int j = rn.nextInt() % 99999;
	    	j = j+4;
	    	InvoiceNumber =  String.valueOf((j));
	    	Log.e("invoice", InvoiceNumber + j);
        }
        Log.e("serial no", InvoiceNumber);
        
        InvoiceAndStoreNamePreferences s = new InvoiceAndStoreNamePreferences(MergingText.this);
        s.clearData();
        s.createSession(storeName, InvoiceNumber,StoreLocation);
        //Splitting string
        itemslistsplit = itemlist.split("\n");

        
        for(int i = 0 ; i< itemslistsplit.length ; i++){
               Items.add(itemslistsplit[i]);
        }
      
        dataAdapter = new ArrayAdapter<String>(this,
          android.R.layout.simple_list_item_multiple_choice, Items);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(dataAdapter);
 
        
        submit.setOnClickListener(new View.OnClickListener() {
   
   @Override
      public void onClick(View v) {
	   
	  
	   new  UserIdAsynTask().execute(userName);
	   
        new ProductsAsyncTask(MergingText.this,userName).execute(Items);
       
        Intent mainIntent = new Intent(MergingText.this,MainActivity.class);
        mainIntent.putExtra("name", userName);
        mainIntent.putExtra("tab","Upload Receipt");
        startActivity(mainIntent);
       // finish();
      }});
         
    }
    public void InsertItemsInLocalDB(int uid){
		 Log.e("student uid", String.valueOf(uid));
		 SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
		 String date = df.format(Calendar.getInstance().getTime());
		 ItemsData info = new ItemsData(this);
		 info.open();
		 Log.e("date", date);
		 long pi = info.createListEntry(date);
		 data.add(String.valueOf(uid));
		System.out.print("i am last one" + pi);	 
		 int id = info.getlastId();
		 for(int i = 0 ; i< Items.size() ; i++){
		 info.createItemsListEntry(id, Items.get(i));
		 }
		 data.add(String.valueOf(id));
		 data.add(date);	
		 Log.e("task", data.get(0));
		    Log.e("task", data.get(1));
		    Log.e("task", data.get(2));
		 info.createUserIDAndListEntry(id, uid);
		 info.close();
		 
		 new InsertDataInListAsyncTask().execute(Items,data);
	 }
 
    @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent mainIntent = new Intent(MergingText.this,MainActivity.class);
        mainIntent.putExtra("name", userName);
        mainIntent.putExtra("tab","Upload Receipt");
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
		finish();
	}


	public void onUndoClick(View v) {
     
     
      dataAdapter.remove(dataAdapter.getItem(position1)); 
      NotifyAdapterChanges();
      dataAdapter.insert(item1, position1);
      NotifyAdapterChanges();
      dataAdapter.insert(item2, position2);
      NotifyAdapterChanges();
        
        undobtn.setEnabled(false);
    }
    
 public void onMergeUpClick(View v) {
    
    SparseBooleanArray checked = listView.getCheckedItemPositions();
       ArrayList<String> selectedItems = new ArrayList<String>();
       String selected = "";
       int cntChoice = checked.size();
       position1 = -1;
      
       if(cntChoice < 3){
         for (int i = 0; i < cntChoice ; i++) {
             // Item position in adapter
             int position = checked.keyAt(i);
             // Add sport if it is checked i.e.) == TRUE!
             if (checked.valueAt(i) && (position1 != -1)){
                 selectedItems.add(dataAdapter.getItem(position));
               position2 = position;
              }else{
               selectedItems.add(dataAdapter.getItem(position));
               position1 = position;
              }
         
             }
         
         String[] outputStrArr = new String[selectedItems.size()];
     
         for (int i = 0; i < selectedItems.size(); i++) {
             outputStrArr[i] = selectedItems.get(i);
             selected +=  outputStrArr[i] + ",";
         }
         
         
         Toast.makeText(getApplicationContext(),"Selected items list " +  selected, Toast.LENGTH_LONG).show();
         checkPostionOfItemsAndNewItem(position1 ,position2,true);
      }
         else {
          
          Toast.makeText(getApplicationContext(), "Please select only 2 items", Toast.LENGTH_LONG).show();
         }

      checked.clear();
   
  }
 

 public void onMergeDownClick(View v) {
  
   SparseBooleanArray checked = listView.getCheckedItemPositions();
      ArrayList<String> selectedItems = new ArrayList<String>();
      String selected = "";
      int cntChoice = checked.size();
      position1 = -1;
     
      if(cntChoice < 3){
        for (int i = 0; i < cntChoice ; i++) {
            // Item position in adapter
            int position = checked.keyAt(i);
            // Add sport if it is checked i.e.) == TRUE!
            if (checked.valueAt(i) && (position1 != -1)){
                selectedItems.add(dataAdapter.getItem(position));
              position2 = position;
             }else{
              selectedItems.add(dataAdapter.getItem(position));
              position1 = position;
             }
        
            }
        
        String[] outputStrArr = new String[selectedItems.size()];
    
        for (int i = 0; i < selectedItems.size(); i++) {
            outputStrArr[i] = selectedItems.get(i);
            selected +=  outputStrArr[i] + ",";
        }
        
        
        Toast.makeText(getApplicationContext(),"Selected items list " +  selected, Toast.LENGTH_LONG).show();
        checkPostionOfItemsAndNewItem(position1 ,position2,false);
     }
        else {
         
         Toast.makeText(getApplicationContext(), "Please select only 2 items", Toast.LENGTH_LONG).show();
        }

     checked.clear();
  
 }
 

    private void checkPostionOfItemsAndNewItem(int pos1 ,int pos2, boolean up){
     
     String tempitem = "";
     
     if(up && (pos1+1) == pos2){
      
      item1 = dataAdapter.getItem(pos1);
      item2 =  dataAdapter.getItem(pos2);
      tempitem = item1+ " " +item2;
      dataAdapter.remove(dataAdapter.getItem(pos1)); 
      NotifyAdapterChanges();
      dataAdapter.insert(tempitem, pos1);
      NotifyAdapterChanges();
      dataAdapter.remove(dataAdapter.getItem(pos2)); 
      NotifyAdapterChanges();
         
         // Enabling undo button 
         undobtn.setEnabled(true);
         
     }else if((!up) && ((pos2-1) == pos1)){
       item1 = dataAdapter.getItem(pos1);
       item2 =  dataAdapter.getItem(pos2);
       tempitem = item2 +  " " + item1;
       dataAdapter.remove(dataAdapter.getItem(pos1)); 
       NotifyAdapterChanges();
       dataAdapter.insert(tempitem, pos1);
       NotifyAdapterChanges();
       dataAdapter.remove(dataAdapter.getItem(pos2)); 
       NotifyAdapterChanges();
         
         // Enabling undo button 
         undobtn.setEnabled(true);
     }
     else{
      Toast.makeText(getApplicationContext(),"Incorrect items selected please item in sequence" , Toast.LENGTH_LONG).show();
     }
     
    }
    
    private void NotifyAdapterChanges(){     
        dataAdapter.notifyDataSetChanged();     
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
    	int userid;

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
		  InsertItemsInLocalDB(Integer.valueOf(result));
	}

	
} 
    
 
   
}

