/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation and/or initial documentation
 *    ...
 *******************************************************************************/
package eu.modelwriter.semantic.ide.ui.marker;

import org.eclipse.osgi.util.TextProcessor;
import org.eclipse.ui.views.markers.MarkerField;
import org.eclipse.ui.views.markers.MarkerItem;
import org.eclipse.ui.views.markers.MarkerViewUtil;

/**
 * {@link MarkerField} for the resource name.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class MarkerResourceField extends MarkerField {

	@Override
	public String getValue(MarkerItem item) {
		final String res;

		if (item.getMarker() == null) {
			res = "";
		} else {
			res = TextProcessor.process(item.getAttributeValue(MarkerViewUtil.NAME_ATTRIBUTE, item
					.getMarker().getResource().getName()));
		}

		return res;
	}

}
