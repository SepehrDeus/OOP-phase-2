package entity;

public class Post {
    private String postID;
    private String posterID; // userID
    private boolean ad; // advertisement or not
    private String picture; //image ID
    private String caption;
    private int likesNum;
    private int commentsNum;
    private int viewsNum;
    private final String time ; // final because it doesn't change
    private String location;
    private String field; //field of the post


    public Post(String postID,String posterID,boolean ad,String picture,String caption,String time,String field,String location){
        this.postID=postID;
        this.posterID=posterID;
        this.ad=ad;
        this.picture=picture;
        this.caption=caption;
        this.time=time;
        this.location=location;
        this.field=field;
        this.likesNum=this.commentsNum=viewsNum=0;
    }


    public String getPicture() {
        return picture;
    }

    public String getCaption() {
        return caption;
    }

    public int getLikesNum() {
        return likesNum;
    }

    public int getCommentsNum() {
        return commentsNum;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
            return location;
    }


    public int isAd() {
        if(this.ad){
            return 1;
        }else {
            return 0;
        }
    }

    public String getPosterID() {
        return posterID;
    }

    public String getPostID() {
        return postID;
    }
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
