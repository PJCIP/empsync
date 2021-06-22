package com.srinidhi.lgb;

public class dataUser {
    private String id;

    private String cat;
    private String name;
    private String text;
    private long date,enddate,diff;

    public dataUser() {
    }

    public dataUser(String name,String cat, String text, long date,long enddate,long diff) {

        this.name = name;
        this.cat = cat;
        this.text = text;
        this.date = date;
        this.enddate = enddate;
        this.diff = diff;
    }

    public String getId() {
        return id;
    }

    public String getCat() {
        return cat;
    }

    public String getName() {
        return name;
    }

    public String getReason() {
        return text;
    }

    public long getStartDate() {
        return date;
    }
    public long getEndDate() {
        return enddate;
    }
    public long getDiff() {
        return diff;
    }

}
