package info.androidhive.tabsswipe;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
	int itemPosition;
    String  itemValue = "";
    
	
    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.viewalllists);
        
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        setListAdapter(adapter);
        
        ItemsData datacount = new ItemsData(this);
        datacount.open();
        counter = datacount.getCountDataList();
        	for (int s=1; s<=counter; s++){
        		String data = datacount.getDataList(s);
        		list.add(data);
        		adapter.notifyDataSetChanged();
        		}
        datacount.close();
//        adapter.notifyDataSetChanged();
         
        Button createlist;
		createlist = (Button) findViewById(R.id.bcreate);
		createlist.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				finish();
				Intent myIntent2 = new Intent(getApplicationContext(), CreateList.class);
				startActivity(myIntent2);
			}
		});
		
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
		data = info.getDataItems(itemPosition, itemValue );
		info.close();
		
		Intent myIntent = new Intent(getApplicationContext(), ViewList.class);
		myIntent.putExtra("itemValue", itemValue);
		myIntent.putExtra("key", itemPosition);
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
}
