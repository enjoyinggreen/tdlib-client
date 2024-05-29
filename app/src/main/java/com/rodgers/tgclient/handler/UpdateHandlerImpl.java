package com.rodgers.tgclient.handler;

import com.rodgers.tdlib.Client;
import com.rodgers.tdlib.TdApi;

public class UpdateHandlerImpl extends MessageLogger implements Client.ResultHandler {
    Long queryId;
    public UpdateHandlerImpl(Long queryId){
        this.queryId=queryId;
    }
    @Override
    public void onResult(TdApi.Object object) {
        info(object);

    }
}
