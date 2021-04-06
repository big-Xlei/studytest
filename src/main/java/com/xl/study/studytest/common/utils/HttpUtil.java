package com.xl.study.studytest.common.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public class HttpUtil {

    public Future<String> getResult(String url, Map<String, String> paramsMap, Map<String, String> headsMap) {
        String result = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = null;
        try {
            URIBuilder builder = new URIBuilder(url);
            if (paramsMap!=null && paramsMap.size()!=0){
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                builder.setParameters(pairs);
            }
            HttpGet get = new HttpGet(builder.build());

            if (headsMap !=null && headsMap.size()!=0){
                List<BasicHeader> headpairs = new ArrayList<>();
                for (Map.Entry<String, String> entry : headsMap.entrySet()) {
                    headpairs.add(new BasicHeader(entry.getKey(), entry.getValue()));
                }
                BasicHeader[] BasicHeaders = new BasicHeader[headpairs.size()];
                get.setHeaders(headpairs.toArray(BasicHeaders));
            }


            response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
            return new AsyncResult<String>(result);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new AsyncResult<String>(result);
    }

    public String postResult(String url, Map<String, String> paramsMap, Map<String,String> body,Map<String,String> headsMap) {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = createIgnoreSSLHttpClient();
        String result = "";
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            URIBuilder builder = new URIBuilder(url);

            //设置请求参数
            if (paramsMap!=null && paramsMap.size()!=0){
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                builder.setParameters(pairs);
            }

            HttpPost httpPost = new HttpPost(builder.build());

            //设置请求头
            if (headsMap !=null && headsMap.size()!=0){
                List<BasicHeader> headpairs = new ArrayList<>();
                for (Map.Entry<String, String> entry : headsMap.entrySet()) {
                    headpairs.add(new BasicHeader(entry.getKey(), entry.getValue()));
                }
                BasicHeader[] BasicHeaders = new BasicHeader[headpairs.size()];
                httpPost.setHeaders(headpairs.toArray(BasicHeaders));
            }


            // 配置信息
            RequestConfig requestConfig = RequestConfig.custom()
                    // 设置连接超时时间(单位毫秒)
                    .setConnectTimeout(100000)
                    // 设置请求超时时间(单位毫秒)
                    .setConnectionRequestTimeout(100000)
                    // socket读写超时时间(单位毫秒)
                    .setSocketTimeout(100000)
                    // 设置是否允许重定向(默认为true)
                    .setRedirectsEnabled(true).build();
            httpPost.setConfig(requestConfig);

            //设置请求体
            if (body!=null && body.size()!=0){
                String bodys = JSON.toJSONString(body);
                StringEntity stringEntity = new StringEntity(bodys, "utf-8");
                httpPost.setEntity(stringEntity);
            }

            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null) {
                result = EntityUtils.toString(responseEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    //跳过ssl底层协议用
    public static CloseableHttpClient createIgnoreSSLHttpClient() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain,
                                         String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
            return HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
