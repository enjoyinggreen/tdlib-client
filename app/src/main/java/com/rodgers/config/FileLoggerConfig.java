package com.rodgers.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class FileLoggerConfig {
    @Bean(name = "updateMessageLogger")
    Logger getUpdateMessageLogger(){
        return LoggerFactory.getLogger("UpdateMessageLogFile");
    }
    @Bean(name = "commandMessageLogger")
    Logger getCommandMessageLogger(){
        return LoggerFactory.getLogger("commandMessageLogFile");
    }
}
