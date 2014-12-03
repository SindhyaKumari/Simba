package dataAccessPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser
{
	static InputStream inputStream = null;
    static JSONObject jsonObject = null;
    static String json = "";
    static String rawJson = "";
    // constructor
    public JSONParser()
    {}
 
    public JSONObject getJSONFromUrl(String method_, String url_, List<NameValuePair> params_)
    {
    	// Making HTTP request
        try
        {
        	// defaultHttpClient  
            // check for request method
            if(method_ == "POST")
            {
            	// request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url_);
                httpPost.setEntity(new UrlEncodedFormEntity(params_));
                //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                //StrictMode.setThreadPolicy(policy);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();
 
            }
            else if(method_ == "GET")
            {
            	// request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params_, "utf-8");
                url_ += "?" + paramString;
                HttpGet httpGet = new HttpGet(url_);
                // StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                // StrictMode.setThreadPolicy(policy);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();
            }
        }
        catch (UnsupportedEncodingException e)
        {
        	e.printStackTrace();
        }
        catch (ClientProtocolException e)
        {
        	e.printStackTrace();
        }
        catch (IOException e) 
        {
        	e.printStackTrace();
        }      
        try
        {
        	BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
            	sb.append(line + "\n");
                Log.d("fffff", sb.toString());
            }
            inputStream.close();
         
            // splittng string to avoid hosting domian string
            rawJson = sb.toString();
            String[] parts = rawJson.split("<");
            json = parts[0];
        }
        catch (Exception e)
        {
        	Log.e("Buffer Error", "Error converting result " + e.toString());
        }
 
        // try parse the string to a JSON object
        try
        {
        	jsonObject = new JSONObject(json);        	   
        }
        catch (JSONException e)
        {
        	Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
 
        // return JSON String
        return jsonObject;
    }
}