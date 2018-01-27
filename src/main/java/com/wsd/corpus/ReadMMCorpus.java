package com.wsd.corpus;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.wsd.mmap.SparqlMM;
import com.wsd.util.Pair;



/**
 * class for retrieving sense annotations from
 * the MetaMap/SemRep gold corpus
 * 
 * @author Camilo Thorne
 *
 */
public class ReadMMCorpus {
	
	
    // annotations
    public static final String RESULTS = "results/annotations-wsd.json";
    // corpus
    public static final String CORPUS = "results/adjudicated.xml"; 

	
	/**
	 * constructor - runs the extraction process
	 * 
	 */
	public ReadMMCorpus() {
		readCorpus(CORPUS);
	}

	
	/**
	 * constructor - runs the extraction process
	 * (for tests)
	 * 
	 */
	public ReadMMCorpus(String corpus) {
		readCorpus(corpus);
	}	
	

	/**
	 * method for reading and parsing
	 * the MetaMap corpus XML file
	 * 
	 * @param filepath
	 */
	public void readCorpus(String filepath) {			
		
		// read the file
	    try {    	
	    	
	    	// read XML file
	    	File fXmlFile = new File(filepath);
	    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    	Document doc = dBuilder.parse(fXmlFile);	 
	    	// optional, but recommended
	    	doc.getDocumentElement().normalize();
    		
	    	// get sentences
	    	NodeList nList = doc.getElementsByTagName("Sentence");
	    	// count sentences
	    	int count = 0;
	    	
	    	// scan sentences
	    	for (int temp = 0; temp < nList.getLength(); temp++) {
	    		
	    		// recover descendants
	    		Node nNode = nList.item(temp);
	    		
    			// if the node is not empty (has annotations), we
    			// proceed with the processing
	    		if (!checkEmpty(nNode)){
	    			
	    			// get sentence
	    			String sen = ((Element) nNode).getAttribute("text");
	    			// explore the sentence annotations
	    			List<Node> desc = getAllDescendants(nNode);
	    			
    				// sense of subject NP
    				String sense1 = "";
    				// CUI of subject NP
    				String cui1 = "";   				
    				// phrase
    				String nptext1 = "";
    				// sense of object NP
    				String sense2 = "";
    				// CUI of object NP
    				String cui2 = "";      				
    				// phrase
    				String nptext2 = "";

	    			for (Node n : desc){
	    				// recover CUI
	    				if (n.getNodeName() == "CUI" && n.getParentNode().getNodeName() == "Subject"){
	    					Element eElement = (Element) n;
	    					cui1 = eElement.getTextContent(); 
	    					
	    					//System.out.println(cui1);
	    					
	    				}		    				
	    				// recover sense of subject
	    				if (n.getNodeName() == "PreferredName" && n.getParentNode().getNodeName() == "Subject"){
	    					Element eElement = (Element) n;
	    					sense1 = eElement.getTextContent(); 
	    				}    				
	    				// recover subject text
	    				if (n.getNodeName() == "Subject"){
	    					Element eElement = (Element) n;    					
	    					nptext1 = eElement.getAttribute("text");
	    				}	
	    				// recover CUI
	    				if (n.getNodeName() == "CUI" && n.getParentNode().getNodeName() == "Object"){
	    					Element eElement = (Element) n;    					
	    					cui2 = eElement.getTextContent();  
	    					
	    					//System.out.println(cui2);
	    					
	    				}	 	    				
    					// recover sense of object
	    				if (n.getNodeName() == "PreferredName" && n.getParentNode().getNodeName() == "Object"){
	    					Element eElement = (Element) n;    					
	    					sense2 = eElement.getTextContent();    	    			
	    				}		    				
	    				// recover object text
	    				if (n.getNodeName() == "Object"){
	    					Element eElement = (Element) n;    					
	    					nptext2 = eElement.getAttribute("text");
		    			}
	    			}
	    			
					System.out.println(cui1);
					System.out.println(cui2);
	    			
	    			// recover DBpedia/MeSH mappings
	    			Pair<String,String> res1 = SparqlMM.askGloss(cui1);
	    			Pair<String,String> res2 = SparqlMM.askGloss(cui2);
	    			
	    			// create list of mappings
	    			Map<String,String> 	ann1 = new HashMap<String,String>();
	    			ann1.put("phrase", 	nptext1);
	    			ann1.put("sense",  	sense1);
	    			ann1.put("cui",  	cui1);
	    			ann1.put("dbid",  	res1.getLeft());		    			
	    			ann1.put("gloss",  	res1.getRight());	
	    			
	    			Map<String,String> 	ann2 = new HashMap<String,String>();
	    			ann2.put("phrase", 	nptext2);
	    			ann2.put("sense",  	sense2);   
	    			ann2.put("cui",  	cui2);	
	    			ann2.put("dbid",  	res2.getLeft());	    			
	    			ann2.put("gloss",  	res2.getRight());	
	    			
	    			List<Map> annos 	= new ArrayList<Map>();
	    			annos.add(ann2);
	    			annos.add(ann1);
	    			
	    			// save data
	    			writeJSON(annos,sen,count);
	    			
	    			// increment count
	    			count = count + 1;
	    			
	    			System.out.println(count);
	    			
	    		}	
	    	}    	
	    }     
	    // catch IO exception
	    catch (Exception e) {
	    		e.printStackTrace();
	    }  
	}
	
	
	/**
	 * writing the gold standard sense annotations
	 * in the JSON file
	 * 
	 * @param annotations
	 * @param inputText
	 * @param num
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public void writeJSON(List<Map> annotations, String inputText, int num) 
			throws Exception{
		
    	// load result file into JSON object    	
    	JSONTokener tokener = new JSONTokener(new FileReader(RESULTS));
      	JSONObject root = new JSONObject(tokener);  	    	

	    // set sentence text
    	Map<String,String> 	text = new HashMap<String, String>();
	    text.put("sentence", inputText);
	    	
    	// set NPs with annotations 
	    Map<String,List<Map>>  	anno = new HashMap<String, List<Map>>();
	    List<Map> anns = new LinkedList<Map>();  
	    
	    for (Map map : annotations){
    	    	Map<String,String> 	 ph = new HashMap<String, String>();
    	    	ph.put("phrase", 	 map.get("phrase").toString().toLowerCase());
    	    	ph.put("sense", 	 map.get("sense").toString().toLowerCase());
    	    	ph.put("cui", 	 	 map.get("cui").toString());
    	    	ph.put("gloss", 	 map.get("gloss").toString());   	    	
    	    	ph.put("dbid", 	 	 map.get("dbid").toString());   	    	
    	    	anns.add(ph);
	    }
    	anno.put("Corpus", anns);
    	    
    	// check if annotations already exist
    	if (root.isNull(""+num+"")){
    	    	
    		// wrap tags into JSON
    		JSONObject janno = new JSONObject(anno);
    		root.append(""+num+"", janno);
	    	    
    		// set/wrap sentence
    		JSONObject jsen = new JSONObject(text);
    		root.append(""+num+"", jsen);
	    	    
//    		// update file
//    		FileWriter file = new FileWriter(RESULTS);
//    		file.write(root.toString(5));
//    		file.flush();
//    		file.close();		    	    
	    	    
    	}else{
	    	    
    	    // wrap tags into JSON
	    	JSONObject janno = new JSONObject(anno);
	    	
//	    	// update file
	    	root.append(""+num+"", janno);
//	    	FileWriter file = new FileWriter(RESULTS);
//	    	file.write(root.toString(5));
//	    	file.flush();
//	    	file.close();

    	}
    	    
//    	}else{
//    		
//    		String data = EntityUtils.toString(entity);
//    	    Map jsonData = parser.parseJson(data);
//    	
//	    	// set sentence text
//    	    Map<String,String> 	text = new HashMap();
//	    	text.put("sentence", inputText);
//	    	Map<String,List<Map>>  	anno = new HashMap();
//	    	
//	    	// set empty annotations
//	    	List<Map> anns = new LinkedList();		       
//    	    anno.put("TagMe", anns);
//    	    
//    	    // check if annotations already exist
//    	    if (root.isNull(""+num+"")){
//    	    	
//    	    	// wrap tags into JSON
//	    	    JSONObject janno = new JSONObject(anno);
//	    	    root.append(""+num+"", janno);
//	    	    
//	    	    // set/wrap sentence
//	    	    JSONObject jsen = new JSONObject(text);
//	    	    root.append(""+num+"", jsen);
//	    	    
//	    	    // update file
//	    	    FileWriter file = new FileWriter(CORPUS);
//	    		file.write(root.toString(5));
//	    		file.flush();
//	    		file.close();		    	    
//	    	    
//    	    }else{
//	    	    
//    	    	// wrap tags into JSON
//	    	    JSONObject janno = new JSONObject(anno);
//	    	
//	    	    // update file
//	    	    root.append(""+num+"", janno);
//	    	    FileWriter file = new FileWriter(CORPUS);
//	    		file.write(root.toString(5));
//	    		file.flush();
//	    		file.close();
//
//    	    }
//    		
//    	}
    	
    	
		// update file
		FileWriter file = new FileWriter(RESULTS);
		file.write(root.toString(5));
		file.flush();
		file.close();	
		
		
	}
	
	
	/**
	 * checking for sentences with
	 * no children and no annotations
	 * 
	 * @param node
	 * 
	 * @return boolean
	 */
	public static Boolean checkEmpty(Node n){
		if (n.getChildNodes().getLength()>0){
			return false;
		}
		else{
			return true;
		}
	}
	
	/**
	 * looping recursively over the XML tree structure
	 * to collect *all* the descendants of a XML node n
	 * 
	 * @param  node
	 * 
	 * @return list_of_nodes
	 */
	public static List<Node> getAllDescendants(Node n){
		List<Node> res = new ArrayList<Node>();
		if (n.getChildNodes()==null){
			res.add(n);
			return res;
		}
		else{
			for (int temp = 0; temp < n.getChildNodes().getLength(); temp++) {
				Node m = n.getChildNodes().item(temp);
				res.addAll(getAllDescendants(m));
				res.add(m);
				}
			return res;
		}
	}
	
	
	/**
	 * main method
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		
		new ReadMMCorpus();
		
	}

}
