/**
 *  Copyright (c) 2015 Obeo.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 * 
 *  Contributors:
 *     Obeo - initial API and implementation and/or initial documentation
 *     ...
 * 
 */
package org.eclipse.mylyn.docs.intent.mapping.ide;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.mylyn.docs.intent.mapping.MappingPackage;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains accessors for the meta objects to
 * represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each operation of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * 
 * @see org.eclipse.mylyn.docs.intent.mapping.ide.IdeFactory
 * @model kind="package"
 * @generated
 */
public interface IdePackage extends EPackage {
	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "ide";

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://www.modelwriter.eu/mapping/ide";

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "ide";

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	IdePackage eINSTANCE = org.eclipse.mylyn.docs.intent.mapping.ide.impl.IdePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.mylyn.docs.intent.mapping.ide.impl.ResourceLocationImpl
	 * <em>Resource Location</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.impl.ResourceLocationImpl
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.impl.IdePackageImpl#getResourceLocation()
	 * @generated
	 */
	int RESOURCE_LOCATION = 0;

	/**
	 * The feature id for the '<em><b>Contents</b></em>' containment reference list. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RESOURCE_LOCATION__CONTENTS = MappingPackage.LOCATION__CONTENTS;

	/**
	 * The feature id for the '<em><b>Source Links</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RESOURCE_LOCATION__SOURCE_LINKS = MappingPackage.LOCATION__SOURCE_LINKS;

	/**
	 * The feature id for the '<em><b>Target Links</b></em>' containment reference list. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RESOURCE_LOCATION__TARGET_LINKS = MappingPackage.LOCATION__TARGET_LINKS;

	/**
	 * The feature id for the '<em><b>Container</b></em>' container reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RESOURCE_LOCATION__CONTAINER = MappingPackage.LOCATION__CONTAINER;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RESOURCE_LOCATION__TYPE = MappingPackage.LOCATION__TYPE;

	/**
	 * The feature id for the '<em><b>Marked As Deleted</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RESOURCE_LOCATION__MARKED_AS_DELETED = MappingPackage.LOCATION__MARKED_AS_DELETED;

	/**
	 * The feature id for the '<em><b>Full Path</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RESOURCE_LOCATION__FULL_PATH = MappingPackage.LOCATION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Resource Location</em>' class. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RESOURCE_LOCATION_FEATURE_COUNT = MappingPackage.LOCATION_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Resource Location</em>' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RESOURCE_LOCATION_OPERATION_COUNT = MappingPackage.LOCATION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.mylyn.docs.intent.mapping.ide.impl.FileLocationImpl
	 * <em>File Location</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.impl.FileLocationImpl
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.impl.IdePackageImpl#getFileLocation()
	 * @generated
	 */
	int FILE_LOCATION = 1;

	/**
	 * The feature id for the '<em><b>Contents</b></em>' containment reference list. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FILE_LOCATION__CONTENTS = RESOURCE_LOCATION__CONTENTS;

	/**
	 * The feature id for the '<em><b>Source Links</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FILE_LOCATION__SOURCE_LINKS = RESOURCE_LOCATION__SOURCE_LINKS;

	/**
	 * The feature id for the '<em><b>Target Links</b></em>' containment reference list. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FILE_LOCATION__TARGET_LINKS = RESOURCE_LOCATION__TARGET_LINKS;

	/**
	 * The feature id for the '<em><b>Container</b></em>' container reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FILE_LOCATION__CONTAINER = RESOURCE_LOCATION__CONTAINER;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FILE_LOCATION__TYPE = RESOURCE_LOCATION__TYPE;

	/**
	 * The feature id for the '<em><b>Marked As Deleted</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FILE_LOCATION__MARKED_AS_DELETED = RESOURCE_LOCATION__MARKED_AS_DELETED;

	/**
	 * The feature id for the '<em><b>Full Path</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FILE_LOCATION__FULL_PATH = RESOURCE_LOCATION__FULL_PATH;

	/**
	 * The number of structural features of the '<em>File Location</em>' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FILE_LOCATION_FEATURE_COUNT = RESOURCE_LOCATION_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>File Location</em>' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FILE_LOCATION_OPERATION_COUNT = RESOURCE_LOCATION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.mylyn.docs.intent.mapping.ide.impl.TextFileLocationImpl
	 * <em>Text File Location</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.impl.TextFileLocationImpl
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.impl.IdePackageImpl#getTextFileLocation()
	 * @generated
	 */
	int TEXT_FILE_LOCATION = 2;

	/**
	 * The feature id for the '<em><b>Contents</b></em>' containment reference list. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TEXT_FILE_LOCATION__CONTENTS = FILE_LOCATION__CONTENTS;

	/**
	 * The feature id for the '<em><b>Source Links</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TEXT_FILE_LOCATION__SOURCE_LINKS = FILE_LOCATION__SOURCE_LINKS;

	/**
	 * The feature id for the '<em><b>Target Links</b></em>' containment reference list. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TEXT_FILE_LOCATION__TARGET_LINKS = FILE_LOCATION__TARGET_LINKS;

	/**
	 * The feature id for the '<em><b>Container</b></em>' container reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TEXT_FILE_LOCATION__CONTAINER = FILE_LOCATION__CONTAINER;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TEXT_FILE_LOCATION__TYPE = FILE_LOCATION__TYPE;

	/**
	 * The feature id for the '<em><b>Marked As Deleted</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TEXT_FILE_LOCATION__MARKED_AS_DELETED = FILE_LOCATION__MARKED_AS_DELETED;

	/**
	 * The feature id for the '<em><b>Full Path</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TEXT_FILE_LOCATION__FULL_PATH = FILE_LOCATION__FULL_PATH;

	/**
	 * The feature id for the '<em><b>Text</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TEXT_FILE_LOCATION__TEXT = FILE_LOCATION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Text File Location</em>' class. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TEXT_FILE_LOCATION_FEATURE_COUNT = FILE_LOCATION_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Text File Location</em>' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TEXT_FILE_LOCATION_OPERATION_COUNT = FILE_LOCATION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.mylyn.docs.intent.mapping.ide.impl.EObjectFileLocationImpl <em>EObject File
	 * Location</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.impl.EObjectFileLocationImpl
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.impl.IdePackageImpl#getEObjectFileLocation()
	 * @generated
	 */
	int EOBJECT_FILE_LOCATION = 3;

	/**
	 * The feature id for the '<em><b>Contents</b></em>' containment reference list. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EOBJECT_FILE_LOCATION__CONTENTS = FILE_LOCATION__CONTENTS;

	/**
	 * The feature id for the '<em><b>Source Links</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EOBJECT_FILE_LOCATION__SOURCE_LINKS = FILE_LOCATION__SOURCE_LINKS;

	/**
	 * The feature id for the '<em><b>Target Links</b></em>' containment reference list. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EOBJECT_FILE_LOCATION__TARGET_LINKS = FILE_LOCATION__TARGET_LINKS;

	/**
	 * The feature id for the '<em><b>Container</b></em>' container reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EOBJECT_FILE_LOCATION__CONTAINER = FILE_LOCATION__CONTAINER;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EOBJECT_FILE_LOCATION__TYPE = FILE_LOCATION__TYPE;

	/**
	 * The feature id for the '<em><b>Marked As Deleted</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EOBJECT_FILE_LOCATION__MARKED_AS_DELETED = FILE_LOCATION__MARKED_AS_DELETED;

	/**
	 * The feature id for the '<em><b>Full Path</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EOBJECT_FILE_LOCATION__FULL_PATH = FILE_LOCATION__FULL_PATH;

	/**
	 * The feature id for the '<em><b>XMI Content</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EOBJECT_FILE_LOCATION__XMI_CONTENT = FILE_LOCATION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>EObject File Location</em>' class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EOBJECT_FILE_LOCATION_FEATURE_COUNT = FILE_LOCATION_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>EObject File Location</em>' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EOBJECT_FILE_LOCATION_OPERATION_COUNT = FILE_LOCATION_OPERATION_COUNT + 0;

	/**
	 * Returns the meta object for class '{@link org.eclipse.mylyn.docs.intent.mapping.ide.ResourceLocation
	 * <em>Resource Location</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Resource Location</em>'.
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.ResourceLocation
	 * @generated
	 */
	EClass getResourceLocation();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.mylyn.docs.intent.mapping.ide.ResourceLocation#getFullPath <em>Full Path</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Full Path</em>'.
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.ResourceLocation#getFullPath()
	 * @see #getResourceLocation()
	 * @generated
	 */
	EAttribute getResourceLocation_FullPath();

	/**
	 * Returns the meta object for class '{@link org.eclipse.mylyn.docs.intent.mapping.ide.FileLocation
	 * <em>File Location</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>File Location</em>'.
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.FileLocation
	 * @generated
	 */
	EClass getFileLocation();

	/**
	 * Returns the meta object for class '{@link org.eclipse.mylyn.docs.intent.mapping.ide.TextFileLocation
	 * <em>Text File Location</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Text File Location</em>'.
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.TextFileLocation
	 * @generated
	 */
	EClass getTextFileLocation();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.eclipse.mylyn.docs.intent.mapping.ide.TextFileLocation#getText <em>Text</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Text</em>'.
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.TextFileLocation#getText()
	 * @see #getTextFileLocation()
	 * @generated
	 */
	EAttribute getTextFileLocation_Text();

	/**
	 * Returns the meta object for class '{@link org.eclipse.mylyn.docs.intent.mapping.ide.EObjectFileLocation
	 * <em>EObject File Location</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>EObject File Location</em>'.
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.EObjectFileLocation
	 * @generated
	 */
	EClass getEObjectFileLocation();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.mylyn.docs.intent.mapping.ide.EObjectFileLocation#getXMIContent <em>XMI
	 * Content</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>XMI Content</em>'.
	 * @see org.eclipse.mylyn.docs.intent.mapping.ide.EObjectFileLocation#getXMIContent()
	 * @see #getEObjectFileLocation()
	 * @generated
	 */
	EAttribute getEObjectFileLocation_XMIContent();

	/**
	 * Returns the factory that creates the instances of the model. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	IdeFactory getIdeFactory();

	/**
	 * <!-- begin-user-doc --> Defines literals for the meta objects that represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each operation of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.mylyn.docs.intent.mapping.ide.impl.ResourceLocationImpl <em>Resource
		 * Location</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.mylyn.docs.intent.mapping.ide.impl.ResourceLocationImpl
		 * @see org.eclipse.mylyn.docs.intent.mapping.ide.impl.IdePackageImpl#getResourceLocation()
		 * @generated
		 */
		EClass RESOURCE_LOCATION = eINSTANCE.getResourceLocation();

		/**
		 * The meta object literal for the '<em><b>Full Path</b></em>' attribute feature. <!-- begin-user-doc
		 * --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute RESOURCE_LOCATION__FULL_PATH = eINSTANCE.getResourceLocation_FullPath();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.mylyn.docs.intent.mapping.ide.impl.FileLocationImpl <em>File Location</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.mylyn.docs.intent.mapping.ide.impl.FileLocationImpl
		 * @see org.eclipse.mylyn.docs.intent.mapping.ide.impl.IdePackageImpl#getFileLocation()
		 * @generated
		 */
		EClass FILE_LOCATION = eINSTANCE.getFileLocation();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.mylyn.docs.intent.mapping.ide.impl.TextFileLocationImpl <em>Text File
		 * Location</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.mylyn.docs.intent.mapping.ide.impl.TextFileLocationImpl
		 * @see org.eclipse.mylyn.docs.intent.mapping.ide.impl.IdePackageImpl#getTextFileLocation()
		 * @generated
		 */
		EClass TEXT_FILE_LOCATION = eINSTANCE.getTextFileLocation();

		/**
		 * The meta object literal for the '<em><b>Text</b></em>' attribute feature. <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute TEXT_FILE_LOCATION__TEXT = eINSTANCE.getTextFileLocation_Text();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.mylyn.docs.intent.mapping.ide.impl.EObjectFileLocationImpl <em>EObject File
		 * Location</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.mylyn.docs.intent.mapping.ide.impl.EObjectFileLocationImpl
		 * @see org.eclipse.mylyn.docs.intent.mapping.ide.impl.IdePackageImpl#getEObjectFileLocation()
		 * @generated
		 */
		EClass EOBJECT_FILE_LOCATION = eINSTANCE.getEObjectFileLocation();

		/**
		 * The meta object literal for the '<em><b>XMI Content</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute EOBJECT_FILE_LOCATION__XMI_CONTENT = eINSTANCE.getEObjectFileLocation_XMIContent();

	}

} // IdePackage
