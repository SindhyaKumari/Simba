package info.androidhive.tabsswipe;

import com.SIMBA.Welcome.StartupActivity;

import info.androidhive.tabsswipe.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ShoppingListManager extends Fragment implements OnClickListener
{
	Button showlistbtn;
	Button createlistbtn;
	String user = null ;
	Bundle b ;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_shoppinglist, container, false);
		
		Bundle r =getArguments();
		if(r == null){
			Log.e("fff", r + " ");
		}else{
			Log.e("fff", r.toString());
		}
	//	user = getArguments().getString("name");
		showlistbtn = (Button) rootView.findViewById(R.id.showlist);
		showlistbtn.setOnClickListener(this);
		//createlistbtn = (Button) rootView.findViewById(R.id.createlist);
		//createlistbtn.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()== R.id.showlist)
		{
			Intent viewlistIntent = new Intent(getActivity(), ViewAllLists.class);
			
			viewlistIntent.putExtra("userName","k112218"); // put your data
			
			getActivity().startActivity( viewlistIntent );
		}
	/*	if (v.getId()== R.id.createlist)
		{
			Intent createlistIntent = new Intent(getActivity(), CreateList.class);
			getActivity().startActivity(createlistIntent);
		}*/
	}
	
}