package com.wsd.babelnet;

import com.jakewharton.fliptables.FlipTable;
import com.wsd.util.Pair;

import java.util.ArrayList;
import java.util.List;


//import org.apache.jena.atlas.web.auth.HttpAuthenticator;
//import org.apache.jena.atlas.web.auth.SimpleAuthenticator;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;


/**
* SPARQL JENA BabelNet interface (generic)
* 
* @author Camilo Thorne
* 
*/
public class SparqlBN{
		
		
	// dataset name
	private static final String graphURI 	 = "<http://babelnet.org/rdf/>";

	
	// schema prefixes
	private static final String lemon		 = "<http://www.lemon-model.net/lemon#>";	
	private static final String babelnet 	 = "<http://babelnet.org/model/babelnet#>";
	private static final String skos 		 = "<http://www.w3.org/2009/08/skos-reference/skos.html#>";
	private static final String lexinfo 	 = "<http://www.lexinfo.net/ontology/2.0/lexinfo#>";
	private static final String rdfschema 	 = "<http://www.w3.org/2000/01/rdf-schema#>";
	private static final String dublin_core  = "<http://dublincore.org/documents/2012/06/14/dcmi-terms/?v=elements#>";
	private static final String dublin_terms = "<http://dublincore.org/documents/2012/06/14/dcmi-terms/?v=terms#>";
	
	
	// API key (if needed)
	private static final String apikey 		 = "51bde060-bd99-49e0-a78f-5cc4e9cd8add";
	
	
	// virtuoso BabelNet 3.5 URI
	private static final String BabelNetURI  = "http://babelnet.org/sparql";
	
	
	/**
	 * stub constructor
	 */
	public SparqlBN(){}
	
	

	/**
	 * check if query is empty
	 * 
	 * @param queryString
	 * @return
	 * @throws Exception
	 */
	public static boolean isEmpty (String queryString) throws Exception{
		
		// value to return
		boolean res;
		
	    // create query
		Query query=QueryFactory.create(queryString);
		QueryExecution queryExec = QueryExecutionFactory.
				sparqlService(BabelNetURI, query);

		try {

			// execute query
			ResultSet rs = queryExec.execSelect();

		    if (!rs.hasNext()){
		    	res= true;
		    }else{
		    	res= false;
		    }

		} catch (Throwable e) {
			// throw exception
			throw new Exception(
					"Couldn't execute SPARQL query " + query.serialize(), e);
		} finally {
			// close
			queryExec.close();
		}
		
	    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
	    System.out.println(queryString);
	    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");	
	    if (res==true){
	    	System.out.println("empty query!");
	    }else{
	    	System.out.println("non-empty query!");
	    }
	    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");	    
		
	    return res;
		
	}
	

    /**
     * pretty print n-D result set as a (string) table 
     * 
     * @param queryString
     */
	public static String prettyPrint (String queryString) 
			throws Exception{
		
	    ResultSet result;
	    int count;
	    ArrayList<String[]> myres;
	    String[] metadata;
	    String[][] myres2;
	    
	    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
	    System.out.println(queryString);
	    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
	    
	    // create query
		Query query=QueryFactory.create(queryString);
		
		//HttpAuthenticator authenticator = new SimpleAuthenticator("camilothorne", apikey.toCharArray());
		
		QueryExecution queryExec = QueryExecutionFactory.
				createServiceRequest(BabelNetURI, query);
		
		try {
		
			// get result set
			result = queryExec.execSelect();
			
		    // store results
		    myres = new ArrayList<String[]>();
		    
		    // get result set metadata
		    List<String> mdata = result.getResultVars();  
		    metadata = new String[mdata.size()];
		    mdata.toArray(metadata);
		    count = mdata.size();
	    
		    // loop over result set
		    while (result.hasNext()){
		    	String[] data = new String[count];
	    		QuerySolution qs = result.next();
		    	for (int c = 0; c < count; c++){		    		
		    		if (!qs.contains(metadata[c])){
		    			data[c] = "NaN";
		    		}
		    		else{
		    			data[c] = qs.get(metadata[c]).toString();
		    		}
		    	}
		    	myres.add(data);
		    }
	    
		} catch (Exception e) {
			// throw exception
			throw new Exception(
					"Couldn't execute SPARQL query \n\n" + query.serialize(), e);
		} finally {
			// close
			queryExec.close();
		}
	    
	    // pretty print result set as table
	    myres2 = myres.toArray(new String[myres.size()][count]);
	    return FlipTable.of(metadata,myres2);
	    
	}	
	
    
    /**
     * pretty print n-D result set as a (string) table 
     * 
     * @param queryString
     * @param limit
     */
	public static String prettyPrint (String queryString, int limit) 
			throws Exception{
		
	    ResultSet result;
	    int count;
	    ArrayList<String[]> myres;
	    String[] metadata;
	    String[][] myres2;
	    
	    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
	    System.out.println(queryString);
	    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
	    
	    // create query
		Query query=QueryFactory.create(queryString);
		query.setLimit(limit);
		
		//HttpAuthenticator authenticator = new SimpleAuthenticator("camilothorne", apikey.toCharArray());
		
		QueryExecution queryExec = QueryExecutionFactory.
				createServiceRequest(BabelNetURI, query);
		
		try {
		
			// get result set
			result = queryExec.execSelect();
			
		    // store results
		    myres = new ArrayList<String[]>();
		    
		    // get result set metadata
		    List<String> mdata = result.getResultVars();  
		    metadata = new String[mdata.size()];
		    mdata.toArray(metadata);
		    count = mdata.size();
	    
		    // loop over result set
		    while (result.hasNext()){
		    	String[] data = new String[count];
	    		QuerySolution qs = result.next();
		    	for (int c = 0; c < count; c++){		    		
		    		if (!qs.contains(metadata[c])){
		    			data[c] = "NaN";
		    		}
		    		else{
		    			data[c] = qs.get(metadata[c]).toString();
		    		}
		    	}
		    	myres.add(data);
		    }
	    
		} catch (Exception e) {
			// throw exception
			throw new Exception(
					"Couldn't execute SPARQL query \n\n" + query.serialize(), e);
		} finally {
			// close
			queryExec.close();
		}
	    
	    // pretty print result set as table
	    myres2 = myres.toArray(new String[myres.size()][count]);
	    return FlipTable.of(metadata,myres2);
	    
	}
	
	
	/**
	 * 
	 * @param synsetURI
	 * @throws Exception
	 */
	public static void askSenses(String synsetURI) throws Exception{
		
		// sample query 2 (return all multilingual (eq) senses for "dentritic cells" lemma's synset)
		String queryString2 = 
				  "PREFIX lemon: " 	+ lemon + " "
				+ "PREFIX rdfs: " 	+ rdfschema + " " 
				+ "PREFIX lexinfo:" + lexinfo + " "
				+ "SELECT DISTINCT ?lexicon ?lang ?label ?synset "
				+ "FROM " + graphURI + " "		  
				+ "WHERE "
				+ "{"
				+		"?lexicon a lemon:Lexicon . "
				+		"?lexicon lemon:language ?lang . "
				+		"?lexicon lemon:entry ?entry . "
				+	    "?entry a lemon:LexicalEntry . "
				+ 		"?entry rdfs:label ?label . "
				+	    "?entry lemon:sense ?sense . "
				+		"?sense a lemon:LexicalSense . "
				+	    "?sense lemon:reference ?synset . "
				+	    "?sense lemon:reference <" + synsetURI +"> . "
				+ "}";
		
		// print results
		System.out.println(prettyPrint(queryString2));
		
	}
	
	
	/**
	 * 
	 * @param synsetURI
	 * @param language
	 * @throws Exception
	 */
	public static void askSenses(String synsetURI, String language) throws Exception{
		
		// sample query 2 (return all multilingual (eq) senses for "dentritic cells" lemma's synset)
		String queryString2 = 
				  "PREFIX lemon: " 	+ lemon + " "
				+ "PREFIX rdfs: " 	+ rdfschema + " " 
				+ "PREFIX lexinfo:" + lexinfo + " "
				+ "SELECT DISTINCT ?lexicon ?lang ?entry ?label ?sense ?synset "
				+ "FROM " + graphURI + " "		  
				+ "WHERE "
				+ "{"
				+		"?lexicon a lemon:Lexicon . "
				+		"?lexicon lemon:language ?lang . "
				+		"?lexicon lemon:language '" + language + "' . "					
				+		"?lexicon lemon:entry ?entry . "
				+	    "?entry a lemon:LexicalEntry . "
				+ 		"?entry rdfs:label ?label . "
				+	    "?entry lemon:sense ?sense . "
				+		"?sense a lemon:LexicalSense . "
				+	    "?sense lemon:reference ?synset . "
				+	    "?sense lemon:reference <" + synsetURI +"> . "
				+ "}";
		
		// print results
		System.out.println(prettyPrint(queryString2));
		
	}	
	
	
	/**
	 * 
	 * @param synsetURI
	 * @param language
	 * @param lemma
	 * @throws Exception
	 */
	public static void askSenses(String synsetURI, String language, String lemma) throws Exception{
		
		// sample query 2 (return all multilingual (eq) senses for "dentritic cells" lemma's synset)
		String queryString2 = 
				  "PREFIX lemon: " 	+ lemon + " "
				+ "PREFIX rdfs: " 	+ rdfschema + " " 
				+ "PREFIX lexinfo:" + lexinfo + " "
				+ "SELECT DISTINCT ?lexicon ?lang ?entry ?label ?sense ?synset "
				+ "FROM " + graphURI + " "		  
				+ "WHERE "
				+ "{"
				+		"?lexicon a lemon:Lexicon . "
				+		"?lexicon lemon:language ?lang . "
				+		"?lexicon lemon:language '" + language + "' . "					
				+		"?lexicon lemon:entry ?entry . "
				+	    "?entry a lemon:LexicalEntry . "
				+ 		"?entry rdfs:label ?label . "
				+		"?entry rdfs:label " + lemma + " . "				
				+	    "?entry lemon:sense ?sense . "
				+		"?sense a lemon:LexicalSense . "
				+	    "?sense lemon:reference ?synset . "
				+	    "?sense lemon:reference <" + synsetURI +"> . "
				+ "}";
		
		// print results
		System.out.println(prettyPrint(queryString2));
		
	}	
	
	
	/**
	 * return the top (1st) DBpedia URI, given a BabelNet synset URI
	 * 
	 * @param synsetID
	 * @return DBpedia URI
	 * @throws Exception
	 */
	public static String getDBpedia(String synsetID) throws Exception{
		
		// init result, format synset URI
		String dbpedia = null;
		String synsetURI = "http://babelnet.org/rdf/" + synsetID.replace("bn:", "s");
	
		// build query
		String queryString = 
				  "PREFIX lemon: " 	+ lemon + " "
				+ "PREFIX rdfs: " 	+ rdfschema + " " 
				+ "PREFIX lexinfo:" + lexinfo + " "				
				+ "SELECT DISTINCT ?label ?sense "
				+ "FROM " + graphURI + " "		  
				+ "WHERE "
				+ "{"
				+		"?lexicon a lemon:Lexicon . "
				+		"?lexicon lemon:language 'EN' . "				
				+		"?lexicon lemon:entry ?entry . "
				+	    "?entry a lemon:LexicalEntry . "
				+ 		"?entry rdfs:label ?lab . "
				+	    "?entry lemon:sense ?sense . "
				+		"?sense a lemon:LexicalSense . "
				+	    "?sense lemon:reference <" + synsetURI + "> . "
				+       "BIND(STR(?lab) AS ?label ) . "				
				+ "}";		
		
		// evaluate, post-process answer
		Pair<String,String> ans = getField(queryString);
		String label = ans.getRight();
		char in = Character.toUpperCase(label.charAt(0));
		dbpedia = "http://dbpedia.org/resource/"+in+label.substring(1);
		
		return dbpedia;
		
	}
	
	
	
    /**
     * return top DBpedia link + sense of BabelNet synset
     * 
     * @param queryString
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Pair<String,String> getField (String queryString) 
			throws Exception{
		
		// store answer set
	    ResultSet result;
	    
	    // create query
		Query query=QueryFactory.create(queryString);
		query.setLimit(1);
		QueryExecution queryExec = QueryExecutionFactory.
				createServiceRequest(BabelNetURI, query);
		
		// return value
	    Pair<String,String> data = null;
	    String left = null;
	    String right = null;
		
		try {
		
			// get result set
			result = queryExec.execSelect();
		    
		    // loop over result set
		    while (result.hasNext()){
	    		QuerySolution qs = result.next();	    		
		    	if (!qs.contains("sense")){
		    		left = "";
		    	}
		    	else{
		    		left = qs.get("sense").toString();
		    	}
		    	if (!qs.contains("label")){
		    		right = "";
		    	}
		    	else{
		    		right = qs.get("label").toString();
		    	}		    	
		    	
		    }
		    
	    
		} catch (Exception e) {
			// throw exception
			throw new Exception(
					"Couldn't execute SPARQL query \n\n" + query.serialize(), e);
		} finally {
			// close
			queryExec.close();
		}
	    
		// return field
		data = new Pair(left,right);
		return data; 
	    
	}	
	
	
	
	// main class (for tests)
	public static void main(String[] args) throws Exception{
		
		
		// instantiate query engine
		//SparqlBN spar = new SparqlBN();
		
		
//		// sample query 1 (return the (Italian) sense and synset of the "cellule_dendritiche" lemma) 
//		String queryString = 
//				  "PREFIX lemon: " 	+ lemon + " "
//				+ "PREFIX rdfs: " 	+ rdfschema + " " 
//				+ "PREFIX lexinfo:" + lexinfo + " "
//				+ "SELECT DISTINCT ?lexicon ?lang ?entry ?label ?sense ?synset "
//				+ "FROM " + graphURI + " "		  
//				+ "WHERE "
//				+ "{"
//				+		"?lexicon a lemon:Lexicon . "
//				+		"?lexicon lemon:language ?lang . "
//				+		"?lexicon lemon:entry ?entry . "
//				+	    "?entry a lemon:LexicalEntry . "
//				+ 		"?entry rdfs:label ?label . "
//				+		"?entry rdfs:label 'cellule_dendritiche'@it . "
//				+	    "?entry lemon:sense ?sense . "
//				+		"?sense a lemon:LexicalSense . "
//				+	    "?sense lemon:reference ?synset . "
//				+ "}";
//		
//		// print results
//		System.out.println(spar.prettyPrint(queryString));
//		
//		
//		// sample query 2 (return all multilingual (eq) senses for "dentritic cells" lemma's synset)
//		String queryString2 = 
//				  "PREFIX lemon: " 	+ lemon + " "
//				+ "PREFIX rdfs: " 	+ rdfschema + " " 
//				+ "PREFIX lexinfo:" + lexinfo + " "
//				+ "SELECT DISTINCT ?lexicon ?lang ?entry ?label ?sense ?synset "
//				+ "FROM " + graphURI + " "		  
//				+ "WHERE "
//				+ "{"
//				+		"?lexicon a lemon:Lexicon . "
//				+		"?lexicon lemon:language ?lang . "
//				+		"?lexicon lemon:entry ?entry . "
//				+	    "?entry a lemon:LexicalEntry . "
//				+ 		"?entry rdfs:label ?label . "
//				+	    "?entry lemon:sense ?sense . "
//				+		"?sense a lemon:LexicalSense . "
//				+	    "?sense lemon:reference ?synset . "
//				+	    "?sense lemon:reference <http://babelnet.org/rdf/s00696672n> . "
//				+ "}";
//		
//		// print results
//		System.out.println(spar.prettyPrint(queryString2));
		
		
//		// sample query 3 (return all English (eq) senses for "dentritic cells" lemma's synset)
//		String queryString3 = 
//				  "PREFIX lemon: " 	+ lemon + " "
//				+ "PREFIX rdfs: " 	+ rdfschema + " " 
//				+ "PREFIX lexinfo:" + lexinfo + " "
//				+ "SELECT DISTINCT ?lexicon ?lang ?entry ?label ?sense ?synset "
//				+ "FROM " + graphURI + " "		  
//				+ "WHERE "
//				+ "{"
//				+		"?lexicon a lemon:Lexicon . "
//				+		"?lexicon lemon:language ?lang . "
//				+		"?lexicon lemon:language 'EN' . "				
//				+		"?lexicon lemon:entry ?entry . "
//				+	    "?entry a lemon:LexicalEntry . "
//				+ 		"?entry rdfs:label ?label . "
//				+	    "?entry lemon:sense ?sense . "
//				+		"?sense a lemon:LexicalSense . "
//				+	    "?sense lemon:reference ?synset . "
//				+	    "?sense lemon:reference <http://babelnet.org/rdf/s00696672n> . "
//				+ "}";
//		
//		// print results
//		System.out.println(SparqlBN.prettyPrint(queryString3,10));
		
		
		// sample query 4 (return top 10 English senses --lemmas-- for "car" and DBpedia links)
		String queryString4 = 
				  "PREFIX lemon: " 	+ lemon + " "
				+ "PREFIX rdfs: " 	+ rdfschema + " " 
				+ "PREFIX lexinfo:" + lexinfo + " "				
				+ "SELECT DISTINCT ?label ?sense "
				+ "FROM " + graphURI + " "		  
				+ "WHERE "
				+ "{"
				+		"?lexicon a lemon:Lexicon . "
				+		"?lexicon lemon:language 'EN' . "				
				+		"?lexicon lemon:entry ?entry . "
				+	    "?entry a lemon:LexicalEntry . "
				+ 		"?entry rdfs:label ?lab . "
				+	    "?entry lemon:sense ?sense . "
				+		"?sense a lemon:LexicalSense . "
				+	    "?sense lemon:reference <http://babelnet.org/rdf/s00015786n> . "
				+       "BIND(STR(?lab) AS ?label ) . "				
				+ "}";
		// print results
		System.out.println(SparqlBN.prettyPrint(queryString4,10));		
		
		
		// sample query 5 (return the top English sense --lemma-- for "car" and its top DBpedia link)
		String queryString5 = 
				  "PREFIX lemon: " 	+ lemon + " "
				+ "PREFIX rdfs: " 	+ rdfschema + " " 
				+ "PREFIX lexinfo:" + lexinfo + " "				
				+ "SELECT DISTINCT ?label ?sense "
				+ "FROM " + graphURI + " "		  
				+ "WHERE "
				+ "{"
				+		"?lexicon a lemon:Lexicon . "
				+		"?lexicon lemon:language 'EN' . "				
				+		"?lexicon lemon:entry ?entry . "
				+	    "?entry a lemon:LexicalEntry . "
				+ 		"?entry rdfs:label ?lab . "
				+	    "?entry lemon:sense ?sense . "
				+		"?sense a lemon:LexicalSense . "
				+	    "?sense lemon:reference <http://babelnet.org/rdf/s00015786n> . "
				+       "BIND(STR(?lab) AS ?label ) . "				
				+ "}";
		// print results
		System.out.println(SparqlBN.prettyPrint(queryString5,1));			
		
	}	
	
	
}


