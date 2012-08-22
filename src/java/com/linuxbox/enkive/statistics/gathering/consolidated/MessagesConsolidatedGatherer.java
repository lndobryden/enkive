package com.linuxbox.enkive.statistics.gathering.consolidated;

import static com.linuxbox.enkive.statistics.StatsConstants.STAT_GATHERER_NAME;
import static com.linuxbox.enkive.statistics.StatsConstants.STAT_NUM_ENTRIES;
import static com.linuxbox.enkive.statistics.StatsConstants.STAT_TIMESTAMP;
import static com.linuxbox.enkive.statistics.StatsConstants.STAT_TOTAL_MSGS;
import static com.linuxbox.enkive.statistics.granularity.GrainConstants.GRAIN_AVG;
import static com.linuxbox.enkive.statistics.granularity.GrainConstants.GRAIN_MAX;
import static com.linuxbox.enkive.statistics.granularity.GrainConstants.GRAIN_MIN;
import static com.linuxbox.enkive.statistics.granularity.GrainConstants.GRAIN_TYPE;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.linuxbox.enkive.message.search.exception.MessageSearchException;

import com.linuxbox.enkive.statistics.gathering.GathererException;
import com.linuxbox.enkive.statistics.gathering.GathererMessageSearchService;
import com.linuxbox.enkive.statistics.services.StatsClient;

public class MessagesConsolidatedGatherer extends ConsolidatedGatherer{
	GathererMessageSearchService searchService;
	
	public MessagesConsolidatedGatherer(GathererMessageSearchService searchService, String name, StatsClient client, int hrKeepTime, int dayKeepTime, int weekKeepTime, int monthKeepTime) {
		super(name, client, hrKeepTime, dayKeepTime, weekKeepTime, monthKeepTime);
		this.searchService = searchService;
	}
	
	@PostConstruct
	public void init(){
		System.out.println("Start: " + new Date());
		consolidatePastHours();
		consolidatePastDays();
		consolidatePastWeeks();
		consolidatePastMonths();
		System.out.println("End: " + new Date());
	}

	protected Map<String, Object> getConsolidatedData(Date start, Date end, int grain) throws GathererException{
		Map<String, Object> result = new HashMap<String, Object>();
		int numEntries = 0;
		int totalMsgs = 0;
		try {
			numEntries = searchService.getNumberOfMessages(start, end);
			totalMsgs = searchService.getNumberOfMessages(new Date(0L), end);
		} catch (MessageSearchException e) {
			throw new GathererException(e);
		}
		
		if(totalMsgs == 0){
			System.out.println("totalMsgs: " + totalMsgs);
			return null;
		}
		
		Map<String, Object> dateMap = new HashMap<String, Object>();
		dateMap.put(GRAIN_MIN, start);
		dateMap.put(GRAIN_MAX, end);
		
		Map<String,Object> innerNumEntries = new HashMap<String,Object>();
		innerNumEntries.put(GRAIN_AVG, numEntries);
		Map<String,Object> innerTotalMsgs = new HashMap<String,Object>();
		innerTotalMsgs.put(GRAIN_AVG, totalMsgs);
		
		result.put(GRAIN_TYPE, grain);
		result.put(STAT_TIMESTAMP, dateMap);
		result.put(STAT_GATHERER_NAME, gathererName);
		result.put(STAT_TOTAL_MSGS, innerTotalMsgs);
		result.put(STAT_NUM_ENTRIES, innerNumEntries);
		return result;
	}
}
