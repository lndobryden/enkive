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

import static com.linuxbox.enkive.web.WebConstants.DATA_TAG;
import static com.linuxbox.enkive.web.WebConstants.ITEM_TOTAL_TAG;
import static com.linuxbox.enkive.web.WebConstants.QUERY_TAG;
import static com.linuxbox.enkive.web.WebConstants.RESULTS_TAG;
import static com.linuxbox.enkive.web.WebConstants.SEARCH_ID_TAG;
import static com.linuxbox.enkive.web.WebPageInfo.PAGE_POSITION_PARAMETER;
import static com.linuxbox.enkive.web.WebPageInfo.PAGE_SIZE_PARAMETER;
import static com.linuxbox.enkive.web.WebPageInfo.PAGE_SORT_BY_PARAMETER;
import static com.linuxbox.enkive.web.WebPageInfo.PAGE_SORT_DIR_PARAMETER;
import static com.linuxbox.enkive.web.WebPageInfo.PAGING_LABEL;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.linuxbox.enkive.exception.CannotRetrieveException;
import com.linuxbox.enkive.exception.EnkiveServletException;
import com.linuxbox.enkive.message.MessageSummary;
import com.linuxbox.enkive.retriever.MessageRetrieverService;
import com.linuxbox.enkive.web.AbstractJsonServlet;
import com.linuxbox.enkive.web.WebConstants;
import com.linuxbox.enkive.web.WebPageInfo;
import com.linuxbox.enkive.web.WebScriptUtils;
import com.linuxbox.enkive.workspace.WorkspaceException;
import com.linuxbox.enkive.workspace.WorkspaceService;
import com.linuxbox.enkive.workspace.searchQuery.SearchQuery;
import com.linuxbox.enkive.workspace.searchResult.ResultPageException;
import com.linuxbox.enkive.workspace.searchResult.SearchResult;

/**
 * This webscript is run when a user wants to see the results of a prior search,
 * either recent or saved
 */
public class ViewSavedResultsServlet extends AbstractJsonServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1226107681645083623L;

	protected static final Log LOGGER = LogFactory
			.getLog("com.linuxbox.enkive.webscripts.search.saved");

	protected MessageRetrieverService archiveService;
	protected WorkspaceService workspaceService;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.workspaceService = getWorkspaceService();
		this.archiveService = getMessageRetrieverService();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		String sortBy = null;
		int sortDir = 1;
		res.setCharacterEncoding("UTF-8");
		try {
			String searchId = WebScriptUtils.cleanGetParameter(req, "id");
			sortBy = WebScriptUtils.cleanGetParameter(req, PAGE_SORT_BY_PARAMETER);
			String sortDirString = WebScriptUtils.cleanGetParameter(req,
					PAGE_SORT_DIR_PARAMETER);
			if (sortDirString != null)
				sortDir = Integer.parseInt(sortDirString);

			WebPageInfo pageInfo = new WebPageInfo(
					WebScriptUtils.cleanGetParameter(req,
							PAGE_POSITION_PARAMETER),
					WebScriptUtils.cleanGetParameter(req, PAGE_SIZE_PARAMETER));

			JSONObject dataJSON = new JSONObject();
			JSONObject jsonResult = new JSONObject();
			dataJSON.put(SEARCH_ID_TAG, searchId);
			if (LOGGER.isInfoEnabled())
				LOGGER.info("Loading " + searchId);

			SearchQuery query = workspaceService.getSearch(searchId);

			/* Query */
			try {
				dataJSON.put(QUERY_TAG, query.toJson());
			} catch (JSONException e) {
				LOGGER.error("could not return search criteria for search "
						+ searchId, e);
			}

			/* Message Result List */

			try {
				SearchResult result = query.getResult();
				List<String> messageIds = result.getPage(sortBy, sortDir,
						pageInfo.getPagePos(), pageInfo.getPageSize());

				List<MessageSummary> messageSummaries = archiveService
						.retrieveSummary(messageIds);
				pageInfo.setItemTotal(result.getCount());
				dataJSON.put(WebConstants.STATUS_ID_TAG, query.getStatus());

				JSONArray jsonMessageSummaryList = SearchResultsBuilder
						.getMessageListJSON((Collection<MessageSummary>) messageSummaries);

				dataJSON.put(ITEM_TOTAL_TAG, pageInfo.getItemTotal());

				dataJSON.put(RESULTS_TAG, jsonMessageSummaryList);
			} catch (CannotRetrieveException e) {
				LOGGER.error("Could not access result message list", e);
				// throw new WebScriptException(
				// "Could not access query result message list", e);
			} catch (ResultPageException e) {
				LOGGER.error("Could not get page of results", e);
				this.addError(dataJSON, e.toString());
			}
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Returning saved search results for search id "
						+ searchId);

			jsonResult.put(DATA_TAG, dataJSON);
			jsonResult.put(PAGING_LABEL, pageInfo.getPageJSON());
			jsonResult.write(res.getWriter());
		} catch (WorkspaceException e) {
			respondError(HttpServletResponse.SC_UNAUTHORIZED, null, res);
			throw new EnkiveServletException(
					"Could not login to repository to retrieve search", e);
		} catch (JSONException e) {
			respondError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, null,
					res);
			throw new EnkiveServletException("Unable to serialize JSON", e);
		} finally {

		}
	}
}
