/*******************************************************************************
 * Copyright 2015 Enkive, LLC.
 *
 * This file is part of Enkive CE (Community Edition).
 *
 * Enkive CE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Enkive CE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with Enkive CE. If not, see
 * <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package com.linuxbox.enkive.web.search;

import static com.linuxbox.enkive.search.Constants.CONTENT_PARAMETER;
import static com.linuxbox.enkive.search.Constants.DATE_EARLIEST_PARAMETER;
import static com.linuxbox.enkive.search.Constants.DATE_LATEST_PARAMETER;
import static com.linuxbox.enkive.search.Constants.MESSAGE_ID_PARAMETER;
import static com.linuxbox.enkive.search.Constants.RECIPIENT_PARAMETER;
import static com.linuxbox.enkive.search.Constants.SENDER_PARAMETER;
import static com.linuxbox.enkive.search.Constants.SUBJECT_PARAMETER;
import static com.linuxbox.enkive.web.WebConstants.SEARCH_IS_IMAP;
import static com.linuxbox.enkive.web.WebConstants.SEARCH_IS_SAVED;
import static com.linuxbox.enkive.web.WebConstants.SEARCH_NAME_TAG;
import static com.linuxbox.util.StringUtils.isEmpty;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import com.linuxbox.enkive.web.AbstractJsonServlet;
import com.linuxbox.util.StringUtils;

public abstract class AbstractSearchWebScript extends AbstractJsonServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9081244471746409976L;

	static private final DateFormat ISO_8601_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");

	protected static final Log LOGGER = LogFactory
			.getLog("com.linuxbox.enkive.webscripts");

	// formats to try parsing dates in; attempted in the order given
	static private final DateFormat[] DATE_FORMATS = { ISO_8601_DATE_FORMAT,
			DateFormat.getDateInstance(DateFormat.SHORT),
			DateFormat.getDateInstance(DateFormat.MEDIUM),
			DateFormat.getDateInstance(DateFormat.LONG) };

	public AbstractSearchWebScript() {
		super();
	}

	public Date parseDate(String s) throws ParseException {
		if (isEmpty(s)) {
			return null;
		}

		ParseException firstException = null;
		for (DateFormat f : DATE_FORMATS) {
			try {
				return f.parse(s);
			} catch (ParseException e) {
				if (firstException == null) {
					firstException = e;
				}
			}
		}

		throw firstException;
	}

	protected JSONObject searchParametersToJson(String sender, String recipient,
			String dateEarliest, String dateLatest, String subject,
			String messageId, String content)
					throws JSONException {
		JSONObject params = new JSONObject();

		if (StringUtils.hasData(sender)) {
			params.put(SENDER_PARAMETER, sender);
		}
		if (StringUtils.hasData(recipient)) {
			params.put(RECIPIENT_PARAMETER, recipient);
		}
		if (StringUtils.hasData(dateEarliest)) {
			params.put(DATE_EARLIEST_PARAMETER, dateEarliest);
		}
		if (StringUtils.hasData(dateLatest)) {
			params.put(DATE_LATEST_PARAMETER, dateLatest);
		}
		if (StringUtils.hasData(subject)) {
			params.put(SUBJECT_PARAMETER, subject);
		}
		if (StringUtils.hasData(messageId)) {
			params.put(MESSAGE_ID_PARAMETER, messageId);
		}
		if (StringUtils.hasData(content)) {
			params.put(CONTENT_PARAMETER, content);
		}

		return params;
	}
	protected JSONObject searchQueryToJson(String name, boolean isSaved, boolean isIMAP)
					throws JSONException {
		JSONObject query = new JSONObject();

		if (StringUtils.hasData(name)) {
			query.put(SEARCH_NAME_TAG, name);
		}
		if (isSaved) {
			query.put(SEARCH_IS_SAVED, "true");
		} else {
			query.put(SEARCH_IS_SAVED, "false");
		}
		if (isIMAP) {
			query.put(SEARCH_IS_IMAP, "true");
		} else {
			query.put(SEARCH_IS_IMAP, "false");
		}

		return query;
	}
}
