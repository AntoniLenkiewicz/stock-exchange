package com.antoni.exchange.writeAheadLogger;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
public class WriteAheadLoggerImpl extends WriteAheadLogger {

    @Value("${app.log.path}")
    private Path logPath;

    @PostConstruct
    private void run (){
        try {
            this.openLog(logPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    private void onExit(){
        try {
            this.closeLog();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
