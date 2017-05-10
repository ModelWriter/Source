package synalp.parsing.Inputs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import synalp.parsing.ParseResult;
import synalp.parsing.utils.FileOperations;

public class Batch {
	
	private ArrayList<Input> allInputs = new ArrayList<Input>();
	
	private double countParseSuccess;
	private double countParseCompleteSuccess;
	private double countParsePartialSuccess;
	private double countParseFailures;
	private double avgParsePerSuccess;
	private double avgParsePerTotal;
	
	private double countGenerationSuccess;
	private double countGenerationFailure;
	private double avgGenerationPerParseResult;
	
	private Long batchParseDuration;
	
	
	
	public Batch() {
		countParseSuccess = 0;
		countParseCompleteSuccess = 0;
		countParsePartialSuccess = 0;
		countParseFailures = 0;
		avgParsePerSuccess = 0;
		avgParsePerTotal = 0;
		
		
		countGenerationSuccess = 0;
		countGenerationFailure = 0;
		avgGenerationPerParseResult = 0;
		
		
		
		batchParseDuration = new Long(0);
	}
	
	public Batch(File file) throws IOException {
		this();
		for (String line:FileOperations.readUncommentedLines(file,'#')) {
			Input in = new Input(new Sentence(line));
			allInputs.add(in);
		}
	}
	
	public Batch(File file, int countProcessingLines) throws IOException {
		this();
		ArrayList<String> totalContent = FileOperations.readUncommentedLines(file,'#');
		if (countProcessingLines>totalContent.size()) {
			countProcessingLines=totalContent.size();
		}
		for (int i=0; i<countProcessingLines; i++) {
			Input in = new Input(new Sentence(totalContent.get(i)));
			allInputs.add(in);
		}
	}
	
	public ArrayList<Input> getInputs() {
		return allInputs;
	}
	
	
	public void computeStats() {
		double totalParseCount = 0;
		for (Input in:allInputs) {
			// Count of Parse Success and Failure
			if (in.isParseSuccess()) {
				countParseSuccess++;
				if (in.areResultsTypeComplete()) {
					countParseCompleteSuccess++;
				}
				else {
					countParsePartialSuccess++;
				}
				totalParseCount = totalParseCount + in.getParseCount();
			}
			else {
				countParseFailures++;
			}
			// Count of Generation Success and Failure -- Bit indirect because the given Input could have more than 1 parseResults and one or more of those parseResults could have one or more generation results
			boolean hasGeneration = false; // flag to indicate if any of the parse results obtained for this input could generate something.
			for (ParseResult p:in.getParseResults()) {
				ArrayList<String> genResults = p.getGeneratedSentences();
				avgGenerationPerParseResult = avgGenerationPerParseResult + genResults.size();
				if (!genResults.isEmpty()) {
					hasGeneration = true;
				}
			}
			if (hasGeneration) { // If any of the parseResults were able to generate anything
				countGenerationSuccess++;
			}
			else {
				countGenerationFailure++;
			}
		}
		if (totalParseCount>0) {
			avgParsePerSuccess = totalParseCount/countParseSuccess;
			avgParsePerTotal = totalParseCount/(allInputs.size());
			avgGenerationPerParseResult = avgGenerationPerParseResult/totalParseCount;
		}
	}
	
	public void setBatchParseDuration(Long duration) {
		batchParseDuration = duration;
	}
	
	public Long getBatchParseDuration() {
		return batchParseDuration;
	}
	
	public void writeStatsToFile(File statsFile) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		StringBuilder str = new StringBuilder();
		str.append("Nb. of Inputs = "+allInputs.size()+"\n\n");
		str.append("\n\nNb. of Parse Success = "+(int)countParseSuccess+"\n");
		str.append("\tComplete Parse Success = "+(int)countParseCompleteSuccess+"\n");
		str.append("\tPartial Parse Success = "+(int)countParsePartialSuccess+"\n");
		str.append("Nb. of Parse Failures = "+(int)countParseFailures+"\n\n");
		str.append("Average Parse per : \n");
		str.append("\tSuccessful Inputs : "+avgParsePerSuccess+"\n");
		str.append("\tTotal Inputs : "+avgParsePerTotal+"\n\n");
		str.append("\n\nNb. of Generation Success = "+countGenerationSuccess);
		str.append("\nNb. of Generation Failure = "+countGenerationFailure);
		str.append("\n\nAverage Generations per : \n");
		str.append("\tParse Result : "+avgGenerationPerParseResult);
		str.append("\n\n\n\n\nTotal Time taken for (batch) parsing ONLY =  "+batchParseDuration+ " seconds.\n\n");
		FileOperations.writeToFile(statsFile, str.toString());
	}
	
	public void writeSucccessSentencesToFile(File completeSuccessFile, File partialSuccessFile) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		StringBuilder strCompleteSuccess = new StringBuilder();
		StringBuilder strPartialSuccess = new StringBuilder();
		StringBuilder strPartialSuccessExplanation = new StringBuilder();
		
		for (Input in:allInputs) {
			if (in.isParseSuccess()) {
				if (in.areResultsTypeComplete()) {
					strCompleteSuccess.append(in.getSentence().getPlainSentence_AllWords()+"\n");
				}
				else {
					strPartialSuccess.append(in.getSentence().getPlainSentence_AllWords()+"\n");
					strPartialSuccessExplanation.append(in.getSentence().getPlainSentence_AllWords()+"\t\t[Cause : "+in.getParseMessage()+"]\n");
					strPartialSuccessExplanation.append("\tFor the partial parse result\n");
					for (int i=0;i<in.getParseCount();i++) {
						ParseResult result = in.getParseResults().get(i);
						strPartialSuccessExplanation.append("\t\t"+(i+1)+". "+result.getDerivationTree()+"\n");
						strPartialSuccessExplanation.append("\t\t\tMissing words = "+result.getMissingWordsinParseResult()+"\n\n");
					}
				}
			}
		}
		FileOperations.writeToFile(completeSuccessFile, strCompleteSuccess.toString());
		FileOperations.writeToFile(partialSuccessFile, strPartialSuccess.toString());
		
		String partialSuccessWithExplanationFileName = partialSuccessFile.getParentFile().getAbsolutePath()+"/"+"PartialSuccessSentences_Explanations.txt";
		FileOperations.writeToFile(new File(partialSuccessWithExplanationFileName), strPartialSuccessExplanation.toString());
	}
	
	public void writeFailureSentencesToFile(File failureFile) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		StringBuilder str = new StringBuilder();
		for (Input in:allInputs) {
			if (!in.isParseSuccess()) {
				str.append(in.getSentence().getPlainSentence_AllWords()+"\t\t[Cause : "+in.getParseMessage()+"]\n");
			}
		}
		FileOperations.writeToFile(failureFile, str.toString());
	}
	
	
	public void writeResultsToFile(File resultsFile) throws UnsupportedEncodingException, FileNotFoundException, IOException, OWLOntologyStorageException {
		StringBuilder batchResults = new StringBuilder();
		StringBuilder parseGenSentences = new StringBuilder();
		
		for (int i=0;i<allInputs.size();i++) {
			Input in = allInputs.get(i);
			batchResults.append((i+1)+". "+in.getSentence().getPlainSentence_AllWords()+"\t\tParse OK? : "+(in.getParseResults().size()>0?"Yes"+"\t\tComplete Parses? : "+(in.areResultsTypeComplete()?"Yes":"No")+"\t\tCount Results : "+in.getParseResults().size():"No")+"\n");
			parseGenSentences.append((i+1)+". "+in.getSentence().getPlainSentence_AllWords()+"\n");
			
			for (ParseResult p:in.getParseResults()) {
				batchResults.append("\n\n");
				batchResults.append("\tDerivation Tree = "+p.getDerivationTree()+"\n");
				batchResults.append("\tParsed Sentence = "+p.getSentenceofParseOutput()+"\n\n");
				batchResults.append("\tSemantics:["+p.getparseString()+"]\n");
				
				parseGenSentences.append("\tSemantics:["+p.getparseString()+"]\n");
				
				batchResults.append("\tDL semantics = "+p.getDLTree().getAxiomString()+"\n\n");
				batchResults.append("\tList of ConceptNames =  "+p.getDLTree().getConceptNames()+"\n");
				batchResults.append("\tList of RelationNames =  "+p.getDLTree().getRelationNames()+"\n\n");
				
				batchResults.append("\tOntology Enrichment Remarks = \n");
				for (String item:p.getDLTree().getOntologyEnrichmentStatus()) {
					batchResults.append("\t\t\t" + item + "\n");
				}
				
				batchResults.append("\n\tGeneration : \n");
				parseGenSentences.append("\t\t\tGeneration : \n");
				for (String x:p.getGeneratedSentences()) {
					batchResults.append("\t\t\t\t"+x+"\n");
					parseGenSentences.append("\t\t\t\t"+x+"\n");
				}
				batchResults.append("\n");
				parseGenSentences.append("\n");
			}
			batchResults.append("\n\n\n\n");
			parseGenSentences.append("\n\n\n\n");
		}
		FileOperations.writeToFile(resultsFile, batchResults.toString());
		
		String parseGenListFileName = resultsFile.getParentFile().getAbsolutePath()+"/"+"Parse-Gen_SentencesList.txt";
		FileOperations.writeToFile(new File(parseGenListFileName), parseGenSentences.toString());
	}
	
	
	public void writeBLUEFiles(File resultsFile) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		// Each of these files should be written inside separate uniquely named directory of its own. 
		// A reference file looks like the same as the source sentence except for few changes.
		// Since for each input, the files will be written inside a separate directory, the names of files can be generic. 
		
		String common_refSentence = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<!DOCTYPE mteval SYSTEM \"ftp://jaguar.ncsl.nist.gov/mt/resources/mteval-xml-v1.3.dtd\">\n"
				+ "<mteval>\n\n\n\n\n"
				+ "<!-- A reference section will begin with a <refset> tag which contains a set of documents -->\n"
				+ "<refset setid=\"source_sentence\" srclang=\"English\" trglang=\"English\" refid=\"reference_sentence\">  <!-- setid's value should match the name of the source xml file for which this the translation was carried out. -->\n\n"
				+ "<!-- Each document, defined by the <doc> tag, contains a set of segments  -->\n"
				+ "<doc docid=\"doc1\" genre=\"nw\"> <!-- docid value is the name identifying the document within the source xml file. -->\n\n"
				+ "<!-- Each segment, defined by the <seg> tag, contains a single reference text corresponding to the source sentence of this document -->\n"
				+ "<seg id=\"1\"> <!-- the id's value identifies this sentence as the reference sentence of the corresponding source sentence of this document -->\n"
				;
		
		String common_sourceSentence = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<!DOCTYPE mteval SYSTEM \"ftp://jaguar.ncsl.nist.gov/mt/resources/mteval-xml-v1.3.dtd\">\n"
				+ "<mteval>\n\n\n\n\n"
				+ "<!-- Although the source file is the same as the reference file in my case, the script still needs it. So, need to create one -->\n"
				+ "<!-- A single source_sentence.xml file will contain all the source sentences (can be more than 1) needed to be translated for all the documents. Here we have just 1 document containing just 1 source sentence -->\n\n\n\n"
				+ "<!-- A source section will begin with a <srcset> tag which contains a set of documents -->\n"
				+ "<srcset setid=\"source_sentence\" srclang=\"English\"> <!-- setid's value should match the name of the source xml file\n\n -->"
				+ "<!-- Each document, defined by the <doc> tag, contains a set of segments  -->\n"
				+ "<doc docid=\"doc1\" genre=\"nw\"> <!-- To each document, we give a unique identifier -->\n\n"
				+ "<!-- Each segment, defined by the <seg> tag, contains a distinct source sentence in this document. It is usual that there are more than 1 sentences in a document, each one being a different sentence. Here, in my case, I have only one source sentence per document and the source sentence is the same as the reference sentence. So, I copy the sentence from the reference file -->\n"
				+ "<seg id=\"1\"> <!-- unique number identifying this sentence in this document  -->\n"
				;
		
		String common_testSentence = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<!DOCTYPE mteval SYSTEM \"ftp://jaguar.ncsl.nist.gov/mt/resources/mteval-xml-v1.3.dtd\">\n"
				+ "<mteval>\n\n\n\n\n"
				+ "<!-- If there are multiple test sentences for the same source sentence (as in my case, there can be many generations for each parse input), need to create different files (with same value for docid and segid fields) for each of the test sentence -->\n\n\n\n"
				+ "<!-- A test section will begin with a <tstset> tag which identifies a set of test documents -->\n"
				+ "<tstset setid=\"source_sentence\" srclang=\"English\" trglang=\"English\" sysid=\"pjeni_application\"> <!-- setid's value should match the name of the source xml file for which this the translation was carried out. sysid contains the name of the system using it-->\n\n\n\n"
				+ "<!-- Each document, defined by the <doc> tag, contains a set of segments  -->\n"
				+ "<doc docid=\"doc1\" genre=\"nw\"> <!-- docid value is the name identifying the document within the source xml file. -->\n\n"
				+ "<!-- Each segment, defined by the <seg> tag, contains the translated text corresponding to the segment of the document described in the reference xml file. When there is no translation available for a reference sentence for the document described in the reference xml file, the segment has to be declared empty  -->\n"
				+ "<seg id=\"1\"> <!-- the id's value identifies this sentence as the translated sentence of the corresponding source sentence of this document -->\n"
				;
		
		
		// Write BLEU Files for all parses.
		// All = complete, partial and failures
		// Partial == Success but only partial
		// Complete = Success but only complete
		for (int i=0;i<allInputs.size();i++) {
			String allparses_outDirName =  resultsFile.getParentFile().getAbsolutePath()+"/BLUE_Files/all_parses/"+(i+1)+"/";
			String partialparsesOnly_outDirName =  resultsFile.getParentFile().getAbsolutePath()+"/BLUE_Files/partial_parses_only/"+(i+1)+"/";
			String completeparsesOnly_outDirName =  resultsFile.getParentFile().getAbsolutePath()+"/BLUE_Files/complete_parses_only/"+(i+1)+"/";
			
			Input in = allInputs.get(i);
			
			writeBLEUFilesForInput(in,allparses_outDirName,common_refSentence,common_sourceSentence,common_testSentence);
			
			if(in.areResultsTypeComplete()) {
				writeBLEUFilesForInput(in,completeparsesOnly_outDirName,common_refSentence,common_sourceSentence,common_testSentence);
			}
			if (!in.areResultsTypeComplete() && in.getParseResults().size()!=0) { // (partialparsesOnly = Those which are not complete and don't belong to the failure cases.)
				writeBLEUFilesForInput(in,partialparsesOnly_outDirName,common_refSentence,common_sourceSentence,common_testSentence);
			}
		}
		
	}

	private void writeBLEUFilesForInput(Input in, String outDirName, String common_refSentence, String common_sourceSentence, String common_testSentence) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		// There are certain characteds that must be escaped in xml
		String inputSentence = in.getSentence().getPlainSentence_AllWords();
		inputSentence = inputSentence.replace("&", "&amp;");
		
		// Write Reference Sentence File
		String refSentence = common_refSentence+inputSentence+"\n";
		refSentence = refSentence + "</seg>\n\n</doc>\n\n</refset>\n\n</mteval>";
		FileOperations.writeToFile(new File(outDirName+"reference_sentence.xml"),refSentence);
		
		// Write Source Sentence File
		String sourceSentence = common_sourceSentence+inputSentence+"\n";
		sourceSentence = sourceSentence + "</seg>\n\n</doc>\n\n</srcset>\n\n</mteval>";
		FileOperations.writeToFile(new File(outDirName+"source_sentence.xml"),sourceSentence);
	
		// Write the Test Sentences Files. Each of the test sentence will have to be stored in a separate file.
		for (int k=0; k<in.getParseResults().size();k++) {
			ParseResult p = in.getParseResults().get(k);
			for (int j=0;j<p.getGeneratedSentences().size();j++) {
				String sentence = p.getGeneratedSentences().get(j);
				// There are certain characteds that must be escaped in xml
				sentence = sentence.replace("&", "&amp;");
				
				String testSentence = common_testSentence+sentence+"\n";
				testSentence = testSentence + "</seg>\n\n</doc>\n\n</tstset>\n\n</mteval>";
				FileOperations.writeToFile(new File(outDirName+"test_sentence_"+k+"_parseresult_"+j+"_generation.xml"),testSentence);
			}
		}
	}
	
	
	public void writeNewLexicalItemsProposed(File newLexiconFile) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		Set<String> newLexicalItemsProposed = new HashSet<String>();
		for (Input x:allInputs) {
			newLexicalItemsProposed.addAll(x.getNewLexicalItemsProposed());
		}
		
		String newLexEntryPropositions = "";
		for (String proposition:newLexicalItemsProposed) {
			newLexEntryPropositions = newLexEntryPropositions + proposition + "\n\n";
		}
		// Write the instantiated lexicon to file
		FileOperations.writeToFile(newLexiconFile, "include macros.mac\n\n"+newLexEntryPropositions);	
	}


	public double getCountParseSuccess() {
		return countParseSuccess;
	}


	public double getCountParseFailures() {
		return countParseFailures;
	}


	public double getAvgParsePerSuccess() {
		return avgParsePerSuccess;
	}


	public double getAvgParsePerTotal() {
		return avgParsePerTotal;
	}


	public double getAvgGenerationPerParseResult() {
		return avgGenerationPerParseResult;
	}
	
	public double getCountParseCompleteSuccess() {
		return countParseCompleteSuccess;
	}
	
	public double getCountParsePartialSuccess() {
		return countParsePartialSuccess;
	}

	public int getInputsCount() {
		return allInputs.size();
	}
}
