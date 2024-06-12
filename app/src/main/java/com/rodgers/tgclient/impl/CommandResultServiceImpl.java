package com.rodgers.tgclient.impl;

import com.rodgers.tdlib.TdApi;
import com.rodgers.tgclient.CommandResultService;
import com.rodgers.utils.TgConstants;
import com.rodgers.service.TgMessageProcessor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SuperBuilder
public class CommandResultServiceImpl <T extends  TdApi.Object>implements CommandResultService {
    protected final CompletableFuture<T> completableFuture=new CompletableFuture<>();
    protected TgMessageProcessor tgMessageProcessor;
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
        tgMessageProcessor.processTgCommandObject("got result form command:",this.command,object);
    }
}
