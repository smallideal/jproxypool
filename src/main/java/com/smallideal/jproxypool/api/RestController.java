package com.smallideal.jproxypool.api;

import com.smallideal.jproxypool.db.ProxyIp;
import com.smallideal.jproxypool.db.ProxyIpService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

@Controller
@RequestMapping("/api")
public class RestController {

    @Resource
    private ProxyIpService proxyIpService;

    public static void main(String[] args) {
        System.out.println(DateFormatUtils.format(new Date(1553567641238L),"yyyy-MM-dd HH:mm:ss"));
    }

    @RequestMapping("/get/{size}")
    @ResponseBody
    public Object findValidIp(@PathVariable int size) {
        List<ProxyIp> proxyIpList = proxyIpService.findValidIp(size);
        List<Map<String, Object>> proxyList = new ArrayList<>();
        for (ProxyIp proxyIp : proxyIpList) {
            Map<String, Object> map = new HashMap<>();
            map.put("ip", proxyIp.getIp());
            map.put("port", proxyIp.getPort());
            map.put("address", proxyIp.getAddress());
            map.put("lastCheckTime",DateFormatUtils.format(new Date(proxyIp.getCheckTime()),"yyyy-MM-dd HH:mm:ss"));
            map.put("type",proxyIp.getProtocol()==1?"http":(proxyIp.getProtocol()==2)?"https":"none");
            map.put("ttl", proxyIp.getTtl() > 60 ? (proxyIp.getTtl() / 60) + "分钟" : proxyIp.getTtl() + "秒钟");
            proxyList.add(map);
        }
        return proxyList;
    }
}
