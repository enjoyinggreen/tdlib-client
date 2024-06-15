package com.rodgers.tgclient.impl;

import com.rodgers.service.AuthorizationService;
import com.rodgers.tdlib.TdApi;
import com.rodgers.tgclient.UpdateResultService;
import com.rodgers.service.TgMessageProcessor;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Builder
public class UpdateResultServiceImpl <T extends  TdApi.Object> implements UpdateResultService {
    protected final CompletableFuture<T> completableFuture=new CompletableFuture<>();
    protected TgMessageProcessor tgMessageProcessor;
    protected AuthorizationService authorizationService;
    @Override
    public void onResult(TdApi.Object object) {
        switch (object.getConstructor()) {
            case TdApi.UpdateAuthorizationState.CONSTRUCTOR:
                authorizationService.updateAuthorizationState(((TdApi.UpdateAuthorizationState) object).authorizationState);
                break;
            default:
                tgMessageProcessor.processTgObject(object);
        }
    }
}
