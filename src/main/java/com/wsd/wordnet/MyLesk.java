package com.wsd.wordnet;


// Java API
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;


// Varia
//import org.apache.commons.io.output.TeeOutputStream;
//import org.json.JSONArray;





// JSON
import org.json.JSONObject;
import org.json.JSONTokener;





import com.wsd.babelnet.SparqlBN;
import com.wsd.babelnet.UrlBN;

import edu.smu.tspell.wordnet.impl.file.synset.NounReferenceSynset;
// Stanford NLP API
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.CollinsHeadFinder;
import edu.stanford.nlp.ling.TaggedWord;


/**
 * 
 * @author camilo
 *
 */
public class MyLesk{

	
    	  // annotations
    	  public static String CORPUS = "results/annotations-wsd.json";
	
    	  
		  // Public static fields
		  public static TreebankLanguagePack tlp; // treebank functions
		  public static GrammaticalStructureFactory gsf; // dependency parsing functions
		  public static LexicalizedParser lp; // stanford parser
		  public static TreePrint tp; // tree print
		  public static CollinsHeadFinder head; // head finder 
		  public static MyLemmatizer lemmatizer;
		  
		  
		  // Dynamic field(s)
		  public BufferedReader file; // file to read
		  public String path; // path to file
		  public Tree parse; // parse tree
		  public ArrayList<String> sents; // sentences to parse
		  public ArrayList<MySentence> mycorpus; // storing NPs
		  
		  
		  // uncomment for output redirect
//		  private static FileOutputStream fos;
//		  private static TeeOutputStream myOut;
//		  private static PrintStream ps;
		  
		  
		  // WordNet
		  public static WNTagger wnTagger;
		  
		  
		  /** empty constructor
		   * 
		   */
		  public MyLesk() {}
		  
		  
		  /** constructor
		   * 
		   * @param path
		   * @param file
		   * 
		   * @throws Exception
		   */
		  public MyLesk(String path, BufferedReader file) throws Exception
		  {
			
			  // uncomment to send stout to file
//			  fos 	= 	new FileOutputStream("/home/camilo/wordnet.txt");
//			  myOut	=	new TeeOutputStream(System.out, fos);
//			  ps 		= 	new PrintStream(myOut,true,"UTF-8");
//			  System.setOut(ps);
			  
			  // buffer must be initialized to null
			  if (file.equals(null)){
			  
				  // taggers
				  wnTagger = new WNTagger();
				  
				  this.path = path;
				  this.file = file;
				  
				  // Stanford tagger
				  tlp = new PennTreebankLanguagePack();
				  gsf = tlp.grammaticalStructureFactory();
				  // loading language model + parsing options
				  lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
				  lp.setOptionFlags("-maxLength", "80", "-outputFormat", "typedDependenciesCollapsed");
				  //head finder
				  head = new CollinsHeadFinder(tlp);
				  //lemmatizer
				  lemmatizer = new MyLemmatizer();
				  // open corpus
				  if (path != ""){
					  this.sents = this.readFile(path,file);
					  // parse and annotate
					  this.processLines(this.sents);
				  }
				  
				  // save all
				  this.mycorpus = new ArrayList<MySentence>();
			  
			  }
			  
		  }

		  
		  /** constructor
		   * 
		   * @param sentence
		   * 
		   * @throws Exception
		   */
		  public MyLesk(String[] sentences) throws Exception
		  {
			  
			  // uncomment to send stout to file
//			  fos 	= 	new FileOutputStream("/home/camilo/wordnet.txt");
//			  myOut	=	new TeeOutputStream(System.out, fos);
//			  ps 		= 	new PrintStream(myOut,true,"UTF-8");
//			  System.setOut(ps);			  
			  
			  // taggers
			  wnTagger = new WNTagger();
			  
			  // Stanford tagger
			  tlp = new PennTreebankLanguagePack();
			  gsf = tlp.grammaticalStructureFactory();
			  // loading language model + parsing options
			  lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
			  lp.setOptionFlags("-maxLength", "80", "-outputFormat", "typedDependenciesCollapsed");
			  // head finder
			  head = new CollinsHeadFinder(tlp);
			  //lemmatizer
			  lemmatizer = new MyLemmatizer();
			  // process input
			  this.processLines(Arrays.asList(sentences));
			  
		  }		  
		  
		  
		  /** constructor
		   * 
		   * @param sentence
		   * 
		   * @throws Exception
		   */
		  public MyLesk(String sentence) throws Exception
		  {
			  
			  // uncomment to send stout to file
//			  fos 	= 	new FileOutputStream("/home/camilo/wordnet.txt");
//			  myOut	=	new TeeOutputStream(System.out, fos);
//			  ps 		= 	new PrintStream(myOut,true,"UTF-8");
//			  System.setOut(ps);
			  
			  // taggers
			  wnTagger = new WNTagger();
			  
			  // Stanford tagger
			  tlp = new PennTreebankLanguagePack();
			  gsf = tlp.grammaticalStructureFactory();
			  // loading language model + parsing options
			  lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
			  lp.setOptionFlags("-maxLength", "80", "-outputFormat", "typedDependenciesCollapsed");
			  // head finder
			  head = new CollinsHeadFinder(tlp);
			  //lemmatizer
			  lemmatizer = new MyLemmatizer();
			  // process input
			  this.processLine(sentence);
			  
		  }
		   
		   
		   /** Tag sets of sentences
		    * 
		    * @param samples
		    * 
		    * @throws Exception
		    */
		   public void processLines(List<String> samples) throws Exception
		   {			   
			   
			   int num = 0; // counter (count # of sentences)		   
			   for (String samp: samples){
				   List<CoreLabel> tokens = this.myTokenize(samp);// call tokenizer
				   if ((tokens.size()>0)){
					   //System.out.println("============================================================");
					   //System.out.println("SENT " + num + " : " + samp);
					   //System.out.println("============================================================");
					   this.parse = lp.apply(tokens); // call/set parser
					   //System.out.println();
					   //this.parse.pennPrint(); // print parse tree
					   // annotate NPs, NNs
					   MySentence sen = mineTree(this.parse,samp,num);
					   // save parse(s)
					   sen.setSentokens(tokens);
					   sen.setParsing(this.parse);
					   sen.setNum(num);
					   num = num + 1;
					   // save sentence
					   // this.mycorpus.add(sen);
					   
				   }				   	
			   }
			   
		  }		   
		   

		  /** Tag one sentence only
		   * 
		   * @param sample
		   * 
		   * @throws Exception
		   */
		  public void processLine(String sample) throws Exception
		  {
			   
			  //PrintStream myoutput = System.out; // print to stout			   		   			   
			   List<CoreLabel> tokens = this.myTokenize(sample);// call tokenizer
			   //System.out.println("============================================================");
			   //System.out.println("SAMP : " + sample);
			   //System.out.println("============================================================");
			   this.parse = lp.apply(tokens); // set/call parser
			   System.out.println();
			   //this.parse.pennPrint(); // print parse tree
			   // annotate NPs, NNs	
			   MySentence sen = mineTree(this.parse,sample,0);
			   // save parse(s)
			   // save parse(s)
			   sen.setSentokens(tokens);
			   sen.setParsing(this.parse);
			   sen.setNum(0);
			   // save sentence
			   this.mycorpus.add(sen);
			   
		  }
		  
		  
		  /** NP senses (WordNet)
		   * 
		   * @param word
		   * 
		   * @return senses
		   */
		  public Set<MySense> sensesNPWordNet(String word){ 
			  
			  //System.out.println("trigger");
			  
			  return wnTagger.annotateNoun(word,lemmatizer);
		  }		  
		  		  
		  
		  /** mine NP annotations (main annotation method)
		   * 
		   * @param myparse
		   * @param sent
		   * @param num
		   * 
		   * @return sentence
		   * @throws Exception
		   * 
		   */
		   @SuppressWarnings({ "rawtypes", "unchecked" })
		   public MySentence mineTree(Tree myparse, String sent, int num) throws Exception
		   { 
			    
			  	// intialize sentence
			  	MySentence sen = new MySentence(myparse.taggedLabeledYield());
			  	sen.noun_phrases = new ArrayList<MyNounPhrase>();
				sen.setSentence(sent);
			  	// return all subtrees
				Set<Tree> sub = myparse.subTrees();
				// filter non basic NPs 
				filterNPs(sub);	
				
		    	// load result file into JSON object    	
		    	JSONTokener tokener = new JSONTokener(new FileReader(CORPUS));
	          	JSONObject root = new JSONObject(tokener);
	          	
		    	// set sentence text
	    	    Map<String,String> 	text = new HashMap();
		    	text.put("sentence", sent);
		    	
	    	    // set NPs with annotations 
		    	Map<String,List<Map>>  	anno = new HashMap();
		    	List<Map> anns = new LinkedList();

				// loop over sentence constituents
				for (Tree tree: sub){
					
					// NNs
					if (tree.label().value().matches("NN") || 
						tree.label().value().matches("NNS") ){
						
						// mine NP
						MyNounPhrase mynp = mineNP(sen,tree,myparse,sub);
						
						// retrieve annotations
						
						// store annotations in JSON
						if (mynp.getSynset() != null){
						
							String np = mynp.getPhrase().trim();
							String sense = mynp.getSynset().nounSynset.getWordForms()[0];
							String gloss = mynp.getSynset().nounSynset.getDefinition();
							int offset  = ((NounReferenceSynset)mynp.getSynset().nounSynset).getOffset();

							int val = 8-String.valueOf(offset).length();
							String prefix = "";
							for (int i=0;i<val;i++){
								prefix = prefix + "0"; 
							}	
							String wnid = prefix + offset + "n";
							
							String[] maps = UrlBN.WNmappings(wnid);
							
							float  score = mynp.getSynset().rank;				
							Map<String,String> 	 ph = new HashMap();
							ph.put("phrase", 	 np);
							ph.put("sense", 	 sense);
							ph.put("bnid", 		 maps[0]);
							ph.put("wnid", 		 "wn:"+wnid);
							
							ph.put("dbid", 	 maps[1]);
							
//							if (!maps[1].trim().equals("NaN")){
//								ph.put("dbid", 	 maps[1]);
//							}else{
//								String dbid = SparqlBN.getDBpedia(maps[0]);
//								ph.put("dbid", 	 dbid);								
//							}
							
							ph.put("gloss", 	 gloss);		
							ph.put("confidence", "" + score + "");
							anns.add(ph);
							
						}
						
						// save NP
						sen.noun_phrases.add(mynp);
					}
					
				}
				
				// set annotations
	    	    anno.put("WordNet", anns);
	    	    
	    	    //System.out.println("***"+num+"***");
	    	    
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
		    	    
	    	    	// wrap tags into JSON
		    	    JSONObject janno = new JSONObject(anno);
		    	
		    	    // update file
		    	    root.append(""+num+"", janno);
		    	    FileWriter file = new FileWriter(CORPUS);
		    		file.write(root.toString(5));
		    		file.flush();
		    		file.close();

	    	    }
				
				return sen;
				
		  }
		  
		  
		  /** annotate single NPs
		   * 
		   * @param sen
		   * @param tree
		   * @param myparse
		   * @param sub
		   * 
		   * @return noun phrase
		   * @throws Exception
		   */
		  public MyNounPhrase mineNP(MySentence sen, Tree tree, Tree myparse,
				  Set<Tree> sub) throws Exception
		  {
				
				// recover context
				List<String> rawContext = sen.getTokens();
				String[] context = rawContext.toArray(new String[rawContext.size()]);
				
				// init NP
				MyNounPhrase my_np = new MyNounPhrase();
				
				// harvest text
				String np = ""; // np phrase 
				ArrayList<TaggedWord> words = tree.taggedYield();
				for (Word wor: words){
					np = np + " " + wor.toString().replaceAll("/.*", "");	
				}
				
				// collect candidate senses (if any)
				List<MySense> np_senses = new ArrayList<MySense>(sensesNPWordNet(np));
				
				// compute ranks
				for (MySense sense : np_senses){
					float score = sense.computeSimGloss(context);
					sense.rank = score;
				}	
				
				// sort them (by rank)
				Collections.sort(np_senses);
				
				// return the max
				MySense np_sense;
				if (np_senses.size()==0){
					np_sense = null;
				}else{
					np_sense = np_senses.get(0);
				}
				
				// NP yield (words/phrase)
				my_np.setPhrase(np);							// set NP
				my_np.setContext(sen.getTokens());				// set context
				my_np.setSynsets(np_senses);					// collect senses/synsets			
				my_np.setSynset(np_sense);						// set the most likely sense
				
				// sense lemma
				String lemma;
				if (np_sense == null){
					lemma = null;
				}else{
					lemma = np_sense.nounSynset.getWordForms()[0];
				}
				
				// sense score
				float score;
				if (np_sense == null){
					score = 0;
				}else{
					score = np_sense.rank;
				}
				
				// print results
				//System.out.println("-------------------------------------------------------");
				//System.out.println("phrase: " 	+ np);
				//System.out.println("sense: " 	+ lemma);
				//System.out.println("rank: " 	+ score);
				
				// return NP
				return my_np;
		  }

		  
		  /** Converting file to list of sentences (one per line, 
		   * otherwise, use sentence segmentator)
		   * 
		   * @param  filepath
		   * @param  filebuffer
		   * @return sentences
		   */
		  public ArrayList<String> readFile(String filepath, BufferedReader filebuffer){
			   ArrayList<String> result = new ArrayList<String>();
			   try {
					String sCurrentLine;
					filebuffer = new BufferedReader(new FileReader(filepath));
					while ((sCurrentLine = filebuffer.readLine()) != null) {
						System.out.println(sCurrentLine);
						result.add(sCurrentLine);
					}	 
					filebuffer.close();
				} 
			   catch (IOException ex) {
					ex.printStackTrace();
				} 
			   return result;
		  }
		   
		   
		  /** Sentence/line tokenizer
		   * 
		   * @param  sent : sentence
		   * 
		   * @return rawWords : POS tagged tokens
		   */
		  public List<CoreLabel> myTokenize(String sent){
				TokenizerFactory<CoreLabel> tokenizerFactory = 
						PTBTokenizer.factory(new CoreLabelTokenFactory(), "");// initialize tokenizer
				List<CoreLabel> rawWords = 
						tokenizerFactory.getTokenizer(new StringReader(sent)).tokenize(); // run tokenizer
				return rawWords;
		  }		  
		  

		  /** Filter out complex NPs (we want only non-recursive NPs)
		   * 
		   * @param mytree
		   */
		  public void filterNPs(Set<Tree> mytree){
			  Set<Tree> NPs = new HashSet<Tree>();
			  for (Tree tree: mytree){					
				if (tree.label().value().matches("NP")){
					NPs.add(tree);
				}
			  }
			  for (Tree tree1: NPs){
				  for (Tree tree2: NPs){
					  if((tree2.subTrees().contains(tree1))&&!(tree1.equals(tree2))){ // strict containment
						  mytree.remove(tree2);
					  }
				  }
			  }
		  }
	  

		  /** complex condition for NP filtering
		   * 
		   * @param tree
		   * @param text2
		   * 
		   * @return boolean
		   */
		  public boolean filterCond(Tree tree, String text2){
			String np = "";
			ArrayList<TaggedWord> words = tree.taggedYield();
			for (Word wor: words){
				np = np + " " + wor.toString().replaceAll("/.*", "");
			}
			if (np.matches(".*"+text2+".*")){
				return true;
			}
			else{
				return false;
			}
		  }		  
		  
}
