package com.daniil.tgt.store;

import com.daniil.tgt.dto.SequenceDto;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

public class InMemorySequenceStore {
    // thread-safe in-memory storage
    private final ConcurrentMap<Long, SequenceDto> db = new ConcurrentHashMap<>();
    private final Set<String> randomStrings = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public void save(SequenceDto dto) {
        db.put(dto.getSeqNum(), dto);
        randomStrings.add(dto.getRandomStr());
    }

    public Optional<SequenceDto> getBySeqNum(long seqNum) {
        return Optional.ofNullable(db.get(seqNum));
    }

    public boolean existsRandomString(String s) {
        return randomStrings.contains(s);
    }
}
