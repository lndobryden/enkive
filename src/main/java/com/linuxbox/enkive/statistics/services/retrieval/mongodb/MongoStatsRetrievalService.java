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
package com.linuxbox.enkive.statistics.services.retrieval.mongodb;

import static com.linuxbox.enkive.statistics.StatsConstants.STAT_GATHERER_NAME;
import static com.linuxbox.enkive.statistics.StatsConstants.STAT_TIMESTAMP;
import static com.linuxbox.enkive.statistics.consolidation.ConsolidationConstants.CONSOLIDATION_MAX;
import static com.linuxbox.enkive.statistics.gathering.mongodb.MongoConstants.MONGO_ID;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;

import com.linuxbox.enkive.statistics.VarsMaker;
import com.linuxbox.enkive.statistics.services.StatsRetrievalService;
import com.linuxbox.enkive.statistics.services.retrieval.StatsFilter;
import com.linuxbox.enkive.statistics.services.retrieval.StatsQuery;
import com.linuxbox.enkive.statistics.services.retrieval.StatsRetrievalException;
import com.linuxbox.util.dbinfo.mongodb.MongoDbInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoStatsRetrievalService extends VarsMaker implements
		StatsRetrievalService {
	protected final static Log LOGGER = LogFactory
			.getLog("com.linuxbox.enkive.statistics.services.retrieval.mongodb");

	private DBCollection coll;

	public MongoStatsRetrievalService(MongoClient mongo, String dbName,
			String collectionName) {
		this(mongo.getDB(dbName).getCollection(collectionName));
	}

	public MongoStatsRetrievalService(MongoDbInfo dbInfo) {
		this(dbInfo.getCollection());
	}

	public MongoStatsRetrievalService(DBCollection collection) {
		coll = collection;
		LOGGER.info("RetrievalService(MongoClient, String) successfully created");
	}

	/**
	 * performs a query on the database based on a query map
	 * 
	 * @param hmap
	 *            - the formatted query map
	 * @return the query results as a set of maps
	 */
	private Set<DBObject> getQuerySet(StatsQuery query) {
		Set<DBObject> result = new HashSet<DBObject>();
		result.addAll(coll.find(new BasicDBObject(query.getQuery())).toArray());
		return result;
	}

	/**
	 * Takes a DBObject, extracts the map from it, and inserts that map into the
	 * given set. NOTE: warnings are suppressed because they are being activated
	 * because the actual type of the map is not specified in the dbObject but
	 * it should not matter so long as it conforms to MongoClient's key:value format
	 * 
	 * @param entry
	 *            -dbObject to extract map from
	 * @param stats
	 *            -set to add map to
	 */
	@SuppressWarnings("unchecked")
	private void addMapToSet(DBObject entry, Set<Map<String, Object>> stats) {
		stats.add(entry.toMap());
	}

	@SuppressWarnings("unchecked")
	private void addMapToList(DBObject entry, List<Map<String, Object>> stats) {
		stats.add(entry.toMap());
	}

	@Override
	public Set<Map<String, Object>> queryStatistics() {
		Set<Map<String, Object>> result = createSetOfMaps();
		for (DBObject entry : coll.find()) {
			addMapToSet(entry, result);
		}

		return result;
	}

	@Override
	public Set<Map<String, Object>> queryStatistics(StatsQuery query) {
		Set<Map<String, Object>> result = createSetOfMaps();
		for (DBObject entry : getQuerySet(query)) {
			addMapToSet(entry, result);
		}
		return result;
	}

	private DBObject getFilter(StatsFilter filter) {
		return new BasicDBObject(filter.getFilter());
	}

	@Override
	public Set<Map<String, Object>> queryStatistics(StatsQuery query,
			StatsFilter filter) throws StatsRetrievalException {
		if (filter == null) {
			return queryStatistics(query);
		}
		DBObject mongoQuery = new BasicDBObject(query.getQuery());
		DBObject mongoFilter = getFilter(filter);
		Set<Map<String, Object>> result = createSetOfMaps();
		for (DBObject entry : coll.find(mongoQuery, mongoFilter)) {
			addMapToSet(entry, result);
		}
		return result;
	}

	// lists guarantee order (ex: querylist[1] MUST correspond to filterlist[1])
	public List<Map<String, Object>> queryStatistics(
			List<StatsQuery> queryList, List<StatsFilter> filterList)
			throws StatsRetrievalException {
		List<DBObject> allStats = new LinkedList<DBObject>();
		for (int i = 0; i < queryList.size(); i++) {
			DBObject query = new BasicDBObject(queryList.get(i).getQuery());
			DBObject filter = null;
			if (filterList != null && !filterList.isEmpty()
					&& filterList.get(i) != null) {
				filter = getFilter(filterList.get(i));
			}

			if (filter != null) {
				allStats.addAll(coll
						.find(query, filter)
						.sort(new BasicDBObject(STAT_TIMESTAMP + "."
								+ CONSOLIDATION_MAX, 1)).toArray());
			} else {
				allStats.addAll(coll.find(query).toArray());
			}
		}

		List<Map<String, Object>> result = createListOfMaps();
		for (DBObject entry : allStats) {
			addMapToList(entry, result);
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> queryStatistics(
			Map<String, Map<String, Object>> queryMap,
			Map<String, Map<String, Object>> filterMap)
			throws StatsRetrievalException {
		Set<DBObject> allStats = new HashSet<DBObject>();
		for (String gathererName : queryMap.keySet()) {
			BasicDBObject query = new BasicDBObject();
			query.put(STAT_GATHERER_NAME, gathererName);
			query.putAll(queryMap.get(gathererName));
			if (filterMap.get(gathererName) != null
					&& !filterMap.get(gathererName).isEmpty()) {
				BasicDBObject filter = new BasicDBObject(
						filterMap.get(gathererName));
				allStats.addAll(coll
						.find(query, filter)
						.sort(new BasicDBObject(STAT_TIMESTAMP + "."
								+ CONSOLIDATION_MAX, 1)).toArray());
			} else {
				allStats.addAll(coll
						.find(query)
						.sort(new BasicDBObject(STAT_TIMESTAMP + "."
								+ CONSOLIDATION_MAX, 1)).toArray());
			}
		}
		List<Map<String, Object>> result = createListOfMaps();
		for (DBObject entry : allStats) {
			addMapToList(entry, result);
		}
		return result;
	}

	@Override
	public void remove(Set<Object> set) throws StatsRetrievalException {
		if (set != null) {// not null
			if (!set.isEmpty()) { // not empty
				for (Object id : set) {
					if (id instanceof ObjectId) {
						Map<String, ObjectId> map = new HashMap<String, ObjectId>();
						map.put(MONGO_ID, (ObjectId) id);
						coll.remove(new BasicDBObject(map));
					}
				}
			}
		}
	}
}
