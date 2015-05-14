package dataAccessPackage;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class InvoiceAndStoreNamePreferences {
	
	private  SharedPreferences prefInvoice; //Shared preferences for login
	private Editor editorInvoice;   //Editor for shared preferences of login
	Context context;
	private int PRIVATE_MODE = 0;       //Shared pref mode
	private static final String invoiceFile_ = "InvoiceSession";  //Shared pref file name
	private static final String storeKey = "StoreName" ; //SharedPreference store key
	private static final String invoiceKey = "InvoiceNumber"; //SharedPreference invoice key
	private static final String storeLocationKey = "StoreLocation"; //SharedPreference Store Location key
	//Invoice And StoreName Preferences Constructor
	public InvoiceAndStoreNamePreferences(Context context_)
	{
		context = context_;
		prefInvoice = context.getSharedPreferences(invoiceFile_, PRIVATE_MODE);
		editorInvoice = prefInvoice.edit();		
	}
	
	//Storing store name and invoice in sharedpreferences
	public void createSession(String store_, String invoice_, String storeloc_)
	{
		editorInvoice.putString(storeKey, store_);
		editorInvoice.putString(invoiceKey, invoice_);
		editorInvoice.putString(storeLocationKey, storeloc_);
		editorInvoice.commit();
	}
	
	//Get stored session data
	public HashMap<String , String> getStoreAndInvoiceDetails()
	{
		HashMap<String,String> storeAndInvoice = new HashMap<String,String>();
		storeAndInvoice.put(storeKey, prefInvoice.getString(storeKey, null));
		storeAndInvoice.put(invoiceKey, prefInvoice.getString(invoiceKey, null));
		storeAndInvoice.put(storeLocationKey, prefInvoice.getString(storeLocationKey, null));
		return storeAndInvoice;
	}
	
	//Get editor
	Editor getEditor()
	{
		return editorInvoice;
	}
	
	//Clear editor 
	public void clearData()
	{
		editorInvoice.clear();
		editorInvoice.commit();
	}

}
