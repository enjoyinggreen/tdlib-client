package com.rodgers.service.impl;

import com.rodgers.service.AuthorizationService;
import com.rodgers.service.TgMessageProcessor;
import com.rodgers.service.UpdateService;
import com.rodgers.tgclient.UpdateResultService;
import com.rodgers.tgclient.impl.UpdateResultServiceImpl;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;


@Builder
public class UpdateServiceImpl implements UpdateService {
    Environment environment;
    TgMessageProcessor tgMessageProcessor;
    AuthorizationService authorizationService;
    @Builder.Default
    UpdateResultService updateResultService=null;
    @Override
    public UpdateResultService getUpdateResultService() {
        if(updateResultService==null)
            updateResultService=UpdateResultServiceImpl.builder()
                    .tgMessageProcessor(tgMessageProcessor)
                    .authorizationService(authorizationService)
                    .build();
        return updateResultService;
    }
}
