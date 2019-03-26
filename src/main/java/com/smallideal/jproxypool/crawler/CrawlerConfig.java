package com.smallideal.jproxypool.crawler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlerConfig {

    public static List<CrawlerItem> load() throws IOException {
        InputStream inputStream = CrawlerConfig.class.getResourceAsStream("/crawler.json");
        JSONArray crawlerItemJsonArray = JSONArray.parseArray(IOUtils.toString(inputStream));
        List<CrawlerItem> crawlerItemList = new ArrayList<>();
        for (Object jsonObject : crawlerItemJsonArray) {
            JSONObject crawlerItemJson = (JSONObject) jsonObject;
            CrawlerItem crawlerItem = new CrawlerItem();
            crawlerItem.setCategory(crawlerItemJson.getString("category"));
            crawlerItem.setHandler(crawlerItemJson.getString("handler"));
            String url = crawlerItemJson.getString("links");
            Set<String> links = new HashSet<>();
            //先解析分页类url
            Pattern pattern = Pattern.compile("(\\[(\\d+)-(\\d+)])");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                String patternVar = matcher.group(1);
                int start = Integer.valueOf(matcher.group(2));
                int end = Integer.valueOf(matcher.group(3));
                for (int i = start; i <= end; i++) {
                    links.add(url.replace(patternVar, String.valueOf(i)));
                }
            }
            crawlerItem.setLinks(links);
            crawlerItemList.add(crawlerItem);
        }
        return crawlerItemList;
    }
}
