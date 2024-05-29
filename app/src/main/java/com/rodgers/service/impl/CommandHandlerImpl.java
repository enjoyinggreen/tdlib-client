package com.rodgers.service.impl;

import com.rodgers.tdlib.TdApi;
import com.rodgers.tgclient.CommandResultService;

import java.util.concurrent.CompletableFuture;

public class CommandHandlerImpl <T extends  TdApi.Object>implements CommandResultService {
    CompletableFuture<T> completableFuture=new CompletableFuture<>();
    @Override
    public void onResult(TdApi.Object object) {
        completableFuture.complete((T)object);
    }
    @Override
    public <T extends TdApi.Object> CompletableFuture<T> getCompletableFuture() {
        return (CompletableFuture<T>) this.completableFuture;
    }
}
