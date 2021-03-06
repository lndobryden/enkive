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
package com.linuxbox.enkive.statistics;

import static com.linuxbox.enkive.statistics.StatsConstants.STAT_GATHERER_NAME;
import static com.linuxbox.enkive.statistics.StatsConstants.STAT_INTERVAL;
import static com.linuxbox.enkive.statistics.StatsConstants.STAT_POINT;
import static com.linuxbox.enkive.statistics.StatsConstants.STAT_TIMESTAMP;
import static com.linuxbox.enkive.statistics.StatsConstants.STAT_TS_POINT;
import static com.linuxbox.enkive.statistics.consolidation.ConsolidationConstants.CONSOLIDATION_MAX;
import static com.linuxbox.enkive.statistics.consolidation.ConsolidationConstants.CONSOLIDATION_MIN;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to communicate that the data and methods within it only
 * coorespond to raw statistics.
 */
public class RawStats {
	String gathererName;
	Map<String, Object> intervalStats;
	Date startDate = null;
	Date endDate = null;

	Map<String, Object> pointStats;
	Date pointDate = null;

	/**
	 * @param iStats
	 *            - a map containing the interval statistics of a gatherer
	 * @param pStats
	 *            - a map containing the point statistics of a gatherer
	 * @param startDate
	 *            - the idealized start date of the gatherer
	 * @param endDate
	 *            - the idealized end date of the gatherer
	 */
	public RawStats(Map<String, Object> iStats, Map<String, Object> pStats,
			Date startDate, Date endDate) {
		setStartDate(startDate);
		setEndDate(endDate);
		setPointDate(new Date());
		setIntervalMap(iStats);
		setPointMap(pStats);
	}

	/**
	 * 
	 */
	public String getGathererName() {
		return gathererName;
	}

	/**
	 * 
	 */
	public void setGathererName(String name) {
		this.gathererName = name;
	}

	/**
	 * @return the idealized start date
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @return the idealized end date
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param timestamp
	 *            set the idealized start date
	 */
	public void setStartDate(Date timestamp) {
		this.startDate = timestamp;
	}

	/**
	 * @param timestamp
	 *            set the idealized end date
	 */
	public void setEndDate(Date timestamp) {
		this.endDate = timestamp;
	}

	/**
	 * @return the time this rawStats class was instantiated
	 */
	public Date getPointDate() {
		return pointDate;
	}

	/**
	 * @param timestamp
	 *            the time this class was instantiated
	 */
	protected void setPointDate(Date timestamp) {
		this.pointDate = timestamp;
	}

	/**
	 * @return a map in the standard format of storage in the database for raw
	 *         statistical data
	 */
	public Map<String, Object> toMap() {
		Map<String, Object> dateMap = new HashMap<String, Object>();
		dateMap.put(CONSOLIDATION_MIN, getStartDate());
		dateMap.put(STAT_TS_POINT, getPointDate());
		dateMap.put(CONSOLIDATION_MAX, getEndDate());

		Map<String, Object> statsMap = new HashMap<String, Object>();
		statsMap.put(STAT_TIMESTAMP, dateMap);
		if (intervalStats != null) {
			statsMap.put(STAT_INTERVAL, intervalStats);
		}
		if (pointStats != null) {
			statsMap.put(STAT_POINT, pointStats);
		}
		if (getGathererName() != null) {
			statsMap.put(STAT_GATHERER_NAME, getGathererName());
		}

		return statsMap;
	}

	/**
	 * @param stats
	 *            - a map containing the interval statistics of a gatherer
	 */
	public void setIntervalMap(Map<String, Object> stats) {
		this.intervalStats = stats;
	}

	/**
	 * @param stats
	 *            - a map containing the point statistics of a gatherer
	 */
	public void setPointMap(Map<String, Object> stats) {
		this.pointStats = stats;
	}
}
