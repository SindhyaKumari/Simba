package info.androidhive.tabsswipe;

import java.util.Timer;
import java.util.TimerTask;

import info.androidhive.tabsswipe.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher.ViewFactory;

public class PromotionalOffersFragment extends Fragment implements OnClickListener
{
	private ImageSwitcher imageSwitcher;
	Button bStart, bStop ;
    private int[] gallery = {R.drawable.splash0, R.drawable.splash1, R.drawable.splash2, R.drawable.splash3, R.drawable.splash5, R.drawable.splash6};
    private int position = 0;
    boolean found = false;
    private Timer timer = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_promotionaloffers, container, false);
		imageSwitcher = (ImageSwitcher) rootView.findViewById(R.id.imageSwitcher);
		bStart = (Button) rootView.findViewById(R.id.buttonStart);
		bStart.setOnClickListener(this);
		bStop = (Button) rootView.findViewById(R.id.buttonStop);
		bStop.setOnClickListener(this);
		
        imageSwitcher.setFactory(new ViewFactory()
        {
        	public View makeView() 
            {
        		return new ImageView(getActivity());
            } 
        });
        
        // Set animations http://danielme.com/2013/08/18/diseno-android-transiciones-entre-activities/
        Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in2);
        imageSwitcher.setInAnimation(fadeIn);
        imageSwitcher.setOutAnimation(fadeOut);
        return rootView;
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if(v.getId()== R.id.buttonStart)
		{
			found = true;
			timer = new Timer();
		    timer.scheduleAtFixedRate(new TimerTask()
		    {
		    	public void run()
		    	{
		    		// avoid exception: "Only the original thread that created a view hierarchy can touch its views"
		            getActivity().runOnUiThread(new Runnable()
		            {
		            	public void run()
		            	{
		            		imageSwitcher.setImageResource(gallery[position]);
		                    position++;
		                    if (position == 6)
		                    {
		                    	position = 0;
		                    }
		                }
		            });
		    	}
		  
		    }, 0, 2500);
		}
		else if(v.getId() == R.id.buttonStop)
		{
			if (found == true)
			{
				timer.cancel();
			}
		}
	}
}