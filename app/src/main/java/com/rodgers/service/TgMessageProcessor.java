package com.rodgers.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.DefaultBaseTypeLimitingValidator;
import com.rodgers.tdlib.TdApi;
import com.rodgers.utils.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class TgMessageProcessor {
    final static Logger logger= LoggerFactory.getLogger(TgMessageProcessor.class);
    @Autowired
    Environment environment;
    @Autowired
    @Qualifier("updateMessageLogger")
    Logger updateMessageLogger;
    @Autowired
    @Qualifier("commandMessageLogger")
    Logger commandMessageLogger;

    
    public void processTgObject(TdApi.Object object){
        printTgObject(object);
    }
    
    public void processTgCommandObject(String message,TdApi.Function command,TdApi.Object object){
        printTgCommandObject(message,command,object);

    }
    @Async
    private void printTgObject(TdApi.Object object){
        updateMessageLogger.info(JsonUtil.toString(object));
    }
    @Async
    private void printTgCommandObject(String message,TdApi.Function command,TdApi.Object object){
        commandMessageLogger.info(message,JsonUtil.toString(command),JsonUtil.toString(object));
    }
}
