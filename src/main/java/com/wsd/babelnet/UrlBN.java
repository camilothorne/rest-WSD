package com.wsd.babelnet;


import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.wsd.util.Pair;


/**
* A simple Java GET procedure to
* to query the BabelNet 2.5.1 local 
* web server
* 
* Note: the server must be up and running, and
* doesn't return glosses (thus, it is not usable
* for WSD); it is meant to be used in combination
* with the BabelFly web service
* 
*/
@SuppressWarnings("deprecation")
public class UrlBN {
	
	
	// id of client
	private final static String USER_AGENT = "Java";	
  
	
	// uncomment to redirect stout to file
//	private static FileOutputStream fos;
//	private static TeeOutputStream myOut;
//	private static PrintStream ps;


	/**
	 * tagger method
	 * 
	 * @param synsetID
	 * @param language
	 * 
	 * @throws Exception
	 */
    public static void askSenses(String synsetID, String lang) throws Exception{
		
		// uncomment to send stout to file
//		fos 	= 	new FileOutputStream("/home/camilo/tagme.txt");
//		myOut	=	new TeeOutputStream(System.out, fos);
//		ps 		= 	new PrintStream(myOut,true,"UTF-8");
//		System.setOut(ps);
		
		HttpClient httpclient = HttpClientBuilder.create().build();
	    
	    try {
	    	
	    	// specify the host, protocol, and port	       
	    	HttpHost target = new HttpHost("localhost",9000,"http");
	    	
	    	String rooturl = "http://localhost:9000";
	    	
	    	// specify the get request
	    	String stringRequest = 
	    				"/synset/"		+ synsetID
	    			+ 	"/senses/" 		+ lang;
	    	
	    	// create request
	    	HttpGet getRequest = new HttpGet(rooturl+stringRequest);
	    	getRequest.addHeader("User-Agent", USER_AGENT);
	 
	    	//System.out.println("----------------------------------------");
	    	//System.out.println("executing request to " + target + " (BabelNet 2.5.1)\n");
	 
	    	// launch request and retrieve response
	    	HttpResponse httpResponse = httpclient.execute(getRequest);
	    	HttpEntity entity = httpResponse.getEntity();
	 
	    	// uncomment to print response headers
//	    	System.out.println("----------------------------------------");
//	    	System.out.println(httpResponse.getStatusLine());
//	    	Header[] headers = httpResponse.getAllHeaders();
//	    	for (int i = 0; i < headers.length; i++) {
//	    		System.out.println(headers[i]);
//	    	}
//	    	System.out.println("----------------------------------------");
	    	
	    	// print response object
	    	if (entity != null) {
	    		
	    		String data = EntityUtils.toString(entity);
	    		
	    		// uncomment to print raw response
//	    		System.out.println("(response:)\n" + data);
	    	    
	    		// process response
	    		String[] 	rows 			= data.split("\n");
	    		String[][]  lines			= new String[rows.length][4];
	    		for (int i=0;i<rows.length;i++){
	    			lines[i] = rows[i].split("\t"); 
	    		}

		    	if (lines[0].length>1){
		    	    for (int c=0;c<rows.length;c++){
		    	    	System.out.println("\t ------------------------------");
		    	    	System.out.println("\t sense " + c + ": " 	+ lines[c][0]);
		    	    	System.out.println("\t WordNet: " 		  	+ lines[c][6]);   	
		    	    	System.out.println("\t POS: " 				+ lines[c][1]);		    	    	
		    	    	System.out.println("\t language: " 		 	+ lines[c][2]);
		    	    	System.out.println("\t gloss: " 		  	+ lines[c][4]);	 
		    	    	System.out.println("\t source: " 		  	+ lines[c][3]);	   
		    	    }
			    	System.out.println("\t ------------------------------");
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
	 * @param synsetID
	 * @param language
	 * 
	 * @returns sense
	 * 
	 * @throws Exception
	 */
    public static String[] askSense(String synsetID, String lang) throws Exception{
		
		String sense = null;
		String gloss = null;
		String wnnet = null;
		
		HttpClient httpclient = HttpClientBuilder.create().build();
	    
	    try {
	    	
	    	// specify the host, protocol, and port	       
	    	HttpHost target = new HttpHost("localhost",9000,"http");
	    	
	    	String rooturl = "http://localhost:9000";
	    	
	    	// specify the get request
	    	String stringRequest = 
	    				"/synset/"		+ synsetID
	    			+ 	"/senses/" 		+ lang;
	    	
	    	// create request
	    	HttpGet getRequest = new HttpGet(rooturl+stringRequest);
	    	getRequest.addHeader("User-Agent", USER_AGENT);
	 
	    	//System.out.println("----------------------------------------");
	    	//System.out.println("executing request to " + target + " (BabelNet 2.5.1)\n");
	 
	    	// launch request and retrieve response
	    	HttpResponse httpResponse = httpclient.execute(getRequest);
	    	HttpEntity entity = httpResponse.getEntity();
	    	
	    	// get JSON response object
	    	if (entity != null) {
	    		
	    		String data = EntityUtils.toString(entity);
	    		
	    		// uncomment to print raw response
	    		//System.out.println("(response:)\n" + data);
	    	    
	    		// process response
	    		String[] 	rows 			= data.split("\n");
	    		String[][]  lines			= new String[rows.length][4];
	    		for (int i=0;i<rows.length;i++){
	    			lines[i] = rows[i].split("\t");
	    		}
	    	    
	    		// get topmost sense in babelsynset
			    if (lines[0].length>1){
			    	sense = lines[0][0];
			    	gloss = lines[0][4];
			    	wnnet = lines[0][6];
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
	    
	    String res[] = {sense,wnnet,gloss};
	    
		return res;
		
    }
    
    
    /**
     * ask for DBpedia URI (via BableNet)
     * 
     * @param wordnetid
     * @return
     */
    public static String[] WNmappings(String wordnetid){
 
    	String babelnet = null;
    	String dbpedia  = null;
    	
		HttpClient httpclient = HttpClientBuilder.create().build();
	    
	    try {
	    	
	    	// specify the host, protocol, and port	       
	    	HttpHost target = new HttpHost("localhost",9000,"http");
	    	
	    	String rooturl = "http://localhost:9000";
	    	
	    	// specify the get request
	    	String stringRequest = 
	    				"/wordnet/"		+ wordnetid;
	    	
	    	// create request
	    	HttpGet getRequest = new HttpGet(rooturl+stringRequest);
	    	getRequest.addHeader("User-Agent", USER_AGENT);
	 
	    	//System.out.println("----------------------------------------");
	    	//System.out.println("executing request to " + target + " (BabelNet 2.5.1)\n");
	 
	    	// launch request and retrieve response
	    	HttpResponse httpResponse = httpclient.execute(getRequest);
	    	HttpEntity entity = httpResponse.getEntity();
	    	
	    	// get JSON response object
	    	if (entity != null) {
	    		
	    		String data = EntityUtils.toString(entity);
	    		
	    		// uncomment to print raw response
	    		//System.out.println("(response:)\n" + data);
	    	    
	    		// process response
	    		String[] 	rows 			= data.split("\n");
	    		String[][]  lines			= new String[rows.length][4];
	    		for (int i=0;i<rows.length;i++){
	    			lines[i] = rows[i].split("\t");
	    		}
	    	    
	    		// get topmost sense in babelsynset
			    if (lines[0].length>1){
			    	babelnet 	= lines[0][0];
			    	dbpedia 	= lines[0][1];
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
    	
	    String res[] = {babelnet,dbpedia};
	    
		return res;
    	
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
	  
		// call tagger (all senses)
		askSenses(input, "en");
		askSenses(input, "es");
		askSenses(input, "de");
	    
		// call tagger (1 sense)
		System.out.println(askSense(input,"es")[0]);
		
	}
	
}

