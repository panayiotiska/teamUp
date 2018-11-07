package com.github.h01d.teamup_v2.models;

public class User
{
    private String userID;
    private String firstName;
    private String lastName;
    private String createdAt;

    public User(String userID, String firstName, String lastName, String createdAt)
    {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = createdAt;
    }

    public String getUserID()
    {
        return userID;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getCreatedAt()
    {
        return createdAt;
    }
}