package com.mqiu.myserver.api.rest.resources;

import com.linkedin.parseq.Task;
import com.linkedin.restli.server.resources.CollectionResourceTaskTemplate;
import com.mqiu.myserver.api.rest.services.AbcService;

import com.linkedin.restli.server.CreateResponse;
import com.linkedin.restli.server.annotations.RestLiCollection;
import nam.e.spa.ce.Abc;

import javax.inject.Inject;
import javax.inject.Named;

@RestLiCollection(name = "abc", namespace = "nam.e.spa.ce")
public class AbcResource extends CollectionResourceTaskTemplate<String, Abc> {

    // private AbcService _abcService = R2ClientServiceFactory.createInstance();
    @Inject @Named("abcService")
    private AbcService _abcService;

    @Override
    public Task<Abc> get(String longUrl){
        return _abcService.get(longUrl);
    }

    public Task<CreateResponse> create(Abc abc){
        return _abcService.create(abc);
    }
/*
    @Override
    public UpdateResponse delete(String longUrl) {
        return _abcService.delete(longUrl);
    }

    @Override
    public UpdateResponse update(String longUrl, Abc abc) {
        return _abcService.update(longUrl, abc);
    }
    */
}
