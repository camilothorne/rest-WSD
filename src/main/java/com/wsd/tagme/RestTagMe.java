package com.wsd.tagme;

//import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
//import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


//import org.apache.commons.io.output.TeeOutputStream;
import org.apache.http.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.codesnippets4all.json.parsers.JSONParser;
import com.codesnippets4all.json.parsers.JsonParserFactory;

import org.json.*;
 

/**
 * A simple Java REST GET example using the Apache HTTP library
 * to query the TagMe service
 */
@SuppressWarnings("deprecation")
public class RestTagMe {
	
	
	// API key
    public static final String KEY = "12340987ADcsWLKio77dc8377b";
    
    // annotations
    public static final String CORPUS = "results/annotations-wsd.json";
    
    
    // uncomment to redirect stout to file
//	private static FileOutputStream fos;
//	private static TeeOutputStream myOut;
//	private static PrintStream ps;


	/**
	 * tagger method (test)
	 * 
	 * @param inputText
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "resource" })
    public static void taggerTagMe(String inputText) throws Exception{
		
		// normalize input
		String norm = inputText.replaceAll(" ", "+");
		norm = norm.replaceAll("%", "+percent");
		norm = norm.replaceAll("<", "smaller+than");
		norm = norm.replaceAll(">", "greater+than");			
		
		// uncomment to send stout to file
//		fos 	= 	new FileOutputStream("/home/camilo/tagme.txt");
//		myOut	=	new TeeOutputStream(System.out, fos);
//		ps 		= 	new PrintStream(myOut,true,"UTF-8");
//		System.setOut(ps);
		
	    DefaultHttpClient httpclient = new DefaultHttpClient();
	    JsonParserFactory factory	 =	JsonParserFactory.getInstance();
	    JSONParser parser			 =	factory.newJsonParser();
	    
	    try {
	    	
	    	// specify the host, protocol, and port	       
	    	HttpHost target = new HttpHost("tagme.di.unipi.it", 80, "http");
	    	
	    	// specify the get request
	    	String stringRequest = "/tag?text=" + norm + "&key=" + KEY + "&include_abstract=true";
	    	
	    	// create request
	    	HttpGet getRequest = new HttpGet(stringRequest);
	 
	    	System.out.println("----------------------------------------");
	    	System.out.println("executing request to " + target);
	 
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
	    		System.out.println("JSON: " + data);
	    		
	    	    Map jsonData = parser.parseJson(data);
	    	    
	    	    ArrayList<Map> annotations =  (ArrayList<Map>)jsonData.get("annotations");
		    	System.out.println("----------------------------------------");
		    	System.out.println("input: " + inputText);
	    	    for (Map map : annotations){
	    	    	System.out.println("----------------------------------------");
	    	    	if (map.containsKey("title")){
		    	    	System.out.println("phrase: " 		+ map.get("spot").toString().toLowerCase());	    	    	
		    	    	System.out.println("gloss: " 		+ map.get("abstract").toString().toLowerCase());		    	    	
		    	    	System.out.println("dbid: " 			+ "http://dbpedia.org/resource/"+map.get("title").toString().replace(" ","_"));	    	    		    	    	
		    	    	System.out.println("confidence: " 	+ map.get("rho"));
		    	    	System.out.println("sense: " 		+ map.get("title").toString().toLowerCase());		    	    	
	    	    	}
	    	    }
		    	System.out.println("----------------------------------------");
	    	    
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
	 * @param  inputText
	 * @param  num
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "resource" })
    public static void taggerTagMe(String inputText, int num) throws Exception{
		
		// normalize input
		String norm = inputText.replaceAll(" ", "+");
		norm = norm.replaceAll("%", "+percent");
		norm = norm.replaceAll("<", "smaller+than");
		norm = norm.replaceAll(">", "greater+than");		
		
	    DefaultHttpClient httpclient	= new DefaultHttpClient();
	    JsonParserFactory factory		= JsonParserFactory.getInstance();
	    JSONParser parser				= factory.newJsonParser();
	    
	    try {
	    	
	    	// specify the host, protocol, and port	       
	    	HttpHost target = new HttpHost("tagme.di.unipi.it", 80, "http");		    	
	    	// specify the get request
	    	String stringRequest = "/tag?text=" + norm + "&key=" + KEY + "&include_abstract=true";
	    	// create request
	    	HttpGet getRequest = new HttpGet(stringRequest);
	    	// launch request and retrieve response
	    	HttpResponse httpResponse = httpclient.execute(target, getRequest);
	    	HttpEntity entity = httpResponse.getEntity();
	    	
	    	// load result file into JSON object    	
	    	JSONTokener tokener = new JSONTokener(new FileReader(CORPUS));
          	JSONObject root = new JSONObject(tokener);  	    	
	    	
	    	// save JSON response object
	    	if (entity != null) {	
	    			    		
	    		String data = EntityUtils.toString(entity);
	    		
	    		// uncomment to print raw JSON response
	    		// System.out.println("JSON: " + data);   
	    		
	    	    Map jsonData = parser.parseJson(data);
	    	    ArrayList<Map> annotations =  (ArrayList<Map>)jsonData.get("annotations");	    
	    	
		    	// set sentence text
	    	    Map<String,String> 	text = new HashMap();
		    	text.put("sentence", inputText);
		    	
	    	    // set NPs with annotations 
		    	Map<String,List<Map>>  	anno = new HashMap();
		    	List<Map> anns = new LinkedList();
	    	    for (Map map : annotations){
	    	    	Map<String,String> 	 ph = new HashMap();
	    	    	ph.put("phrase", 	 map.get("spot").toString().toLowerCase());
	    	    	ph.put("sense", 	 map.get("title").toString().toLowerCase());
	    	    	ph.put("dbid",    	 "http://dbpedia.org/resource/"+map.get("title").toString().replace(" ","_"));
	    	    	if (!map.containsKey("abstract")){
	    	    		ph.put("gloss",  "NaN");		    	    		
	    	    	}else{
	    	    		if (map.get("abstract").equals("")){
	    	    			ph.put("gloss",  "NaN");
	    	    		}else{
	    	    			ph.put("gloss",  map.get("abstract").toString().toLowerCase());
	    	    		}
	    	    	}
	    	    	ph.put("confidence", map.get("rho").toString());
	    	    	anns.add(ph);
	    	    }	    
	    	    anno.put("TagMe", anns);
	    	    
	    	    // check if annotations already exist
	    	    if (root.isNull(""+num+"")){
	    	    	
	    	    	// wrap tags into JSON
		    	    JSONObject janno = new JSONObject(anno);
		    	    root.append(""+num+"", janno);
		    	    
		    	    // set/wrap sentence
		    	    JSONObject jsen = new JSONObject(text);
		    	    root.append(""+num+"", jsen);
		    	    
		    	    // update file
//		    	    FileWriter file = new FileWriter(CORPUS);
//		    		file.write(root.toString(5));
//		    		file.flush();
//		    		file.close();		    	    
		    	    
	    	    }else{
		    	    
	    	    	// wrap tags into JSON
		    	    JSONObject janno = new JSONObject(anno);
		    	
		    	    // update file
		    	    root.append(""+num+"", janno);
//		    	    FileWriter file = new FileWriter(CORPUS);
//		    		file.write(root.toString(5));
//		    		file.flush();
//		    		file.close();

	    	    }
	    	    
	    	}else{
	    		
	    		String data = EntityUtils.toString(entity);
	    	    Map jsonData = parser.parseJson(data);
	    	
		    	// set sentence text
	    	    Map<String,String> 	text = new HashMap();
		    	text.put("sentence", inputText);
		    	Map<String,List<Map>>  	anno = new HashMap();
		    	
		    	// set empty annotations
		    	List<Map> anns = new LinkedList();		       
	    	    anno.put("TagMe", anns);
	    	    
	    	    // check if annotations already exist
	    	    if (root.isNull(""+num+"")){
	    	    	
	    	    	// wrap tags into JSON
		    	    JSONObject janno = new JSONObject(anno);
		    	    root.append(""+num+"", janno);
		    	    
		    	    // set/wrap sentence
		    	    JSONObject jsen = new JSONObject(text);
		    	    root.append(""+num+"", jsen);
		    	    
		    	    // update file
//		    	    FileWriter file = new FileWriter(CORPUS);
//		    		file.write(root.toString(5));
//		    		file.flush();
//		    		file.close();		    	    
		    	    
	    	    }else{
		    	    
	    	    	// wrap tags into JSON
		    	    JSONObject janno = new JSONObject(anno);
		    	
		    	    // update file
		    	    root.append(""+num+"", janno);
//		    	    FileWriter file = new FileWriter(CORPUS);
//		    		file.write(root.toString(5));
//		    		file.flush();
//		    		file.close();

	    	    }
	    		
	    	}
	    	
    	    // update file
    	    FileWriter file = new FileWriter(CORPUS);
    		file.write(root.toString(5));
    		file.flush();
    		file.close();
	 
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
	 * main method
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		// input sentence(s)
		
		String input0 = "Strategies for preclinical evaluation of "
				+ "dendritic cell subsets for promotion of transplant "
				+ "tolerance in the nonhuman primate.";

		String input1 = "CONCLUSIONS: The high success rate of this treatment supports the tentative "
				+ "diagnosis of oviducal obstruction in these mares and indicates that blockage of the mare's oviducts "
				+ "may occur in the form of a moveable accumulation of debris rather than "
				+ "from permanent fibrous adhesions resulting from salpingitis.";
		
		String input2 = "Concomitant fractures of the acetabulum and spine: a retrospective "
				+ "review of over 300 patients.";
		
		String input3 = "METHODS: From June 1995 to December 2003, 8 patients with Treacher "
				+ "Collins syndrome underwent microsurgical correction of facial contour using 16 free flaps.";		
		
		// call remote tagger, print to STOUT
		
		//taggerTagMe(input0);
		//taggerTagMe(input1);
		//taggerTagMe(input2);
		//taggerTagMe(input3);		
		
		// call remote tagger, output JSON
		//taggerTagMe(input,0);
	    
  	}
	
}
