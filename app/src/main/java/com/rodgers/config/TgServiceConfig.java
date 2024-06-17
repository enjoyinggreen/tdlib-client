package com.rodgers.config;

import com.rodgers.service.AuthorizationService;
import com.rodgers.service.TgMessageProcessor;
import com.rodgers.service.UpdateService;
import com.rodgers.service.impl.AuthorizationServiceImpl;
import com.rodgers.service.impl.TgClientServiceImpl;
import com.rodgers.service.impl.UpdateServiceImpl;
import com.rodgers.tgclient.ExceptionResultService;
import com.rodgers.tgclient.LogMessageService;
import com.rodgers.tgclient.TgClientService;
import com.rodgers.tgclient.impl.UpdateResultServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;

@Configuration
public class TgServiceConfig {
    @Autowired
    Environment environment;
    @Autowired
    TgMessageProcessor tgMessageProcessor;
    @Autowired
    LogMessageService logMessageService;
    @Autowired
    ExceptionResultService exceptionResultService;

    @Bean("tgClientService")
    @Autowired
    TgClientService getTgClientService(Environment environment,TgMessageProcessor tgMessageProcessor,LogMessageService logMessageService,ExceptionResultService exceptionResultService){
        return TgClientServiceImpl.builder()
                .environment(environment)
                .tgMessageProcessor(tgMessageProcessor)
                .logMessageService(logMessageService)
                .exceptionResultService(exceptionResultService)
                .build();
    }
    @Bean("authorizationService")
    @Autowired
    AuthorizationService getAuthorizationService( Environment environment,@Qualifier("tgClientService") TgClientService tgClientService,TgMessageProcessor tgMessageProcessor){
        return AuthorizationServiceImpl.builder()
                .environment(environment)
                .tgClientService(tgClientService)
                .tgMessageProcessor(tgMessageProcessor).build();
    }
    @Bean("updateService")
    @DependsOn({"tgClientService", "authorizationService"})
    @Autowired
    UpdateService getUpdateService(Environment environment,@Qualifier("tgClientService") TgClientService tgClientService,TgMessageProcessor tgMessageProcessor,AuthorizationService authorizationService){
        UpdateService  updateService=UpdateServiceImpl.builder()
                .environment(environment)
                .authorizationService(authorizationService)
                .tgMessageProcessor(tgMessageProcessor)
                .updateResultService(UpdateResultServiceImpl.builder()
                        .tgMessageProcessor(tgMessageProcessor)
                        .authorizationService(authorizationService)
                        .build()).build();
        ((TgClientServiceImpl)tgClientService).setUpdateService(updateService);
        return updateService;
    }
}
