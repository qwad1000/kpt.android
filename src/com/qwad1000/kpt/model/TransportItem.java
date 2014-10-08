package com.qwad1000.kpt.model;

import java.net.URL;

/**
 * Created by Сергій on 14.09.2014.
 */
public class TransportItem {
    private long id;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransportItem that = (TransportItem) o;

        if (id != that.id) return false;
        if (isWeekend != that.isWeekend) return false;
        if (!number.equals(that.number)) return false;
        if (type != that.type) return false;
        if (!url.equals(that.url)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        long result = id;
        result = 31 * result + number.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + url.hashCode();
        result = 31 * result + (isWeekend ? 1 : 0);
        return (int) result;
    }
}
