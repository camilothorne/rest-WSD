package tests;

import gov.nih.nlm.nls.metamap.MetaMapApi;
import it.uniroma1.lcl.jlt.util.Language;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.wsd.babelfly.BFlyTagger;
import com.wsd.corpus.ReadMMCorpus;
import com.wsd.mmap.MMAnnotator;
import com.wsd.tagme.RestTagMe;
import com.wsd.wordnet.MyLesk;


/**
 * Annotation test
 * ===============
 * 
 * Prerequisites:
 * 
 * - BabelNet 	(API keys for babelfly, babelnet -- limited to 1000 queries)
 * - TagMe 		(API key -- unlimited)
 * - WordNet	(WordNet 3.0)
 * - MetaMap 	(mmserver14, wsdserverctl, skrmedpostctl)
 * 
 * Note: might exceed JRE memory and/or core allocation
 * 
 * @author Camilo Thorne
 *
 */
public class JSONTest extends TestCase{

	
   /**
    * Create the test case
    *
    * @param testName name of the test case
    * 
    * @throws Exception 
    */	
	public JSONTest(String name) throws Exception {
		// inherit from TestCase
		super(name);
	}

	
   /**
    * @return the suite of tests being tested
    */
   public static Test suite(){
       return new TestSuite( JSONTest.class );
   }	
	
	
	   /**
	    * Test - Full corpus
	    * 
	    * @throws Exception 
	    */ 
		public void testAnnotationFull() throws Exception {
		
			// uncomment to skip
			// fail();
			
			// unannotated corpus
			String contents = FileUtils.readFileToString(new File("results/mmcorpus-raw.txt"), "UTF-8");
			contents = contents.replace("\n\n", "\n");
			String[] sentences = contents.split("\n");
			
			// comment out to display corpus
//			for (String sent: sentences){
//				if (!sent.equals("")){
//					System.out.println(sent);
//				}
//			}
			
			// Gold corpus
//			new ReadMMCorpus();
			
			// WordNet (takes array as input)
//			new MyLesk(sentences);
			
			// MetaMap (takes array as input)
			String serverhost = MetaMapApi.DEFAULT_SERVER_HOST; // default host
			int serverport = MetaMapApi.DEFAULT_SERVER_PORT; 	// default port	
			MMAnnotator ann = new MMAnnotator(serverhost,serverport);
			ann.processLines(Arrays.asList(sentences));			// transform to list				
		
			// BabelFly, TagMe (take single sentence as input)
//			int i = 0;
//			for (String s: sentences){
//				//System.out.println(i);
//				//System.out.println(s);
//				RestTagMe.taggerTagMe(s,i);
//				BFlyTagger.taggerFly(s,Language.EN,i);
//				i = i + 1;
//			}		
			
			// display results (JSON object/file)    	
//		    JSONTokener tokener = new JSONTokener(new FileReader("results/annotations-wsd.json"));
//	        JSONObject root = new JSONObject(tokener);
//	        System.out.println(root.toString(5));
		
		}
		
		
//		/**
//		 * Test - Generate report
//		 * 
//		 * @throws Exception
//		 */
//		public void testBuildReport() throws Exception {
//			
//			// uncomment to skip
//			fail();
//			
//			String python = "/usr/bin/python";
//			String script = "/home/camilo/workspace-git/restwsdstats/wordstats/main.py"; 
//			ProcessBuilder pb = new ProcessBuilder(python,script);
//			//pb.inheritIO();
//			pb.start();
//			//pb.wait();
//			
//		}
	
	
}
