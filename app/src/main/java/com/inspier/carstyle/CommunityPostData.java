package com.inspier.carstyle;

//Community 게시판 글 목록 Data 타입 세팅 class
public class CommunityPostData {

    private String Community_Post_Board_Name;
    private String Community_Post_Title;
    private String Community_Post_Contents;
    private String Community_Post_Photo;
    private String Community_Post_NickName;
    private String Community_Post_WriteTime;
    private String Community_Post_Views;
    private int Community_Post_Index_Num;
    private int Community_Post_Photo_Count;
    private int Community_Post_ViewLike;

    //GET-------------------------------------------------------------------

    public String getCommunity_Post_Board_Name() {
        return Community_Post_Board_Name;
    }

    public String getCommunity_Post_Title() {
        return Community_Post_Title;
    }

    public String getCommunity_Post_Contents() {
        return Community_Post_Contents;
    }

    public String getCommunity_Post_Photo() {
        return Community_Post_Photo;
    }

    public String getCommunity_Post_NickName() {
        return Community_Post_NickName;
    }

    public String getCommunity_Post_WriteTime() {
        return Community_Post_WriteTime;
    }

    public String getCommunity_Post_Views() {
        return Community_Post_Views;
    }

    public int getCommunity_Post_Index_Num() {
        return Community_Post_Index_Num;
    }

    public int getCommunity_Post_Photo_Count() {
        return Community_Post_Photo_Count;
    }

    public int getCommunity_Post_ViewLike() {
        return Community_Post_ViewLike;
    }

    //SET-------------------------------------------------------------------

    public void setCommunity_Post_Board_Name(String Community_Post_Board_Name) {
        this.Community_Post_Board_Name = Community_Post_Board_Name;
    }

    public void setCommunity_Post_Title(String Community_Post_Title) {
        this.Community_Post_Title = Community_Post_Title;
    }

    public void setCommunity_Post_Contents(String Community_Post_Contents) {
        this.Community_Post_Contents = Community_Post_Contents;
    }

    public void setCommunity_Post_Photo(String Community_Post_Photo) {
        this.Community_Post_Photo = Community_Post_Photo;
    }

    public void setCommunity_Post_NickName(String Community_Post_Nickname) {
        this.Community_Post_NickName = Community_Post_Nickname;
    }

    public void setCommunity_Post_WriteTime(String Community_Post_WriteTime) {
        this.Community_Post_WriteTime = Community_Post_WriteTime;
    }

    public void setCommunity_Post_Views(String Community_Post_Views) {
        this.Community_Post_Views = Community_Post_Views;
    }

    public void setCommunity_Post_Index_Num(int Community_Post_Index_Num) {
        this.Community_Post_Index_Num = Community_Post_Index_Num;
    }

    public void setCommunity_Post_Photo_Count(int Community_Post_Photo_Count) {
        this.Community_Post_Photo_Count = Community_Post_Photo_Count;
    }

    public void setCommunity_Post_ViewLike(int Community_Post_ViewLike) {
        this.Community_Post_ViewLike = Community_Post_ViewLike;
    }
}
