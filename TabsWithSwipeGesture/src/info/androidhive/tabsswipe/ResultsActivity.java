package info.androidhive.tabsswipe;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class ResultsActivity extends Activity
{
	String outputPath;
	TextView tv;
	//yai add ki hy idhar line neche s cooment kar dia hy
	StringBuffer contents = new StringBuffer();
	StringSplitter SS;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		tv = new TextView(this);
		setContentView(tv);
		
		String imageUrl = "unknown";
		
		Bundle extras = getIntent().getExtras();
		if( extras != null)
		{
			imageUrl = extras.getString("IMAGE_PATH" );
			outputPath = extras.getString( "RESULT_PATH" );
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

			displayMessage(contents.toString());
			SS = new StringSplitter(display());
			MergeIntent(SS.Splitter());
		}
		catch (Exception e)
		{
			displayMessage("Error: " + e.getMessage());
		}
	}
	
	private void MergeIntent(String outputstr){
		
		Intent MergeIntent = new Intent(ResultsActivity.this, MergingText.class);
		MergeIntent.putExtra("SplittedString", outputstr);
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
			setContentView( tv );
		}

		private final String _message;
	}
}