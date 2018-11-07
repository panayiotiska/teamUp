package com.github.h01d.teamup_v2.network;

import org.json.JSONArray;

public interface NetworkManagerListener
{
    void getResult(JSONArray result);
    void getError(String error, int code);
}