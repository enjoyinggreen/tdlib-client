package com.rodgers.tgclient.impl;

import com.rodgers.service.impl.CommandHandlerImpl;
import com.rodgers.tdlib.Client;
import com.rodgers.tdlib.TdApi;
import com.rodgers.tgclient.CommandResultService;
import com.rodgers.tgclient.ExceptionResultService;
import com.rodgers.tgclient.TgClientService;
import com.rodgers.tgclient.UpdateResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOError;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TgClientServiceImpl implements TgClientService {
    static private final Logger logger= LoggerFactory.getLogger(TgClientServiceImpl.class);
    private static final AtomicLong currentQueryId = new AtomicLong();
    Client client=null;
    @Autowired
    Environment environment;
    @Autowired
    UpdateResultService updateResultService;
    @Override
    public void start() {
        if(client==null){
            setUpTgClient(Integer.parseInt(Optional.ofNullable(environment.getProperty("rodgers.tdlib.loglevel")).orElse("0")));
            client=Client.create(updateResultService,null,null);
        }
    }


    @Override
    public <T extends TdApi.Object> CompletableFuture<T> close() {
         return sent(new TdApi.Close());
    }
    @Override
    public <T extends TdApi.Object, F extends TdApi.Object> CompletableFuture<T> sent(TdApi.Function<F> query) {
        CommandResultService commandResultService=CommandHandlerImpl.builder().command(query).build();
        client.send(query, commandResultService );
        return commandResultService.getCompletableFuture();
    }

    @Override
    public <T extends TdApi.Object, F extends TdApi.Object> CompletableFuture<T> sent(TdApi.Function<F> query, CommandResultService commandResultService) {
        client.send(query,commandResultService);
        return commandResultService.getCompletableFuture();
    }
    private void setUpTgClient(int tgServerLogLevel){
        Client.setLogMessageHandler(0, new LogMessageHandler());

        // disable TDLib log and redirect fatal errors and plain log messages to a file
        try {
            Client.execute(new TdApi.SetLogVerbosityLevel(0));
            Client.execute(new TdApi.SetLogStream(new TdApi.LogStreamFile("tdlib.log", 1 << 27, false)));
        } catch (Client.ExecutionException error) {
            throw new IOError(new IOException("Write access to the current directory is required"));
        }

    }
    private static class LogMessageHandler implements Client.LogMessageHandler {
        @Override
        public void onLogMessage(int verbosityLevel, String message) {
            logger.error(message);
            if (verbosityLevel == 0) {
                onFatalError(message);
                return;
            }

        }
    }
    //todo: rewrite with spring executor
    private static void onFatalError(String errorMessage) {
        final class ThrowError implements Runnable {
            private final String errorMessage;
            private final AtomicLong errorThrowTime;

            private ThrowError(String errorMessage, AtomicLong errorThrowTime) {
                this.errorMessage = errorMessage;
                this.errorThrowTime = errorThrowTime;
            }

            @Override
            public void run() {
                if (isDatabaseBrokenError(errorMessage) || isDiskFullError(errorMessage) || isDiskError(errorMessage)) {
                    processExternalError();
                    return;
                }

                errorThrowTime.set(System.currentTimeMillis());
                throw new ClientError("TDLib fatal error: " + errorMessage);
            }

            private void processExternalError() {
                errorThrowTime.set(System.currentTimeMillis());
                throw new ExternalClientError("Fatal error: " + errorMessage);
            }

            final class ClientError extends Error {
                private ClientError(String message) {
                    super(message);
                }
            }

            final class ExternalClientError extends Error {
                public ExternalClientError(String message) {
                    super(message);
                }
            }

            private boolean isDatabaseBrokenError(String message) {
                return message.contains("Wrong key or database is corrupted") ||
                        message.contains("SQL logic error or missing database") ||
                        message.contains("database disk image is malformed") ||
                        message.contains("file is encrypted or is not a database") ||
                        message.contains("unsupported file format") ||
                        message.contains("Database was corrupted and deleted during execution and can't be recreated");
            }

            private boolean isDiskFullError(String message) {
                return message.contains("PosixError : No space left on device") ||
                        message.contains("database or disk is full");
            }

            private boolean isDiskError(String message) {
                return message.contains("I/O error") || message.contains("Structure needs cleaning");
            }
        }

        final AtomicLong errorThrowTime = new AtomicLong(Long.MAX_VALUE);
        new Thread(new ThrowError(errorMessage, errorThrowTime), "TDLib fatal error thread").start();

        // wait at least 10 seconds after the error is thrown
        while (errorThrowTime.get() >= System.currentTimeMillis() - 10000) {
            try {
                Thread.sleep(1000 /* milliseconds */);
            } catch (InterruptedException ignore) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
