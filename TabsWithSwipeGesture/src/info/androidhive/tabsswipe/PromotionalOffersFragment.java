package info.androidhive.tabsswipe;

import java.util.Timer;
import java.util.TimerTask;

import info.androidhive.tabsswipe.R;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class PromotionalOffersFragment extends Fragment {

	public int currentimageindex=0;
	ImageView slidingimage;
	
	private int[] IMAGE_IDS = {
			R.drawable.splash0, R.drawable.splash1, R.drawable.splash2,
			R.drawable.splash3
		};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_promotionaloffers, container, false);
        final Handler mHandler = new Handler();

        // Create runnable for posting
        final Runnable mUpdateResults = new Runnable() {
            public void run() {
            	AnimateandSlideShow();
            }
        };
		
        int delay = 500; // delay for 1 sec.
        int period = 7000; // repeat every 4 sec.
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
        	
	        public void run() {
	        	 mHandler.post(mUpdateResults);
	        }
	        
        }, delay, period);
        
		return rootView;
	}

	private void AnimateandSlideShow() {
    	slidingimage = (ImageView)getView().findViewById(R.id.ImageView3_Left);
   		slidingimage.setImageResource(IMAGE_IDS[currentimageindex%IMAGE_IDS.length]);
   		currentimageindex++;
   		Animation rotateimage = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
   		slidingimage.startAnimation(rotateimage);
    }
	
}
