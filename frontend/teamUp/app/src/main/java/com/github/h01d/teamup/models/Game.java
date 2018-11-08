package com.github.h01d.teamup.models;

public class Game
{
    private int gameID;
    private String userId;
    private String name;
    private int type;
    private int size;
    private boolean opponents;
    private String locationCity;
    private String locationAddress;
    private String locationCountry;
    private double locationLat;
    private double locationLong;
    private String gameDate;
    private String phone;
    private String comment;
    private String status;
    private String gameCreated;

    public Game(int gameID, String userId, String name, int type, int size, boolean opponents, String locationCity, String locationAddress, String locationCountry, double locationLat, double locationLong, String gameDate, String phone, String comment, String status, String gameCreated)
    {
        this.gameID = gameID;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.size = size;
        this.opponents = opponents;
        this.locationCity = locationCity;
        this.locationAddress = locationAddress;
        this.locationCountry = locationCountry;
        this.locationLat = locationLat;
        this.locationLong = locationLong;
        this.gameDate = gameDate;
        this.phone = phone;
        this.comment = comment;
        this.status = status;
        this.gameCreated = gameCreated;
    }

    public int getGameID()
    {
        return gameID;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getName()
    {
        return name;
    }

    public int getType()
    {
        return type;
    }

    public int getSize()
    {
        return size;
    }

    public boolean isOpponents()
    {
        return opponents;
    }

    public String getLocationCity()
    {
        return locationCity;
    }

    public String getLocationAddress()
    {
        return locationAddress;
    }

    public String getLocationCountry()
    {
        return locationCountry;
    }

    public double getLocationLat()
    {
        return locationLat;
    }

    public double getLocationLong()
    {
        return locationLong;
    }

    public String getGameDate()
    {
        return gameDate;
    }

    public String getPhone()
    {
        return phone;
    }

    public String getComment()
    {
        return comment;
    }

    public String getStatus()
    {
        return status;
    }

    public String getGameCreated()
    {
        return gameCreated;
    }
}
