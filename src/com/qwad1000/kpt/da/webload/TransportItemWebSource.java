package com.qwad1000.kpt.da.webload;

import android.content.Context;
import com.qwad1000.kpt.R;
import com.qwad1000.kpt.model.TransportItem;
import com.qwad1000.kpt.model.TransportTypeEnum;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Сергій on 03.10.2014.
 */
public class TransportItemWebSource {
    private HtmlHelper htmlHelper;
    private Context context;

    public TransportItemWebSource(Context context) {
        this.context = context;
    }

    public List<TransportItem> getTransportItemsByType(TransportTypeEnum typeEnum, boolean isWeekend) throws IOException {
        String loadStr = context.getResources().getString(isWeekend ? R.string.weekend_day_url : R.string.working_day_url);
        URL loadURL = new URL(loadStr);
        htmlHelper = new HtmlHelper(loadURL);
        String filterStr = context.getResources().getString(R.string.schedule_url);
        String dayPart = context.getResources().getString(isWeekend ? R.string.weekend_day : R.string.working_day);
        List<TagNode> links = htmlHelper.getLinks(filterStr,
                typeEnum.getUrlPart(context), dayPart);

        List<TransportItem> loadedItems = new ArrayList<>();

        for (TagNode node : links) {
            String url = node.getAttributeByName("href");
            CharSequence ch = node.getElementListByName("strong", true).get(0).getText();
            try {
                TransportItem item = new TransportItem(0, ch.toString(), typeEnum, new URL(url), isWeekend);
                loadedItems.add(item);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return loadedItems;
    }
}
