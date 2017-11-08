package com.mqiu.myserver.api.rest.clients.sync;

import com.linkedin.restli.client.CreateIdRequest;
import com.linkedin.restli.client.DeleteRequest;
import com.linkedin.restli.client.GetRequest;
import com.linkedin.restli.client.RestClient;
import com.linkedin.restli.client.UpdateRequest;
import nam.e.spa.ce.Abc;
import nam.e.spa.ce.AbcRequestBuilders;


public class AbcClient {
    RestClient _restClient;

    /**
     * below is r2client creation, which is already defined in beans.xml
     *
     * HttpClientFactory httpClientFactory = new HttpClientFactory();
     * TransportClientAdapter r2Client = new TransportClientAdapter(
     *     httpClientFactory.getClient(Collections.<String, String>emptyMap()));
     * RestClient _restClient = new RestClient(r2Client, uri);
     *
     * below is d2client creation
     *
     * D2Client d2Client = new D2ClientBuilder()
     *     .setZkHosts("localhost:21 81")
     *     .setZkSessionTimeout(5000, TimeUnit.MILLISECONDS)
     *     .setZkStartupTimeout(5000, TimeUnit.MILLISECONDS)
     *     .setLbWaitTimeout(5000, TimeUnit.MILLISECONDS)
     *     .setFlagFile("/tmp/suppressZkFlag")
     *     .setBasePath("/d2")
     *     .setFsBasePath("/tmp/backup")
     *     .build();
     * _restClient = new RestClient(d2Client, "d2://");
     */
    public AbcClient(RestClient restClient) {
        _restClient = restClient;
    }

    public Abc get(String longUrl) throws Exception {
        GetRequest<Abc> request = new AbcRequestBuilders().get().id(longUrl).build();
        return _restClient.sendRequest(request).getResponse().getEntity();
    }

    public void create(Abc abc) throws Exception {
        CreateIdRequest<String, Abc> requestCreate = new AbcRequestBuilders().create()
            .input(new Abc().setLongUrl(abc.getLongUrl()).setShortUrl(abc.getShortUrl()))
            .build();
        _restClient.sendRequest(requestCreate).getResponse();
    }

    public void delete(String longUrl) throws Exception {
        DeleteRequest<Abc> requestDelete = new AbcRequestBuilders().delete().id(longUrl).build();
        _restClient.sendRequest(requestDelete).getResponse();
    }

    public void update(String longUrl, Abc abc) throws Exception {
        UpdateRequest<Abc> requestUpdate = new AbcRequestBuilders().update()
            .id(longUrl)
            .input(new Abc().setLongUrl(longUrl).setShortUrl(abc.getShortUrl()))
            .build();
        _restClient.sendRequest(requestUpdate).getResponse();
    }
}