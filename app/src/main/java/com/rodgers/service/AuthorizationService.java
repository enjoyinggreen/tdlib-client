package com.rodgers.service;


import com.rodgers.tdlib.TdApi;

import java.util.concurrent.CompletableFuture;

public interface AuthorizationService {
    public void updateAuthorizationState(TdApi.AuthorizationState authorizationState);
    public <T extends TdApi.Object> CompletableFuture<T> handleAuthorizationState();
}
