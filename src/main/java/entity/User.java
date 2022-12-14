package entity;

import jdbc.Database;

public class User {
    private String id;
    private String username;
    private String password;
    private boolean business; // 1 = true, 0 = false
    private String biography;
    private String email;
    private String website;
    private String followersTableName; // id+"FollowersTable"
    private int followersNum;
    private String followingTableName; // id+"FollowingTable"
    private int followingNum;
    private int postsNum;
    private String groupsTableName; // id+"GroupsTable"
    private String Logged_in;
    private String pet;
    private String profilePicture;

    public User(String id, String username, String password, boolean business, String biography, String email, String website, String pet, String profilePicture) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.business = business;
        this.biography = biography;
        this.email = email;
        this.website = website;
        followersNum = followingNum = postsNum = 0;
        followersTableName = Database.create_followersTable(id);
        followingTableName = Database.create_followingTable(id);
        groupsTableName = Database.create_groupsTable(id);
        this.Logged_in="no";
        this.pet = pet;
        this.profilePicture = profilePicture;
    }

    public User() {}

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getBiography() {
        return biography;
    }

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return website;
    }

    public String getFollowersTableName() {
        return followersTableName;
    }

    public String getFollowingTableName() {
        return followingTableName;
    }

    public String getGroupsTableName() {
        return groupsTableName;
    }

    public String getPet() {
        return pet;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public int isBusiness() {
        return business ? 1 : 0;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setFollowersNum(int followersNum) {
        this.followersNum = followersNum;
    }

    public void setFollowingNum(int followingNum) {
        this.followingNum = followingNum;
    }

    public void setPostsNum(int postsNum) {
        this.postsNum = postsNum;
    }

    public String isLogged_in() {
        return Logged_in;
    }

    public void setLogged_in(String logged_in) {
        Logged_in = logged_in;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Override
    public String toString() {
        return "id: "+id+"\n" +
                "username: "+username+"\n" +
                "biography: "+biography+"\n" +
                "email: "+email+"\n" +
                "website: "+website+"\n" +
                "followers: "+followersNum+"\n" +
                "following: "+followingNum+"\n" +
                "posts: "+postsNum+"\n";
    }
}

