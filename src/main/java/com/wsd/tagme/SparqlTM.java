package com.wsd.tagme;

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
* SPARQL JENA Ontotext interface (generic)
* 
* @author Camilo Thorne
* 
*/
public class SparqlTM{
		
			
	// schema prefixes
	private static final String dbo			 = "<http://dbpedia.org/ontology/>";
	private static final String rdfschema 	 = "<http://www.w3.org/2000/01/rdf-schema#>";
	
	
	// virtuoso Ontotext URL
	private static final String URI  		 = "http://dbpedia.org/sparql";
	
	
	/**
	 * stub constructor
	 */
	public SparqlTM(){}
	
	

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
				sparqlService(URI, query);

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
	    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++\n");	
	    if (res==true){
	    	System.out.println("empty query!");
	    }else{
	    	System.out.println("non-empty query!");
	    }
	    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++\n");	    
		
	    return res;
		
	}
	
	
    /**
     * return glosses
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
		QueryExecution queryExec = QueryExecutionFactory.
				createServiceRequest(URI, query);
		
		// return value
	    Pair<String,String> data = null;
	    String left  = "";
	    String right = "";
		
		try {
		
			// get result set
			result = queryExec.execSelect();
		    
		    // loop over result set
		    while (result.hasNext()){
		    	
	    		QuerySolution qs = result.next();
		    	left = qs.get("abs").toString();
		    	right = qs.get("comm").toString();	    	
		    	
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
	    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++\n");
	    
	    // create query
		Query query=QueryFactory.create(queryString);
		QueryExecution queryExec = QueryExecutionFactory.
				createServiceRequest(URI, query);
		
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
	    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++\n");
	    
	    // create query
		Query query=QueryFactory.create(queryString);
		query.setLimit(limit);
		
		//HttpAuthenticator authenticator = new SimpleAuthenticator("camilothorne", apikey.toCharArray());
		
		QueryExecution queryExec = QueryExecutionFactory.
				createServiceRequest(URI, query);
		
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
	 * return unique gloss (if any) from TagMe
	 * 
	 * @param conceptCUI
	 * 
	 * @return gloss
	 * @throws Exception
	 */
	public static String askGloss(String conceptURI) throws Exception{
		
		String queryString = 
				  "PREFIX dbo: " 	+ dbo + " "
				+ "PREFIX rdfs: " 	+ rdfschema + " " 
				+ "select distinct ?comm ?abs where " 
				+ "{"
				+		"OPTIONAL{<" + conceptURI + "> dbo:abstract ?_abs . "
				+		"filter(langMatches(lang(?_abs),'en')) . } .  "				
				+		"OPTIONAL{<" + conceptURI + "> rdfs:comment ?_comm ."			
				+		"filter(langMatches(lang(?_comm),'en')) .} .  "
				+       "BIND(STR(?_comm) as ?comm ) . "
				+       "BIND(STR(?_abs) as ?abs ) . "				
				+ "} LIMIT 1";
		
		System.out.println(prettyPrint(queryString));
		Pair<String,String> res = getField(queryString);
		
		// return value
		if (res.getLeft().equals("")){
			return res.getRight().toLowerCase();
		}else{
		   return res.getLeft().toLowerCase();
		}
				
	}
	
	
	
	// main class (for tests)
	public static void main(String[] args) throws Exception{
		
		String conceptURI = "http://dbpedia.org/resource/Strategy";
		
		// sample queries by URI
		System.out.println(SparqlTM.askGloss(conceptURI)+"\n");
	}	
	
	
}


