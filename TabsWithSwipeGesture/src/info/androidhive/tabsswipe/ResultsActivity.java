package info.androidhive.tabsswipe;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ResultsActivity extends Activity
{
	private String outputPath,userName,ocresult;
	private TextView tv;
	private Button mergeBtn;
	//yai add ki hy idhar line neche s cooment kar dia hy
	StringBuffer contents = new StringBuffer();
	StringSplitter SS,SS1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ocrresult_activity);
		tv = (TextView) findViewById(R.id.ocrResult);
		mergeBtn= (Button) findViewById(R.id.continueMergeText);
		mergeBtn.setEnabled(false);
		
		String imageUrl = "unknown";
		
		Bundle extras = getIntent().getExtras();
		if( extras != null)
		{
			imageUrl = extras.getString("IMAGE_PATH" );
			outputPath = extras.getString( "RESULT_PATH" );
			userName = extras.getString("name");
		}
		
		// Starting recognition process
		new AsyncProcessTask(this).execute(imageUrl, outputPath);
	}

	public void updateResults(Boolean success)
	{
		if (!success)
		{
			return;
		}
		try
		{
			//StringBuffer contents = new StringBuffer();

			FileInputStream fis = openFileInput(outputPath);
			try
			{
				Reader reader = new InputStreamReader(fis, "UTF-8");
				BufferedReader bufReader = new BufferedReader(reader);
				String text = null;
				while ((text = bufReader.readLine()) != null)
				{
					contents.append(text).append(System.getProperty("line.separator"));
				}
			}
			finally
			{
				fis.close();
			}

			
		    ocresult= display();
		    SS = new StringSplitter(ocresult);
			if(SS.checkOCRResult()){
		       displayMessage(contents.toString());	
			   mergeBtn.setEnabled(true);
			   mergeBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub		
					Log.e("this is output: ", ocresult);
					SS1 = new StringSplitter(ocresult);
					MergeIntent(SS1.Stringsplitter());
				}
			  });
		  }else{
			   displayMessage(contents.toString());
		    	createIncorrectImageFormatAlertDailog();
		    }		
		}
		catch (Exception e)
		{
			displayMessage("Error: " + e);
			createIncorrectImageFormatAlertDailog();
		}
		
		
	}
	
	void createIncorrectImageFormatAlertDailog(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ResultsActivity.this);
 
			// set title
			alertDialogBuilder.setTitle("Extract Result");
 
			// set dialog message
			alertDialogBuilder
				.setMessage("Incorrect Image Format")
				.setCancelable(false)
				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
						Intent i = new Intent(ResultsActivity.this,MainActivity.class);
				    	i.putExtra("name", userName);
				    	i.putExtra("tab", "Upload Receipt");
				    	startActivity(i);
				    	finish();
					}
				  });
				
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
	}
	
	
	@Override
	public void onBackPressed() {
		finish();
	}

	private void MergeIntent(String outputstr){
		
		
		Intent MergeIntent = new Intent(ResultsActivity.this, MergingText.class);
		MergeIntent.putExtra("SplittedString", outputstr);
		MergeIntent.putExtra("StoreName", SS1.getStoreName());
		MergeIntent.putExtra("InvoiceNumber", SS1.getInvoiceNumber());
		MergeIntent.putExtra("StoreLocation", SS1.getStoreLocation());
		MergeIntent.putExtra("name", userName);
		startActivity(MergeIntent);
		
	}
	
	public String display ()
	{
		String str = contents.toString();
		return str;
	}
	
	public void displayMessage( String text )
	{
		tv.post( new MessagePoster( text ) );
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_results, menu);
		return true;
	}

	class MessagePoster implements Runnable
	{
		public MessagePoster( String message )
		{
			_message = message;
		}

		public void run()
		{
			tv.append( _message + "\n" );
			//setContentView( tv );
		}

		private final String _message;
	}
}