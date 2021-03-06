/***
 * Copyright (c) 2009 Caelum - www.caelum.com.br/opensource
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */
package org.vraptor.impl.converter;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.vraptor.converter.ConversionError;
import org.vraptor.converter.Converter;

/**
 * VRaptor's BigDecimal converter.
 *
 * @author Cecilia Fernandes
 */
public class BigDecimalConverter implements Converter<BigDecimal>{

	public BigDecimal convert(String value, Class<? extends BigDecimal> type, ResourceBundle bundle) {
		if (isNullOrEmpty(value)) {
			return null;
		}
		
		try {
			return new BigDecimal(value);
		} catch (NumberFormatException e) {
			throw new ConversionError(MessageFormat.format(bundle.getString("is_not_a_valid_number"), value));
		}

	}

	public Class<?> type() {
		return BigDecimal.class;
	}
}
