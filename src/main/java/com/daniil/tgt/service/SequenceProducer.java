package com.daniil.tgt.service;

import jakarta.annotation.PreDestroy;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class SequenceProducer {
    private static final Logger log = LoggerFactory.getLogger(SequenceProducer.class);
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
                log.error("SequenceProducer failed to create next sequence: {}", e.getMessage());
            }
        }, 0, delayMs, TimeUnit.MILLISECONDS);
    }

    @PreDestroy
    public void stop() {
        exec.shutdownNow();
    }
}
