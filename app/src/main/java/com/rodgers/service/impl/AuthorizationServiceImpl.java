package com.rodgers.service.impl;

import com.rodgers.service.TgMessageProcessor;
import com.rodgers.tdlib.Client;
import com.rodgers.tdlib.TdApi;
import com.rodgers.tdlib.TdApi.Function;
import com.rodgers.tdlib.TdApi.Object;
import com.rodgers.service.AuthorizationService;
import com.rodgers.tgclient.CommandResultService;
import com.rodgers.tgclient.TgClientService;
import com.rodgers.tgclient.UpdateResultService;
import com.rodgers.tgclient.impl.AuthorizationResultServiceImpl;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Builder
public class AuthorizationServiceImpl implements AuthorizationService {
    private static Logger logger= LoggerFactory.getLogger(AuthorizationServiceImpl.class);
    private static TdApi.AuthorizationState authorizationState = null;

    Environment environment;

    TgClientService tgClientService;

    TgMessageProcessor tgMessageProcessor;
    private static volatile boolean haveAuthorization = false;
    private static final Lock authorizationLock = new ReentrantLock();
    private static final Condition gotAuthorization = authorizationLock.newCondition();
    public void updateAuthorizationState(TdApi.AuthorizationState authorizationState){
        if (authorizationState != null) {
            AuthorizationServiceImpl.authorizationState = authorizationState;
        }
    }
    public <T extends TdApi.AuthorizationState> CompletableFuture<T> getAuthorizationState(){
        return CompletableFuture.completedFuture((T)AuthorizationServiceImpl.authorizationState);
    }

    @Override
    public <T extends Object, U extends Function<TdApi.Ok>> CompletableFuture<T> sentAuthorizationQuery(U authorizationQuery) {
        return tgClientService.sent(authorizationQuery,
                AuthorizationResultServiceImpl.builder()
                        .command(authorizationQuery)
                        .tgMessageProcessor(tgMessageProcessor)
                .build());

    }

}
