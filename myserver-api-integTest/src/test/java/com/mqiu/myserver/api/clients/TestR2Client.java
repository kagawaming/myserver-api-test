package com.mqiu.myserver.api.clients;

/**
 * Created by bingzhang on 8/20/17.
 */
import com.linkedin.r2.transport.common.bridge.client.TransportClientAdapter;
import com.linkedin.r2.transport.http.client.HttpClientFactory;
import com.linkedin.restli.client.*;
import nam.e.spa.ce.Abc;
import nam.e.spa.ce.AbcRequestBuilders;
import org.testng.annotations.Test;

import java.util.Collections;

public class TestR2Client {

    @Test
    public void testR2Client() throws Exception{
        HttpClientFactory http = new HttpClientFactory();
        TransportClientAdapter r2Client = new TransportClientAdapter(http.getClient(Collections.<String, String>emptyMap()));
        RestClient _restClient = new RestClient(r2Client, "http://localhost:7077/myserver-backend/");

        final String longUrl = "asdfghjkl";
        // get
        GetRequest<Abc> request = new AbcRequestBuilders()
                .get()
                .id(longUrl)
                .build();
        Abc abc = _restClient.sendRequest(request).getResponse().getEntity();

        // create
        CreateIdRequest<String, Abc> requestCreate = new AbcRequestBuilders()
                .create()
                .input(new Abc().setLongUrl(longUrl).setShortUrl("abcd"))
                .build();
        _restClient.sendRequest(requestCreate).getResponse();

        // update
        UpdateRequest<Abc> requestUpdate = new AbcRequestBuilders()
                .update()
                .id(longUrl)
                .input(new Abc().setLongUrl(longUrl).setShortUrl("aaaaaa"))
                .build();
        _restClient.sendRequest(requestUpdate).getResponse();

        // delete
        DeleteRequest<Abc> requestDelete = new AbcRequestBuilders().delete()
                .id(longUrl)
                .build();
        _restClient.sendRequest(requestDelete).getResponse();
    }

}
