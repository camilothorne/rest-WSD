package com.wsd.mmap;

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
public class SparqlMM{
		
			
	// schema prefixes
//	private static final String owl			 = "http://www.w3.org/2002/07/owl#";
//	private static final String rdf			 = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	private static final String skos 		 = "<http://www.w3.org/2004/02/skos/core#>";
	private static final String rdfschema 	 = "<http://www.w3.org/2000/01/rdf-schema#>";
	
	
	// virtuoso Ontotext URL
	private static final String MMapURI  		= "http://linkedlifedata.com/sparql/";
	// DBpedia
	private static final String DBpediaURI  	= "http://dbpedia.org/sparql/";	
	
	
	/**
	 * stub constructor
	 */
	public SparqlMM(){}
	
	

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
				sparqlService(MMapURI, query);

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
	 * check if link is empty
	 * 
	 * @param queryString
	 * @return
	 * @throws Exception
	 */
	public static boolean isVoid (String dbpediaURI) throws Exception{
		
		// value to return
		boolean res;
		
		String queryString = "SELECT * WHERE {<" + dbpediaURI + "> ?p ?o}";
		
	    // create query
		Query query=QueryFactory.create(queryString);
		query.setLimit(5);
		QueryExecution queryExec = QueryExecutionFactory.
				sparqlService(DBpediaURI, query);

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
		
		// comment out to print to stout the result of the test
	    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
	    System.out.println(queryString);
	    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++\n");	
	    if (res==true){
	    	System.out.println("empty URI!");
	    }else{
	    	System.out.println("non-empty URI!");
	    }
	    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++\n");	    
		
	    return res;
		
	}	
	
	
    /**
     * return unique field from UMLS query
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
				createServiceRequest(MMapURI, query);
		
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
		    	if (!qs.contains("def")){
		    		left = "";
		    	}
		    	else{
		    		left = qs.get("def").toString();
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
				createServiceRequest(MMapURI, query);
		
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
				createServiceRequest(MMapURI, query);
		
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
	 * return unique gloss (if any) from UMLS CUI query
	 * 
	 * @param conceptCUI
	 * 
	 * @return gloss
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Pair<String,String> askGloss(String conceptCUI) throws Exception{
		
		// check DBpedia
		String queryString = 
				  "PREFIX skos: " 	+ skos + " "
				+ "PREFIX rdfs: " 	+ rdfschema + " " 
				+ "SELECT DISTINCT ?label ?def "	  
				+ "WHERE "
				+ "{"
				+		"OPTIONAL{<http://linkedlifedata.com/resource/umls/id/" + conceptCUI + "> skos:definition ?def} . "
				+ 		"OPTIONAL{<http://linkedlifedata.com/resource/umls/id/" + conceptCUI + "> skos:prefLabel ?label} . "				
				+ "}";
		// evaluate
		Pair<String,String> res =  getField(queryString);
		
		// return value
		String label = (res.getRight().toLowerCase());
		
		// postprocess label
		if (label.contains(",")){
			label = (label.split(",")[1] + label.split(",")[0]).trim(); // prefix all after comma
			//label = label.split(",")[0].trim(); // delete all after comma
		}else{
			label = label.trim();
		}
		label = label.replace(" ", "_");
		label = label.replace("_-_", "-");		
		label = label.replace("*", "");		
		label = label.replace("<", "");
		label = label.replace(">", "");		
		label = label.replace("{", "");
		label = label.replace("}", "");		
		
		// check if it mentions points in time
		if (label.contains("point_in_time")){
			label = "point_in_time";
		}
		
		// create DBepdia URI
		if (!(label=="")){
			char in = Character.toUpperCase(label.charAt(0));
			String dbpedia = "http://dbpedia.org/resource/"+in+label.substring(1);
			//System.out.println(dbpedia);
			if (SparqlMM.isVoid(dbpedia)){
				return new Pair("NaN",res.getLeft());
			}else{
				return new Pair(dbpedia,res.getLeft());
			}
		}else{
			return new Pair("NaN",res.getLeft());
		}
				
	}
	
	
	// main class (for tests)
	public static void main(String[] args) throws Exception{

		
//		// sample query 
//		String queryString1 = 
//				  "PREFIX skos: " 	+ skos + " "
//				+ "PREFIX rdfs: " 	+ rdfschema + " " 
//				+ "SELECT DISTINCT ?label ?def "	  
//				+ "WHERE "
//				+ "{"
//				+		"<http://linkedlifedata.com/resource/umls/id/C0027051> skos:definition ?def . "
//				+ 		"<http://linkedlifedata.com/resource/umls/id/C0027051> skos:prefLabel ?label . "				
//				+ "}";
//		
//		// print results
//		System.out.println(SparqlMM.prettyPrint(queryString1));	
		

		// sample queries by CUI
		System.out.println("C0027051");
		System.out.println(SparqlMM.askGloss("C0027051").getRight());
		System.out.println(SparqlMM.askGloss("C0027051").getLeft()+"\n");

		
		System.out.println("C0018681");		
		System.out.println(SparqlMM.askGloss("C0018681").getRight());
		System.out.println(SparqlMM.askGloss("C0018681").getLeft()+"\n");
		
		
		System.out.println("C0018681");		
		System.out.println(SparqlMM.askGloss("C0600139").getRight());
		System.out.println(SparqlMM.askGloss("C0600139").getLeft()+"\n");
		
		
		System.out.println("C0018681");		
		System.out.println(SparqlMM.askGloss("C2825142").getRight());
		System.out.println(SparqlMM.askGloss("C2825142").getLeft()+"\n");		
		
		
	}	
	
	
}


