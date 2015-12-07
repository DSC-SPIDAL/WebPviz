package db;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import models.Group;
import org.bson.Document;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import play.Logger;

public class MongoConnection {
    public MongoCollection<Document> filesCollection;
    public MongoCollection<Document> clustersCollection;
    public MongoCollection<Document> groupsCollection;

    public static MongoConnection con = new MongoConnection();

    public static MongoConnection getInstance() {
        return con;
    }

    public MongoConnection() {
        Config conf = ConfigFactory.load();
        String mongoHost = conf.getString(Constants.DB.MONGO_HOST);
        int mongoPort = conf.getInt(Constants.DB.MONGO_PORT);

        MongoClient mongoClient;
        if (mongoHost != null) {
            Logger.info("Using mongo DB " + mongoHost + ":" + mongoPort);
            mongoClient = new MongoClient(mongoHost, mongoPort);
        } else {
            Logger.info("Using local mongo DB " + "localhost:27017");
            mongoClient = new MongoClient("localhost", 27017);
        }

        MongoDatabase db = mongoClient.getDatabase(Constants.DB.DB_NAME);

        filesCollection = db.getCollection(Constants.DB.ARTIFACTS_COLLECTION);
        clustersCollection = db.getCollection(Constants.DB.FILES_COLLECTION);
        groupsCollection = db.getCollection(Constants.DB.GROUPS_COLLECTION);
    }

    public void initGroupsCollection() {
        Group defaultGroup = new Group(0, "default", "The default group");
        if (!GroupsDAO.groupExists(defaultGroup)) {
            GroupsDAO.insertGroup(defaultGroup);
        } else {
            System.out.println("Default exists");
        }
    }

}
