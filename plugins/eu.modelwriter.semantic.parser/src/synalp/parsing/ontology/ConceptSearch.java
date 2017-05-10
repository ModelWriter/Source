package synalp.parsing.ontology;

import synalp.parsing.utils.FileOperations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLException;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
/**
 * This is a singleton class
 * @author Anastasia
 *
 */


public class ConceptSearch {
	
	private Map<String, String> matchingRdfsLabels;
	private Map<String, String> partialMatching;
	private Map<String, ArrayList<String>> matchingSuperClasses;
	private static ConceptSearch instance = null;

	private ConceptSearch() {
      	// constructor. Exists only to defeat instantiation.
		matchingRdfsLabels = new HashMap<String, String>();
		partialMatching = new HashMap<String, String>();
		matchingSuperClasses = new HashMap<String, ArrayList<String>>();
   	}

	public static ConceptSearch getInstance() {
	      if(instance == null) {
		 instance = new ConceptSearch();
	      }
	      return instance;
	 }

	public String morpholiser(String str) {
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		// create an empty Annotation just with the given text
		Annotation document = new Annotation(str);
		
		// run all Annotators on this text
		pipeline.annotate(document);
		String lemmas = new String();
		// get lemmas for each token in the string
		for (CoreLabel token: document.get(TokensAnnotation.class)) {
		    String lemma = token.get(LemmaAnnotation.class);
		    // in the case of MWE, remove determiners
		    if(str.contains("_+_")) {
		    	String pos = token.get(PartOfSpeechAnnotation.class);
		    	if (!pos.equals("DT")) {
		    		lemmas += lemma;
		    	}
		    }
		    else {
		    	lemmas += lemma;
		    }
		  }
		// after removing determiners we have an output:
		// _+_sufficient_distance_+_from_+__+_pipe_+_extremity => delete double spaces
		String lemmas_filtered = new String();
		if (str.contains("_+_")) {
			String[] parts = lemmas.split("_\\+_");
			for(int i=0; i < parts.length; i++) {
				if (!parts[i].isEmpty()) {
					lemmas_filtered += parts[i] + "_+_";
				}
			}
			// delete the last space
			if (lemmas_filtered.endsWith("_+_")) {
				lemmas_filtered = lemmas_filtered.substring(0, lemmas_filtered.length() - 3);
				}
		}
		else {
			lemmas_filtered = lemmas;
		}
		return lemmas_filtered;
	}
	
	// link new concepts and rdfs-labels, if they have the same string
	public void rdfsMatching (String str, Map<String,String> rdfsLabels) {
		// rdfs labels use spaces (" "), we use "_+_" instead.
		// replace spaces in rdfs labels with our notation
		//rdfsLabels.put("conduit", "<http://airbus-group.installsys/component#Sleeve>");
		for (String rdfsLabel: rdfsLabels.keySet()) {
			String rdfsLabelSpaces = rdfsLabel.replace(" ", "_+_");
			if (str.equals(rdfsLabelSpaces)) {
				matchingRdfsLabels.put(str.replace("_+_", " "), rdfsLabels.get(rdfsLabel));
			}
			else if (str.contains(rdfsLabelSpaces)) {
				partialMatching.put(str.replace("_+_", " "), rdfsLabels.get(rdfsLabel));
			}
		}
	}
	
	
	// relate new concepts to existing classes; detect superclasses of new concepts
	public void superClassMatching (String str, Set<OWLClass> initialClasses) {
		// iterate over tokens of a new concept and
		// match them to all possible classes of the initial ontology
		// create a one-to-many mapping: new concept -- possible super classes
		String[] parts = str.split("_\\+_");
		ArrayList<String> classes = new ArrayList<String>();
		for (int i=0; i < parts.length; i++) {
			for (OWLClass cls: initialClasses) {
				// get fragment of IRI
				String className = cls.getIRI().getFragment();
				if (className.toLowerCase().equals(parts[i])) {
					classes.add(cls.toString());
				}
			}
		}
		matchingSuperClasses.put(str.replace("_+_", "_"), classes);
	}
	
	
	public ArrayList<String> getSuggestions(String str) {
		return matchingSuperClasses.get(str);
	}
	
	
	private StringBuilder hashMapToString(Map<String, String> hashMap){
		StringBuilder str = new StringBuilder();
		for (Map.Entry<String, String> entry : hashMap.entrySet()) {
		    str.append(entry.getKey() + "  ::  " + entry.getValue() + "\n");
		}
		return str;
	}
	
	
	private StringBuilder hashMapWithArrayToString(Map<String, ArrayList<String>> hashMapArray){
		StringBuilder str = new StringBuilder();
		for (Map.Entry<String, ArrayList<String>> entry : hashMapArray.entrySet()) {
		    str.append(entry.getKey() + "  ::  " + entry.getValue() + "\n");
		}
		return str;
	}
	
	
	public Integer getSizeMatching() {
		return matchingRdfsLabels.size();
	}
	
	public Integer getSizeSuperClassMatching () {
		// count number of new concepts for which at least one superclass was found
		Integer count = 0;
		for (Map.Entry<String, ArrayList<String>> entry : matchingSuperClasses.entrySet()) {
		    if(!entry.getValue().isEmpty())
		    	count++;
		}
		return count;
	}
	
	
	public void writeOutput(String outputFolderName) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		File fileRdfsLabels = new File(outputFolderName+"ontology_enrichment/new_classes_RDFS.txt");
		File filePartialRdfs = new File(outputFolderName+"ontology_enrichment/new_classes_partialRDFS.txt");
		File fileSuperClasses = new File(outputFolderName+"ontology_enrichment/new_classes_to_SuperClasses.txt");
		FileOperations.writeToFile(fileRdfsLabels, hashMapToString(matchingRdfsLabels).toString());
		FileOperations.writeToFile(filePartialRdfs, hashMapToString(partialMatching).toString());
		FileOperations.writeToFile(fileSuperClasses, hashMapWithArrayToString(matchingSuperClasses).toString());
	}
	
}