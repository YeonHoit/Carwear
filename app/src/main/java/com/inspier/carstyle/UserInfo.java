package com.inspier.carstyle;

import java.io.Serializable;

//접속한 사용자의 정보를 저장하는 class
public class UserInfo implements Serializable {

    //사용자의 데이터 종류
    private int Index_ID;
    private long ID;
    private String Login;
    private String Nickname;
    private String UserCar;
    private String Photo;
    private String Email;
    private String Phone;
    private boolean NoticeAlert;
    private boolean CommentAlert;
    private boolean LikeAlert;
    private boolean TagAlert;

    //GET-------------------------------------------------------------------

    public int getIndex_ID() {
        return Index_ID;
    }

    public long getID() {
        return ID;
    }

    public String getLogin() {
        return Login;
    }

    public String getNickname() {
        return Nickname;
    }

    public String getUserCar() {
        return UserCar;
    }

    public String getPhoto() {
        return Photo;
    }

    public String getEmail() {
        return Email;
    }

    public String getPhone() {
        return Phone;
    }

    public boolean isNoticeAlert() {
        return NoticeAlert;
    }

    public boolean isCommentAlert() {
        return CommentAlert;
    }

    public boolean isLikeAlert() {
        return LikeAlert;
    }

    public boolean isTagAlert() {
        return TagAlert;
    }

    //SET-------------------------------------------------------------------

    public void setIndex_ID(int Index_ID) {
        this.Index_ID = Index_ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public void setLogin(String Login) {
        this.Login = Login;
    }

    public void setNickname(String Nickname) {
        this.Nickname = Nickname;
    }

    public void setUserCar(String UserCar) {
        this.UserCar = UserCar;
    }

    public void setPhoto(String Photo) {
        this.Photo = Photo;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public void setNoticeAlert(boolean NoticeAlert) {
        this.NoticeAlert = NoticeAlert;
    }

    public void setCommentAlert(boolean CommentAlert) {
        this.CommentAlert = CommentAlert;
    }

    public void setLikeAlert(boolean LikeAlert) {
        this.LikeAlert = LikeAlert;
    }

    public void setTagAlert(boolean TagAlert) {
        this.TagAlert = TagAlert;
    }
}
