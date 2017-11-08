package com.mqiu.myserver.api.rest.services;

import com.linkedin.parseq.Task;
import com.linkedin.restli.server.RestLiServiceException;
import com.mqiu.myserver.api.rest.clients.async.AbcClient;
import com.linkedin.restli.common.HttpStatus;
import com.linkedin.restli.server.CreateResponse;
import nam.e.spa.ce.Abc;


public class AbcService {
    private AbcClient _abcClient;

    public AbcService(AbcClient abcClient){
        _abcClient = abcClient;
    }

    public Task<Abc> get(String longUrl) {
        try {
            return _abcClient.get(longUrl);
        } catch (Exception e) {
            throw new RestLiServiceException(HttpStatus.S_500_INTERNAL_SERVER_ERROR);
        }
    }

    public Task<CreateResponse> create(Abc abc) {
        return _abcClient.create(abc);
    }
/*
    public UpdateResponse delete(String longUrl) {
        try {
            _abcClient.delete(longUrl);
            return new UpdateResponse(HttpStatus.S_200_OK);
        } catch (Exception e) {
            return new UpdateResponse(HttpStatus.S_500_INTERNAL_SERVER_ERROR);
        }
    }

    public UpdateResponse update(String longUrl, Abc abc) {
        try {
            _abcClient.update(longUrl, abc);
            return new UpdateResponse(HttpStatus.S_200_OK);
        } catch (Exception e) {
            return new UpdateResponse(HttpStatus.S_500_INTERNAL_SERVER_ERROR);
        }
    }
    */
}
