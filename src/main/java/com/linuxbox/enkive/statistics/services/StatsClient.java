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
package com.linuxbox.enkive.statistics.services;

import static com.linuxbox.enkive.statistics.VarsMaker.createListOfMaps;

import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.linuxbox.enkive.statistics.RawStats;
import com.linuxbox.enkive.statistics.gathering.GathererAttributes;
import com.linuxbox.enkive.statistics.gathering.GathererException;
import com.linuxbox.enkive.statistics.services.retrieval.StatsFilter;
import com.linuxbox.enkive.statistics.services.retrieval.StatsQuery;
import com.linuxbox.enkive.statistics.services.retrieval.StatsRetrievalException;
import com.linuxbox.enkive.statistics.services.storage.StatsStorageException;

public class StatsClient {
	protected final static Log LOGGER = LogFactory
			.getLog("com.linuxbox.enkive.statistics.services.StatsClient");
	protected StatsGathererService gathererService;
	protected StatsRetrievalService retrievalService;
	protected StatsStorageService storageService;

	public StatsClient(StatsGathererService gatherer,
			StatsStorageService storer, StatsRetrievalService retriever) {
		gathererService = gatherer;
		storageService = storer;
		retrievalService = retriever;
		LOGGER.info("Client successfully created");
	}

	/**
	 * gathers raw statistics from every known gatherer in the gathererService
	 * 
	 * @return returns all statistics gathered from every known gatherer
	 */
	public List<RawStats> gatherData() {
		try {
			return gathererService.gatherStats();
		} catch (ParseException e) {
			LOGGER.warn("Client.gatherData() ParseException", e);
		} catch (GathererException e) {
			LOGGER.warn("Client.gatherData() GathererException", e);
		}

		return null;
	}

	/**
	 * takes a map and filters the raw statistics based on it. Each map contains
	 * keys corresponding to a gatherer name followed by an string array of keys
	 * to return
	 * 
	 * @param gathererFilter
	 *            - a map in the form {gathererName:[array of keys for that
	 *            gatherer]}
	 * @return stats returned after filter
	 */
	public List<RawStats> gatherData(Map<String, List<String>> gathererFilter) {
		try {
			return gathererService.gatherStats(gathererFilter);
		} catch (GathererException e) {
			LOGGER.error("Client.gatherData(Map) GathererException", e);
		}
		return null;
	}

	/**
	 * @return all gathererNames
	 */
	public Set<String> gathererNames() {
		return gathererService.getStatsGatherers().keySet();
	}

	/**
	 * @return all attributes
	 */
	public Set<GathererAttributes> getAttributes() {
		Set<GathererAttributes> attributeSet = new HashSet<GathererAttributes>();
		for (String name : gathererNames()) {
			attributeSet.add(getAttributes(name));
		}
		return attributeSet;
	}

	/**
	 * @param serviceName
	 *            - the servicename for which to return the gathererAttributes
	 * @return gathererAttributes of the gatherer specified by the servicename
	 */
	public GathererAttributes getAttributes(String serviceName) {
		return gathererService.getStatsGatherers(serviceName).get(serviceName)
				.getAttributes();
	}

	/**
	 * query the database for all objects
	 * 
	 * @return result of objects found with query
	 */
	public Set<Map<String, Object>> queryStatistics() {
		try {
			return retrievalService.queryStatistics();
		} catch (StatsRetrievalException e) {
			LOGGER.error(
					"Client.queryStatistics(StatsQuery) StatsRetrievalException",
					e);
		}
		return null;
	}

	/**
	 * query the database using a query argument and a date range
	 * 
	 * @param query
	 *            the statsQuery used to get objects for this class
	 * @return result of objects found with query
	 */
	public Set<Map<String, Object>> queryStatistics(StatsQuery query) {
		try {
			return retrievalService.queryStatistics(query);
		} catch (StatsRetrievalException e) {
			LOGGER.error(
					"Client.queryStatistics(StatsQuery) StatsRetrievalException",
					e);
		}
		return null;
	}

	public Set<Map<String, Object>> queryStatistics(StatsQuery query,
			StatsFilter filter) {
		try {
			return retrievalService.queryStatistics(query, filter);
		} catch (StatsRetrievalException e) {
			LOGGER.error(
					"Client.queryStatistics(StatsQuery) StatsRetrievalException",
					e);
		}
		return null;
	}

	/**
	 * //TODO fix documentation query the database and filter the result before
	 * returning both arguments should be formated like so: queryMap -
	 * {gathererName:{key:value, ...}, ...} filterMap - {gathererName:{key:1,
	 * key:1,...},...}
	 * 
	 * @param queryMap
	 *            - the query object to use
	 * @param filterMap
	 *            - the keys to be returned for each service
	 * @return the resultant set of objects from the database
	 */
	public List<Map<String, Object>> queryStatistics(
			Map<String, Map<String, Object>> queryMap,
			Map<String, Map<String, Object>> filterMap) {
		try {
			return retrievalService.queryStatistics(queryMap, filterMap);
		} catch (StatsRetrievalException e) {
			LOGGER.error(
					"Client.queryStatistics(map, map) StatsRetrievalException",
					e);
		}
		return null;
	}

	// TODO document
	public List<Map<String, Object>> queryStatistics(List<StatsQuery> query,
			List<StatsFilter> filter) {
		try {
			return retrievalService.queryStatistics(query, filter);
		} catch (StatsRetrievalException e) {
			LOGGER.error(
					"Client.queryStatistics(map, map) StatsRetrievalException",
					e);
		}
		return null;
	}

	/**
	 * removes all objects in the parameter deletionset
	 * 
	 * @param deletionSet
	 *            - a set of objectIds to be removed
	 */
	public void remove(Set<Object> deletionSet) {
		try {
			retrievalService.remove(deletionSet);
		} catch (StatsRetrievalException e) {
			LOGGER.error("Client.remove(Set) StatsRetrievalException", e);
		}
	}

	/**
	 * stores data in the mongo database
	 * 
	 * @param dataToStore
	 *            - the data to be stored
	 */
	public void storeData(List<Map<String, Object>> dataToStore) {
		try {
			storageService.storeStatistics(dataToStore);
		} catch (StatsStorageException e) {
			LOGGER.error("Client.storeData StatsStorageException", e);
		}
	}

	/**
	 * converts the Set<rawData> into a Set<map> that can be stored in the mongo
	 * database then stores it
	 * 
	 * @param dataToStore
	 *            - the data to be stored
	 */
	public void storeRawStatsData(List<RawStats> rawDataSet) {
		List<Map<String, Object>> dataToStore = createListOfMaps();
		for (RawStats stat : rawDataSet) {
			dataToStore.add(stat.toMap());
		}
		storeData(dataToStore);
	}
}
