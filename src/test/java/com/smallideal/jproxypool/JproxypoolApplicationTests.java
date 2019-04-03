package com.smallideal.jproxypool;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/*@RunWith(SpringRunner.class)
@SpringBootTest*/
public class JproxypoolApplicationTests {

    @Test
    public void contextLoads() throws Exception{
        URL url = new URL("http://localhost:8080/api/get/100");
        URLConnection urlConnection = url.openConnection();
        String proxyIp = IOUtils.toString(urlConnection.getInputStream());
        JSONArray jsonArray = JSONArray.parseArray(proxyIp);
        for(Object object:jsonArray){
            JSONObject j=(JSONObject) object;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    request(j.getString("ip"),j.getInteger("port"));
                }
            }).start();
        }
        System.in.read();

    }

    private void request(String ip,int port){
        String result = null;
        String url = "http://www.google.com";
        HttpHost proxy = new HttpHost(ip, port, "http"); //添加代理，IP为本地IP 8888就是fillder的端口
        CloseableHttpClient httpClient = HttpClients.createDefault();//添加代理
        try {
            // 创建httpGet
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Connection", "keep-alive");
            //httpGet.addHeader(new BasicHeader("Cookie", cookies));

            //代理
            RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
            httpGet.setConfig(config);
            // 执行get请求
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                int code = response.getStatusLine().getStatusCode();
                System.out.println("代理IP"+ip+"返回的状态码:" + code);

            } finally {
                response.close();
            }
        } catch (Exception e) {
            System.out.println("代理IP"+ip+"不可用");
        } finally {
            // 关闭连接，释放资源
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
