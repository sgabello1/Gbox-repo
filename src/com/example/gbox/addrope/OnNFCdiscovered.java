package com.example.gbox.addrope;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import android.app.Activity;

public class OnNFCdiscovered extends AsyncTask {

	private String rs; 
	private String ropeId;
	private String httpIndex;
	public String wMethod;
	private Context c;
	private static final String TAG = null;
	String  text = "";
	String	httpAddress = "http://192.168.137.23:8080/wines/";
	String response ="";
	Rope  jrope;
	
	
	public OnNFCdiscovered( Rope j, String[] separated, Context context) {
		// TODO Auto-generated constructor stub
		
		ropeId = separated[0];
		httpIndex = separated[1];
		c = context;
		jrope = j;
		
	}
	@Override
	protected Object doInBackground(Object... arg0) {
		 
		 try {
			setRs(connect(ropeId, httpIndex));
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 

		return rs;
	}

	public String getRs() {
		return rs;
	}
	public void setRs(String rs) {
		this.rs = rs;
		
	}
	

	
	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		 
		 setText("Your rope is been forecasted to be used already  "+ this.getRs() +"   times");
		 
		 text = returnText();
			Toast.makeText(c, text, Toast.LENGTH_SHORT).show();

	}
	

	private void setText(String sometxt) {
		// TODO Auto-generated method stub
		text = sometxt;
		
	}
	
	
	public String returnText() {
		// TODO Auto-generated method stub
	return text;
	}
	
	private String connect(String ropeId, String httpIndex) throws URISyntaxException, ClientProtocolException, IOException, JSONException {

		
		String returnString = "";
		
		
		if(wMethod.equals("get"))
		{

			HttpClient client = new DefaultHttpClient();
	        HttpGet request = new HttpGet();
	        request.setURI(new URI(httpAddress + ropeId));
	        HttpResponse res= client.execute(request);
	        
	        HttpEntity entity = res.getEntity();
	        
	        if (entity != null) {
		           String retSrc = EntityUtils.toString(entity); 
		          //parsing JSON
		           
		           JSONObject result = new JSONObject(retSrc); //Convert String to JSON Object
		           
		           jrope.setName(result.getString("name"));
		           jrope.setUsage(Integer.parseInt(result.getString("usage")));
		           jrope.setLastUpdate(result.getString("last_update"));
		           jrope.setMaxForce(Float.parseFloat(result.getString("max_force")));
		           jrope.setMND(Integer.parseInt(result.getString("MND")));
		           
		           returnString = "Name: "+ jrope.getName() + "Usage :" + Integer.toString(jrope.getUsage()) + " " + "Last_update" + jrope.getLastUpdate();

		           	// writing response to log
		 			Log.d("Http Response:", retSrc);
		 			
		 			//returnString = retSrc;
		        }
	        
		}else if(wMethod.equals("put"))		{
			// Creating HTTP client
	        HttpClient httpClient = new DefaultHttpClient();
	        // Creating HTTP Post
	        HttpPost httpPost = new HttpPost(
	        		httpAddress+ ropeId);
	 
	        // Building post parameters
	        // key and value pair
	        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
	        
	        nameValuePair.add(new BasicNameValuePair("name", jrope.getName()));
	        nameValuePair.add(new BasicNameValuePair("usage", Integer.toString(jrope.getUsage())));
	        nameValuePair.add(new BasicNameValuePair("last_update",
	        		jrope.getLastUpdate()));
	 
	        
	        // Url Encoding the POST parameters
	        try {
	        	
	            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
	            
	        } catch (UnsupportedEncodingException e) {
	            // writing error to Log
	            e.printStackTrace();
	        }
	 
	        // Making HTTP Request
	        try {
	            HttpResponse response = httpClient.execute(httpPost);
	 
	            HttpEntity entity2 = response.getEntity();
	            
	            if(entity2 != null)
	            	{
	            	
	            	String retSrc = EntityUtils.toString(entity2); 
	            	returnString = retSrc;
	            	}
	            // writing response to log
	            Log.d("Http Response:", response.toString());
	        } catch (ClientProtocolException e) {
	            // writing exception to log
	            e.printStackTrace();
	        } catch (IOException e) {
	            // writing exception to log
	            e.printStackTrace();
	 
	        }
	        
	    
		}
	   
		return returnString;
		

	}	

}
