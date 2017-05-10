package synalp.generation;

import java.util.*;

import synalp.commons.output.SyntacticRealization;
import synalp.commons.semantics.Semantics;
import synalp.generation.jeni.JeniChartItems;
import synalp.generation.selection.LexicalSelectionResult;


/**
 * A GenerationReport is an object produced while doing generation.
 * @author Alexandre Denis
 */
public class GenerationReport
{
	private long totalTime;
	private Date startTime;
	private String errorMessage;
	private Semantics originalInput;
	private JeniChartItems allItems;
	private JeniChartItems resultingItems;
	private LexicalSelectionResult lexicalSelection;
	private List<? extends SyntacticRealization> syntacticRealizations;


	/**
	 * @return the resultingItems
	 */
	public JeniChartItems getResultingItems()
	{
		return resultingItems;
	}


	/**
	 * @param resultingItems the resultingItems to set
	 */
	public void setResultingItems(JeniChartItems resultingItems)
	{
		this.resultingItems = resultingItems;
	}


	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage()
	{
		return errorMessage;
	}


	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}


	/**
	 * @return the totalTime
	 */
	public long getTotalTime()
	{
		return totalTime;
	}


	/**
	 * @param totalTime the totalTime to set
	 */
	public void setTotalTime(long totalTime)
	{
		this.totalTime = totalTime;
	}


	/**
	 * @return the startTime
	 */
	public Date getStartTime()
	{
		return startTime;
	}


	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}


	/**
	 * @return the originalInput
	 */
	public Semantics getOriginalInput()
	{
		return originalInput;
	}


	/**
	 * @param originalInput the originalInput to set
	 */
	public void setOriginalInput(Semantics originalInput)
	{
		this.originalInput = originalInput;
	}


	/**
	 * @return the lexicalSelection
	 */
	public LexicalSelectionResult getLexicalSelection()
	{
		return lexicalSelection;
	}


	/**
	 * @param lexicalSelection the lexicalSelection to set
	 */
	public void setLexicalSelection(LexicalSelectionResult lexicalSelection)
	{
		this.lexicalSelection = lexicalSelection;
	}


	/**
	 * @return the syntacticRealizations
	 */
	public List<? extends SyntacticRealization> getSyntacticRealizations()
	{
		return syntacticRealizations;
	}


	/**
	 * @param syntacticRealizations the syntacticRealizations to set
	 */
	public void setSyntacticRealizations(List<? extends SyntacticRealization> syntacticRealizations)
	{
		this.syntacticRealizations = syntacticRealizations;
	}


	/**
	 * @return the allItems
	 */
	public JeniChartItems getAllItems()
	{
		return allItems;
	}


	/**
	 * @param allItems the allItems to set
	 */
	public void setAllItems(JeniChartItems allItems)
	{
		this.allItems = allItems;
	}

}
