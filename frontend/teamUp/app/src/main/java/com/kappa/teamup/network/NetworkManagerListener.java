package com.kappa.teamup.network;

public interface NetworkManagerListener<String>
{
    void getResult(String result);
    void getError(String error);
}
