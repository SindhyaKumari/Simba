package info.androidhive.tabsswipe;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


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
/*
        ItemsData info = new ItemsData(this);
		info.open();
		for (int s=1; s<=counter; s++){
		String data = info.getDataList(s);
		list.add(data);
        adapter.notifyDataSetChanged();
		}
		info.close();
*/		
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
			    public boolean onItemLongClick(    AdapterView<?> arg0,    View v,    int position,    long id){
			       
			    	Dialog d = new Dialog(ViewAllLists.this);
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
					
					final CharSequence[] items = {"Red", "Green", "Blue"};

					AlertDialog.Builder builder = new AlertDialog.Builder(ViewAllLists.this);

					builder.setTitle("Pick a color");
					builder.setItems(items, new DialogInterface.OnClickListener() {

					   public void onClick(DialogInterface dialog, int item) {
					        Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
					   }

					});

					AlertDialog alert = builder.create();

					alert.show();
			        return false;
			      }
			    }
			  );
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

/*	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId()==R.id.ll1) {
		    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		    menu.setHeaderTitle(Countries[info.position]);
		    String[] menuItems = getResources().getStringArray(R.array.menu);
		    for (int i = 0; i<menuItems.length; i++) {
		      menu.add(Menu.NONE, i, i, menuItems[i]);
		    }

	}
	
*/

}
