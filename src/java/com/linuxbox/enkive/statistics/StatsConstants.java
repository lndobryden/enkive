package com.linuxbox.enkive.statistics;

import java.text.SimpleDateFormat;

public interface StatsConstants {
	public static String QUEUE_LENGTH = "QueueLength";
	public static String ARCHIVE_SIZE = "MessagesInArchive";
	public static String STATISTIC_CHECK_ERROR = "CheckError";

	// key names

	// Attachments
	public static String STAT_MAX_ATTACH = "maxStat";
	public static String STAT_AVG_ATTACH = "avgStat";

	// DBs only
	public static String STAT_FILE_SIZE = "fileSize";
	public static String STAT_NUM_COLLECTIONS = "numColls";

	// Colls only
	public static String STAT_NS = "ns";
	public static String STAT_INDEX_SIZES = "indexSizes";
	public static String STAT_LAST_EXTENT_SIZE = "lastExSz";

	// Runtime only
	public static String STAT_MAX_MEMORY = "maxMem";
	public static String STAT_FREE_MEMORY = "freeMem";
	public static String STAT_TOTAL_MEMORY = "totalMem";
	public static String STAT_PROCESSORS = "processors";

	// DBs & Colls
	public static String STAT_NAME = "dbName";
	public static String STAT_TYPE = "type";// runtime also has a 'type' field
	public static String STAT_TIME = "time";
	public static String STAT_NUM_OBJS = "numObjs";
	public static String STAT_DATA_SIZE = "dataSize";
	public static String STAT_NUM_INDEX = "numIndex";
	public static String STAT_NUM_EXTENT = "numExtent";
	public static String STAT_TOTAL_SIZE = "totalSize";
	public static String STAT_AVG_OBJ_SIZE = "avgObjSize";
	public static String STAT_TOTAL_INDEX_SIZE = "indexSize";

	// MsgEntries
	public static String STAT_NUM_ENTRIES = "numEntries";

	// ErrorKey
	public static String STAT_ERROR = "ERROR";

	public static String STAT_STORAGE_COLLECTION = "statistics";
	public static String STAT_SERVICE_NAME = "serviceName";
	public static String STAT_TIME_STAMP = "timeStamp";
	// end of keynames

	// misc
	public static long THIRTY_DAYS = 2592000000L;// millisecond value of 30 days
	// don't forget MM starts index at jan = 01 (not 00)!
	public static SimpleDateFormat SIMPLE_DATE = new SimpleDateFormat(
			"yyyy-MM-dd");

}