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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;
import org.vraptor.converter.ConversionError;
import org.vraptor.impl.core.Localization;
import org.vraptor.impl.http.MutableRequest;

public class LocaleBasedCalendarConverterTest extends WeldTest {

	private LocaleBasedCalendarConverter converter;
	private Mockery mockery;
	private MutableRequest request;
	private HttpSession session;
	private ServletContext context;
	private ResourceBundle bundle;
	private Localization localization;

	@Before
	public void setup() {
		this.mockery = new Mockery();
		this.request = mockery.mock(MutableRequest.class);
		this.session = mockery.mock(HttpSession.class);
		this.context = mockery.mock(ServletContext.class);
		this.localization = mockery.mock(Localization.class);
		this.converter = new LocaleBasedCalendarConverter(localization);
		this.bundle = ResourceBundle.getBundle("messages");
        Locale.setDefault(Locale.ENGLISH);
	}

	@Test
	public void shouldBeAbleToConvert() {
		mockery.checking(new Expectations() {
			{
				exactly(1).of(request).getAttribute("javax.servlet.jsp.jstl.fmt.locale.request");
				will(returnValue("pt_br"));
			}
		});
		assertThat(converter.convert("10/06/2008", Calendar.class, bundle),
				is(equalTo((Calendar) new GregorianCalendar(2008, 5, 10))));
		mockery.assertIsSatisfied();
	}

	@Test
	public void shouldUseTheDefaultLocale() throws ParseException {
		mockery.checking(new Expectations() {
			{
				one(request).getAttribute("javax.servlet.jsp.jstl.fmt.locale.request");
				will(returnValue(null));
				one(request).getSession();
				will(returnValue(session));
				one(session).getAttribute("javax.servlet.jsp.jstl.fmt.locale.session");
				will(returnValue(null));
				one(context).getAttribute("javax.servlet.jsp.jstl.fmt.locale.application");
				will(returnValue(null));
				one(context).getInitParameter("javax.servlet.jsp.jstl.fmt.locale");
				will(returnValue(null));
				one(request).getLocale();
				will(returnValue(Locale.getDefault()));
			}
		});
		Date date = new SimpleDateFormat("dd/MM/yyyy").parse("10/05/2010");
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		String formattedToday = DateFormat.getDateInstance(DateFormat.SHORT).format(date);
		assertThat(converter.convert(formattedToday, Calendar.class, bundle), is(equalTo(cal)));
		mockery.assertIsSatisfied();
	}

	@Test
	public void shouldBeAbleToConvertEmpty() {
		assertThat(converter.convert("", Calendar.class, bundle), is(nullValue()));
		mockery.assertIsSatisfied();
	}

	@Test
	public void shouldBeAbleToConvertNull() {
		assertThat(converter.convert(null, Calendar.class, bundle), is(nullValue()));
		mockery.assertIsSatisfied();
	}

	@Test
	public void shouldThrowExceptionWhenUnableToParse() {
		mockery.checking(new Expectations() {
			{
				exactly(2).of(request).getAttribute("javax.servlet.jsp.jstl.fmt.locale.request");
				will(returnValue("pt_br"));
			}
		});
		try {
			converter.convert("a,10/06/2008/a/b/c", Calendar.class, bundle);
		} catch (ConversionError e) {
			assertThat(e.getMessage(), is(equalTo("a,10/06/2008/a/b/c is not a valid date.")));
		}
	}

}
