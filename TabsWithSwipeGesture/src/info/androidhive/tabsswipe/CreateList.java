package info.androidhive.tabsswipe;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CreateList extends ListActivity implements OnClickListener{

	Button addItem;
	EditText inputItem;
	AlertDialog.Builder alert;
	String listname;
	String itemnamepush = "";
	ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> add_items;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.inputitems);
		add_items = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
		
		addItem = (Button) findViewById(R.id.bitem);
		inputItem = (EditText) findViewById(R.id.iteminput);
		addItem.setOnClickListener(this); 
		final Dialog d_box = new Dialog(this);
		final TextView display = new TextView (this);
		
		inputItem.setHint("Enter Item name ");
		alert = new AlertDialog.Builder(this);
		alert.setTitle("List Name");
//		alert.setMessage("Type your list name.");
		final EditText inputListName = new EditText(this);
		inputListName.setHint("  Enter list name here ");
		LinearLayout ll=new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(inputListName);
        alert.setView(ll);

		
		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				boolean list_work =true;
				try{
				listname = inputListName.getText().toString();

				}
				catch (Exception e) {
					list_work = false;
					d_box.setTitle("Error");
					display.setText("Can not accept name.");
					d_box.setContentView(display);
					d_box.show();
				}
				finally{
					if (list_work){
/*						d_box.setTitle("list name saved in variable");
						display.setText("Success!!");
						d_box.setContentView(display);
						d_box.show();
*/					}
				}
	
			}
		});
		
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		alert.show();
		setListAdapter(add_items);
	}

	@Override
	public void onClick(View v) {
	
		boolean work =true;
		try{
			
		String itemname = inputItem.getText().toString();
		list.add(itemname);
		itemnamepush = itemnamepush.concat(itemname) + "\n";
		inputItem.setText("");
		} 
		catch (Exception e) {
			work = false;
			Dialog d = new Dialog(this);
			d.setTitle("Error!");
			TextView display = new TextView (this);
			display.setText("Unable to create list.");
			d.setContentView(display);
			d.show();
		} 
		finally{
			if (work){
				add_items.notifyDataSetChanged();
/*				Dialog d = new Dialog(this);
				d.setTitle("list created. item added.");
				TextView display = new TextView (this);
				display.setText("Success!!"+itemnamepush);
				d.setContentView(display);
				d.show();
*/
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
		boolean work =true;
		try{
		ItemsData entry = new ItemsData (CreateList.this);
		entry.open();
		entry.createListEntry(listname);
		entry.close();
				
		} 
		catch (Exception e) {
			work = false;
			Dialog d = new Dialog(this);
			d.setTitle("Error!");
			TextView display = new TextView (this);
			display.setText("Unable to create list.");
			d.setContentView(display);
			d.show();
			
		} finally { 
			if (work){
				
				finish();
				Intent myIntent3 = new Intent(getApplicationContext(), ViewAllLists.class);
				startActivity(myIntent3);	
			}	
		}
	}
}
