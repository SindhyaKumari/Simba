package info.androidhive.tabsswipe;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ViewItemsList extends Activity{


    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ListView listView;
    String[] itemset ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewlists);

		 
		 listView = (ListView) findViewById(R.id.listView1);
		 Intent intent = getIntent();
		 String items = intent.getExtras().getString("items");
	     
		 if(items.length() > 0){
			 itemset = items.split("\n");
			 for(int i = 0 ; i < itemset.length ; i++){
				 list.add(itemset[i]);
				 
				 Log.e("lsit",itemset[i] );
			 }
			 adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
			 
		     listView.setAdapter(adapter);
		 }
	    
	
		}
	}