package com.daniil.tgt.dto;

import java.time.Instant;

public class SequenceDto {
    private long seqNum;
    private String randomStr;
    private Instant timestamp;

    public SequenceDto(long seqNum, String randomStr, Instant timestamp) {
        this.seqNum = seqNum;
        this.randomStr = randomStr;
        this.timestamp = timestamp;
    }

    public long getSeqNum() {
        return seqNum;
    }

    public String getRandomStr() {
        return randomStr;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
