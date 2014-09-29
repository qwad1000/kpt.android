package com.qwad1000.kpt;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Сергій on 08.09.2014.
 */
public class HtmlHelper {
    TagNode rootNode;

    public HtmlHelper(URL htmlPage) throws IOException //http://kpt.kiev.ua/ua/schedule/rozklad/rozklad_rob.htm
    {
        HtmlCleaner cleaner = new HtmlCleaner();

        rootNode = cleaner.clean(htmlPage, "windows-1251"); //windows-1251
    }

    List<TagNode> getLinks(String rootUrl, String transType, String dayType) {
        List<TagNode> tagNodeList = new ArrayList<TagNode>();
        String str = rootUrl + transType + "/" + dayType + "/";
        //"http://kpt.kiev.ua/ua/schedule/rozklad/"+transType+"/"+dayType+"/";//"tram_url/"+"rob/";


        TagNode linkElements[] = rootNode.getElementsByName("a", true);
        for (int i = 0; linkElements != null && i < linkElements.length; i++) {
            String classType = linkElements[i].getAttributeByName("href");
            if (classType != null && classType.contains(str)) {
                tagNodeList.add(linkElements[i]);
            }
        }
        return tagNodeList;
    }
}
