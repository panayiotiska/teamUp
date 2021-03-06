package com.github.h01d.teamup.models;

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

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null) return false;

        return this.getUserID().equals(((User) obj).getUserID());
    }
}