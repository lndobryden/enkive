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
package com.linuxbox.enkive.workspace;

import java.util.Collection;

import com.linuxbox.enkive.workspace.searchQuery.SearchQuery;
import com.linuxbox.enkive.workspace.searchQuery.SearchQueryBuilder;

/**
 * Interface for the creation of and access to @ref Workspace objects.
 * @author dang
 *
 */
public interface WorkspaceService extends WorkspaceServiceMBean {

	// NOTE: inherits getInteractiveSearchWaitSeconds() and
	// setInteractiveSearchWaitSeconds from WorkspaceServiceMBean

	Workspace getActiveWorkspace(String userId) throws WorkspaceException;

	Workspace getWorkspace(String workspaceUUID) throws WorkspaceException;

	Collection<Workspace> getUserWorkspaces(String userId)
			throws WorkspaceException;

	SearchQuery getSearch(String searchId)
			throws WorkspaceException;

/*	SearchFolder getSearchFolder(String searchFolderId)
			throws WorkspaceException;*/

	public SearchQueryBuilder getSearchQueryBuilder();

	public void setSearchQueryBuilder(SearchQueryBuilder searchQueryBuilder);

}
