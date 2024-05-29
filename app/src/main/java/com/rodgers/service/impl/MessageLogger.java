package com.rodgers.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.DefaultBaseTypeLimitingValidator;
import com.rodgers.tdlib.TdApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageLogger {
    static ObjectMapper objectMapper=new ObjectMapper();
    static{
        objectMapper.activateDefaultTypingAsProperty(new DefaultBaseTypeLimitingValidator(),ObjectMapper.DefaultTyping.NON_FINAL,"class");
    }
    final Logger logger= LoggerFactory.getLogger(this.getClass());
    public void info(TdApi.Object object){
        try {
            logger.info(objectMapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());

        }
    }
}
