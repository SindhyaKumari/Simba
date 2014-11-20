package dataAccessPackage;

import java.util.ArrayList;
import java.util.List;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;


public class UserFunctions {
	 
   private JSONParser jsonParser;


   // constructor
   public UserFunctions(){
       jsonParser = new JSONParser();
   }
	     

	     
   /**
	  * function make Register request
	  * @param name
	  * @param email
	  * @param password
	  * @param contact
	  * @param dateofbirth
	  * @param address
	  * @throws JSONException 
   * */
   public JSONObject registerUser(String name_, String email_, String password_, String contact_ ,String dateOfBirth_,String address_) {
	        // Building Parameters
	        List<NameValuePair> params_ = new ArrayList<NameValuePair>();
	     //   params_.add(new BasicNameValuePair("tag", registerTag));
	        params_.add(new BasicNameValuePair("name", name_));
	        params_.add(new BasicNameValuePair("email", email_));
	        params_.add(new BasicNameValuePair("password", password_));
	        params_.add(new BasicNameValuePair("contact", contact_));
	        params_.add(new BasicNameValuePair("dob", dateOfBirth_));
	        params_.add(new BasicNameValuePair("address", address_));
	        // getting JSON Object
	        JSONObject json = jsonParser.getJSONFromUrl("POST","registerURL_", params_);
	        // return json
	      
	        return json;
  }



}
