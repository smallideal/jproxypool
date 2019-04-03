package com.smallideal.jproxypool.job;

import com.smallideal.jproxypool.IpUtils;
import com.smallideal.jproxypool.crawler.CrawlerStarter;
import com.smallideal.jproxypool.db.ProxyIp;
import com.smallideal.jproxypool.db.ProxyIpService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

public class IpTestJob implements Job {

    private ProxyIpService proxyIpService;

    private static final Logger logger = LoggerFactory.getLogger(IpTestJob.class);

    private int availableProcessorsSize;

    public IpTestJob() {
        proxyIpService = CrawlerStarter.applicationContext.getBean(ProxyIpService.class);
        availableProcessorsSize = Runtime.getRuntime().availableProcessors();
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            if (logger.isInfoEnabled()) {
                logger.info("Start to test proxy ip connection");
            }
            int dataSizePerJob = 10000;
            List<ProxyIp> proxyIpList = proxyIpService.findIp(dataSizePerJob);
            if (CollectionUtils.isEmpty(proxyIpList)) {
                return;
            }
            dataSizePerJob = proxyIpList.size() > dataSizePerJob ? dataSizePerJob : proxyIpList.size();
            //记录数小于CPU核数
            if (dataSizePerJob <= availableProcessorsSize) {
                new IpTestThread(proxyIpList,proxyIpService).start();
            }
            else {
                int period = BigDecimal.valueOf(dataSizePerJob).divide(BigDecimal.valueOf(availableProcessorsSize), BigDecimal.ROUND_UP, 2).intValue();
                for (int i = 0; i < availableProcessorsSize;i++) {

                    final int start = i * period;
                    final int end = ((i + 1)*period - 1) > dataSizePerJob ? dataSizePerJob : ((i + 1)*period - 1);
                    logger.info(Thread.currentThread().getName() + " process data range[" + start + "," + end + "]");
                    new IpTestThread(proxyIpList.subList(start, end),proxyIpService).start();
                }

            }


        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e.getMessage(), e);
            }
        }
    }

    private static class IpTestThread extends Thread {
        private List<ProxyIp> proxyIpList;
        private ProxyIpService proxyIpService;

        public IpTestThread(List<ProxyIp> proxyIpList,ProxyIpService proxyIpService) {
            this.proxyIpList = proxyIpList;
            this.proxyIpService = proxyIpService;
        }

        @Override
        public void run() {
            for (ProxyIp proxyIp : proxyIpList) {
                int protocol = IpUtils.testProtocol(proxyIp);
                if (protocol >= 0) {
                    proxyIp.setProtocol(protocol);
                    proxyIpService.updateValid(proxyIp);
                } else {
                    proxyIpService.updateInValid(proxyIp);
                }
            }
        }
    }

}
