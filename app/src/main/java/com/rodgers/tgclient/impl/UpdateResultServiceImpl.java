package com.rodgers.tgclient.impl;

import com.rodgers.service.AuthorizationService;
import com.rodgers.tdlib.TdApi;
import com.rodgers.tgclient.UpdateResultService;
import com.rodgers.service.TgMessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateResultServiceImpl implements UpdateResultService {
    @Autowired
    TgMessageProcessor tgMessageProcessor;
    @Autowired
    AuthorizationService authorizationService;
    @Override
    public void onResult(TdApi.Object object) {
        switch (object.getConstructor()) {
            case TdApi.UpdateAuthorizationState.CONSTRUCTOR:
                authorizationService.updateAuthorizationState(((TdApi.UpdateAuthorizationState) object).authorizationState);
                break;
            default:
                tgMessageProcessor.processTgObject(object);
                // print("Unsupported update:" + newLine + object);
        }
    }
    //distribute updating message base on message type
}
