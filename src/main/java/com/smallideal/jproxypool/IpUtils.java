package com.smallideal.jproxypool;


import com.smallideal.jproxypool.db.ProxyIp;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.regex.Pattern;

public class IpUtils {

    //测试协议
    public static int testProtocol(ProxyIp proxyIp) {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(proxyIp.getIp(), proxyIp.getPort());
        Socket socket = new Socket();
        try {
            socket.connect(inetSocketAddress, 5000);
            socket.close();
            if (isHttpProxy(proxyIp)) {
                return 1;
            }
            if (isHttpsProxy(proxyIp)) {
                return 2;
            }
            return 0;

        } catch (IOException e) {
            return -1;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
               // e.printStackTrace();
            }
        }

    }

    private static boolean isHttpsProxy(ProxyIp proxyIp) {
        HttpsURLConnection httpsConn = null;
        try {
            URL urlClient = new URL("https://www.baidu.com");
            SSLContext sc = SSLContext.getInstance("SSL");
            // 指定信任https
            sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
            //创建代理虽然是https也是Type.HTTP
            Proxy proxy1 = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp.getIp(), proxyIp.getPort()));
            //设置代理
            httpsConn = (HttpsURLConnection) urlClient.openConnection(proxy1);

            httpsConn.setSSLSocketFactory(sc.getSocketFactory());
            httpsConn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            // 设置通用的请求属性
            httpsConn.setRequestProperty("accept", "*/*");
            httpsConn.setRequestProperty("connection", "Keep-Alive");
            httpsConn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 断开连接
            httpsConn.disconnect();
            return httpsConn.getResponseCode() > 0;

        } catch (Exception e) {
           // e.printStackTrace();
        }
        return false;
    }

    private static boolean isHttpProxy(ProxyIp proxyIp) {
        HttpURLConnection httpConn = null;
        try {
            URL urlClient = new URL("http://www.baidu.com");
            //创建代理
            Proxy proxy1 = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp.getIp(), proxyIp.getPort()));
            //设置代理
            httpConn = (HttpURLConnection) urlClient.openConnection(proxy1);
            // 设置通用的请求属性
            httpConn.setRequestProperty("accept", "*/*");
            httpConn.setRequestProperty("connection", "Keep-Alive");
            httpConn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 断开连接
            httpConn.disconnect();
            //System.out.println("http response code:"+httpConn.getResponseCode());
            return httpConn.getResponseCode() > 0;
        } catch (Exception e) {
            //e.printStackTrace();
        }

        return false;
    }


    private static class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }


    public static boolean isIpAddress(String ip) {
        String ipPattern = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(ipPattern);
        return pattern.matcher(ip).matches();
    }
}
