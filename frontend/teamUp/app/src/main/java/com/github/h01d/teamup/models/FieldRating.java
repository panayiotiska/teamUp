package com.github.h01d.teamup.models;

public class FieldRating
{
    private int ratingId;
    private String createdById;
    private String createdByFirstName;
    private String createdByLastName;
    private String createdAt;
    private String comment;
    private float rating;

    public FieldRating(int ratingId, String createdById, String createdByFirstName, String createdByLastName, String createdAt, String comment, float rating)
    {
        this.ratingId = ratingId;
        this.createdById = createdById;
        this.createdByFirstName = createdByFirstName;
        this.createdByLastName = createdByLastName;
        this.createdAt = createdAt;
        this.comment = comment;
        this.rating = rating;
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

    public float getRating()
    {
        return rating;
    }
}
