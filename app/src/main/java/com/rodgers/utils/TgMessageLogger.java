package com.rodgers.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.DefaultBaseTypeLimitingValidator;
import com.rodgers.tdlib.TdApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class TgMessageLogger {
    final static ObjectMapper objectMapper=new ObjectMapper();
    static{
        objectMapper.activateDefaultTypingAsProperty(new DefaultBaseTypeLimitingValidator(),ObjectMapper.DefaultTyping.NON_FINAL,"class");
    }
    final static Logger logger= LoggerFactory.getLogger(TgMessageLogger.class);

    @Async
    public void printLog(TdApi.Object object){
        try {
            logger.info(objectMapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
    }
}
