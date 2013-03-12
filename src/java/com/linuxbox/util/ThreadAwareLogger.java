/*******************************************************************************
 * Copyright 2013 The Linux Box Corporation.
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
package com.linuxbox.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.Log4JLogger;

public class ThreadAwareLogger implements Log {
	private Log actualLog;

	public ThreadAwareLogger(String name) {
		actualLog = new Log4JLogger(name);
	}

	Object process(Object o) {
		if (o instanceof String) {
			StringBuilder s = new StringBuilder((String) o);
			s.insert(0, "] ");
			s.insert(0, Thread.currentThread().getId());
			s.insert(0, "[thread ");
			return s.toString();
		} else {
			return o;
		}
	}

	@Override
	public void debug(Object logObj) {
		actualLog.debug(process(logObj));
	}

	@Override
	public void debug(Object logObj, Throwable thrown) {
		actualLog.debug(process(logObj), thrown);
	}

	@Override
	public void error(Object logObj) {
		actualLog.error(process(logObj));
	}

	@Override
	public void error(Object logObj, Throwable thrown) {
		actualLog.error(process(logObj), thrown);
	}

	@Override
	public void fatal(Object logObj) {
		actualLog.fatal(process(logObj));
	}

	@Override
	public void fatal(Object logObj, Throwable thrown) {
		actualLog.fatal(process(logObj), thrown);
	}

	@Override
	public void info(Object logObj) {
		actualLog.info(process(logObj));
	}

	@Override
	public void info(Object logObj, Throwable thrown) {
		actualLog.info(process(logObj), thrown);

	}

	@Override
	public void trace(Object logObj) {
		actualLog.trace(process(logObj));
	}

	@Override
	public void trace(Object logObj, Throwable thrown) {
		actualLog.trace(process(logObj), thrown);
	}

	@Override
	public void warn(Object logObj) {
		actualLog.warn(process(logObj));
	}

	@Override
	public void warn(Object logObj, Throwable thrown) {
		actualLog.warn(process(logObj), thrown);
	}

	@Override
	public boolean isDebugEnabled() {
		return actualLog.isDebugEnabled();
	}

	@Override
	public boolean isErrorEnabled() {
		return actualLog.isErrorEnabled();
	}

	@Override
	public boolean isFatalEnabled() {
		return actualLog.isFatalEnabled();
	}

	@Override
	public boolean isInfoEnabled() {
		return actualLog.isInfoEnabled();
	}

	@Override
	public boolean isTraceEnabled() {
		return actualLog.isTraceEnabled();
	}

	@Override
	public boolean isWarnEnabled() {
		return actualLog.isWarnEnabled();
	}
}
