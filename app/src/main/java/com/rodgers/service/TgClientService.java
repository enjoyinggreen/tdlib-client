package com.rodgers.tgclient;

import com.rodgers.tdlib.Client;
import com.rodgers.tdlib.TdApi;

import java.util.concurrent.CompletableFuture;

public interface TgClientService {
    public void startClient();
    public <T extends TdApi.Object> CompletableFuture<T>  closeClient();
    public <T extends TdApi.Object, F extends TdApi.Object> CompletableFuture<T> sent(TdApi.Function<F> query );
    public <T extends TdApi.Object, F extends TdApi.Object> CompletableFuture<T> sent(TdApi.Function<F> query,CommandResultService commandResultService);
}
