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
package com.linuxbox.enkive.statistics.services.storage.mongodb;

import static com.linuxbox.enkive.statistics.StatsConstants.STAT_GATHERER_NAME;
import static com.linuxbox.enkive.statistics.StatsConstants.STAT_STORAGE_COLLECTION;
import static com.linuxbox.enkive.statistics.StatsConstants.STAT_STORAGE_DB;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.linuxbox.enkive.exception.UnimplementedMethodException;
import com.linuxbox.enkive.statistics.RawStats;
import com.linuxbox.enkive.statistics.VarsMaker;
import com.linuxbox.enkive.statistics.services.StatsStorageService;
import com.linuxbox.enkive.statistics.services.storage.StatsStorageException;
import com.linuxbox.util.dbinfo.mongodb.MongoDbInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class MongoStatsStorageService extends VarsMaker implements
		StatsStorageService {
	protected final static Log LOGGER = LogFactory
			.getLog("com.linuxbox.enkive.statistics.services.storage.mongodb");

	private DBCollection coll;

	/**
	 * Since we like to configure everything from the outside, why is this
	 * constructor even available?
	 */
	public MongoStatsStorageService(MongoClient mongo) {
		this(mongo, STAT_STORAGE_DB, STAT_STORAGE_COLLECTION);

		// *** let's figure out when/if this constructor is ever called
		throw new UnimplementedMethodException();
	}

	public MongoStatsStorageService(MongoClient mongo, String dbName,
			String collectionName) {
		this(mongo.getDB(dbName).getCollection(collectionName));
	}
	
	public MongoStatsStorageService(MongoDbInfo dbInfo) {
		this(dbInfo.getCollection());
	}

	public MongoStatsStorageService(DBCollection collection) {
		this.coll = collection;
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("StorageService successfully created");
		}
	}

	@Override
	public void storeStatistics(List<Map<String, Object>> dataSet)
			throws StatsStorageException {
		if (dataSet != null) {
			for (Map<String, Object> map : dataSet) {
				coll.insert(new BasicDBObject(map));
			}
		} else {
			LOGGER.warn("dataSet is null nothing will be stored");
		}
	}

	@Override
	public void storeStatistics(String service, RawStats data)
			throws StatsStorageException {
		Map<String, Object> result = createMap();
		result.put(STAT_GATHERER_NAME, service);
		result.putAll(data.toMap());
		coll.insert(new BasicDBObject(result));
	}
}
