package entity;

import jdbc.Database;

public class Group {
    private String id;
    private String groupName;
    private String biography;
    private String ownerID;
    private String membersTableName; // id + "MembersTable"
    private String adminsTableName; // id + "AdminsTable"
    private String messagesTableName; // id + "MessagesTable"
    private String groupPicture;

    public Group(String id, String groupName, String ownerID,String biography, String groupPicture) {
        this.id = id;
        this.groupName = groupName;
        this.biography = biography;
        this.ownerID = ownerID;
        membersTableName = Database.create_membersTable(id);
        adminsTableName = Database.create_adminsTable(id);
        messagesTableName = Database.create_messagesTable(id);
        this.groupPicture = groupPicture;
    }

    public String getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public String getMembersTableName() {
        return membersTableName;
    }

    public String getAdminsTableName() {
        return adminsTableName;
    }

    public String getMessagesTableName() {
        return messagesTableName;
    }

    public String getBiography() {
        return biography;
    }

    public String getGroupPicture() {
        return groupPicture;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    @Override
    public String toString() {
        return "@" + id + "\n" +
                groupName + "\n" +
                biography + "\n" +
                "owner: @" + ownerID;
    }
}

