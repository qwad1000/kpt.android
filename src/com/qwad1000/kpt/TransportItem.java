package com.qwad1000.kpt;

import java.net.URL;

/**
 * Created by Сергій on 14.09.2014.
 */
public class TransportItem {
    private int id;
    private String number;
    private TransportTypeEnum type;
    private URL url;
    private boolean isWeekend;

    public TransportItem(int id, String number, TransportTypeEnum type, URL url, boolean isWeekend) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.url = url;
        this.isWeekend = isWeekend;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number.trim();
    }//warning : test it

    public TransportTypeEnum getType() {
        return type;
    }

    public void setType(TransportTypeEnum type) {
        this.type = type;
    }

    public boolean isWeekend() {
        return isWeekend;
    }

    public void setWeekend(boolean isWeekend) {
        this.isWeekend = isWeekend;
    }
}
