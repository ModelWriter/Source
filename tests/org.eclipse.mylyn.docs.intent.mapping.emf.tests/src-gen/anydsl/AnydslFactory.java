/**
 */
package anydsl;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a create method for each non-abstract
 * class of the model. <!-- end-user-doc -->
 * 
 * @see anydsl.AnydslPackage
 * @generated
 */
public interface AnydslFactory extends EFactory {
	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	AnydslFactory eINSTANCE = anydsl.impl.AnydslFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>World</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>World</em>'.
	 * @generated
	 */
	World createWorld();

	/**
	 * Returns a new object of class '<em>Producer</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Producer</em>'.
	 * @generated
	 */
	Producer createProducer();

	/**
	 * Returns a new object of class '<em>Adress</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Adress</em>'.
	 * @generated
	 */
	Adress createAdress();

	/**
	 * Returns a new object of class '<em>Production Company</em>'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return a new object of class '<em>Production Company</em>'.
	 * @generated
	 */
	ProductionCompany createProductionCompany();

	/**
	 * Returns a new object of class '<em>Restaurant</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Restaurant</em>'.
	 * @generated
	 */
	Restaurant createRestaurant();

	/**
	 * Returns a new object of class '<em>Chef</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Chef</em>'.
	 * @generated
	 */
	Chef createChef();

	/**
	 * Returns a new object of class '<em>Recipe</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Recipe</em>'.
	 * @generated
	 */
	Recipe createRecipe();

	/**
	 * Returns a new object of class '<em>Food</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Food</em>'.
	 * @generated
	 */
	Food createFood();

	/**
	 * Returns a new object of class '<em>Plant</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Plant</em>'.
	 * @generated
	 */
	Plant createPlant();

	/**
	 * Returns a new object of class '<em>Animal</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Animal</em>'.
	 * @generated
	 */
	Animal createAnimal();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	AnydslPackage getAnydslPackage();

} // AnydslFactory
