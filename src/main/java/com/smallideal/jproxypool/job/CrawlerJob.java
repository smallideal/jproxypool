package com.smallideal.jproxypool.job;

import com.smallideal.jproxypool.crawler.CrawlerConfig;
import com.smallideal.jproxypool.crawler.CrawlerItem;
import com.smallideal.jproxypool.crawler.CrawlerStarter;
import com.smallideal.jproxypool.crawler.CrawlerWorker;
import com.smallideal.jproxypool.crawler.handler.AbstractPageHandler;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

public class CrawlerJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try{
            List<CrawlerItem> crawlerItemList = CrawlerConfig.load();
            for (CrawlerItem crawlerItem : crawlerItemList) {
                new CrawlerWorker(
                        crawlerItem,
                        (AbstractPageHandler) CrawlerStarter.applicationContext.getBean(crawlerItem.getHandler())
                ).start();
            }
        }catch (Exception e){
            throw new JobExecutionException(e.getMessage(),e);
        }
    }
}
