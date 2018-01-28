package com.nextdev.googlemapsexperiment;

/**
 * Created by joshu on 1/27/2018.
 */

public class Event {
    //Variable Declaration
    public String name;
    public String date;
    public String time;
    public String location;

    //Variable Initialization
    public Event(String name, String date, String time, String location){
        this.name=name;
        this.date=date;
        this.time=time;
        this.location=location;
    }

    // Returns the event name
    public String getName() {
        return name;
    }

    // Returns the event date
    public String getDate() {
        return date;
    }

    // Returns the event time
    public String getTime() {
        return time;
    }

    // Returns the event location
    public String getLocation() {
        return location;
    }

    // Places all the info in one string so it is easy to output
    public String getInfo() {
        return name + "\n" + date + "\n" + time + "\n" + location;
    }

    public void print() {
        System.out.println(getInfo());
    }
}
