package com.wsd.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

/**
 * 
 * @author camilo
 *
 */
public class Norm {

	
	// Stanford NLP
    protected StanfordCoreNLP pipeline;
	
    
	/**
	 * constructor (inits Stanford NLP pipeline)
	 */
	public Norm() {

		// Create StanfordCoreNLP object properties, with POS tagging
		// (required for lemmatization), and lemmatization
		Properties props;
		props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma");

		// StanfordCoreNLP loads a lot of models, so you probably
		// only want to do this once per execution
		this.pipeline = new StanfordCoreNLP(props);
	
	}
	
	
	/**
	 * convert MetaMap phrases into text
	 * 
	 * @param text
	 * 
	 * @return normalized_text
	 */
	public String normMM(String text){
		String result = "";
		text = text.replace("[", "");
		text = text.replace("]", "");		
		String[] token = text.split(",");
		for (String tok: token){
			result = result + lemmaWord(tok.toLowerCase()) + " ";
		}
		return result.trim();
	}
	
	
	/**
	 * convert BabelNet phrases into text
	 * 
	 * @param text
	 * 
	 * @return normalized_text
	 */
	public String normBN(String text){
		String result = "";		
		String[] token = text.split("_");
		for (String tok: token){
			result = result + lemmaWord(tok.toLowerCase()) + " ";
		}
		return result.trim();
	}
	
	
	/**
	 * lemmatize word (Stanford NLP)
	 * 
	 * @param text
	 * 
	 * @return lemma
	 */
	public String lemmaWord(String text){
        String lemma = "";
        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);
        // run lematizer on this text
        this.pipeline.annotate(document);
        CoreMap sentence = document.get(SentencesAnnotation.class).get(0);
        CoreLabel token = sentence.get(TokensAnnotation.class).get(0);    	
        lemma = token.get(LemmaAnnotation.class);
        return lemma;
	}

}
