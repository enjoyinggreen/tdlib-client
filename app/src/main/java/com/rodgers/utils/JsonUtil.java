package com.rodgers.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.DefaultBaseTypeLimitingValidator;
import com.rodgers.tdlib.TdApi;

public class JsonUtil {
    static Logger logger=LoggerFactory.getLogger(JsonUtil.class);
    static ObjectMapper objectMapper=new ObjectMapper();
    static{
        objectMapper.activateDefaultTypingAsProperty(new DefaultBaseTypeLimitingValidator(),ObjectMapper.DefaultTyping.NON_FINAL,"class");
    }
    public static String toString(TdApi.Object object){
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("JsonUtil::toString:", e);
            return null;
        }
    }
}
