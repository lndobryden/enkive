/*******************************************************************************
 * Copyright 2012 The Linux Box Corporation.
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
package com.linuxbox.enkive;

public interface TestingConstants {

	String TEST_MESSAGE_DIRECTORY = "test/data/unitTestData";
	String TEST_EMERGENCY_SAVE_ROOT = "test/emergencySaveFiles";

	String MONGODB_TEST_DATABASE = "enkive-test";
	String MONGODB_TEST_MESSAGES_COLLECTION = "messages-test";
	String MONGODB_TEST_DOCUMENTS_COLLECTION = "documents-test";
	String MONGODB_TEST_LOCK_COLLECTION = "lock-test";
	String MONGODB_TEST_AUDIT_COLLECTION = "audit-test";
	String MONGODB_TEST_QUEUE_COLLECTION = "queue-test";
	String MONGODB_TEST_FSFILES_COLLECTION = "documents-test.files";
	String MONGODB_TEST_CHUNKS_COLLECTION = "documents-test.chunks";

}