package com.rodgers.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.DefaultBaseTypeLimitingValidator;
import com.rodgers.tdlib.TdApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class TgMessageLogger {
    final static ObjectMapper objectMapper=new ObjectMapper();
    static{
        objectMapper.activateDefaultTypingAsProperty(new DefaultBaseTypeLimitingValidator(),ObjectMapper.DefaultTyping.NON_FINAL,"class");
    }
    final static Logger logger= LoggerFactory.getLogger(TgMessageLogger.class);
    @Autowired
    Environment environment;
    @Autowired
    @Qualifier("updateMessageLogger")
    Logger updateMessageLogger;
    @Autowired
    @Qualifier("commandMessageLogger")
    Logger commandMessageLogger;
    @Async
    public void printUpdateMessage(TdApi.Object object){
        try {
            updateMessageLogger.info(objectMapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            updateMessageLogger.error(e.getMessage());
        }
    }
    @Async
    public void printCommandMessage(String message,TdApi.Function command,TdApi.Object object){
        try {
            commandMessageLogger.info(message,objectMapper.writeValueAsString(command),objectMapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            commandMessageLogger.error(e.getMessage());
        }
    }
}
