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
 ******************************************************************************/
package com.linuxbox.util.queueservice.mongodb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.linuxbox.util.queueservice.QueueEntry;
import com.linuxbox.util.queueservice.QueueServiceException;

public class JavaQueueServiceTest {

	private static JavaQueueService service;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		service = new JavaQueueService();
		// Don't start it up; that needs a docstore service
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		// empty
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testEnqueueWithNotes() throws Exception {
		Date now = new Date();

		service.enqueue("foo", 200, 3);
		Thread.sleep(1001);
		service.enqueue("bar", 300, 3.14);
		Thread.sleep(1001);
		service.enqueue("bar", -400, now);

		QueueEntry entry1 = service.dequeue();
		assertNotNull(entry1);
		assertEquals("foo", entry1.getIdentifier());
		assertTrue(entry1.getNote() instanceof Integer);
		assertEquals((Integer) entry1.getNote(), (Integer) 3);
		Date entry1At = entry1.getEnqueuedAt();
		service.finishEntry(entry1);

		QueueEntry entry2 = service.dequeue();
		assertNotNull(entry2);
		assertEquals("bar", entry2.getIdentifier());
		assertTrue(entry2.getNote() instanceof Double);
		assertEquals(((Double) entry2.getNote()).doubleValue(), 3.14, 0.0001);
		Date entry2At = entry2.getEnqueuedAt();
		service.finishEntry(entry2);

		QueueEntry entry3 = service.dequeue();
		assertNotNull(entry3);
		assertEquals("bar", entry3.getIdentifier());
		assertTrue(entry3.getNote() instanceof Date);
		assertEquals((Date) entry3.getNote(), now);
		Date entry3At = entry3.getEnqueuedAt();
		service.finishEntry(entry3);

		QueueEntry entry4 = service.dequeue();
		assertNull(entry4);

		/*
		 * this assertion is not guaranteed to work since MongoDB rounds (down?)
		 * to the nearest second, so entry1At rounded down could be less than
		 * now!
		 */
		// assertTrue(now.compareTo(entry1At) <= 0);

		assertTrue(entry1At.compareTo(entry2At) < 0);
		assertTrue(entry2At.compareTo(entry3At) < 0);
	}

	@Test
	public void testEnqueueWithoutNote() throws Exception {
		service.enqueue("foo");

		QueueEntry entry1 = service.dequeue();
		assertNotNull(entry1);
		assertEquals("foo", entry1.getIdentifier());
		assertNull(entry1.getNote());
		service.finishEntry(entry1);

		QueueEntry entry2 = service.dequeue();
		assertNull(entry2);
	}

	@Test
	public void testDequeueWithIdentifier() throws Exception {
		Date now = new Date();

		service.enqueue("foo", 200, 3);
		Thread.sleep(1001);
		service.enqueue("bar", -300, 3.14);
		Thread.sleep(1001);
		service.enqueue("bar", 400, now);

		QueueEntry entry2 = service.dequeue("bar");
		assertNotNull(entry2);
		assertEquals("make sure we got the second item by identifer", "bar",
				entry2.getIdentifier());
		assertTrue("make sure we got the second item by note's type",
				entry2.getNote() instanceof Double);
		assertEquals("make sure we got the second item by note's value",
				((Double) entry2.getNote()).doubleValue(), 3.14, 0.0001);
		Date entry2At = entry2.getEnqueuedAt();

		QueueEntry entry3 = service.dequeue("bar");
		assertNotNull(entry3);
		assertEquals("bar", entry3.getIdentifier());
		assertTrue(entry3.getNote() instanceof Date);
		assertEquals((Date) entry3.getNote(), now);
		Date entry3At = entry3.getEnqueuedAt();

		QueueEntry entry4 = service.dequeue("bar");
		assertNull(entry4);

		QueueEntry entry1 = service.dequeue("foo");
		assertNotNull(entry1);
		assertEquals("foo", entry1.getIdentifier());
		assertTrue(entry1.getNote() instanceof Integer);
		assertEquals((Integer) entry1.getNote(), (Integer) 3);
		Date entry1At = entry1.getEnqueuedAt();

		QueueEntry entry5 = service.dequeue();
		assertNull(entry5);

		// finish them in whatever order

		service.finishEntry(entry1);
		service.finishEntry(entry2);
		service.finishEntry(entry3);

		assertTrue(entry1At.compareTo(entry2At) < 0);
		assertTrue(entry2At.compareTo(entry3At) < 0);
	}

	@Test(expected = QueueServiceException.class)
	public void testBadFinish() throws Exception {
		service.enqueue("bazooka");

		QueueEntry entry1 = service.dequeue();
		assertNotNull(entry1);
		assertEquals("bazooka", entry1.getIdentifier());
		service.finishEntry(new QueueEntry() {
			@Override
			public Object getNote() {
				return null;
			}

			@Override
			public String getIdentifier() {
				return null;
			}

			@Override
			public Date getEnqueuedAt() {
				return null;
			}

			@Override
			public int getShardKey() {
				return 0;
			}
		});
	}
}
