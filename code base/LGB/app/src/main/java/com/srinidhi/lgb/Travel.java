package com.srinidhi.lgb;

public class Travel {
    private String start_address, end_address, start_cont,end_cont,journey_status,start_subloc,end_subloc,start_loc,end_loc;
    private Double start_lat,end_lat,start_lon,end_lon;
    private Long start_pincode,end_pincode;

    public Travel() {
    }

    public String getStart_address() {
        return start_address;
    }

    public void setStart_address(String start_address) {
        this.start_address = start_address;
    }

    public String getEnd_address() {
        return end_address;
    }

    public void setEnd_address(String end_address) {
        this.end_address = end_address;
    }

    public String getStart_cont() {
        return start_cont;
    }

    public void setStart_cont(String start_cont) {
        this.start_cont = start_cont;
    }

    public String getEnd_cont() {
        return end_cont;
    }

    public void setEnd_cont(String end_cont) {
        this.end_cont = end_cont;
    }

    public String getJourney_status() {
        return journey_status;
    }

    public void setJourney_status(String journey_status) {
        this.journey_status = journey_status;
    }

    public String getStart_subloc() {
        return start_subloc;
    }

    public void setStart_subloc(String start_subloc) {
        this.start_subloc = start_subloc;
    }

    public String getStart_loc() {
        return start_loc;
    }

    public void setStart_loc(String start_loc) {
        this.start_loc = start_loc;
    }

    public String getEnd_subloc() {
        return end_subloc;
    }

    public void setEnd_subloc(String end_subloc) {
        this.end_subloc = end_subloc;
    }

    public String getEnd_loc() {
        return end_loc;
    }

    public void setEnd_loc(String end_loc) {
        this.end_loc = end_loc;
    }

    public double getStart_lat() {
        return start_lat;
    }

    public void setStart_lat(double start_lat) {
        this.start_lat = start_lat;
    }

    public double getEnd_lat() {
        return end_lat;
    }

    public void setEnd_lat(double end_lat) {
        this.end_lat = end_lat;
    }

    public double getStart_lon() {
        return start_lon;
    }

    public void setStart_lon(double start_lon) {
        this.start_lon = start_lon;
    }

    public double getEnd_lon() {
        return end_lon;
    }

    public void setEnd_lon(double end_lon) {
        this.end_lon = end_lon;
    }

    public long getEndPincode() {
        return end_pincode;
    }

    public void setEndPincode(long end_pincode) {
        this.end_pincode = end_pincode;
    }

    public long getStartPincode() {
        return start_pincode;
    }

    public void setStartPincode(long start_pincode) {
        this.start_pincode = start_pincode;
    }
}
