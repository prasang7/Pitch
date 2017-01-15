package com.eventapp.pitch;

/**
 * Created by Sid on 1/15/17.
 */

public class template {
    String EventName,EventType,time,websiteURL,Address,Description,openClosed,targetAudience;
    String duration,organizersName,orgEmail,orgContact,sponsor,appCredits,date;

    public template(String eventName, String eventType, String time, String websiteURL, String address, String description, String openClosed, String targetAudience, String duration, String organizersName, String orgEmail, String orgContact, String sponsor, String appCredits,String date) {
        EventName = eventName;
        EventType = eventType;
        this.time = time;
        this.websiteURL = websiteURL;
        Address = address;
        Description = description;
        this.openClosed = openClosed;
        this.targetAudience = targetAudience;
        this.duration = duration;
        this.organizersName = organizersName;
        this.orgEmail = orgEmail;
        this.orgContact = orgContact;
        this.sponsor = sponsor;
        this.appCredits = appCredits;
        this.date=date;
    }
    public template(){};

    public String getEventName() {
        return EventName;
    }

    public String getEventType() {
        return EventType;
    }

    public String getTime() {
        return time;
    }

    public String getWebsiteURL() {
        return websiteURL;
    }

    public String getAddress() {
        return Address;
    }

    public String getDescription() {
        return Description;
    }

    public String getOpenClosed() {
        return openClosed;
    }

    public String getTargetAudience() {
        return targetAudience;
    }

    public String getDuration() {
        return duration;
    }

    public String getOrganizersName() {
        return organizersName;
    }

    public String getOrgEmail() {
        return orgEmail;
    }

    public String getOrgContact() {
        return orgContact;
    }

    public String getSponsor() {
        return sponsor;
    }

    public String getAppCredits() {
        return appCredits;
    }

    public String getDate(){return this.date;}

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public void setEventType(String eventType) {
        EventType = eventType;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setWebsiteURL(String websiteURL) {
        this.websiteURL = websiteURL;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setOpenClosed(String openClosed) {
        this.openClosed = openClosed;
    }

    public void setTargetAudience(String targetAudience) {
        this.targetAudience = targetAudience;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setOrganizersName(String organizersName) {
        this.organizersName = organizersName;
    }

    public void setOrgEmail(String orgEmail) {
        this.orgEmail = orgEmail;
    }

    public void setOrgContact(String orgContact) {
        this.orgContact = orgContact;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public void setAppCredits(String appCredits) {
        this.appCredits = appCredits;
    }

    public void setDate(String date){
        this.date=date;
    }
}
