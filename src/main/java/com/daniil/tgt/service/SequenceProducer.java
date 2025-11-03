package com.daniil.tgt.service;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class SequenceProducer {
    private final SequenceService sequenceService;
    private final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();

    @Value("${sequence.producer.delay.ms:1000}")
    private long delayMs;

    public SequenceProducer(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        exec.scheduleAtFixedRate(() -> {
            try {
                sequenceService.createNext();
            } catch (Exception e) {
                System.err.printf("Failed to create next sequence: %s%n", e.getMessage());
            }
        }, 0, delayMs, TimeUnit.MILLISECONDS);
    }

    @PreDestroy
    public void stop() {
        exec.shutdownNow();
    }
}
