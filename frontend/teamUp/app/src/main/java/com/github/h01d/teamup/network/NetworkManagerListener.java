package com.github.h01d.teamup.network;

import org.json.JSONArray;

public interface NetworkManagerListener
{
    void getResult(JSONArray result);
    void getError(String error, int code);
}