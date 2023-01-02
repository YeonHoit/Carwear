package com.inspier.carstyle;

//Comments Data 타입 세팅 class
public class CommentsData {

    private int Community_Comments_Index_Num;
    private String Community_Comments_NickName;
    private String Community_Comments_DateTime;
    private String Community_Comments_Comment;

    //GET-------------------------------------------------------------------

    public int getCommunity_Comments_Index_Num() {
        return Community_Comments_Index_Num;
    }

    public String getCommunity_Comments_NickName() {
        return Community_Comments_NickName;
    }

    public String getCommunity_Comments_DateTime() {
        return Community_Comments_DateTime;
    }

    public String getCommunity_Comments_Comment() {
        return Community_Comments_Comment;
    }

    //SET-------------------------------------------------------------------

    public void setCommunity_Comments_Index_Num(int Community_Comments_Index_Num) {
        this.Community_Comments_Index_Num = Community_Comments_Index_Num;
    }

    public void setCommunity_Comments_NickName(String Community_Comments_NickName) {
        this.Community_Comments_NickName = Community_Comments_NickName;
    }

    public void setCommunity_Comments_DateTime(String Community_Comments_DateTime) {
        this.Community_Comments_DateTime = Community_Comments_DateTime;
    }

    public void setCommunity_Comments_Comment(String Community_Comments_Comment) {
        this.Community_Comments_Comment = Community_Comments_Comment;
    }
}
