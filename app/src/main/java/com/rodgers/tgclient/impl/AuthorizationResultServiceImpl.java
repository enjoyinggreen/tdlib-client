package com.rodgers.tgclient.impl;

import com.rodgers.tdlib.TdApi;
import com.rodgers.tgclient.AuthorizationResultService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class AuthorizationResultServiceImpl extends CommandResultServiceImpl<TdApi.UpdateAuthorizationState> implements AuthorizationResultService{
}
