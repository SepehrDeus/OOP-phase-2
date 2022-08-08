package jdbc;
import entity.*;
import java.sql.*;
public class Database {
    private static Connection connection;

    public static void setConnection(Connection connection) {
        Database.connection = connection;
    }

    // USERS
    public static int add_user(User user) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO users VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0, ?, 0, 0, ?, ?, ?, ?)"
        );
        preparedStatement.setString(1, user.getId());
        preparedStatement.setString(2, user.getUsername());
        preparedStatement.setString(3, user.getPassword());
        preparedStatement.setInt(4, user.isBusiness());
        preparedStatement.setString(5, user.getBiography());
        preparedStatement.setString(6, user.getEmail());
        preparedStatement.setString(7, user.getWebsite());
        preparedStatement.setString(8, user.getFollowersTableName());
        preparedStatement.setString(9, user.getFollowingTableName());
        preparedStatement.setString(10, user.getGroupsTableName());
        preparedStatement.setString(11, "no");
        preparedStatement.setString(12, user.getPet());
        preparedStatement.setString(13, user.getProfilePicture());

        return preparedStatement.executeUpdate();
    }

    public static int delete_user(String id) throws SQLException {
        delete_from_followers(id);
        delete_from_following(id);


        PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM users WHERE id=?"
        );
        preparedStatement.setString(1, id);
        return preparedStatement.executeUpdate() + delete_followersTable(id) + delete_followingTable(id);
    }

    private static int delete_followersTable(String id) throws SQLException {
        String followersTableName = id+"FollowersTable";
        Statement statement = connection.createStatement();
        return statement.executeUpdate(
                "DROP TABLE "+followersTableName
        );
    }

    private static int delete_followingTable(String id) throws SQLException {
        String followingTableName = id+"FollowingTable";
        Statement statement = connection.createStatement();
        return statement.executeUpdate(
                "DROP TABLE "+followingTableName
        );
    }

    private static void delete_from_followers(String id) throws SQLException {
        String followingTableName = id+"FollowingTable";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "SELECT * FROM "+followingTableName
        );
        while (resultSet.next()) {
            unfollow(id, resultSet.getString(1));
        }
    }

    private static void delete_from_following(String id) throws SQLException {
        String followersTableName = id+"FollowersTable";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "SELECT * FROM "+followersTableName
        );
        while (resultSet.next()) {
            unfollow(resultSet.getString(1), id);
        }
    }

    public static boolean user_exists(String id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT COUNT(id) FROM users WHERE id=?"
        );
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1) > 0;
    }

    public static ResultSet get_Users () throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT id, profilePicture FROM users",ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE
        );
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public static User get_user_by_id(String id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM users WHERE id=?"
        );
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        User user = new User();
        user.setId(resultSet.getString("id"));
        user.setUsername(resultSet.getString("username"));
        user.setBiography(resultSet.getString("biography"));
        user.setEmail(resultSet.getString("email"));
        user.setWebsite(resultSet.getString("website"));
        user.setFollowersNum(resultSet.getInt("followersNum"));
        user.setFollowingNum(resultSet.getInt("followingNum"));
        user.setPostsNum(resultSet.getInt("postsNum"));
        user.setProfilePicture(resultSet.getString("profilePicture"));

        return user;
    }

    public static int number_of_users() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "SELECT COUNT(*) FROM users"
        );
        resultSet.next();
        return resultSet.getInt(1);
    }

    public static boolean check_password(String id, String password) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT password FROM users WHERE id=?"
        );
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString(1).equals(password);
    }

    public static int update_username(String id, String newUsername) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE users SET username=? WHERE id=?"
        );
        preparedStatement.setString(1, newUsername);
        preparedStatement.setString(2, id);
        return preparedStatement.executeUpdate();
    }

    public static int update_password(String id, String newPass) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE users SET password=? WHERE id=?"
        );
        preparedStatement.setString(1, newPass);
        preparedStatement.setString(2, id);
        return preparedStatement.executeUpdate();
    }

    public static int update_biography(String id, String biography) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE users SET biography=? WHERE id=?"
        );
        preparedStatement.setString(1, biography);
        preparedStatement.setString(2, id);
        return preparedStatement.executeUpdate();
    }

    public static int update_email(String id, String email) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE users SET email=? WHERE id=?"
        );
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, id);
        return preparedStatement.executeUpdate();
    }

    public static int update_website(String id, String website) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE users SET website=? WHERE id=?"
        );
        preparedStatement.setString(1, website);
        preparedStatement.setString(2, id);
        return preparedStatement.executeUpdate();
    }

    public static int update_profilePicture(String id, String profilePicture) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE users SET profilePicture=? WHERE id=?"
        );
        preparedStatement.setString(1, profilePicture);
        preparedStatement.setString(2, id);
        return preparedStatement.executeUpdate();
    }

    public static String create_followersTable(String userID) {
        try {
            JDBC.initFollowersTable(userID.trim()+"FollowersTable");
            return userID + "FollowersTable";
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String create_followingTable(String userID) {
        try {
            JDBC.initFollowingTable(userID.trim()+"FollowingTable");
            return userID + "FollowingTable";
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isFollowed(String followerID, String followingID) throws SQLException {
        String followingTableName = followerID+"FollowingTable";
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT COUNT(followingID) FROM "+followingTableName+" WHERE followingID=?"
        );
        preparedStatement.setString(1, followingID);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1) > 0;
    }

    public static boolean follow(String followerID, String followingID) throws SQLException {
        String followingTableName = followerID+"FollowingTable";
        String followersTableName = followingID+"FollowersTable";

        PreparedStatement preparedStatement1 = connection.prepareStatement(
                "INSERT INTO "+followingTableName+" VALUE (?)"
        );
        preparedStatement1.setString(1, followingID);

        PreparedStatement preparedStatement2 = connection.prepareStatement(
                "INSERT INTO "+followersTableName+" VALUE (?)"
        );
        preparedStatement2.setString(1, followerID);

        return preparedStatement1.executeUpdate()>0 && preparedStatement2.executeUpdate()>0 &&
                increase_followersNum(followingID)>0 && increase_followingNum(followerID)>0;
    }

    public static boolean unfollow(String followerID, String followingID) throws SQLException {
        String followingTableName = followerID+"FollowingTable";
        String followersTableName = followingID+"FollowersTable";

        PreparedStatement preparedStatement1 = connection.prepareStatement(
                "DELETE FROM "+followingTableName+" WHERE followingID=?"
        );
        preparedStatement1.setString(1, followingID);

        PreparedStatement preparedStatement2 = connection.prepareStatement(
                "DELETE FROM "+followersTableName+" WHERE followersID=?"
        );
        preparedStatement2.setString(1, followerID);

        return preparedStatement1.executeUpdate()>0 && preparedStatement2.executeUpdate()>0 &&
                decrease_followersNum(followingID)>0 && decrease_followingNum(followerID)>0;
    }

    private static int increase_followersNum(String id) throws SQLException {
        PreparedStatement preparedStatement1 = connection.prepareStatement(
                "SELECT followersNum FROM users WHERE id=?"
        );
        preparedStatement1.setString(1, id);

        ResultSet resultSet = preparedStatement1.executeQuery();
        resultSet.next();
        int newFollowersNum = resultSet.getInt("followersNum")+1;

        PreparedStatement preparedStatement2 = connection.prepareStatement(
                "UPDATE users SET followersNum=? WHERE id=?"
        );
        preparedStatement2.setInt(1, newFollowersNum);
        preparedStatement2.setString(2, id);

        return preparedStatement2.executeUpdate();
    }

    private static int decrease_followersNum(String id) throws SQLException {
        PreparedStatement preparedStatement1 = connection.prepareStatement(
                "SELECT followersNum FROM users WHERE id=?"
        );
        preparedStatement1.setString(1, id);

        ResultSet resultSet = preparedStatement1.executeQuery();
        resultSet.next();
        int newFollowersNum = resultSet.getInt("followersNum")-1;

        PreparedStatement preparedStatement2 = connection.prepareStatement(
                "UPDATE users SET followersNum=? WHERE id=?"
        );
        preparedStatement2.setInt(1, newFollowersNum);
        preparedStatement2.setString(2, id);

        return preparedStatement2.executeUpdate();
    }

    private static int increase_followingNum(String id) throws SQLException {
        PreparedStatement preparedStatement1 = connection.prepareStatement(
                "SELECT followingNum FROM users WHERE id=?"
        );
        preparedStatement1.setString(1, id);

        ResultSet resultSet = preparedStatement1.executeQuery();
        resultSet.next();
        int newFollowingNum = resultSet.getInt("followingNum")+1;

        PreparedStatement preparedStatement2 = connection.prepareStatement(
                "UPDATE users SET followingNum=? WHERE id=?"
        );
        preparedStatement2.setInt(1, newFollowingNum);
        preparedStatement2.setString(2, id);

        return preparedStatement2.executeUpdate();
    }

    private static int decrease_followingNum(String id) throws SQLException {
        PreparedStatement preparedStatement1 = connection.prepareStatement(
                "SELECT followingNum FROM users WHERE id=?"
        );
        preparedStatement1.setString(1, id);

        ResultSet resultSet = preparedStatement1.executeQuery();
        resultSet.next();
        int newFollowingNum = resultSet.getInt("followingNum")-1;

        PreparedStatement preparedStatement2 = connection.prepareStatement(
                "UPDATE users SET followingNum=? WHERE id=?"
        );
        preparedStatement2.setInt(1, newFollowingNum);
        preparedStatement2.setString(2, id);

        return preparedStatement2.executeUpdate();
    }

    public static String create_groupsTable(String userID) {
        try {
            JDBC.initGroupsTable(userID.trim()+"GroupsTable");
            return userID.trim()+"GroupsTable";
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet get_followersTable(String id) throws SQLException {
        String followersTableName = id+"FollowersTable";
        Statement statement = connection.createStatement();
        return statement.executeQuery(
                "SELECT * FROM "+followersTableName
        );
    }

    public static ResultSet get_followingTable(String id) throws SQLException {
        String followingTableName = id +"FollowingTable";
        Statement statement = connection.createStatement();
        return statement.executeQuery(
                "SELECT * FROM "+followingTableName
        );
    }

    public static int Update_logged_in_yes(String userid) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE users " +
                        "SET Logged_in='yes' " +
                        "WHERE id=?"
        );
        preparedStatement.setString(1,userid);
        return preparedStatement.executeUpdate();
    }

    public static int Update_logged_in_no(String userid) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE users " +
                        "SET Logged_in='no' " +
                        "WHERE id=?"
        );
        preparedStatement.setString(1,userid);
        return preparedStatement.executeUpdate();
    }

    public static String user_loggedIn() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT id FROM users WHERE Logged_in=?"
        );
        preparedStatement.setString(1,"yes");
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            return  resultSet.getString("id");
        }else {
            return "";
        }
    }

    public static String check_pet(String id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT Pet FROM users WHERE id=?"
        );
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString(1);
    }











    // MESSAGES
    public static int add_message(Message message) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO messages(text, time, senderID, receiverID) " +
                        "VALUES(?, ?, ?, ?)"
        );
        preparedStatement.setString(1, message.getText());
        preparedStatement.setString(2, message.getTime());
        preparedStatement.setString(3, message.getSenderID());
        preparedStatement.setString(4, message.getReceiverID());

        return preparedStatement.executeUpdate();
    }

    public static ResultSet received_messages(String receiverID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM messages WHERE receiverID=? ORDER BY id DESC"
        );
        preparedStatement.setString(1, receiverID);
        return preparedStatement.executeQuery();
    }

    public static ResultSet sent_messages(String senderID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM messages WHERE  senderID=? ORDER BY id DESC"
        );
        preparedStatement.setString(1, senderID);
        return preparedStatement.executeQuery();
    }













    // GROUPS
    public static int add_group(Group group) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO groupsAll VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
        );
        preparedStatement.setString(1, group.getId());
        preparedStatement.setString(2, group.getGroupName());
        preparedStatement.setString(3, group.getBiography());
        preparedStatement.setString(4, group.getOwnerID());
        preparedStatement.setString(5, group.getMembersTableName());
        preparedStatement.setString(6, group.getAdminsTableName());
        preparedStatement.setString(7, group.getMessagesTableName());
        preparedStatement.setString(8, group.getGroupPicture());

        return preparedStatement.executeUpdate();
    }

    public static boolean group_exists(String id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT COUNT(id) FROM groupsAll WHERE id=?"
        );
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1) > 0;
    }

    public static String create_membersTable(String id) {
        try {
            JDBC.initMembersTable(id + "MembersTable");
            return id + "MembersTable";
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String create_adminsTable(String id) {
        try {
            JDBC.initAdminsTable(id + "AdminsTable");
            return id + "AdminsTable";
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int join_group(String userID, String groupID) throws SQLException {
        String membersTableName = groupID + "MembersTable";
        PreparedStatement preparedStatement1 = connection.prepareStatement(
                "INSERT INTO "+membersTableName+" VALUE (?)"
        );
        preparedStatement1.setString(1, userID);

        String groupsTableName = userID+"GroupsTable";
        PreparedStatement preparedStatement2 = connection.prepareStatement(
                "INSERT INTO "+groupsTableName+" VALUE (?)"
        );
        preparedStatement2.setString(1, groupID);
        return preparedStatement1.executeUpdate() + preparedStatement2.executeUpdate();
    }

    public static int add_admin(String userID, String groupID) throws SQLException {
        String adminsTableName = groupID + "AdminsTable";
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO "+adminsTableName+" VALUE (?)"
        );
        preparedStatement.setString(1, userID);
        return preparedStatement.executeUpdate();
    }

    public static ResultSet joined_groups(String userID) throws SQLException {
        String groupsTableName = userID+"GroupsTable";
        Statement statement = connection.createStatement();
        return statement.executeQuery("SELECT * FROM "+groupsTableName);
    }

    public static boolean isJoined(String userID, String groupID) throws SQLException {
        String groupsTableName = userID+"GroupsTable";
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT COUNT(groupsID) FROM "+groupsTableName+" WHERE groupsID=?"
        );
        preparedStatement.setString(1, groupID);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1) > 0;
    }

    public static boolean isAdmin(String userID, String groupID) throws SQLException {
        String adminsTableName = groupID + "AdminsTable";
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT COUNT(adminsID) FROM "+adminsTableName+" WHERE adminsID=?"
        );
        preparedStatement.setString(1, userID);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1) > 0;
    }

    public static Group get_group(String id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM groupsAll WHERE id=?"
        );
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        Group group = new Group(
                resultSet.getString("id"),
                resultSet.getString("groupName"),
                resultSet.getString("ownerID"),
                resultSet.getString("biography"),
                resultSet.getString("groupPicture")
        );
        group.setBiography(resultSet.getString("biography"));
        return group;
    }

    public static int update_groupName(String groupID, String newGroupName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE groupsAll SET groupName=? WHERE id=?"
        );
        preparedStatement.setString(1, newGroupName);
        preparedStatement.setString(2, groupID);
        return preparedStatement.executeUpdate();
    }

    public static int update_groupBiography(String groupID, String biography) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE groupsAll SET biography=? WHERE id=?"
        );
        preparedStatement.setString(1, biography);
        preparedStatement.setString(2, groupID);
        return preparedStatement.executeUpdate();
    }

    public static int update_groupPicture(String groupID, String groupPicture) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE groupsAll SET groupPicture=? WHERE id=?"
        );
        preparedStatement.setString(1, groupPicture);
        preparedStatement.setString(2, groupID);
        return preparedStatement.executeUpdate();
    }

    public static ResultSet get_membersTable(String groupID) throws SQLException {
        String membersTableName = groupID + "MembersTable";
        Statement statement = connection.createStatement();
        return statement.executeQuery(
                "SELECT * FROM "+membersTableName
        );
    }

    public static ResultSet get_adminsTable(String groupID) throws SQLException {
        String adminsTableName = groupID + "AdminsTable";
        Statement statement = connection.createStatement();
        return statement.executeQuery(
                "SELECT * FROM "+adminsTableName
        );
    }

    public static boolean isOwner(String userID, String groupID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT ownerID FROM groupsAll WHERE id=?"
        );
        preparedStatement.setString(1,groupID);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString(1).equals(userID);
    }

    public static int leave(String userID, String groupID) throws SQLException {
        String membersTableName = groupID + "MembersTable";
        PreparedStatement preparedStatement1 = connection.prepareStatement(
                "DELETE FROM "+membersTableName+" WHERE membersID=?"
        );
        preparedStatement1.setString(1, userID);

        String adminsTableName = groupID + "AdminsTable";
        PreparedStatement preparedStatement2 = connection.prepareStatement(
                "DELETE FROM "+adminsTableName+" WHERE adminsID=?"
        );
        preparedStatement2.setString(1, userID);

        String groupsTableName = userID+"GroupsTable";
        PreparedStatement preparedStatement3 = connection.prepareStatement(
                "DELETE FROM "+groupsTableName+" WHERE groupsID=?"
        );
        preparedStatement3.setString(1, groupID);

        return preparedStatement1.executeUpdate() + preparedStatement2.executeUpdate() + preparedStatement3.executeUpdate(); // > 1
    }

    public static int remove_admin(String adminID, String groupID) throws SQLException {
        String adminsTableName = groupID + "AdminsTable";
        PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM "+adminsTableName+" WHERE adminsID=?"
        );
        preparedStatement.setString(1,adminID);
        return preparedStatement.executeUpdate();
    }

    public static int kick_member(String memberID, String groupID) throws SQLException {
        String membersTableName = groupID + "MembersTable";
        PreparedStatement preparedStatement1 = connection.prepareStatement(
                "DELETE FROM "+membersTableName+" WHERE membersID=?"
        );
        preparedStatement1.setString(1, memberID);

        String groupsTableName = memberID+"GroupsTable";
        PreparedStatement preparedStatement2 = connection.prepareStatement(
                "DELETE FROM "+groupsTableName+" WHERE groupsID=?"
        );
        preparedStatement2.setString(1, groupID);

        return preparedStatement1.executeUpdate() + preparedStatement2.executeUpdate();
    }

    public static int kick_admin(String adminID, String groupID) throws SQLException {
        return leave(adminID,groupID);
    }

    public static String create_messagesTable(String id) {
        try {
            JDBC.initGroupMessagesTable(id+"MessagesTable");
            return id+"MessagesTable";
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int send_message_to_group(Message message) throws SQLException {
        String messagesTableName = message.getReceiverID() + "MessagesTable";
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO "+messagesTableName+" (text, time, senderID) VALUES (?, ?, ?)"
        );
        preparedStatement.setString(1, message.getText());
        preparedStatement.setString(2, message.getTime());
        preparedStatement.setString(3, message.getSenderID());

        return preparedStatement.executeUpdate();
    }

    public static ResultSet get_groupMessagesTable(String groupID) throws SQLException {
        String messagesTableName = groupID + "MessagesTable";
        Statement statement = connection.createStatement();
        return statement.executeQuery(
                "SELECT * FROM "+messagesTableName+" ORDER BY id DESC"
        );
    }

    public static int destroy(String groupID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM groupsAll WHERE id=?"
        );
        preparedStatement.setString(1, groupID);

        String messagesTableName = groupID + "MessagesTable";
        Statement statement1 = connection.createStatement();
        int s1 = statement1.executeUpdate(
                "DROP TABLE "+messagesTableName
        );
        statement1.close();

        String membersTableName = groupID + "MembersTable";
        Statement statement2 = connection.createStatement();
        int s2 = statement2.executeUpdate(
                "DROP TABLE "+membersTableName
        );
        statement2.close();

        String adminsTableName = groupID + "AdminsTable";
        Statement statement3 = connection.createStatement();
        int s3 = statement3.executeUpdate(
                "DROP TABLE "+adminsTableName
        );
        statement3.close();

        return preparedStatement.executeUpdate() + s1 + s2 + s3;
    }













    //  POSTS
    public static int get_postNum (String userID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT postsNum FROM users WHERE id=?"
        );
        preparedStatement.setString(1,userID);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return  resultSet.getInt("postsNum");
    }
    public static ResultSet get_ADposts() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT id, posterid, likesNum, viewsNum, field, timy, pictureid, caption FROM posts WHERE ad=?",
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE
        );
        preparedStatement.setInt(1, 1);
        return preparedStatement.executeQuery();
    }
    public static ResultSet getLikes() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM Likes",
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE
        );
        return preparedStatement.executeQuery();
    }
    public static int Update_postNum(String userid,int prepostNum) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE users " +
                        "SET postsNum=? " +
                        "WHERE id=?"
        );
        int a=prepostNum+1;
        preparedStatement.setString(2,userid);
        preparedStatement.setInt(1,a);
        return preparedStatement.executeUpdate();
    }

    public static boolean check_ads(String id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT business FROM users WHERE id=?"
        );
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt("business")==1;
    }

    public static boolean check_existence_post(String id_post) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM posts WHERE id=?"
        );
        preparedStatement.setString(1, id_post);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    public static boolean check_existence_comment(String id_post) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM comments WHERE id=?"
        );
        preparedStatement.setString(1, id_post);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }
    public static int add_post(Post post) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO  posts VALUES(?, ?, ?, ?, ?, 0, 0, ?, ?, ?, 0)"
        );
        preparedStatement.setString(1,post.getPostID() );
        preparedStatement.setString(2, post.getPosterID() );
        preparedStatement.setInt(3, post.isAd());
        preparedStatement.setString(4,post.getCaption() );
        preparedStatement.setString(5, post.getPicture());
        preparedStatement.setString(6, post.getTime());
        preparedStatement.setString(7, post.getLocation());
        preparedStatement.setString(8, post.getField());
        return preparedStatement.executeUpdate();
    }
    public static ResultSet SHOW_LATEST_posts_10 ()throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM posts ORDER BY Timy DESC"
        );
        return preparedStatement.executeQuery();
    }
    public static boolean CheckForDuplicateLike (String postCommentID,String LikerID)throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM Likes WHERE post_OR_comment_ID=? AND Liker_ID=?"
        );
        preparedStatement.setString(1,postCommentID);
        preparedStatement.setString(2,LikerID);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    public static int Update_post_view (String ID,int views)throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE posts SET viewsNum=? WHERE id=?"
        );
        preparedStatement.setString(2,ID);
        preparedStatement.setInt(1,views+1);
        return preparedStatement.executeUpdate();
    }
    public static int delete_Post(String id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM posts WHERE id=?"
        );
        preparedStatement.setString(1, id);
        return preparedStatement.executeUpdate();
    }
    // Updating caption of post
    public static int Update_Caption(String new_caption,String id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE posts " +
                        "SET caption=? " +
                        "WHERE id=?"
        );
        preparedStatement.setString(1,new_caption );
        preparedStatement.setString(2,id);
        return preparedStatement.executeUpdate();
    }

    public static int Update_location(String new_location,String id)throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE posts " +
                        "SET Location=? " +
                        "WHERE id=?"
        );
        preparedStatement.setString(1,new_location);
        preparedStatement.setString(2,id);
        return preparedStatement.executeUpdate();
    }
    public static ResultSet show_posts()throws  SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(
                "SELECT * FROM posts ORDER BY Timy DESC"
        );
    }



    public static int Update_commentNum_from_posts (String id,int new_commentNum)throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE posts " +
                        "SET commentsNum=? " +
                        "WHERE id=?"
        );
        preparedStatement.setInt(1,new_commentNum);
        preparedStatement.setString(2,id);
        return preparedStatement.executeUpdate();
    }
    public static ResultSet get_AllComments() throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(
                "SELECT * FROM comments ORDER BY timy ASC "
        );
    }
    public static int get_commentNum_from_posts (String id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT commentsNum FROM posts WHERE id=?"
        );
        preparedStatement.setString(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return  resultSet.getInt("commentsNum");
    }

    public static int Update_likesNum_into_posts (int new_likesNum,String id)throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE posts " +
                        "SET likesNum=? " +
                        "WHERE id=?"
        );
        preparedStatement.setInt(1,new_likesNum);
        preparedStatement.setString(2,id);
        return preparedStatement.executeUpdate();
    }
    public static int get_likesNum_from_posts (String ID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT likesNum FROM posts WHERE id=?"
        );
        preparedStatement.setString(1,ID);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            return resultSet.getInt("likesNum");
        }else{
            return -1;
        }
    }
    public  static ResultSet get_URl(String userID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT id, pictureid, ad, caption FROM posts WHERE posterid=?"
        );
        preparedStatement.setString(1,userID);
        return preparedStatement.executeQuery();
    }

    public  static ResultSet get_likeANDview_num (String postID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT likesNum, viewsNum FROM posts WHERE id=?"
        );
        preparedStatement.setString(1,postID);
        return preparedStatement.executeQuery();
    }
    // COMMENTS
    public static int add_comment(Comment comment) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO  comments " +
                        "VALUES(?, ?, ?, ?, 0, 0, ?)"
        );
        preparedStatement.setString(1,comment.getPost_OR_commentID());
        preparedStatement.setString(2, comment.getCommentID() );
        preparedStatement.setString(3,comment.getCommenterID());
        preparedStatement.setString(4, comment.getComment_caption() );
        preparedStatement.setString(5, comment.getTime());
        return preparedStatement.executeUpdate();
    }

    public static boolean delete_comment(String id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM comments WHERE id=?"
        );
        preparedStatement.setString(1, id);
        return preparedStatement.executeUpdate()>0;
    }

    public static int Update_commentNum_from_comments (String id,int new_commentNum)throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE comments " +
                        "SET commentsNum=? " +
                        "WHERE id=?"
        );
        preparedStatement.setInt(1,new_commentNum);
        preparedStatement.setString(2,id);
        return preparedStatement.executeUpdate();
    }

    public static int get_commentNum_from_comments (String id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT commentsNum FROM comments WHERE id=?"
        );
        preparedStatement.setString(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return  resultSet.getInt("commentsNum");
    }

    public static int Update_likesNum_into_comments (int new_likesNum,String id)throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE comments " +
                        "SET likesNum=? " +
                        "WHERE id=?"
        );
        preparedStatement.setInt(1,new_likesNum);
        preparedStatement.setString(2,id);
        return preparedStatement.executeUpdate();
    }

    public static int get_likesNum_from_comments (String id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT likesNum FROM comments WHERE id=?"
        );
        preparedStatement.setString(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return  resultSet.getInt("likesNum");
    }

    public static ResultSet SHOW_COMMENTS_comment (String post_ID)throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT commenterid, comment_caption FROM comments WHERE postORcommentID=?"
        );
        preparedStatement.setString(1,post_ID+"#");
        return preparedStatement.executeQuery();
    }

    public static ResultSet SHOW_COMMENTS_post (String post_ID)throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT commenterid, comment_caption FROM comments WHERE postORcommentID=?"
        );
        preparedStatement.setString(1,post_ID+"*");
        return preparedStatement.executeQuery();
    }
    public static int Update_comment(String new_text,String id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE comments " +
                        "SET comment_caption=? " +
                        "WHERE id=?"
        );
        preparedStatement.setString(1,new_text);
        preparedStatement.setString(2,id);
        return preparedStatement.executeUpdate();
    }










    // LIKES
    public static int add_likes(Like like) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO  Likes (post_OR_comment_ID, Like_Id, Liker_ID) " +
                        "VALUES(?, ?, ?)"
        );
        preparedStatement.setString(1,like.getPost_OR_comment_ID());
        preparedStatement.setString(2, like.getLike_ID());
        preparedStatement.setString(3, like.getLiker_id());
        return preparedStatement.executeUpdate();
    }

    public static boolean delete_like (String id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM Likes WHERE Like_ID=?"
        );
        preparedStatement.setString(1, id);
        return preparedStatement.executeUpdate()>0;
    }

    public static ResultSet SHOW_LIKES_post (String post_ID)throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM Likes WHERE post_OR_comment_ID=?"
        );
        preparedStatement.setString(1, post_ID+"*");
        return preparedStatement.executeQuery();
    }
    public static ResultSet SHOW_LIKES_comment (String post_ID)throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM Likes WHERE post_OR_comment_ID=?"
        );
        preparedStatement.setString(1, post_ID+"#");
        return preparedStatement.executeQuery();
    }

    public static ResultSet get_usernameANDpictureurl (String user_ID)throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT username, profilepicture FROM users WHERE id=?"
        );
        preparedStatement.setString(1, user_ID);
        return preparedStatement.executeQuery();
    }
}