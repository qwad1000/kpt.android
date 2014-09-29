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

    public TransportItem(int id, String number, TransportTypeEnum type, URL url) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.url = url;
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
        this.number = number;
    }

    public TransportTypeEnum getType() {
        return type;
    }

    public void setType(TransportTypeEnum type) {
        this.type = type;
    }
}
