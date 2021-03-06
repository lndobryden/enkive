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

import java.text.SimpleDateFormat;

public interface StatsConstants {
	public static String STAT_ARCHIVE_SIZE = "MsgArchive";
	public static String STAT_ATTACH_ARCHIVE_SIZE = "totAttSz";
	public static String QUEUE_LENGTH = "queLen";
	public static SimpleDateFormat SIMPLE_DATE = new SimpleDateFormat(
			"yyyy-MM-dd-HH-mm-ss");
	public static String STAT_ATTACH_SIZE = "attSz";
	public static String STAT_ATTACH_NUM = "attNum";
	public static String STAT_AVG_OBJ_SIZE = "avgOSz";
	public static String STAT_DATA_SIZE = "dataSz";
	public static String STAT_TS_POINT = "ptTime";
	public static String STAT_INTERVAL = "ival";
	public static String STAT_POINT = "pt";
	// DBs only
	public static String STAT_FILE_SIZE = "fileSz";
	public static String STAT_FREE_MEMORY = "freeM";
	public static String STAT_INDEX_SIZES = "indSzs";

	public static String STAT_LAST_EXTENT_SIZE = "lExSz";

	// Attachments
	public static String STAT_MAX_ATTACH = "maxAtt";
	// Runtime only
	public static String STAT_MAX_MEMORY = "maxM";
	// DBs & Colls
	public static String STAT_NAME = "db";

	// Colls only
	public static String STAT_NS = "ns";
	public static String STAT_NUM_COLLECTIONS = "nColls";
	// MsgEntries
	public static String STAT_TOTAL_MSGS = "totMsg";
	public static String STAT_NUM_ENTRIES = "numMsg";
	public static String STAT_NUM_EXTENT = "numExt";

	public static String STAT_NUM_INDEX = "numInd";
	public static String STAT_NUM_OBJS = "numObj";
	public static String STAT_PROCESSORS = "cores";
	public static String STAT_GATHERER_NAME = "gn";
	public static String STAT_STORAGE_COLLECTION = "statistics";
	public static String STAT_STORAGE_DB = "enkive";
	public static String STAT_TIMESTAMP = "ts";
	// end of keynames
	public static String STAT_TOTAL_INDEX_SIZE = "indSz";
	public static String STAT_TOTAL_MEMORY = "totM";

	public static String STAT_TOTAL_SIZE = "totSz";

	public static String STAT_TYPE = "type";

	public static String STAT_TYPE_COLL = "COLL";
	public static String STAT_TYPE_DB = "DB";
	public static String STAT_TYPE_RUN = "RUN";
	public static String STAT_UPLOAD_DATE = "upDate";

	public static String STATISTIC_CHECK_ERROR = "CheckError";
	// misc
	public static long THIRTY_DAYS = 2592000000L;// millisecond value of 30 days

}
