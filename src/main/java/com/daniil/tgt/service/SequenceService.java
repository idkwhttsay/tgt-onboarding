package com.daniil.tgt.service;

import com.daniil.tgt.dto.SequenceDto;
import com.daniil.tgt.store.InMemorySequenceStore;
import com.daniil.tgt.websocket.WebSocketPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class SequenceService {
    private final AtomicLong seqNumberCounter;
    private final InMemorySequenceStore db;
    private final WebSocketPublisher publisher;

    public SequenceService(InMemorySequenceStore db, WebSocketPublisher publisher) {
        this.seqNumberCounter = new AtomicLong(0);
        this.db = db;
        this.publisher = publisher;
    }

    public SequenceDto createNext() {
        long seqNum = getNextSeqNumber();
        String randomStr = generateUniqueRandomStr();
        SequenceDto dto = new SequenceDto(seqNum, randomStr, java.time.Instant.now());
        db.save(dto);

        // publish to websocket topic
        publisher.publish(dto);
        return dto;
    }

    public Optional<SequenceDto> getBySeqNum(long seqNum) {
        return db.getBySeqNum(seqNum);
    }

    private long getNextSeqNumber() {
        return seqNumberCounter.getAndIncrement();
    }

    private String generateUniqueRandomStr() {
        String randomStr;
        do {
            randomStr = java.util.UUID.randomUUID().toString();
        } while (db.existsRandomString(randomStr));
        return randomStr;
    }
}
