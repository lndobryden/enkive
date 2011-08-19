/*
 *  Copyright 2011 The Linux Box Corporation.
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

package com.linuxbox.enkive.importer;

import static com.linuxbox.enkive.mailprocessor.MailDirConstants.END_OF_STREAM_INDICATOR;
import static com.linuxbox.enkive.mailprocessor.MailDirConstants.HAS_END_OF_STREAM_REPLACEMENT_REGEX;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.mail.Message;
import javax.mail.MessagingException;

public abstract class AbstractMessageImporter {

	protected BufferedWriter writer;
	protected InetAddress host;
	protected int port;
	Socket socket = null;

	AbstractMessageImporter(String host, String port)
			throws UnknownHostException {
		this(InetAddress.getByName(host), Integer.parseInt(port));
	}

	AbstractMessageImporter(InetAddress host, int port) {
		this.host = host;
		this.port = port;
	}

	protected void sendMessage(Message m) throws IOException,
			MessagingException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		m.writeTo(os);
		os.close();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new ByteArrayInputStream(os.toByteArray())));
		sendMessage(reader);
		reader.close();
	}

	protected void sendMessage(File m) throws IOException, MessagingException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(m)));
		sendMessage(reader);
		reader.close();
	}

	protected void sendMessage(BufferedReader reader) throws IOException {
		String tmp;
		// Server expects MAIL_FROM and RCPT_TO on first line
		writer.write(";");
		while ((tmp = reader.readLine()) != null) {
			if (HAS_END_OF_STREAM_REPLACEMENT_REGEX.matcher(tmp.trim())
					.matches()) {
				int index = tmp.indexOf(END_OF_STREAM_INDICATOR);
				// add an extra "."
				tmp = tmp.substring(0, index) + END_OF_STREAM_INDICATOR
						+ tmp.substring(index);
			}
			writer.write(tmp + "\r\n");
		}
		writer.write(END_OF_STREAM_INDICATOR + "\r\n");
		writer.flush();
	}

	protected void setWriter() throws IOException {
		socket = new Socket(host, port);
		socket.setSendBufferSize(8);
		writer = new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream()));
	}

	protected void closeWriter() {
		if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
