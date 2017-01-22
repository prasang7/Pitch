package com.eventapp.pitch;


import com.eventapp.pitch.template;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by Sid on 1/15/17.
 */

public class project {
    public String name;
    public int templateId;
    public String dateCreated;
    public String informationJson;

    public project(String name, int templateId,String dateCreated) {
        this.templateId = templateId;
        this.name=name;
        this.dateCreated=dateCreated;
    }
    public void saveData(template temp){
        this.informationJson= (new Gson()).toJson(temp);
    }
    public template getInformation(){
        return (new Gson()).fromJson(this.informationJson,new TypeToken<template>(){}.getType());
    }
}
