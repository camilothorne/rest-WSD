package com.wsd.babelnet;

//import java.io.FileOutputStream;
//import java.io.PrintStream;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;


//import org.apache.commons.io.output.TeeOutputStream;
import org.apache.http.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

//import com.codesnippets4all.json.parsers.JSONParser;
//import com.codesnippets4all.json.parsers.JsonParserFactory;

import org.json.*;
 

/**
 * A simple Java REST GET example using the Apache HTTP library
 * to query the BabelNet service
 */
@SuppressWarnings("deprecation")
public class BNRest {
	
	
	// API key
    public static final String KEY = "467dc09a-0698-453d-93bc-bef0a020f7a0";
    
    
    //  uncomment to redirect stout to file
//	private static FileOutputStream fos;
//	private static TeeOutputStream myOut;
//	private static PrintStream ps;


	/**
	 * tagger method
	 * 
	 * @param inputText
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "resource" })
    public static void askSenses(String synsetID, String lang) throws Exception{
		
		// uncomment to send stout to file
//		fos 	= 	new FileOutputStream("/home/camilo/tagme.txt");
//		myOut	=	new TeeOutputStream(System.out, fos);
//		ps 		= 	new PrintStream(myOut,true,"UTF-8");
//		System.setOut(ps);
		
	    DefaultHttpClient httpclient 	= new DefaultHttpClient();
	    //JsonParserFactory factory		= JsonParserFactory.getInstance();
	    //JSONParser parser				= factory.newJsonParser();
	    
	    try {
	    	
	    	// specify the host, protocol, and port	       
	    	HttpHost target = new HttpHost("babelnet.io");	
	    	
	    	// specify the get request
	    	String stringRequest = 
	    			"/v2/getSynset?id=" 	+ synsetID
	    			+ "&filterLangs=" 	+ lang
	    			+ "&key=" + KEY;
	    	
	    	// create request
	    	HttpGet getRequest = new HttpGet(stringRequest);
	 
	    	//System.out.println("----------------------------------------");
	    	//System.out.println("executing request to " + target);
	 
	    	// launch request and retrieve response
	    	HttpResponse httpResponse = httpclient.execute(target, getRequest);
	    	HttpEntity entity = httpResponse.getEntity();
	 
	    	// uncomment to print response headers
//	    	System.out.println("----------------------------------------");
//	    	System.out.println(httpResponse.getStatusLine());
//	    	Header[] headers = httpResponse.getAllHeaders();
//	    	for (int i = 0; i < headers.length; i++) {
//	    		System.out.println(headers[i]);
//	    	}
//	    	System.out.println("----------------------------------------");
	    	
	    	// print JSON response object
	    	if (entity != null) {
	    		
	    		String data = EntityUtils.toString(entity);
	    		
	    		// uncomment to print raw JSON response
	    		//System.out.println("JSON: " + data);
	    	    
	    		JSONObject obj = new JSONObject(data);
	    		JSONArray res = obj.getJSONArray("senses");
	    	    
	    	    //ArrayList<Map> annotations =  (ArrayList<Map>)res;

	    	    //System.out.println("\t ------------------------------");
		    	//System.out.println("\t BN synset: " + synsetID);
		    	if (!res.equals(null)){
		    	    for (int count=0;count<res.length();count++){
		    	    	System.out.println("\t ------------------------------");
		    	    	System.out.println("\t sense " + count + ": " + res.getJSONObject(count).getString("lemma"));	    	    	
		    	    	System.out.println("\t language: " + res.getJSONObject(count).getString("language"));
		    	    	System.out.println("\t source: " 	+ res.getJSONObject(count).getString("source"));
		    	    }
			    	//System.out.println("\t ------------------------------");
		    	}
	    	}
	 
	    } 
	    catch (Exception e) {
	    		e.printStackTrace();
	    } 
	    finally {
	    		// When HttpClient instance is no longer needed,
	    		// shut down the connection manager to ensure
	    		// immediate deallocation of all system resources
	    		httpclient.getConnectionManager().shutdown();
	    }	
		
    }
	
	
	/**
	 * tagger method
	 * 
	 * @param inputText
	 * @param jsonobject
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "resource" })
    public static String askSense(String synsetID, String lang) throws Exception{
		
		String sense = null;
		
	    DefaultHttpClient httpclient 	= new DefaultHttpClient();
	    
	    try {
	    	
	    	// specify the host, protocol, and port	       
	    	HttpHost target = new HttpHost("babelnet.io");	
	    	
	    	// specify the get request
	    	String stringRequest = 
	    			"/v2/getSynset?id=" 	+ synsetID
	    			+ "&filterLangs=" 	+ lang
	    			+ "&key=" + KEY;
	    	
	    	// create request
	    	HttpGet getRequest = new HttpGet(stringRequest);
	 
	    	// launch request and retrieve response
	    	HttpResponse httpResponse = httpclient.execute(target, getRequest);
	    	HttpEntity entity = httpResponse.getEntity();
	    	
	    	// get JSON response object
	    	if (entity != null) {
	    		
	    		String data = EntityUtils.toString(entity);
	    		JSONObject obj = new JSONObject(data);
	    		JSONArray res = obj.getJSONArray("senses");
	    	    
	    		// get topmost sense in babelsynset
		    	if (!res.equals(null)){
		    	    sense = res.getJSONObject(0).getString("lemma");
		    	}
	    	}
	 
	    } 
	    catch (Exception e) {
	    		e.printStackTrace();
	    } 
	    finally {
	    		// When HttpClient instance is no longer needed,
	    		// shut down the connection manager to ensure
	    		// immediate deallocation of all system resources
	    		httpclient.getConnectionManager().shutdown();
	    }	
		return sense;
    }	
	
	
	/**
	 * main method
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		// input sentence
		String input = "bn:03244279n";
	  
		// call remote tagger
		askSenses(input, "EN");
		askSenses(input, "IT");
		askSenses(input, "DE");
	    
  	}
	
}
