package com.srinidhi.lgb;

public class Attendance {
    private String login_address, logout_address, login_cont,logout_cont,attendance_status,login_subloc,logout_subloc,login_loc,logout_loc,login_time,logout_time;
    private Double login_lat,logout_lat,login_lon,logout_lon;
    private Long login_pincode,logout_pincode;

    public Attendance() {
    }

    public String getLoginTime() {
        return login_time;
    }

    public void setLoginTime(String login_time) {
        this.login_time = login_time;
    }

    public String getLogoutTime() {
        return logout_time;
    }

    public void setLogoutTime(String logout_time) {
        this.logout_time = logout_time;
    }
    public String getLogin_address() {
        return login_address;
    }

    public void setLogin_address(String login_address) {
        this.login_address = login_address;
    }

    public String getLogout_address() {
        return logout_address;
    }

    public void setLogout_address(String logout_address) {
        this.logout_address = logout_address;
    }

    public String getLogin_cont() {
        return login_cont;
    }

    public void setLogin_cont(String login_cont) {
        this.login_cont = login_cont;
    }

    public String getLogout_cont() {
        return logout_cont;
    }

    public void setLogout_cont(String logout_cont) {
        this.logout_cont = logout_cont;
    }

    public String getAttendance_status() {
        return attendance_status;
    }

    public void setAttendance_status(String attendance_status) {
        this.attendance_status = attendance_status;
    }

    public String getLogin_subloc() {
        return login_subloc;
    }

    public void setLogin_subloc(String login_subloc) {
        this.login_subloc = login_subloc;
    }

    public String getLogin_loc() {
        return login_loc;
    }

    public void setLogin_loc(String login_loc) {
        this.login_loc = login_loc;
    }

    public String getLogout_subloc() {
        return logout_subloc;
    }

    public void setLogout_subloc(String logout_subloc) {
        this.logout_subloc = logout_subloc;
    }

    public String getLogout_loc() {
        return logout_loc;
    }

    public void setLogout_loc(String logout_loc) {
        this.logout_loc = logout_loc;
    }

    public double getLogin_lat() {
        return login_lat;
    }

    public void setLogin_lat(double login_lat) {
        this.login_lat = login_lat;
    }

    public double getLogout_lat() {
        return logout_lat;
    }

    public void setLogout_lat(double logout_lat) {
        this.logout_lat = logout_lat;
    }

    public double getLogin_lon() {
        return login_lon;
    }

    public void setLogin_lon(double login_lon) {
        this.login_lon = login_lon;
    }

    public double getLogout_lon() {
        return logout_lon;
    }

    public void setLogout_lon(double logout_lon) {
        this.logout_lon = logout_lon;
    }

    public long getLogoutPincode() {
        return logout_pincode;
    }

    public void setLogoutPincode(long logout_pincode) {
        this.logout_pincode = logout_pincode;
    }

    public long getLoginPincode() {
        return login_pincode;
    }

    public void setLoginPincode(long login_pincode) {
        this.login_pincode = login_pincode;
    }
}

