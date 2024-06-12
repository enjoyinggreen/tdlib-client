package com.rodgers.service;


import com.rodgers.tdlib.TdApi;

import java.util.concurrent.CompletableFuture;

public interface AuthorizationService {
    public void updateAuthorizationState(TdApi.AuthorizationState authorizationState);
    public <T extends TdApi.AuthorizationState> CompletableFuture<T> getAuthorizationState();
    public <T extends TdApi.Object,U extends TdApi.Function<TdApi.Ok>> CompletableFuture<T> sentAuthorizationQuery(U authorizationQuery);
}
