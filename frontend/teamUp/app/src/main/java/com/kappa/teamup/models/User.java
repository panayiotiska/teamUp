package com.kappa.teamup.models;

public class User
{
    private String userUID, userName, deviceToken;

    public User(String userUID, String userName, String deviceToken)
    {
        this.userUID = userUID;
        this.userName = userName;
        this.deviceToken = deviceToken;
    }

    public String getUserUID()
    {
        return userUID;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getDeviceToken()
    {
        return deviceToken;
    }
}
