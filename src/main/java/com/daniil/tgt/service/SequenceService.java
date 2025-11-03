package com.daniil.tgt.service;

import com.daniil.tgt.dto.SequenceDto;
import com.daniil.tgt.store.SequenceStore;
import com.daniil.tgt.websocket.WebSocketPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class SequenceService {
    private static final Logger log = LoggerFactory.getLogger(SequenceService.class);
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
        log.info("Created sequence with seqNum={} and published via WebSocket", seqNum);
        return dto;
    }

    public Optional<SequenceDto> getBySeqNum(long seqNum) {
        log.debug("Fetching sequence by seqNum={}", seqNum);
        return db.getBySeqNum(seqNum);
    }

    public Optional<List<SequenceDto>> getRangeSeq(long fromSeq, long toSeq) {
        log.debug("Fetching sequence range fromSeq={} toSeq={}", fromSeq, toSeq);
        return Optional.ofNullable(db.getRangeSeq(fromSeq, toSeq));
    }

    public Optional<List<SequenceDto>> getLatestSeq(int limit) {
        log.debug("Fetching latest {} sequences", limit);
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
