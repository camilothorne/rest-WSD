package com.wsd.mmap;


//Java
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;



import java.util.Map;










// commons
import org.apache.commons.io.output.TeeOutputStream;
import org.json.JSONObject;
import org.json.JSONTokener;











import com.wsd.util.Norm;

import com.wsd.util.Pair;

// NLM java APIs
import gov.nih.nlm.nls.metamap.*;



/**
 * MetaMap annotator
 * 
 * @author camilo
 *
 */
public class MMAnnotator{
	
	
		  // uncomment to send annotation stout to file
//		  private static FileOutputStream fos;
//		  private static TeeOutputStream myOut;
//		  private static PrintStream ps;
	
	
	  	  // annotations
	  	  public static final String CORPUS = "results/annotations-wsd.json";

	
		  // MetaMap interface
		  public static MetaMapApi api;
		  
		  
		  // MetaMap options
		  public static final String[] opts = {
			  	"--all_derivational_variants", "--word_sense_disambiguation",
		   		"--ignore_stop_phrases", "--show_cuis", "--show_candidates"};
		  
		  
		  /**
		   * Generic MetaMap instantiator
		   */
		  public MMAnnotator() {
			  api = new MetaMapApiImpl();
		  }
	
	
		  /**
		   * Instantiates a new MetaMap interface using specified host and port
		   * 
		   * @param serverHostname
		   * @param serverPort
		   * 
		   * @throws Exception
		   */
		  public MMAnnotator(String serverHostname, int serverPort) throws Exception {
			  api = new MetaMapApiImpl();
			  api.setHost(serverHostname);
			  api.setPort(serverPort);
		  }		  
		  
		  
		  /**
		   * Instantiates a new MetaMap interface using specified host and port and annotates file
		   * 
		   * @param serverHostname
		   * @param serverPort
		   * @param filepath
		   * 
		   * @throws Exception
		   */
		  public MMAnnotator(String serverHostname, int serverPort, String filepath) throws Exception {
			  api = new MetaMapApiImpl();
			  api.setHost(serverHostname);
			  api.setPort(serverPort);
		  }
		  
		  
		  /**
		   * timeout
		   * 
		   * @param interval
		   */
		  // Timout method
		  public static void setTimeout(int interval) {
			  api.setTimeout(interval);
		  }
		  
		  /**
		   * set options
		   * 
		   * @return options
		   */
		  public ArrayList<String> setOptions(){
			  ArrayList<String> res = new ArrayList<String>();
			  for (int i=0;i<opts.length;i++){
				  res.add(opts[i]);
			  }
			  return res;
		  }
		  
		  
		  /**
		   * Converting file to list of sentences (one per line, 
		   * otherwise, use sentence segmentator)
		   * 
		   * @param filepath
		   * @param file
		   * 
		   * @return
		   */
		  @SuppressWarnings("resource")
		  public ArrayList<String> readFile(String filepath, BufferedReader file){
			   ArrayList<String> result = new ArrayList<String>();
			   try {
					String sCurrentLine;
					file = new BufferedReader(new FileReader(filepath));
					while ((sCurrentLine = file.readLine()) != null) {
						System.out.println(sCurrentLine);
						result.add(sCurrentLine);
					}	 
				} 
			   catch (IOException ex) {
					ex.printStackTrace();
				} 
			   finally {
					try {
						if (file != null)
							file.close();
					} 
					catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			   return result;
		  }
		  

		  /**
		   * Process terms using MetaMap API and display result to standard output.
		   * 
		   * @param terms
		   * @param out
		   * @param serverOptions
		   * 
		   * @throws Exception
		   */
		  public void process(String terms, PrintStream out, List<String> serverOptions) 
			throws Exception{
	  		//Set server options
			if (serverOptions.size() > 0) {
				 api.setOptions(serverOptions);
		    }
			
			// uncomment to send stout to file
//			fos 	= 	new FileOutputStream("/home/camilo/metamap.txt");
//			myOut	=	new TeeOutputStream(System.out, fos);
//			ps 		= 	new PrintStream(myOut,true,"UTF-8");
//			System.setOut(ps);
			
    	    // set NPs with annotations 
	    	Map<String,List<Map>>  	anno = new HashMap();
	    	List<Map> anns = new LinkedList();
			
		    //Retrieve the results
		    List<Result> resultList = api.processCitationsFromString(terms);
		    for (Result result: resultList) {
		      if (result != null) {
		    	System.out.println("============================================================");
		    	System.out.println("SENT: " + result.getInputText());
		    	System.out.println("============================================================");
		    	
			    out.println("------------------------------------------------------------");
				//Retrieve complex utterances
				for (Utterance utterance: result.getUtteranceList()) {
				  int mapp = 0;
				  for (PCM pcm: utterance.getPCMList()) {
				    for (Mapping map: pcm.getMappingList()) {
				    	mapp = mapp + 1;
				    	for (Ev mapEv: map.getEvList()) {
				    		System.out.println("\tScore: " 				+ (-mapEv.getScore()));
				    		System.out.println("\tMatched Words: " 		+ mapEv.getMatchedWords());
				    		System.out.println("\tConcept Name: " 		+ mapEv.getConceptName());
				    		System.out.println("\tCUI: " 				+ mapEv.getConceptId());
				    		//System.out.println("\tGloss: " 				+ SparqlMM.askGloss(mapEv.getConceptId()));				    		
				    		System.out.println("\tPreferred Name: " 	+ mapEv.getPreferredName());
				    		System.out.println("\tSemantic Types: " 	+ recoverSemType(mapEv.getSemanticTypes().toString(),0));
				    		System.out.println("\tSources: " 			+ mapEv.getSources().toString()); 		
				    		//out.println();
				    		System.out.println("------------------------------------------------------------");
					   }
					}
				  }
				}
		      } 
		      else{
			   out.println("NULL result instance! ");
		      }
		    }
		  }	
		  
		  
		  /**
		   * Process terms using MetaMap API and save result on JSON file.
		   * 
		   * @param terms
		   * @param out
		   * @param serverOptions
		   * @param jsonobject
		   * 
		   * @throws Exception
		   */
		  @SuppressWarnings({ "rawtypes", "unchecked" })
		public void process(String terms, PrintStream out, List<String> serverOptions,
				  JSONObject root, int num) 
			throws Exception{
			  
	  		//Set server options
			if (serverOptions.size() > 0) {
				 api.setOptions(serverOptions);
		    }
			
			// uncomment to send stout to file
//			fos 	= 	new FileOutputStream("/home/camilo/metamap.txt");
//			myOut	=	new TeeOutputStream(System.out, fos);
//			ps 		= 	new PrintStream(myOut,true,"UTF-8");
//			System.setOut(ps);
			
			// load normalizer
			Norm norm = new Norm();
			
    	    // set NPs with annotations 
	    	Map<String,List<Map>>  	anno = new HashMap();
	    	List<Map> anns = new LinkedList();
			
		    //Retrieve the results
		    List<Result> resultList = api.processCitationsFromString(terms);
		    for (Result result: resultList) {
		      if (result != null) {
		    	
		    	//System.out.println("============================================================");
		    	//System.out.println("SENT: " + result.getInputText());
		    	//System.out.println("============================================================");
			    //out.println("------------------------------------------------------------");
				
			    //Retrieve complex utterances
				for (Utterance utterance: result.getUtteranceList()) {
					
				  int mapp = 0;
				  for (PCM pcm: utterance.getPCMList()) {
				    for (Mapping map: pcm.getMappingList()) {
				    	mapp = mapp + 1;
				    	
				    	for (Ev mapEv: map.getEvList()) {
				    		
				    		// save annotations on JSON file
							String np = norm.normMM(mapEv.getMatchedWords().toString().trim());
							String sense = mapEv.getConceptName().trim();
							String mmid  = mapEv.getConceptId();
							
							System.out.println(mmid);
							
			    			// recover DBpedia/MeSH mappings
			    			Pair<String,String>  res = SparqlMM.askGloss(mmid);						
			    			// score
							float  score = (((float)(-mapEv.getScore()))/1000);				
							Map<String,String> 	 ph = new HashMap();
							ph.put("phrase", 	 np);
							ph.put("sense", 	 sense.toLowerCase());
							ph.put("cui" , 	 	 mmid);
							ph.put("gloss",      res.getRight());
							ph.put("dbid",       res.getLeft());							
							ph.put("confidence", ""+score+"");
							anns.add(ph);
				    		
					   }
				    	
						// set annotations
			    	    anno.put("MetaMap", anns);
				    	
					}
				  }
				}
				
    	    	// wrap tags into JSON
	    	    JSONObject janno = new JSONObject(anno);
	    	
	    	    // update file
	    	    root.append(""+num+"", janno);
	    	    FileWriter file = new FileWriter(CORPUS);
	    		file.write(root.toString(5));
	    		file.flush();
	    		file.close();
				
		      }else{
			   out.println("NULL result instance! ");
		      }
		    }
		  }		  
		   
		   
		  /**
		   * tag multiple sentences
		   * 
		   * @param samples
		   * 
		   * @throws Exception
		   */
		  @SuppressWarnings({ "rawtypes", "unchecked" })
		  public void processLines(List<String> samples) throws Exception{			   
			   
			   int num = 0; // counter
			   
			   PrintStream myoutput = System.out; // print to stout			   		   
			   ArrayList<String> myoptions = setOptions(); // initialize options
			   
			   // load result file into JSON object    	
		       JSONTokener tokener = new JSONTokener(new FileReader(CORPUS));
	           JSONObject root = new JSONObject(tokener);
			   
	           // process strings 
			   for (String samp: samples){
				   
			       	// set sentence text
			   	    Map<String,String> 	text = new HashMap();
			       	text.put("sentence", samp);
				   
		    	    // check if annotations already exist
		    	    if (root.isNull(""+num+"")){
			    	    
			    	    // set/wrap sentence
			    	    JSONObject jsen = new JSONObject(text);
			    	    root.append(""+num+"", jsen);
				   
			    	    // annotate NPs, NNs and verbs with MetaMap, save to JSON
			    	    process(samp,myoutput,myoptions,root,num);
			    	    
		    	    }else{
		    	    	
			    	    // annotate NPs, NNs and verbs with MetaMap, save to JSON
			    	    process(samp,myoutput,myoptions,root,num);	    	    	
		    	    	
		    	    }
				   
		    	    num = num + 1;// update counter
		    	    
			   }			   
		  }
		   
		  
		  /**
		   * tag one sentence
		   * 
		   * @param sample
		   * 
		   * @throws Exception
		   */
		  public void processLine(String sample) throws Exception{
			   PrintStream myoutput = System.out; // print to stout			   		   
			   List<String> myoptions = setOptions(); // initialize options	   			   
			   // annotate NPs, NNs and verbs with MetaMap
			   process(sample,myoutput,myoptions);
		  }
		   		  
		  
		  /**
		   * expand CUI abbreviations (if needed)
		   * 
		   * @param type
		   * @param typ
		   * 
		   * @return semantic type
		   */
		  public String recoverSemType(String type, int typ){
			  Abbrev abb = new Abbrev();
			  if (typ == 1){
				  return abb.mySimpLabel(type);
				  }
			  if (typ == 2){
				  return abb.myLabel(type);
			  }else{
				  return abb.myFilter(type);
			  }
		  }
		  
		  
		  /**
		   * main method (for tests)
		   * @param args
		   * 
		   * @throws Exception 
		   */
		  public static void main(String[] args) throws Exception{
			  
			  // MetaMap parameters
			  String serverhost = MetaMapApi.DEFAULT_SERVER_HOST; // default host
			  int serverport = MetaMapApi.DEFAULT_SERVER_PORT; 	// default port
			  int timeout = -1; // use default timeout		
			  
			  // connect to service
			  MMAnnotator ann = new MMAnnotator(serverhost,serverport);
			  
			  // examples
			  String inputText1 = "BabelNet is both a multilingual encyclopedic "
			  			+ "dictionary and a semantic network.";
			  String inputText2 = "Strategies for preclinical evaluation of "
						+ "dendritic cell subsets for promotion of transplant tolerance "
						+ "in the nonhuman primate.";
			  
			  // print to stout
			  ann.processLine(inputText1);
			  ann.processLine(inputText2);
			  
			  // print to JSON
			  //ann.processLines((List<String>) Arrays.asList(new String[]{inputText1,inputText2}));
			  
			  // timeout
			  if (timeout > -1) {
			    setTimeout(timeout);
			  } 
			  
		  }

}