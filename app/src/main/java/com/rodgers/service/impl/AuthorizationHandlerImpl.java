package com.rodgers.service.impl;

import com.rodgers.tdlib.TdApi;
import com.rodgers.service.AuthorizationService;
import com.rodgers.tgclient.TgClientService;
import com.rodgers.tgclient.UpdateResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class AuthorizationHandlerImpl implements AuthorizationService {
    private static Logger logger= LoggerFactory.getLogger(AuthorizationHandlerImpl.class);
    private static TdApi.AuthorizationState authorizationState = null;
    @Autowired
    Environment environment;
    @Autowired
    TgClientService tgClientService;
    @Autowired
    UpdateResultService updateResultService;
    private static volatile boolean haveAuthorization = false;
    private static final Lock authorizationLock = new ReentrantLock();
    private static final Condition gotAuthorization = authorizationLock.newCondition();
    public void updateAuthorizationState(TdApi.AuthorizationState authorizationState){
        if (authorizationState != null) {
            AuthorizationHandlerImpl.authorizationState = authorizationState;
        }
    }

    @Override
    public <T extends TdApi.Object> CompletableFuture<T> handleAuthorizationState() {
        return null;
    }

    public <T extends TdApi.AuthorizationState> CompletableFuture<T> getAuthorizationState(){
        return CompletableFuture.completedFuture((T)AuthorizationHandlerImpl.authorizationState);
    }

    public <T extends TdApi.Object,U extends TdApi.Function> CompletableFuture<T> handleAuthorizationState(U authorizationQuery) {
        tgClientService.sentAuthorizationState(authorizationQuery);
        CompletableFuture ret=CompletableFuture.completedFuture(AuthorizationHandlerImpl.authorizationState);
        switch (AuthorizationHandlerImpl.authorizationState.getConstructor()) {
            case TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR:
                TdApi.SetTdlibParameters request = new TdApi.SetTdlibParameters();
                request.databaseDirectory = "tdlib";
                request.useMessageDatabase = true;
                request.useSecretChats = true;
                request.apiId = Integer.parseInt(environment.getProperty("com.rodgers.tdlib.apiId","94575"));
                request.apiHash =environment.getProperty( "com.rodgers.tdlib.apiHash","a3406de8d171bb422bb6ddf3bbd800e2");
                request.systemLanguageCode = environment.getProperty( "com.rodgers.tdlib.systemLanguageCode","en");
                request.deviceModel = "Desktop";
                request.applicationVersion = "1.0";

                ret=CompletableFuture.completedFuture(request);
                break;
            case TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR: {
                String phoneNumber = promptString("Please enter phone number: ");
                getTgClient().send(new TdApi.SetAuthenticationPhoneNumber(phoneNumber, null), new AuthorizationRequestHandler());
                break;
            }
            case TdApi.AuthorizationStateWaitOtherDeviceConfirmation.CONSTRUCTOR: {
                String link = ((TdApi.AuthorizationStateWaitOtherDeviceConfirmation) AuthorizationMessenger.authorizationState).link;
                System.out.println("Please confirm this login link on another device: " + link);
                break;
            }
            case TdApi.AuthorizationStateWaitEmailAddress.CONSTRUCTOR: {
                String emailAddress = promptString("Please enter email address: ");
                getTgClient().send(new TdApi.SetAuthenticationEmailAddress(emailAddress), new AuthorizationRequestHandler());
                break;
            }
            case TdApi.AuthorizationStateWaitEmailCode.CONSTRUCTOR: {
                String code = promptString("Please enter email authentication code: ");
                getTgClient().send(new TdApi.CheckAuthenticationEmailCode(new TdApi.EmailAddressAuthenticationCode(code)), new AuthorizationRequestHandler());
                break;
            }
            case TdApi.AuthorizationStateWaitCode.CONSTRUCTOR: {
                String code = promptString("Please enter authentication code: ");
                getTgClient().send(new TdApi.CheckAuthenticationCode(code), new AuthorizationRequestHandler());
                break;
            }
            case TdApi.AuthorizationStateWaitRegistration.CONSTRUCTOR: {
                String firstName = promptString("Please enter your first name: ");
                String lastName = promptString("Please enter your last name: ");
                getTgClient().send(new TdApi.RegisterUser(firstName, lastName, false), new AuthorizationRequestHandler());
                break;
            }
            case TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR: {
                String password = promptString("Please enter password: ");
                getTgClient().send(new TdApi.CheckAuthenticationPassword(password), new AuthorizationRequestHandler());
                break;
            }
            case TdApi.AuthorizationStateReady.CONSTRUCTOR:
                haveAuthorization = true;
                authorizationLock.lock();
                try {
                    gotAuthorization.signal();
                } finally {
                    authorizationLock.unlock();
                }
                break;
            case TdApi.AuthorizationStateLoggingOut.CONSTRUCTOR:
                haveAuthorization = false;
                print("Logging out");
                break;
            case TdApi.AuthorizationStateClosing.CONSTRUCTOR:
                haveAuthorization = false;
                print("Closing");
                break;
            case TdApi.AuthorizationStateClosed.CONSTRUCTOR:
                print("Closed");
                break;
            default:
                logger.error("Unsupported authorization state:{}" ,AuthorizationMessenger.authorizationState);
        }
        return ret;
    }

    private String promptString(String strPrompt) {
        //todo:
        return "";
        //return consoleReader.readLineConsole("tg:>",strPrompt);
    }
    private void print(String s){
        //todo:
        //consoleReader.print(s);
    }

    private static Client getTgClient(){
        return TGServer.getTgClient();
    }

    @Override
    public void onResult(TdApi.Object object) {

    }
}
