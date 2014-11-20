package info.androidhive.tabsswipe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MyDialog extends DialogFragment implements View.OnClickListener{

	
	Communicator communicator;
	Button yes, camera, cancel;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		communicator = (Communicator) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.my_dialog, null);
		yes = (Button) view.findViewById(R.id.yes);
		yes.setOnClickListener(this);
		camera = (Button) view.findViewById(R.id.camera);
		camera.setOnClickListener(this);
		cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(this);
		setCancelable(false);
		return view;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId()==R.id.yes){
			communicator.onDialogMessage();
			dismiss();
		}else if (v.getId()==R.id.camera){
			communicator.onDialogMessage1();
			dismiss();
		}else if(v.getId()==R.id.cancel) {
			dismiss();
		}
		
	}

	interface Communicator
	{
		public void onDialogMessage ();
		public void onDialogMessage1();
	}
}
