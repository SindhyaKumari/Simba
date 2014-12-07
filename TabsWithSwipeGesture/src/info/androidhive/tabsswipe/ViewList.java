package info.androidhive.tabsswipe;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ViewList extends Activity{

	TextView txt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewlists);
		txt = (TextView) findViewById(R.id.viewitem);
		
		Intent intent = getIntent();
		String value = intent.getExtras().getString("itemValue");
		ItemsData info = new ItemsData(this);
		info.open();
		txt.setText(info.getDataItemsView(value));
		info.close();
		}
	}