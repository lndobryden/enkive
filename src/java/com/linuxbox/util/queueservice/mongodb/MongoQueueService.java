package com.linuxbox.util.queueservice.mongodb;

import java.net.UnknownHostException;
import java.util.Date;

import org.bson.types.BSONTimestamp;
import org.bson.types.ObjectId;

import com.linuxbox.util.queueservice.AbstractQueueEntry;
import com.linuxbox.util.queueservice.QueueEntry;
import com.linuxbox.util.queueservice.QueueService;
import com.linuxbox.util.queueservice.QueueServiceException;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

public class MongoQueueService implements QueueService {
	private static final String STATUS_FIELD = "status";
	private static final String CREATED_AT_FIELD = "createdAt";
	private static final String DEQUEUED_AT_FIELD = "dequeuedAt";
	private static final String IDENTIFIER_FIELD = "identifier";
	private static final String NOTE_FIELD = "note";

	private static final int STATUS_INITIAL = 1;
	private static final int STATUS_PENDING = 2;

	@SuppressWarnings("unused")
	private static final int STATUS_COMPLETE = 3;

	private Mongo mongo;
	private DB mongoDB;
	private DBCollection queueCollection;
	private boolean createdMongo;

	public MongoQueueService(String server, int port, String database,
			String collection) throws UnknownHostException, MongoException {
		this(new Mongo(server, port), database, collection);
		createdMongo = true;
	}

	public MongoQueueService(String database, String collection)
			throws UnknownHostException, MongoException {
		this(new Mongo(), database, collection);
		createdMongo = true;
	}

	public MongoQueueService(Mongo mongo, String database, String collection) {
		this.mongo = mongo;
		this.mongoDB = mongo.getDB(database);
		this.queueCollection = mongoDB.getCollection(collection);

		DBObject statusTimestampIndex = new BasicDBObject(STATUS_FIELD, 1)
				.append(CREATED_AT_FIELD, 1).append(IDENTIFIER_FIELD, 1);
		queueCollection.ensureIndex(statusTimestampIndex,
				"statusTimestampIdentifierIndex");

		queueCollection.setWriteConcern(WriteConcern.FSYNC_SAFE);
	}

	@Override
	public void startup() throws QueueServiceException {
		// empty
	}

	@Override
	public void shutdown() throws QueueServiceException {
		if (createdMongo) {
			mongo.close();
		}
	}

	@Override
	public void enqueue(String identifier) throws QueueServiceException {
		enqueue(identifier, null);
	}

	@Override
	public void enqueue(String identifier, Object note)
			throws QueueServiceException {
		final DBObject entry = new BasicDBObject(CREATED_AT_FIELD,
				new BSONTimestamp()).append(STATUS_FIELD, STATUS_INITIAL)
				.append(IDENTIFIER_FIELD, identifier).append(NOTE_FIELD, note);
		WriteResult result = queueCollection.insert(entry);
		if (!result.getLastError().ok()) {
			throw new QueueServiceException("could not enqueue \"" + identifier
					+ "\" (note: \"" + note.toString() + "\")", result
					.getLastError().getException());
		}
	}

	@Override
	public QueueEntry dequeue() throws QueueServiceException {
		DBObject query = new BasicDBObject(STATUS_FIELD, STATUS_INITIAL);
		DBObject update = new BasicDBObject(DEQUEUED_AT_FIELD,
				new BSONTimestamp()).append(STATUS_FIELD, STATUS_PENDING);
		DBObject fields = new BasicDBObject("_id", 1)
				.append(IDENTIFIER_FIELD, 1).append(NOTE_FIELD, 1)
				.append(CREATED_AT_FIELD, 1);
		DBObject sort = new BasicDBObject(CREATED_AT_FIELD, 1);

		DBObject result = queueCollection.findAndModify(query, fields, sort,
				false, update, false, false);
		
		if (result == null) {
			return null;
		}

		BSONTimestamp createdAtBSON = (BSONTimestamp) result
				.get(CREATED_AT_FIELD);
		Date createdAt = new Date(createdAtBSON.getTime() * 1000L);

		return new MongoQueueEntry((ObjectId) result.get("_id"),
				(String) result.get(IDENTIFIER_FIELD), result.get(NOTE_FIELD),
				createdAt);
	}

	@Override
	public QueueEntry dequeue(String identifer) throws QueueServiceException {
		throw new RuntimeException("unimplemented method");
	}

	@Override
	public void finish(QueueEntry entry) throws QueueServiceException {
		if (entry instanceof MongoQueueEntry) {
			final MongoQueueEntry mongoEntry = (MongoQueueEntry) entry;
			final DBObject query = new BasicDBObject("_id", mongoEntry.mongoID);
			final DBObject result = queueCollection.findAndRemove(query);
			if (result == null) {
				throw new QueueServiceException(
						"could not finish queue entry \""
								+ entry.getIdentifier() + "\" "
								+ entry.getEnqueuedAt());
			}
		} else {
			throw new QueueServiceException(
					"tried to finish a queue entry that was not generated by this queue");
		}
	}

	static class MongoQueueEntry extends AbstractQueueEntry {
		private ObjectId mongoID;

		public MongoQueueEntry(ObjectId mongoID, String identifier,
				Object note, Date enqueuedAt) {
			super(identifier, note, enqueuedAt);
			this.mongoID = mongoID;
		}
	}
}
