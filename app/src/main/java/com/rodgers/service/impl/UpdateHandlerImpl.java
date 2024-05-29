package com.rodgers.service.impl;

import com.rodgers.tdlib.TdApi;
import com.rodgers.tgclient.UpdateResultService;
import com.rodgers.utils.TgMessageLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateHandlerImpl implements UpdateResultService {
    @Autowired
    TgMessageLogger tgMessageLogger;

    @Override
    public void onResult(TdApi.Object object) {
        tgMessageLogger.printLog(object);
    }
    //distribute updating message base on message type
}
