package com.mqiu.myserver.api.clients;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Created by bingzhang on 8/20/17.
 */



public class TestHttpClient {

    @Test
    public void test() throws IOException{
        HttpClient _httpClient = HttpClientBuilder.create().build();

        // get
        String getUrl = "http://localhost:7077/myserver-backend/abc/asdfg";
        HttpGet httpGet = new HttpGet(getUrl);
        HttpResponse responseGet = _httpClient.execute(httpGet);
        httpGet.releaseConnection();
        String result = EntityUtils.toString(responseGet.getEntity(), "UTF-8");
        System.out.println(result);

        // post + get
        String postUrl = "http://localhost:7077/myserver-backend/abc";
        HttpPost httpPost = new HttpPost(postUrl);
        String jsonPost = "{\"shortUrl\":\"abcd\", \"longUrl\":\"dddd\"}";
        StringEntity entityPost = new StringEntity(jsonPost);
        httpPost.setEntity(entityPost);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        HttpResponse responsePost = _httpClient.execute(httpPost);
        httpPost.releaseConnection();

        HttpGet httpGetAfterPost = new HttpGet(getUrl);
        HttpResponse responseGetAfterPost = _httpClient.execute(httpGetAfterPost);
        httpGet.releaseConnection();
        String resultAfterPost = EntityUtils.toString(responseGetAfterPost.getEntity(), "UTF-8");
        System.out.println(resultAfterPost);

        // delete + get
        String putUrl = "http://localhost:7077/myserver-backend/abc/asdfghjkl";
        HttpPost httpPut = new HttpPost(putUrl);
        String jsonPut = "{\"shortUrl\":\"abcd\", \"longUrl\":\"asdfghjkl\"}";
        StringEntity entityPut = new StringEntity(jsonPut);
        httpPost.setEntity(entityPut);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        HttpResponse responsePut = _httpClient.execute(httpPut);
        httpPost.releaseConnection();
        HttpGet httpGetAfterDelete = new HttpGet(getUrl);
        HttpResponse responseGetAfterDelete = _httpClient.execute(httpGetAfterDelete);
        httpGet.releaseConnection();
        String resultAfterDelete = EntityUtils.toString(responseGetAfterDelete.getEntity(), "UTF-8");
        System.out.println(resultAfterDelete);

        // get

        HttpGet httpGet2 = new HttpGet(getUrl);
        HttpResponse responseGet2 = _httpClient.execute(httpGet2);
        httpGet.releaseConnection();
        String result2 = EntityUtils.toString(responseGet2.getEntity(), "UTF-8");
        System.out.println(result2);

    }
}
