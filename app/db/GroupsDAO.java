package db;

import com.mongodb.client.FindIterable;
import models.Group;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;


public class GroupsDAO {
    public static boolean groupExists(Group group) {
        if (Constants.Group.DEFAULT_GROUP.equals(group.name)) {
            return true;
        }
        MongoConnection db = MongoConnection.getInstance();
        Document groupDocument = new Document();
        groupDocument.append(Constants.Group.NAME, group.name);
        groupDocument.append(Constants.Group.USER, group.userId);
        FindIterable<Document> iterable = db.groupsCol.find(groupDocument);
        return iterable.iterator().hasNext();
    }

    public static void insertGroup(Group group) {
        MongoConnection db = MongoConnection.getInstance();
        Document groupDocument = new Document();
        groupDocument.append(Constants.Group.NAME, group.name);
        groupDocument.append(Constants.Group.DESCRIPTION, group.description);
        groupDocument.append(Constants.Group.USER, group.userId);
        db.groupsCol.insertOne(groupDocument);
    }

    public static void updateGroup(Group oldGroup, Group newGroup) {
        MongoConnection db = MongoConnection.getInstance();
        Document oldGroupDocument = new Document();
        oldGroupDocument.append(Constants.Group.NAME, oldGroup.name);
        oldGroupDocument.append(Constants.Group.USER, oldGroup.userId);

        Document groupDocument = new Document();
        groupDocument.append(Constants.Group.NAME, newGroup.name);
        groupDocument.append(Constants.Group.DESCRIPTION, newGroup.description);
        groupDocument.append(Constants.Group.USER, newGroup.userId);

        db.groupsCol.findOneAndReplace(oldGroupDocument, groupDocument);
    }

    public static void deleteGroup(Group group) {
        MongoConnection db = MongoConnection.getInstance();
        Document groupDocument = new Document();
        groupDocument.append(Constants.Group.NAME, group.name);
        groupDocument.append(Constants.Group.USER, group.userId);
        db.groupsCol.deleteOne(groupDocument);
    }

    public static List<Group> allGroups() {
        MongoConnection db = MongoConnection.getInstance();
        FindIterable<Document> iterable =  db.groupsCol.find();
        List<Group> groups = new ArrayList<Group>();
        for (Document d : iterable) {
            Group group = new Group();
            String name = (String) d.get(Constants.Group.NAME);
            int user = (int) d.get(Constants.Group.USER);
            group.description = (String) d.get(Constants.Group.DESCRIPTION);
            group.name = name;
            group.userId = user;
            groups.add(group);
            System.out.println(group.name);
        }
        return groups;
    }
}
