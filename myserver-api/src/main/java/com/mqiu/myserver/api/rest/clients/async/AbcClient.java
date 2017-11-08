package com.mqiu.myserver.api.rest.clients.async;

import com.linkedin.parseq.Task;
import com.linkedin.restli.client.CreateIdRequest;
import com.linkedin.restli.client.GetRequest;
import com.linkedin.restli.common.HttpStatus;
import com.linkedin.restli.server.CreateResponse;
import nam.e.spa.ce.Abc;
import nam.e.spa.ce.AbcRequestBuilders;

import com.linkedin.restli.client.ParSeqRestClient;

public class AbcClient {

  private ParSeqRestClient _parSeqRestClient;

  public AbcClient(ParSeqRestClient parSeqRestClient) {
    _parSeqRestClient = parSeqRestClient;
  }

  public Task<Abc> get(String id) {
    GetRequest<Abc> getRequest = new AbcRequestBuilders()
        .get()
        .id(id)
        .build();
    return _parSeqRestClient.createTask(getRequest)
        .map(abcResponse -> abcResponse.getEntity())
        .recover(e -> {
          throw new RuntimeException(e);
        });
  }

    public Task<CreateResponse> create(Abc abc) {
        CreateIdRequest<String, Abc> createIdRequest = new AbcRequestBuilders()
                .create()
                .input(abc)
                .build();
        return _parSeqRestClient.createTask(createIdRequest).map(resp ->
                new CreateResponse(HttpStatus.S_201_CREATED));
    }
}
