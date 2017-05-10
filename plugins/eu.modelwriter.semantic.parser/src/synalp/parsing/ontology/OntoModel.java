package synalp.parsing.ontology;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.search.EntitySearcher;

import synalp.parsing.utils.FileOperations;

/**
 * This module performs the ontology enrichment and consistency checking. Multi-word expressions must be
 * represented with the "_+_" symbol. For example, "spare wire" is "spare_+_wire".
 */

public class OntoModel {

	private OWLOntologyManager manager;

	private OWLOntology ontology;

	private OWLDataFactory dataFactory;

	private OWLReasoner reasoner;

	private Map<String, String> nameSpaces;

	private Map<String, String> entitiesToUri;

	private Map<String, String> rdfsLabels;

	private Map<String, String> prefAltLabels;

	private Set<OWLClass> initialClasses;

	private Set<OWLObjectProperty> initialProperties;

	private Set<OWLAxiom> initialAxioms;

	private int initialCountAxioms;

	private int countNewConcepts;

	private int countExistingConcepts;

	private int countNewRelations;

	private int countExistingRelations;

	private int countProcessedAxioms; // count axioms we get from the parsing step

	private int countNewAxioms;

	private int countNewAxiomsDeclIncl; // count all new axioms including declarations of classes and
										// properties

	private int countAxiomsRejectedSyntax;

	private int countAxiomsRejectedRedund; // count parsed axioms that we already added

	private int countAxiomsRejectedRedundInitOnt; // count axioms that were present in the initial ontology

	private int countAxiomsRejectedInconsistent; // count axioms that make the ontology inconsistent

	private int countClassAssertionAxioms; // count individuals

	private Set<String> newConcepts;

	private Set<String> existingConcepts;

	private Set<String> newRelations;

	private Set<String> existingRelations;

	private StringBuilder rejectedAxioms;

	private StringBuilder rejectedInconsistentAxioms;

	private StringBuilder newAxioms;

	private static Logger log = Logger.getLogger(OntoModel.class.getName());

	public OntoModel(File inputOntologyFile, String outputFolderName) throws OWLException {
		manager = OWLManager.createOWLOntologyManager();
		ontology = manager.loadOntologyFromOntologyDocument(inputOntologyFile);
		dataFactory = manager.getOWLDataFactory();
		findAllNameSpaces();
		getRdfsLabels();
		initialClasses = ontology.getClassesInSignature();
		initialProperties = ontology.getObjectPropertiesInSignature();
		initialAxioms = ontology.getAxioms();
		initialCountAxioms = ontology.getAxiomCount();
		countNewAxioms = 0;
		countNewAxiomsDeclIncl = 0;
		countProcessedAxioms = 0;
		countAxiomsRejectedSyntax = 0;
		countAxiomsRejectedRedund = 0;
		countAxiomsRejectedInconsistent = 0;
		countClassAssertionAxioms = 0;
		newConcepts = new HashSet<String>();
		existingConcepts = new HashSet<String>();
		newRelations = new HashSet<String>();
		existingRelations = new HashSet<String>();
		rejectedAxioms = new StringBuilder();
		rejectedInconsistentAxioms = new StringBuilder();
		newAxioms = new StringBuilder();

		ReasonerFactory factory = new Reasoner.ReasonerFactory() {
			protected OWLReasoner createHermiTOWLReasoner(org.semanticweb.HermiT.Configuration configuration,
					OWLOntology ontology) {
				// don't throw an exception since otherwise we cannot compute explanations
				configuration.throwInconsistentOntologyException = false;
				return new Reasoner(configuration, ontology);
			}
		};
		// ignore unsupported datatypes
		Configuration configuration = new Configuration();
		configuration.throwInconsistentOntologyException = false;
		configuration.ignoreUnsupportedDatatypes = true;
		// the factory can be used to obtain an instance of HermiT as an OWLReasoner
		reasoner = factory.createReasoner(ontology, configuration);

		// System.out.println("Data Factory = "+dataFactory);
		// System.out.println("Document IRI = "+manager.getOntologyDocumentIRI(ontology));
		// System.out.println("Number of axioms: " + ontology.getAxiomCount());
	}

	public void loggerConfiguration(String outputFolderName) throws SecurityException, IOException {
		// Configure the logger with handler and formatter
		FileOperations.createNewFolder(outputFolderName + "ontology_enrichment");
		FileHandler fh;
		fh = new FileHandler(outputFolderName + "ontology_enrichment/enrichment.log");
		log.addHandler(fh);
		SimpleFormatter formatter = new SimpleFormatter();
		fh.setFormatter(formatter);
		log.setUseParentHandlers(false); // remove the console handler
		// LOG this level to the log and PUBLISH this level
		log.setLevel(Level.FINER);
		fh.setLevel(Level.FINER);
	}

	// Couldn't find an existing implementation for this. So, for now listing it manually
	private void findAllNameSpaces() {
		nameSpaces = new HashMap<String, String>();
		nameSpaces.put("xmlns:acs", "http://airbus-group/aircraft-system#");
		nameSpaces.put("xmlns:evt", "http://airbus-group.installsys/event#");
		nameSpaces.put("xmlns:rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		nameSpaces.put("xmlns:spin", "http://spinrdf.org/spin#");
		nameSpaces.put("xmlns:qudt", "http://qudt.org/schema/qudt#");
		nameSpaces.put("xmlns:dct", "http://purl.org/dc/terms/");
		nameSpaces.put("xmlns:arg", "http://spinrdf.org/arg#");
		nameSpaces.put("xmlns:xsd", "http://www.w3.org/2001/XMLSchema#");
		nameSpaces.put("xmlns:vaem", "http://www.linkedmodel.org/schema/vaem#");
		nameSpaces.put("xmlns:skos", "http://www.w3.org/2004/02/skos/core#");
		nameSpaces.put("xmlns:voag", "http://voag.linkedmodel.org/voag/");
		nameSpaces.put("xmlns:comp", "http://airbus-group.installsys/component#");
		nameSpaces.put("xmlns:qudt-dimension", "http://qudt.org/vocab/dimension#");
		nameSpaces.put("xmlns:dc", "http://purl.org/dc/elements/1.1/");
		nameSpaces.put("xmlns:iems", "http://airbus-group/installationMeasure#");
		nameSpaces.put("xmlns:dtype", "http://www.linkedmodel.org/schema/dtype#");
		nameSpaces.put("xmlns:mat", "http://airbus-group/material#");
		nameSpaces.put("xmlns:sp", "http://spinrdf.org/sp#");
		nameSpaces.put("xmlns:qudt-quantity", "http://qudt.org/vocab/quantity#");
		nameSpaces.put("xmlns:owl", "http://www.w3.org/2002/07/owl#");
		nameSpaces.put("xmlns:ata", "http://airbus-group.ata#");
		nameSpaces.put("xmlns:spl", "http://spinrdf.org/spl#");
		nameSpaces.put("xmlns:rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		nameSpaces.put("xmlns:opdf", "http://airbus-group/opd-function#");
		nameSpaces.put("xmlns:unit", "http://qudt.org/vocab/unit#");
		nameSpaces.put("xml:base", "http://airbus-group.installsys/component#");
	}

	public OWLOntology getOWLOntology() {
		return ontology;
	}

	public Set<OWLClass> getAllConcepts() {
		return ontology.getClassesInSignature();
	}

	public IRI getOntologyIRI() {
		return manager.getOntologyDocumentIRI(ontology);
	}

	public Set<OWLAxiom> getOntologyAxioms() {
		return ontology.getAxioms();
	}

	public void getRdfsLabels() {
		rdfsLabels = new HashMap<String, String>();
		prefAltLabels = new HashMap<String, String>();
		Set<OWLClass> classes = getAllConcepts();
		String prefLabelIri = "<http://www.w3.org/2004/02/skos/core#prefLabel>";
		String altLabelIri = "<http://www.w3.org/2004/02/skos/core#altLabel>";
		for (OWLClass cls : classes) {
			for (OWLAnnotation a : EntitySearcher.getAnnotations(cls, ontology)) {
				// properties are of several types: rdfs-label, altLabel or prefLabel
				OWLAnnotationProperty prop = a.getProperty();
				OWLAnnotationValue val = a.getValue();
				if (val instanceof OWLLiteral) {
					// RDFS-labels
					if (prop.isLabel()) {
						// System.out.println(cls + " labelled " + ((OWLLiteral) val).getLiteral());
						// classes can have several rdfs labels
						rdfsLabels.put(((OWLLiteral)val).getLiteral(), cls.toString());
					}
					// preferred or alternative labels
					else if (prop.toString().equals(prefLabelIri) || prop.toString().equals(altLabelIri)) {
						// System.out.println(cls + " labelled (pref or alt) " + ((OWLLiteral)
						// val).getLiteral());
						// classes can have several labels
						prefAltLabels.put(((OWLLiteral)val).getLiteral(), cls.toString());
					}
				}
			}
		}
	}

	private void saveOntologyCopy(File file) throws OWLOntologyStorageException {
		IRI newOntologyIRI = IRI.create(file.toURI());
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		manager.saveOntology(ontology, new RDFXMLOntologyFormat(), newOntologyIRI);
	}

	private OWLClass getExistingConcept(String className) {
		for (String classNamewithCase : generateCaseCombinationsSimple(className)) {
			for (String nameSpace : nameSpaces.values()) {
				IRI fetchIRI = IRI.create(nameSpace + classNamewithCase);
				if (ontology.containsClassInSignature(fetchIRI)) {
					// consider classes as new if they are not present in the initial ontology
					if (initialClasses.contains(dataFactory.getOWLClass(fetchIRI))) {
						existingConcepts.add(fetchIRI.toString() + "\n");
					}
					countExistingConcepts += 1; // for a current axiom, take into account current state of the
												// ontology
					return dataFactory.getOWLClass(fetchIRI); // Settle on the first match found
				}
			}
		}
		return null; // return none if nothing matches.
	}

	private OWLProperty getExistingProperty(String relationName) {
		for (String relationNamewithCase : generateCaseCombinationsSimple(relationName)) {
			for (String nameSpace : nameSpaces.values()) {
				IRI fetchIRI = IRI.create(nameSpace + relationNamewithCase);
				if (ontology.containsDataPropertyInSignature(fetchIRI)) {
					// consider properties as new if they are not present in the initial ontology
					if (initialProperties.contains(dataFactory.getOWLObjectProperty(fetchIRI))) {
						existingRelations.add(fetchIRI.toString() + "\n");
					}
					countExistingRelations += 1; // for a current axiom, take into account current state of
													// the ontology
					return dataFactory.getOWLDataProperty(fetchIRI);
				}
				if (ontology.containsObjectPropertyInSignature(fetchIRI)) {
					// consider properties as new if they are not present in the initial ontology
					if (initialProperties.contains(dataFactory.getOWLObjectProperty(fetchIRI))) {
						existingRelations.add(fetchIRI.toString() + "\n");
					}
					countExistingRelations += 1;
					return dataFactory.getOWLObjectProperty(fetchIRI);
				}
			}
		}
		return null; // return null if nothing matches.
	}

	private ArrayList<String> generateCaseCombinationsSimple(String text) {
		// produce "pipe", "Pipe", "PIPE". For MWE, produce p-clamp, P-Clamp
		ArrayList<String> combinations = new ArrayList<String>();
		// deal with MWE
		// for multi-word concepts, try to replace spaces by underscores, hyphens or nothing
		Set<String> spaceReplacements = new HashSet<String>();
		spaceReplacements.add("");
		spaceReplacements.add("_");
		spaceReplacements.add("-");

		if (text.contains("_+_")) {
			String[] mweParts = text.split("_\\+_"); // need to escape in regex
			StringBuilder mweUpper = new StringBuilder();
			for (int i = 0; i < mweParts.length; i++) {
				// some numbers disappear: minimum_+__+_times_+_outer_+_bundle_+_diameter
				if (!mweParts[i].isEmpty()) {
					if (i != mweParts.length - 1) {
						mweUpper.append(mweParts[i].substring(0, 1).toUpperCase() + mweParts[i].substring(1)
								+ " "); // Sas Ramp Boom
					} else {
						mweUpper.append(mweParts[i].substring(0, 1).toUpperCase() + mweParts[i].substring(1)); // don't
																												// append
																												// space
					}
				}
			}
			combinations.add(mweUpper.toString().replace(" ", "")); // PClamp
			combinations.add(mweUpper.toString().replace(" ", "-")); // P-Clamp
			combinations.add(mweUpper.toString().replace(" ", "_")); // P_Clamp
			combinations.add(text.toLowerCase().replace("_+_", "")); // pclamp
			combinations.add(text.toLowerCase().replace("_+_", "-")); // p-clamp
			combinations.add(text.toLowerCase().replace("_+_", "_")); // p_clamp
		} else {
			combinations.add(text.toLowerCase());
			combinations.add(text.toUpperCase());
			combinations.add(text.substring(0, 1).toUpperCase() + text.substring(1));
		}
		return combinations;
	}

	// This method caused the exception "OutOfMemoryError: GC overhead limit exceeded"
	// Instead, we use another method: generateCaseCombinationsSimple
	private ArrayList<String> generateCaseCombinations(String text) {
		ArrayList<String> combinations = new ArrayList<String>();
		char[] chars = text.toCharArray();
		for (int i = 0, n = (int)Math.pow(2, chars.length); i < n; i++) {
			char[] permutation = new char[chars.length];
			for (int j = 0; j < chars.length; j++) {
				permutation[j] = (isBitSet(i, j)) ? Character.toUpperCase(chars[j]) : chars[j];
			}
			combinations.add(new String(permutation));
		}
		return combinations;
	}

	private boolean isBitSet(int n, int offset) {
		return (n >> offset & 1) != 0;
	}

	private OWLClass addNewClass(String className) {
		IRI classIRI = IRI.create(nameSpaces.get("xml:base") + className); // The new Concept to be made will
																			// always be made using the base
																			// uri
		OWLClass newClass = dataFactory.getOWLClass(classIRI);
		// In order to get any entity added to the ontology,
		// it must be used in some axiom included in the ontology.
		// In this example, that axiom is the class declaration axiom;
		OWLAxiom declareClass = dataFactory.getOWLDeclarationAxiom(newClass);
		addAxiom(declareClass);
		countNewConcepts += 1;
		newConcepts.add(newClass.toString() + "\n");
		// whenever we add a new class, we add also the individual belonging to this class,
		// the new individual will always be made using the base uri
		/*
		 * IRI individIRI = IRI.create(nameSpaces.get("xml:base") + className + "1"); OWLIndividual newIndivid
		 * = dataFactory.getOWLNamedIndividual(individIRI); addClassAssertionAxiom(newClass, newIndivid);
		 */
		return newClass;
	}

	private OWLObjectProperty addNewObjectProperty(String relationName) {
		IRI relationIRI = IRI.create(nameSpaces.get("xml:base") + relationName); // The new relation to be
																					// made will always be
																					// made using the base uri
		OWLObjectProperty newRelation = dataFactory.getOWLObjectProperty(relationIRI);
		// In order to get any entity added to the ontology,
		// it must be used in some axiom included in the ontology.
		// In this example, that axiom is the class declaration axiom;
		OWLAxiom declareRelation = dataFactory.getOWLDeclarationAxiom(newRelation);
		addAxiom(declareRelation);
		countNewRelations += 1;
		newRelations.add(newRelation.toString() + "\n");
		return newRelation;
	}

	private OWLAxiom getAxiomFromString(String axiomString) {
		// FIXME Bikash/Anastasia
		// InputStream in = new ByteArrayInputStream(axiomString.getBytes());
		// OWLFunctionalSyntaxParser parser = new OWLFunctionalSyntaxParser(in);
		// parser.setUp(ontology, new OWLOntologyLoaderConfiguration());
		// if (axiomString.startsWith("SubClassOf") || axiomString.startsWith("EquivalentClasses")) {
		// try { // parse only SubClassOf and EquivalentClasses axioms!
		// if (axiomString.startsWith("SubClassOf")) {
		// OWLAxiom axiom = parser.SubClassOf();
		// return axiom;
		// } else {
		// OWLAxiom axiom = parser.EquivalentClasses();
		// return axiom;
		// }
		// } catch (ParseException e) {
		// System.out.println("The axiom doesn't follow the functional owl syntax: " + axiomString + e
		// .getMessage());
		// log.log(Level.SEVERE, "The axiom doesn't follow the functional owl syntax: " + axiomString + e
		// .getMessage());
		// countAxiomsRejectedSyntax++;
		// rejectedAxioms.append(axiomString + "\n");
		// return null;
		// }
		// } else {
		// log.log(Level.INFO, "Currently we support only SubClassOf and EquivalentClasses axioms. "
		// + "The following type of the axiom is not supported: " + axiomString);
		// countAxiomsRejectedSyntax++;
		// rejectedAxioms.append(axiomString + "\n");
		// return null;
		// }
		return null;
	}

	private void addAxiom(OWLAxiom axiom) {
		AddAxiom addAxiom = new AddAxiom(ontology, axiom);
		manager.applyChange(addAxiom);
		newAxioms.append(axiom.toString() + "\n");
		countNewAxiomsDeclIncl++;
		log.log(Level.FINER, "The axiom was added. Count of axioms in the enriched ontology: " + ontology
				.getAxiomCount());
	}

	private void removeAxiom(OWLAxiom axiom) {
		// Remove axiom and its individual, if it makes the ontology inconsistent
		RemoveAxiom removeAxiom = new RemoveAxiom(ontology, axiom);
		manager.applyChange(removeAxiom);
		countAxiomsRejectedInconsistent++;
		rejectedInconsistentAxioms.append(axiom.toString() + "\n");
		log.log(Level.FINER, "The axiom was removed: " + axiom.toString());
		countNewAxiomsDeclIncl--;
		// TODO remove from newAxioms
	}

	private void addObjectPropertyAssertionAxiom(OWLIndividual subject, OWLObjectProperty predicate,
			OWLIndividual object) {
		// Sth. like Peter(Individual) hasFather Mark(Individual)
		OWLAxiom assertion = dataFactory.getOWLObjectPropertyAssertionAxiom(predicate, subject, object);
		addAxiom(assertion);
	}

	private void addClassAssertionAxiom(OWLClass classname, OWLIndividual individ) {
		// pipe1 (individual) belongs to the class Pipe
		OWLClassAssertionAxiom assertion = dataFactory.getOWLClassAssertionAxiom(classname, individ);
		AddAxiom addAxiom = new AddAxiom(ontology, assertion);
		manager.applyChange(addAxiom);
		countClassAssertionAxioms++;
		log.log(Level.FINER, "The class assertion axiom was added: " + assertion.toString());
	}

	private Object[] addConcepts(Set<String> conceptNames) {
		// search for concepts in the initial ontology
		// if they don't exist, create a new entity with the base URI
		List<String> newConceptsInAxiom = new ArrayList<String>();
		List<String> existingConceptsInAxiom = new ArrayList<String>();
		ConceptSearch cs;
		for (String conceptName : conceptNames) {
			String conceptNameNoColon = conceptName.replace(":", ""); // classes start with a colon, delete it
			OWLClass cls = getExistingConcept(conceptNameNoColon);
			if (cls == null) {
				// get lemma in case of one token, or get lemmas and remove determiners in case of MWE
				cs = ConceptSearch.getInstance();
				String conceptLemmatised = cs.morpholiser(conceptNameNoColon);
				OWLClass clsLemma = getExistingConcept(conceptLemmatised);
				if (clsLemma != null) {
					entitiesToUri.put(conceptNameNoColon, clsLemma.toString());
					log.log(Level.FINER, "lemmatised class exists in the ontology: " + conceptNameNoColon);
					existingConceptsInAxiom.add(clsLemma.toString());
				} else {
					// we don't find any existing class, so search in rdfs labels and create hypothetical
					// matchings
					cs.rdfsMatching(conceptLemmatised, rdfsLabels);
					cs.rdfsMatching(conceptLemmatised, prefAltLabels);
					// matching to superclasses
					cs.superClassMatching(conceptLemmatised, initialClasses);
					// deal with MWE
					if (conceptLemmatised.contains("_+_")) {
						String conceptNameMWE = conceptLemmatised.replace("_+_", "_");
						addNewClass(conceptNameMWE);
						String conceptNameMweUri = "<http://airbus-group.installsys/component#"
								+ conceptNameMWE + ">";
						entitiesToUri.put(conceptNameNoColon, conceptNameMweUri);
						log.log(Level.FINER,
								"add a new class (lemmatised) to the ontology: <http://airbus-group.installsys/component#"
										+ conceptNameMWE);
						newConceptsInAxiom.add(conceptNameMWE);
					} else {
						addNewClass(conceptLemmatised);
						String conceptLemmatisedUri = "<http://airbus-group.installsys/component#"
								+ conceptLemmatised + ">";
						entitiesToUri.put(conceptNameNoColon, conceptLemmatisedUri);
						log.log(Level.FINER,
								"add a new class (lemmatised) to the ontology: <http://airbus-group.installsys/component#"
										+ conceptLemmatised + ">");
						newConceptsInAxiom.add(conceptLemmatised);
					}
				}
			} else {
				entitiesToUri.put(conceptNameNoColon, cls.toString());
				log.log(Level.FINER, "class exists in the ontology: " + conceptNameNoColon);
				existingConceptsInAxiom.add(cls.toString());
				// add the individual belonging to this existing class
				// this is done to enable the consistency checking
				// we add individuals to an existing class if it doesn't have other individuals
				// don't set to true (direct instances of a class), takes A LOT MORE time than false
				/*
				 * NodeSet<OWLNamedIndividual> individuals = reasoner.getInstances(cls, false); // get
				 * indirect individuals, which belong to subclasses if(individuals.isEmpty()) { if
				 * (!reasoner.isSatisfiable(cls)) { log.log(Level.INFO, "it is an unsatisfiable class: " +
				 * cls); } else { log.log(Level.INFO, "it is a satisfiable class: " + cls); } IRI individIRI =
				 * IRI.create(cls.getIRI() + "1"); OWLIndividual newIndivid =
				 * dataFactory.getOWLNamedIndividual(individIRI); addClassAssertionAxiom(cls, newIndivid); }
				 * else { log.log(Level.FINER, "class has one or more individuals in the ontology"); }
				 */
			}
		}
		return new Object[] {existingConceptsInAxiom, newConceptsInAxiom };
	}

	private Object[] addRelations(Set<String> relationNames) {
		// search for relations and add them if they don't exist
		List<String> newRelationsInAxiom = new ArrayList<String>();
		List<String> existingRelationsInAxiom = new ArrayList<String>();
		for (String relationName : relationNames) {
			String relationNameNoColon = relationName.replace(":", "");
			// disallow MWE, but keep track of the original relation in entitiesToUri
			String relationNameNoMwe = relationNameNoColon.replace("_+_", "_");
			OWLProperty property = getExistingProperty(relationNameNoMwe);
			if (property == null) {
				addNewObjectProperty(relationNameNoMwe);
				entitiesToUri.put(relationNameNoColon, "<http://airbus-group.installsys/component#"
						+ relationNameNoMwe + ">");
				log.log(Level.FINER,
						"add a new property to the ontology: <http://airbus-group.installsys/component#"
								+ relationNameNoMwe + ">");
				newRelationsInAxiom.add(relationNameNoMwe);
			} else {
				entitiesToUri.put(relationNameNoColon, property.toString());
				existingRelationsInAxiom.add(relationNameNoMwe);
			}
		}
		return new Object[] {existingRelationsInAxiom, newRelationsInAxiom };
	}

	public ArrayList<String> getOntologyEnrichmentRemarks(String axiomStringNoUri, Set<String> conceptNames,
			Set<String> relationNames, String outputFolderName, boolean useRealReasoner)
			throws OWLOntologyStorageException {
		ArrayList<String> message = new ArrayList<String>();
		countProcessedAxioms++;
		log.log(Level.FINER, "#### Ontology Enrichment: handling axiom " + countProcessedAxioms + " ####");
		log.log(Level.FINER, "axiom " + countProcessedAxioms + " from parsing: " + axiomStringNoUri);
		countNewConcepts = 0;
		countExistingConcepts = 0;
		countNewRelations = 0;
		countExistingRelations = 0;
		// StringBuilder message = new StringBuilder();
		// store entity names and their URIs: Pipe - <http://airbus-group.installsys/component#Pipe>
		// (couldn't find implementation for that in owl api (maybe PrefixManager??))
		entitiesToUri = new HashMap<String, String>();
		log.log(Level.FINER, "adding concepts...");
		Object[] concepts = addConcepts(conceptNames);
		log.log(Level.FINER, "adding relations...");
		Object[] relations = addRelations(relationNames);
		log.log(Level.FINER, "getting axiom form with uri...");
		// Replace short forms of entities by their full forms with URI
		String axiomString = axiomStringNoUri;
		for (String entityName : entitiesToUri.keySet()) {
			axiomString = axiomString.replace(":" + entityName + " ", entitiesToUri.get(entityName) + " ");
			// log
		}
		log.log(Level.FINER, "axiom with uri: " + axiomString);
		log.log(Level.FINER, "getting list of current axioms...");
		// Add axiom if it isn't present in the ontology
		Set<OWLAxiom> axioms = getOntologyAxioms();
		OWLAxiom axiom = getAxiomFromString(axiomString);
		message.add(countNewConcepts + " new class(es) added: " + concepts[1].toString());
		message.add(countNewRelations + " new relation(s) added: " + relations[1].toString());
		message.add(countExistingConcepts + " old class(es) detected: " + concepts[0].toString());
		message.add(countExistingRelations + " old relation(s) detected: " + relations[0].toString());

		if (axiom != null) {
			if (!axioms.contains(axiom)) {
				addAxiom(axiom);
				countNewAxioms++;
				message.add("The SIDP axiom was added.");
			} else {// check if the axiom was present in the initial ontology
				if (initialAxioms.contains(axiom)) {
					log.log(Level.FINER, "The SIDP axiom exists in the initial ontology: " + axiom
							.toString());
					countAxiomsRejectedRedundInitOnt++;
					message.add("The SIDP axiom exists in the initial ontology.");
					return message;
				} else {
					log.log(Level.FINER, "The SIDP axiom was already added (i.e. it is in the ontology): "
							+ axiom.toString());
					countAxiomsRejectedRedund++;
					message.add("The SIDP axiom was already added.");
					return message;
				}
			}
		} else {
			message.add("The SIDP axiom was not added.");
			return message;
		}
		// save the enriched ontology
		File file = new File(outputFolderName
				+ "ontology_enrichment/component-03072015_changed_enriched.rdf");
		saveOntologyCopy(file);
		// Use a real reasoner (will slow down the pipeline! ca 1 sec per a parsed axiom)
		if (useRealReasoner) {
			log.log(Level.FINER, "checking consistency and satisfiability...");
			reasoner.flush();
			boolean consistency = checkOntologyConsistency(ontology, concepts);
			// if not consistent, remove axiom; otherwise, save the ontology
			if (consistency) {
				message.add("The ontology is consistent, and it has no unsatisfiable classes.");
			} else {
				removeAxiom(axiom);
				reasoner.flush();
				countNewAxioms--;
				message.add("Later it was removed, as it caused inconsistency/unsatisfiability.");
				boolean consistencyDoubleCheck = checkOntologyConsistency(ontology, concepts);
				if (!consistencyDoubleCheck) {
					System.out.println(
							"Warning! The removed axiom was not removed properly, and the inconsistency/unsatisfiability issue is still present!");
					log.log(Level.FINER,
							"Warning! The removed axiom was not removed properly, and the inconsistency/unsatisfiability issue is still present!");
				}
			}
		}
		// for all new concepts, give suggestions for superclasses
		if (countNewConcepts > 0) {
			ConceptSearch cs = ConceptSearch.getInstance();
			message.add("Suggestions for superclasses:");
			@SuppressWarnings("unchecked")
			ArrayList<String> newConcepts = (ArrayList<String>)concepts[1];
			for (String concept : newConcepts) {
				ArrayList<String> suggestions = cs.getSuggestions(concept);
				if (suggestions.isEmpty()) {
					message.add(concept + ": no suggestions found.");
				} else {
					message.add(concept + ": " + suggestions.toString());
				}
			}
		}
		return message;
	}

	public void writeOutputToFiles(String outputFolderName) throws UnsupportedEncodingException,
			FileNotFoundException, IOException {
		// write output of ConceptSearch class: matching to RDFS-labels, partial matching of RDFS-labels and
		// hypothetical superclasses
		ConceptSearch cs = ConceptSearch.getInstance();
		cs.writeOutput(outputFolderName);
		// write output of OntoModel
		File conceptsFile = new File(outputFolderName + "ontology_enrichment/new_classes.txt");
		File relationsFile = new File(outputFolderName + "ontology_enrichment/new_relations.txt");
		File existingConceptsFile = new File(outputFolderName + "ontology_enrichment/existing_classes.txt");
		File existingRelationsFile = new File(outputFolderName
				+ "ontology_enrichment/existing_relations.txt");
		File newAxiomsFile = new File(outputFolderName + "ontology_enrichment/new_axioms.txt");
		File rejectedAxiomsFile = new File(outputFolderName + "ontology_enrichment/rejected_axioms.txt");
		File rejectedInconsistentAxiomsFile = new File(outputFolderName
				+ "ontology_enrichment/rejected_inconsistent_axioms.txt");
		File statsFile = new File(outputFolderName + "ontology_enrichment/stats.txt");
		StringBuilder str = new StringBuilder();
		str.append(
				"Numbers of new classes/relations are given without repetition and in comparison to the initial ontology.\n\n");
		str.append("Nb. of new classes = " + newConcepts.size() + "\n");
		str.append("\tNb. of superclasses found = " + cs.getSizeSuperClassMatching() + "\n");
		str.append("\tNb. of RDFS-label matches (incl. altLabel and prefLabel) = " + cs.getSizeMatching()
				+ "\n");
		str.append("Nb. of existing classes = " + existingConcepts.size() + "\n\n");
		str.append("Nb. of new object properties = " + newRelations.size() + "\n");
		str.append("Nb. of existing object properties = " + existingRelations.size() + "\n\n");
		str.append("Nb. of new class assertion axioms (new individuals) = " + countClassAssertionAxioms
				+ "\n\n");
		str.append("Nb. of processed SIDP axioms after parsing = " + countProcessedAxioms + "\n");
		str.append("\tNb. of new SIDP axioms = " + countNewAxioms + "\n");
		str.append("\tNb. of rejected SIDP axioms due to syntax errors = " + countAxiomsRejectedSyntax
				+ "\n");
		str.append("\tNb. of rejected SIDP axioms due to the redundancy = " + countAxiomsRejectedRedund
				+ "\n");
		str.append("\tNb. of rejected SIDP axioms due to their presence in the initial ontology = "
				+ countAxiomsRejectedRedundInitOnt + "\n");
		str.append("\tNb. of rejected SIDP axioms that caused the inconsistency/unsatisfiability = "
				+ countAxiomsRejectedInconsistent + "\n\n");
		str.append("====Summary====\n");
		str.append("The initial ontology contains " + initialCountAxioms + " axioms.\n");
		str.append(countNewAxiomsDeclIncl
				+ " axioms were added (including declaration axioms, excluding individuals).\n");
		str.append("The enriched ontology contains " + ontology.getAxiomCount()
				+ " axioms in total (with individuals).\n");
		FileOperations.writeToFile(conceptsFile, newConcepts.toString());
		FileOperations.writeToFile(relationsFile, newRelations.toString());
		FileOperations.writeToFile(existingConceptsFile, existingConcepts.toString());
		FileOperations.writeToFile(existingRelationsFile, existingRelations.toString());
		FileOperations.writeToFile(newAxiomsFile, newAxioms.toString());
		FileOperations.writeToFile(rejectedAxiomsFile, rejectedAxioms.toString());
		FileOperations.writeToFile(rejectedInconsistentAxiomsFile, rejectedInconsistentAxioms.toString());
		FileOperations.writeToFile(statsFile, str.toString());
		// System.out.println(prefAltLabels.size());
	}

	public boolean checkOntologyConsistency(OWLOntology ontology, Object[] concepts) {
		// Ask the reasoner to precompute some inferences (takes ca 10 min)
		// reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
		// Node<OWLClass> unsatClasses = reasoner.getUnsatisfiableClasses();
		// System.out.println("unsatisfiable classes: " + unsatClasses.getSize());
		// System.out.println(unsatClasses);
		/*
		 * if (unsatClasses.getSize() > 1) {
		 * System.out.println("Warning! The ontology has unsatisfiable classes."); log.log(Level.WARNING,
		 * "Warning! The ontology has unsatisfiable classes: " + unsatClasses); } else {
		 * log.log(Level.WARNING, "The ontology has no unsatisfiable classes"); }
		 */
		boolean consistency = true;

		// check if all classes in the SIDP axiom are satisfiable
		for (int i = 0; i < 2; i++) {
			@SuppressWarnings("unchecked")
			ArrayList<String> newConcepts = (ArrayList<String>)concepts[i];
			for (String cls : newConcepts) {
				// list of new classes doesn't have uris
				String clsUri;
				if (i == 1) {
					clsUri = "http://airbus-group.installsys/component#" + cls;
				} else {
					clsUri = cls.substring(1, cls.length() - 1); // cut <> -- the last and the first chars
				}
				OWLClass owlCls = dataFactory.getOWLClass(IRI.create(clsUri));
				if (!reasoner.isSatisfiable(owlCls)) {
					System.out.println("Warning! The ontology has unsatisfiable classes.");
					log.log(Level.WARNING, "Warning! The ontology has an unsatisfiable class: " + owlCls);
					return false;
				} else {
					log.log(Level.FINER, "The following class is satisfiable: " + owlCls);
				}
			}
		}
		// Determine if the ontology is actually consistent
		consistency = reasoner.isConsistent();
		if (!consistency) {
			System.out.println("Warning! The loaded ontology is not consistent");
			log.log(Level.WARNING, "Warning! The loaded ontology is not consistent");
		} else
			log.log(Level.FINER, "The loaded ontology is consistent");
		return consistency;
	}

}
