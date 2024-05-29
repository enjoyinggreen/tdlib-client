package com.rodgers.tgclient;

import com.rodgers.tdlib.TdApi;

import java.util.concurrent.CompletableFuture;

public interface TgClientService {
    public <T extends TdApi.UpdateAuthorizationState> CompletableFuture<T> start();
    public <T extends TdApi.UpdateAuthorizationState,F extends TdApi.Object> CompletableFuture<T> sentAuthorizationState(TdApi.Function<F> authorization);
    public <T extends TdApi.UpdateAuthorizationState> CompletableFuture<T> close();
    public <T extends TdApi.Object,F extends TdApi.Object>  CompletableFuture<T> sent( TdApi.Function<F> query );

}
