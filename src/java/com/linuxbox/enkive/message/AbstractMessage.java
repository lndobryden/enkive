/*
 *  Copyright 2010 The Linux Box Corporation.
 *
 *  This file is part of Enkive CE (Community Edition).
 *
 *  Enkive CE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of
 *  the License, or (at your option) any later version.
 *
 *  Enkive CE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public
 *  License along with Enkive CE. If not, see
 *  <http://www.gnu.org/licenses/>.
 */

package com.linuxbox.enkive.message;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.james.mime4j.message.Header;

import com.linuxbox.enkive.exception.BadMessageException;

public abstract class AbstractMessage extends AbstractMessageSummary
		implements Message {
	protected String originalHeaders;
	protected String mimeVersion;
	protected ContentHeader contentHeader;
	protected Header parsedHeader;

	public AbstractMessage() {
		super();
		mailFrom = "";
		rcptTo = new ArrayList<String>();
		to = new ArrayList<String>();
		cc = new ArrayList<String>();
	}

	@Override
	public String getMimeVersion() {
		return mimeVersion;
	}

	@Override
	public String getOriginalHeaders() {
		return originalHeaders;
	}

	@Override
	public void setOriginalHeaders(String originalHeaders)
			throws BadMessageException, IOException {
		this.originalHeaders = originalHeaders;
		parseHeaders(originalHeaders);
	}

	@Override
	public ContentHeader getContentHeader() {
		return contentHeader;
	}

	@Override
	public void setContentHeader(ContentHeader contentHeader) {
		this.contentHeader = contentHeader;
	}

	@Override
	public void setMimeVersion(String mimeVersion) {
		this.mimeVersion = mimeVersion;
	}

	@Override
	public void pushReconstitutedEmail(Writer output) throws IOException {
		PrintWriter writer = new PrintWriter(output);
		writer.print(originalHeaders);
		writer.flush();
		contentHeader.pushReconstitutedEmail(writer);
	}

	@Override
	public String getReconstitutedEmail() throws IOException {
		StringWriter out = new StringWriter();
		pushReconstitutedEmail(out);
		return out.toString();
	}

	public void setParsedHeader(Header parsedHeader) {
		this.parsedHeader = parsedHeader;
	}

	public Header getParsedHeader() {
		return parsedHeader;
	}

	public Set<String> getAttachmentFileNames() {
		Set<String> result = new HashSet<String>();

		return result;
	}

	public Set<String> getAttachmentMimeTypes() {
		Set<String> result = new HashSet<String>();

		return result;
	}

	/**
	 * Parses the original headers and puts data in the attributes.
	 * 
	 * @param originalHeaders
	 * @throws IOException
	 * @throws BadMessageException
	 */
	protected abstract void parseHeaders(String originalHeaders)
			throws IOException, BadMessageException;

}