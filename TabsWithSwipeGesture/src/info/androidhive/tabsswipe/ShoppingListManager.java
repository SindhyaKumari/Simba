package info.androidhive.tabsswipe;

import info.androidhive.tabsswipe.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ShoppingListManager extends Fragment implements OnClickListener
{
	Button bt,bt1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_shoppinglist, container, false);
		bt = (Button) rootView.findViewById(R.id.button1);
		bt.setOnClickListener(this);
		bt1 = (Button) rootView.findViewById(R.id.button2);
		bt1.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()== R.id.button1)
		{
			Intent intent = new Intent(getActivity(), ViewAllLists.class);
	
			// intent.putExtra(....); // put your data
			
			getActivity().startActivity(intent);
		}
		if (v.getId()== R.id.button2)
		{
			Intent myIntent2 = new Intent(getActivity(), CreateList.class);
			getActivity().startActivity(myIntent2);
		}
	}
	
}