package com.rodgers.service.impl;

import com.rodgers.service.AuthorizationService;
import com.rodgers.tdlib.TdApi;
import com.rodgers.tgclient.UpdateResultService;
import com.rodgers.service.TgMessageLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateHandlerImpl implements UpdateResultService {
    @Autowired
    TgMessageLogger tgMessageLogger;
    @Autowired
    AuthorizationService authorizationService;

    @Override
    public void onResult(TdApi.Object object) {
        switch (object.getConstructor()) {
            case TdApi.UpdateAuthorizationState.CONSTRUCTOR:
                authorizationService.updateAuthorizationState(((TdApi.UpdateAuthorizationState) object).authorizationState);
                break;
            default:
                tgMessageLogger.printUpdateMessage(object);
                // print("Unsupported update:" + newLine + object);
        }
    }
    //distribute updating message base on message type
}
