package com.smallideal.jproxypool.crawler.handler;

import com.smallideal.jproxypool.db.ProxyIp;
import com.smallideal.jproxypool.db.ProxyIpService;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.Resource;
import java.util.List;

public abstract class AbstractPageHandler implements PageProcessor {

    public abstract List<ProxyIp> resolveProxyIpList(Page page);

    @Resource
    private ProxyIpService proxyIpTableService;

    @Override
    public void process(Page page) {
        List<ProxyIp> proxyIpList = resolveProxyIpList(page);
        proxyIpTableService.saveProxyIp(proxyIpList);
    }

    @Override
    public Site getSite() {
        return Site.me().setRetryTimes(3).setSleepTime(1000);
    }
}
