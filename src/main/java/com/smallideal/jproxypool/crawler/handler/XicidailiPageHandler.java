package com.smallideal.jproxypool.crawler.handler;

import com.smallideal.jproxypool.IpUtils;
import com.smallideal.jproxypool.crawler.handler.AbstractPageHandler;
import com.smallideal.jproxypool.db.ProxyIp;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

@Component("xicidaili")
public class XicidailiPageHandler extends AbstractPageHandler {
    @Override
    public List<ProxyIp> resolveProxyIpList(Page page) {
        Selectable selectable = page.getHtml().$("#ip_list").$("tr");
        List<Selectable> nodes = selectable.nodes();
        List<ProxyIp> proxyIpList = new ArrayList<>();
        for (Selectable tr : nodes) {
            String ip = tr.xpath("//td[2]/text()").get();
            if (IpUtils.isIpAddress(ip)) {
                ProxyIp proxyIp = new ProxyIp();
                proxyIp.setIp(ip);
                proxyIp.setCreateTime(System.currentTimeMillis());
                proxyIp.setPort(Integer.valueOf(tr.xpath("//td[3]/text()").get()));
                proxyIp.setAddress(tr.xpath("//td[4]/a/text()").get());
                proxyIp.setType(ProxyIp.Type.GN.getValue());
                proxyIpList.add(proxyIp);
            }
        }
        return proxyIpList;
    }
}
