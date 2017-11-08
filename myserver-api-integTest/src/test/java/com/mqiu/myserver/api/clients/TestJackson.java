package com.mqiu.myserver.api.clients;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.testng.Assert;
import org.testng.annotations.Test;


import java.io.IOException;

/**
 * Created by bingzhang on 8/20/17.
 */
public class TestJackson {
    private static final ObjectMapper _MAPPER = new ObjectMapper()
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    static class Xyz {
        int a;
        int b;
        int sum;
        int product;
    }


    @Test
    public void testObjectToJson() throws JsonProcessingException {
        Xyz xyz = new Xyz();
        xyz.a = 3;
        xyz.b = 5;
        xyz.sum = 20;
        xyz.product = 9;
        String jsonString = _MAPPER.writeValueAsString(xyz);
        System.out.println(jsonString);
    }

    @Test
    public void testJsonToObject() throws IOException{
        String jsonString = "{\"a\":3, \"b\":5, \"sum\":20, \"product\": 9}";

        Xyz xyz = _MAPPER.readValue(jsonString, Xyz.class);
        Assert.assertEquals(xyz.b, 5);
        Assert.assertEquals(xyz.sum, 20);
        Assert.assertEquals(xyz.product, 9);
    }

}
