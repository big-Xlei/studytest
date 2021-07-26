package com.xl.study.studytest.domin.po;

public class TokenToUser {
    private Integer userId;

    private String userName;

    private String password;

    public TokenToUser() {
    }

    public Integer getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
