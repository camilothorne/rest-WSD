package tests;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.output.TeeOutputStream;

import gov.nih.nlm.nls.metamap.MetaMapApi;
import it.uniroma1.lcl.jlt.util.Language;

import com.wsd.babelfly.BFlyTagger;
import com.wsd.corpus.ReadMMCorpus;
import com.wsd.mmap.MMAnnotator;
import com.wsd.tagme.RestTagMe;
import com.wsd.wordnet.MyLesk;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * @author camilo
 *
 */
public class TextTest extends TestCase {
	
	
	 // Output redirect
//	 private static FileOutputStream fos;
//	 private static TeeOutputStream myOut;
//	 private static PrintStream ps;

	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     * @throws Exception 
     */	
	public TextTest(String name) throws Exception {
		
		// inherit from TestCase
		super(name);
		
		// 1) create a java calendar instance
		Calendar calendar = Calendar.getInstance();
		// 2) get a Date from the calendar instance.
		//this date will represent the current instant, or "now".
		Date now = calendar.getTime();
		// 3) a java current time (now) instance
		//Timestamp currTime = new java.sql.Timestamp(now.getTime());
		// send stout to file
//		fos 		= 	new FileOutputStream("/home/camilo/workspace-git/RestWSD/annotation-test"
//				+ ""+now.toString()+".txt");
//		myOut		=	new TeeOutputStream(System.out, fos);
//		ps 			= 	new PrintStream(myOut,true,"UTF-8");
//		System.setOut(ps);
		  
	}

	
    /**
     * @return the suite of tests being tested
     */
    public static Test suite(){
        return new TestSuite( TextTest.class );
    }	
    
    
//    /**
//     * Test - BabelNet
//     */ 
//	public void testBabelFly() throws Exception {
//	
//		// uncomment to skip
//		// fail();
//		
//		String inputText1 = "BabelNet is both a multilingual encyclopedic dictionary and a semantic network.";
//		String inputText2 = "Strategies for preclinical evaluation of "
//				+ "dendritic cell subsets for promotion of transplant tolerance in the nonhuman primate.";
//		String inputText3 = "John Dee experienced a malfunction of his car.";
//		String[] sentences = {inputText1,inputText2,inputText3};
//		
//		// BabelFly
//		int i = 0;
//		for (String s: sentences){
//			BFlyTagger.taggerFly(s, Language.EN,i);
//			i++;
//		}
//	
//	}
    
    
//    /**
//     * Test - WordNet
//     */ 
//	public void testWordNet() throws Exception {
//		
//		// uncomment to skip
//		//fail();
//		
//		String inputText1 = "BabelNet is both a multilingual encyclopedic dictionary and a semantic network.";
//		String inputText2 = "Strategies for preclinical evaluation of "
//				+ "dendritic cell subsets for promotion of transplant tolerance in the nonhuman primate.";
//		String inputText3 = "John Dee experienced a malfunction of his car.";
//		String[] sentences = {inputText1,inputText2,inputText3};
//		
//		// WordNet
//		new MyLesk(sentences);
//		
//	}
		
	
//    /**
//     * Test - TagMe
//     */ 
//	public void testTagMe() throws Exception {
//		
//		// uncomment to skip
//		//fail();
//		
//		String inputText1 = "BabelNet is both a multilingual encyclopedic dictionary and a semantic network.";
//		String inputText2 = "Strategies for preclinical evaluation of "
//				+ "dendritic cell subsets for promotion of transplant tolerance in the nonhuman primate.";
//		String inputText3 = "John Dee experienced a malfunction of his car.";
//		String[] sentences = {inputText1,inputText2,inputText3};
//		
//		// TagMe
//		int i = 0;
//		for (String s: sentences){
//			RestTagMe.taggerTagMe(s,i);
//			i++;
//		}
//	
//	}	
	
	
//    /**
//     * Test - MetaMap
//     */ 
//	public void testMetaMap() throws Exception {
//		
//		// uncomment to skip
//		//fail();
//		
//		String inputText1 = "BabelNet is both a multilingual encyclopedic dictionary and a semantic network.";
//		String inputText2 = "Strategies for preclinical evaluation of "
//				+ "dendritic cell subsets for promotion of transplant tolerance in the nonhuman primate.";
//		String inputText3 = "John Dee experienced a malfunction of his car.";
//		String[] sentences = {inputText1,inputText2,inputText3};
//
//		// MetaMap connect
//		String serverhost = MetaMapApi.DEFAULT_SERVER_HOST; // default host
//		int serverport = MetaMapApi.DEFAULT_SERVER_PORT; 	// default port
//		int timeout = -1; // use default timeout
//		MMAnnotator ann = new MMAnnotator(serverhost,serverport);
//		
//		// print to JSON
//		ann.processLines((List<String>) Arrays.asList(new String[]{inputText1,inputText2}));
//		
////		// MetaMap (with timeout)
////		for (String s: sentences){
////			ann.processLine(s);
////			if (timeout > -1) {
////				MMAnnotator.setTimeout(timeout);
////			}
////		}
//	
//	}	
	
	/**
	 * Test - Corpus
	 * @throws Exception
	 */
	public void testCorpus() throws Exception {
		
		new ReadMMCorpus("results/adjudicated-test.xml");
		
	}
	
}
