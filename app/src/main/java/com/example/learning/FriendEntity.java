package com.example.learning;

public class FriendEntity {
    private int friendId;
    private FriendStatus friendStatus;
    private String friendName;
    private String friendPicture;
    private String date;

    public FriendEntity(int friendId, String friendStatusString, String friendName, String friendPicture, String date) {
        this.friendId = friendId;
        friendStatusString = friendStatusString.toUpperCase();
        this.friendStatus = FriendStatus.valueOf(friendStatusString);
        this.friendName = friendName;
        this.friendPicture = friendPicture;
        this.date = date;
    }

    public FriendEntity(int friendId, FriendStatus friendStatus, String friendName) {
        this.friendId = friendId;
        this.friendStatus = friendStatus;
        this.friendName = friendName;
    }

    public int getFriendId() {
        return this.friendId;
    }

    public FriendStatus getFriendStatus() {
        return this.friendStatus;
    }

    public String getFriendName() {
        return this.friendName;
    }

    public String getFriendPicture() { return this.friendPicture; }

    public String getDate() { return this.date; }
}
