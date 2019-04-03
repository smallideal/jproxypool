package com.smallideal.jproxypool.crawler;

import com.smallideal.jproxypool.job.IpTestJob;
import com.smallideal.jproxypool.job.CrawlerJob;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * proxy ip crawler
 */
@Component
public class CrawlerStarter implements  ApplicationContextAware {

    public static ApplicationContext applicationContext;

    @Resource
    private Scheduler scheduler;

    private static final Logger logger =LoggerFactory.getLogger(CrawlerStarter.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        CrawlerStarter.applicationContext = applicationContext;
        startCrawlProxyIp();
        startConnectionTest();
    }

    private void startCrawlProxyIp() {
        //配置定时任务对应的Job，这里执行的是ScheduledJob类中定时的方法
        String jobName = "crawl-test";
        String groupName = "default";
        String cron = "*/59 * * * * ?";
        JobDetail jobDetail = JobBuilder
                .newJob(CrawlerJob.class)
                .usingJobData("jobName", jobName)
                .withIdentity(jobName, groupName)
                .build();

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger-" + jobName, groupName)
                .withSchedule(scheduleBuilder)
                .build();

        try {
            scheduler.scheduleJob(jobDetail, cronTrigger);
        } catch (SchedulerException e) {
            logger.error("任务启动失败>{}", e);
        }

    }


    private void startConnectionTest() {
        //配置定时任务对应的Job，这里执行的是ScheduledJob类中定时的方法
        String jobName = "connection-test";
        String groupName = "default";
        String cron = "*/5 * * * * ?";
        JobDetail jobDetail = JobBuilder
                .newJob(IpTestJob.class)
                .usingJobData("jobName", jobName)
                .withIdentity(jobName, groupName)
                .build();

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger-" + jobName, groupName)
                .withSchedule(scheduleBuilder)
                .build();

        try {
            scheduler.scheduleJob(jobDetail, cronTrigger);
        } catch (SchedulerException e) {
            logger.error("任务启动失败>{}", e);
        }

    }

}
