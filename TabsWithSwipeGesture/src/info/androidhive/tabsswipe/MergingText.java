package info.androidhive.tabsswipe;

import java.util.ArrayList;

import dataAccessPackage.InvoiceAndStoreNamePreferences;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
    Button mergeUpbtn,undobtn,mergeDownbtn,submit;
    ArrayList<String> Items = new ArrayList<String>();
    int position1= -1 , position2 = -1;
    String item1, item2, itemlist,storeName,InvoiceNumber;
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
   
        
        InvoiceAndStoreNamePreferences s = new InvoiceAndStoreNamePreferences(MergingText.this);
        s.clearData();
        s.createSession(storeName, InvoiceNumber);
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
    new ProductsAsyncTask(MergingText.this).execute(Items);
   }
  });
         
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
    
   
}
