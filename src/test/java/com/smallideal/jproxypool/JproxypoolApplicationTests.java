package com.smallideal.jproxypool;

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

/*@RunWith(SpringRunner.class)
@SpringBootTest*/
public class JproxypoolApplicationTests {

    @Test
    public void contextLoads() {
        String result = null;
        String url = "http://www.google.com";
        HttpHost proxy = new HttpHost("109.201.96.171", 31773, "http"); //添加代理，IP为本地IP 8888就是fillder的端口
        CloseableHttpClient httpClient = HttpClients.createDefault();//添加代理
        try {
            // 创建httpGet
            HttpGet httpGet = new HttpGet(url);
            System.out.println("获取的url为:"+url);
            httpGet.setHeader("Connection", "keep-alive");
            //httpGet.addHeader(new BasicHeader("Cookie", cookies));

            //代理
            RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
            httpGet.setConfig(config);

            System.out.println("executing request:" + httpGet.getURI());
            // 执行get请求
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                int code = response.getStatusLine().getStatusCode();
                System.out.println("返回的状态码:" + code);
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                // 打印响应状态
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    System.out.println("Response content length" + entity.getContentLength());
                    // 打印响应内容
                    result = EntityUtils.toString(entity);
                    // 打印响应头
                    System.out.println("Response content" + entity.getContent());
                    System.out.println("Response Contentype" + entity.getContentType());
                    System.out.println("Response ContenEncoding" + entity.getContentEncoding());
                }
                System.out.println("--------------");
                Header[] hr = response.getAllHeaders();
                for (int i = 0; i < hr.length; i++) {
                    Header header1 = hr[i];
                    System.out.println("头部内容：" + header1);
                }

            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
