package com.qwad1000.kpt.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.qwad1000.kpt.R;

import java.util.Arrays;

/**
 * Created by Сергій on 08.09.2014.
 */
public enum TransportTypeEnum {
    Bus, Trolley, Tram;

    public Drawable getDrawable(Context context) {
        int id = 0;
        switch (this) {
            case Bus:
                id = R.drawable.bus48;
                break;
            case Trolley:
                id = R.drawable.trolleybus48;
                break;
            case Tram:
                id = R.drawable.tram48;
                break;
        }
        return context.getResources().getDrawable(id);
    }

    public String getName(Context context) {
        int id = 0;
        switch (this) {
            case Bus:
                id = R.string.bus;
                break;
            case Trolley:
                id = R.string.trolleybus;
                break;
            case Tram:
                id = R.string.tram;
                break;
        }
        return context.getResources().getString(id);
    }

    public String getUrlPart(Context context) {
        int id = 0;
        switch (this) {
            case Bus:
                id = R.string.bus_url;
                break;
            case Trolley:
                id = R.string.trolley_url;
                break;
            case Tram:
                id = R.string.tram_url;
                break;
        }
        return context.getResources().getString(id);
    }

    public int toInt() {
        return Arrays.binarySearch(values(), this);
    }
}


