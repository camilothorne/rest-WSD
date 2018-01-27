package com.wsd.babelfly;


// Java
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
// import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;








// commons
import org.apache.commons.io.output.TeeOutputStream;



import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;








// BabelFly
//import it.uniroma1.lcl.babelfy.commons.BabelfyConstraints;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.MCS;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.PosTaggingOptions;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.ScoredCandidates;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.SemanticAnnotationResource;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.SemanticAnnotationType;
import it.uniroma1.lcl.babelfy.commons.annotation.SemanticAnnotation;
//import it.uniroma1.lcl.babelfy.commons.annotation.SemanticAnnotation.Source;
//import it.uniroma1.lcl.babelfy.commons.annotation.TokenOffsetFragment;
import it.uniroma1.lcl.babelfy.core.Babelfy;
import it.uniroma1.lcl.jlt.util.Language;











import com.wsd.babelnet.BNRest;
// BabelNet (SPARQL)
import com.wsd.babelnet.SparqlBN;
import com.wsd.babelnet.UrlBN;
import com.wsd.util.Norm;


/**
 * 
 * @author camilo
 *
 */
public class BFlyTagger {
	
	
	// annotations
	public static final String CORPUS = "results/annotations-wsd.json";
	
	// to redirect stout to file
	private static FileOutputStream fos;
	private static TeeOutputStream myOut;
	private static PrintStream ps;
	
	
	/**
	 * tagger method
	 * 
	 * @param inputText
	 * @param lang
	 * @throws Exception
	 */
	public static void taggerFly(String inputText, Language lang) throws Exception{
		
		// send stout to file
		fos 	= 	new FileOutputStream("/home/camilo/babelfly.txt");
		myOut	=	new TeeOutputStream(System.out, fos);
		ps 		= 	new PrintStream(myOut,true,"UTF-8");
		System.setOut(ps);
		
		// synset to retrieve per annotation
		// String synsetURI = null;
		String synsetID = null;
		
		// configuration
		BabelfyParameters bp = new BabelfyParameters();
		
		// parameters
		bp.setAnnotationResource(SemanticAnnotationResource.BN);
		bp.setMCS(MCS.ON_WITH_STOPWORDS);
		bp.setScoredCandidates(ScoredCandidates.ALL);
		bp.setAnnotationType(SemanticAnnotationType.CONCEPTS);
		bp.setPoStaggingOptions(PosTaggingOptions.STANDARD);
		bp.setMultiTokenExpression(true);
		bp.setDensestSubgraph(true);
		
		// BabelFly instance
		Babelfy bfy = new Babelfy(bp);
		List<SemanticAnnotation> bfyAnnotations = bfy.babelfy(inputText, lang);
		
		System.out.println("\n-------------------------------------------------------");
		System.out.println(inputText);
		System.out.println("-------------------------------------------------------\n");
		
		// bfyAnnotations is the result of Babelfy.babelfy() call
		for (SemanticAnnotation annotation : bfyAnnotations){
	
		    System.out.println("\t ==============================");
			
		    // splitting the input text using the CharOffsetFragment start and end anchors
		    String frag = inputText.substring(annotation.getCharOffsetFragment().getStart(),
		        annotation.getCharOffsetFragment().getEnd() + 1);
			
		    // displaying annotations
		    System.out.println("\t NP: " 			+ frag);
		    System.out.println("\t ==============================");
		    //System.out.println("\t BN (RDF): " 	+ annotation.getBabelNetURL());
		    
		    // annotations
		    System.out.println("\t BN: " 		+ annotation.getBabelSynsetID());
		    System.out.println("\t DBpedia: " 	+ annotation.getDBpediaURL());
		    System.out.println("\t source: " 	+ annotation.getSource());
		    System.out.println("\t coh score: " + annotation.getCoherenceScore());
		    System.out.println("\t dis score: " + annotation.getScore());
		    System.out.println("\t rel score: " + annotation.getGlobalScore());	    
		    
		    // querying BabelNet for senses and lemmas 
		    
//		    i. via Sparql endpoint
//		    synsetURI =  annotation.getBabelNetURL();
//		    SparqlBN.askSenses(synsetURI);
		    
//		    ii. via RESTful service
//		    synsetID =  annotation.getBabelSynsetID();
//			System.out.println("\t ++++++++++++++++++++++++++++++");
//		    UrlBN.askSenses(synsetID, "en");
//			System.out.println("\t ++++++++++++++++++++++++++++++");		    
//			UrlBN.askSenses(synsetID, "es");
//			System.out.println("\t ++++++++++++++++++++++++++++++");		    
//			UrlBN.askSenses(synsetID, "de");
//			System.out.println("\t ++++++++++++++++++++++++++++++");		    
		    
		}
		
		System.out.println("\t ==============================");
		
	}
	
	
	/**
	 * tagger method
	 * 
	 * @param inputText
	 * @param lang
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void taggerFly(String inputText, Language lang, int num) throws Exception{
		
		// configuration
		BabelfyParameters bp = new BabelfyParameters();
		
		// parameters
		bp.setAnnotationResource(SemanticAnnotationResource.BN);
		bp.setMCS(MCS.ON_WITH_STOPWORDS);
		bp.setScoredCandidates(ScoredCandidates.ALL);
		bp.setAnnotationType(SemanticAnnotationType.CONCEPTS);
		bp.setPoStaggingOptions(PosTaggingOptions.STANDARD);
		bp.setMultiTokenExpression(true);
		bp.setDensestSubgraph(true);
		
		// BabelFly instance
		Babelfy bfy = new Babelfy(bp);
		List<SemanticAnnotation> bfyAnnotations = bfy.babelfy(inputText, lang);
		
		// load normalizer
		Norm norm = new Norm();
		
    	// load result file into JSON object    	
    	JSONTokener tokener = new JSONTokener(new FileReader(CORPUS));
      	JSONObject root = new JSONObject(tokener);
      	
    	// set sentence text
	    Map<String,String> 	text = new HashMap();
    	text.put("sentence", inputText);
    	
	    // set NPs with annotations 
    	Map<String,List<Map>>  	anno = new HashMap();
    	List<Map> anns = new LinkedList();
		
		// bfyAnnotations is the result of Babelfy.babelfy() call
		for (SemanticAnnotation annotation : bfyAnnotations){
			
		    // splitting the input text using the CharOffsetFragment start and end anchors
		    String frag = inputText.substring(annotation.getCharOffsetFragment().getStart(),
		        annotation.getCharOffsetFragment().getEnd() + 1);
			
		    // annotations
		    String np 			= frag;
		    double score	 	= annotation.getCoherenceScore();
		    
		    // remote
		    // String sense 		= norm.normBN(BNRest.askSense(annotation.getBabelSynsetID(), "EN"));	

		    // local
		    String sense 		= UrlBN.askSense(annotation.getBabelSynsetID(), "en")[0];
		    String wntid 		= UrlBN.askSense(annotation.getBabelSynsetID(), "en")[1];				    
		    String gloss 		= UrlBN.askSense(annotation.getBabelSynsetID(), "en")[2];
			String dbpedia 		= annotation.getDBpediaURL();		    
		    
			Map<String,String> 	 ph = new HashMap();
			ph.put("phrase", 	 np);
			ph.put("sense", 	 sense);
			ph.put("gloss", 	 gloss);
			ph.put("bnid", 		 annotation.getBabelSynsetID());
			
			if(wntid == null){
				ph.put("wnid", 		 "NaN");
			}else{
				ph.put("wnid", 		 wntid);
			}
			
			if(dbpedia == null){
				//dbpedia = SparqlBN.getDBpedia(annotation.getBabelSynsetID());
				ph.put("dbid",   "NaN");	
			}else{
				ph.put("dbid",       annotation.getDBpediaURL());
			}
			
			ph.put("confidence", ""+score+"");
			anns.add(ph);
		    
		}
		
		// set annotations
	    anno.put("BabelFly", anns);
	    
	    // check if annotations already exist
	    if (root.isNull(""+num+"")){
	    	
	    	// wrap tags into JSON
    	    JSONObject janno = new JSONObject(anno);
    	    root.append(""+num+"", janno);
    	    
    	    // set/wrap sentence
    	    JSONObject jsen = new JSONObject(text);
    	    root.append(""+num+"", jsen);
    	    
    	    // update file
    	    FileWriter file = new FileWriter(CORPUS);
    		file.write(root.toString(5));
    		file.flush();
    		file.close();		    	    
    	    
	    }else{
	    	
	    	JSONArray arr = (JSONArray) root.get(""+num+"");
	    	
	    	if(arr.length()<5){
	    		
	    		// wrap tags into JSON
	    		JSONObject janno = new JSONObject(anno);
    	
	    		// update file
	    		root.append(""+num+"", janno);
	    		FileWriter file = new FileWriter(CORPUS);
	    		file.write(root.toString(5));
	    		file.flush();
	    		file.close();
	    		}

	    	}
		
	}
	
	
	/**
	 * main method
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		
		// input
		String inputText1 = "BabelNet is both a multilingual encyclopedic dictionary and a semantic network";
		String inputText2 = "Strategies for preclinical evaluation of "
				+ "dendritic cell subsets for promotion of transplant tolerance in the nonhuman primate.";
		
		// tagger
		try {
			taggerFly(inputText2, Language.EN);
			taggerFly(inputText1, Language.EN);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
