package com.czdxwx.comptition.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class NullOrEmptyArrayDeserializer extends JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (p.isExpectedStartArrayToken()) {
            // 跳过数组内容
            p.skipChildren();
            return null;
        }
        return p.getText();
    }
}
