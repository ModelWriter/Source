/**
 */
package anydsl;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Recipe</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link anydsl.Recipe#getIngredients <em>Ingredients</em>}</li>
 * </ul>
 * </p>
 *
 * @see anydsl.AnydslPackage#getRecipe()
 * @model
 * @generated
 */
public interface Recipe extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Ingredients</b></em>' reference list. The list contents are of type
	 * {@link anydsl.Food}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ingredients</em>' reference list isn't clear, there really should be more of
	 * a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Ingredients</em>' reference list.
	 * @see anydsl.AnydslPackage#getRecipe_Ingredients()
	 * @model
	 * @generated
	 */
	EList<Food> getIngredients();

} // Recipe
