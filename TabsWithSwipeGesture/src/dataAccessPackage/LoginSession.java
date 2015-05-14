package dataAccessPackage;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.provider.SyncStateContract.Constants;

public class LoginSession
{
	private  SharedPreferences prefLogin; //Shared preferences for login
	private Editor editorLogin;   //Editor for shared preferences of login
	Context context;
	private int PRIVATE_MODE = 0;       //Shared pref mode
	private static final String loginFile_ = "LoginSession";  //Shared pref file name
	private static final String loginKey = "IsLoggedIn"; //SharedPrefrence login key
	private static final String emailKey = "email" ; //SharedPreference email key
	private static final String passwordKey = "password"; //SharedPreference password key
	private static final String nameKey = "name"; //SharedPreference name key
	//Login Session Constructor
	
    public LoginSession(){};
	public LoginSession(Context context_)
	{
		context = context_;
		prefLogin = context.getSharedPreferences(loginFile_, PRIVATE_MODE);
		editorLogin = prefLogin.edit();		
	}
	
	//Storing login status, email and password in sharedpreferences
	public void createLoginSession(String email_, String password_, String name_)
	{
		editorLogin.putBoolean(loginKey, true);
		editorLogin.putString(emailKey, email_);
		editorLogin.putString(passwordKey, password_);
		editorLogin.putString(nameKey, name_);
		editorLogin.commit();
	}
	
	//Checking user login status
	public boolean checkLoginStatus()
	{
		return prefLogin.getBoolean(loginKey,false);
	}
	
	//Get stored session data
	public HashMap<String , String> getUserDetails()
	{
		HashMap<String,String> user = new HashMap<String,String>();
		user.put(emailKey, prefLogin.getString(emailKey, null));
		user.put(passwordKey, prefLogin.getString(passwordKey, null));
		user.put(nameKey, prefLogin.getString(nameKey, null));
		return user;
	}
	
	//Get email 
	public String  getUserEmail()
	{
			return prefLogin.getString(emailKey, null);
	}
		
	//Get editor
	Editor getLoginEditor()
	{
		return editorLogin;
	}
	
	//Clear editor data after logout
	public void clearDataAfterLogout()
	{
		editorLogin.clear();
		editorLogin.commit();
	}

	
	
}