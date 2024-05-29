package com.rodgers.tgclient;

import com.rodgers.tdlib.Client;
import com.rodgers.tdlib.TdApi;

import java.util.concurrent.CompletableFuture;

public interface CommandResultService extends Client.ResultHandler{
    public <T extends TdApi.Object> CompletableFuture<T> getCompletableFuture();
}
