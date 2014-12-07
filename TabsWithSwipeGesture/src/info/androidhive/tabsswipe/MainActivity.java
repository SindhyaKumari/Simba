package info.androidhive.tabsswipe;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.SIMBA.Welcome.LoginActivity;
import com.SIMBA.Welcome.ResetPasswordActivity;

import dataAccessPackage.LoginSession;
import info.androidhive.tabsswipe.adapter.TabsPagerAdapter;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, MyDialog.Communicator,OnClickListener
{
	Uri contentUri;
	private String resultUrl = "result.txt";
	static final int REQUEST_TAKE_PHOTO = 2;
	private String fileName;
	private static int RESULT_LOAD_IMAGE = 1;
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	String mCurrentPhotoPath;
	// Tab titles
	private String[] tabs = { "ShoppingList Manager", "Upload Receipt", "Promotional Offers" };
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		
		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		

		// Adding Tabs
		for (String tab_name : tabs)
		{
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * 
		 */
		
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
		{
			@Override
			public void onPageSelected(int position)
			{
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{}

			@Override
			public void onPageScrollStateChanged(int arg0)
			{}
		});
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft)
	{}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft)
	{
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft)
	{}
	
	//Start of onActivityResult
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
    	super.onActivityResult(requestCode, resultCode, data);
    	
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data)
		{
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			fileName = picturePath;
			ImageView mImageView = (ImageView) findViewById(R.id.imgView);
            //imageView.setImageBitmap(photo);
			int targetW = mImageView.getWidth();
			int targetH = mImageView.getHeight();

			// Get the dimensions of the bitmap
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			bmOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(picturePath, bmOptions);
			int photoW = bmOptions.outWidth;
			int photoH = bmOptions.outHeight;

			// Determine how much to scale down the image
			int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

			// Decode the image file into a Bitmap sized to fill the View
			bmOptions.inJustDecodeBounds = false;
			bmOptions.inSampleSize = scaleFactor;
			bmOptions.inPurgeable = true;

			Bitmap bitmap = BitmapFactory.decodeFile(picturePath, bmOptions);
			mImageView.setImageBitmap(bitmap);
			cursor.close();
			
			//ImageView imageView = (ImageView) findViewById(R.id.imgView);
			//imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
		}
		if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK)
		{
			ImageView mImageView = (ImageView) findViewById(R.id.imgView);
            //imageView.setImageBitmap(photo);
			int targetW = mImageView.getWidth();
			int targetH = mImageView.getHeight();

			// Get the dimensions of the bitmap
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			bmOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
			int photoW = bmOptions.outWidth;
			int photoH = bmOptions.outHeight;

			// Determine how much to scale down the image
			int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

			// Decode the image file into a Bitmap sized to fill the View
			bmOptions.inJustDecodeBounds = false;
			bmOptions.inSampleSize = scaleFactor;
			bmOptions.inPurgeable = true;

			Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
			mImageView.setImageBitmap(bitmap);
        }
	}	//End of onActivityResult
	
/*	
	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
	    String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
	    return Uri.parse(path);
	}

	public String getRealPathFromURI(Uri uri) {
	    Cursor cursor = getContentResolver().query(uri, null, null, null, null); 
	    cursor.moveToFirst(); 
	    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
	    return cursor.getString(idx); 
	}
	*/
	
	public void showDialog(View v)
	{
		FragmentManager manager = getFragmentManager();
		MyDialog mydialog= new MyDialog();
		mydialog.show(manager, "My Dialog");
	}
	
	public void showDialog1(View v)
	{
		final Button convertButton = (Button) findViewById(R.id.convert);
		convertButton.setOnClickListener(this);
	}
	
	@Override
	public void onDialogMessage()
	{
		// TODO Auto-generated method stub
		Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, RESULT_LOAD_IMAGE);
	}
	
	public void onDialogMessage1()
	{
		// TODO Auto-generated method stub
		dispatchTakePictureIntent();
	}

	private void dispatchTakePictureIntent()
	{
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    // Ensure that there's a camera activity to handle the intent
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null)
	    {
	    	// Create the File where the photo should go
	        File photoFile = null;
	        try
	        {
	        	photoFile = createImageFile();
	        }
	        catch (IOException ex)
	        {
	        	// Error occurred while creating the File
	        }
	        // Continue only if the File was successfully created
	        if (photoFile != null)
	        {
	        	takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile));
	            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
	        }
	    }
	}
	
	private File createImageFile() throws IOException
	{
		// Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );

	    // Save a file: path for use with ACTION_VIEW intents
	    mCurrentPhotoPath = image.getAbsolutePath();
	    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    File f = new File(mCurrentPhotoPath);
	    contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    this.sendBroadcast(mediaScanIntent);
	    fileName = mCurrentPhotoPath;
	    return image;
	}

	// Start of onClick
	
	@Override
	public void onClick(View v)
	{
		Intent results = new Intent(this, ResultsActivity.class);
    	results.putExtra("IMAGE_PATH", fileName);
    	results.putExtra("RESULT_PATH", resultUrl);
    	startActivity(results);
		// TODO Auto-generated method stub
    	//This is online OCR Api code
		/*apiKey = "89EzYZTSxr";
		langCode = "en";
		
		// Checking are all fields set
		if (fileName != null && !apiKey.equals("") && !langCode.equals(""))
		{
			final ProgressDialog dialog = ProgressDialog.show( MainActivity.this, "Loading ...", "Converting to text.", true, false);
			final Thread thread = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					final OCRServiceAPI apiClient = new OCRServiceAPI(apiKey);
					apiClient.convertToText(langCode, fileName);
					
					// Doing UI related code in UI thread
					runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							dialog.dismiss();
							
							// Showing response dialog
							final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
							
							alert.setMessage(apiClient.getResponseText());
							alert.setPositiveButton("OK", new DialogInterface.OnClickListener()
							{
								public void onClick( DialogInterface dialog, int id)
								{}
							});
							
							// Setting dialog title related from response code
							if (apiClient.getResponseCode() == RESPONSE_OK)
							{
								alert.setTitle("Success");
							}
							else
							{
								alert.setTitle("Failed");
							}
							
							alert.show();
						}
					});
				}
			});
			thread.start();
		}
		else
		{
			Toast.makeText(MainActivity.this, "All data are required.", Toast.LENGTH_SHORT).show();
		}
		*/	
	} // End of onClick
	

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		//get intent 
		Intent i = getIntent();
		final String email = i.getExtras().getString("email");
		// checking which item is being selected
		LoginSession loginObj = new LoginSession(getApplicationContext());
		String str = (String) item.getTitle();
		if(str.equals("Logout"))
		{
			Toast.makeText(MainActivity.this, "Logout!", Toast.LENGTH_SHORT).show();
			loginObj.checkLoginStatus();
			
			//calling login activity
			Intent startUpIntent = new Intent(MainActivity.this, LoginActivity.class);
			startActivity(startUpIntent);
			
	       //closing main activity			
			finish();
		}else if(str.equals("Delete Account")){
			  createDeleteAccountAlertDailog(email);			
		}else if(str.equals("Change Password")){
			//calling login activity
			
			Intent resetPasswordIntent = new Intent(MainActivity.this,ResetPasswordActivity.class);
			resetPasswordIntent.putExtra("email", email);
			startActivity(resetPasswordIntent);
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	void createDeleteAccountAlertDailog(final String email){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
 
			// set title
			alertDialogBuilder.setTitle("Delete Account");
 
			// set dialog message
			alertDialogBuilder
				.setMessage("Are you sure you want to delete your account")
				.setCancelable(false)
				.setPositiveButton("Cancel",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				  })
				.setNegativeButton("OK",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						new DeleteAccountTask().execute(email);
						 dialog.dismiss();
					}
				});
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
	}
}