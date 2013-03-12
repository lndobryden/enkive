/*******************************************************************************
 * Copyright 2013 The Linux Box Corporation.
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
package com.linuxbox.enkive.imap;

import org.apache.james.container.spring.resource.AbstractJamesResourceLoader;

/**
 * 
 * This is a dummy class for enabling StartTLS with the James IMAP server
 * 
 * @author lee
 * 
 */
public class EnkiveResourceLoader extends AbstractJamesResourceLoader {

	@Override
	public String getAbsoluteDirectory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getConfDirectory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVarDirectory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRootDirectory() {
		// TODO Auto-generated method stub
		return null;
	}

}
