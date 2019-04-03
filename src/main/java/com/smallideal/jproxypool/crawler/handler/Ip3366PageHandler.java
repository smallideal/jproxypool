package com.smallideal.jproxypool.crawler.handler;

import com.smallideal.jproxypool.IpUtils;
import com.smallideal.jproxypool.db.ProxyIp;
import com.sun.deploy.net.proxy.ProxyType;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

@Component("ip3366")
public class Ip3366PageHandler extends AbstractPageHandler {
    @Override
    public List<ProxyIp> resolveProxyIpList(Page page) {
        Selectable selectable = page.getHtml().$("#list").$("tr");
        List<Selectable> nodes = selectable.nodes();
        List<ProxyIp> proxyIpList = new ArrayList<>();
        for (Selectable tr : nodes) {
            String ip = tr.xpath("//td[1]/text()").get();
            if (IpUtils.isIpAddress(ip)) {
                ProxyIp proxyIp = new ProxyIp();
                proxyIp.setIp(ip);
                proxyIp.setCreateTime(System.currentTimeMillis());
                proxyIp.setPort(Integer.valueOf(tr.xpath("//td[2]/text()").get()));
                proxyIp.setAddress(tr.xpath("//td[5]/a/text()").get());
                String type = tr.xpath("//td[3]/text()").get();
                if(type.contains("高匿")){
                    proxyIp.setType(ProxyIp.Type.GN.getValue());
                }
                String protocol = tr.xpath("//td[4]/text()").get();
                if ("HTTP".equals(protocol)) {
                    proxyIp.setProtocol(1);
                }
                if ("HTTPS".equals(protocol)) {
                    proxyIp.setProtocol(2);
                }
                proxyIpList.add(proxyIp);
            }
        }
        return proxyIpList;
    }
}
