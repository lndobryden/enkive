package com.linuxbox.enkive.statistics.granularity;

import static com.linuxbox.enkive.statistics.granularity.GrainConstants.GRAIN_HOUR;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.linuxbox.enkive.statistics.KeyConsolidationHandler;
import com.linuxbox.enkive.statistics.services.StatsClient;

public class HourGrain extends AbstractGrain {
	public HourGrain(StatsClient client) {
		super(client);
	}

	@Override
	protected void consolidateMaps(Map<String, Object> consolidatedData,
			Set<Map<String, Object>> serviceData, KeyConsolidationHandler keyDef,
			LinkedList<String> dataPath) {

		if (keyDef.getMethods() != null) {
			DescriptiveStatistics statsMaker = new DescriptiveStatistics();
			Object dataVal = null;

			// get data from maps and add to statMakers
			for (Map<String, Object> dataMap : serviceData) {
				dataVal = getDataVal(dataMap, dataPath);
				double input = -1;

				if (dataVal != null) {
					input = statToDouble(dataVal);
					if (input > -1) {
						statsMaker.addValue(input);
					}
				}
			}

			// loop over methods to populate map with max, min, etc.
			Map<String, Object> methodData = new HashMap<String, Object>();
			for (String method : keyDef.getMethods()) {
				methodMapBuilder(method, dataVal, statsMaker, methodData);
			}
			// store in new map on path
			putOnPath(dataPath, consolidatedData, methodData);
		}
	}

	@Override
	public void setDates() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		Date upperDate = cal.getTime();
		cal.add(Calendar.HOUR_OF_DAY, -1);
		Date lowerDate = cal.getTime();
		setDates(upperDate, lowerDate);
	}

	@Override
	public void setTypes() {
		setTypes(GRAIN_HOUR, null);
	}
}
