package com.web.content.crawler.utils;

import java.util.List;
import java.util.stream.Collectors;

public class LinkUtil 
{
    public static boolean isValidLink(String link)
    {
        return link != null && link.length() > 1 && isLinkCanBeCrawl(link);
    }

    public static String removeGetParameters(String link) 
    {
        return link.contains("?") ? link.split("\\?")[0] : link;
    }

    public  static boolean isLinkCanBeCrawl(String link) 
    {
        return !(link.contains("https") || link.contains("http") || link.contains(".net") | link.contains(".com"));
    }
    
    public static List<String> getValidLinkList(List<String> webLinks)
    {
        return webLinks.stream()
                .filter(it -> isValidLink(it))
                .collect(Collectors.toList());
    }
}
