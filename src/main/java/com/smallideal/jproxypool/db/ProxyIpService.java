package com.smallideal.jproxypool.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ProxyIpService {

    @Resource
    private MongoTemplate mongoTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ProxyIpService.class);

    public void saveProxyIp(List<ProxyIp> proxyIpList) {

        for (ProxyIp proxyIp : proxyIpList) {
            if (mongoTemplate.exists(Query.query(Criteria.where("ip").is(proxyIp.getIp())), ProxyIp.class)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("The proxy ip " + proxyIp.getIp() + " is exists");
                }
                continue;
            }
            try {
                mongoTemplate.save(proxyIp);
            } catch (Exception e) {
                if(logger.isDebugEnabled()){
                    logger.debug("Proxy ip insert error:"+e.getMessage(),e);
                }
            }
        }
    }


    public void updateValid(ProxyIp proxyIp) {
        if (logger.isDebugEnabled()) {
            logger.debug("The proxy ip " + proxyIp.getIp() + " connection testConnection valid");
        }
        mongoTemplate.updateMulti(Query.query(Criteria.where("ip").is(proxyIp.getIp())),
                Update.update("checkTime",System.currentTimeMillis()).set("protocol",proxyIp.getProtocol()),
                ProxyIp.class);
    }

    public void updateInValid(ProxyIp proxyIp) {
        if (logger.isDebugEnabled()) {
            logger.debug("The proxy ip " + proxyIp.getIp() + " connection testConnection invalid");
        }
        mongoTemplate.remove(proxyIp);
    }

    public List<ProxyIp> find() {
        return mongoTemplate.findAll(ProxyIp.class);
    }

    public List<ProxyIp> findValidIp(int limit) {
        Query query = new Query();
        query.addCriteria(Criteria.where("protocol").is(1));
        query.with(Sort.by(Sort.Order.desc("checkTime")));
        query.limit(limit);
        return mongoTemplate.find(query,ProxyIp.class);
    }

    public List<ProxyIp> findIp(int limit) {
        Query query = new Query();
        query.with(Sort.by(Sort.Order.asc("checkTime")));
        query.limit(limit);
        return mongoTemplate.find(query,ProxyIp.class);
    }
}
