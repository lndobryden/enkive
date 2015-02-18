/*******************************************************************************
 * Copyright 2015 Enkive, LLC.
 * 
 * This file is part of Enkive CE (Community Edition).
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
 ******************************************************************************/
package com.linuxbox.enkive.workspace.searchQuery;

import java.util.Collection;

import com.linuxbox.enkive.workspace.WorkspaceException;

/**
 * A factory interface for creating and finding @ref SeachQuery objects.
 * @author dang
 *
 */
public interface SearchQueryBuilder {

	public SearchQuery getSearchQuery() throws WorkspaceException;

	public SearchQuery getSearchQuery(String searchQueryId)
			throws WorkspaceException;

	public SearchQuery getSearchQueryByName(String name)
			throws WorkspaceException;
	
	public SearchQuery getSearchQueryByNameAndImap(String name, boolean isImap)
			throws WorkspaceException;

	public Collection<SearchQuery> getSearchQueries(
			Collection<String> searchQueryUUIDs)
			throws WorkspaceException;
}
