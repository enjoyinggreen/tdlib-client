package com.rodgers.service.impl;

import com.rodgers.tdlib.TdApi;
import com.rodgers.tgclient.CommandResultService;
import com.rodgers.utils.TgConstants;
import com.rodgers.service.TgMessageLogger;

import java.util.concurrent.CompletableFuture;

public class CommandHandlerImpl <T extends  TdApi.Object>implements CommandResultService {
    protected CompletableFuture<T> completableFuture=new CompletableFuture<>();
    protected TgMessageLogger tgMessageLogger;
    protected TdApi.Function command;
    @Override
    public void onResult(TdApi.Object object) {
        try {
            processResult(object);
            completableFuture.complete((T)object);
        }catch (Exception e){
            TdApi.Error error=new TdApi.Error(TgConstants.SERVER_ERROR, e.getMessage());
            completableFuture.complete((T)error);
        }

    }
    @Override
    public <T extends TdApi.Object> CompletableFuture<T> getCompletableFuture() {
        return (CompletableFuture<T>) this.completableFuture;
    }
    protected void processResult(TdApi.Object object){
        tgMessageLogger.printCommandMessage("got result form command:",this.command,object);
    }
}
