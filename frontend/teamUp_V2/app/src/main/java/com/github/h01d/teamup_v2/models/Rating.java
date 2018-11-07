package com.github.h01d.teamup_v2.models;

public class Rating
{
    private int ratingId;
    private String createdById;
    private String createdByFirstName;
    private String createdByLastName;
    private String createdAt;
    private String comment;
    private int onTime;
    private int skills;
    private int behavior;

    public Rating(int ratingId, String createdById, String createdByFirstName, String createdByLastName, String createdAt, String comment, int onTime, int skills, int behavior)
    {
        this.ratingId = ratingId;
        this.createdById = createdById;
        this.createdByFirstName = createdByFirstName;
        this.createdByLastName = createdByLastName;
        this.createdAt = createdAt;
        this.comment = comment;
        this.onTime = onTime;
        this.skills = skills;
        this.behavior = behavior;
    }

    public int getRatingId()
    {
        return ratingId;
    }

    public String getCreatedById()
    {
        return createdById;
    }

    public String getCreatedByFirstName()
    {
        return createdByFirstName;
    }

    public String getCreatedByLastName()
    {
        return createdByLastName;
    }

    public String getCreatedAt()
    {
        return createdAt;
    }

    public String getComment()
    {
        return comment;
    }

    public int getOnTime()
    {
        return onTime;
    }

    public int getSkills()
    {
        return skills;
    }

    public int getBehavior()
    {
        return behavior;
    }
}
