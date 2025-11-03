package com.daniil.tgt.service;

import com.daniil.tgt.dto.SequenceDto;
import com.daniil.tgt.store.SequenceStore;
import com.daniil.tgt.websocket.WebSocketPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class SequenceService {
    private final AtomicLong seqNumberCounter;
    private final SequenceStore db;
    private final WebSocketPublisher publisher;

    public SequenceService(SequenceStore db, WebSocketPublisher publisher) {
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

    public Optional<List<SequenceDto>> getRangeSeq(long fromSeq, long toSeq) {
        return Optional.ofNullable(db.getRangeSeq(fromSeq, toSeq));
    }

    public Optional<List<SequenceDto>> getLatestSeq(int limit) {
        return Optional.ofNullable(db.getLatestSeq(limit));
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
