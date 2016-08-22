package com.expenses.expenses.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by User on 2/1/2016.
 */
public class BeanConverter {
    ObjectMapper mapper = new ObjectMapper();
    private static BeanConverter instance;

    public BeanConverter() {

    }

    public static BeanConverter get() {
        if (instance == null) {
            instance = new BeanConverter();
        }
        return instance;
    }

    public  String toString(Object o) throws JsonProcessingException {
        return mapper.writeValueAsString(o);
    }

    public <T> T toObject(String value,Class<T> clazz) throws IOException {

        return (T)mapper.readValue(value,clazz);
    }

}
