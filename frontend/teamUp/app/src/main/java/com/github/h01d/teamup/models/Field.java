package com.github.h01d.teamup.models;

import java.io.Serializable;

public class Field implements Serializable
{
    private int fieldId;
    private String name;
    private int type;
    private String image;
    private boolean sponsored;
    private String phone;
    private String city;
    private String address;
    private String countryCode;
    private String postalCode;
    private double latitute;
    private double longtitude;
    private float averageRating;
    private int totalRatings;

    public Field(int fieldId, String name, int type, String image, boolean sponsored, String phone, String city, String address, String countryCode, String postalCode, double latitute, double longtitude, float averageRating, int totalRatings)
    {
        this.fieldId = fieldId;
        this.name = name;
        this.type = type;
        this.image = image;
        this.sponsored = sponsored;
        this.phone = phone;
        this.city = city;
        this.address = address;
        this.countryCode = countryCode;
        this.postalCode = postalCode;
        this.latitute = latitute;
        this.longtitude = longtitude;
        this.averageRating = averageRating;
        this.totalRatings = totalRatings;
    }

    public int getFieldId()
    {
        return fieldId;
    }

    public String getName()
    {
        return name;
    }

    public int getType()
    {
        return type;
    }

    public String getImage()
    {
        return image;
    }

    public boolean isSponsored()
    {
        return sponsored;
    }

    public String getPhone()
    {
        return phone;
    }

    public String getCity()
    {
        return city;
    }

    public String getAddress()
    {
        return address;
    }

    public String getCountryCode()
    {
        return countryCode;
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public double getLatitute()
    {
        return latitute;
    }

    public double getLongtitude()
    {
        return longtitude;
    }

    public float getAverageRating()
    {
        return averageRating;
    }

    public int getTotalRatings()
    {
        return totalRatings;
    }
}
