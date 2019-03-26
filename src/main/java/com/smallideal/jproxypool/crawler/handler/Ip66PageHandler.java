package com.smallideal.jproxypool.crawler.handler;

import com.smallideal.jproxypool.IpUtils;
import com.smallideal.jproxypool.db.ProxyIp;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

@Component("66ip")
public class Ip66PageHandler extends AbstractPageHandler {
    @Override
    public List<ProxyIp> resolveProxyIpList(Page page) {
        Selectable selectable = page.getHtml().$(".containerbox table").$("tr");
        List<Selectable> nodes = selectable.nodes();
        List<ProxyIp> proxyIpList = new ArrayList<>();
        for (Selectable tr : nodes) {
            String ip = tr.xpath("//td[1]/text()").get();
            if (IpUtils.isIpAddress(ip)) {
                ProxyIp proxyIp = new ProxyIp();
                proxyIp.setIp(ip);
                proxyIp.setCreateTime(System.currentTimeMillis());
                proxyIp.setPort(Integer.valueOf(tr.xpath("//td[2]/text()").get()));
                proxyIp.setAddress(tr.xpath("//td[3]/text()").get());
                String ipType = tr.xpath("//td[4]/text()").get();
                if ("高匿代理".equals(ipType)) {
                    proxyIp.setType(ProxyIp.Type.GN.getValue());
                }
                proxyIpList.add(proxyIp);
            }
        }
        return proxyIpList;
    }
}
