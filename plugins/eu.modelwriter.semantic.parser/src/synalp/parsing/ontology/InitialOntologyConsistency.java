package synalp.parsing.ontology;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

//import synalp.parsing.utils.FileOperations;

/**
 * This class was used once to run experiments with the initial Airbus ontology,
 * so the code is not optimised.
 * It loads the original inconsistent ontology and builds a new ontology by adding
 * axioms of each type incrementally. That allows us to see at which step the reasoner
 * complains about inconsistency. It has separate loops for each type of axioms
 * so that they can be easily reordered and modified. We had to make different
 * combinations of the order in which axioms are loaded to the new ontology
 * as, in some combinations, the reasoner is heavily affected
 * (it slows down or runs out of memory).
 * @author Anastasia
 */


public class InitialOntologyConsistency {
	
		private static Logger log = Logger.getLogger(InitialOntologyConsistency.class.getName());
		private static String outputFolderName = "resources/airbus/output/consistency_checking/";
		
		
	    public static void main(String []args) throws Exception {
			// Configure the logger with handler and formatter
	        //FileOperations.createNewFolder(outputFolderName+"ontology_enrichment");
	    	FileHandler fh;
	        fh = new FileHandler(outputFolderName+"ontology_enrichment/enrichment.log");
	        log.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);
	        log.setUseParentHandlers(false); // remove the console handler
	        // LOG this level to the log and PUBLISH this level
	        log.setLevel(Level.FINER);
	        fh.setLevel(Level.FINER);
	        
		    //Load the input ontology
			OntoModel ontModel = new OntoModel(new File("resources/airbus/input/ontology/component-03072015_original.rdf"), outputFolderName);
			//ontModel.checkOntologyConsistency(ontModel.getOWLOntology());
			
			
			// We create a new ontology where we will put things from the Airbus ontology (gradually)
			File file = new File(outputFolderName + "airbus_PartialOntology_temp.rdf");
			IRI newOntologyIRI = IRI.create(file.toURI());
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLOntology ontology = manager.createOntology(newOntologyIRI);
			
			// Get axioms from Airbus
			Set<OWLAxiom> axioms = ontModel.getOntologyAxioms();
			
			ReasonerFactory factory = new Reasoner.ReasonerFactory() {
	            protected OWLReasoner createHermiTOWLReasoner(org.semanticweb.HermiT.Configuration configuration, OWLOntology ontology) {
	                // don't throw an exception since otherwise we cannot compute explanations 
	                configuration.throwInconsistentOntologyException = false;
	                return new Reasoner(configuration, ontology);
	            }  
	        };
	    	
	    	Configuration configuration = new Configuration();
	        configuration.throwInconsistentOntologyException = false;
	        configuration.ignoreUnsupportedDatatypes = true;
			// The factory can now be used to obtain an instance of HermiT as an OWLReasoner. 
	        OWLReasoner reasoner = factory.createReasoner(ontology, configuration);
			
			// Add axioms to the new ontology one by one.
	        // We separate axioms into classes so that we could see where the inconsistency occurs.
	        int countIter = 0;
	        Set<OWLAxiom> axiomsRemoved = new HashSet<OWLAxiom>();
			for (OWLAxiom axiom : axioms) {
				if (axiom.isOfType(AxiomType.DECLARATION)) {
					manager.addAxiom(ontology, axiom);
					countIter++;
					log.log(Level.FINER, "Axiom {0} was added: {1}", new Object[]{countIter, axiom});
					// As we use a buffering reasoner, we need to flush the buffer to see changes in ontology
					reasoner.flush();
					boolean consistency = reasoner.isConsistent();
					if (consistency == false) {
						log.log(Level.FINER, "Warning! The loaded ontology is not consistent");
						break;
					}
				}
			}
			
			for (OWLAxiom axiom : axioms) {
				if (axiom.isOfType(AxiomType.SUBCLASS_OF)) {
					manager.addAxiom(ontology, axiom);
					countIter++;
					log.log(Level.FINER, "Axiom {0} was added: {1}", new Object[]{countIter, axiom});
					// As we use a buffering reasoner, we need to flush the buffer to see changes in ontology
					reasoner.flush();
					boolean consistency = reasoner.isConsistent();
					if (consistency == false) {
						log.log(Level.FINER, "Warning! The loaded ontology is not consistent");
						break;
					}
				}
			}
			
			for (OWLAxiom axiom : axioms) {
				if (axiom.isOfType(AxiomType.DISJOINT_CLASSES)) {
					manager.addAxiom(ontology, axiom);
					countIter++;
					log.log(Level.FINER, "Axiom {0} was added: {1}", new Object[]{countIter, axiom});
					// As we use a buffering reasoner, we need to flush the buffer to see changes in ontology
					reasoner.flush();
					boolean consistency = reasoner.isConsistent();
					if (consistency == false) {
						log.log(Level.FINER, "Warning! The loaded ontology is not consistent");
						break;
					}
				}
			}

			for (OWLAxiom axiom : axioms) {
				if (axiom.isOfType(AxiomType.DATA_PROPERTY_ASSERTION) || axiom.isOfType(AxiomType.OBJECT_PROPERTY_ASSERTION)) {
					manager.addAxiom(ontology, axiom);
					countIter++;
					log.log(Level.FINER, "Axiom {0} was added: {1}", new Object[]{countIter, axiom});
					// As we use a buffering reasoner, we need to flush the buffer to see changes in ontology
					reasoner.flush();
					boolean consistency = reasoner.isConsistent();
					if (consistency == false) {
						log.log(Level.FINER, "Warning! The loaded ontology is not consistent");
						break;
					}
				}
			}
			
			// Data Property Axioms
			for (OWLAxiom axiom : axioms) {
				if (axiom.isOfType(AxiomType.SUB_DATA_PROPERTY) || axiom.isOfType(AxiomType.FUNCTIONAL_DATA_PROPERTY) || 
						axiom.isOfType(AxiomType.DATA_PROPERTY_DOMAIN) || axiom.isOfType(AxiomType.DATA_PROPERTY_RANGE)) {
					manager.addAxiom(ontology, axiom);
					countIter++;
					log.log(Level.FINER, "Axiom {0} was added: {1}", new Object[]{countIter, axiom});
					// As we use a buffering reasoner, we need to flush the buffer to see changes in ontology
					reasoner.flush();
					boolean consistency = reasoner.isConsistent();
					if (consistency == false) {
						log.log(Level.FINER, "Warning! The loaded ontology is not consistent");
						manager.removeAxiom(ontology, axiom);
						log.log(Level.FINER, "Remove axiom {0} from ontology", axiom);
						reasoner.flush();
						axiomsRemoved.add(axiom);
						//break;
					}
				}
			}
			
			// Object Property Axioms
			for (OWLAxiom axiom : axioms) {
				if (axiom.isOfType(AxiomType.SUB_OBJECT_PROPERTY) || axiom.isOfType(AxiomType.INVERSE_OBJECT_PROPERTIES) || 
						axiom.isOfType(AxiomType.FUNCTIONAL_OBJECT_PROPERTY) || axiom.isOfType(AxiomType.INVERSE_FUNCTIONAL_OBJECT_PROPERTY) || 
								axiom.isOfType(AxiomType.TRANSITIVE_OBJECT_PROPERTY) || axiom.isOfType(AxiomType.SYMMETRIC_OBJECT_PROPERTY)
								|| axiom.isOfType(AxiomType.OBJECT_PROPERTY_DOMAIN)) {
					manager.addAxiom(ontology, axiom);
					countIter++;
					log.log(Level.FINER, "Axiom {0} was added: {1}", new Object[]{countIter, axiom});
					// As we use a buffering reasoner, we need to flush the buffer to see changes in ontology
					reasoner.flush();
					boolean consistency = reasoner.isConsistent();
					if (consistency == false) {
						log.log(Level.FINER, "Warning! The loaded ontology is not consistent");
						break;
					}
				}
			}
			
			// Annotation Axioms
			for (OWLAxiom axiom : axioms) {
				if (axiom.isOfType(AxiomType.ANNOTATION_ASSERTION) || axiom.isOfType(AxiomType.ANNOTATION_PROPERTY_DOMAIN) || 
						axiom.isOfType(AxiomType.ANNOTATION_PROPERTY_RANGE)) {
					manager.addAxiom(ontology, axiom);
					countIter++;
					log.log(Level.FINER, "Axiom {0} was added: {1}", new Object[]{countIter, axiom});
					// As we use a buffering reasoner, we need to flush the buffer to see changes in ontology
					reasoner.flush();
					boolean consistency = reasoner.isConsistent();
					if (consistency == false) {
						log.log(Level.FINER, "Warning! The loaded ontology is not consistent");
						break;
					}
				}
			}
			
			// Datatype Definition and Sub annotation property Axioms
			for (OWLAxiom axiom : axioms) {
				if (axiom.isOfType(AxiomType.DATATYPE_DEFINITION) || axiom.isOfType(AxiomType.SUB_ANNOTATION_PROPERTY_OF)) {
					manager.addAxiom(ontology, axiom);
					countIter++;
					log.log(Level.FINER, "Axiom {0} was added: {1}", new Object[]{countIter, axiom});
					// As we use a buffering reasoner, we need to flush the buffer to see changes in ontology
					reasoner.flush();
					boolean consistency = reasoner.isConsistent();
					if (consistency == false) {
						log.log(Level.FINER, "Warning! The loaded ontology is not consistent");
						break;
					}
				}
			}
			
			for (OWLAxiom axiom : axioms) {
				if (axiom.isOfType(AxiomType.EQUIVALENT_CLASSES)) {
					manager.addAxiom(ontology, axiom);
					countIter++;
					log.log(Level.FINER, "Axiom {0} was added: {1}", new Object[]{countIter, axiom});
					// As we use a buffering reasoner, we need to flush the buffer to see changes in ontology
					reasoner.flush();
					boolean consistency = reasoner.isConsistent();
					if (consistency == false) {
						log.log(Level.FINER, "Warning! The loaded ontology is not consistent");
						manager.removeAxiom(ontology, axiom);
						log.log(Level.FINER, "Remove axiom {0} from ontology", axiom);
						reasoner.flush();
						axiomsRemoved.add(axiom);
						//break;
					}
				}
			}
			
			for (OWLAxiom axiom : axioms) {
				if (axiom.isOfType(AxiomType.CLASS_ASSERTION)) {
					manager.addAxiom(ontology, axiom);
					countIter++;
					log.log(Level.FINER, "Axiom {0} was added: {1}", new Object[]{countIter, axiom});
					// As we use a buffering reasoner, we need to flush the buffer to see changes in ontology
					reasoner.flush();
					boolean consistency = reasoner.isConsistent();
					if (consistency == false) {
						log.log(Level.FINER, "Warning! The loaded ontology is not consistent");
						manager.removeAxiom(ontology, axiom);
						log.log(Level.FINER, "Remove axiom {0} from ontology", axiom);
						reasoner.flush();
						axiomsRemoved.add(axiom);
						//break;
					}
				}
			}
			
			for (OWLAxiom axiom : axioms) {
				if (axiom.isOfType(AxiomType.OBJECT_PROPERTY_RANGE)) {
					manager.addAxiom(ontology, axiom);
					countIter++;
					log.log(Level.FINER, "Axiom {0} was added: {1}", new Object[]{countIter, axiom});
					// As we use a buffering reasoner, we need to flush the buffer to see changes in ontology
					reasoner.flush();
					boolean consistency = reasoner.isConsistent();
					if (consistency == false) {
						log.log(Level.FINER, "Warning! The loaded ontology is not consistent");
						manager.removeAxiom(ontology, axiom);
						log.log(Level.FINER, "Remove axiom {0} from ontology", axiom);
						reasoner.flush();
						axiomsRemoved.add(axiom);
						//break;
					}
				}
			}
			
			// 
			System.out.println("Count of removed axioms: " + axiomsRemoved.size());

			// Write deleted axioms to file
			String OntFileName = outputFolderName + "ontology_removedaxioms.txt";
			String out = new String();
			java.util.Iterator<OWLAxiom> it = axiomsRemoved.iterator();
			while(it.hasNext()) {
			    out += it.next() + "\n";
			}
			//FileOperations.writeToFile(new File(OntFileName), out);
			
			// Save ontology
			manager.saveOntology(ontology,  new RDFXMLOntologyFormat(),  newOntologyIRI);
			
			int axiomsCount = ontology.getAxiomCount();
			System.out.println("Count of axioms in new ontology: " + axiomsCount);
			
			int countAllClasses = 0;
	        for (OWLClass cls : ontology.getClassesInSignature()) {
	            countAllClasses += 1;
	        }
			System.out.println("Count of classes in new ontology: " + countAllClasses);
			
	    }
	   
	    
	    private static void compareOntologies() throws OWLException, UnsupportedEncodingException, FileNotFoundException, IOException {
	    	// Write the diff between two ontologies to file
	    	OntoModel ontModelOriginal = new OntoModel(new File("resources/airbus/input/ontology/component-03072015_original.rdf"), outputFolderName);
			Set<OWLAxiom> axiomsOriginal = ontModelOriginal.getOntologyAxioms();
			OntoModel ontModel = new OntoModel(new File("resources/airbus/input/ontology/component-03072015_changed.rdf"), outputFolderName);
			Set<OWLAxiom> axioms = ontModel.getOntologyAxioms();
			axioms.removeAll(axiomsOriginal);
			// Write axioms that appeared in the second ontology to file
			String OntFileName = outputFolderName + "new_axioms.txt";
			String out = new String();
			java.util.Iterator<OWLAxiom> it = axioms.iterator();
			while(it.hasNext()) {
			    out += it.next() + "\n";
			}
			//FileOperations.writeToFile(new File(OntFileName), out);
	    }
	    
	}

// Useful pieces of code:

// add the individual belonging to this existing class
// this is done to enable the consistency checking
// we add individuals to an existing class if it doesn't have other individuals
// don't set to true (direct instances of a class), takes A LOT MORE time than false
/*NodeSet<OWLNamedIndividual> individuals = reasoner.getInstances(cls, false);  // get indirect individuals, which belong to subclasses
if(individuals.isEmpty()) {
	if (!reasoner.isSatisfiable(cls)) {
		log.log(Level.INFO, "it is an unsatisfiable class: " + cls);
	}
	else {
		log.log(Level.INFO, "it is a satisfiable class: " + cls);
	}
	IRI individIRI = IRI.create(cls.getIRI() + "1");
	OWLIndividual newIndivid = dataFactory.getOWLNamedIndividual(individIRI);
	addClassAssertionAxiom(cls, newIndivid);
}
else {
	log.log(Level.FINER, "class has one or more individuals in the ontology");
}*/

// Compute explanations for inconsistent ontologies [need to pass ReasonerFactory as a parameter!]
/*System.out.println("Computing explanations for the inconsistency...");
BlackBoxExplanation exp = new BlackBoxExplanation(ontology, factory, reasoner);
HSTExplanationGenerator multExplanator = new HSTExplanationGenerator(exp);
// Now we can get explanations for the inconsistency 
Set<Set<OWLAxiom>> explanations = multExplanator.getExplanations(dataFactory.getOWLThing());
// Let us print them. Each explanation is one possible set of axioms that cause the 
// unsatisfiability. 
for (Set<OWLAxiom> explanation : explanations) {
    System.out.println("------------------");
    System.out.println("Axioms causing the inconsistency: ");
    for (OWLAxiom causingAxiom : explanation) {
        System.out.println(causingAxiom);
    }
    System.out.println("------------------");
}*/

		