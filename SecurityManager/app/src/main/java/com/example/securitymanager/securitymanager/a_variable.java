package com.example.securitymanager.securitymanager;

import android.app.Application;

public class a_variable extends Application {

    private int healthcheck=0;

    public int getHealthcheck() {
        return healthcheck;
    }

    public void setHealthcheck(Integer healthcheck) {
        this.healthcheck = healthcheck;
    }
}
