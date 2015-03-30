package info.androidhive.tabsswipe;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
    Button mergebtn,undobtn;
    ArrayList<String> Items = new ArrayList<String>();
    int position1= -1 , position2 = -1;
    String item1,item2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mergingtext);
        
        listView = (ListView) findViewById(R.id.listView1);
        mergebtn = (Button) findViewById(R.id.MergeBtn);
        undobtn = (Button) findViewById(R.id.UndoBtn);
        
        undobtn.setEnabled(false);
        // textview;
        Items.add("Coca cola");
        Items.add("Kurkure chips");
        Items.add("Wheatable Biscuit");
        Items.add("Green Tea");
        
        dataAdapter = new ArrayAdapter<String>(this,
        		android.R.layout.simple_list_item_multiple_choice, Items);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(dataAdapter);
 
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			
		
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				EditItemName(view,position);
				return false;
			}
		});
			
			
        
    }
    
    private String  EditItemName(final View view, final int pos){
       final String result = "";
    	// get edititemnamedailog.xml view
		LayoutInflater li = LayoutInflater.from(MergingText.this);
		View promptsView = li.inflate(R.layout.edititemnamedialog, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				MergingText.this);

		// set edititemnamedailog  to alertdialog builder
		alertDialogBuilder.setView(promptsView);

		 final EditText userInput = (EditText) promptsView
				.findViewById(R.id.editTextDialogUserInput);

		// set dialog message
		alertDialogBuilder.setCancelable(false).setPositiveButton("OK",new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {
				// get user input and set it to result
				
		    		dataAdapter.remove(dataAdapter.getItem(pos)); 
		    		NotifyAdapterChanges();
		           	dataAdapter.insert(userInput.getText().toString(), pos);
		            NotifyAdapterChanges();
			    	  

			    }
			  })
			.setNegativeButton("Cancel",
			  new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			    }
			  });

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

		
		return result;
	
    }
    public void onUndoClick(View v) {
    	
    	
		dataAdapter.remove(dataAdapter.getItem(position1)); 
		NotifyAdapterChanges();
       	dataAdapter.insert(item1, position1);
        NotifyAdapterChanges();
      	dataAdapter.insert(item2, position2);
        NotifyAdapterChanges();
       
    
    }

	public void onMergeClick(View v) {
		undobtn.setEnabled(true);	
	  SparseBooleanArray checked = listView.getCheckedItemPositions();
      //  ArrayList<String> selectedItems = new ArrayList<String>();
      String selected = "";
      int cntChoice = checked.size();
      
     if(cntChoice < 3){
        for (int i = 0; i < cntChoice ; i++) {
            // Item position in adapter
            int position = checked.keyAt(i);
            // Add sport if it is checked i.e.) == TRUE!
            if (checked.valueAt(i) && (position1 != -1)){
            	    //selectedItems.add(dataAdapter.getItem(position));
            		position2 = position;
            	}else{
            		position1 = position;
            	}
        
            }
        
        /*String[] outputStrArr = new String[selectedItems.size()];
    
        for (int i = 0; i < selectedItems.size(); i++) {
            outputStrArr[i] = selectedItems.get(i);
            selected +=  outputStrArr[i] + ",";
        }*/
        
        
        Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_LONG).show();
        checkPostionOfItemsAndNewItem(position1 ,position2);
     }
        else {
        	
        	Toast.makeText(getApplicationContext(), "check more than 2", Toast.LENGTH_LONG).show();
        }

     checked.clear();
		
	}

    private void checkPostionOfItemsAndNewItem(int pos1 ,int pos2){
    	
    	String tempitem = "";
    	
    	if( (pos1+1) == pos2 ){
    		
    		item1 = dataAdapter.getItem(pos1);
    		item2 =  dataAdapter.getItem(pos2);
    		tempitem = item1+item2;
    		dataAdapter.remove(dataAdapter.getItem(pos1)); 
    		NotifyAdapterChanges();
           	dataAdapter.insert(tempitem, pos1);
            NotifyAdapterChanges();
            dataAdapter.remove(dataAdapter.getItem(pos2)); 
        	NotifyAdapterChanges();
    	}
    	
    }
    private void NotifyAdapterChanges(){
       	dataAdapter.notifyDataSetChanged();
    	
    }
	
}
