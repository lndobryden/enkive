/*******************************************************************************
 * Copyright 2015 Enkive, LLC.
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
package com.linuxbox.enkive.mailprocessor.processors;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.linuxbox.enkive.exception.SocketClosedException;
import com.linuxbox.enkive.mailprocessor.AbstractMailProcessor;
import com.linuxbox.enkive.message.Message;

public class PostfixFilterProcessor extends AbstractMailProcessor {
	private static final String RECEIVED_MESSAGE = "RECVD\n";
	private static final String ARCHIVED_MESSAGE = "ARCHD\n";
	private static final String FAILED_MESSAGE = "FAILD\n";

	InputStream is;
	DataOutputStream os;

	String mailFrom;
	ArrayList<String> rcptTo;

	@Override
	protected void closeProcessor() {
		try {
			if (messageSaved)
				os.writeBytes(ARCHIVED_MESSAGE);
			else
				os.writeBytes(FAILED_MESSAGE);
			os.close();
			is.close();
		} catch (IOException e) {
			LOGGER.error("Error closing streams to filter client");
		}
	}

	@Override
	protected Message postProcess(Message message) {
		message.setMailFrom(mailFrom);
		message.setRcptTo(rcptTo);
		return message;
	}

	@Override
	protected void prepareProcessor() throws IOException {
		is = socket.getInputStream();
		os = new DataOutputStream(socket.getOutputStream());
	}

	@Override
	protected String processInput() throws SocketClosedException, IOException {
		ArrayList<String> addresses = new ArrayList<String>();
		char character;
		StringBuffer address = new StringBuffer();
		do {
			character = (char) is.read();
			final boolean isEmpty = address.length() == 0;
			if (character == ';' && !isEmpty) {
				addresses.add(address.toString());
			} else if (character != ' ') {
				address.append(character);
			} else {
				if (!isEmpty) {
					addresses.add(address.toString());
				}
				address.setLength(0);
			}
		} while (character != ';');

		mailFrom = addresses.remove(0);
		rcptTo = addresses;

		StringBuilder sb = new StringBuilder();
		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\r\n");
		}

		os.writeBytes(RECEIVED_MESSAGE);
		os.flush();

		return sb.toString();
	}
}
