package com.smallideal.jproxypool.crawler;

import com.smallideal.jproxypool.crawler.handler.AbstractPageHandler;
import us.codecraft.webmagic.Spider;

public class CrawlerWorker extends Thread {

    private CrawlerItem crawlerItem;

    private AbstractPageHandler pageHandler;

    public CrawlerWorker(CrawlerItem crawlerItem, AbstractPageHandler pageHandler) {
        this.crawlerItem = crawlerItem;
        this.pageHandler = pageHandler;
    }

    @Override
    public void run() {
        Spider.create(pageHandler).addUrl(crawlerItem.getLinks().toArray(new String[]{})).thread(5).run();
    }


}
