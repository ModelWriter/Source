package synalp.parsing.dlSemantics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import synalp.parsing.ontology.OntoModel;


/**
 * This class will be used to store the tree representation of the Jeni parse 
 * semantics into Description Logic Syntax. An object of this class will represent
 * the complete DL tree representation of the input Parser semantics.
 * @author bikash
 */

public class DLTree {

	private ArrayList<DLNode> allNodes;
	private DLNode rootNode;
	private ArrayList<String> axiom;
	private Set<String> propertyAssertions;
	
	private Set<String> conceptNames;
	private Set<String> relationNames;
	
	private ArrayList<String> ontologyEnrichmentStatus;
	
	
	// Mappings proposed on OWL Functional Syntax.
	// https://www.w3.org/TR/owl2-syntax/#Functional-Style_Syntax . Section 8
	private static Map<String,String> StringMappings;
	static {
		StringMappings = new HashMap<String,String>();
		StringMappings.put("subset", "SubClassOf");
		StringMappings.put("equiv", "EquivalentClasses");
		
		StringMappings.put("and", "ObjectIntersectionOf");
		StringMappings.put("conj", "ObjectIntersectionOf");
		StringMappings.put("not", "ObjectComplementOf");
		StringMappings.put("or", "ObjectUnionOf");
		/*StringMappings.put(key, "ObjectOneOf");*/
		
		StringMappings.put("exists", "ObjectSomeValuesFrom");
		StringMappings.put("forall", "ObjectAllValuesFrom");
		/*StringMappings.put(key, "ObjectHasValue");
		StringMappings.put(key, "ObjectHasSelf");*/
		
		/*StringMappings.put(key, "ObjectMinCardinality");
		StringMappings.put(key, "ObjectMaxCardinality");
		StringMappings.put(key, "ObjectExactCardinality");*/
		
		/*StringMappings.put(key, "DataSomeValuesFrom");
		StringMappings.put(key, "DataAllValuesFrom");
		StringMappings.put(key, "DataHasValue");*/
		
		/*StringMappings.put(key, "DataMinCardinality");
		StringMappings.put(key, "DataMaxCardinality");
		StringMappings.put(key, "DataExactCardinality");*/
		
	}
	
	
	public DLTree() {
		allNodes = new ArrayList<DLNode>();
		rootNode = null;
		axiom = new ArrayList<String>();;
		propertyAssertions = new HashSet<String>();
		
		conceptNames = new HashSet<String>();
		relationNames = new HashSet<String>();
		ontologyEnrichmentStatus = new ArrayList<String>();
		ontologyEnrichmentStatus.add("No action performed."); // Default Message
	}
	
	
	public void setNodes(ArrayList<DLNode> allNodes) {
		this.allNodes = allNodes;
		setRoot();
		axiom = identifyConceptsRelationsAndMakeAxiom(rootNode);
	}
	
	private void setRoot() {
		for (DLNode node:allNodes) {
			if (!node.hasParent()) {
				rootNode = node;
				break;
			}
		}
	}
	
	@SuppressWarnings("unused")
	@Deprecated 
	// The logic is the same as identifyConceptsRelationsAndMakeAxiom() but directly gives the string. Using identifyConceptsRelationsAndMakeAxiom() instead of this gives more flexibility   
	private String preorderTraversalString(DLNode node) {
		// if the parent of this node was the terminal node, return
	    if(node == null) {
	    	return "";
	    }
	    
	    //String text = "";
	    
	    // if this node was the terminal node; this is 
	    // going to be the variable, like x and y and z;
	    // or things like identifiedarg2inv, identifiedarg3 etc.
	    // we only identifier for them. 
	    if (node.getLabel()==null) {
	    	String return_text = node.getIdentifier().trim();
	    	// Further, if it begins with @, it means that it was a variable like 
	    	// x and y, which we don't want to keep for DL semantics.
	    	if (return_text.toLowerCase().startsWith("@")) { 
	    		return ""; 
	    	}
	    	else if (return_text.equalsIgnoreCase("semtop")) { // There is a special case where the variable is "semtop" meaning that it represents the owl:Thing class
	    		return "owl:Thing";
	    	}
	    	// Else, its going to be the name of the relation
	    	else {
	    		String relationName = ":"+return_text;
	    		relationNames.add(relationName);
	    		return relationName;
	    	}
	    }
	    else {
		    String text = "";
		    //System.out.println(node.getLabel());
		    for (DLNode child:node.getChildren()) {
		    	text = text + preorderTraversalString(child) + " ";
		    }
		    //System.out.println("text = "+text);
		    
		    // For the node whose children is a @ commencing variable,
		    // we would get a empty "( )" which we replace by nothing.
		    // For example, Pipe, labels etc.
		    // Those are going to be the Concept Names
		    if (text.equalsIgnoreCase(" ")) {
		    	String conceptName = ":"+node.getLabel();
		    	conceptNames.add(conceptName);
		    	text = conceptName;
		    }
		    else {
		    	text = (StringMappings.containsKey(node.getLabel())?StringMappings.get(node.getLabel()):node.getLabel())+" ( "+text;
		    	text = text +")";
	    	}
		    return text;
	    }
	}
	
	private ArrayList<String> identifyConceptsRelationsAndMakeAxiom(DLNode node) {
		/**
		 * This is a preorder traversal of the DLTree
		 */
		// if the parent of this node was the terminal node, return
	    if(node == null) {
	    	return new ArrayList<String>();
	    }
	    
	    // if this node was the terminal node; this is 
	    // going to be the variable, like x and y and z;
	    // or things like identifiedarg2inv, identifiedarg3 etc.
	    // we only identifier for them. 
	    if (node.getLabel()==null) {
	    	String return_text = node.getIdentifier().trim();
	    	// Further, if it begins with @, it means that it was a variable like 
	    	// x and y, which we don't want to keep for DL semantics.
	    	if (return_text.toLowerCase().startsWith("@")) { 
	    		return new ArrayList<String>(); 
	    	}
	    	else if (return_text.equalsIgnoreCase("semtop")) { // There is a special case where the variable is "semtop" meaning that it represents the owl:Thing class
	    		return new ArrayList<String>(Arrays.asList("owl:Thing"));
	    	}
	    	// Else, its going to be the name of the relation
	    	else {
	    		String relationName = ":"+return_text;
	    		relationNames.add(relationName);
	    		return new ArrayList<String>(Arrays.asList(relationName));
	    	}
	    }
	    else {
		    ArrayList<String> ret = new ArrayList<String>();
		    //System.out.println(node.getLabel());
		    for (DLNode child:node.getChildren()) {
		    	ret.addAll(identifyConceptsRelationsAndMakeAxiom(child));
		    }
		    //System.out.println("text = "+text);
		    
		    // For the node whose children is a @ commencing variable,
		    // we would get a empty "( )" which we replace by nothing.
		    // For example, Pipe, labels etc.
		    // Those are going to be the Concept Names
		    if (ret.isEmpty()) {
		    	String conceptName = ":"+node.getLabel();
		    	conceptNames.add(conceptName);
		    	ret.add(conceptName);
		    }
		    else {
		    	String text = StringMappings.containsKey(node.getLabel())?StringMappings.get(node.getLabel()):node.getLabel();
		    	ret.add(0,"("); // prepend "(" to existing ret
		    	ret.add(0,text); // prepend text to the updated ret 
		    	ret.add(")"); // append ")" to the updated ret
	    	}
		    return ret;
	    }
	}
	
	
	public ArrayList<String> getAxiom() {
		return axiom;
	}
	
	public String getAxiomString() {
		String axiomString = "";
		for (String w:axiom) {
			axiomString = axiomString + w + " ";
		}
		return axiomString.trim();
	}


	public void doOntologyEnrichment(OntoModel ontoModel, String outputFolderName, boolean useRealReasoner) throws OWLOntologyStorageException {
		ontologyEnrichmentStatus = ontoModel.getOntologyEnrichmentRemarks(getAxiomString(), conceptNames, relationNames, outputFolderName, useRealReasoner);
	}
	
	public ArrayList<String> getOntologyEnrichmentStatus() {
		return ontologyEnrichmentStatus;
	}
	
	public void setOntologyEnrichmentStatusMessage(ArrayList<String> s) {
		ontologyEnrichmentStatus = s;
	}
	
	
 	public Set<String> getPropertyAssertions() {
		return propertyAssertions;
	}

	
	public Set<String> getConceptNames() {
		return conceptNames;
	}

	public Set<String> getRelationNames() {
		return relationNames;
	}

	
	
	
	
	
	/**
	 * Same as identifyConceptsRelationsAndMakeAxiom() except that the actual the axiom built will contain delexicalised ConceptNames and RelationNames -- i.e. Conceptnamae such as "Pipe" is represented by C1.
	 * This method was needed to prepare data for Deep Learning Seq2Seq model where I wanted to learn the axiom pattern for a given string rather than the actual string itself.
	 * Also, this method doesn't update any class member variables (unlike the identifyConceptsRelationsAndMakeAxiom() method which actually does); it just returns the arraylist of strings (some of which are 
	 * going to be delexicalised strings).
	 */
	private ArrayList<String> preorderTraversal_Delexicalised(DLNode node, LinkedHashMap<String,Integer> seenConcepts, LinkedHashMap<String,Integer> seenRelations) {
		/**
		 * The technique I will follow here is that the value stored for each key in the linkedhashmap will be the same as the index of that key in the linkedhashmap.
		 */
		// if the parent of this node was the terminal node, return
	    if(node == null) {
	    	return new ArrayList<String>();
	    }
	    
	    // if this node was the terminal node; this is 
	    // going to be the variable, like x and y and z;
	    // or things like identifiedarg2inv, identifiedarg3 etc.
	    // we only identifier for them. 
	    if (node.getLabel()==null) {
	    	String return_text = node.getIdentifier().trim();
	    	// Further, if it begins with @, it means that it was a variable like 
	    	// x and y, which we don't want to keep for DL semantics.
	    	if (return_text.toLowerCase().startsWith("@")) { 
	    		return new ArrayList<String>(); 
	    	}
	    	else if (return_text.equalsIgnoreCase("semtop")) { // There is a special case where the variable is "semtop" meaning that it represents the owl:Thing class
	    		return new ArrayList<String>(Arrays.asList("owl:Thing"));
	    	}
	    	// Else, its going to be the name of the relation
	    	else {
	    		String relationName = return_text;
	    		if (seenRelations.containsKey(relationName)) {
	    			return new ArrayList<String>(Arrays.asList(":R"+seenRelations.get(relationName)));
	    		}
	    		else {
	    			int newValue = seenRelations.size();
	    			seenRelations.put(relationName, newValue);
	    			return new ArrayList<String>(Arrays.asList(":R"+newValue));
	    		}
	    	}
	    }
	    else {
		    ArrayList<String> ret = new ArrayList<String>();
		    //System.out.println(node.getLabel());
		    for (DLNode child:node.getChildren()) {
		    	ret.addAll(preorderTraversal_Delexicalised(child, seenConcepts, seenRelations));
		    }
		    //System.out.println("text = "+text);
		    
		    // For the node whose children is a @ commencing variable,
		    // we would get a empty "( )" which we replace by nothing.
		    // For example, Pipe, labels etc.
		    // Those are going to be the Concept Names
		    if (ret.isEmpty()) {
		    	String conceptName = node.getLabel();
		    	if (seenConcepts.containsKey(conceptName)) {
		    		ret.add(":C"+seenConcepts.get(conceptName));
		    	}
		    	else {
		    		int newValue = seenConcepts.size();
		    		seenConcepts.put(conceptName, newValue);
		    		ret.add(":C"+newValue);
		    	}
		    }
		    else {
		    	String text = StringMappings.containsKey(node.getLabel())?StringMappings.get(node.getLabel()):node.getLabel();
		    	ret.add(0,"("); // prepend "(" to existing ret
		    	ret.add(0,text); // prepend text to the updated ret 
		    	ret.add(")"); // append ")" to the updated ret
	    	}
		    return ret;
	    }
	}
	/**
	 * Same as getAxiom() except that the actual ConceptNames and RelationNames are delexicalised -- i.e. Conceptnamae such as "Pipe" is represented by C1.
	 * This method was needed to prepare data for Deep Learning Seq2Seq model where I wanted to learn the axiom pattern for a given string rather than the actual string itself.
	 * @throws IOException 
	 */
	public ArrayList<String> getAxiom_DeLexicalised(String delexicalisedConceptVocab_FileName, String delexicalisedRelationVocab_FileName) throws IOException {
		LinkedHashMap<String, Integer> seenConcepts = new LinkedHashMap<String,Integer>();
		LinkedHashMap<String, Integer> seenRelations = new LinkedHashMap<String,Integer>();
		ArrayList<String> axiomDelexicalised = preorderTraversal_Delexicalised(rootNode, seenConcepts, seenRelations);
		
		// Before returning, I will also write the dexlicalisation vocab to a file
		FileOutputStream fos=new FileOutputStream(new File(delexicalisedConceptVocab_FileName));
		ObjectOutputStream oos=new ObjectOutputStream(fos);
		oos.writeObject(seenConcepts);
		oos.flush();
        oos.close();
        fos.close();
        FileOutputStream fos1=new FileOutputStream(new File(delexicalisedRelationVocab_FileName));
		ObjectOutputStream oos1=new ObjectOutputStream(fos1);
		oos1.writeObject(seenRelations);
		oos1.flush();
        oos1.close();
        fos1.close();
        
		return axiomDelexicalised;
	}
	
}
