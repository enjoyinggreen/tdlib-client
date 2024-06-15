package com.rodgers.service.impl;

import com.rodgers.tgclient.LogMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogMessageServiceImpl implements LogMessageService {
    final static Logger logger= LoggerFactory.getLogger(LogMessageServiceImpl.class);

    @Override
    public void onLogMessage(int verbosityLevel, String message) {
        logger.info("LogMessageServiceImpl:onLogMessage:verbosityLevel->{},message->{}",verbosityLevel,message);
    }
}
